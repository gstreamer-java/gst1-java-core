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

import org.freedesktop.gstreamer.lowlevel.GObjectAPI;

/**
 * Quarks — a 2-way association between a string and a unique integer identifier.
 * 
 * See upstream documentation at <a href="https://developer.gnome.org/glib/stable/glib-Quarks.html"
 * >https://developer.gnome.org/glib/stable/glib-Quarks.html</a>
 * 
 */
public class GQuark {
    
    private final int value;
    
    public GQuark(int value) {
        this.value = value;
    }
    
    public int intValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return GObjectAPI.GOBJECT_API.g_quark_to_string(this);
    }
    
    public static GQuark valueOf(String quark) {
        return GObjectAPI.GOBJECT_API.g_quark_from_string(quark);
    }
}
