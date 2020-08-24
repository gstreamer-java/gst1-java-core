package org.freedesktop.gstreamer.meta;

import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;
import org.freedesktop.gstreamer.Gst;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertNotNull;

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
@RunWith(Parameterized.class)
public class GstMetaDataTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return  // remove unsupported api for baseline 1.8
                EnumSet.complementOf(EnumSet.of(GstMetaData.VIDEO_TIME_CODE_META))
                        .stream()
                        .map(gstMetaData -> new Object[]{gstMetaData})
                        .collect(Collectors.toList());
    }

    private final GstMetaData gstMetaData;

    @BeforeClass
    public static void beforeClass() {
        Gst.init();
    }

    @AfterClass
    public static void afterClass() {
        Gst.deinit();
    }

    public GstMetaDataTest(GstMetaData gstMetaData) {
        this.gstMetaData = gstMetaData;
    }


    @Test
    public void testGetType() {
        assertNotNull(gstMetaData.getType());
    }
}