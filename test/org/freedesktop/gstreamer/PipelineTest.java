/* 
 * Copyright (c) 2007 Wayne Meissner
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

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.GObject;
import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Pipeline;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.ref.WeakReference;

import org.freedesktop.gstreamer.lowlevel.GObjectAPI.GObjectStruct;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author wayne
 */
public class PipelineTest {
    
    public PipelineTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("PipelineTest", new String[] {});
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        Gst.deinit();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    public boolean waitGC(WeakReference<? extends Object> ref) throws InterruptedException {
        System.gc();
        for (int i = 0; ref.get() != null && i < 20; ++i) {
            Thread.sleep(10);
            System.gc();
        }
        return ref.get() == null;
    }
    public boolean waitRefCnt(GObjectStruct struct, int refcnt) throws InterruptedException {
        System.gc();
        struct.read();
        for (int i = 0; struct.ref_count != refcnt && i < 20; ++i) {
            Thread.sleep(10);
            System.gc();
            struct.read();
        }
        return struct.ref_count == refcnt;
    }
    
    @Test
    public void testPipelineGC() throws Exception {
        Pipeline p = new Pipeline();
        int refcnt = new GObjectStruct(p).ref_count;
        
        assertEquals("Refcount should be 1", refcnt, 1);
    }
    @Test
    public void testBusGC() throws Exception {
        Pipeline pipe = new Pipeline("test playbin");
        pipe.play();
        Bus bus = pipe.getBus();
        GObjectStruct struct = new GObjectStruct(bus);
        int refcnt = struct.ref_count;
        assertTrue(refcnt > 1);
        // reget the Bus - should return the same object and not increment ref count
        Bus bus2 = pipe.getBus();
        assertTrue("Did not get same Bus object", bus == bus2);
        struct.read(); // update struct fields
        assertEquals("ref_count not equal", refcnt, struct.ref_count);   
        bus2 = null;
        
        WeakReference<Bus> bref = new WeakReference<Bus>(bus);
        bus = null;      
        // Since the pipeline holds a reference to the GstBus, the proxy should not be disposed
        assertFalse("bus disposed prematurely", waitGC(bref));
        assertFalse("ref_count decremented prematurely", waitRefCnt(struct, refcnt - 1));
        
        WeakReference<GObject> pref = new WeakReference<GObject>(pipe);
        pipe.stop();
        pipe = null;
        assertTrue("pipe not disposed", waitGC(pref));
        struct.read();
        System.out.println("bus ref_count=" + struct.ref_count);
        bus = null;
        assertTrue("bus not disposed " + struct.ref_count, waitGC(bref));
        // This is a bit dangerous, since that memory could have been reused
//        assertTrue("ref_count not decremented", waitRefCnt(struct, 0));
    } /* Test of getBus method, of class Pipeline. */
    
    @Ignore
    @Test
    public void testLaunch() {
        Pipeline pipeline = Pipeline.launch("fakesrc ! fakesink");
        assertNotNull("Pipeline not created", pipeline);
    }
    @Ignore
    @Test
    public void testLaunchElementCount() {
        Pipeline pipeline = Pipeline.launch("fakesrc ! fakesink");
        assertEquals("Number of elements in pipeline incorrect", 2, pipeline.getElements().size());
    }
    @Ignore
    @Test
    public void testLaunchSrcElement() {
        Pipeline pipeline = Pipeline.launch("fakesrc ! fakesink");
        assertEquals("First element not a fakesrc", "fakesrc", pipeline.getSources().get(0).getFactory().getName());
    }
    @Ignore
    @Test
    public void testLaunchSinkElement() {
        Pipeline pipeline = Pipeline.launch("fakesrc ! fakesink");
        assertEquals("First element not a fakesink", "fakesink", pipeline.getSinks().get(0).getFactory().getName());
    }
    @Ignore
    @Test
    public void testVarargLaunch() {
        Pipeline pipeline = Pipeline.launch("fakesrc", "fakesink");
        assertNotNull("Pipeline not created", pipeline);
    } 
    @Ignore
    @Test
    public void testVarargLaunchElementCount() {
        Pipeline pipeline = Pipeline.launch("fakesrc", "fakesink");
        assertEquals("Number of elements in pipeline incorrect", 2, pipeline.getElements().size());
    }
    @Ignore
    @Test
    public void testVarargLaunchSrcElement() {
        Pipeline pipeline = Pipeline.launch("fakesrc", "fakesink");
        assertEquals("First element not a fakesrc", "fakesrc", pipeline.getSources().get(0).getFactory().getName());
    }
    @Ignore
    @Test
    public void testVarargLaunchSinkElement() {
        Pipeline pipeline = Pipeline.launch("fakesrc", "fakesink");
        assertEquals("First element not a fakesink", "fakesink", pipeline.getSinks().get(0).getFactory().getName());
    }
}
