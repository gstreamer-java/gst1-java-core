/* 
 * Copyright (c) 2020 John Cortell
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

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SampleTest {

    public SampleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("SampleTest", new String[] {});
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        Gst.deinit();
    }

    @Test
    public void testGetCaps() {
    	SampleTester.test((Sample sample) -> {
    		Caps caps = sample.getCaps();
    		Structure struct = caps.getStructure(0);
    		String name = struct.getName();
    		assertEquals(name, "video/x-raw");
    	});
    }

    @Test
    public void testGetBuffer() {
    	SampleTester.test((Sample sample) -> {
    		Buffer buffer = sample.getBuffer();
    		assertEquals(buffer.getMemoryCount(), 1);
    	});
    }
}
