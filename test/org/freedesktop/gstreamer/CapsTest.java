/*
 * Copyright (c) 2007 Wayne Meissner
 * Copyright (C) 2005 Andy Wingo <wingo@pobox.com>
 * Copyright (C) <2005> Thomas Vander Stichele <thomas at apestaart dot org>
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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for GstCaps
 */
public class CapsTest {

    public CapsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("CapsTest", new String[] {});
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        Gst.deinit();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void capsMerge() {
        Caps c1 = new Caps("video/x-raw, format=RGB, bpp=32, depth=24");
        Caps c2 = new Caps("video/x-raw, format=RGB, width=640, height=480");
        Caps c3 = c1.merge(c2);

        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c2.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}
        boolean widthFound = false, heightFound = false;
        for (int i = 0; i < c3.size(); ++i) {
            Structure s = c3.getStructure(i);
            if (s.hasIntField("width")) {
                widthFound = true;
            }
            if (s.hasIntField("height")) {
                heightFound = true;
            }
        }
        Assert.assertTrue("width not appended", widthFound);
        Assert.assertTrue("height not appended", heightFound);
        Assert.assertTrue(c1.getRefCount() == 1);
        Assert.assertTrue(c3.getRefCount() == 1);
        c1.dispose(); c3.dispose();
    }

    @Test
    public void capsAppend() {
        Caps c1 = new Caps("video/x-raw, format=RGB, bpp=32, depth=24");
        Caps c2 = new Caps("video/x-raw, format=RGB, width=640, height=480");
        c1.append(c2);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c2.toString();
            Assert.fail("appended caps not invalidated");
        } catch (IllegalStateException ex) {}
        boolean widthFound = false, heightFound = false;
        for (int i = 0; i < c1.size(); ++i) {
            Structure s = c1.getStructure(i);
            if (s.hasIntField("width")) {
                widthFound = true;
            }
            if (s.hasIntField("height")) {
                heightFound = true;
            }
        }
        Assert.assertTrue("width not appended", widthFound);
        Assert.assertTrue("height not appended", heightFound);
    }
    private static final String non_simple_caps_string =
        "video/x-raw, format=(string)I420, framerate=(fraction)[ 1/100, 100 ], "
        + "width=(int)[ 16, 4096 ], height=(int)[ 16, 4096 ]; video/x-raw, "
        + "format=(string)YUY2, framerate=(fraction)[ 1/100, 100 ], width=(int)[ 16, 4096 ], "
        + "height=(int)[ 16, 4096 ]; video/x-raw, format=rgb, bpp=(int)8, depth=(int)8, "
        + "endianness=(int)1234, framerate=(fraction)[ 1/100, 100 ], width=(int)[ 16, 4096 ], "
        + "height=(int)[ 16, 4096 ]; video/x-raw, "
        + "format=(string){ I420, YUY2, YV12 }, width=(int)[ 16, 4096 ], "
        + "height=(int)[ 16, 4096 ], framerate=(fraction)[ 1/100, 100 ]";
    @Test
    public void simplify() {
        Caps c1 = new Caps(CapsTest.non_simple_caps_string);
        Assert.assertNotNull("Caps not created", c1);
        Caps c2 = c1.simplify();
        Assert.assertNotNull("Caps not created", c2);
        /* check simplified caps, should be:
         *
         * video/x-raw, format=RGB, bpp=(int)8, depth=(int)8, endianness=(int)1234,
         *     framerate=(fraction)[ 1/100, 100 ], width=(int)[ 16, 4096 ],
         *     height=(int)[ 16, 4096 ];
         * video/x-raw, format=((string)){ YV12, YUY2, I420 },
         *     width=(int)[ 16, 4096 ], height=(int)[ 16, 4096 ],
         *     framerate=(fraction)[ 1/100, 100 ]
         */
        Assert.assertEquals("Caps not simplified to 2 structures", 2, c2.size());
        Structure s1 = c2.getStructure(0);
        Assert.assertNotNull("Caps.getStructure(0) failed", s1);
        Structure s2 = c2.getStructure(1);
        Assert.assertNotNull("Caps.getStructure(1) failed", s2);
        if (!s1.hasName("video/x-raw")) {
            Structure tmp = s1;
            s1 = s2;
            s2 = tmp;
        }
        Assert.assertTrue("Could not locate video/x-raw structure", s1.hasName("video/x-raw"));
        Assert.assertEquals("bpp not retrieved", 8, s1.getInteger("bpp"));
        Assert.assertEquals("depth not retrieved", 8, s1.getInteger("depth"));

        Assert.assertTrue("Could not locate video/x-raw structure", s2.hasName("video/x-raw"));
        Assert.assertEquals(1, c1.getRefCount());
        Assert.assertEquals(1, c2.getRefCount());
        c1.dispose(); c2.dispose();
    }
    @Test
    public void truncate() {
        Caps c1 = Caps.fromString(CapsTest.non_simple_caps_string);
        Assert.assertNotNull("Caps.fromString failed", c1);
        Assert.assertEquals("Incorrect number of structures in caps", 4, c1.size());
        Caps c2 = c1.truncate();
        Assert.assertEquals("Caps not truncated", 1, c2.size());
        c1.dispose(); c2.dispose();

    }
    @Test
    public void mergeANYAndSpecific() {
        /* ANY + specific = ANY */
        Caps c1 = Caps.fromString("audio/x-raw,rate=44100");
        Caps c2 = Caps.anyCaps();
        Caps c3 = c2.merge(c1);

        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}

        Assert.assertEquals("Too many structures in merged caps", 0, c3.size());
        Assert.assertTrue("Merged caps should be ANY", c3.isAny());

        Assert.assertEquals(1, c2.getRefCount());
        Assert.assertEquals(1, c3.getRefCount());

        // Force cleanup to bring out any memory bugs
        c2.dispose(); c3.dispose();
    }
    @Test
    public void mergeSpecificAndANY() {
        /* specific + ANY = ANY */
        Caps c1 = Caps.anyCaps();
        Caps c2 = Caps.fromString("audio/x-raw,rate=44100");
        Caps c3 = c2.merge(c1);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}
        Assert.assertEquals("Too many structures in merged caps", 0, c3.size());
        Assert.assertTrue("Merged caps should be ANY", c3.isAny());

        Assert.assertEquals(1, c2.getRefCount());
        Assert.assertEquals(1, c3.getRefCount());

        // Force cleanup to bring out any memory bugs
        c2.dispose();
        c3.dispose();
    }
    @Test
    public void mergeSpecificAndEMPTY() {
        /* EMPTY + specific = specific */
        Caps c1 = Caps.fromString("audio/x-raw,rate=44100");
        Caps c2 = Caps.emptyCaps();
        Caps c3 = c2.merge(c1);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}

        Assert.assertEquals("Wrong number of structures in merged structure", 1, c3.size());
        Assert.assertFalse("Merged caps should not be empty", c3.isEmpty());

        Assert.assertEquals(1, c2.getRefCount());
        Assert.assertEquals(1, c3.getRefCount());

        // Force cleanup to bring out any memory bugs
        c2.dispose();
        c3.dispose();
    }
    @Test
    public void mergeEMPTYAndSpecific() {
        /* specific + EMPTY = specific */
        Caps c2 = Caps.fromString("audio/x-raw,rate=44100");
        Caps c1 = Caps.emptyCaps();
        Caps c3 = c2.merge(c1);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}
        Assert.assertEquals("Merged Caps structure count incorrect", 1, c3.size());
        Assert.assertFalse("Merged caps should not be empty", c2.isEmpty());

        Assert.assertEquals(1, c2.getRefCount());
        Assert.assertEquals(1, c3.getRefCount());

        // Force cleanup to bring out any memory bugs
        c2.dispose();
        c3.dispose();
    }
    @Test
    public void mergeSame() {
        /* this is the same */
        Caps c1 = Caps.fromString("audio/x-raw,rate=44100,channels=1");
        Caps c2 = Caps.fromString("audio/x-raw,rate=44100,channels=1");
        Caps c3 = c2.merge(c1);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}

        Assert.assertEquals("Merged Caps structure count incorrect", 1, c3.size());
        // Force cleanup to bring out any memory bugs
        c2.dispose();
    }
    @Test
    public void mergeSameWithDifferentOrder() {
        /* and so is this */
        Caps c1 = Caps.fromString("audio/x-raw,rate=44100,channels=1");
        Caps c2 = Caps.fromString("audio/x-raw,channels=1,rate=44100");
        Caps c3 = c2.merge(c1);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}

        Assert.assertEquals("Merged Caps structure count incorrect", 1, c3.size());
        // Force cleanup to bring out any memory bugs
        c2.dispose();
    }
    @Test public void mergeSameWithBufferData() {
        Caps c1 = Caps.fromString("video/x-foo, data=(buffer)AA");
        Caps c2 = Caps.fromString("video/x-foo, data=(buffer)AABB");
        Caps c3 = c2.merge(c1);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}

        Assert.assertEquals("Merged Caps structure count incorrect", 2, c3.size());
        // Force cleanup to bring out any memory bugs
        c2.dispose(); c3.dispose();
    }
    @Test public void mergeSameWithBufferDataReversed() {
        Caps c1 = Caps.fromString("video/x-foo, data=(buffer)AABB");
        Caps c2 = Caps.fromString("video/x-foo, data=(buffer)AA");
        Caps c3 = c2.merge(c1);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}

        Assert.assertEquals("Merged Caps structure count incorrect", 2, c3.size());
        // Force cleanup to bring out any memory bugs
        c2.dispose();
    }
    @Test public void mergeSameWithBufferDataSame() {
        Caps c1 = Caps.fromString("video/x-foo, data=(buffer)AA");
        Caps c2 = Caps.fromString("video/x-foo, data=(buffer)AA");
        Caps c3 = c2.merge(c1);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}

        Assert.assertEquals("Merged Caps structure count incorrect", 1, c3.size());
        // Force cleanup to bring out any memory bugs
        c2.dispose();
    }
    @Test public void mergeDifferentWithBufferDataSame() {
        Caps c1 = Caps.fromString("video/x-foo, data=(buffer)AA");
        Caps c2 = Caps.fromString("video/x-bar, data=(buffer)AA");
        Caps c3 = c2.merge(c1);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}

        Assert.assertEquals("Merged Caps structure count incorrect", 2, c3.size());
        // Force cleanup to bring out any memory bugs
        c2.dispose();
    }
    @Test public void mergeSubset() {
        /* the 2nd is already covered */
        Caps c2 = Caps.fromString("audio/x-raw,channels=[1,2]");
        Caps c1 = Caps.fromString("audio/x-raw,channels=1");
        Caps c3 = c2.merge(c1);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}

        Assert.assertEquals("Merged Caps structure count incorrect", 1, c3.size());
        // Force cleanup to bring out any memory bugs
        c2.dispose();
    }
    @Test public void mergeNonSubset() {
        /* here it is not */
        Caps c2 = Caps.fromString("audio/x-raw,channels=1,rate=44100");
        Caps c1 = Caps.fromString("audio/x-raw,channels=[1,2],rate=44100");
        Caps c3 = c2.merge(c1);
        // Verify that the victim caps were invalidated and cannot be used.
        try {
            c1.toString();
            Assert.fail("merged caps not invalidated");
        } catch (IllegalStateException ex) {}

        Assert.assertEquals("Merged Caps structure count incorrect", 2, c3.size());
        // Force cleanup to bring out any memory bugs
        c2.dispose();
    }
    @Test public void intersect() {
        Caps c2 = Caps.fromString("video/x-raw,format=(string)I420,width=20");
        Caps c1 = Caps.fromString("video/x-raw,format=(string)I420,height=30");

        Caps ci1 = c2.intersect(c1);
        Assert.assertEquals("Intersected Caps structure count incorrect", 1, ci1.size());

        Structure s = ci1.getStructure(0);
        Assert.assertTrue("Incorrect name on intersected structure", s.hasName("video/x-raw"));
        Assert.assertTrue("Intersected structure does not have 'format' field", s.hasField("format"));
        Assert.assertTrue("Intersected structure does not have 'width' field", s.hasField("width"));
        Assert.assertTrue("Intersected structure does not have 'height' field", s.hasField("height"));

        /* with changed order */
        Caps ci2 = c1.intersect(c2);
        Assert.assertEquals("Intersected Caps structure count incorrect", 1, ci2.size());
        s = ci2.getStructure(0);
        Assert.assertTrue("Incorrect name on intersected structure", s.hasName("video/x-raw"));
        Assert.assertTrue("Intersected structure does not have 'format' field", s.hasField("format"));
        Assert.assertTrue("Intersected structure does not have 'width' field", s.hasField("width"));
        Assert.assertTrue("Intersected structure does not have 'height' field", s.hasField("height"));

        Assert.assertTrue("Intersection should be same in both directions", ci1.isEqual(ci2));
        // Force cleanup to bring out any memory bugs
        c2.dispose(); c1.dispose(); ci1.dispose(); ci2.dispose();
    }
    @Test public void intersectUnspecified() {
        /* field not specified = any value possible, so the intersection
         * should keep fields which are only part of one set of caps */
        Caps c2 = Caps.fromString("video/x-raw,format=(string)I420,width=20");
        Caps c1 = Caps.fromString("video/x-raw,format=(string)I420");

        Caps ci1 = c2.intersect(c1);
        Assert.assertEquals("Intersected Caps structure count incorrect", 1, ci1.size());
        Structure s = ci1.getStructure(0);
        Assert.assertTrue("Incorrect name on intersected structure", s.hasName("video/x-raw"));
        Assert.assertTrue("Intersected structure does not have 'format' field", s.hasField("format"));
        Assert.assertTrue("Intersected structure does not have 'width' field", s.hasField("width"));

        /* with changed order */

        Caps ci2 = c1.intersect(c2);
        Assert.assertEquals("Intersected Caps structure count incorrect", 1, ci2.size());
        s = ci2.getStructure(0);
        Assert.assertTrue("Incorrect name on intersected structure", s.hasName("video/x-raw"));
        Assert.assertTrue("Intersected structure does not have 'format' field", s.hasField("format"));
        Assert.assertTrue("Intersected structure does not have 'width' field", s.hasField("width"));
        Assert.assertTrue("Intersection should be same in both directions", ci1.isEqual(ci2));
        // Force cleanup to bring out any memory bugs
        c2.dispose(); c1.dispose(); ci1.dispose(); ci2.dispose();
    }
    @Test public void intersectUnequal() {
        Caps c2 = Caps.fromString("video/x-raw,format=(string)I420,width=20");
        Caps c1 = Caps.fromString("video/x-raw,format=(string)I420,width=30");

        Caps ci1 = c2.intersect(c1);
        Assert.assertTrue("Intersection of unequal caps should be empty", ci1.isEmpty());
        /* with changed order */
        Caps ci2 = c1.intersect(c2);
        Assert.assertTrue("Intersection of unequal caps should be empty", ci1.isEmpty());
        Assert.assertTrue("Intersection should be same in both directions", ci1.isEqual(ci2));
        // Force cleanup to bring out any memory bugs
        c2.dispose(); c1.dispose(); ci1.dispose(); ci2.dispose();
    }

    @Test public void intersectDifferentType() {
        Caps c2 = Caps.fromString("video/x-raw,format=(string)I420,width=20");
        Caps c1 = Caps.fromString("video/x-raw,format=(string)RGB,width=20");

        Caps ci1 = c2.intersect(c1);
        Assert.assertTrue("Intersection of different type caps should be empty", ci1.isEmpty());

        /* with changed order */
        Caps ci2 = c1.intersect(c2);
        Assert.assertTrue("Intersection of different type caps should be empty", ci1.isEmpty());
        Assert.assertTrue("Intersection should be same in both directions", ci1.isEqual(ci2));
        // Force cleanup to bring out any memory bugs
        c2.dispose(); c1.dispose(); ci1.dispose(); ci2.dispose();
    }

}