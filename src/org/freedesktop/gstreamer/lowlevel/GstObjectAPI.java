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

import com.sun.jna.Callback;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.FreeReturnValue;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.List;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.elements.BaseSrc;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;

/**
 * GstObject method and structures
 * @see https://cgit.freedesktop.org/gstreamer/gstreamer/tree/gst/gstobject.h?h=1.8
 */
public interface GstObjectAPI extends com.sun.jna.Library {
    
    GstObjectAPI GSTOBJECT_API = GstNative.load(GstObjectAPI.class);

    GType gst_object_get_type();
    void gst_object_ref(GstObjectPtr ptr);
    void gst_object_unref(GstObjectPtr ptr);
    void gst_object_ref_sink(GstObjectPtr ptr);
    
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
    
    /**
    * GstObject:
    * @lock: object LOCK
    * @name: The name of the object
    * @parent: this object's parent, weak ref
    * @flags: flags for this object
    *
    * GStreamer base object class.
    */
    public static final class GstObjectStruct extends com.sun.jna.Structure {        
        public GObjectAPI.GObjectStruct object;
        
        /*< public >*/ /* with LOCK */        
        public volatile Pointer /* GMutex */ lock;  /* object LOCK */
        public volatile String name;                /* object name */
        public volatile Pointer /* GstObject */ parent; /* this object's parent, weak ref */
        public volatile int flags;
        
        /*< private >*/
        public volatile GList control_bindings;
        public volatile long control_rate;
        public volatile long last_sync;
        
        public volatile Pointer _gst_reserved;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "object",
                "lock", "name", "parent", "flags",
                "control_bindings", "control_rate", "last_sync",
                "_gst_reserved"
            });
        }
    }
    
    // -------------- Callbacks -----------------
    public static interface DeepNotify extends Callback {
        public void callback(GstObject object, GstObject orig, GObjectAPI.GParamSpec pspec);
    }
    
    /**
    * GstObjectClass:
    * @parent_class: parent
    * @path_string_separator: separator used by gst_object_get_path_string()
    * @deep_notify: default signal handler
    *
    * GStreamer base object class.
    */
    public static final class GstObjectClass extends com.sun.jna.Structure {
        public GObjectAPI.GObjectClass parent_class;
        
        public volatile String path_string_separator;
        
        /* signals */
        public DeepNotify deep_notify;
        
        public volatile Pointer[] _gst_reserved = new Pointer[GstAPI.GST_PADDING];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_class", "path_string_separator",
                "deep_notify", 
                "_gst_reserved"
            });
        }
    }
}
