/*
 * 
 * Copyright (c) 2019 Neil C Smith
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
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;

import static org.freedesktop.gstreamer.lowlevel.GstDeviceMonitorAPI.GSTDEVICEMONITOR_API;

/**
 * A device monitor and prober
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/gstreamer-GstDeviceMonitor.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/gstreamer-GstDeviceMonitor.html</a>
 * <p>
 * Applications should create a DeviceMonitor when they want to probe, list and
 * monitor devices of a specific type. The DeviceMonitor will create the
 * appropriate {@link DeviceProvider} objects and manage them. It will then post
 * messages on its {@link Bus} for devices that have been added and removed.
 * <p>
 * The device monitor will monitor all devices matching the filters that the
 * application has set.
 */
public class DeviceMonitor extends GstObject {

    public static final String GTYPE_NAME = "GstDeviceMonitor";

    public DeviceMonitor() {
        super(Natives.initializer(GSTDEVICEMONITOR_API.ptr_gst_device_monitor_new(), false, true));
    }

    DeviceMonitor(Initializer init) {
        super(init);
    }

    /**
     * Get the {@link Bus} for this DeviceMonitor
     *
     * @return bus
     */
    public Bus getBus() {
        return GSTDEVICEMONITOR_API.gst_device_monitor_get_bus(this);
    }

    /**
     * Adds a filter for which devices will be monitored, any device that
     * matches all these classes and the {@link Caps} will be returned.
     * <p>
     * If this function is called multiple times to add more filters, each will
     * be matched independently. That is, adding more filters will not further
     * restrict what devices are matched.
     * <p>
     * The GstCaps supported by the device as returned by gst_device_get_caps()
     * are not intersected with caps filters added using this function.
     * <p>
     * Filters must be added before the GstDeviceMonitor is started.
     *
     * @param classes device classes to use as filter or NULL for any class.
     * @param caps the GstCaps to filter or NULL for ANY.
     * @return id the id of the new filter or 0 if no provider matched the
     * filter's classes.
     */
    public int addFilter(String classes, Caps caps) {
        return GSTDEVICEMONITOR_API.gst_device_monitor_add_filter(this, classes, caps);
    }

    /**
     * Remove a filter from the DeviceMonitor using the id that was returned
     * from {@link #addFilter(java.lang.String, org.freedesktop.gstreamer.Caps)
     * }
     *
     * @param filterId of filter to remove
     * @return true if the filter id was valid and the filter was removed
     */
    public boolean removeFilter(int filterId) {
        return GSTDEVICEMONITOR_API.gst_device_monitor_remove_filter(this, filterId);
    }

    /**
     * Starts monitoring the devices, once this has succeeded, the
     * GST_MESSAGE_DEVICE_ADDED and GST_MESSAGE_DEVICE_REMOVED messages will be
     * emitted on the bus when the list of devices changes.
     *
     * @return true if the device monitoring could be started
     */
    public boolean start() {
        return GSTDEVICEMONITOR_API.gst_device_monitor_start(this);
    }

    /**
     * Stop monitoring devices.
     */
    public void stop() {
        GSTDEVICEMONITOR_API.gst_device_monitor_stop(this);
    }

    /**
     * Gets a list of devices from all of the relevant monitors. This may
     * actually probe the hardware if the monitor is not currently started.
     * 
     * @return list of {@link Device}
     */
    public List<Device> getDevices() {
        GList glist = GSTDEVICEMONITOR_API.gst_device_monitor_get_devices(this);
        List<Device> list = new ArrayList<>();

        GList next = glist;
        while (next != null) {
            if (next.data != null) {
                Device dev = new Device(Natives.initializer(next.data, false, true));
                list.add(dev);
            }
            next = next.next();
        }

        return list;
    }
}
