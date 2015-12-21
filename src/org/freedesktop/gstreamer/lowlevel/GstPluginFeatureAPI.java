/*
 * Copyright (c) 2015 Christophe Lafolet
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

import java.util.Arrays;
import java.util.List;

import org.freedesktop.gstreamer.PluginFeature;
import org.freedesktop.gstreamer.PluginFeature.Rank;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

/**
 * GstPluginFeature functions
 */
public interface GstPluginFeatureAPI extends com.sun.jna.Library {
    GstPluginFeatureAPI GSTPLUGINFEATURE_API = GstNative.load(GstPluginFeatureAPI.class);

    /* normal GObject stuff */
    GType gst_plugin_feature_get_type();

    @CallerOwnsReturn PluginFeature gst_plugin_feature_load(PluginFeature feature);
    public static final class TypeNameData extends com.sun.jna.Structure {
        public String name;
        public GType type;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "name", "type"
            });
        }
    }
    boolean gst_plugin_feature_type_name_filter(PluginFeature feature, TypeNameData data);

    void gst_plugin_feature_set_rank(PluginFeature feature, Rank rank);
    void gst_plugin_feature_set_name(PluginFeature feature, String name);
    Rank gst_plugin_feature_get_rank(PluginFeature feature);

    boolean gst_plugin_feature_check_version(PluginFeature feature,
            int min_major, int min_minor, int min_micro);

}
