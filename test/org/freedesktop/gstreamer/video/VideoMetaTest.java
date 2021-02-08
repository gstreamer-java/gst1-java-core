/*
 * Copyright (c) 2020 Christophe Lafolet
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

import static org.freedesktop.gstreamer.lowlevel.GstVideoAPI.GSTVIDEO_API;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.EnumSet;
import java.util.Set;

import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.PadProbeType;
import org.freedesktop.gstreamer.ProbeTester;
import org.freedesktop.gstreamer.query.AllocationQuery;
import org.freedesktop.gstreamer.query.Query;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class VideoMetaTest {

    @BeforeClass
    public static void beforeClass() {
        Gst.init(Gst.getVersion());
        
        // Force registration of the GType of the VideoMeta api
        GSTVIDEO_API.gst_video_meta_api_get_type();
    }

    @AfterClass
    public static void afterClass() {
        Gst.deinit();
    }
    
    @Test
    public void test_RGB_format() {

        Set<PadProbeType> padProbeTypes = EnumSet.of(PadProbeType.QUERY_DOWNSTREAM, PadProbeType.BUFFER);
    
        ProbeTester.test(
            "videotestsrc ! video/x-raw,format=RGB,width=320,height=240 ! appsink name=sink",
            padProbeTypes, info -> {
        
                if (info.getType().contains(PadProbeType.QUERY_DOWNSTREAM)) {
                    Query q = info.getQuery();
                    if (q instanceof AllocationQuery) {
                        AllocationQuery allocationQuery = (AllocationQuery)q;
                        allocationQuery.addAllocationMeta(VideoMeta.API, null);
                    }
                }
        
                if (info.getType().contains(PadProbeType.BUFFER)) {
                    Buffer buffer = info.getBuffer();
                    VideoMeta meta = buffer.getMeta(VideoMeta.API);
                    assertNotNull(meta);
                
                    // format : 1 plane of RGB pixels (3 bytes)
                    assertEquals(320, meta.getWidth());
                    assertEquals(240, meta.getHeight());
                    assertEquals(0,   meta.getFrameId());
                    assertEquals(1,   meta.getNumberOfPlanes());
                    assertEquals(0,   meta.getOffset(0)); // plane RGB : size=320*3
                    assertEquals(320*3, meta.getStride(0));

                    return true;
                }
                return false;
            }
        );
    }

    @Test
    public void test_I420_format() {

        Set<PadProbeType> padProbeTypes = EnumSet.of(PadProbeType.QUERY_DOWNSTREAM, PadProbeType.BUFFER);
    
        ProbeTester.test(
            "videotestsrc ! video/x-raw,format=I420,width=320,height=240 ! appsink name=sink",
            padProbeTypes, info -> {
        
                if (info.getType().contains(PadProbeType.QUERY_DOWNSTREAM)) {
                    Query q = info.getQuery();
                    if (q instanceof AllocationQuery) {
                        AllocationQuery allocationQuery = (AllocationQuery)q;
                        allocationQuery.addAllocationMeta(VideoMeta.API, null);
                     }
                }
        
                if (info.getType().contains(PadProbeType.BUFFER)) {
                    Buffer buffer = info.getBuffer();
                    VideoMeta meta = buffer.getMeta(VideoMeta.API);
                    assertNotNull(meta);
                
                    // format : 3 planes for Y,U,V planes
                    assertEquals(320, meta.getWidth());
                    assertEquals(240, meta.getHeight());
                    assertEquals(0,   meta.getFrameId());
                    assertEquals(3,   meta.getNumberOfPlanes());
                    assertEquals(0,   meta.getOffset(0)); // plane Y : size 320x240
                    assertEquals(320*240, meta.getOffset(1)); // plane U : size : 160x120
                    assertEquals(320*240*5/4, meta.getOffset(2)); // plane V : size : 160x120
                    assertEquals(320, meta.getStride(0)); // plane Y
                    assertEquals(160, meta.getStride(1)); // plane U
                    assertEquals(160, meta.getStride(2)); // plane V

                    return true;
        		}
        		return false;
        	}
        );
    }
}
