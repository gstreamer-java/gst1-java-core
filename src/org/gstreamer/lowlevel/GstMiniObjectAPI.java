/* 
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

package org.gstreamer.lowlevel;

import org.gstreamer.MiniObject;
import org.gstreamer.lowlevel.GObjectAPI.GTypeInstance;
import org.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.List;

/**
 * GstMiniObject functions
 */
public interface GstMiniObjectAPI extends com.sun.jna.Library {
    GstMiniObjectAPI GSTMINIOBJECT_API = GstNative.load(GstMiniObjectAPI.class);

    void gst_mini_object_ref(MiniObject ptr);
    void gst_mini_object_unref(MiniObject ptr);
    void gst_mini_object_unref(Pointer ptr);
    @CallerOwnsReturn Pointer ptr_gst_mini_object_copy(MiniObject mini_object);
    @CallerOwnsReturn MiniObject gst_mini_object_copy(MiniObject mini_object);
    boolean gst_mini_object_is_writable(MiniObject mini_object);
    /* FIXME - invalidate the argument, and return a MiniObject */
    @CallerOwnsReturn Pointer ptr_gst_mini_object_make_writable(@Invalidate MiniObject mini_object);
    @CallerOwnsReturn MiniObject gst_mini_object_make_writable(@Invalidate MiniObject mini_object);
    
    public static final class MiniObjectStruct extends com.sun.jna.Structure {
        public volatile NativeLong type; // GType - this is a guess from http://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer/html/gstreamer-GstMiniObject.html#GstMiniObject
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
            useMemory(ptr);
            read();
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
