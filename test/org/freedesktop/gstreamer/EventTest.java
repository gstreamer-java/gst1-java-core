/*
 * Copyright (c) 2015 Christophe Lafolet
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

package org.freedesktop.gstreamer;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import org.freedesktop.gstreamer.event.BufferSizeEvent;
import org.freedesktop.gstreamer.event.EOSEvent;
import org.freedesktop.gstreamer.event.FlushStartEvent;
import org.freedesktop.gstreamer.event.FlushStopEvent;
import org.freedesktop.gstreamer.event.LatencyEvent;
import org.freedesktop.gstreamer.event.QOSEvent;
import org.freedesktop.gstreamer.event.SeekEvent;
import org.freedesktop.gstreamer.event.SegmentEvent;
import org.freedesktop.gstreamer.event.TagEvent;
import org.freedesktop.gstreamer.lowlevel.GstEventAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wayne
 */
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
           int flags = EventTest.gst.gst_event_type_get_flags(t);
           Assert.assertEquals("Incorrect flags", flags, t.intValue() & 0xff);
       }
   }
   @Test public void createEOSEvent() throws Exception {
       new EOSEvent();
   }
   @Test public void createFlushStartEvent() throws Exception {
       new FlushStartEvent();
   }
   @Test public void createFlushStopEvent() throws Exception {
       new FlushStopEvent(false);
   }
   @Test public void createLatencyEvent() throws Exception {
       new LatencyEvent(ClockTime.ZERO);
   }
   @Test public void createSegmentEvent() throws Exception {
	   new SegmentEvent(new Segment(Format.TIME));
   }
   @Test public void gst_event_new_eos() {
       Event eos = EventTest.gst.gst_event_new_eos();
       Assert.assertNotNull("gst_event_new_eos returned null", eos);
       Assert.assertTrue("gst_event_new_eos returned a non-EOS event", eos instanceof EOSEvent);
   }
   @Test public void gst_event_new_flush_start() {
       Event ev = EventTest.gst.gst_event_new_flush_start();
       Assert.assertNotNull("gst_event_new_flush_start returned null", ev);
       Assert.assertTrue("gst_event_new_flush_start returned a non-FLUSH_START event", ev instanceof FlushStartEvent);
   }
   @Test public void gst_event_new_flush_stop() {
       Event ev = EventTest.gst.gst_event_new_flush_stop(false);
       Assert.assertNotNull("gst_event_new_flush_stop returned null", ev);
       Assert.assertTrue("gst_event_new_flush_stop returned a non-FLUSH_STOP event", ev instanceof FlushStopEvent);
   }
   @Test public void gst_event_new_latency() {
       Event ev = EventTest.gst.gst_event_new_latency(ClockTime.ZERO);
       Assert.assertNotNull("gst_event_new_latency returned null", ev);
       Assert.assertTrue("gst_event_new_latency returned a non-LATENCY event", ev instanceof LatencyEvent);
   }
   @Test public void gst_event_new_segment() {
       Event ev = EventTest.gst.gst_event_new_segment(new Segment(Format.TIME));
       Assert.assertNotNull("gst_event_new_segment returned null", ev);
       Assert.assertTrue("gst_event_new_segment returned a non-NEWSEGMENT event", ev instanceof SegmentEvent);
   }
   @Test public void getLatency() {
       final ClockTime MAGIC = ClockTime.valueOf(0xdeadbeef, TimeUnit.NANOSECONDS);
       LatencyEvent ev = new LatencyEvent(MAGIC);
       Assert.assertEquals("Incorrect latency returned", MAGIC, ev.getLatency());
   }
   @Test public void gst_event_new_tag() {
       Event ev = EventTest.gst.gst_event_new_tag(new TagList());
       Assert.assertNotNull("gst_event_new_tag returned null", ev);
       Assert.assertTrue("gst_event_new_tag returned a non-TAG event", ev instanceof TagEvent);
   }
   @Test public void TagEvent_testGC() throws Exception {
       TagEvent ev = new TagEvent(new TagList());
       ev.getTagList();
       WeakReference<Event> evRef = new WeakReference<Event>(ev);
       ev = null;
       Assert.assertFalse("Event ref collected before TagList is unreferenced", this.waitGC(evRef));
       Assert.assertTrue("Event ref not collected after TagList is unreferenced", this.waitGC(evRef));
   }
   @Test public void Event_testGC() throws Exception {
       Event ev = new LatencyEvent(ClockTime.NONE);
       ev.getStructure();
       WeakReference<Event> evRef = new WeakReference<Event>(ev);
       ev = null;
       Assert.assertFalse("Event ref collected before Structure is unreferenced", this.waitGC(evRef));
       Assert.assertTrue("Event ref not collected after Structure is unreferenced", this.waitGC(evRef));
   }
   @Test public void gst_event_new_buffer_size() {
       final long MIN = 0x1234;
       final long MAX = 0xdeadbeef;
       final boolean ASYNC = false;
       Event ev = EventTest.gst.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
       Assert.assertNotNull("gst_event_new_buffer_size returned null", ev);
       Assert.assertTrue("gst_event_new_buffer_size returned a non-BUFFERSIZE event", ev instanceof BufferSizeEvent);
   }
   @Test public void BufferSize_getMinimumSize() {
       final long MIN = 0x1234;
       final long MAX = 0xdeadbeef;
       final boolean ASYNC = false;
       BufferSizeEvent ev = (BufferSizeEvent) EventTest.gst.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
       Assert.assertEquals("Wrong minimum size stored", MIN, ev.getMinimumSize());
   }
   @Test public void BufferSize_getMaximumSize() {
       final long MIN = 0x1234;
       final long MAX = 0xdeadbeef;
       final boolean ASYNC = false;
       BufferSizeEvent ev = (BufferSizeEvent) EventTest.gst.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
       Assert.assertEquals("Wrong minimum size stored", MAX, ev.getMaximumSize());
   }
   @Test public void BufferSize_isAsync() {
       final long MIN = 0x1234;
       final long MAX = 0xdeadbeef;
       final boolean ASYNC = false;
       BufferSizeEvent ev = (BufferSizeEvent) EventTest.gst.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, ASYNC);
       Assert.assertEquals("Wrong minimum size stored", ASYNC, ev.isAsync());
       BufferSizeEvent ev2 = (BufferSizeEvent) EventTest.gst.gst_event_new_buffer_size(Format.BYTES, MIN, MAX, !ASYNC);
       Assert.assertEquals("Wrong minimum size stored", !ASYNC, ev2.isAsync());
   }
   @Test public void gst_event_new_qos() {
       Event ev = EventTest.gst.gst_event_new_qos(QosType.THROTTLE, 0.0, 0, ClockTime.NONE);
       Assert.assertNotNull("gst_event_new_qos returned null", ev);
       Assert.assertTrue("gst_event_new_qos returned a non-QOS event", ev instanceof QOSEvent);
   }
   @Test public void QOS_getQosType() {
   	final QosType TYPE = QosType.THROTTLE;
       QOSEvent ev = new QOSEvent(TYPE, 0.0, 0, ClockTime.ZERO);
       Assert.assertEquals("Wrong proportion", TYPE, ev.getQosType());
   }
   @Test public void QOS_getProportion() {
       final double PROPORTION = 0xdeadbeef;
       QOSEvent ev = new QOSEvent(QosType.THROTTLE, PROPORTION, 0, ClockTime.ZERO);
       Assert.assertEquals("Wrong proportion", PROPORTION, ev.getProportion(), 0d);
   }
   @Test public void QOS_getDifference() {
       long DIFF = 0xdead;
       QOSEvent ev = new QOSEvent(QosType.THROTTLE, 0.0, DIFF, ClockTime.ZERO);
       Assert.assertEquals("Wrong difference", DIFF, ev.getDifference());
   }
   @Test public void QOS_getTimestamp() {
       final ClockTime STAMP = ClockTime.valueOf(0xdeadbeef, TimeUnit.NANOSECONDS);
       QOSEvent ev = new QOSEvent(QosType.THROTTLE, 0d, 0, STAMP);
       Assert.assertEquals("Wrong timestamp", STAMP, ev.getTimestamp());
   }
   @Test public void gst_event_new_seek() {
       Event ev = EventTest.gst.gst_event_new_seek(1.0, Format.TIME, 0,
               SeekType.SET, 0, SeekType.SET, 0);
       Assert.assertNotNull("gst_event_new_seek returned null", ev);
       Assert.assertTrue("gst_event_new_seek returned a non-SEEK event", ev instanceof SeekEvent);
   }
   @Test public void Seek_getFormat() {
       for (Format FORMAT : new Format[] { Format.TIME, Format.BYTES }) {
           SeekEvent ev = new SeekEvent(1.0, FORMAT, 0,
                   SeekType.SET, 0, SeekType.SET, 0);
           Assert.assertEquals("Wrong format in SeekEvent", FORMAT, ev.getFormat());
       }
   }
   @Test public void Seek_getStartType() {
       for (SeekType TYPE : new SeekType[] { SeekType.SET, SeekType.END }) {
           SeekEvent ev = new SeekEvent(1.0, Format.TIME, 0,
                   TYPE, 0, SeekType.NONE, 0);
           Assert.assertEquals("Wrong startType in SeekEvent", TYPE, ev.getStartType());
       }
   }
   @Test public void Seek_getStopType() {
       for (SeekType TYPE : new SeekType[] { SeekType.SET, SeekType.END }) {
           SeekEvent ev = new SeekEvent(1.0, Format.TIME, 0,
                   SeekType.NONE, 0, TYPE, 0);
           Assert.assertEquals("Wrong stopType in SeekEvent", TYPE, ev.getStopType());
       }
   }
   @Test public void Seek_getStart() {
       final long START = 0xdeadbeef;
       SeekEvent ev = new SeekEvent(1.0, Format.TIME, 0,
                   SeekType.SET, START, SeekType.SET, -1);
           Assert.assertEquals("Wrong start in SeekEvent", START, ev.getStart());
   }
   @Test public void Seek_getStop() {
       final long STOP = 0xdeadbeef;
       SeekEvent ev = new SeekEvent(1.0, Format.TIME, 0,
                   SeekType.SET, 0, SeekType.SET, STOP);
           Assert.assertEquals("Wrong stop in SeekEvent", STOP, ev.getStop());
   }
   @Test public void Seek_rateZero() {
       try {
           new SeekEvent(0.0, Format.TIME, 0,
                   SeekType.SET, 0, SeekType.SET, -1);
           Assert.fail("A rate of 0.0 should throw an exception");
       } catch (IllegalArgumentException ex) {

       }
   }
}