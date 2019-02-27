/*
 * Copyright (c) 2016 Christophe Lafolet
 * Copyright (c) 2014 Tom Greenwood <tgreenwood@cafex.com>
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

import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.glib.GQuark;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GDestroyNotify;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.Pointer;

/**
 * GstMiniObject functions
 */
public interface GstMiniObjectAPI extends com.sun.jna.Library {
    GstMiniObjectAPI GSTMINIOBJECT_API = GstNative.load(GstMiniObjectAPI.class);

    void gst_mini_object_ref(GstMiniObjectPtr ptr);
    void gst_mini_object_unref(GstMiniObjectPtr ptr);
    void gst_mini_object_unref(Pointer ptr);

    @CallerOwnsReturn MiniObject gst_mini_object_make_writable(@Invalidate MiniObject mini_object);
    @CallerOwnsReturn MiniObject gst_mini_object_copy(MiniObject mini_object);
    boolean gst_mini_object_is_writable(MiniObject mini_object);

    void gst_mini_object_set_qdata(MiniObject mini_object, GQuark quark, Object data, GDestroyNotify destroyCallback);
    Pointer gst_mini_object_get_qdata(MiniObject mini_object, GQuark quark);
    Pointer gst_mini_object_steal_qdata(MiniObject mini_object, GQuark quark);

    public static final class MiniObjectStruct extends com.sun.jna.Structure {
        public volatile GType type; 
        public volatile int refcount;
        public volatile int lockstate;
        public volatile int flags;
        public volatile Pointer copyFn;
        public volatile Pointer disposeFn;
        public volatile Pointer freeFn;
        public volatile int n_qdata; // Private parts - there for alignment
        public volatile Pointer qdata;

        /** Creates a new instance of GstMiniObjectStructure */
        public MiniObjectStruct() {}
        public MiniObjectStruct(Pointer ptr) {
            super(ptr);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "type", "refcount", "lockstate", "flags",
                "copyFn", "disposeFn", "freeFn", "n_qdata", "qdata"
            });
        }
    }
}
