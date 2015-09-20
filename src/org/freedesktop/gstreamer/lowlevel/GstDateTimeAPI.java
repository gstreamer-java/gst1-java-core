/* 
 * Copyright (c) 2010 Levente Farkas
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

import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;


/**
 * GstDateTime functions
 * 
 * A date, time and timezone structure
 *
 * Struct to store date, time and timezone information altogether.
 * GstDateTime is refcounted and immutable.
 *
 * Date information is handled using the proleptic Gregorian calendar.
 *
 * Provides basic creation functions and accessor functions to its fields.
 */
public interface GstDateTimeAPI extends com.sun.jna.Library {
	GstDateTimeAPI GSTDATETIME_API = GstNative.load(GstDateTimeAPI.class);

	GType	gst_date_time_get_type();
	int	gst_date_time_get_year(Pointer datetime);
	int	gst_date_time_get_month(Pointer datetime);
	int	gst_date_time_get_day(Pointer datetime);
	int	gst_date_time_get_hour(Pointer datetime);
	int	gst_date_time_get_minute(Pointer datetime);
	int	gst_date_time_get_second(Pointer datetime);
	int	gst_date_time_get_microsecond(Pointer datetime);
	float gst_date_time_get_time_zone_offset(Pointer datetime);

	@CallerOwnsReturn Pointer gst_date_time_new_from_unix_epoch_local_time(long secs);
	@CallerOwnsReturn Pointer gst_date_time_new_from_unix_epoch_utc(long secs);
	@CallerOwnsReturn Pointer gst_date_time_new_local_time(int year, 
			int month, int day, int hour, int minute, double seconds);
	@CallerOwnsReturn Pointer gst_date_time_new(float tzoffset, int year, 
			int month, int day, int hour, int minute, double seconds);
	@CallerOwnsReturn Pointer gst_date_time_new_now_local_time();
	@CallerOwnsReturn Pointer gst_date_time_new_now_utc();

	Pointer gst_date_time_ref(Pointer datetime);
	void gst_date_time_unref(Pointer datetime);	
}
