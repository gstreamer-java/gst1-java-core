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

import org.freedesktop.gstreamer.TagList;
import org.freedesktop.gstreamer.TagMergeMode;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * GstTagList functions
 */
public interface GstTagListAPI extends com.sun.jna.Library {
	GstTagListAPI GSTTAGLIST_API = GstNative.load(GstTagListAPI.class);

    interface TagForeachFunc extends GstCallback {
        void callback(Pointer list, String tag, Pointer user_data);
    }
    interface TagMergeFunc extends GstCallback {
        void callback(Pointer dest, Pointer src);
    }
    
    @CallerOwnsReturn Pointer ptr_gst_tag_list_new_empty();
    
    void gst_tag_list_add(TagList list, TagMergeMode mode, String tag, Object... tags);
    @CallerOwnsReturn TagList gst_tag_list_copy(TagList list);
    @CallerOwnsReturn Pointer ptr_gst_tag_list_copy(TagList list);
    boolean gst_tag_list_is_empty(TagList list);
    void gst_tag_list_insert(TagList into, TagList from, TagMergeMode mode);

    @CallerOwnsReturn TagList gst_tag_list_merge(TagList list1, TagList list2, TagMergeMode mode);
    @CallerOwnsReturn Pointer ptr_gst_tag_list_merge(TagList list1, TagList list2, TagMergeMode mode);
    int gst_tag_list_get_tag_size(TagList list, String tag);
    void gst_tag_list_remove_tag(TagList list, TagList tag);
    void gst_tag_list_foreach(TagList list, TagForeachFunc func, Pointer user_data);
    
    boolean gst_tag_list_get_char(TagList list, String tag, byte[] value);
    boolean gst_tag_list_get_char_index(TagList list, String tag, int index, byte[] value);
    boolean gst_tag_list_get_uchar(TagList list, String tag, byte[] value);
    boolean gst_tag_list_get_uchar_index(TagList list, String tag, int index, byte[] value);
    boolean gst_tag_list_get_boolean(TagList list, String tag, int[] value);
    boolean gst_tag_list_get_boolean_index(TagList list, String tag, int index, int[] value);
    boolean gst_tag_list_get_int(TagList list, String tag, int[] value);
    boolean gst_tag_list_get_int_index(TagList list, String tag, int index, int[] value);
    boolean gst_tag_list_get_uint(TagList list, String tag, int[] value);
    boolean gst_tag_list_get_uint_index(TagList list, String tag, int index, int[] value);
    boolean gst_tag_list_get_int64(TagList list, String tag, long[] value);
    boolean gst_tag_list_get_int64_index(TagList list, String tag, int index, long[] value);
    boolean gst_tag_list_get_double(TagList list, String tag, double[] value);
    boolean gst_tag_list_get_double_index(TagList list, String tag, int index, double[] value);
    boolean gst_tag_list_get_string(TagList list, String tag, PointerByReference value);
    boolean gst_tag_list_get_string_index(TagList list, String tag, int index, PointerByReference value);
    boolean gst_tag_list_get_string_index(TagList list, String tag, int index, Pointer[] value);
    boolean gst_tag_list_get_date_index(TagList list, String tag, int index, PointerByReference value);
    boolean gst_tag_list_get_date_index(TagList list, String tag, int index, Pointer[] value);
    boolean gst_tag_list_get_date_time(TagList list, String tag, PointerByReference value);
    boolean gst_tag_list_get_date_time_index(TagList list, String tag, int index, PointerByReference value);
    
}
