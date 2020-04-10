/* 
 * Copyright (c) 2020 Neil C Smith
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

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;

/**
 * GstValue functions
 */
public interface GstValueAPI extends com.sun.jna.Library {
	GstValueAPI GSTVALUE_API = GstNative.load(GstValueAPI.class);

    GType gst_fourcc_get_type();
    GType gst_int_range_get_type();
    GType gst_double_range_get_type();
    GType gst_fraction_range_get_type();
    GType gst_value_list_get_type();
    GType gst_fraction_get_type();
    
    int gst_value_get_fraction_numerator(GValue  value);
    int gst_value_get_fraction_denominator(GValue value);
    GValue gst_value_get_fraction_range_min(GValue value);
    GValue gst_value_get_fraction_range_max(GValue value);
    double gst_value_get_double_range_min(GValue value);
    double gst_value_get_double_range_max(GValue value);
    int gst_value_get_int_range_min(GValue value);
    int gst_value_get_int_range_max(GValue value);
    int gst_value_list_get_size(GValue value);
    GValue gst_value_list_get_value(GValue value, int index);
    
    boolean gst_value_deserialize(GValue value, String src);
    Pointer gst_value_serialize(GValue value);
    
}
