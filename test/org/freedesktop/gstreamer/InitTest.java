/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2007 Wayne Meissner
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

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class InitTest {
    
    public InitTest() {
        
    }
    @Test
    public void testInit() {
        Version available = Gst.getVersion();
        Version notAvailable = new Version(available.getMajor(), available.getMinor() + 2);
        try {
            Gst.init(notAvailable);
            assertTrue("Version check exception not thrown!", false);
        } catch (GstException ex) {
            System.out.println("Expected init failure");
            System.out.println(ex);
        }
        String[] args = Gst.init(available, "InitTest", "--gst-plugin-spew");
        assertTrue(args.length == 0);
        
        assertTrue(Gst.testVersion(available.getMajor(), available.getMinor()));
        assertTrue(Gst.testVersion(available.getMajor(), available.getMinor() - 2));
        assertTrue(!Gst.testVersion(notAvailable.getMajor(), notAvailable.getMinor()));
        
        Gst.deinit();
    }
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
