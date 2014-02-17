/* 
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

package org.gstreamer.glib;

import org.gstreamer.lowlevel.GObjectAPI;

public class GQuark {
    private final int value;
    public GQuark(int value) {
        this.value = value;
    }
    public int intValue() {
        return value;
    }
    
    public GQuark valueOf(String quark) {
        return GObjectAPI.GOBJECT_API.g_quark_from_string(quark);
    }
    
    @Override
    public String toString() {
        return GObjectAPI.GOBJECT_API.g_quark_to_string(this);
    }
}
