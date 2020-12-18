/* 
 * Copyright (c) 2021 Neil C Smith
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

import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

/**
 * GstBin functions
 */
public interface GstBinAPI extends com.sun.jna.Library {
    GstBinAPI GSTBIN_API = GstNative.load(GstBinAPI.class);

    @CallerOwnsReturn Pointer ptr_gst_bin_new(String name);
    @CallerOwnsReturn Pointer ptr_gst_pipeline_new(String name);
    @CallerOwnsReturn Bin gst_bin_new(String name);
    GType gst_bin_get_type();
    
    boolean gst_bin_add(Bin bin, Element element);
    void gst_bin_add_many(Bin bin, Element... elements);
    boolean gst_bin_remove(Bin bin, Element element);
    void gst_bin_remove_many(Bin bin, Element... elements);
    @CallerOwnsReturn Element gst_bin_get_by_name(Bin bin, String name);
    @CallerOwnsReturn Element gst_bin_get_by_name_recurse_up(Bin bin, String name);
    @CallerOwnsReturn Element gst_bin_get_by_interface(Bin bin, GType iface);
    GstIteratorPtr gst_bin_iterate_elements(Bin bin);
    GstIteratorPtr gst_bin_iterate_sorted(Bin bin);
    GstIteratorPtr gst_bin_iterate_recurse(Bin bin);
    GstIteratorPtr gst_bin_iterate_sinks(Bin bin);
    GstIteratorPtr gst_bin_iterate_sources(Bin bin);
    GstIteratorPtr gst_bin_iterate_all_by_interface(Bin bin, GType iface);

    //Debugging
    void gst_debug_bin_to_dot_file (Bin bin, int details, String file_name);
    void gst_debug_bin_to_dot_file_with_ts (Bin bin, int details, String file_name);
    
}
