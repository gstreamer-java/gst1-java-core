/* 
 * Copyright (c) 2020 Peter Körner
 * Copyright (c) 2020 Neil C Smith
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

import org.freedesktop.gstreamer.glib.NativeEnum;
import org.freedesktop.gstreamer.util.TestAssumptions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyTypeTest {

    private Element audiotestsrc;
    private Element filesink;
    private Element convert;

    @BeforeClass
    public static void setUpClass() {
        Gst.init(Gst.getVersion(), "PropertyTypeTest");
    }

    @AfterClass
    public static void tearDownClass() {
        Gst.deinit();
    }

    @Before
    public void createElements() {
        audiotestsrc = ElementFactory.make("audiotestsrc", null);
        filesink = ElementFactory.make("filesink", null);
        convert = ElementFactory.make("audioconvert", null);
    }

    @After
    public void disposeElements() {
        audiotestsrc.dispose();
        filesink.dispose();
        convert.dispose();
    }

    @Test
    public void setBool() {
        audiotestsrc.set("do-timestamp", true);
        assertEquals(true, audiotestsrc.get("do-timestamp"));

        audiotestsrc.set("do-timestamp", false);
        assertEquals(false, audiotestsrc.get("do-timestamp"));

        audiotestsrc.setAsString("do-timestamp", "true");
        assertEquals("true", audiotestsrc.getAsString("do-timestamp"));
    }

    @Test
    public void setDouble() {
        audiotestsrc.set("freq", 42.23);
        assertEquals(42.23, audiotestsrc.get("freq"));

        audiotestsrc.set("freq", 5_000_000.);
        assertEquals(5_000_000., audiotestsrc.get("freq"));

        audiotestsrc.setAsString("freq", "42.23");
        assertEquals(42.23, audiotestsrc.get("freq"));
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

        long maxUnsignedLong = Long.parseUnsignedLong("18446744073709551615");
        filesink.set("max-bitrate", maxUnsignedLong);
        assertEquals(maxUnsignedLong, filesink.get("max-bitrate"));

        filesink.set("max-bitrate", 42L);
        assertEquals(42L, filesink.get("max-bitrate"));

        filesink.setAsString("max-bitrate", "18446744073709551615");
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

        audiotestsrc.setAsString("wave", "Silence");
        assertEquals(4, audiotestsrc.get("wave"));
        assertEquals("Silence", audiotestsrc.getAsString("wave"));

        audiotestsrc.setAsString("wave", "square");
        assertEquals(1, audiotestsrc.get("wave"));
        assertEquals("Square", audiotestsrc.getAsString("wave"));

        audiotestsrc.setAsString("wave", "red-noise");
        assertEquals(10, audiotestsrc.get("wave"));
        String redNoise = audiotestsrc.getAsString("wave");
        assertEquals("Red (brownian) noise", redNoise);
        audiotestsrc.setAsString("wave", redNoise);
        assertEquals(10, audiotestsrc.get("wave"));

        // invalid value
        audiotestsrc.set("wave", -256);
        assertEquals(0, audiotestsrc.get("wave"));
        
        // native enum
        audiotestsrc.set("wave", AudioTestSrcWave.SILENCE);
        assertEquals(AudioTestSrcWave.SILENCE.intValue(), audiotestsrc.get("wave"));
        assertEquals("Silence", audiotestsrc.getAsString("wave"));
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void setEnumInvalidString() {
        audiotestsrc.setAsString("wave", "FOO");
    }

    @Test
    public void setValueArrayFromString() {
        TestAssumptions.requireGstVersion(1, 14);

        convert.setAsString("mix-matrix", "<<(float)0.25, (float)0.45>,<(float)0.65, (float)0.85>>");
        String mixMatrix = convert.getAsString("mix-matrix");
        assertTrue(mixMatrix.contains("0.2"));
        assertTrue(mixMatrix.contains("0.4"));
        assertTrue(mixMatrix.contains("0.6"));
        assertTrue(mixMatrix.contains("0.8"));
    }
    
     private static enum AudioTestSrcWave implements NativeEnum<AudioTestSrcWave>{
        SINE(0),
        SQUARE(1),
        SAW(2),
        TRIANGLE(3),
        SILENCE(4),
        WHITE_NOISE(5),
        PINK_NOISE(6),
        SINE_TABLE(7),
        TICKS(8),
        GAUSSIAN_NOISE(9),
        RED_NOISE(10),
        BLUE_NOISE(11),
        VIOLET_NOISE(12);

        private final int value;

        private AudioTestSrcWave(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }
    }
    
}
