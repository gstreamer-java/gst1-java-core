/*
 * Copyright (c) 2007 Wayne Meissner
 *
 * This file is part of gstreamer-java.
 *
 * gstreamer-java is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * gstreamer-java is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with gstreamer-java.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.gstreamer;

import java.util.List;

import org.freedesktop.gstreamer.PluginFeature.Rank;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jna.Platform;

/**
 *
 * @author wayne
 */
public class RegistryTest {

    public RegistryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("RegistryTest", new String[] {});
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        Gst.deinit();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void getDefault() {
        Registry registry = Registry.getInstance();
        Assert.assertNotNull("Registry.getDefault() returned null", registry);
    }
    @Test
    public void listPlugins() {
        final String PLUGIN = "vorbis"; // Use something that is likely to be there
        Registry registry = Registry.getInstance();
        // Ensure some plugins are loaded
        ElementFactory.make("playbin", "test");
        ElementFactory.make("vorbisdec", "vorbis");
        ElementFactory.make("decodebin", "decoder");
        List<Plugin> plugins = registry.getPluginList();
        Assert.assertFalse("No plugins found", plugins.isEmpty());
        boolean pluginFound = false;
        for (Plugin p : plugins) {
//            System.out.println("Found plugin: " + p.getName());
            if (p.getName().equals(PLUGIN)) {
                pluginFound = true;
            }
        }
        Assert.assertTrue(PLUGIN + " plugin not found", pluginFound);
    }
    @Test
    public void filterPlugins() {
        if (Platform.isWindows()) {
            return; // gst_registry_plugin_filter doesn't exist on windows
        }
        final String PLUGIN = "vorbis"; // Use something that is likely to be there
        Registry registry = Registry.getInstance();
        // Ensure some plugins are loaded
        ElementFactory.make("playbin", "test");
        ElementFactory.make("vorbisdec", "vorbis");
        ElementFactory.make("decodebin", "decoder");
        final boolean[] filterCalled = { false };
        List<Plugin> plugins = registry.getPluginList(new Registry.PluginFilter() {

            @Override
			public boolean accept(Plugin plugin) {
                filterCalled[0] = true;
                return plugin.getName().equals(PLUGIN);
            }
        }, true);
        Assert.assertFalse("No plugins found", plugins.isEmpty());
        Assert.assertTrue("PluginFilter not called", filterCalled[0]);
        Assert.assertEquals("Plugin list should contain 1 item", 1, plugins.size());
        Assert.assertEquals(PLUGIN + " plugin not found", PLUGIN, plugins.get(0).getName());
    }
    @Test
    public void listPluginFeatures() {
        final String PLUGIN = "vorbis"; // Use something that is likely to be there
        final String FEATURE = "vorbisdec";
        Registry registry = Registry.getInstance();
        // Ensure some plugins are loaded
        ElementFactory.make("playbin", "test");
        ElementFactory.make("vorbisdec", "vorbis");
        ElementFactory.make("decodebin", "decoder");
        List<PluginFeature> features = registry.getPluginFeatureListByPlugin(PLUGIN);
        Assert.assertFalse("No plugin features found", features.isEmpty());
        boolean pluginFound = false;
        for (PluginFeature p : features) {
//            System.out.println("Found plugin feature " + p.getName());
            if (p.getName().equals(FEATURE)) {
                pluginFound = true;
            }
        }
        Assert.assertTrue(PLUGIN + " plugin not found", pluginFound);
    }
    @Test
    public void lookupFeature() {
        @SuppressWarnings("unused")
        PluginFeature f = Registry.getInstance().findPluginFeature("decodebin");
        f.setRank(Rank.GST_RANK_PRIMARY);
        Assert.assertSame("Can't modify rank", Rank.GST_RANK_PRIMARY, f.getRank());
        f.setRank(Rank.GST_RANK_NONE);
        Assert.assertSame("Can't modify rank", Rank.GST_RANK_NONE, f.getRank());
    }
}
