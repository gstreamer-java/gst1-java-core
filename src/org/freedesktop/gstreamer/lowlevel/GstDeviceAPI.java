/*
 * Copyright (c) 2017 Neil C Smith
 * Copyright (c) 2015 Andres Colubri <andres.colubri@gmail.com>
 * Copyright (C) 2013 Olivier Crete <olivier.crete@collabora.com>
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

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.device.Device;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

/**
 *
 * http://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer/html/gstreamer-GstDevice.html
 */ 
public interface GstDeviceAPI extends com.sun.jna.Library {
    GstDeviceAPI GSTDEVICE_API = GstNative.load(GstDeviceAPI.class);
    
    @CallerOwnsReturn Element gst_device_create_element(Device device, String name);
    @CallerOwnsReturn Caps gst_device_get_caps(Device device);
    Pointer gst_device_get_device_class(Device device);
    Pointer gst_device_get_display_name(Device device);
    boolean gst_device_has_classes(Device device, String classes);
    boolean gst_device_has_classesv(Device device, String[] classes);
    boolean gst_device_reconfigure_element(Device device, Element element);
    Structure gst_device_get_properties(Device device);
}
