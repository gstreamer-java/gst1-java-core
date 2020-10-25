/*
 * Copyright (c) 2020 Neil C Smith
 *
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.freedesktop.gstreamer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class ClockTest {
    
    @BeforeClass
    public static void beforeClass() {
        Gst.init(Gst.getVersion());
    }

    @AfterClass
    public static void afterClass() {
        Gst.deinit();
    }
    
    @Test
    public void calibrationTest() {
        Pipeline pipe = (Pipeline) Gst.parseLaunch("autovideosrc ! autovideosink");
        Clock clock = pipe.getClock();
        Clock.Calibration cal1 = clock.getCalibration();
        System.out.println(cal1);
        assertEquals(0, cal1.internal());
        assertEquals(0, cal1.external());
        assertEquals(1, cal1.rateNum());
        assertEquals(1, cal1.rateDenom());
        clock.setCalibration(-100, 1000, 8, 5);
        
        Clock.Calibration cal2 = clock.getCalibration();
        System.out.println(cal2);
        assertEquals(-100, cal2.internal());
        assertEquals(1000, cal2.external());
        assertEquals(8, cal2.rateNum());
        assertEquals(5, cal2.rateDenom());
        
        Clock.Calibration cal3 = clock.getCalibration();
        assertEquals(cal2, cal3);
        assertEquals(cal2.hashCode(), cal3.hashCode());
        assertNotEquals(cal1, cal3);
        
    }
    
    
}
