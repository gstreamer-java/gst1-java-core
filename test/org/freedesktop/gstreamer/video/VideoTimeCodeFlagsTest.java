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

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class VideoTimeCodeFlagsTest {

    private final VideoTimeCodeFlags flags;
    private final int intValue;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
//                        {VideoTimeCodeFlags.GST_VIDEO_TIME_CODE_FLAGS_NONE, 0},
                        {VideoTimeCodeFlags.GST_VIDEO_TIME_CODE_FLAGS_DROP_FRAME, 1},
                        {VideoTimeCodeFlags.GST_VIDEO_TIME_CODE_FLAGS_INTERLACED, 2}
                });
    }

    public VideoTimeCodeFlagsTest(VideoTimeCodeFlags flags, int intValue) {
        this.flags = flags;
        this.intValue = intValue;
    }

    @Test
    public void testIntValue() {
        assertEquals(intValue,flags.intValue());
    }
}