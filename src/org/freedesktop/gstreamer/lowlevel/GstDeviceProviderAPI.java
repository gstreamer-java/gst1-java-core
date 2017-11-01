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
import java.util.Arrays;
import java.util.List;
import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Plugin;
import org.freedesktop.gstreamer.device.Device;
import org.freedesktop.gstreamer.device.DeviceProvider;
import org.freedesktop.gstreamer.device.DeviceProviderFactory;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;

/**
 *
 * http://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer/html/gstreamer-GstDeviceProvider.html
 */
public interface GstDeviceProviderAPI extends com.sun.jna.Library {
    GstDeviceProviderAPI GSTDEVICEPROVIDER_API = GstNative.load(GstDeviceProviderAPI.class);
    
    boolean gst_device_provider_can_monitor(DeviceProvider provider);
    void gst_device_provider_class_add_metadata(GstDeviceProviderClass klass,
                                                String key, String value);    
    void gst_device_provider_class_add_static_metadata(GstDeviceProviderClass klass,
                                                       String key, String  value);
    String gst_device_provider_class_get_metadata(GstDeviceProviderClass klass, String key);
    void gst_device_provider_class_set_metadata(GstDeviceProviderClass klass,
                                                String longname, String classification, String description, String author);
    void gst_device_provider_class_set_static_metadata(GstDeviceProviderClass klass,
                                                       String longname, String classification, String description, String author);
    void gst_device_provider_device_add(DeviceProvider provider, Device device);
    void gst_device_provider_device_remove(DeviceProvider provider, Device device);
    Bus gst_device_provider_get_bus(DeviceProvider provider);
    GList gst_device_provider_get_devices(DeviceProvider provider);
    DeviceProviderFactory gst_device_provider_get_factory(DeviceProvider provider);
    boolean gst_device_provider_register(Plugin plugin, String name, int rank, GType type);
    boolean gst_device_provider_start(DeviceProvider provider);
    void gst_device_provider_stop(DeviceProvider provider);
        
    public static final class GstDeviceProviderClass extends com.sun.jna.Structure {
        public GObjectAPI.GObjectClass parent_class;
        public volatile Pointer factory;
        public volatile Pointer probe;
        public volatile Pointer start;
        public volatile Pointer provider;        
        public volatile Pointer metadata;
        public volatile Pointer[] _gst_reserved = new Pointer[GstAPI.GST_PADDING];        
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_class", "factory",
                "probe", "start", "provider",
                "metadata", "_gst_reserved"
            });
        }
    }
}
