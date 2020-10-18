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

import org.freedesktop.gstreamer.glib.Natives;
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
    		assertEquals("video/x-raw", name);
    	});
    }

    @Test
    public void testGetBuffer() {
    	SampleTester.test((Sample sample) -> {
    		Buffer buffer = sample.getBuffer();
    		assertEquals(1, buffer.getMemoryCount());
    	});
    }
    
    @Test
    public void testSetBuffer() {
    	// since gst 1.16, sample is recycled and keep a reference on the last buffer received  
    	SampleTester.test((Sample sample) -> {
    		Buffer buffer = sample.getBuffer();

    		int refCount = buffer.getRefCount();
    		    		
    		if (Gst.getVersion().checkSatisfies(new Version(1, 16))) {
        		assertEquals(2, sample.getRefCount());

        		// make sample writable
        		Natives.unref(sample);
        		
        		// force sample to release the buffer
        		sample.setBuffer(null);
        		
        		Natives.ref(sample);
        		
        		assertEquals(2, sample.getRefCount());
        		
        		assertEquals(refCount-1, buffer.getRefCount());
    		}
    	});
    }
    
}
