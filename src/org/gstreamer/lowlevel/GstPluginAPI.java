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

package org.gstreamer.lowlevel;

import org.gstreamer.Plugin;
import org.gstreamer.lowlevel.GlibAPI.GList;
import org.gstreamer.lowlevel.annotations.CallerOwnsReturn;

/**
 * GstPlugin functions
 */
public interface GstPluginAPI extends com.sun.jna.Library {
	GstPluginAPI GSTPLUGIN_API = GstNative.load(GstPluginAPI.class);

    GType gst_plugin_get_type();

    String gst_plugin_get_name(Plugin plugin);
    String gst_plugin_get_description(Plugin plugin);
    String gst_plugin_get_filename(Plugin plugin);
    String gst_plugin_get_version(Plugin plugin);
    String gst_plugin_get_license(Plugin plugin);
    String gst_plugin_get_source(Plugin plugin);
    String gst_plugin_get_package(Plugin plugin);
    String gst_plugin_get_origin(Plugin plugin);
    //GModule *		gst_plugin_get_module		(Plugin plugin);
    boolean gst_plugin_is_loaded(Plugin plugin);
    boolean gst_plugin_name_filter(Plugin plugin, String name);

    //Plugin 		gst_plugin_load_file		(String filename, GError** error);

    @CallerOwnsReturn Plugin gst_plugin_load(Plugin plugin);
    @CallerOwnsReturn Plugin gst_plugin_load_by_name(String name);

    void gst_plugin_list_free(GList list);
}
