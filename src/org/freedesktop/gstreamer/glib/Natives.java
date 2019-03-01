/*
 * Copyright (c) 2019 Neil C Smith
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License version 3 only, as published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3 along with
 * this work. If not, see <http://www.gnu.org/licenses/>.
 */
package org.freedesktop.gstreamer.glib;

import com.sun.jna.Pointer;
import java.util.ServiceLoader;
import java.util.function.Function;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.lowlevel.GObjectPtr;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectPtr;

/**
 * <b>Here be Dragons!</b>
 * <p>
 * This class provides utility functions for working with the underlying native
 * bindings, creating {@link NativeObject} from pointers and extracting native
 * pointers from NativeObjects. It should normally only be necessary to make use
 * of these methods if extending the bindings externally or interacting with
 * other native code.
 */
public final class Natives {

    private Natives() {
    }

    /**
     * Create a {@link NativeObject.Initializer} for the provided Pointer.
     * <p>
     * This initializer will own the handle.
     * <p>
     * This initializer will not request a ref increase (only relevant if used
     * with instance of {@link RefCountedObject})
     *
     * @param ptr native pointer
     * @return initializer
     */
    public static final NativeObject.Initializer initializer(Pointer ptr) {
        NativeObject.Initializer initializer = initializer(ptr, false, true);
        return initializer;
    }

    /**
     * Create a {@link NativeObject.Initializer} for the provided Pointer.
     * <p>
     * This initializer will own the handle.
     *
     * @param ptr native pointer
     * @param needRef whether to request a ref increase (only relevant if used
     * with instance of {@link RefCountedObject})
     * @return initializer
     */
    public static final NativeObject.Initializer initializer(Pointer ptr, boolean needRef) {
        NativeObject.Initializer initializer = initializer(ptr, needRef, true);
        return initializer;
    }

    /**
     * Create a {@link NativeObject.Initializer} for the provided Pointer.
     *
     * @param ptr native pointer
     * @param needRef whether to request a ref increase (only relevant if used
     * with instance of {@link RefCountedObject})
     * @param ownsHandle whether the NativeObject will own the handle, and
     * should dispose of the native resource when GC'd or explicitly disposed.
     * @return initializer
     */
    public static final NativeObject.Initializer initializer(Pointer ptr, boolean needRef, boolean ownsHandle) {
        if (ptr == null) {
            throw new IllegalArgumentException("Invalid native pointer");
        }
        return new NativeObject.Initializer(new GPointer(ptr), needRef, ownsHandle);
    }


    /**
     * Get a {@link NativeObject} instance of the requested type for the
     * provided Pointer. Will return a cached instance if one already exists.
     *
     * @param <T> NativeObject type to return
     * @param ptr native Pointer
     * @param cls Class of type T
     * @param needRef whether to request a ref increase (only relevant if T is
     * subclass of {@link RefCountedObject})
     * @param ownsHandle whether the NativeObject will own the handle, and
     * should dispose of the native resource when GC'd or explicitly disposed.
     * @return native object of type T
     */
    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, boolean needRef, boolean ownsHandle) {
        return objectFor(ptr, cls, needRef ? 1 : 0, ownsHandle);
    }

    /**
     * Get a {@link NativeObject} instance of the requested type for the
     * provided Pointer, for use with native functions returning
     * {@code Transfer Full} or {@code Transfer Floating} results.
     * <p>
     * This method will return a cached instance if one already exists. If the
     * cached instance is a {@link RefCountedObject} this method will release a
     * reference.
     *
     * @param <T> NativeObject type to return
     * @param ptr native Pointer
     * @param cls Class of type T
     * @return native object of type T
     */
    public static <T extends NativeObject> T callerOwnsReturn(Pointer ptr, Class<T> cls) {
        return objectFor(ptr, cls, -1, true);
    }

    private static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, int refAdjust, boolean ownsHandle) {
        final GPointer gptr = GObject.class.isAssignableFrom(cls) ? new GObjectPtr(ptr)
                : MiniObject.class.isAssignableFrom(cls) ? new GstMiniObjectPtr(ptr)
                : new GPointer(ptr);
        return NativeObject.objectFor(gptr, cls, refAdjust, ownsHandle);
    }

    /**
     * Get the underlying raw native Pointer for a {@link NativeObject}.
     *
     * @param obj native object
     * @return native pointer
     * @throws IllegalStateException if the native reference has been
     * invalidated or disposed
     */
    public static Pointer getRawPointer(NativeObject obj) {
        return obj.getRawPointer();
    }

    /**
     * Get the underlying native typed GPointer for a {@link NativeObject}.
     *
     * @param obj native object
     * @return native typed pointer
     * @throws IllegalStateException if the native reference has been
     * invalidated or disposed
     */
    public static GPointer getPointer(NativeObject obj) {
        return obj.getPointer();
    }

    /**
     * Increase the reference count of a {@link RefCountedObject}
     *
     * @param <T> type of object
     * @param obj object to increase reference count on
     * @return object
     */
    public static <T extends RefCountedObject> T ref(T obj) {
        ((RefCountedObject.Handle) obj.handle).ref();
        return obj;
    }

    /**
     * Decrease the reference count of a {@link RefCountedObject}
     *
     * @param <T> type of object
     * @param obj object to decrease reference count on
     * @return object
     */
    public static <T extends RefCountedObject> T unref(T obj) {
        ((RefCountedObject.Handle) obj.handle).unref();
        return obj;
    }

    /**
     * Create a {@link NativeObject.TypeRegistration} for linking
     * {@link NativeObject} subclasses to GTypes.
     * <p>
     * Be careful to respect the link between Java type hierarchy of registered
     * classes and the underlying GType hierarchy. eg. if the GType is a
     * subclass of GObject then the Java type must extend from {@link GObject}
     * <p>
     * The factory function should normally be a constructor reference.
     * <p>
     * Registrations can be provided externally using
     * {@link NativeObject.TypeProvider} instances registered for use with
     * {@link ServiceLoader}
     *
     * @param <T> Java type
     * @param javaType Java type class
     * @param gTypeName name of the GType
     * @param factory a factory function to return an instance of T given a
     * {@link NativeObject.Initializer}. Normally a constructor reference -
     * {@code T::new}
     * @return registration
     */
    public static <T extends NativeObject> NativeObject.TypeRegistration<T>
            registration(Class<T> javaType, String gTypeName, Function<NativeObject.Initializer, ? extends T> factory) {
        return new NativeObject.TypeRegistration<>(javaType, gTypeName, factory);
    }

}
