/*
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007, 2008 Wayne Meissner
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

import java.util.Arrays;
import java.util.List;

import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 *
 */
public interface GstAPI extends Library {
    GstAPI GST_API = GstNative.load(GstAPI.class);
    int GST_PADDING = 4;
    int GST_PADDING_LARGE = 20;

    GType gst_type_find_get_type();
    @CallerOwnsReturn String gst_version_string();
    void gst_version(long[] major, long[] minor, long[] micro, long[] nano);
    boolean gst_init(IntByReference argc, PointerByReference argv);
    boolean gst_init_check(IntByReference argc, PointerByReference argv, Pointer[] err);
    boolean gst_init_check(IntByReference argc, PointerByReference argv, GErrorStruct[] err);
    boolean gst_segtrap_is_enabled();
    void gst_segtrap_set_enabled(boolean enabled);
    void gst_deinit();

    public static final class GErrorStruct extends com.sun.jna.Structure {
        public volatile int domain; /* GQuark */
        public volatile int code;
        public volatile String message;

        /** Creates a new instance of GError */
        public GErrorStruct() { this.clear(); }
        public GErrorStruct(Pointer ptr) {
            this.useMemory(ptr);
        }
        public int getCode() {
            return (Integer) this.readField("code");
        }
        public String getMessage() {
            return (String) this.readField("message");
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "domain", "code", "message"
            });
        }
    }
    // Do nothing, but provide a base Callback class that gets automatic type conversion
    public static interface GstCallback extends com.sun.jna.Callback {
        static final com.sun.jna.TypeMapper TYPE_MAPPER = new GTypeMapper();
    }
}
