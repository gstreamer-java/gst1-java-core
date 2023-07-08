/*
 * Copyright (c) 2023 Jan Weber
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

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.freedesktop.gstreamer.lowlevel.GPointer;

import static org.freedesktop.gstreamer.lowlevel.GlibAPI.GLIB_API;

/**
 * Wrapper to the GBytes data structure,
 * see <a href="https://docs.gtk.org/glib/struct.Bytes.html">https://docs.gtk.org/glib/struct.Bytes.html</a>
 */
public class GBytes extends RefCountedObject {

    public static final String GTYPE_NAME = "GBytes";

    GBytes(Initializer init) {
        super(new GBytes.Handle(init.ptr, init.ownsHandle), init.needRef);
    }

    GBytes(Handle handle) {
        super(handle);
    }

    public int getSize() {
        return GLIB_API.g_bytes_get_size(getRawPointer());
    }

    public byte[] getBytes() {
        IntByReference size = new IntByReference();
        Pointer bytes = GLIB_API.g_bytes_get_data(getRawPointer(), size);
        return bytes != null ? bytes.getByteArray(0, size.getValue()) : new byte[0];
    }

    public static GBytes createInstance(byte[] bytes) {
        Pointer source = new Memory(bytes.length);
        source.write(0, bytes, 0, bytes.length);
        Pointer ptr = GLIB_API.g_bytes_new(source, bytes.length);
        return new GBytes(new Handle(new GPointer(ptr), true));
    }

    private static final class Handle extends RefCountedObject.Handle {

        Handle(GPointer ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GLIB_API.g_bytes_unref(ptr.getPointer());
        }

        @Override
        protected void ref() {
            GLIB_API.g_bytes_ref(getPointer().getPointer());
        }

        @Override
        protected void unref() {
            GLIB_API.g_bytes_unref(getPointer().getPointer());
        }

    }

}
