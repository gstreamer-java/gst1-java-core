package org.freedesktop.gstreamer.timecode;

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.lowlevel.GstMetaApi;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
public class GstVideoTimeCodeConfigTest {
    private GstMetaApi.GstVideoTimeCodeConfigStruct origStruct;
    private GstVideoTimeCodeConfig codeConfig;


    @Before
    public void setUp() {
        origStruct = new GstMetaApi.GstVideoTimeCodeConfigStruct();
        origStruct.fps_d = 25;
        origStruct.fps_n = 1;
        origStruct.flags = GstVideoTimeCodeFlags.GST_VIDEO_TIME_CODE_FLAGS_DROP_FRAME;
        origStruct.write();

        codeConfig = new GstVideoTimeCodeConfig(origStruct.getPointer());
    }

    @Test
    public void testGetTimeCodeFlags() {
        assertEquals(origStruct.flags, codeConfig.getTimeCodeFlags());
    }

    @Test
    public void testGetFramerateNumerator() {
        assertEquals(origStruct.fps_n, codeConfig.getFramerateNumerator());
    }

    @Test
    public void testGetFramerateDenominator() {
        assertEquals(origStruct.fps_d, codeConfig.getFramerateDenominator());
    }
}