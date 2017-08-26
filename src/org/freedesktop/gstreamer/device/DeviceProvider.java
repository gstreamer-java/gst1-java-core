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
package org.freedesktop.gstreamer.device;

import java.util.ArrayList;
import java.util.List;
import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.Plugin;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GlibAPI;
import org.freedesktop.gstreamer.lowlevel.GstDeviceProviderAPI;

import static org.freedesktop.gstreamer.lowlevel.GstDeviceProviderAPI.GSTDEVICEPROVIDER_API;

/**
 *
 * @author andres
 */
public class DeviceProvider extends GstObject {
    public static final String GTYPE_NAME = "GstDeviceProvider";
    
    public DeviceProvider(Initializer init) {
        super(init);
    }
    
    public boolean canMonitor() {
        return GSTDEVICEPROVIDER_API.gst_device_provider_can_monitor(this);
    }

    public void addMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass, String key, String value) {
        GSTDEVICEPROVIDER_API.gst_device_provider_class_add_metadata(klass, key, value);        
    }
    
    public void addStaticMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass, String key, String value) {
        GSTDEVICEPROVIDER_API.gst_device_provider_class_add_static_metadata(klass, key, value);
    }
    
    public String getMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass, String key) {
        return GSTDEVICEPROVIDER_API.gst_device_provider_class_get_metadata(klass, key);
    }
    
    public void setMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass,
                            String longname, String classification, String description, String author) {
        GSTDEVICEPROVIDER_API.gst_device_provider_class_set_metadata(klass, longname, classification, description, author);
    }
    
    public void setStaticMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass,
                                  String longname, String classification, String description, String author) {
        GSTDEVICEPROVIDER_API.gst_device_provider_class_set_static_metadata(klass, longname, classification, description, author);
    }
    
    public void add(Device device) {
        GSTDEVICEPROVIDER_API.gst_device_provider_device_add(this, device);
    }
    
    public void remove(Device device) {
        GSTDEVICEPROVIDER_API.gst_device_provider_device_remove(this, device);  
    }
    
    public Bus getBus() {
        return GSTDEVICEPROVIDER_API.gst_device_provider_get_bus(this);
    }
    
    public List<Device> getDevices() {
        GlibAPI.GList glist = GSTDEVICEPROVIDER_API.gst_device_provider_get_devices(this);        
        List<Device> list = new ArrayList<Device>();

        GlibAPI.GList next = glist;
        while (next != null) {
            if (next.data != null) {
                Device dev = new Device(initializer(next.data, true, true));
                list.add(dev);
            }
            next = next.next();
        }
        
        return list;        
    }
    
    public DeviceProviderFactory getFactory() {
        return GSTDEVICEPROVIDER_API.gst_device_provider_get_factory(this);
    }

    public boolean register(Plugin plugin, String name, int rank, GType type) {
        return GSTDEVICEPROVIDER_API.gst_device_provider_register(plugin, name, rank, type);
    }
    
    public boolean start() {
        return GSTDEVICEPROVIDER_API.gst_device_provider_start(this);
    }

    public void stop() {
        GSTDEVICEPROVIDER_API.gst_device_provider_stop(this);
    }    
}
