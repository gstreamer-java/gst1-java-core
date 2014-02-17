/*
 * Copyright (c) 2014 Tom Greenwood <tgreenwood@cafex.com>
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
 */
package org.gstreamer.event;

import static org.junit.Assert.*;

import org.gstreamer.Bus;
import org.gstreamer.ClockTime;
import org.gstreamer.Element;
import org.gstreamer.Gst;
import org.gstreamer.lowlevel.GstEventAPI;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ForceKeyUnitTest
{
    @BeforeClass
    public static void init() throws Exception
    {
        Gst.init("ForceKeyUnitTest", new String[] {});
    }
    
    @AfterClass
    public static void deinit() throws Exception
    {
        Gst.deinit();
    }
    
    @Test
    public void testUpstreamEventHasUpstreamType()
    {
        final ForceKeyUnit event = new ForceKeyUnit(ClockTime.NONE, true, 0);
    }

    @Test
    public void testUpstreamEventHasDownstreamType()
    {
        final ForceKeyUnit event = new ForceKeyUnit(ClockTime.fromMicros(1000), ClockTime.fromMicros(1000), ClockTime.NONE, true, 0);
    }
}
