package org.freedesktop.gstreamer;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyTypeTest {

    private Element audiotestsrc;
    private Element filesink;

    @BeforeClass
    public static void setUpClass() {
        Gst.init("PropertyTypeTest");
    }

    @AfterClass
    public static void tearDownClass() {
        Gst.deinit();
    }

    @Before
    public void before() {
        audiotestsrc = ElementFactory.make("audiotestsrc", null);
        filesink = ElementFactory.make("filesink", null);
    }

    @Test
    public void setBool() {
        audiotestsrc.set("do-timestamp", true);
        assertEquals(true, audiotestsrc.get("do-timestamp"));

        audiotestsrc.set("do-timestamp", false);
        assertEquals(false, audiotestsrc.get("do-timestamp"));
    }

    @Test
    public void setDouble() {
        audiotestsrc.set("freq", 42.23);
        assertEquals(42.23, audiotestsrc.get("freq"));

        audiotestsrc.set("freq", 5_000_000.);
        assertEquals(5_000_000., audiotestsrc.get("freq"));
    }

    @Test
    public void setInt() {
        audiotestsrc.set("num-buffers", 0);
        assertEquals(0, audiotestsrc.get("num-buffers"));

        audiotestsrc.set("num-buffers", 42);
        assertEquals(42, audiotestsrc.get("num-buffers"));

        audiotestsrc.set("num-buffers", 2147483647);
        assertEquals(2147483647, audiotestsrc.get("num-buffers"));
    }

    @Test
    public void setUInt() {
        audiotestsrc.set("blocksize", 0);
        assertEquals(0, audiotestsrc.get("blocksize"));

        audiotestsrc.set("blocksize", 42);
        assertEquals(42, audiotestsrc.get("blocksize"));

        int maxUnsignedInt = Integer.parseUnsignedInt("4294967295");
        audiotestsrc.set("blocksize", maxUnsignedInt);
        assertEquals(maxUnsignedInt, audiotestsrc.get("blocksize"));
    }

    @Test
    public void setLong() {
        audiotestsrc.set("timestamp-offset", 0L);
        assertEquals(0L, audiotestsrc.get("timestamp-offset"));

        audiotestsrc.set("timestamp-offset", 42L);
        assertEquals(42L, audiotestsrc.get("timestamp-offset"));

        audiotestsrc.set("timestamp-offset", -42L);
        assertEquals(-42L, audiotestsrc.get("timestamp-offset"));

        audiotestsrc.set("timestamp-offset", 9223372036854775807L);
        assertEquals(9223372036854775807L, audiotestsrc.get("timestamp-offset"));

        audiotestsrc.set("timestamp-offset", -9223372036854775808L);
        assertEquals(-9223372036854775808L, audiotestsrc.get("timestamp-offset"));
    }

    @Test
    public void setULong() {
        filesink.set("max-bitrate", 0L);
        assertEquals(0L, filesink.get("max-bitrate"));

        filesink.set("max-bitrate", 42L);
        assertEquals(42L, filesink.get("max-bitrate"));

        long maxUnsignedLong = Long.parseUnsignedLong("18446744073709551615");
        filesink.set("max-bitrate", maxUnsignedLong);
        assertEquals(maxUnsignedLong, filesink.get("max-bitrate"));
    }

    @Test
    public void setString() {
        filesink.set("location", "");
        assertEquals("", filesink.get("location"));

        filesink.set("location", null);
        assertNull(filesink.get("location"));

        filesink.set("location", "foobar");
        assertEquals("foobar", filesink.get("location"));
    }

    @Test
    public void setEnum() {
        audiotestsrc.set("wave", 8);
        assertEquals(8, audiotestsrc.get("wave"));

        audiotestsrc.set("wave", "silence");
        assertEquals(4, audiotestsrc.get("wave"));

        audiotestsrc.set("wave", "square");
        assertEquals(1, audiotestsrc.get("wave"));
    }
}
