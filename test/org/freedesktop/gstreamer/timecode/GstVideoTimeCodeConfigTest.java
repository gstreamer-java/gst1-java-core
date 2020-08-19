package org.freedesktop.gstreamer.timecode;

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.lowlevel.GstMetaApi;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * @author Jokertwo
 */
public class GstVideoTimeCodeConfigTest {
    private GstMetaApi.GstVideoTimeCodeConfigStruct origStruct;
    private GstVideoTimeCodeConfig codeConfig;


    @Before
    public void setUp() {
        origStruct = new GstMetaApi.GstVideoTimeCodeConfigStruct();
        origStruct.fps_d = 25;
        origStruct.fps_n = 1;
        origStruct.flags = GstVideoTimeCodeFlags.GST_VIDEO_TIME_CODE_FLAGS_DROP_FRAME;
        origStruct.write();

        codeConfig = new GstVideoTimeCodeConfig(origStruct.getPointer());
    }

    @Test
    public void testGetTimeCodeFlags() {
        assertEquals(origStruct.flags, codeConfig.getTimeCodeFlags());
    }

    @Test
    public void testGetFramerateNumerator() {
        assertEquals(origStruct.fps_n, codeConfig.getFramerateNumerator());
    }

    @Test
    public void testGetFramerateDenominator() {
        assertEquals(origStruct.fps_d, codeConfig.getFramerateDenominator());
    }
}