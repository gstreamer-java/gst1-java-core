/*
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

/**
 * GstStructure functions
 */
public interface GstEnumAPI extends com.sun.jna.Library {
    GstEnumAPI GSTENUM_API = GstNative.load(GstEnumAPI.class);

    String g_enum_to_string(GType g_enum_type, int value);

    GEnumValue g_enum_get_value_by_nick(GObjectAPI.GTypeClass enum_class, String nick);

    GEnumValue g_enum_get_value_by_name(GObjectAPI.GTypeClass enum_class, String name);
}
