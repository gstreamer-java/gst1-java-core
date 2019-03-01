/* 
 * Copyright (c) 2019 Neil C Smith
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.ref.WeakReference;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class GarbageCollectionEDTTest {
    
    public GarbageCollectionEDTTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.setProperty("glib.reapOnEDT", "true");
        Gst.init("test", new String[] {});
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
//        Gst.deinit();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testElement() throws Exception {
        
        Element e = ElementFactory.make("fakesrc", "test element");
        GCTracker tracker = new GCTracker(e);
        e = null;        
        assertTrue("Element not garbage collected", tracker.waitGC());        
        assertTrue("GObject not destroyed", tracker.waitDestroyed());
    }
    @Test
    public void testBin() throws Exception {
        Bin bin = new Bin("test");
        Element e1 = ElementFactory.make("fakesrc", "source");
        Element e2 = ElementFactory.make("fakesink", "sink");
        bin.addMany(e1, e2);
        
        assertEquals("source not returned", e1, bin.getElementByName("source"));
        assertEquals("sink not returned", e2, bin.getElementByName("sink"));
        WeakReference<Element> binRef = new WeakReference<Element>(bin);
        bin = null;
        assertTrue("Bin not garbage collected", GCTracker.waitGC(binRef));
        WeakReference<Element> e1Ref = new WeakReference<Element>(e1);
        WeakReference<Element> e2Ref = new WeakReference<Element>(e2);
        e1 = null;
        e2 = null;
        
        assertTrue("First Element not garbage collected", GCTracker.waitGC(e1Ref));
        assertTrue("Second Element not garbage collected", GCTracker.waitGC(e2Ref));
        
    }
    @Test
    public void testBinRetrieval() throws Exception {
        Bin bin = new Bin("test");
        Element e1 = ElementFactory.make("fakesrc", "source");
        Element e2 = ElementFactory.make("fakesink", "sink");
        bin.addMany(e1, e2);
        int id1 = System.identityHashCode(e1);
        int id2 = System.identityHashCode(e2);
        
        e1 = null;
        e2 = null;
        System.gc();
        Thread.sleep(10);
        // Should return the same object that was put into the bin
        assertEquals("source ID does not match", id1, System.identityHashCode(bin.getElementByName("source")));
        assertEquals("sink ID does not match", id2, System.identityHashCode(bin.getElementByName("sink")));       
    }
    @Test
    public void pipeline() {
        Pipeline pipe = new Pipeline("test");
        GCTracker pipeTracker = new GCTracker(pipe);
        pipe = null;
        assertTrue("Pipe not garbage collected", pipeTracker.waitGC());
        System.out.println("checking if pipeline is destroyed");
        assertTrue("Pipe not destroyed", pipeTracker.waitDestroyed());
    }
    @Test
    public void pipelineBus() {
        Pipeline pipe = new Pipeline("test");
        Bus bus = pipe.getBus();
        GCTracker busTracker = new GCTracker(bus);
        GCTracker pipeTracker = new GCTracker(pipe);
        
        pipe = null;
        bus = null;
        assertTrue("Bus not garbage collected", busTracker.waitGC());
        assertTrue("Bus not destroyed", busTracker.waitDestroyed());
        assertTrue("Pipe not garbage collected", pipeTracker.waitGC());
        assertTrue("Pipe not destroyed", pipeTracker.waitDestroyed());

    }
    @Test
    @Ignore("See comment below.")
    public void busWithListeners() {
        Pipeline pipe = new Pipeline("test");
        Bus bus = pipe.getBus();
        // Reasoning for @Igore:
        //
        // #connect increments the native reference count this in turn prevents 
        // the native part of the BUS to get out of scope.
        bus.connect(new Bus.EOS() {

            public void endOfStream(GstObject source) {
            }
        });
        
        GCTracker busTracker = new GCTracker(bus);
        GCTracker pipeTracker = new GCTracker(pipe);
        bus = null;
        pipe = null;
        assertTrue("Bus not garbage collected", busTracker.waitGC());
        assertTrue("Bus not destroyed", busTracker.waitDestroyed());
        assertTrue("Pipe not garbage collected", pipeTracker.waitGC());
        assertTrue("Pipe not destroyed", pipeTracker.waitDestroyed());
    }
}
