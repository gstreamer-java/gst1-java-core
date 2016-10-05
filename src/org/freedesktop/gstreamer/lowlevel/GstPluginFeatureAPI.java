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
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

/**
 * GstPluginFeature functions
 */
public interface GstPluginFeatureAPI extends com.sun.jna.Library {
    GstPluginFeatureAPI GSTPLUGINFEATURE_API = GstNative.load(GstPluginFeatureAPI.class);

    /* normal GObject stuff */
    GType gst_plugin_feature_get_type();

    @CallerOwnsReturn PluginFeature gst_plugin_feature_load(PluginFeature feature);

    void gst_plugin_feature_set_rank(PluginFeature feature, int rank);
    int gst_plugin_feature_get_rank(PluginFeature feature);
    String gst_plugin_feature_get_plugin_name(PluginFeature feature);
    @CallerOwnsReturn Plugin gst_plugin_feature_get_plugin(PluginFeature feature);

    boolean gst_plugin_feature_check_version(PluginFeature feature,
            int min_major, int min_minor, int min_micro);

    void gst_plugin_feature_list_free (GList list);
}
