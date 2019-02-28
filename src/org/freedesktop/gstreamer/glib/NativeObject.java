/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2007 Wayne Meissner
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freedesktop.gstreamer.glib;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.lowlevel.annotations.HasSubtype;

import com.sun.jna.Pointer;
import java.lang.ref.ReferenceQueue;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GstTypes;
import org.freedesktop.gstreamer.lowlevel.SubtypeMapper;

/**
 *
 */
public abstract class NativeObject {

    private static final Level LIFECYCLE = Level.FINE;
    private static final Logger LOG = Logger.getLogger(NativeObject.class.getName());
    private static final ConcurrentMap<Pointer, NativeRef> INSTANCES = new ConcurrentHashMap<>();

    final Handle handle;
    private final Pointer ptr;
    private final NativeRef nativeRef;

//    /**
//     * Creates a new instance of NativeObject
//     */
    protected NativeObject(Handle handle) {
        this.handle = Objects.requireNonNull(handle);
        //
        // Only store this object in the map if we can tell when it has been disposed 
        // (i.e. must be at least a GObject - MiniObject and other NativeObject subclasses
        // don't signal destruction, so it is impossible to know if the instance 
        // is stale or not
        //
        this.ptr = handle.ptrRef.get().getPointer();
        this.nativeRef = new NativeRef(this, handle);
//        if (GObject.class.isAssignableFrom(getClass())) {

        // need to put all nativeRef in map now so WeakReference doesn't go out of scope
        INSTANCES.put(this.ptr, nativeRef);
//        }
    }

    //
    // No longer want to garbage collect this object
    //
    public void disown() {
        LOG.log(LIFECYCLE, "Disowning " + getRawPointer());
        handle.ownsHandle.set(false);
    }

    public void dispose() {
        LOG.log(LIFECYCLE, "Disposing object " + getClass().getName() + " = " + handle);
        handle.dispose();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NativeObject && ((NativeObject) o).ptr.equals(ptr);
    }

    
    protected GPointer getPointer() {
        GPointer ptr = handle.ptrRef.get();
        if (ptr == null) {
            throw new IllegalStateException("Native object has been disposed");
        }
        return ptr;
    }
    
    protected Pointer getRawPointer() {
        GPointer ptr = handle.ptrRef.get();
        if (ptr == null) {
            throw new IllegalStateException("Native object has been disposed");
        }
        return ptr.getPointer();
    }

    @Override
    public int hashCode() {
        return ptr.hashCode();
    }

    public void invalidate() {
        LOG.log(LIFECYCLE, () -> "Invalidating object " + this + " = " + getRawPointer());
        handle.invalidate();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getRawPointer() + ")";
    }


    static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, int refAdjust, boolean ownsHandle) {

        // Ignore null pointers
        if (ptr == null) {
            return null;
        }
        NativeObject obj = GObject.class.isAssignableFrom(cls) ? NativeObject.instanceFor(ptr) : null;
        if (obj != null && cls.isInstance(obj)) {
            if (refAdjust < 0) {
                ((RefCountedObject.Handle) obj.handle).unref(); // Lose the extra ref added by gstreamer
            }
            return cls.cast(obj);
        }

        final GType gType
                = GObject.class.isAssignableFrom(cls) ? GObject.getType(ptr)
                : MiniObject.class.isAssignableFrom(cls) ? MiniObject.getType(ptr)
                : null; // shall never appears

        //
        // For a GObject, MiniObject, ..., use the GType field to find the most
        // exact class match
        //
        if (gType != null) {
            cls = NativeObject.classFor(ptr, gType, cls);
        }

        try {
            Constructor<T> constructor = cls.getDeclaredConstructor(Initializer.class);
            constructor.setAccessible(true);
            T retVal = constructor.newInstance(Natives.initializer(ptr, refAdjust > 0, ownsHandle));
            //retVal.initNativeHandle(ptr, refAdjust > 0, ownsHandle);
            return retVal;
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }

    }

    @SuppressWarnings("unchecked")
    private static <T extends NativeObject> Class<T> classFor(Pointer ptr, final GType gType, final Class<T> defaultClass) {
        Class<T> cls = (Class<T>) GstTypes.classFor(gType);
        if (cls == null) {
            cls = defaultClass;
        }

        if (cls.isAnnotationPresent(HasSubtype.class)) {
            cls = (Class<T>) SubtypeMapper.subtypeFor(cls, ptr);
        }

        return cls;
    }


    static NativeObject instanceFor(Pointer ptr) {
        WeakReference<NativeObject> ref = INSTANCES.get(ptr);

        //
        // If the reference was there, but the object it pointed to had been collected, remove it from the map
        //
        if (ref != null && ref.get() == null) {
            INSTANCES.remove(ptr);
        }
        return ref != null ? ref.get() : null;
    }

    // Use this to propagate low level pointer arguments up the constructor chain
    public static final class Initializer {

        public final GPointer ptr;
        public final boolean needRef, ownsHandle;

        Initializer(GPointer ptr, boolean needRef, boolean ownsHandle) {
            this.ptr = ptr;
            this.needRef = needRef;
            this.ownsHandle = ownsHandle;
        }

    }

    private static final class NativeRef extends WeakReference<NativeObject> {

        private static final ReferenceQueue<NativeObject> QUEUE = new ReferenceQueue<>();
        private static final ExecutorService REAPER
                = Executors.newSingleThreadExecutor((r) -> {
                    Thread t = new Thread(r, "NativeObject Reaper");
                    t.setDaemon(true);
                    return t;
                });

        static {
            REAPER.submit(() -> {
                while (true) {
                    try {
                        NativeRef ref = (NativeRef) QUEUE.remove();
                        LOG.log(LIFECYCLE, "Disposing of " + ref.type + " : " + ref.handle.ptrRef.get());
                        ref.handle.dispose();
                    } catch (Throwable t) {
                        LOG.log(Level.WARNING, "Reaper thread exception", t);
                    }
                }
            });
        }

        private final Handle handle;
        private final String type;

        private NativeRef(NativeObject obj, Handle handle) {
            super(obj, QUEUE);
            this.type = obj.getClass().getSimpleName();
            this.handle = handle;
        }

    }

    protected static abstract class Handle {

        protected final AtomicReference<GPointer> ptrRef;
        protected final AtomicBoolean ownsHandle;

        public Handle(GPointer ptr, boolean ownsHandle) {
            this.ptrRef = new AtomicReference<>(ptr);
            this.ownsHandle = new AtomicBoolean(ownsHandle);
        }

        public void disown() {
            ownsHandle.set(false);
        }

        public void invalidate() {
            GPointer ptr = ptrRef.getAndSet(null);
            ownsHandle.set(false);
            if (ptr != null) {
                INSTANCES.remove(ptr.getPointer());
            }
        }

        public void dispose() {
            GPointer ptr = ptrRef.getAndSet(null);
            if (ptr != null) {
                INSTANCES.remove(ptr.getPointer());
                if (ownsHandle.compareAndSet(true, false)) {
                    disposeNativeHandle(ptr);
                }
            }
        }

        public boolean isCacheable() {
            return false;
        }

        protected abstract void disposeNativeHandle(GPointer ptr);

        protected GPointer getPointer() {
            return ptrRef.get();
        }

        protected boolean ownsHandle() {
            return ownsHandle.get();
        }
    }
}
