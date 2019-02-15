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

import org.freedesktop.gstreamer.event.Event;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

import org.freedesktop.gstreamer.event.TagEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class PadTest {
    
    public PadTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("test", new String[] {});
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
    
    // Does not work yet (<- this comment was here in the 0.10 code)
    @Ignore("Didn't work in the 0.10 either by the comment so still ignored")
    @Test
    public void getPad() throws Exception {
        Element src = ElementFactory.make("fakesrc", "src");
        Element sink = ElementFactory.make("fakesink", "sink");
        Pad srcPad = src.getStaticPad("src");
        Pad sinkPad = sink.getStaticPad("sink");
        assertNotNull("Could not get src pad", srcPad);
        assertNotNull("Could not get sink pad", sinkPad);
        src = null;
        sink = null;
        WeakReference<Pad> srcRef = new WeakReference<Pad>(srcPad);
        WeakReference<Pad> sinkRef = new WeakReference<Pad>(sinkPad);
        srcPad = null;
        sinkPad = null;
        assertTrue("Src pad not garbage collected", GCTracker.waitGC(srcRef));
        assertTrue("Sink pad not garbage collected", GCTracker.waitGC(sinkRef));
    }
    @Test
    public void padLink()
        throws Exception
    {
        Element src = ElementFactory.make("fakesrc", "src");
        Element sink = ElementFactory.make("fakesink", "src");
        Pad srcPad = src.getStaticPad("src");
        Pad sinkPad = sink.getStaticPad("sink");
        srcPad.link(sinkPad);
    }

    @Ignore("This seems to fail because gst1.0 doesn't actually send the event because pads " +
    		"are now created in FLUSHING state")
    @Test
    public void addEventProbe() {
        Element elem = ElementFactory.make("identity", "src");
        TagList taglist = new TagList();
		Event ev = new TagEvent(taglist);

        Pad sink = elem.getStaticPad("sink");

        final AtomicReference<Event> e = new AtomicReference<Event>();

        Pad.EVENT_PROBE event_probe = new Pad.EVENT_PROBE() {

            public PadProbeReturn eventReceived(Pad pad, Event event) {
                e.set(event);
                return PadProbeReturn.OK;
            }
        };

        sink.addEventProbe(event_probe);
        sink.sendEvent(ev);
        assertEquals("event_prober.probeEvent() was not called", ev, e.get());

        sink.removeEventProbe(event_probe);

        Event ev2 = new TagEvent(taglist);
        sink.sendEvent(ev2);
        assertNotSame("event_prober.probeEvent() should not have been called", ev2, e.get());
    }
        
    
    @Test
    public void addDataProbe() {
        
        Element elem = ElementFactory.make("identity", "src");
        TagList taglist = new TagList();
        Buffer buf = new Buffer(3);
        Buffer buf2 = new Buffer(2);
        final AtomicReference<Buffer> b = new AtomicReference<Buffer>();
        
        Pad src = elem.getStaticPad("src");
        
        Pad.DATA_PROBE data_probe = new Pad.DATA_PROBE() {
         
            @Override
            public PadProbeReturn dataReceived(Pad pad, Buffer buffer) {
                b.set(buffer);
                return PadProbeReturn.OK;
            }
        };
        
        elem.play();
        
        // add a dataprobe
        src.addDataProbe(data_probe);

        // push data
        FlowReturn res = src.push(buf);
        assertEquals("event_prober.probeEvent() was not called", buf, b.get());
        
        // remove the dataprobe
        src.removeDataProbe(data_probe);       
        
        // push data
        res = src.push(buf2);        
        assertNotSame("event_prober.probeEvent() should not have been called", buf2, b.get());
    }
}
