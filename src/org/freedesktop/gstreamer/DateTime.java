/* 
 * Copyright (c) 2019 Neil C Smith
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

import org.freedesktop.gstreamer.glib.NativeObject;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.lowlevel.GPointer;

/**
 */
public class DateTime extends NativeObject {

    public static final String GTYPE_NAME = "GstDateTime";

    public static DateTime createInstanceLocalEpoch(long secs) {
        return new DateTime(GSTDATETIME_API.gst_date_time_new_from_unix_epoch_local_time(secs), false, true);
    }

    DateTime(Initializer init) {
        this(new Handle(init.ptr, init.ownsHandle));
    }

    DateTime(Pointer ptr, boolean needRef, boolean ownsHandle) {
        this(new Handle(new GPointer(ptr), ownsHandle));
    }
    
    DateTime(Handle handle) {
        super(handle);
    }

    public int getYear() {
        return GSTDATETIME_API.gst_date_time_get_year(getRawPointer());
    }

    public int getMonth() {
        return GSTDATETIME_API.gst_date_time_get_month(getRawPointer());
    }

    public int getDay() {
        return GSTDATETIME_API.gst_date_time_get_day(getRawPointer());
    }

    public int getHour() {
        return GSTDATETIME_API.gst_date_time_get_hour(getRawPointer());
    }

    public int getMinute() {
        return GSTDATETIME_API.gst_date_time_get_minute(getRawPointer());
    }

    public int getSecond() {
        return GSTDATETIME_API.gst_date_time_get_second(getRawPointer());
    }

    public int getMicrosecond() {
        return GSTDATETIME_API.gst_date_time_get_microsecond(getRawPointer());
    }

    @Override
    public String toString() {
        return "" + getYear() + "-" + getMonth() + "-" + getDay() + " " + getHour() + ":" + getMinute() + ":" + getSecond() + "." + getMicrosecond();
    }

    private static final class Handle extends NativeObject.Handle {

        public Handle(GPointer ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GSTDATETIME_API.gst_date_time_unref(ptr.getPointer());
        }

    }

}
