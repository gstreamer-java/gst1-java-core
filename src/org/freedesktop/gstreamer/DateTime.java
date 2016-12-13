/* 
 * Copyright (c) 2016 Christophe Lafolet
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

package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GstDateTimeAPI.GSTDATETIME_API;

import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.NativeObject;

import com.sun.jna.Pointer;

/**
 */
public class DateTime extends NativeObject {
	
    public static final String GTYPE_NAME = "GstDateTime";
    public static final GType GTYPE = GSTDATETIME_API.gst_date_time_get_type();


    public static DateTime createInstanceLocalEpoch(long secs) {
    	return new DateTime(GSTDATETIME_API.gst_date_time_new_from_unix_epoch_local_time(secs), false, true);
    }

    public DateTime(Initializer init) {
        super(init); 
    }
    
    public DateTime(Pointer ptr, boolean needRef, boolean ownsHandle) {
        this(initializer(ptr, needRef, ownsHandle));
    }

    @Override
    protected void disposeNativeHandle(Pointer ptr) {
    	GSTDATETIME_API.gst_date_time_unref(ptr);
    }
    
    public int getYear() {
        return GSTDATETIME_API.gst_date_time_get_year(handle());
    }
    
    public int getMonth() {
        return GSTDATETIME_API.gst_date_time_get_month(handle());
    }
    
    public int getDay() {
        return GSTDATETIME_API.gst_date_time_get_day(handle());
    }

    public int getHour() {
        return GSTDATETIME_API.gst_date_time_get_hour(handle());
    }
    
    public int getMinute() {
        return GSTDATETIME_API.gst_date_time_get_minute(handle());
    }

    public int getSecond() {
        return GSTDATETIME_API.gst_date_time_get_second(handle());
    }

    public int getMicrosecond() {
        return GSTDATETIME_API.gst_date_time_get_microsecond(handle());
    }

    @Override
    public String toString() {
        return "" + getYear() + "-" + getMonth() + "-" + getDay() + " " + getHour() + ":" + getMinute() + ":" + getSecond() + "." + getMicrosecond();
    }

}
