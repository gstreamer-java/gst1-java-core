package org.freedesktop.gstreamer.timecode;

import org.freedesktop.gstreamer.lowlevel.GstMetaApi;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Jokertwo
 */
public class GstVideoTimeCodeTest {


    private GstMetaApi.GstVideoTimeCodeStruct timeCodeStruct;
    private GstMetaApi.GstVideoTimeCodeConfigStruct.ByValue configStruct;

    private GstVideoTimeCode timeCode;

    @Before
    public void setUp() {
        timeCodeStruct = new GstMetaApi.GstVideoTimeCodeStruct();
        configStruct = new GstMetaApi.GstVideoTimeCodeConfigStruct.ByValue();

        // 01:02:03:04
        timeCodeStruct.hours = 1;
        timeCodeStruct.minutes = 2;
        timeCodeStruct.seconds = 3;
        timeCodeStruct.frames = 4;

        timeCodeStruct.field_count = 55;

        // config
        timeCodeStruct.config = configStruct;

        timeCodeStruct.write();
        timeCode = new GstVideoTimeCode(timeCodeStruct.getPointer());


    }

    @Test
    public void testGetTCConfig() {
        assertNotNull(timeCode.getTCConfig());
    }

    @Test
    public void testGetHours() {
        assertEquals(1,timeCode.getHours());
    }

    @Test
    public void testGetMinutes() {
        assertEquals(2,timeCode.getMinutes());
    }

    @Test
    public void testGetSeconds() {
        assertEquals(3,timeCode.getSeconds());
    }

    @Test
    public void testGetFrames() {
        assertEquals(4,timeCode.getFrames());
    }
}