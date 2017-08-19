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
package org.freedesktop.gstreamer.device;

import java.util.ArrayList;
import java.util.List;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.PluginFeature.Rank;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;
import org.freedesktop.gstreamer.lowlevel.GstDeviceProviderFactoryAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;

/**
 *
 * @author andres
 */
public class DeviceProviderFactory extends GstObject {
    public static final String GTYPE_NAME = "GstDeviceProviderFactory";
    
    private static final GstDeviceProviderFactoryAPI gst = GstNative.load(GstDeviceProviderFactoryAPI.class);

    public DeviceProviderFactory(Initializer init) {
        super(init);
    }
    
    static public DeviceProviderFactory find(String name) {
      return gst.gst_device_provider_factory_find(name);
    }
    
    public DeviceProvider get() {
        return gst.gst_device_provider_factory_get(this);
    }
    
    public DeviceProvider getByName(String factoryName) {
        return gst.gst_device_provider_factory_get_by_name(factoryName);
    }
    
    public GType getDeviceProviderType() {
        return gst.gst_device_provider_factory_get_device_provider_type(this);
    }
    
    public String getMetadata(String key) {
        return gst.gst_device_provider_factory_get_metadata(this, key);
    }
    
    public String[] getMetadataKeys() {
        return gst.gst_device_provider_factory_get_metadata_keys(this);
    }
    
    public boolean hasClasses(String classes) {
        return gst.gst_device_provider_factory_has_classes(this, classes);
    }
    
    public boolean hasClasses(String[] classes) {
        return gst.gst_device_provider_factory_has_classesv(this, classes);
    }

    public List<DeviceProvider> getDeviceProviders(Rank minRank) {
        GList glist = gst.gst_device_provider_factory_list_get_device_providers(minRank);
        List<DeviceProvider> list = new ArrayList<DeviceProvider>();
              
        GList next = glist;
        while (next != null) {
            if (next.data != null) {
                DeviceProvider prov = new DeviceProvider(initializer(next.data, true, true));
                list.add(prov);
            }
            next = next.next();
        }
        
        return list;         
    }
}
