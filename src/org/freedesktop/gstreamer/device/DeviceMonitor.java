/*
 * 
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
package org.freedesktop.gstreamer.device;

import java.util.ArrayList;
import java.util.List;
import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;

import static org.freedesktop.gstreamer.lowlevel.GstDeviceMonitorAPI.GSTDEVICEMONITOR_API;

/**
 *
 * @author andres
 */
public class DeviceMonitor extends GstObject {

    public static final String GTYPE_NAME = "GstDeviceMonitor";

    public DeviceMonitor(Initializer init) {
        super(init);
    }

    static public DeviceMonitor createNew() {
        return GSTDEVICEMONITOR_API.gst_device_monitor_new();
    }

    public Bus getBus() {
        return GSTDEVICEMONITOR_API.gst_device_monitor_get_bus(this);
    }

    public int addFilter(String classes, Caps caps) {
        return GSTDEVICEMONITOR_API.gst_device_monitor_add_filter(this, classes, caps);
    }

    public boolean removeFilter(int filterId) {
        return GSTDEVICEMONITOR_API.gst_device_monitor_remove_filter(this, filterId);
    }

    public boolean start() {
        return GSTDEVICEMONITOR_API.gst_device_monitor_start(this);
    }

    public void stop() {
        GSTDEVICEMONITOR_API.gst_device_monitor_stop(this);
    }

    public List<Device> getDevices() {
        GList glist = GSTDEVICEMONITOR_API.gst_device_monitor_get_devices(this);
        List<Device> list = new ArrayList<Device>();

        GList next = glist;
        while (next != null) {
            if (next.data != null) {
                Device dev = new Device(initializer(next.data, false, true));
                list.add(dev);
            }
            next = next.next();
        }

        return list;
    }
}
