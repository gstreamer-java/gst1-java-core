package org.freedesktop.gstreamer.meta;

import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.SampleTester;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.timecode.GstVideoTimeCode;
import org.freedesktop.gstreamer.timecode.GstVideoTimeCodeConfig;
import org.freedesktop.gstreamer.util.TestAssumptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.freedesktop.gstreamer.timecode.GstVideoTimeCodeFlags.GST_VIDEO_TIME_CODE_FLAGS_DROP_FRAME;
import static org.freedesktop.gstreamer.timecode.GstVideoTimeCodeFlags.GST_VIDEO_TIME_CODE_FLAGS_NONE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Jokertwo
 */
public class GstTimeCodeMetaTest {

    @BeforeClass
    public static void beforeClass() {
        Gst.init();
    }

    @AfterClass
    public static void afterClass() {
        Gst.deinit();
    }

    @Test
    public void testVideoWithoutMeta() {
        // method containsMetadata is available since 1.14
        TestAssumptions.requireGstVersion(1,14);
        SampleTester.test(sample -> {
            Buffer buffer = sample.getBuffer();
            assertFalse("Default video not contains timecode metadata", buffer.containsMetadata(GstMetaData.VIDEO_TIME_CODE_META));
            assertFalse("Default video not contains region metadata", buffer.containsMetadata(GstMetaData.VIDEO_REGION_OF_INTEREST));
            assertFalse("Default video not contains metadata", buffer.containsMetadata(GstMetaData.VIDEO_META));
            assertFalse("Default video not contains gl texture metadata", buffer.containsMetadata(GstMetaData.VIDEO_GL_TEXTURE_META));
            assertFalse("Default video not contains crop metadata", buffer.containsMetadata(GstMetaData.VIDEO_CROP_META));
        }, "videotestsrc do-timestamp=true ! x264enc  ! mxfmux ! decodebin ! appsink name=myappsink");
    }

    /**
     * Contains video meta
     */
    @Test
    public void testVideoMeta() {
        // method containsMetadata is available since 1.14
        TestAssumptions.requireGstVersion(1,14);
        SampleTester.test(sample -> {
            Buffer buffer = sample.getBuffer();
            assertFalse("Default video not contains timecode metadata", buffer.containsMetadata(GstMetaData.VIDEO_TIME_CODE_META));
            assertFalse("Default video not contains region metadata", buffer.containsMetadata(GstMetaData.VIDEO_REGION_OF_INTEREST));
            assertFalse("Default video not contains gl texture metadata", buffer.containsMetadata(GstMetaData.VIDEO_GL_TEXTURE_META));
            assertFalse("Default video not contains crop metadata", buffer.containsMetadata(GstMetaData.VIDEO_CROP_META));
            assertTrue(buffer.containsMetadata(GstMetaData.VIDEO_META));
        }, "videotestsrc ! videocrop top=42 left=1 right=4 bottom=0 ! appsink name=myappsink");
    }

    /**
     * Contains video meta
     */
    @Test
    public void testVideoCropMeta() {
        // method containsMetadata is available since 1.14
        TestAssumptions.requireGstVersion(1,14);
        SampleTester.test(sample -> {
            Buffer buffer = sample.getBuffer();
            assertFalse("Default video not contains timecode metadata", buffer.containsMetadata(GstMetaData.VIDEO_TIME_CODE_META));
            assertFalse("Default video not contains region metadata", buffer.containsMetadata(GstMetaData.VIDEO_REGION_OF_INTEREST));
            assertFalse("Default video not contains gl texture metadata", buffer.containsMetadata(GstMetaData.VIDEO_GL_TEXTURE_META));
            assertFalse("Default video not contains crop metadata", buffer.containsMetadata(GstMetaData.VIDEO_CROP_META));
            assertFalse("Default video not contains metadata", buffer.containsMetadata(GstMetaData.VIDEO_META));
        }, "videotestsrc ! videoscale !" +
                " appsink name=myappsink");
    }

    @Test
    public void testVideoTimeCodeMetaPal() {
        SampleTester.test(sample -> {
            Buffer buffer = sample.getBuffer();
            if(Gst.testVersion(1,14)) {
                assertTrue("Video should contains timecode meta", buffer.containsMetadata(GstMetaData.VIDEO_TIME_CODE_META));
            }
            GstVideoTimeCodeMeta meta = buffer.getVideoTimeCodeMeta();
            assertNotNull(meta);
            GstVideoTimeCode timeCode = meta.getTimeCode();

            //first frame 00:00:00:00
            assertEquals(0, timeCode.getHours());
            assertEquals(0, timeCode.getMinutes());
            assertEquals(0, timeCode.getSeconds());
            assertEquals(0, timeCode.getFrames());

            GstVideoTimeCodeConfig timeCodeConfig = timeCode.getTCConfig();
            // PAL standard 25/1
            assertEquals(25, timeCodeConfig.getFramerateNumerator());
            assertEquals(1, timeCodeConfig.getFramerateDenominator());
            assertEquals(GST_VIDEO_TIME_CODE_FLAGS_NONE, timeCodeConfig.getTimeCodeFlags());


            meta.disown();
        }, "videotestsrc ! video/x-raw,framerate=25/1 ! timecodestamper ! videoconvert ! appsink name=myappsink");
    }

