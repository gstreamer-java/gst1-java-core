/*
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2008 Andres Colubri
 * Copyright (c) 2008 Wayne Meissner
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

package org.gstreamer.lowlevel;

import org.gstreamer.interfaces.PropertyProbe;
import org.gstreamer.lowlevel.GlibAPI.GList;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface GstPropertyProbeAPI extends Library {
	GstPropertyProbeAPI GSTPROPERTYPROBE_API = GstNative.load("gstinterfaces", GstPropertyProbeAPI.class);

    GType gst_property_probe_get_type();

    /* virtual class function wrappers */
    GList gst_property_probe_get_properties(PropertyProbe probe);
    Pointer gst_property_probe_get_property(PropertyProbe probe, String name);

    void gst_property_probe_probe_property(PropertyProbe probe, GObjectAPI.GParamSpec pspec);
    void gst_property_probe_probe_property_name(PropertyProbe probe, String name);

    boolean gst_property_probe_needs_probe(PropertyProbe probe, GObjectAPI.GParamSpec pspec);
    boolean gst_property_probe_needs_probe_name(PropertyProbe probe, String name);

    Pointer gst_property_probe_get_values(PropertyProbe probe, GObjectAPI.GParamSpec pspec);
    Pointer gst_property_probe_get_values_name(PropertyProbe probe, String name);

    Pointer gst_property_probe_probe_and_get_values(PropertyProbe probe, GObjectAPI.GParamSpec pspec);
    Pointer gst_property_probe_probe_and_get_values_name(PropertyProbe probe, String name);
}
