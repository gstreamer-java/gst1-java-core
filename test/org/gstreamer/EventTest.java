/* 
 * Copyright (c) 2008 Wayne Meissner
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

package org.gstreamer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import org.gstreamer.event.BufferSizeEvent;
import org.gstreamer.event.EOSEvent;
import org.gstreamer.event.FlushStartEvent;
import org.gstreamer.event.FlushStopEvent;
import org.gstreamer.event.LatencyEvent;
import org.gstreamer.event.NewSegmentEvent;
import org.gstreamer.event.QOSEvent;
import org.gstreamer.event.SeekEvent;
import org.gstreamer.event.TagEvent;
import org.gstreamer.lowlevel.GstEventAPI;
import org.gstreamer.lowlevel.GstNative;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wayne
 */
public class EventTest {
    private static final GstEventAPI gst = GstNative.load(GstEventAPI.class);

    public EventTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("EventTest", new String[] {});
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        Gst.deinit();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    public boolean waitGC(WeakReference<? extends Object> ref) throws InterruptedException {
        System.gc();
        for (int i = 0; ref.get() != null && i < 20; ++i) {
            Thread.sleep(10);
            System.gc();
        }
        return ref.get() == null;
    }
    @Test public void verifyFlags() {
        // Verify that the flags in the enum match the native ones.
        EventType[] types = EventType.values();
        for (EventType t : types) {
            int flags = gst.gst_event_type_get_flags(t);
            assertEquals("Incorrect flags", flags, t.intValue() & 0xf);
        }
    }
    @Test public void createEOSEvent() throws Exception {
        new EOSEvent();
    }
    @Test public void createFlushStartEvent() throws Exception {
        new FlushStartEvent();
    }
    @Test public void createFlushStopEvent() throws Exception {
        new FlushStopEvent();
    }
    @Test public void createLatencyEvent() throws Exception {
        new LatencyEvent(ClockTime.ZERO);
    }
    @Test public void createSegmentEvent() throws Exception {
        new NewSegmentEvent(false, 1.0, Format.TIME, 0L, 0L, 0L);
    }
    @Test public void gst_event_new_eos() {
        Event eos = gst.gst_event_new_eos();
        assertNotNull("gst_event_new_eos returned null", eos);
        assertTrue("gst_event_new_eos returned a non-EOS event", eos instanceof EOSEvent);
    }
    @Test public void gst_event_new_flush_start() {
        Event ev = gst.gst_event_new_flush_start();
        assertNotNull("gst_event_new_flush_start returned null", ev);
        assertTrue("gst_event_new_flush_start returned a non-FLUSH_START event", ev instanceof FlushStartEvent);
    }
    @Test public void gst_event_new_flush_stop() {
        Event ev = gst.gst_event_new_flush_stop();
        assertNotNull("gst_event_new_flush_stop returned null", ev);
        assertTrue("gst_event_new_flush_stop returned a non-FLUSH_STOP event", ev instanceof FlushStopEvent);
    }
    @Test public void gst_event_new_latency() {
        Event ev = gst.gst_event_new_latency(ClockTime.ZERO);
        assertNotNull("gst_event_new_latency returned null", ev);
        assertTrue("gst_event_new_latency returned a non-LATENCY event", ev instanceof LatencyEvent);
    }
    @Test public void gst_event_new_new_segment() {
        Event ev = gst.gst_event_new_new_segment(false, 1.0, Format.TIME, 0L, 0L, 0L);
        assertNotNull("gst_event_new_latency returned null", ev);
        assertTrue("gst_event_new_latency returned a non-NEWSEGMENT event", ev instanceof NewSegmentEvent);
    }
    @Test public void getLatency() {
        final ClockTime MAGIC = ClockTime.valueOf(0xdeadbeef, TimeUnit.NANOSECONDS);
        LatencyEvent ev = new LatencyEvent(MAGIC);
        assertEquals("Incorrect latency returned", MAGIC, ev.getLatency());
    }
    @Test public void NewSegment_isUpdate() {
        NewSegmentEvent ev = new NewSegmentEvent(false, 1.0, Format.TIME, 0L, 0L, 0L);
        assertFalse("Segment should not be an update", ev.isUpdate());
        ev = new NewSegmentEvent(true, 1.0, Format.TIME, 0L, 0L, 0L);
        assertTrue("Segment should be an update", ev.isUpdate());
    }
    @Test public void NewSegment_getRate() {
        final double RATE = (double) 0xdeadbeef;
        NewSegmentEvent ev = new NewSegmentEvent(false, RATE, Format.TIME, 0L, 0L, 0L);
        assertEquals("Incorrect rate returned from getRate", RATE, ev.getRate(), 0.0);
    }
    @Test public void NewSegment_getStart() {
        final long START = 0xdeadbeefL;
        NewSegmentEvent ev = new NewSegmentEvent(false, 0.1, Format.TIME, START, -1L, 0L);
        assertEquals("Incorrect rate returned from getRate", START, ev.getStart());
    }
    @Test public void NewSegment_getStop() {
        final long STOP = 0xdeadbeefL;
        NewSegmentEvent ev = new NewSegmentEvent(false, 1.0, Format.TIME, 0L, STOP, 0L);
        assertEquals("Incorrect rate returned from getRate", STOP, ev.getEnd());
    }
    @Test public void gst_event_new_tag() {
        Event ev = gst.gst_event_new_tag(new TagList());
        assertNotNull("gst_event_new_tag returned null", ev);
        assertTrue("gst_event_new_tag returned a non-TAG event", ev instanceof TagEvent);
    }
    @Test public void TagEvent_testGC() throws Exception {
        TagEvent ev = new TagEvent(new TagList());
        @SuppressWarnings("unused")
        TagList tl = ev.getTagList();
        WeakReference<Event> evRef = new WeakReference<Event>(ev);
        ev = null;
        assertFalse("Event ref collected before TagList is unreferenced", waitGC(evRef));
        tl = null;
        assertTrue("Event ref not collected after TagList is unreferenced", waitGC(evRef));
    }
    @Test public void Event_testGC() throws Exception {
        Event ev = new LatencyEvent(ClockTime.NONE);
        @SuppressWarnings("unused")
        Structure s = ev.getStructure();
        WeakReference<Event> evRef = new WeakReference<Event>(ev);
        ev = null;
        assertFalse("Event ref collected before Structure is unreferenced", waitGC(evRef));
        s = null;
        assertTrue("Event ref not collected after Structure is unreferenced", waitGC(evRef));
    }
    @Test public void gst_event_new_buffer_size() {
        final long MIN = 0x1234;
        final long MAX = 0xdeadbeef;
        final boolean ASYNC = false;
        Event ev = gst.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
        assertNotNull("gst_event_new_buffer_size returned null", ev);
        assertTrue("gst_event_new_buffer_size returned a non-BUFFERSIZE event", ev instanceof BufferSizeEvent);
    }
    @Test public void BufferSize_getMinimumSize() {
        final long MIN = 0x1234;
        final long MAX = 0xdeadbeef;
        final boolean ASYNC = false;
        BufferSizeEvent ev = (BufferSizeEvent) gst.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
        assertEquals("Wrong minimum size stored", MIN, ev.getMinimumSize());
    }
    @Test public void BufferSize_getMaximumSize() {
        final long MIN = 0x1234;
        final long MAX = 0xdeadbeef;
        final boolean ASYNC = false;
        BufferSizeEvent ev = (BufferSizeEvent) gst.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
        assertEquals("Wrong minimum size stored", MAX, ev.getMaximumSize());
    }
    @Test public void BufferSize_isAsync() {
        final long MIN = 0x1234;
        final long MAX = 0xdeadbeef;
        final boolean ASYNC = false;
        BufferSizeEvent ev = (BufferSizeEvent) gst.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
        assertEquals("Wrong minimum size stored", ASYNC, ev.isAsync());
        BufferSizeEvent ev2 = (BufferSizeEvent) gst.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, !ASYNC);
        assertEquals("Wrong minimum size stored", !ASYNC, ev2.isAsync());
    }
    @Test public void gst_event_new_qos() {
        Event ev = gst.gst_event_new_qos(0.0, 0, ClockTime.NONE);
        assertNotNull("gst_event_new_qos returned null", ev);
        assertTrue("gst_event_new_qos returned a non-QOS event", ev instanceof QOSEvent);
    }
    @Test public void QOS_getProportion() {
        final double PROPORTION = (double) 0xdeadbeef;
        QOSEvent ev = new QOSEvent(PROPORTION, 0, ClockTime.ZERO);
        assertEquals("Wrong proportion", PROPORTION, ev.getProportion(), 0d);
    }
    /*@Test */public void QOS_getDifference() {
        long DIFF = 0xdeadbeef;
        
        QOSEvent ev = new QOSEvent(0.0, DIFF, ClockTime.ZERO);
        assertEquals("Wrong difference", DIFF, ev.getDifference());
    }
    @Test public void QOS_getTimestamp() {
        final ClockTime STAMP = ClockTime.valueOf(0xdeadbeef, TimeUnit.NANOSECONDS);
        QOSEvent ev = new QOSEvent(0d, 0, STAMP);
        assertEquals("Wrong timestamp", STAMP, ev.getTimestamp());
    }
    @Test public void gst_event_new_seek() {
        Event ev = gst.gst_event_new_seek(1.0, Format.TIME, 0, 
                SeekType.SET, 0, SeekType.SET, 0);
        assertNotNull("gst_event_new_seek returned null", ev);
        assertTrue("gst_event_new_seek returned a non-SEEK event", ev instanceof SeekEvent);
    }
    @Test public void Seek_getFormat() {
        for (Format FORMAT : new Format[] { Format.TIME, Format.BYTES }) {
            SeekEvent ev = new SeekEvent(1.0, FORMAT, 0, 
                    SeekType.SET, 0, SeekType.SET, 0);
            assertEquals("Wrong format in SeekEvent", FORMAT, ev.getFormat());
        }
    }
    @Test public void Seek_getStartType() {
        for (SeekType TYPE : new SeekType[] { SeekType.CUR, SeekType.SET, SeekType.END }) {
            SeekEvent ev = new SeekEvent(1.0, Format.TIME, 0, 
                    TYPE, 0, SeekType.NONE, 0);
            assertEquals("Wrong startType in SeekEvent", TYPE, ev.getStartType());
        }
    }
    @Test public void Seek_getStopType() {
        for (SeekType TYPE : new SeekType[] { SeekType.CUR, SeekType.SET, SeekType.END }) {
            SeekEvent ev = new SeekEvent(1.0, Format.TIME, 0, 
                    SeekType.NONE, 0, TYPE, 0);
            assertEquals("Wrong stopType in SeekEvent", TYPE, ev.getStopType());
        }
    }
    @Test public void Seek_getStart() {
        final long START = 0xdeadbeef;
        SeekEvent ev = new SeekEvent(1.0, Format.TIME, 0, 
                    SeekType.SET, START, SeekType.SET, -1);
            assertEquals("Wrong start in SeekEvent", START, ev.getStart());
    }
    @Test public void Seek_getStop() {
        final long STOP = 0xdeadbeef;
        SeekEvent ev = new SeekEvent(1.0, Format.TIME, 0, 
                    SeekType.SET, 0, SeekType.SET, STOP);
            assertEquals("Wrong stop in SeekEvent", STOP, ev.getStop());
    }
    @Test public void Seek_rateZero() {
        try {
            new SeekEvent(0.0, Format.TIME, 0, 
                    SeekType.SET, 0, SeekType.SET, -1);
            fail("A rate of 0.0 should throw an exception");
        } catch (IllegalArgumentException ex) {
            
        }
    }
}