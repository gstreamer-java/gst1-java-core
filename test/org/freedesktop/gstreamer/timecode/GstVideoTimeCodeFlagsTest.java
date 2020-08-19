package org.freedesktop.gstreamer.timecode;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Jokertwo
 */
@RunWith(Parameterized.class)
public class GstVideoTimeCodeFlagsTest {

    private final GstVideoTimeCodeFlags flags;
    private final int intValue;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        {GstVideoTimeCodeFlags.GST_VIDEO_TIME_CODE_FLAGS_NONE, 0},
                        {GstVideoTimeCodeFlags.GST_VIDEO_TIME_CODE_FLAGS_DROP_FRAME, 1},
                        {GstVideoTimeCodeFlags.GST_VIDEO_TIME_CODE_FLAGS_INTERLACED, 2}
                });
    }

    public GstVideoTimeCodeFlagsTest(GstVideoTimeCodeFlags flags, int intValue) {
        this.flags = flags;
        this.intValue = intValue;
    }

    @Test
    public void testIntValue() {
        assertEquals(intValue,flags.intValue());
    }
}