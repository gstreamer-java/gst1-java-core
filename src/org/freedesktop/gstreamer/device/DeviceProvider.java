/*
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
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.Plugin;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GlibAPI;
import org.freedesktop.gstreamer.lowlevel.GstDeviceProviderAPI;

import static org.freedesktop.gstreamer.lowlevel.GstDeviceProviderAPI.GSTDEVICEPROVIDER_API;

/**
 * A device provider.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/gstreamer-GstDeviceProvider.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/gstreamer-GstDeviceProvider.html</a>
 * <p>
 * A DeviceProvider subclass is provided by a plugin that handles devices if
 * there is a way to programatically list connected devices. It can also
 * optionally provide updates to the list of connected devices.
 * <p>
 * Each DeviceProvider subclass is a singleton, a plugin should normally provide
 * a single subclass for all devices.
 * <p>
 * Applications would normally use a {@link DeviceMonitor} to monitor devices
 * from all relevant providers.
 */
public class DeviceProvider extends GstObject {

    public static final String GTYPE_NAME = "GstDeviceProvider";

    DeviceProvider(Initializer init) {
        super(init);
    }

    /**
     * Whether the provider can monitor.
     *
     * @return true if the provider can monitor
     */
    public boolean canMonitor() {
        return GSTDEVICEPROVIDER_API.gst_device_provider_can_monitor(this);
    }

//    /**
//     * Set key with value as metadata in klass.
//     *
//     * @param klass class to set metadata for
//     * @param key the key to set
//     * @param value the value to set
//     */
//    public void addMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass, String key, String value) {
//        GSTDEVICEPROVIDER_API.gst_device_provider_class_add_metadata(klass, key, value);
//    }
//
//    /**
//     * Get metadata with key in klass.
//     *
//     * @param klass class to get metadata for
//     * @param key the key to get
//     * @return the metadata for key
//     */
//    public String getMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass, String key) {
//        return GSTDEVICEPROVIDER_API.gst_device_provider_class_get_metadata(klass, key);
//    }

//    public void setMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass,
//            String longname, String classification, String description, String author) {
//        GSTDEVICEPROVIDER_API.gst_device_provider_class_set_metadata(klass, longname, classification, description, author);
//    }
//    public void add(Device device) {
//        GSTDEVICEPROVIDER_API.gst_device_provider_device_add(this, device);
//    }
//
//    public void remove(Device device) {
//        GSTDEVICEPROVIDER_API.gst_device_provider_device_remove(this, device);
//    }
    /**
     * Get the {@link Bus} of this DeviceProvider
     *
     * @return bus
     */
    public Bus getBus() {
        return GSTDEVICEPROVIDER_API.gst_device_provider_get_bus(this);
    }

    /**
     * Gets a list of {@link Device} that this provider understands. This may
     * actually probe the hardware if the provider is not currently started.
     *
     * @return device list
     */
    public List<Device> getDevices() {
        GlibAPI.GList glist = GSTDEVICEPROVIDER_API.gst_device_provider_get_devices(this);
        List<Device> list = new ArrayList<Device>();

        GlibAPI.GList next = glist;
        while (next != null) {
            if (next.data != null) {
                Device dev = new Device(Natives.initializer(next.data, true, true));
                list.add(dev);
            }
            next = next.next();
        }
        

        return list;
    }

    /**
     * Retrieves the factory that was used to create this device provider.
     *
     * @return the {@link DeviceProviderFactory} used for creating this device
     * provider.
     */
    public DeviceProviderFactory getFactory() {
        return GSTDEVICEPROVIDER_API.gst_device_provider_get_factory(this);
    }

//    @TODO rethink this to not require GType
//    /**
//     * Create a new device providerfactory capable of instantiating objects of
//     * the type and add the factory to plugin.
//     * 
//     * @param plugin {@link Plugin} to register the device provider with, or NULL for a static device provider. 
//     * @param name name of device providers of this type
//     * @param rank rank of device provider (higher rank means more importance when autoplugging)
//     * @param type 
//     * @return
//     */
//    public boolean register(Plugin plugin, String name, int rank, GType type) {
//        return GSTDEVICEPROVIDER_API.gst_device_provider_register(plugin, name, rank, type);
//    }

    /**
     * Starts providering the devices. This will cause GST_MESSAGE_DEVICE_ADDED
     * and GST_MESSAGE_DEVICE_REMOVED messages to be posted on the provider's
     * bus when devices are added or removed from the system.
     * <p>
     * Since the DeviceProvider is a singleton, start may already have been
     * called by another user of the object, {@link #stop() } needs to be called
     * the same number of times.
     *
     * @return true if the device providering could be started.
     */
    public boolean start() {
        return GSTDEVICEPROVIDER_API.gst_device_provider_start(this);
    }

    /**
     * Decreases the use-count by one. If the use count reaches zero, this
     * DeviceProvider will stop providering the devices. This needs to be called
     * the same number of times that {@link #start() } was called.
     */
    public void stop() {
        GSTDEVICEPROVIDER_API.gst_device_provider_stop(this);
    }
}
