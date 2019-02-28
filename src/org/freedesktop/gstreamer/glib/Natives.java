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
import java.util.function.Function;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.lowlevel.GObjectPtr;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectPtr;

/**
 *
 */
public final class Natives {

    private Natives() {
    }

    /*
     * The default for new objects is to not need a refcount increase, and that
     * they own the native object.  Special cases can use the other constructor.
     */
    public static final NativeObject.Initializer initializer(Pointer ptr) {
        NativeObject.Initializer initializer = initializer(ptr, false, true);
        return initializer;
    }

    public static final NativeObject.Initializer initializer(Pointer ptr, boolean needRef) {
        NativeObject.Initializer initializer = initializer(ptr, needRef, true);
        return initializer;
    }

    public static final NativeObject.Initializer initializer(Pointer ptr, boolean needRef, boolean ownsHandle) {
        if (ptr == null) {
            throw new IllegalArgumentException("Invalid native pointer");
        }
        return new NativeObject.Initializer(new GPointer(ptr), needRef, ownsHandle);
    }

    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls) {
        return objectFor(ptr, cls, true);
    }

    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, boolean needRef) {
        return objectFor(ptr, cls, needRef, true);
    }

    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, boolean needRef, boolean ownsHandle) {
        return objectFor(ptr, cls, needRef ? 1 : 0, ownsHandle);
    }
    
    public static <T extends NativeObject> T callerOwnsReturn(Pointer ptr, Class<T> cls) {
        return objectFor(ptr, cls, -1, true);
    }

    private static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, int refAdjust, boolean ownsHandle) {
        final GPointer gptr = GObject.class.isAssignableFrom(cls) ? new GObjectPtr(ptr)
                : MiniObject.class.isAssignableFrom(cls) ? new GstMiniObjectPtr(ptr)
                : new GPointer(ptr);
        return NativeObject.objectFor(gptr, cls, refAdjust, ownsHandle);
    }
    
    public static Pointer getRawPointer(NativeObject obj) {
        return obj.getRawPointer();
    }

    public static GPointer getPointer(NativeObject obj) {
        return obj.getPointer();
    }

    public static <T extends RefCountedObject> T ref(T obj) {
        ((RefCountedObject.Handle) obj.handle).ref();
        return obj;
    }

    public static <T extends RefCountedObject> T unref(T obj) {
        ((RefCountedObject.Handle) obj.handle).unref();
        return obj;
    }

    public static <T extends NativeObject> NativeObject.TypeRegistration<T>
            registration(Class<T> javaType, String gTypeName, Function<NativeObject.Initializer, ? extends T> factory) {
        return new NativeObject.TypeRegistration<>(javaType, gTypeName, factory);
    }

}
