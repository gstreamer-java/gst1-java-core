/* 
 * Copyright (C) 2019 Neil C Smith
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

import org.freedesktop.gstreamer.glib.NativeEnum;
import static org.freedesktop.gstreamer.lowlevel.GstObjectAPI.GSTOBJECT_API;
import static org.freedesktop.gstreamer.lowlevel.GstPluginFeatureAPI.GSTPLUGINFEATURE_API;

/**
 * Base class for contents of a {@link Plugin}
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPluginFeature.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPluginFeature.html</a>
 * <p>
 * This is a base class for anything that can be added to a Plugin.
 *
 * @see Plugin
 */
public class PluginFeature extends GstObject {

    public static final String GTYPE_NAME = "GstPluginFeature";

    /**
     * Element priority ranks. Defines the order in which the autoplugger (or
     * similar rank-picking mechanisms, such as e.g.
     * gst_element_make_from_uri()) will choose this element over an alternative
     * one with the same function.
     *
     * These constants serve as a rough guidance for defining the rank of a
     * GstPluginFeature. Any value is valid, including values bigger than
     * GST_RANK_PRIMARY .
     */
    public enum Rank implements NativeEnum<Rank> {

        /**
         * Will be chosen last or not at all.
         */
        NONE(0),

        /**
         * Unlikely to be chosen.
         */
        MARGINAL(64),
        
        /**
         * Likely to be chosen.
         */
        SECONDARY(128),
        
        /**
         * Will be chosen first.
         */
        PRIMARY(256);

        private final int value;

        private Rank(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }
    }

    /**
     * Creates a new instance of PluginFeature
     */
    PluginFeature(Initializer init) {
        super(init);
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Gets the name of a plugin feature.
     *
     * @return The name.
     */
    @Override
    public String getName() {
        return GSTOBJECT_API.gst_object_get_name(this);
    }

    /**
     * Sets the name of the plugin feature, getting rid of the old name if there
     * was one.
     *
     * @param name The name to set.
     */
    @Override
    public boolean setName(String name) {
        GSTOBJECT_API.gst_object_set_name(this, name);
        return true;
    }

    /**
     * Set the rank for the plugin feature. Specifies a rank for a plugin
     * feature, so that autoplugging uses the most appropriate feature.
     *
     * @param rank The rank value - higher number means more priority rank
     */
    public void setRank(int rank) {
        GSTPLUGINFEATURE_API.gst_plugin_feature_set_rank(this, rank);
    }

    /**
     * Set the rank for the plugin feature. Specifies a rank for a plugin
     * feature, so that autoplugging uses the most appropriate feature.
     *
     * @param rank The rank value
     */
    public void setRank(Rank rank) {
        setRank(rank.intValue());
    }

    /**
     * Gets the rank of a plugin feature.
     *
     * @return The rank of the feature.
     */
    public int getRank() {
        return GSTPLUGINFEATURE_API.gst_plugin_feature_get_rank(this);
    }

    /**
     * Checks whether the given plugin feature is at least the required version.
     *
     * @param major Minimum required major version
     * @param minor Minimum required minor version
     * @param micro Minimum required micro version
     * @return true if the plugin feature has at least the required version,
     * otherwise false.
     */
    public boolean checkVersion(int major, int minor, int micro) {
        return GSTPLUGINFEATURE_API.gst_plugin_feature_check_version(this, minor, minor, micro);
    }

    /**
     * Get the name of the plugin that provides this feature.
     *
     * @return the name of the plugin that provides this feature, or NULL if the
     * feature is not associated with a plugin.
     */
    public String getPluginName() {
        return GSTPLUGINFEATURE_API.gst_plugin_feature_get_plugin_name(this);
    }

    /**
     * Get the plugin that provides this feature.
     *
     * @return the plugin that provides this feature, or NULL.
     */
    public Plugin getPlugin() {
        return GSTPLUGINFEATURE_API.gst_plugin_feature_get_plugin(this);
    }
}
