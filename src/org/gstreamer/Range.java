/* 
 * Copyright (c) 2009 Levente Farkas
 * Copyright (C) 2009 Tamas Korodi <kotyo@zamba.fm>
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

package org.gstreamer;

import org.gstreamer.lowlevel.GValueAPI;
import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.GstValueAPI;

/**
 * Represents a range of float, double, int, fraction types stored in a GValue 
 * @author kotyo
 *
 */
public class Range {
	// there are multiple native types
	//public static final String GTYPE_NAME = "GstDoubleRange";

	private static final GstValueAPI gst = GstNative.load(GstValueAPI.class);
	private GValueAPI.GValue value; 
	
	Range(GValueAPI.GValue value) {
		this.value = value;
	}
	
	/**
	 * Gets the minimum fraction of the range
	 * @return minimum fraction of the range
	 */
	public Fraction getMinFraction() {
		GValueAPI.GValue frMin = gst.gst_value_get_fraction_range_min(value); 
		int num = gst.gst_value_get_fraction_numerator(frMin);
		int denom = gst.gst_value_get_fraction_denominator(frMin);
		return new Fraction(num, denom);
	}

	/**
	 * Gets the maximum fraction of the range
	 * @return maximum fraction of the range
	 */
	public Fraction getMaxFraction() {
		GValueAPI.GValue frMax = gst.gst_value_get_fraction_range_max(value); 
		int num = gst.gst_value_get_fraction_numerator(frMax);
		int denom = gst.gst_value_get_fraction_denominator(frMax);
		return new Fraction(num, denom);		
	}

	/**
	 * Gets the minimum double of the range
	 * @return minimum double of the range
	 */
	public double getMinDouble() {
		return gst.gst_value_get_double_range_min(value);
	}
	
	/**
	 * Gets the maximum double of the range
	 * @return maximum double of the range
	 */
	public double getMaxDouble() {
		return gst.gst_value_get_double_range_max(value);
	}
	
	/**
	 * Gets the minimum integer of the range
	 * @return minimum integer of the range
	 */
	public int getMinInt() {
		return gst.gst_value_get_int_range_min(value);
	}
	
	/**
	 * Gets the maximum integer of the range
	 * @return maximum integer of the range
	 */
	public int getMaxInt() {
		return gst.gst_value_get_int_range_max(value);
	}
}
