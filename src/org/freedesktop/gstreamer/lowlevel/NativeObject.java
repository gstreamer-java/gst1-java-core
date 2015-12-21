/*
 * Copyright (c) 2015 Christophe Lafolet
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

package org.freedesktop.gstreamer.lowlevel;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freedesktop.gstreamer.GObject;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.lowlevel.annotations.HasSubtype;

import com.sun.jna.Pointer;

/**
 *
 */
public abstract class NativeObject extends org.freedesktop.gstreamer.lowlevel.Handle {
    private static final Logger logger = Logger.getLogger(NativeObject.class.getName());
    private static final Level LIFECYCLE = Level.FINE;

    // Use this to propagate low level pointer arguments up the constructor chain
    protected static class Initializer {
        public final Pointer ptr;
        public final boolean needRef, ownsHandle;
        public Initializer() {
            this.ptr = null;
            this.needRef = false;
            this.ownsHandle = false;
        }
        public Initializer(Pointer ptr, boolean needRef, boolean ownsHandle) {
            this.ptr = ptr;
            this.needRef = needRef;
            this.ownsHandle = ownsHandle;
        }
    }
    protected static final Initializer defaultInit = new Initializer();

    /*
     * The default for new objects is to not need a refcount increase, and that
     * they own the native object.  Special cases can use the other constructor.
     */
    protected static Initializer initializer(Pointer ptr) {
        Initializer initializer = NativeObject.initializer(ptr, false, true);
        return initializer;
    }
    protected static Initializer initializer(Pointer ptr, boolean needRef, boolean ownsHandle) {
        if (ptr == null) {
            throw new IllegalArgumentException("Invalid native pointer");
        }
        return new Initializer(ptr, needRef, ownsHandle);
    }
    /** Creates a new instance of NativeObject */
    protected NativeObject(final Initializer init) {
    	if (NativeObject.logger.isLoggable(Level.FINER)) {
            NativeObject.logger.entering("NativeObject", "<init>", new Object[] { init });
    	}

        if (init == null) {
            throw new IllegalArgumentException("Initializer cannot be null");
        }
    	if (NativeObject.logger.isLoggable(NativeObject.LIFECYCLE)) {
            NativeObject.logger.log(NativeObject.LIFECYCLE, "Creating " + this.getClass().getSimpleName() + " (" + init.ptr + ")");
    	}

        this.nativeRef = new NativeRef(this);
        this.handle = init.ptr;
        this.ownsHandle.set(init.ownsHandle);

        //
        // Only store this object in the map if we can tell when it has been disposed
        // (i.e. must be at least a GObject - MiniObject and other NativeObject subclasses
        // don't signal destruction, so it is impossible to know if the instance
        // is stale or not
        //
        if (GObject.class.isAssignableFrom(this.getClass())) {
            NativeObject.getInstanceMap().put(init.ptr, this.nativeRef);
        }

    }

    abstract protected void disposeNativeHandle(Pointer ptr);

    public void dispose() {

    	if (NativeObject.logger.isLoggable(NativeObject.LIFECYCLE)) {
            NativeObject.logger.log(NativeObject.LIFECYCLE, "Disposing object " + this.getClass().getName() + " = " + this.handle);
    	}

        if (!this.disposed.getAndSet(true)) {
            NativeObject.getInstanceMap().remove(this.handle, this.nativeRef);
            if (this.ownsHandle.get()) {
                this.disposeNativeHandle(this.handle);
            }
            this.valid.set(false);
        }
    }

