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
package org.freedesktop.gstreamer.timecode;

import org.freedesktop.gstreamer.lowlevel.GstMetaApi;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class VideoTimeCodeTest {


    private GstMetaApi.GstVideoTimeCodeStruct timeCodeStruct;
    private GstMetaApi.GstVideoTimeCodeConfigStruct.ByValue configStruct;

    private VideoTimeCode timeCode;

    @Before
    public void setUp() {
        timeCodeStruct = new GstMetaApi.GstVideoTimeCodeStruct();
        configStruct = new GstMetaApi.GstVideoTimeCodeConfigStruct.ByValue();

        // 01:02:03:04
        timeCodeStruct.hours = 1;
        timeCodeStruct.minutes = 2;
        timeCodeStruct.seconds = 3;
        timeCodeStruct.frames = 4;

        timeCodeStruct.field_count = 55;

        // config
        timeCodeStruct.config = configStruct;

        timeCodeStruct.write();
        timeCode = new VideoTimeCode(timeCodeStruct.getPointer());


    }

    @Test
    public void testGetTCConfig() {
        assertNotNull(timeCode.getTCConfig());
    }

    @Test
    public void testGetHours() {
        assertEquals(1,timeCode.getHours());
    }

    @Test
    public void testGetMinutes() {
        assertEquals(2,timeCode.getMinutes());
    }

    @Test
    public void testGetSeconds() {
        assertEquals(3,timeCode.getSeconds());
    }

    @Test
    public void testGetFrames() {
        assertEquals(4,timeCode.getFrames());
    }
}