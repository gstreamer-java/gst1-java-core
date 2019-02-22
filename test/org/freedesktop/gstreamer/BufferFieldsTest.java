package org.freedesktop.gstreamer;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * <p>
 * Copyright (C) 2018 Robert Forsman, Ericsson SATV $Author thoth $ $Date 3/8/18
 * $
 */
public class BufferFieldsTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("BufferFieldsTest");
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        Gst.deinit();
    }
    
    private Buffer buf;
    
    @Before
    public void setUp() {
        buf = new Buffer(12);
    }
    
    @Test
    public void setPTS() {
        buf.setPresentationTimestamp(ClockTime.fromMicros(5004003));
        long val = buf.getPresentationTimestamp();
        assertEquals(5004003, ClockTime.toMicros(val));
    }
    
    @Test
    public void setDTS() {
        buf.setDecodeTimestamp(ClockTime.fromMicros(9001004));
        long val = buf.getDecodeTimestamp();
        assertEquals(9001004, ClockTime.toMicros(val));
    }
    
    @Test
    public void setDuration() {
        buf.setDuration(ClockTime.fromMicros(4006008));
        long val = buf.getDuration();
        assertEquals(4006008, ClockTime.toMicros(val));
    }
    
    @Test
    public void setOffset() {
        buf.setOffset(2009006);
        long val = buf.getOffset();
        assertEquals(2009006, val);
    }
    
    @Test
    public void setOffsetEnd() {
        buf.setOffsetEnd(7005003);
        long val = buf.getOffsetEnd();
        assertEquals(7005003, val);
    }
    
    @Test
    // cannot test on GStreamer 1.8
    public void setFlags() {
//        assertTrue(buf.setFlags(7));
//        int val = buf.getFlags();
//        assertEquals(7, val);
//
//        assertTrue(buf.setFlags(10));
//        val = buf.getFlags();
//        assertEquals(15, val);
//
//        assertTrue(buf.unsetFlags(20));
//        val = buf.getFlags();
//        assertEquals(11, val);
    }
}