    @Override
    protected void invalidate() {
    	if (NativeObject.logger.isLoggable(NativeObject.LIFECYCLE)) {
            NativeObject.logger.log(NativeObject.LIFECYCLE, "Invalidating object " + this + " = " + this.handle());
    	}
        NativeObject.getInstanceMap().remove(this.handle(), this.nativeRef);
        this.disposed.set(true);
        this.ownsHandle.set(false);
        this.valid.set(false);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
        	if (NativeObject.logger.isLoggable(NativeObject.LIFECYCLE)) {
                NativeObject.logger.log(NativeObject.LIFECYCLE, "Finalizing " + this.getClass().getSimpleName() + " (" + this.handle + ")");
        	}
            this.dispose();
        } finally {
            super.finalize();
        }
    }
    @Override
    protected Object nativeValue() {
        return this.handle();
    }
    protected Pointer handle() {
        if (!this.valid.get() || this.disposed.get()) {
            throw new IllegalStateException("Native object has been disposed");
        }
        return this.handle;
    }
    public Pointer getNativeAddress() {
        return this.handle;
    }
    protected boolean isDisposed() {
        return this.disposed.get();
    }
    protected static NativeObject instanceFor(Pointer ptr) {
        WeakReference<NativeObject> ref = NativeObject.getInstanceMap().get(ptr);

        //
        // If the reference was there, but the object it pointed to had been collected, remove it from the map
        //
        if (ref != null && ref.get() == null) {
            NativeObject.getInstanceMap().remove(ptr);
        }
        return ref != null ? ref.get() : null;
    }
    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls) {
    	return NativeObject.objectFor(ptr, cls, true);
    }
    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, boolean needRef) {
        return NativeObject.objectFor(ptr, cls, needRef, true);
    }
    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, boolean needRef, boolean ownsHandle) {
        return NativeObject.objectFor(ptr, cls, needRef ? 1 : 0, ownsHandle);
    }

    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, int refAdjust, boolean ownsHandle) {
    	if (NativeObject.logger.isLoggable(Level.FINER)) {
            NativeObject.logger.entering("NativeObject", "instanceFor", new Object[] { ptr, refAdjust, ownsHandle });
    	}

        // Ignore null pointers
        if (ptr == null) {
            return null;
        }
        NativeObject obj = GObject.class.isAssignableFrom(cls) ? NativeObject.instanceFor(ptr) : null;
        if (obj != null && cls.isInstance(obj)) {
            if (refAdjust < 0) {
                ((RefCountedObject) obj).unref(); // Lose the extra ref added by gstreamer
            }
            return cls.cast(obj);
        }

        //
        // If it is a GObject or MiniObject, read the GType field to find
        // the most exact class match
        //
        if (GObject.class.isAssignableFrom(cls) || MiniObject.class.isAssignableFrom(cls)) {
            cls = NativeObject.classFor(ptr, cls);
        }
        try {
            Constructor<T> constructor = cls.getDeclaredConstructor(Initializer.class);
            T retVal = constructor.newInstance(NativeObject.initializer(ptr, refAdjust > 0, ownsHandle));
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
    protected static <T extends NativeObject> Class<T> classFor(Pointer ptr, Class<T> defaultClass) {
        Class<? extends NativeObject> cls = GstTypes.classFor(ptr, defaultClass);

    	if (cls.isAnnotationPresent(HasSubtype.class)) {
    		cls = (Class<T>)SubtypeMapper.subtypeFor(cls, ptr);
    	}
        return cls != null && defaultClass.isAssignableFrom(cls) ? (Class<T>) cls : defaultClass;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NativeObject && ((NativeObject) o).handle.equals(this.handle);
    }

    @Override
    public int hashCode() {
        return this.handle.hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + this.handle() + ")";
    }

    //
    // No longer want to garbage collect this object
    //
    public void disown() {
    	if (NativeObject.logger.isLoggable(NativeObject.LIFECYCLE)) {
            NativeObject.logger.log(NativeObject.LIFECYCLE, "Disowning " + this.handle());
    	}
        this.ownsHandle.set(false);
    }

    static {
        //
        // Add a shutdown task to cleanup any dangling object references, so
        // Gst.deinit() can shutdown cleanly.  Unreffing objects after gst_deinit()
        // has been called could be asking for trouble.
        //
        Gst.addStaticShutdownTask(new Runnable() {

            @Override
			public void run() {
                System.gc();
                int gcCount = 20;
                // Give the GC a chance to cleanup nicely
                while (!NativeObject.getInstanceMap().isEmpty() && gcCount-- > 0) {
                    try {
                        Thread.sleep(10);
                        System.gc();
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
                for (Object o : NativeObject.getInstanceMap().values().toArray()) {
                    NativeObject obj = ((NativeRef) o).get();
                    if (obj != null && !obj.disposed.get()) {
                        obj.dispose();
                    }
                }
            }
        });
    }
    private static final ConcurrentMap<Pointer, NativeRef> getInstanceMap() {
        return StaticData.instanceMap;
    }
    static class NativeRef extends WeakReference<NativeObject> {
        public NativeRef(NativeObject obj) {
            super(obj);
        }
    }
    private final AtomicBoolean disposed = new AtomicBoolean(false);
    private final AtomicBoolean valid = new AtomicBoolean(true);
    private final Pointer handle;
    protected final AtomicBoolean ownsHandle = new AtomicBoolean(false);
    private final NativeRef nativeRef;
    private static final class StaticData {
        private static final ConcurrentMap<Pointer, NativeRef> instanceMap = new ConcurrentHashMap<Pointer, NativeRef>();
        static {
            //
            // Add a shutdown task to cleanup any dangling object references, so
            // Gst.deinit() can shutdown cleanly.  Unreffing objects after gst_deinit()
            // has been called could be asking for trouble.
            //
            Gst.addStaticShutdownTask(new Runnable() {

                @Override
				public void run() {
                    System.gc();
                    int gcCount = 20;
                    // Give the GC a chance to cleanup nicely
                    while (!NativeObject.getInstanceMap().isEmpty() && gcCount-- > 0) {
                        try {
                            Thread.sleep(10);
                            System.gc();
                        } catch (InterruptedException ex) {
                            break;
                        }
                    }
                    for (Object o : NativeObject.getInstanceMap().values().toArray()) {
                        NativeObject obj = ((NativeRef) o).get();
                        if (obj != null && !obj.disposed.get()) {
                            obj.dispose();
                        }
                    }
                }
            });
        }
    }
}
