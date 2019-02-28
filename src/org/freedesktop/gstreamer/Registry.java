/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2007 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wtay@chello.be>
 *                    2005 David A. Schleef <ds@schleef.org>
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.freedesktop.gstreamer.glib.Natives;

import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;
import static org.freedesktop.gstreamer.lowlevel.GstPluginAPI.GSTPLUGIN_API;
import static org.freedesktop.gstreamer.lowlevel.GstRegistryAPI.GSTREGISTRY_API;
import static org.freedesktop.gstreamer.lowlevel.GstPluginFeatureAPI.GSTPLUGINFEATURE_API;

/**
 * Abstract base class for management of {@link Plugin} objects.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstRegistry.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstRegistry.html</a>
 * <p>
 * One registry holds the metadata of a set of plugins.
 * All registries build the RegistryPool.
 * <p>
 * <b>Design</b>:
 * <p>
 * The Registry object is a list of plugins and some methods for dealing
 * with them. Plugins are matched 1-1 with a file on disk, and may or may
 * not be loaded at a given time. There may be multiple Registry objects,
 * but the "default registry" is the only object that has any meaning to the
 * core.
 * <p>
 * The registry.xml file is actually a cache of plugin information. This is
 * unlike versions prior to 0.10, where the registry file was the primary source
 * of plugin information, and was created by the gst-register command.
 * <p>
 * The primary source, at all times, of plugin information is each plugin file
 * itself. Thus, if an application wants information about a particular plugin,
 * or wants to search for a feature that satisfies given criteria, the primary
 * means of doing so is to load every plugin and look at the resulting
 * information that is gathered in the default registry. Clearly, this is a time
 * consuming process, so we cache information in the registry.xml file.
 * <p>
 * On startup, plugins are searched for in the plugin search path. This path can
 * be set directly using the GST_PLUGIN_PATH environment variable. The registry
 * file is loaded from ~/.gstreamer-$GST_MAJORMINOR/registry-$ARCH.xml or the
 * file listed in the GST_REGISTRY environment variable. The only reason to change the
 * registry location is for testing.
 * <p>
 * For each plugin that is found in the plugin search path, there could be 3
 * possibilities for cached information:
 * <ol>
 *   <li>
 *     <para>the cache may not contain information about a given file.</para>
 *   </li>
 *   <li>
 *     <para>the cache may have stale information.</para>
 *   </li>
 *   <li>
 *     <para>the cache may have current information.</para>
 *   </li>
 * </ol>
 * <p>
 * In the first two cases, the plugin is loaded and the cache updated. In
 * addition to these cases, the cache may have entries for plugins that are not
 * relevant to the current process. These are marked as not available to the
 * current process. If the cache is updated for whatever reason, it is marked
 * dirty.
 * <p>
 * A dirty cache is written out at the end of initialization. Each entry is
 * checked to make sure the information is minimally valid. If not, the entry is
 * simply dropped.
 * <p>
 * <bold>Implementation notes:</bold>
 * <p>
 * The "cache" and "default registry" are different concepts and can represent
 * different sets of plugins. For various reasons, at init time, the cache is
 * stored in the default registry, and plugins not relevant to the current
 * process are marked with the %GST_PLUGIN_FLAG_CACHED bit. These plugins are
 * removed at the end of intitialization.
 */
public class Registry extends GstObject {
    public static final String GTYPE_NAME = "GstRegistry";

    /** Creates a new instance of Registry */
    Registry(Initializer init) {
        super(init);
    }
    
    /**
     * Find a plugin in the registry.
     * 
     * @param name The plugin name to find.
     * @return The plugin with the given name or null if the plugin was not found.
     */
    public Plugin findPlugin(String name) {
        return GSTREGISTRY_API.gst_registry_find_plugin(this, name);
    }
    /**
     * Add the plugin to the registry. The plugin-added signal will be emitted.
     *
     * @param plugin the {@link Plugin} to add
     * @return true on success.
     */
    public boolean addPlugin(Plugin plugin) {
        return GSTREGISTRY_API.gst_registry_add_plugin(this, plugin);
    }
    
