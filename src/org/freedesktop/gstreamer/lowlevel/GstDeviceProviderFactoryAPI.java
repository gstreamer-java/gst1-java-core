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

import org.freedesktop.gstreamer.PluginFeature.Rank;
import org.freedesktop.gstreamer.device.DeviceProvider;
import org.freedesktop.gstreamer.device.DeviceProviderFactory;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;

/**
 *
 * http://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer/html/GstDeviceProviderFactory.html
 */
public interface GstDeviceProviderFactoryAPI extends com.sun.jna.Library {
    GstDeviceProviderFactoryAPI GSTDEVICEPROVIDERFACTORY_API = GstNative.load(GstDeviceProviderFactoryAPI.class);
    
    DeviceProviderFactory gst_device_provider_factory_find(String name);
    DeviceProvider gst_device_provider_factory_get(DeviceProviderFactory factory);
    DeviceProvider gst_device_provider_factory_get_by_name(String factoryName);
    GType gst_device_provider_factory_get_device_provider_type(DeviceProviderFactory factory);
    String gst_device_provider_factory_get_metadata(DeviceProviderFactory factory, String key);
    String[] gst_device_provider_factory_get_metadata_keys(DeviceProviderFactory factory);    
    boolean gst_device_provider_factory_has_classes(DeviceProviderFactory factory, String classes);
    boolean gst_device_provider_factory_has_classesv(DeviceProviderFactory factory, String[] classes);
    GList gst_device_provider_factory_list_get_device_providers(Rank minRank);
}
