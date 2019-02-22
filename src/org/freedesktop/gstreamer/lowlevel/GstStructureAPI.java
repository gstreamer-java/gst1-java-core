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

import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.FreeReturnValue;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * GstStructure functions
 */
public interface GstStructureAPI extends com.sun.jna.Library {
	GstStructureAPI GSTSTRUCTURE_API = GstNative.load(GstStructureAPI.class);

    GType gst_structure_get_type();

    boolean gst_structure_get_int(Structure structure, String fieldname, IntByReference value);
    boolean gst_structure_fixate_field_nearest_int(Structure structure, String field, int target);
    @FreeReturnValue String gst_structure_to_string(Structure structure);
    @CallerOwnsReturn Structure gst_structure_from_string(String data, PointerByReference end);
    @CallerOwnsReturn Structure gst_structure_new_empty(String name);
    @CallerOwnsReturn Structure gst_structure_new(String name, String firstField, Object... args);
    @CallerOwnsReturn Pointer ptr_gst_structure_from_string(String data, PointerByReference end);
    @CallerOwnsReturn Pointer ptr_gst_structure_new_empty(String name);
    @CallerOwnsReturn Pointer ptr_gst_structure_new(String name, String firstField, Object... args);
    @CallerOwnsReturn Structure gst_structure_copy(Structure src);
    void gst_structure_remove_field(Structure structure, String fieldName);
    void gst_structure_remove_fields(Structure structure, String... fieldNames);
    void gst_structure_remove_all_fields(Structure structure);
    
    String gst_structure_get_name(Structure structure);
    void gst_structure_set_name(Structure structure, String name);
    boolean gst_structure_has_name(Structure structure, String name); 
    int gst_structure_n_fields(Structure structure);
    String gst_structure_nth_field_name(Structure structure, int index);
    boolean gst_structure_has_field(Structure structure, String fieldname);
    boolean gst_structure_has_field_typed(Structure structure, String fieldname, GType type);
    boolean gst_structure_is_equal(Structure structure1, Structure structure2);

    /* utility functions */
    boolean gst_structure_get_boolean(Structure structure, String fieldname, int[] value);
    boolean gst_structure_get_int(Structure structure, String fieldname, int[] value);
    boolean gst_structure_get_uint(Structure structure, String fieldname, int[] value);
    boolean gst_structure_get_fourcc(Structure structure, String fieldname, int[] value);
    boolean gst_structure_get_double(Structure structure, String fieldname, double[] value);
    
    boolean gst_structure_get_date(Structure structure, String fieldname, PointerByReference value);
    boolean gst_structure_get_date(Structure structure, String fieldname, Pointer[] value);
    //boolean gst_structure_get_clock_time(Structure structure, String fieldname, Gstlong *value);
    
    String gst_structure_get_string(Structure structure, String fieldname);
    boolean gst_structure_get_enum(Structure structure, String fieldname, GType enumtype, int[] value);
    boolean gst_structure_get_fraction(Structure structure, String fieldname,
							    int[] value_numerator,
							    int[] value_denominator);
    GValue gst_structure_get_value(Structure structure, String fieldname);
    void gst_structure_set(Structure structure, String fieldname, Object... args);
    
    void gst_structure_free(Pointer ptr);
}
