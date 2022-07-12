/* 
 * Copyright (c) 2020 Neil C Smith
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.freedesktop.gstreamer.event.FlushStopEvent;

import org.freedesktop.gstreamer.event.TagEvent;
import org.freedesktop.gstreamer.query.AllocationQuery;
import org.freedesktop.gstreamer.query.Query;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class PadTest {

    public PadTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("test", new String[]{});
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
        WeakReference<Pad> srcRef = new WeakReference<>(srcPad);
        WeakReference<Pad> sinkRef = new WeakReference<>(sinkPad);
        srcPad = null;
        sinkPad = null;
        assertTrue("Src pad not garbage collected", GCTracker.waitGC(srcRef));
        assertTrue("Sink pad not garbage collected", GCTracker.waitGC(sinkRef));
    }

    @Test
    public void padLink() throws Exception {
        Element src = ElementFactory.make("fakesrc", "src");
        Element sink = ElementFactory.make("fakesink", "src");
        Pad srcPad = src.getStaticPad("src");
        Pad sinkPad = sink.getStaticPad("sink");
        srcPad.link(sinkPad);
    }

    @Test
    public void addEventProbe() {
        Element elem = ElementFactory.make("identity", "src");
        Event ev = new TagEvent(new TagList());

        Pad sink = elem.getStaticPad("sink");

        final AtomicReference<Event> e = new AtomicReference<Event>();

        Pad.EVENT_PROBE event_probe = new Pad.EVENT_PROBE() {

            public PadProbeReturn eventReceived(Pad pad, Event event) {
                e.set(event);
                return PadProbeReturn.OK;
            }
        };

        sink.setActive(true);
        sink.sendEvent(new FlushStopEvent());

        sink.addEventProbe(event_probe);
        sink.sendEvent(ev);
        assertEquals("event_prober.probeEvent() was not called", ev, e.get());

        sink.removeEventProbe(event_probe);

        Event ev2 = new TagEvent(new TagList());
        sink.sendEvent(ev2);
        assertNotSame("event_prober.probeEvent() should not have been called", ev2, e.get());
    }
    
    @Test
    public void addEventProbe_Remove() {
        Element elem = ElementFactory.make("identity", "src");
        Event ev = new TagEvent(new TagList());

        Pad sink = elem.getStaticPad("sink");

        final AtomicReference<Event> e = new AtomicReference<Event>();

        Pad.EVENT_PROBE event_probe = new Pad.EVENT_PROBE() {

            public PadProbeReturn eventReceived(Pad pad, Event event) {
                e.set(event);
                return PadProbeReturn.REMOVE;
            }
        };

        sink.setActive(true);
        sink.sendEvent(new FlushStopEvent());

        sink.addEventProbe(event_probe);
        sink.sendEvent(ev);
        assertEquals("event_prober.probeEvent() was not called", ev, e.get());

        Event ev2 = new TagEvent(new TagList());
        sink.sendEvent(ev2);
        assertNotSame("event_prober.probeEvent() should not have been called", ev2, e.get());
        
        WeakReference<Pad.EVENT_PROBE> probeRef = new WeakReference<>(event_probe);
        event_probe = null;
        assertTrue("Removed probe not collected", GCTracker.waitGC(probeRef));
        
    }
    
    @Test
    public void addProbe_Event() {
        Element elem = ElementFactory.make("identity", "src");
        Event ev = new TagEvent(new TagList());

        Pad sink = elem.getStaticPad("sink");

        final AtomicReference<Event> e = new AtomicReference<>();

        Pad.PROBE probe = (Pad pad, PadProbeInfo info) -> {
            assertTrue("Info type does not include event downstream",
                    info.getType().contains(PadProbeType.EVENT_DOWNSTREAM));
            e.set(info.getEvent());
            return PadProbeReturn.OK;
        };

        sink.setActive(true);
        sink.sendEvent(new FlushStopEvent());

        sink.addProbe(PadProbeType.EVENT_BOTH, probe);
        sink.sendEvent(ev);
        assertEquals("Probe (Event) was not called", ev, e.get());

        sink.removeProbe(probe);

        Event ev2 = new TagEvent(new TagList());
        sink.sendEvent(ev2);
        assertNotSame("Probe (Event) should not have been called", ev2, e.get());
    }
    
    @Test
    public void addProbe_EventRemove() {
        Element elem = ElementFactory.make("identity", "src");
        Event ev = new TagEvent(new TagList());

        Pad sink = elem.getStaticPad("sink");

        final AtomicReference<Event> e = new AtomicReference<>();

        Pad.PROBE probe = (Pad pad, PadProbeInfo info) -> {
            assertTrue("Info type does not include event downstream",
                    info.getType().contains(PadProbeType.EVENT_DOWNSTREAM));
            e.set(info.getEvent());
            return PadProbeReturn.REMOVE;
        };

        sink.setActive(true);
        sink.sendEvent(new FlushStopEvent());

        sink.addProbe(PadProbeType.EVENT_BOTH, probe);
        sink.sendEvent(ev);
        assertEquals("Probe (Event) was not called", ev, e.get());

        Event ev2 = new TagEvent(new TagList());
        sink.sendEvent(ev2);
        assertNotSame("Probe (Event) should not have been called", ev2, e.get());
        
        WeakReference<Pad.PROBE> probeRef = new WeakReference<>(probe);
        probe = null;
        assertTrue("Removed probe not collected", GCTracker.waitGC(probeRef));
        
        Event ev3 = new TagEvent(new TagList());
        sink.sendEvent(ev3);
        assertNotSame("Probe (Event) should not have been called", ev3, e.get());
    }

    @Test
    public void addDataProbe() {

        Element elem = ElementFactory.make("identity", "src");
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
        assertEquals("data_prober.probeData() was not called", buf, b.get());

        // remove the dataprobe
        src.removeDataProbe(data_probe);

        // push data
        res = src.push(buf2);
        assertNotSame("data_prober.probeData() should not have been called", buf2, b.get());

        elem.stop();

    }
    
    @Test
    public void addProbe_Data() {

        Element elem = ElementFactory.make("identity", "src");
        Buffer buf = new Buffer(3);
        Buffer buf2 = new Buffer(2);
        final AtomicReference<Buffer> b = new AtomicReference<>();

        Pad src = elem.getStaticPad("src");

        Pad.PROBE probe = (Pad pad, PadProbeInfo info) -> {
            assertTrue("Info type does not include buffer",
                    info.getType().contains(PadProbeType.BUFFER));
            // These cause assertion messages to be logged by GStreamer
            // assertTrue(info.getEvent() == null);
            // assertTrue(info.getQuery() == null);
            b.set(info.getBuffer());
            return PadProbeReturn.OK;
        };

        elem.play();

        // add a dataprobe
        src.addProbe(PadProbeType.BUFFER, probe);

        // push data
        FlowReturn res = src.push(buf);
        assertEquals("Probe (Data) was not called", buf, b.get());

        // remove the dataprobe
        src.removeProbe(probe);

        // push data
        res = src.push(buf2);
        assertNotSame("Probe (Data) should not have been called", buf2, b.get());

        elem.stop();

    }
    
    @Test
    public void addProbe_Idle() {

        Element elem = ElementFactory.make("identity", "src");
        final AtomicBoolean called = new AtomicBoolean();

        Pad src = elem.getStaticPad("src");

        Pad.PROBE probe = (Pad pad, PadProbeInfo info) -> {
            called.set(true);
            return PadProbeReturn.REMOVE;
        };
        
        src.addProbe(PadProbeType.IDLE, probe);
        
        assertTrue("Idle probe not called", called.get());
        
        WeakReference<Pad.PROBE> probeRef = new WeakReference<>(probe);
        
        probe = null;
        
        assertTrue("Idle probe not collected", GCTracker.waitGC(probeRef));
        
    }
    
    @Test
    public void addProbe_Query() {
        ProbeTester.test(PadProbeType.QUERY_BOTH, info -> {
            Query q = info.getQuery();
            return q instanceof AllocationQuery;
        });
        
    }
    
}
