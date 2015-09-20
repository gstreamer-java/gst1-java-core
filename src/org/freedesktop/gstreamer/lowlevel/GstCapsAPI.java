/* 
 * Copyright (c) 2015 Neil C Smith 
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

import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.FreeReturnValue;
import org.freedesktop.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.List;

/**
 * GstCaps functions
 */
public interface GstCapsAPI extends com.sun.jna.Library {
    GstCapsAPI GSTCAPS_API = GstNative.load(GstCapsAPI.class);

    GType gst_caps_get_type();
    @CallerOwnsReturn Pointer ptr_gst_caps_new_empty();
    @CallerOwnsReturn Pointer ptr_gst_caps_new_any();
    @CallerOwnsReturn Pointer ptr_gst_caps_new_simple(String media_type, String fieldName, Object... args);
    @CallerOwnsReturn Pointer ptr_gst_caps_new_full(Structure... data);
    @CallerOwnsReturn Caps gst_caps_new_empty();
    @CallerOwnsReturn Caps gst_caps_new_any();
    @CallerOwnsReturn Caps gst_caps_new_simple(String media_type, String fieldName, Object... args);
    @CallerOwnsReturn Caps gst_caps_new_full(Structure... data);
    
    Pointer gst_caps_ref(Caps caps);
    void gst_caps_unref(Caps caps);
    void gst_caps_unref(Pointer caps);
    @CallerOwnsReturn Pointer ptr_gst_caps_copy(Caps caps);
    @CallerOwnsReturn Pointer ptr_gst_caps_from_string(String string);
    @CallerOwnsReturn Caps gst_caps_copy(Caps caps);
    @CallerOwnsReturn Caps gst_caps_from_string(String string);
    
    @CallerOwnsReturn Caps gst_caps_make_writable(@Invalidate Caps caps);
    
    /* manipulation */
    void gst_caps_append(Caps caps1, @Invalidate Caps caps2);
    @CallerOwnsReturn Caps gst_caps_merge(Caps caps1, @Invalidate Caps caps2);
    void gst_caps_append_structure(Caps caps, @Invalidate Structure structure);
    void gst_caps_remove_structure(Caps caps, int idx);
    void gst_caps_merge_structure(Caps caps, @Invalidate Structure structure);
    int gst_caps_get_size(Caps caps);
    Structure gst_caps_get_structure(Caps caps, int index);
    @CallerOwnsReturn Structure gst_caps_steal_structure(Caps caps, int index);
    @CallerOwnsReturn Caps gst_caps_copy_nth(Caps caps, int nth);
    void gst_caps_truncate(Caps caps);
    void gst_caps_set_simple(Caps caps, String field, Object... values);
    /* operations */
    @CallerOwnsReturn Caps gst_caps_intersect( Caps caps1,  Caps caps2);
    @CallerOwnsReturn Caps gst_caps_subtract( Caps minuend,  Caps subtrahend);
    @CallerOwnsReturn Caps gst_caps_union( Caps caps1,  Caps caps2);
    @CallerOwnsReturn Caps gst_caps_normalize( Caps caps);
    boolean gst_caps_simplify(Caps caps);
    @FreeReturnValue String gst_caps_to_string(Caps caps);
    /* tests */

    boolean gst_caps_is_any(Caps caps);
    boolean gst_caps_is_empty(Caps caps);
    boolean gst_caps_is_fixed(Caps caps);
    boolean gst_caps_is_always_compatible(Caps caps1,  Caps caps2);
    boolean gst_caps_is_subset(Caps subset,  Caps superset);
    boolean gst_caps_is_equal(Caps caps1,  Caps caps2);
    boolean gst_caps_is_equal_fixed(Caps caps1,  Caps caps2);
    boolean gst_caps_can_intersect(Caps caps1, Caps caps2);

    public static final class GPtrArray extends com.sun.jna.Structure {
    	public volatile Pointer pdata;
    	public volatile int     len;    	

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "pdata", "len"
            });
        }
    }
    public static final class GstCapsStruct extends com.sun.jna.Structure {

        public volatile GType type;
        public volatile int refcount;

        /*< public >*/ /* read only */

        public volatile int flags;

        /*< private >*/
        public volatile GPtrArray structs;

        /*< private >*/
        public volatile byte[] _gst_reserved = new byte[Pointer.SIZE * GstAPI.GST_PADDING];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "type", "refcount", "flags",
                "structs", "_gst_reserved"
            });
        }
    }

    
    public static final class GstStaticCapsStruct extends com.sun.jna.Structure {
    	/*< public >*/
        public volatile GstCapsStruct caps;
        public volatile String string;

        /*< private >*/
        public volatile byte[] _gst_reserved = new byte[Pointer.SIZE * GstAPI.GST_PADDING];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "caps", "string", "_gst_reserved"
            });
        }
    }
   
    GType gst_static_caps_get_type();
    /* static_caps_get returns a static Caps reference - do not deref */
    Caps gst_static_caps_get(GstStaticCapsStruct static_caps);
}