    @Test
    public void testVideoTimeCodeNTSCDrop() {
        // timecodestamper is available since 1.10
        TestAssumptions.requireGstVersion(1,10);
        SampleTester.test(sample -> {
            Buffer buffer = sample.getBuffer();
            if(Gst.testVersion(1,14)) {
                assertTrue("Video should contains timecode meta", buffer.containsMetadata(GstMetaData.VIDEO_TIME_CODE_META));
            }
            GstVideoTimeCodeMeta meta = buffer.getVideoTimeCodeMeta();
            assertNotNull(meta);
            GstVideoTimeCode timeCode = meta.getTimeCode();

            //first frame 00:00:00;00
            assertEquals(0, timeCode.getHours());
            assertEquals(0, timeCode.getMinutes());
            assertEquals(0, timeCode.getSeconds());
            assertEquals(0, timeCode.getFrames());

            GstVideoTimeCodeConfig timeCodeConfig = timeCode.getTCConfig();
            // NTSC drop standard 30000/1001
            assertEquals(30000, timeCodeConfig.getFramerateNumerator());
            assertEquals(1001, timeCodeConfig.getFramerateDenominator());
            assertEquals(GST_VIDEO_TIME_CODE_FLAGS_DROP_FRAME, timeCodeConfig.getTimeCodeFlags());

            meta.disown();

        }, "videotestsrc ! video/x-raw,framerate=30000/1001 ! timecodestamper drop-frame=true ! videoconvert ! appsink name=myappsink");
    }

    /**
     * Handle last frame of first minute
     */
    @Test
    public void testVideoTimeCodeNTSCDropFrame() {
        // timecodestamper is available since 1.10
        TestAssumptions.requireGstVersion(1,10);
        SampleTester.test(sample -> {
            Buffer buffer = sample.getBuffer();
            if(Gst.testVersion(1,14)) {
                assertTrue("Video should contains timecode meta", buffer.containsMetadata(GstMetaData.VIDEO_TIME_CODE_META));
            }
            GstVideoTimeCodeMeta meta = buffer.getVideoTimeCodeMeta();
            assertNotNull(meta);
            GstVideoTimeCode timeCode = meta.getTimeCode();

            //first frame 00:00:00;29
            assertEquals(0, timeCode.getHours());
            assertEquals(0, timeCode.getMinutes());
            assertEquals(0, timeCode.getSeconds());
            assertEquals(29, timeCode.getFrames());

            GstVideoTimeCodeConfig timeCodeConfig = timeCode.getTCConfig();
            // NTSC drop standard 30000/1001
            assertEquals(30000, timeCodeConfig.getFramerateNumerator());
            assertEquals(1001, timeCodeConfig.getFramerateDenominator());
            assertEquals(GST_VIDEO_TIME_CODE_FLAGS_DROP_FRAME, timeCodeConfig.getTimeCodeFlags());
        }, "videotestsrc ! video/x-raw,framerate=30000/1001 ! videoconvert ! timecodestamper drop-frame=true ! videoconvert ! appsink name=myappsink", 29);
    }

    @Test
    public void testVideoTimeCodeNTSCNonDrop() {
        // timecodestamper is available since 1.10
        TestAssumptions.requireGstVersion(1,10);
        SampleTester.test(sample -> {
            Buffer buffer = sample.getBuffer();
            if(Gst.testVersion(1,14)) {
                assertTrue("Video should contains timecode meta", buffer.containsMetadata(GstMetaData.VIDEO_TIME_CODE_META));
            }
            GstVideoTimeCodeMeta meta = buffer.getVideoTimeCodeMeta();
            assertNotNull(meta);
            GstVideoTimeCode timeCode = meta.getTimeCode();

            //first frame 00:00:00:00
            assertEquals(0, timeCode.getHours());
            assertEquals(0, timeCode.getMinutes());
            assertEquals(0, timeCode.getSeconds());
            assertEquals(0, timeCode.getFrames());

            GstVideoTimeCodeConfig timeCodeConfig = timeCode.getTCConfig();
            // NTSC drop standard 30/1
            assertEquals(30, timeCodeConfig.getFramerateNumerator());
            assertEquals(1, timeCodeConfig.getFramerateDenominator());
            assertEquals(GST_VIDEO_TIME_CODE_FLAGS_NONE, timeCodeConfig.getTimeCodeFlags());

            meta.disown();

        }, "videotestsrc ! video/x-raw,framerate=30/1 ! timecodestamper ! videoconvert ! appsink name=myappsink");
    }
}
