/* 
 * Copyright (c) 2009 Levente Farkas
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI;
import org.freedesktop.gstreamer.lowlevel.GstQueryAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.query.DurationQuery;
import org.freedesktop.gstreamer.query.FormatsQuery;
import org.freedesktop.gstreamer.query.LatencyQuery;
import org.freedesktop.gstreamer.query.PositionQuery;
import org.freedesktop.gstreamer.query.SeekingQuery;
import org.freedesktop.gstreamer.query.SegmentQuery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wayne
 */
public class QueryTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("QueryTest", new String[] {});
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        Gst.deinit();
    }

    @Test
    public void gst_query_new_position() {
        Query query = GstQueryAPI.GSTQUERY_API.gst_query_new_position(Format.TIME);
        assertNotNull("Query.newPosition returned null", query);
        assertTrue("Returned query not instance of PositionQuery", query instanceof PositionQuery);
    }
    @Test
    public void getPositionQueryFormat() {
        for (Format format : Format.values()) {
            PositionQuery query = new PositionQuery(format);
            assertEquals("Format returned from getFormat() is incorrect" , format, query.getFormat());
        }
    }
    @Test
    public void getPosition() {
        PositionQuery query = new PositionQuery(Format.TIME);
        final long POSITION = 0xdeadbeef;
        query.setPosition(Format.TIME, POSITION);
        assertEquals("Incorrect position returned", POSITION, query.getPosition());
    }
    @Test 
    public void positionQueryToString() {
        PositionQuery query = new PositionQuery(Format.TIME);
        query.setPosition(Format.TIME, 1234);
        String s = query.toString();
        assertTrue("toString() did not return format", s.contains("format=TIME"));
        assertTrue("toString() did not return position", s.contains("position=1234"));
    }
    @Test
    public void newDurationQuery() {
        Query query = new DurationQuery(Format.TIME);
        assertNotNull("Query.newDuration returned null", query);
        assertTrue("Returned query not instance of DurationQuery", query instanceof DurationQuery);
    }
    @Test
    public void gst_query_new_duration() {
        Query query = GstQueryAPI.GSTQUERY_API.gst_query_new_duration(Format.TIME);
        assertNotNull("Query.newDuration returned null", query);
        assertTrue("Returned query not instance of DurationQuery", query instanceof DurationQuery);
    }
    
    @Test
    public void getDurationQueryFormat() {
        for (Format format : Format.values()) {
            DurationQuery query = new DurationQuery(format);
            assertEquals("Format returned from getFormat() is incorrect" , format, query.getFormat());
        }
    }
    @Test
    public void getDuration() {
        DurationQuery query = new DurationQuery(Format.TIME);
        final long DURATION = 0xdeadbeef;
        query.setDuration(Format.TIME, DURATION);
        assertEquals("Incorrect duration returned", DURATION, query.getDuration());
    }
    @Test 
    public void durationQueryToString() {
        DurationQuery query = new DurationQuery(Format.TIME);
        query.setDuration(Format.TIME, 1234);
        String s = query.toString();
        assertTrue("toString() did not return format", s.contains("format=TIME"));
        assertTrue("toString() did not return duration", s.contains("duration=1234"));
    }
    @Test
    public void gst_query_new_latency() {
        Query query = GstQueryAPI.GSTQUERY_API.gst_query_new_latency();
        assertNotNull("gst_query_new_latency() returned null", query);
        assertTrue("Returned query not instance of LatencyQuery", query instanceof LatencyQuery);
    }
    @Test
    public void latencyIsLive() {
        LatencyQuery query = new LatencyQuery();
        query.setLatency(true, ClockTime.ZERO, ClockTime.ZERO);
        assertTrue("isLive not set to true", query.isLive());
        query.setLatency(false, ClockTime.ZERO, ClockTime.ZERO);
        assertFalse("isLive not set to true", query.isLive());
    }
    @Test 
    public void getMinimumLatency() {
        LatencyQuery query = new LatencyQuery();
        final ClockTime MIN = ClockTime.fromMillis(13000);
        query.setLatency(false, MIN, ClockTime.valueOf(~0L, TimeUnit.SECONDS));
        assertEquals("Min latency not set", MIN, query.getMinimumLatency());
    }
    @Test 
    public void getMaximumLatency() {
        LatencyQuery query = new LatencyQuery();
        final ClockTime MAX = ClockTime.fromMillis(123000);
        query.setLatency(false, ClockTime.ZERO, MAX);
        assertEquals("Min latency not set", MAX, query.getMaximumLatency());
    }
     
    @Test 
    public void latencyQueryToString() {
        LatencyQuery query = new LatencyQuery();
        ClockTime minLatency = ClockTime.fromMillis(13000);
        ClockTime maxLatency = ClockTime.fromMillis(200000);
        query.setLatency(true, minLatency, maxLatency);
        String s = query.toString();
        assertTrue("toString() did not return isLive", s.contains("live=true"));
        assertTrue("toString() did not return minLatency", s.contains("min=" + minLatency));
        assertTrue("toString() did not return minLatency", s.contains("max=" + maxLatency));
    }
    
    @Test public void segmentQuery() {
        SegmentQuery query = new SegmentQuery(Format.TIME);
        ClockTime end = ClockTime.fromMillis(1000);
        query.setSegment(1.0, Format.TIME, 0, end.toNanos());
        assertEquals("Format not set correctly", Format.TIME, query.getFormat());
        assertEquals("Start time not set correctly", 0, query.getStart());
        assertEquals("End time not set correctly", end.toNanos(), query.getEnd());
    }
    @Test public void seekingQuery() {
        SeekingQuery query = new SeekingQuery(Format.TIME);
        ClockTime start = ClockTime.ZERO;
        ClockTime end = ClockTime.fromMillis(1000);
        query.setSeeking(Format.TIME, true, start.toNanos(), end.toNanos());
        assertEquals("Format not set", Format.TIME, query.getFormat());
        assertEquals("Start time not set", start.toNanos(), query.getStart());
        assertEquals("End time not set", end.toNanos(), query.getEnd());
    }
    @Test public void formatsQuery() {
        Query query = GstQueryAPI.GSTQUERY_API.gst_query_new_formats();
        assertNotNull("gst_query_new_latency() returned null", query);
        assertTrue("Returned query not instance of LatencyQuery", query instanceof FormatsQuery);
    }
    @Test public void formatsQueryCount() {
        FormatsQuery query = new FormatsQuery();
        query.setFormats(Format.TIME, Format.PERCENT);
        assertEquals("Wrong formats count", 2, query.getCount());
    }
    @Test public void formatsQueryFormats() {
        FormatsQuery query = new FormatsQuery();
        query.setFormats(Format.TIME, Format.PERCENT);
        assertEquals("First format incorrect", Format.TIME, query.getFormat(0));
        assertEquals("Second format incorrect", Format.PERCENT, query.getFormat(1));
        List<Format> formats = query.getFormats();
        assertEquals("First format incorrect", Format.TIME, formats.get(0));
        assertEquals("Second format incorrect", Format.PERCENT, formats.get(1));
    }

    @Test public void makeWriteable() {
        Query query = new SegmentQuery(Format.TIME);
        assertTrue("New query is not writable", query.isWritable());
        // Bumping the ref count makes this instance non writable
        GstNative.load(GstMiniObjectAPI.class).gst_mini_object_ref(query);
        assertFalse("Query with multiple references should not be writable", query.isWritable());
        // Now get a new reference that is writable
        query = query.makeWritable();
        assertTrue("Query not writable after makeWritable", query.isWritable());
    }
}