/* 
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

import org.freedesktop.gstreamer.message.Message;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.freedesktop.gstreamer.message.TagMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wayne
 */
public class ElementTest {

    public ElementTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("ElementTest", new String[] {});
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void getPads() {
        Element element = ElementFactory.make("fakesink", "fs");
        List<Pad> pads = element.getPads();
        assertTrue("no pads found", !pads.isEmpty());
    }
    @Test
    public void getSinkPads() {
        Element element = ElementFactory.make("fakesink", "fs");
        List<Pad> pads = element.getSinkPads();
        assertTrue("no pads found", !pads.isEmpty());
    }
    @Test
    public void getSrcPads() {
        Element element = ElementFactory.make("fakesrc", "fs");
        List<Pad> pads = element.getSrcPads();
        assertTrue("no pads found", !pads.isEmpty());
    }
    @Test 
    public void setState() {
        Element element = ElementFactory.make("fakesrc", "fs");
        // This should exercise EnumMapper.intValue()
        element.play();
        element.stop();
    }
    @Test 
    public void getState() {
        Element element = ElementFactory.make("fakesrc", "fs");
        // This should exercise EnumMapper.intValue()
        element.play();
        State state = element.getState(-1);
        assertEquals("Element state not set correctly", State.PLAYING, state);
        element.stop();
    }
    @Test public void postMessage() {
        final TestPipe pipe = new TestPipe();
        final AtomicBoolean signalFired = new AtomicBoolean(false);
        //
        // Use a TagMessage, since it is the only type that doesn't get intercepted 
        // by the pipeline
        //
        final Message message = new TagMessage(pipe.src, new TagList());
        pipe.getBus().connect(new Bus.MESSAGE() {

            public void busMessage(Bus bus, Message msg) {
                if (msg.equals(message)) {
                    signalFired.set(true);
                    pipe.quit();
                }
            }
        });
        pipe.sink.postMessage(message);
        pipe.run();
        assertTrue("Message not posted", signalFired.get());
    }
}