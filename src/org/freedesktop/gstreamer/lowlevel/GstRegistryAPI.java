/* 
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007, 2008 Wayne Meissner
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

import org.freedesktop.gstreamer.Plugin;
import org.freedesktop.gstreamer.PluginFeature;
import org.freedesktop.gstreamer.Registry;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

/**
 * GstRegistry functions
 */
public interface GstRegistryAPI extends com.sun.jna.Library {
	GstRegistryAPI GSTREGISTRY_API = GstNative.load(GstRegistryAPI.class);

    /* function for filters */
    static interface PluginFilter extends GstCallback {
        /**
         *
         * A function that can be used with e.g. gst_registry_plugin_filter()
         * to get a list of plugins that match certain criteria.
         *
         * @param plugin the plugin to check
         * @return true for a positive match, false otherwise
         */
        boolean callback(Plugin plugin);
    }

    
    static interface PluginFeatureFilter extends GstCallback {
        /**
         * A function that can be used with e.g. gst_registry_feature_filter()
         * to get a list of pluginfeature that match certain criteria.
         * @param feature the pluginfeature to check
         * @return true if this plugin feature is accepted.
         */
        boolean callback(PluginFeature feature);
    }

    /* normal GObject stuff */
    GType gst_registry_get_type();
    /* registry_get_default returns a non-refcounted object */
    Pointer gst_registry_get_default();
    boolean gst_registry_scan_path(Registry registry, String path);


    boolean gst_registry_add_plugin(Registry registry, Plugin plugin);
    void gst_registry_remove_plugin(Registry registry, Plugin plugin);
    boolean gst_registry_add_feature(Registry  registry, PluginFeature feature);
    void gst_registry_remove_feature(Registry  registry, PluginFeature feature);
    
    @CallerOwnsReturn Plugin gst_registry_find_plugin(Registry registry, String name);
    @CallerOwnsReturn PluginFeature gst_registry_find_feature(Registry registry, String name, GType type);

    @CallerOwnsReturn Plugin gst_registry_lookup(Registry registry, String filename);
    @CallerOwnsReturn PluginFeature gst_registry_lookup_feature(Registry registry, String name);


    boolean gst_registry_binary_read_cache(Registry registry, String location);
    boolean gst_registry_binary_write_cache(Registry registry, String location);

}
