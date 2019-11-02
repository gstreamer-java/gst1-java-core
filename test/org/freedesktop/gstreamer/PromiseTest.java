/* 
 * Copyright (c) 2018 Antonio Morales
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class PromiseTest {

    public PromiseTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init(Gst.getVersion(), "PromiseTest", new String[] {});
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        Gst.deinit();
    }
    
    @Test
    public void testReply() {
        if (!Gst.testVersion(1, 14)) {
            return;
        }
        Promise promise = new Promise();

        promise.reply(null);

        PromiseResult promiseStatus = promise.waitResult();

        assertEquals("promise reply state not correct", promiseStatus, PromiseResult.REPLIED);
    }

    @Test
    public void testInterrupt() {
        if (!Gst.testVersion(1, 14)) {
            return;
        }
        Promise promise = new Promise();
        promise.interrupt();

        PromiseResult promiseStatus = promise.waitResult();

        assertEquals("promise reply state not correct", promiseStatus, PromiseResult.INTERRUPTED);
    }

    @Test
    public void testExpire() {
        if (!Gst.testVersion(1, 14)) {
            return;
        }
        Promise promise = new Promise();
        promise.expire();

        PromiseResult promiseStatus = promise.waitResult();

        assertEquals("promise reply state not correct", promiseStatus, PromiseResult.EXPIRED);
    }

    @Test
    public void testInvalidateReply() {
        if (!Gst.testVersion(1, 14)) {
            return;
        }
        Promise promise = new Promise();
        Structure data = new Structure("data");

        assertTrue(Natives.ownsReference(data));
        promise.reply(data);
        assertFalse(Natives.ownsReference(data));
        assertFalse(Natives.validReference(data));
    }

    @Test
    public void testReplyData() {
        if (!Gst.testVersion(1, 14)) {
            return;
        }
        Promise promise = new Promise();
        Structure data = new Structure("data", "test", GType.UINT, 1);
        GPointer pointer = Natives.getPointer(data);

        promise.reply(data);
        assertEquals("promise state not in replied", promise.waitResult(), PromiseResult.REPLIED);

        Structure result = promise.getReply();
        assertEquals("result of promise does not match reply", pointer, Natives.getPointer(result));
    }
}
