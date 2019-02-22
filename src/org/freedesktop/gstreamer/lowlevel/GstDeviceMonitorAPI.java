/*
 * Copyright (c) 2015 Andres Colubri <andres.colubri@gmail.com>
*  Copyright (C) 2013 Olivier Crete <olivier.crete@collabora.com>
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
import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.device.DeviceMonitor;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;
import org.freedesktop.gstreamer.Caps;

/**
 *
 * http://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer/html/gstreamer-GstDeviceMonitor.html
 */ 
public interface GstDeviceMonitorAPI extends com.sun.jna.Library {
    GstDeviceMonitorAPI GSTDEVICEMONITOR_API = GstNative.load(GstDeviceMonitorAPI.class);
    
    @CallerOwnsReturn DeviceMonitor gst_device_monitor_new();
    @CallerOwnsReturn Pointer ptr_gst_device_monitor_new();
    @CallerOwnsReturn Bus gst_device_monitor_get_bus(DeviceMonitor monitor);
    int gst_device_monitor_add_filter(DeviceMonitor monitor, String classes, Caps caps);
    boolean gst_device_monitor_remove_filter(DeviceMonitor monitor, int filterId);
    boolean gst_device_monitor_start(DeviceMonitor monitor);
    void gst_device_monitor_stop(DeviceMonitor monitor);
    GList gst_device_monitor_get_devices(DeviceMonitor monitor);
}