    /**
     * Remove a plugin from the registry.
     * 
     * @param plugin The plugin to remove.
     */
    public void removePlugin(Plugin plugin) {
        GSTREGISTRY_API.gst_registry_remove_plugin(this, plugin);
    }
    
//    /**
//     * Find the {@link PluginFeature} with the given name and type in the registry.
//     * 
//     * @param name The name of the plugin feature to find.
//     * @param type The type of the plugin feature to find.
//     * @return The pluginfeature with the given name and type or null
//     * if the plugin was not found.
//     */
//    public PluginFeature findPluginFeature(String name, GType type) {
//        return GSTREGISTRY_API.gst_registry_find_feature(this, name, type);
//    }
    
    /**
     * Find a {@link PluginFeature} by name in the registry.
     *
     * @param name The name of the plugin feature to find.
     * @return The {@link PluginFeature} or null if not found.
     */
    public PluginFeature lookupFeature(String name) {
        return GSTREGISTRY_API.gst_registry_lookup_feature(this, name);
    }
    
    /**
     * Get a list of all plugins registered in the registry. 
     *
     * @return a List of {@link Plugin}
     */
    public List<Plugin> getPluginList() {

        GList glist = GSTREGISTRY_API.gst_registry_get_plugin_list(this);      
        List<Plugin> list = objectList(glist, Plugin.class);
        GSTPLUGIN_API.gst_plugin_list_free(glist);
        return list;
    }
    
    /**
     * Get a subset of the Plugins in the registry, filtered by filter.
     * 
     * @param filter the filter to use
     * @return A List of {@link Plugin} objects that match the filter.
     */
    public List<Plugin> getPluginList(final PluginFilter filter) {
        return getPluginList().stream().filter(filter::accept).collect(Collectors.toList());
    }
    
//    /**
//     * Retrieves a list of {@link PluginFeature} of the {@link Plugin} type.
//     * 
//     * @param type The plugin type.
//     * @return a List of {@link PluginFeature} for the plugin type.
//     */
//    public List<PluginFeature> getPluginFeatureListByType(GType type) {
//        GList glist = GSTREGISTRY_API.gst_registry_get_feature_list(this, type);
//        List<PluginFeature> list = objectList(glist, PluginFeature.class);
//        GSTPLUGINFEATURE_API.gst_plugin_feature_list_free(glist);
//        return list;
//    }
    
    /**
     * Retrieves a list of {@link PluginFeature} of the named {@link Plugin}.
     * 
     * @param name The plugin name.
     * @return a List of {@link PluginFeature} for the named plugin.
     */
    public List<PluginFeature> getPluginFeatureListByPlugin(String name) {
        GList glist = GSTREGISTRY_API.gst_registry_get_feature_list_by_plugin(this, name);
        List<PluginFeature> list = objectList(glist, PluginFeature.class);
        GSTPLUGINFEATURE_API.gst_plugin_feature_list_free(glist);
        return list;
    }
    
    /**
     * Add the given path to the registry. The syntax of the
     * path is specific to the registry. If the path has already been
     * added, do nothing.
     *
     * @param path The path to add to the registry.
     * @return true if the registry changed.
     */
    public boolean scanPath(String path) {
        return GSTREGISTRY_API.gst_registry_scan_path(this, path);
    }
    
    /**
     * Build a {@link java.util.List} of {@link GstObject} from the native GList.
     * @param glist The native list to get the objects from.
     * @param objectClass The proxy class to wrap the list elements in.
     * @return The converted list.
     */
    private <T extends GstObject> List<T> objectList(GList glist, Class<T> objectClass) {
        List<T> list = new ArrayList<T>();
        GList next = glist;
        while (next != null) {
            if (next.data != null) {
                list.add(Natives.objectFor(next.data, objectClass, true, true));
            }
            next = next.next();   
        }
        return list;
    }
    
    public static interface PluginFilter {
        public boolean accept(Plugin plugin);
    }
    
//    public static interface PluginFeatureFilter {
//        public boolean accept(PluginFeature feature);
//    }
//    
    /**
     * Retrieves the default registry. 
     * 
     * @return The default Registry.
     */
    public static Registry get() {
        // Need to handle the return value here, as it is a persistent object
        // i.e. the java proxy should not dispose of the underlying object when finalized
        return Natives.objectFor(GSTREGISTRY_API.gst_registry_get(), Registry.class,
                false, false);
    }
    
}
