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

import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GlibAPI;
import org.freedesktop.gstreamer.glib.NativeObject;

import com.sun.jna.Pointer;

/**
 * Wrapper to the GDate data structure.
 * 
 * See upstream documentation at <a href="https://developer.gnome.org/glib/stable/glib-Date-and-Time-Functions.html"
 * >https://developer.gnome.org/glib/stable/glib-Date-and-Time-Functions.html</a>
 */
public class GDate extends NativeObject {
    public static final String GTYPE_NAME = "GDate";
    public static final GType GTYPE = GType.valueOf(GTYPE_NAME); 

    @Deprecated
    public GDate(Initializer init) {
        super(init);
    }
    
    @Deprecated
    public GDate(Pointer ptr, boolean needRef, boolean ownsHandle) {
        this(initializer(ptr, needRef, ownsHandle));
    }
    public int getDay() {
        return GlibAPI.GLIB_API.g_date_get_day(handle());
    }
    
    public int getMonth() {
        return GlibAPI.GLIB_API.g_date_get_month(handle());
    }
    public int getYear() {
        return GlibAPI.GLIB_API.g_date_get_year(handle());
    }
    
    @Override
    public String toString() {
        return "" + getYear() + "-" + getMonth() + "-" + getDay();
    }
    
    @Override
    protected void disposeNativeHandle(Pointer ptr) {
        GlibAPI.GLIB_API.g_date_free(ptr);
    }
    
    public static GDate createInstance(int day, int month, int year) {
        return new GDate(GlibAPI.GLIB_API.g_date_new_dmy(day, month , year), false, true);
    }
    
    public static GDate createInstance(int julian_day) {
        return new GDate(GlibAPI.GLIB_API.g_date_new_julian(julian_day), false, true);
    }
}
