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
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;

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
         * @param user_data the user_data that has been passed on e.g. {@link GstRegistryAPI#gst_registry_feature_filter}
         * @return true for a positive match, false otherwise
         */
        boolean callback(Plugin plugin, Pointer user_data);
    }

    
    static interface PluginFeatureFilter extends GstCallback {
        /**
         * A function that can be used with e.g. gst_registry_feature_filter()
         * to get a list of pluginfeature that match certain criteria.
         * @param feature the pluginfeature to check
         * @param user_data the user_data that has been passed on e.g. {@link GstRegistryAPI#gst_registry_feature_filter}
         * @return true if this plugin feature is accepted.
         */
        boolean callback(PluginFeature feature, Pointer user_data);
    }

    /* normal GObject stuff */
    GType gst_registry_get_type();
    /* registry_get_default returns a non-refcounted object */
    Pointer gst_registry_get();
    
    GList gst_registry_get_feature_list(Registry registry, GType type);
    int gst_registry_get_feature_list_cookie (Registry registry);
    GList gst_registry_get_feature_list_by_plugin(Registry registry, String name);
    GList gst_registry_get_plugin_list(Registry registry);
    boolean gst_registry_add_plugin(Registry registry, Plugin plugin);
    void gst_registry_remove_plugin(Registry registry, Plugin plugin);
    GList gst_registry_plugin_filter(Registry registry, PluginFilter filter, boolean first, Pointer user_data);
    GList gst_registry_feature_filter(Registry registry, PluginFeatureFilter filter, boolean first, Pointer user_data);
    @CallerOwnsReturn Plugin gst_registry_find_plugin(Registry registry, String name);
    @CallerOwnsReturn PluginFeature gst_registry_find_feature(Registry registry, String name, GType type);
    @CallerOwnsReturn PluginFeature gst_registry_lookup_feature(Registry registry, String name);
    void gst_registry_add_path(Registry registry, String path);
    GList gst_registry_get_path_list(Registry registry);
    boolean gst_registry_scan_path(Registry registry, String path);
    @CallerOwnsReturn Plugin gst_registry_lookup(Registry registry, String filename);
    void gst_registry_remove_feature(Registry  registry, PluginFeature feature);
    boolean gst_registry_add_feature(Registry  registry, PluginFeature feature);
    boolean gst_registry_check_feature_version (Registry registry, String feature_name, int min_major, int min_minor, int min_micro);

}
