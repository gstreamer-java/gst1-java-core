/*
 * Copyright (c) 2020 Petr Lastovka
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
 *
 */
package org.freedesktop.gstreamer.video;

import org.freedesktop.gstreamer.glib.NativeFlags;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import org.freedesktop.gstreamer.lowlevel.GstVideoAPI;

public class VideoTimeCodeConfigTest {
    private GstVideoAPI.GstVideoTimeCodeConfigStruct origStruct;
    private VideoTimeCodeConfig codeConfig;


    @Before
    public void setUp() {
        origStruct = new GstVideoAPI.GstVideoTimeCodeConfigStruct();
        origStruct.fps_d = 25;
        origStruct.fps_n = 1;
        origStruct.flags = VideoTimeCodeFlags.GST_VIDEO_TIME_CODE_FLAGS_DROP_FRAME.intValue();
        origStruct.write();

        codeConfig = new VideoTimeCodeConfig(origStruct);
    }

    @Test
    public void testGetTimeCodeFlags() {
        assertEquals(origStruct.flags, NativeFlags.toInt(codeConfig.getFlags()));
    }

    @Test
    public void testGetFramerateNumerator() {
        assertEquals(origStruct.fps_n, codeConfig.getNumerator());
    }

    @Test
    public void testGetFramerateDenominator() {
        assertEquals(origStruct.fps_d, codeConfig.getDenominator());
    }
}