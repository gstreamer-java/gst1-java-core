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

import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.FreeReturnValue;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.List;

/**
 * GstObject functions
 */
public interface GstObjectAPI extends com.sun.jna.Library {
	GstObjectAPI GSTOBJECT_API = GstNative.load(GstObjectAPI.class);

    GType gst_object_get_type();
    void gst_object_ref(GstObject ptr);
    void gst_object_unref(GstObject ptr);
    void gst_object_ref_sink(GstObject ptr);
    
    boolean gst_object_set_name(GstObject obj, String name);
    @FreeReturnValue String gst_object_get_name(GstObject obj);
    void gst_object_set_name_prefix(GstObject object, String name_prefix);
    @FreeReturnValue String gst_object_get_name_prefix(GstObject object);
    
    /* parentage routines */
    boolean gst_object_set_parent(GstObject object, GstObject parent);
    @CallerOwnsReturn GstObject gst_object_get_parent(GstObject object);
    void gst_object_unparent(GstObject object);
    boolean gst_object_has_ancestor(GstObject object, GstObject ancestor);
    
    Pointer gst_implements_interface_cast(GstObject obj, NativeLong gtype);    
    boolean gst_implements_interface_check(GstObject from, NativeLong type);
    
    public static final class GstObjectStruct extends com.sun.jna.Structure {        
        public GObjectAPI.GObjectStruct object;
        public volatile int refcount;
        public volatile Pointer lock;
        public volatile String name;
        public volatile String name_prefix;
        public volatile Pointer parent;
        public volatile int flags;
        public volatile Pointer _gst_reserved;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "object", "refcount", "lock",
                "name", "name_prefix", "parent",
                "flags", "_gst_reserved"
            });
        }
    }
    
    public static final class GstObjectClass extends com.sun.jna.Structure {
        public GObjectAPI.GObjectClass parent_class;
        public volatile Pointer path_string_separator;
        public volatile Pointer signal_object;
        public volatile Pointer lock;
        // These are really Callbacks, but we don't need them yet
        public volatile Pointer parent_set;
        public volatile Pointer parent_unset;
        public volatile Pointer object_saved;
        public volatile Pointer deep_notify;
        public volatile Pointer save_thyself;
        public volatile Pointer restore_thyself;
        public volatile Pointer[] _gst_reserved = new Pointer[GstAPI.GST_PADDING];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_class", "path_string_separator",
                "signal_object", "lock", "parent_set",
                "parent_unset", "object_saved", "deep_notify",
                "save_thyself", "restore_thyself", "_gst_reserved"
            });
        }
    }
}
