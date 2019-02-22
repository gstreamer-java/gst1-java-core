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

package org.freedesktop.gstreamer.event;

import org.freedesktop.gstreamer.event.QOSType;
import org.freedesktop.gstreamer.event.Event;
import org.freedesktop.gstreamer.event.EventType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.ref.WeakReference;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.ClockTime;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.GCTracker;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.TagList;

import org.freedesktop.gstreamer.event.BufferSizeEvent;
import org.freedesktop.gstreamer.event.CapsEvent;
import org.freedesktop.gstreamer.event.EOSEvent;
import org.freedesktop.gstreamer.event.FlushStartEvent;
import org.freedesktop.gstreamer.event.FlushStopEvent;
import org.freedesktop.gstreamer.event.LatencyEvent;
import org.freedesktop.gstreamer.event.QOSEvent;
import org.freedesktop.gstreamer.event.ReconfigureEvent;
import org.freedesktop.gstreamer.event.SeekEvent;
import org.freedesktop.gstreamer.event.SegmentEvent;
import org.freedesktop.gstreamer.event.StepEvent;
import org.freedesktop.gstreamer.event.StreamStartEvent;
import org.freedesktop.gstreamer.event.TagEvent;
import org.freedesktop.gstreamer.lowlevel.GstAPI;
import static org.freedesktop.gstreamer.lowlevel.GstEventAPI.GSTEVENT_API;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wayne
 */
public class EventTest {
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

