/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2016 Christophe Lafolet
 * Copyright (c) 2007 Wayne Meissner
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

package org.freedesktop.gstreamer.glib;

import org.freedesktop.gstreamer.lowlevel.GlibAPI;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.lowlevel.GPointer;

/**
 * Wrapper to the GDate data structure.
 * 
 * See upstream documentation at <a href="https://developer.gnome.org/glib/stable/glib-Date-and-Time-Functions.html"
 * >https://developer.gnome.org/glib/stable/glib-Date-and-Time-Functions.html</a>
 */
public class GDate extends NativeObject {
    
    public static final String GTYPE_NAME = "GDate";

    GDate(Initializer init) {
        this(new Handle(init.ptr, init.ownsHandle));
    }
    
    GDate(Handle handle) {
        super(handle);
    }

    public int getDay() {
        return GlibAPI.GLIB_API.g_date_get_day(getRawPointer());
    }
    
    public int getMonth() {
        return GlibAPI.GLIB_API.g_date_get_month(getRawPointer());
    }
    public int getYear() {
        return GlibAPI.GLIB_API.g_date_get_year(getRawPointer());
    }
    
    @Override
    public String toString() {
        return "" + getYear() + "-" + getMonth() + "-" + getDay();
    }
    
    public static GDate createInstance(int day, int month, int year) {
        Pointer ptr = GlibAPI.GLIB_API.g_date_new_dmy(day, month, year);
        return new GDate(new Handle(new GPointer(ptr), true));
    }
    
    public static GDate createInstance(int julian_day) {
        Pointer ptr = GlibAPI.GLIB_API.g_date_new_julian(julian_day);
        return new GDate(new Handle(new GPointer(ptr), true));
    }
    
    private static final class Handle extends NativeObject.Handle {

        public Handle(GPointer ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GlibAPI.GLIB_API.g_date_free(ptr.getPointer());
        }
        
    }
    
}
