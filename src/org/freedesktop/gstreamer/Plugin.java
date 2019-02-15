/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2009 Levente Farkas
 * Copyright (C) 2007 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wtay@chello.be>
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
package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GstPluginAPI.GSTPLUGIN_API;

/**
 * Container for features loaded from a shared object module
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPlugin.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPlugin.html</a>
 * <p>
 * GStreamer is extensible, so {@link Element} instances can be loaded at
 * runtime. A plugin system can provide one or more of the basic GStreamer
 * {@link PluginFeature} subclasses.
 * <p>
 * A plugin should export a symbol <code>gst_plugin_desc</code> that is a struct
 * of type GstPluginDesc. the plugin loader will check the version of the core
 * library the plugin was linked against and will create a new Plugin. It will
 * then call the #GstPluginInitFunc function that was provided in the
 * <symbol>gst_plugin_desc</symbol>.
 * <p>
 * Once you have a handle to a #GstPlugin (e.g. from the #GstRegistryPool), you
 * can add any object that subclasses #GstPluginFeature.
 * <p>
 * Use gst_plugin_find_feature() and gst_plugin_get_feature_list() to find
 * features in a plugin.
 * <p>
 * Usually plugins are always automatically loaded so you don't need to call
 * {@link #loadByName} explicitly to bring it into memory. There are options to
 * statically link plugins to an app or even use GStreamer without a plugin
 * repository in which case {@link #loadByName} can be needed to bring the
 * plugin into memory.
 * <p>
 * @see PluginFeature
 * @see ElementFactory
 */
public class Plugin extends GstObject {

    public static final String GTYPE_NAME = "GstPlugin";

    /**
     * Creates a new instance of GstElement
     *
     * @param init internal initialization data.
     */
    Plugin(Initializer init) {
        super(init);
    }

    /**
     * Load the named plugin.
     *
     * @param pluginName
     * @return A new Plugin reference if the plugin was loaded, else null.
     */
    public static Plugin loadByName(String pluginName) {
        return GSTPLUGIN_API.gst_plugin_load_by_name(pluginName);
    }

    /**
     * Get the short name of the plugin.
     *
     * @return the name of the plugin.
     */
    @Override
    public String getName() {
        return GSTPLUGIN_API.gst_plugin_get_name(this);
    }

    /**
     * Get the long descriptive name of the plugin.
     *
     * @return The long name of the plugin.
     */
    public String getDescription() {
        return GSTPLUGIN_API.gst_plugin_get_description(this);
    }

    /**
     * Get the filename of the plugin.
     *
     * @return The filename of the plugin.
     */
    public String getFilename() {
        return GSTPLUGIN_API.gst_plugin_get_filename(this);
    }

    /**
     * Get the version of the plugin.
     *
     * @return The version of the plugin.
     */
    public String getVersion() {
        return GSTPLUGIN_API.gst_plugin_get_version(this);
    }

    /**
     * Get the license of the plugin.
     *
     * @return The license of the plugin.
     */
    public String getLicense() {
        return GSTPLUGIN_API.gst_plugin_get_license(this);
    }

    /**
     * Get the source module the plugin belongs to.
     *
     * @return The source of the plugin.
     */
    public String getSource() {
        return GSTPLUGIN_API.gst_plugin_get_source(this);
    }

    /**
     * Get the package the plugin belongs to.
     *
     * @return The package of the plugin.
     */
    public String getPackage() {
        return GSTPLUGIN_API.gst_plugin_get_package(this);
    }

    /**
     * Get the URL where the plugin comes from.
     *
     * @return The origin of the plugin.
     */
    public String getOrigin() {
        return GSTPLUGIN_API.gst_plugin_get_origin(this);
    }

    /**
     * Get the release date (and possibly time) in form of a string, if
     * available.
     *
     * For normal GStreamer plugin releases this will usually just be a date in
     * the form of "YYYY-MM-DD", while pre-releases and builds from git may
     * contain a time component after the date as well, in which case the string
     * will be formatted like "YYYY-MM-DDTHH:MMZ" (e.g.
     * "2012-04-30T09:30Z").Test
     */
    public String getReleaseDateString() {
        return GSTPLUGIN_API.gst_plugin_get_release_date_string(this);
    }

    /**
     * Queries if the plugin is loaded into memory.
     *
     * @return true if it is loaded, false otherwise.
     */
    public boolean isLoaded() {
        return GSTPLUGIN_API.gst_plugin_is_loaded(this);
    }

//    /**
//     * Ensures this plugin is loaded.
//     *
//     * @return a potentially new <tt>Plugin</tt> reference.
//     */
//    public Plugin load() {
//        return GSTPLUGIN_API.gst_plugin_load(this);
//    }
}
