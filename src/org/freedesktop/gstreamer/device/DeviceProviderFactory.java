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
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.PluginFeature.Rank;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;

import static org.freedesktop.gstreamer.lowlevel.GstDeviceProviderFactoryAPI.GSTDEVICEPROVIDERFACTORY_API;
import org.freedesktop.gstreamer.lowlevel.GstPluginAPI;

/**
 * A factory for {@link DeviceProvider}
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstDeviceProviderFactory.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstDeviceProviderFactory.html</a>
 * <p>
 * DeviceProviderFactory is used to create instances of device providers. A
 * DeviceProviderfactory can be added to a Plugin as it is also a PluginFeature.
 *
 * Use the {@link #find(java.lang.String) and
 * {@link #get() } functions to create device provider
 * instances or use {@link #getByName(java.lang.String) } as a convenient
 * shortcut.
 */
public class DeviceProviderFactory extends GstObject {

    public static final String GTYPE_NAME = "GstDeviceProviderFactory";

    DeviceProviderFactory(Initializer init) {
        super(init);
    }

    /**
     * Returns the {@link DeviceProvider} of the type defined by the given
     * device provider factory.
     *
     * @return DeviceProvider or NULL if the device provider couldn't be
     * created.
     */
    public DeviceProvider get() {
        return GSTDEVICEPROVIDERFACTORY_API.gst_device_provider_factory_get(this);
    }

    
    /**
     * Get the metadata on factory with key.
     *
     * @param key
     * @return the metadata with key on factory or null when there was no
     * metadata with the given key .
     */
    public String getMetadata(String key) {
        return GSTDEVICEPROVIDERFACTORY_API.gst_device_provider_factory_get_metadata(this, key);
    }

//    @TODO List<String> ??
//    public String[] getMetadataKeys() {
//        return GSTDEVICEPROVIDERFACTORY_API.gst_device_provider_factory_get_metadata_keys(this);
//    }

    /**
     * Check if factory matches all of the given classes
     *
     * @param classes a "/" separate list of classes to match, only match if all
     * classes are matched.
     * @return true if factory matches
     */
    public boolean hasClasses(String classes) {
        return GSTDEVICEPROVIDERFACTORY_API.gst_device_provider_factory_has_classes(this, classes);
    }

//    public boolean hasClasses(String[] classes) {
//        return GSTDEVICEPROVIDERFACTORY_API.gst_device_provider_factory_has_classesv(this, classes);
//    }
    /**
     * Search for an device provider factory of the given name.
     *
     * @param name name of factory to find
     * @return DeviceProviderFactory if found, or null
     */
    public static DeviceProviderFactory find(String name) {
        return GSTDEVICEPROVIDERFACTORY_API.gst_device_provider_factory_find(name);
    }

    /**
     * Returns the device provider of the type defined by the given device
     * provider factory.
     *
     * @param factoryName a named factory to instantiate
     * @return DeviceProvider or NULL if the device provider couldn't be
     * created.
     */
    public static DeviceProvider getByName(String factoryName) {
        return GSTDEVICEPROVIDERFACTORY_API.gst_device_provider_factory_get_by_name(factoryName);
    }
    
    /**
     * Get a list of factories with a rank greater or equal to minrank . The
     * list of factories is returned by decreasing rank.
     * @param minRank minimum rank
     * @return a list of
     */
    public static List<DeviceProviderFactory> getDeviceProviders(Rank minRank) {
        GList glist = GSTDEVICEPROVIDERFACTORY_API.gst_device_provider_factory_list_get_device_providers(minRank);
        List<DeviceProviderFactory> list = new ArrayList<>();
        
        GList next = glist;
        while (next != null) {
            if (next.data != null) {
                DeviceProviderFactory factory =
                        new DeviceProviderFactory(Natives.initializer(next.data, true, true));
                list.add(factory);
            }
            next = next.next();
        }
        
        GstPluginAPI.GSTPLUGIN_API.gst_plugin_list_free(glist);
        
        return list;
    }
//    public GType getDeviceProviderType() {
//        return GSTDEVICEPROVIDERFACTORY_API.gst_device_provider_factory_get_device_provider_type(this);
//    }
}