    @Test public void verifyFlags() {
        // Verify that the flags in the enum match the native ones.
        EventType[] types = EventType.values();
        for (EventType t : types) {
            int flags = GSTEVENT_API.gst_event_type_get_flags(t);
            assertEquals("Incorrect flags for: " + t.name(), flags, t.intValue() & 0xFF);
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
        GstAPI.GstSegmentStruct struct = new GstAPI.GstSegmentStruct();
        struct.flags = 0;
        struct.rate = 1.0;
        struct.applied_rate = 1.0;
        struct.format = Format.TIME;
        new SegmentEvent(struct);
    }
    @Test public void createCapsEvent() throws Exception {
        new CapsEvent(Caps.fromString("video/x-raw,format=I420"));
    }
    @Test public void createReconfigureEvent() throws Exception {
        new ReconfigureEvent();
    }
    @Test public void createStreamStartEvent() throws Exception {
        new StreamStartEvent("a stream_id");
    }
    @Test public void createStepEvent() throws Exception {
        new StepEvent(Format.BUFFERS, 1, 1, true, false);
    }
    @Test public void gst_event_new_eos() {
        Event eos = GSTEVENT_API.gst_event_new_eos();
        assertNotNull("gst_event_new_eos returned null", eos);
        assertTrue("gst_event_new_eos returned a non-EOS event", eos instanceof EOSEvent);
    }
    @Test public void gst_event_new_flush_start() {
        Event ev = GSTEVENT_API.gst_event_new_flush_start();
        assertNotNull("gst_event_new_flush_start returned null", ev);
        assertTrue("gst_event_new_flush_start returned a non-FLUSH_START event", ev instanceof FlushStartEvent);
    }
    @Test public void gst_event_new_flush_stop() {
        Event ev = GSTEVENT_API.gst_event_new_flush_stop();
        assertNotNull("gst_event_new_flush_stop returned null", ev);
        assertTrue("gst_event_new_flush_stop returned a non-FLUSH_STOP event", ev instanceof FlushStopEvent);
    }
    @Test public void gst_event_new_latency() {
        Event ev = GSTEVENT_API.gst_event_new_latency(0);
        assertNotNull("gst_event_new_latency returned null", ev);
        assertTrue("gst_event_new_latency returned a non-LATENCY event", ev instanceof LatencyEvent);
    }
    @Test public void gst_event_new_new_segment() {
        GstAPI.GstSegmentStruct struct = new GstAPI.GstSegmentStruct();
        struct.flags = 0;
        struct.rate = 1.0;
        struct.applied_rate = 1.0;
        struct.format = Format.TIME;
        Event ev = GSTEVENT_API.gst_event_new_segment(struct);
        assertNotNull("gst_event_new_latency returned null", ev);
        assertTrue("gst_event_new_latency returned a non-NEWSEGMENT event", ev instanceof SegmentEvent);
    }
    @Test public void getLatency() {
//        final ClockTime MAGIC = ClockTime.valueOf(0xdeadbeef, TimeUnit.NANOSECONDS);
        long MAGIC = 0xdeadbeef;
        LatencyEvent ev = new LatencyEvent(MAGIC);
        assertEquals("Incorrect latency returned", MAGIC, ev.getLatency());
    }
    @Test public void NewSegment_getRate() {
        final double RATE = (double) 0xdeadbeef;
        SegmentEvent ev = new SegmentEvent(new GstAPI.GstSegmentStruct(0, RATE, RATE, Format.TIME, 0, 0, 0, 0, 0, 0, 0));
        assertEquals("Incorrect rate returned from getRate", RATE, ev.getSegment().rate, 0.0);
    }
    @Test public void NewSegment_getStart() {
        final long START = 0xdeadbeefL;
        SegmentEvent ev = new SegmentEvent(new GstAPI.GstSegmentStruct(0, 0.1, 0.1, Format.TIME, 0, 0, START, -1L, 0, 0, 0));
        assertEquals("Incorrect rate returned from getStart", START, ev.getSegment().start);
    }
    @Test public void NewSegment_getStop() {
        final long STOP = 0xdeadbeefL;
        SegmentEvent ev = new SegmentEvent(new GstAPI.GstSegmentStruct(0, 0.1, 0.1, Format.TIME, 0, 0, 0L, STOP, 0, 0, 0));
        assertEquals("Incorrect rate returned from getRate", STOP, ev.getSegment().stop);
    }
    @Test public void gst_event_new_tag() {
        Event ev = GSTEVENT_API.gst_event_new_tag(new TagList());
        assertNotNull("gst_event_new_tag returned null", ev);
        assertTrue("gst_event_new_tag returned a non-TAG event", ev instanceof TagEvent);
    }
    @Test public void TagEvent_testGC() throws Exception {
        TagEvent ev = new TagEvent(new TagList());
        @SuppressWarnings("unused")
        TagList tl = ev.getTagList();
        WeakReference<Event> evRef = new WeakReference<Event>(ev);
        ev = null;
        assertFalse("Event ref collected before TagList is unreferenced", GCTracker.waitGC(evRef));
        tl = null;
        assertTrue("Event ref not collected after TagList is unreferenced", GCTracker.waitGC(evRef));
    }
    @Test public void Event_testGC() throws Exception {
        Event ev = new LatencyEvent(ClockTime.NONE);
        @SuppressWarnings("unused")
        Structure s = ev.getStructure();
        WeakReference<Event> evRef = new WeakReference<Event>(ev);
        ev = null;
        assertFalse("Event ref collected before Structure is unreferenced", GCTracker.waitGC(evRef));
        s = null;
        assertTrue("Event ref not collected after Structure is unreferenced", GCTracker.waitGC(evRef));
    }
    @Test public void gst_event_new_buffer_size() {
        final long MIN = 0x1234;
        final long MAX = 0xdeadbeef;
        final boolean ASYNC = false;
        Event ev = GSTEVENT_API.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
        assertNotNull("gst_event_new_buffer_size returned null", ev);
        assertTrue("gst_event_new_buffer_size returned a non-BUFFERSIZE event", ev instanceof BufferSizeEvent);
    }
    @Test public void BufferSize_getMinimumSize() {
        final long MIN = 0x1234;
        final long MAX = 0xdeadbeef;
        final boolean ASYNC = false;
        BufferSizeEvent ev = (BufferSizeEvent) GSTEVENT_API.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
        assertEquals("Wrong minimum size stored", MIN, ev.getMinimumSize());
    }
    @Test public void BufferSize_getMaximumSize() {
        final long MIN = 0x1234;
        final long MAX = 0xdeadbeef;
        final boolean ASYNC = false;
        BufferSizeEvent ev = (BufferSizeEvent) GSTEVENT_API.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
        assertEquals("Wrong minimum size stored", MAX, ev.getMaximumSize());
    }
    @Test public void BufferSize_isAsync() {
        final long MIN = 0x1234;
        final long MAX = 0xdeadbeef;
        final boolean ASYNC = false;
        BufferSizeEvent ev = (BufferSizeEvent) GSTEVENT_API.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
        assertEquals("Wrong minimum size stored", ASYNC, ev.isAsync());
        BufferSizeEvent ev2 = (BufferSizeEvent) GSTEVENT_API.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, !ASYNC);
        assertEquals("Wrong minimum size stored", !ASYNC, ev2.isAsync());
    }
    @Test public void gst_event_new_qos() {
        Event ev = GSTEVENT_API.gst_event_new_qos(QOSType.THROTTLE, 0.0, 0, ClockTime.NONE);
        assertNotNull("gst_event_new_qos returned null", ev);
        assertTrue("gst_event_new_qos returned a non-QOS event", ev instanceof QOSEvent);
    }
    @Test public void QOS_getProportion() {
        final double PROPORTION = (double) 0xdeadbeef;
        QOSEvent ev = new QOSEvent(QOSType.THROTTLE, PROPORTION, 0, ClockTime.ZERO);
        assertEquals("Wrong proportion", PROPORTION, ev.getProportion(), 0d);
    }
    @Test public void QOS_getDifference() {
        long DIFF = 0x4096;
        QOSEvent ev = new QOSEvent(QOSType.THROTTLE, 0d, DIFF, ClockTime.ZERO);
        assertEquals("Wrong difference", DIFF, ev.getDifference());
    }
    @Test public void QOS_getTimestamp() {
        final long STAMP = 0xdeadbeef;
        QOSEvent ev = new QOSEvent(QOSType.THROTTLE, 0d, 0, STAMP);
        assertEquals("Wrong timestamp", STAMP, ev.getTimestamp());
    }
    @Test
    public void QOS_getType() {
        final long STAMP = 0xdeadbeef;
        QOSEvent ev = new QOSEvent(QOSType.THROTTLE, 0d, 0, STAMP);
        assertEquals("Wrong QOSType", QOSType.THROTTLE, ev.getType());
    }
    @Test public void gst_event_new_seek() {
        Event ev = GSTEVENT_API.gst_event_new_seek(1.0, Format.TIME, 0, 
                SeekType.SET, 0, SeekType.SET, 0);
        assertNotNull("gst_event_new_seek returned null", ev);
        assertTrue("gst_event_new_seek returned a non-SEEK event", ev instanceof SeekEvent);
    }
    @Test public void Seek_getFormat() {
        for (Format FORMAT : new Format[] { Format.TIME, Format.BYTES }) {
            SeekEvent ev = new SeekEvent(1.0, FORMAT, EnumSet.noneOf(SeekFlags.class), 
                    SeekType.SET, 0, SeekType.SET, 0);
            assertEquals("Wrong format in SeekEvent", FORMAT, ev.getFormat());
        }
    }
    @Test public void Seek_getStartType() {
        for (SeekType TYPE : new SeekType[] { SeekType.SET, SeekType.END }) {
            SeekEvent ev = new SeekEvent(1.0, Format.TIME, EnumSet.noneOf(SeekFlags.class), 
                    TYPE, 0, SeekType.NONE, 0);
            assertEquals("Wrong startType in SeekEvent", TYPE, ev.getStartType());
        }
    }
    @Test public void Seek_getStopType() {
        for (SeekType TYPE : new SeekType[] { SeekType.SET, SeekType.END }) {
            SeekEvent ev = new SeekEvent(1.0, Format.TIME, EnumSet.noneOf(SeekFlags.class), 
                    SeekType.NONE, 0, TYPE, 0);
            assertEquals("Wrong stopType in SeekEvent", TYPE, ev.getStopType());
        }
    }
    @Test public void Seek_getStart() {
        final long START = 0xdeadbeef;
        SeekEvent ev = new SeekEvent(1.0, Format.TIME, EnumSet.noneOf(SeekFlags.class), 
                    SeekType.SET, START, SeekType.SET, -1);
            assertEquals("Wrong start in SeekEvent", START, ev.getStart());
    }
    @Test public void Seek_getStop() {
        final long STOP = 0xdeadbeef;
        SeekEvent ev = new SeekEvent(1.0, Format.TIME, EnumSet.noneOf(SeekFlags.class), 
                    SeekType.SET, 0, SeekType.SET, STOP);
            assertEquals("Wrong stop in SeekEvent", STOP, ev.getStop());
    }
    @Test public void Seek_rateZero() {
        try {
            new SeekEvent(0.0, Format.TIME, EnumSet.noneOf(SeekFlags.class), 
                    SeekType.SET, 0, SeekType.SET, -1);
            fail("A rate of 0.0 should throw an exception");
        } catch (IllegalArgumentException ex) {
            
        }
    }
    @Test public void gst_event_new_caps() {
        Event ev = GSTEVENT_API.gst_event_new_caps(Caps.fromString("video/x-raw,format=I420"));
        assertNotNull("gst_event_new_caps returned null", ev);
        assertTrue("gst_event_new_caps returned a non-CAPS event", ev instanceof CapsEvent);
    }
    @Test public void gst_event_new_reconfigure() {
        Event ev = GSTEVENT_API.gst_event_new_reconfigure();
        assertNotNull("gst_event_new_reconfigure returned null", ev);
        assertTrue("gst_event_new_reconfigure returned a non-RECONFIGURE event", ev instanceof ReconfigureEvent);
    }
    @Test public void gst_event_new_stream_start() {
        Event ev = GSTEVENT_API.gst_event_new_stream_start("a stream id");
        assertNotNull("gst_event_new_stream_start returned null", ev);
        assertTrue("gst_event_new_stream_start returned a non-STREAM-START event", ev instanceof StreamStartEvent);
    }
    @Test public void gst_event_new_step() {
        Event ev = GSTEVENT_API.gst_event_new_step(Format.BUFFERS, 1, 1, true, false);
        assertNotNull("gst_event_new_step returned null", ev);
        assertTrue("gst_event_new_step returned a non-STEP event", ev instanceof StepEvent);
    }
}
