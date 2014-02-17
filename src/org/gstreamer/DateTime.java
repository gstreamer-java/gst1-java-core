/* 
 * Copyright (c) 2010 Levente Farkas
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

import org.gstreamer.lowlevel.GstDateTimeAPI;
import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.NativeObject;

import com.sun.jna.Pointer;

/**
 */
public class DateTime extends NativeObject {
	public static final String GTYPE_NAME = "GstDateTime";

	private static final GstDateTimeAPI gst = GstNative.load(GstDateTimeAPI.class);
    
    public static DateTime createInstanceLocalEpoch(long secs) {
    	return new DateTime(gst.gst_date_time_new_from_unix_epoch_local_time(secs), false, true);
    }
/*	@CallerOwnsReturn DateTime gst_date_time_new_from_unix_epoch_utc(long secs);
	@CallerOwnsReturn DateTime gst_date_time_new_local_time(int year, 
			int month, int day, int hour, int minute, double seconds);
	@CallerOwnsReturn DateTime gst_date_time_new(float tzoffset, int year, 
			int month, int day, int hour, int minute, double seconds);
	@CallerOwnsReturn DateTime gst_date_time_new_now_local_time();
	@CallerOwnsReturn DateTime gst_date_time_new_now_utc();*/
    
    public DateTime(Initializer init) {
        super(init); 
    }
    public DateTime(Pointer ptr, boolean needRef, boolean ownsHandle) {
        this(initializer(ptr, needRef, ownsHandle));
    }

    @Override
    protected void disposeNativeHandle(Pointer ptr) {
    	gst.gst_date_time_unref(ptr);
    }
    
}
