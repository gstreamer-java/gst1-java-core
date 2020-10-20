/*
 * Copyright (c) 2020 Neil C Smith
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

import org.freedesktop.gstreamer.BufferProbeTester;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstMetaPtr;
import org.freedesktop.gstreamer.util.TestAssumptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;



public class VideoCropMetaTest {

    @BeforeClass
    public static void beforeClass() {
        Gst.init(Gst.getVersion());
    }

    @AfterClass
    public static void afterClass() {
        Gst.deinit();
    }

    @Test
    public void testIterateWithCrop() {
        TestAssumptions.requireGstVersion(1, 14);
        TestAssumptions.requireElement("fakevideosink");
        BufferProbeTester.test(buffer -> {
            buffer.iterateMeta().forEachRemaining(meta -> {
                GstMetaPtr ptr = Natives.getPointer(meta).as(GstMetaPtr.class, GstMetaPtr::new);
                System.out.println(ptr.getGType().getTypeName());
                System.out.println(ptr.getAPIGType().getTypeName());
        });
            
        }, "videotestsrc ! videocrop top=10 left=10 bottom=50 right=50 ! fakevideosink name=sink");
    }
    
}
