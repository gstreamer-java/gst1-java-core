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

package org.freedesktop.gstreamer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wayne
 */
public class ClockTimeTest {

    public ClockTimeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test public void toSeconds() {
        final long TIME = TimeUnit.SECONDS.toNanos(0xdeadbeef);
        assertEquals("toSeconds returned incorrect value", 
                TimeUnit.NANOSECONDS.toSeconds(TIME), ClockTime.toSeconds(TIME)); 
    }
    @Test public void toMillis() {
        final long TIME = TimeUnit.SECONDS.toNanos(0xdeadbeef);
        assertEquals("toMillis returned incorrect value", 
                TimeUnit.NANOSECONDS.toMillis(TIME), ClockTime.toMillis(TIME));        
    }
    @Test public void toMicros() {
        final long TIME = TimeUnit.SECONDS.toNanos(0xdeadbeef);
        assertEquals("toMillis returned incorrect value", 
                TimeUnit.NANOSECONDS.toMicros(TIME), ClockTime.toMicros(TIME));
    }
    @Test public void toStringRepresentation() {
        long hours = 3;
        long minutes = 27;
        long seconds = 13;
        long time = TimeUnit.HOURS.toNanos(hours) +
                TimeUnit.MINUTES.toNanos(minutes) +
                TimeUnit.SECONDS.toNanos(seconds);
        assertEquals("ClockTime.toString() incorrect", "03:27:13", ClockTime.toString(time));
    }
//    @Test public void toNanos() {
//        final long TIME = TimeUnit.SECONDS.toNanos(0xdeadbeef);
//        ClockTime time = ClockTime.valueOf(TIME, TimeUnit.NANOSECONDS);
//        assertEquals("toNanos returned incorrect value", 
//                TimeUnit.NANOSECONDS.toNanos(TIME), time.toNanos());
//        assertEquals("convertTo returned incorrect value", 
//                TimeUnit.NANOSECONDS.toNanos(TIME), time.convertTo(TimeUnit.NANOSECONDS));
//    }
//    @Test public void compareTo() {
//        // Collections.sort uses compareTo()
//        List<ClockTime> list = new ArrayList<ClockTime>();
//        list.add(ClockTime.valueOf(2, TimeUnit.SECONDS));
//        list.add(ClockTime.valueOf(3, TimeUnit.SECONDS));
//        list.add(ClockTime.valueOf(1, TimeUnit.SECONDS));
//        Collections.sort(list);
//        assertEquals("list not sorted correctly", 1, list.get(0).toSeconds());
//        assertEquals("list not sorted correctly", 2, list.get(1).toSeconds());
//        assertEquals("list not sorted correctly", 3, list.get(2).toSeconds());
//    }
//    
}