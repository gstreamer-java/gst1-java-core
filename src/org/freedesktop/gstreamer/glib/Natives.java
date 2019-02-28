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
import org.freedesktop.gstreamer.lowlevel.GPointer;

/**
 *
 */
public final class Natives {
    
    private Natives() {}

    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls) {
        return objectFor(ptr, cls, true);
    }

    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, boolean needRef) {
        return objectFor(ptr, cls, needRef, true);
    }

    public static <T extends NativeObject> T objectFor(Pointer ptr, Class<T> cls, boolean needRef, boolean ownsHandle) {
        return NativeObject.objectFor(ptr, cls, needRef ? 1 : 0, ownsHandle);
    }
    
    public static <T extends NativeObject> T callerOwnsReturn(Pointer ptr, Class<T> cls) {
        return NativeObject.objectFor(ptr, cls, -1, true);
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
    
}
