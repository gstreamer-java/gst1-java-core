/* 
 * Copyright (c) 2021 Neil C Smith
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
package org.freedesktop.gstreamer.elements;

import java.net.URI;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.GCTracker;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.util.TestAssumptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for PlayBin.
 */
public class PlayBinTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init(Gst.getVersion(), "PlayBinTest");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        Gst.deinit();
    }

    @Test
    public void testFlags() {
        PlayBin playbin = new PlayBin("playbin");
        Set<PlayFlags> defaultFlags = EnumSet.of(
                PlayFlags.SOFT_COLORBALANCE,
                PlayFlags.DEINTERLACE,
                PlayFlags.SOFT_VOLUME,
                PlayFlags.TEXT,
                PlayFlags.AUDIO,
                PlayFlags.VIDEO
        );
        Set<PlayFlags> flags = playbin.getFlags();
        assertEquals("PlayBin flags not expected defaults", defaultFlags, flags);

        flags.add(PlayFlags.VIS);
        flags.remove(PlayFlags.DEINTERLACE);
        playbin.setFlags(flags);

        flags = playbin.getFlags();
        assertTrue("VIS flag not set", flags.contains(PlayFlags.VIS));
        assertFalse("Deinterlace not removed from playbin flags",
                flags.contains(PlayFlags.DEINTERLACE));

        playbin.dispose();

    }

    @Test
    public void testSourceSetupSignal() throws Exception {

        PlayBin playbin = new PlayBin("playbin", URI.create("appsrc:/"));
        AtomicReference<AppSrc> sourceRef = new AtomicReference<>(null);
        playbin.connect((PlayBin.SOURCE_SETUP) ((p, e) -> {
            if (e instanceof AppSrc) {
                AppSrc appSrc = (AppSrc) e;
                appSrc.setCaps(Caps.fromString("video/x-raw, format=xRGB, width=640, height=480"));
                sourceRef.set(appSrc);
            }
        }));
        playbin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
        playbin.play();
        playbin.getState(200, TimeUnit.MILLISECONDS);

        AppSrc src = sourceRef.getAndSet(null);
        assertNotNull(src);

        GCTracker sourceTracker = new GCTracker(src);
        GCTracker playbinTracker = new GCTracker(playbin);

        playbin.stop();

        src = null;
        playbin = null;

        assertTrue("AppSrc not garbage collected", sourceTracker.waitGC());
        assertTrue("AppSrc not destroyed", sourceTracker.waitDestroyed());
        assertTrue("PlayBin not garbage collected", playbinTracker.waitGC());
        assertTrue("PlayBin not destroyed", playbinTracker.waitDestroyed());

    }

    @Test
    public void testElementSetupSignal() throws Exception {

        TestAssumptions.requireGstVersion(1, 10);
        PlayBin playbin = new PlayBin("playbin", URI.create("appsrc:/"));
        AtomicReference<AppSrc> sourceRef = new AtomicReference<>(null);
        playbin.connect((PlayBin.ELEMENT_SETUP) ((p, e) -> {
            if (e instanceof AppSrc) {
                AppSrc appSrc = (AppSrc) e;
                sourceRef.set(appSrc);
            }
        }));
        playbin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
        playbin.play();
        playbin.getState(200, TimeUnit.MILLISECONDS);

        AppSrc src = sourceRef.getAndSet(null);
        assertNotNull(src);

        GCTracker sourceTracker = new GCTracker(src);
        GCTracker playbinTracker = new GCTracker(playbin);

        playbin.stop();

        src = null;
        playbin = null;

        assertTrue("AppSrc not garbage collected", sourceTracker.waitGC());
        assertTrue("AppSrc not destroyed", sourceTracker.waitDestroyed());
        assertTrue("PlayBin not garbage collected", playbinTracker.waitGC());
        assertTrue("PlayBin not destroyed", playbinTracker.waitDestroyed());

    }

}
