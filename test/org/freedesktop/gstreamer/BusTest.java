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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.sun.jna.Platform;

import org.freedesktop.gstreamer.lowlevel.GstMessageAPI;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GErrorStruct;
import org.freedesktop.gstreamer.lowlevel.GstElementAPI;
import org.freedesktop.gstreamer.message.EOSMessage;
import org.freedesktop.gstreamer.message.StateChangedMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.freedesktop.gstreamer.lowlevel.GstElementAPI.GSTELEMENT_API;
import org.freedesktop.gstreamer.message.DurationChangedMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BusTest {    
    public BusTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("BusTest", new String[] {});
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        Gst.deinit();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void endOfStream() {
        final TestPipe pipe  = new TestPipe("endOfStream");
        
        final AtomicBoolean signalFired = new AtomicBoolean(false);
        final AtomicReference<GstObject> signalSource = new AtomicReference<GstObject>();
        Bus.EOS eosSignal = new Bus.EOS() {

            public void endOfStream(GstObject source) {
                signalFired.set(true);
                signalSource.set(source);
                pipe.quit();
            }
        };
        pipe.play();
        pipe.getBus().connect(eosSignal);
        //
        // For the pipeline to post an EOS message, all sink elements must post it
        //
        for (Element elem : pipe.pipe.getSinks()) {
            GSTELEMENT_API.gst_element_post_message(elem, GstMessageAPI.GSTMESSAGE_API.gst_message_new_eos(elem));
        }
        pipe.run();
        pipe.getBus().disconnect(eosSignal);

        assertTrue("EOS signal not received", signalFired.get());
        pipe.dispose();
    }
    @Test
    public void stateChanged() {
        final TestPipe pipe = new TestPipe("stateChanged");
        final AtomicBoolean signalFired = new AtomicBoolean(false);
        
        Bus.STATE_CHANGED stateChanged = new Bus.STATE_CHANGED() {
           
            public void stateChanged(GstObject source, State old, State current, State pending) {
                if (pending == State.PLAYING || current == State.PLAYING) {
                    signalFired.set(true);
                    pipe.quit();
                }
            }
            
        };
        pipe.getBus().connect(stateChanged);
        GSTELEMENT_API.gst_element_post_message(pipe.pipe, 
                new StateChangedMessage(pipe.pipe, State.READY, State.PLAYING, State.VOID_PENDING));
        pipe.run();
        pipe.getBus().disconnect(stateChanged);
        assertTrue("STATE_CHANGED signal not received", signalFired.get());
        pipe.dispose();
    }
    
    @Test
    public void errorMessage() {
        final TestPipe pipe = new TestPipe("errorMessage");
       
        final AtomicBoolean signalFired = new AtomicBoolean(false);
        final AtomicReference<GstObject> signalSource = new AtomicReference<GstObject>();
        Bus.ERROR errorSignal = new Bus.ERROR() {

            public void errorMessage(GstObject source, int code, String message) {
                signalFired.set(true);
                signalSource.set(source);
                pipe.quit();
            }
        };
        pipe.getBus().connect(errorSignal);
        
        GErrorStruct msg = new GErrorStruct();
        GSTELEMENT_API.gst_element_post_message(pipe.src, GstMessageAPI.GSTMESSAGE_API.gst_message_new_error(pipe.src, msg, "testing error messages"));
        pipe.play().run();
        pipe.getBus().disconnect(errorSignal);
        pipe.dispose();
        assertTrue("ERROR signal not received", signalFired.get());
        assertEquals("Incorrect source object on signal", pipe.src, signalSource.get());
    }
    @Test
    public void warningMessage() {
        final TestPipe pipe = new TestPipe("warningMessage");
       
        final AtomicBoolean signalFired = new AtomicBoolean(false);
        final AtomicReference<GstObject> signalSource = new AtomicReference<GstObject>();
        Bus.WARNING signal = new Bus.WARNING() {

            public void warningMessage(GstObject source, int code, String message) {
                signalFired.set(true);
                signalSource.set(source);
                pipe.quit();
            }
        };
        pipe.getBus().connect(signal);
        
        GErrorStruct msg = new GErrorStruct();
        pipe.play();
        GSTELEMENT_API.gst_element_post_message(pipe.src, GstMessageAPI.GSTMESSAGE_API.gst_message_new_warning(pipe.src, msg, "testing warning messages"));
        pipe.run();
        pipe.getBus().disconnect(signal);
        pipe.dispose();
        assertTrue("WARNING signal not received", signalFired.get());
        assertEquals("Incorrect source object on signal", pipe.src, signalSource.get());
    }
    @Test
    public void infoMessage() {
        if (Platform.isWindows()) {
            return; // This test does not work on windows - gst_message_new_info() doesn't exist.
        }
        final TestPipe pipe = new TestPipe("infoMessage");
       
        final AtomicBoolean signalFired = new AtomicBoolean(false);
        final AtomicReference<GstObject> signalSource = new AtomicReference<GstObject>();
        Bus.INFO signal = new Bus.INFO() {

            public void infoMessage(GstObject source, int code, String message) {
                signalFired.set(true);
                signalSource.set(source);
                pipe.quit();
            }
        };
        pipe.getBus().connect(signal);
        
        GErrorStruct msg = new GErrorStruct();
        pipe.play();
        GSTELEMENT_API.gst_element_post_message(pipe.src, GstMessageAPI.GSTMESSAGE_API.gst_message_new_info(pipe.src, msg, "testing warning messages"));
        pipe.run();
        pipe.getBus().disconnect(signal);
        pipe.dispose();
        assertTrue("INFO signal not received", signalFired.get());
        assertEquals("Incorrect source object on signal", pipe.src, signalSource.get());
    }
    @Test
    public void bufferingData() {
        final TestPipe pipe = new TestPipe("bufferingData");
       
        final AtomicBoolean signalFired = new AtomicBoolean(false);
        final AtomicInteger signalValue = new AtomicInteger(-1);
        final AtomicReference<GstObject> signalSource = new AtomicReference<GstObject>();
        final int PERCENT = 95;
        Bus.BUFFERING signal = new Bus.BUFFERING() {

            public void bufferingData(GstObject source, int percent) {
                signalFired.set(true);
                signalValue.set(percent);
                signalSource.set(source);
                pipe.quit();
            }
        };
        pipe.getBus().connect(signal);
        GSTELEMENT_API.gst_element_post_message(pipe.src, GstMessageAPI.GSTMESSAGE_API.gst_message_new_buffering(pipe.src, PERCENT));
        pipe.play().run();
        pipe.getBus().disconnect(signal);
        pipe.dispose();
        assertTrue("BUFFERING signal not received", signalFired.get());
        assertEquals("Wrong percent value received for signal", PERCENT, signalValue.get());
        assertEquals("Incorrect source object on signal", pipe.src, signalSource.get());
    }
    @Test
    public void tagsFound() {
        final TestPipe pipe = new TestPipe("tagsFound");
       
        final AtomicBoolean signalFired = new AtomicBoolean(false);
        final AtomicReference<GstObject> signalSource = new AtomicReference<GstObject>();
        Bus.TAG signal = new Bus.TAG() {

            public void tagsFound(GstObject source, TagList tagList) {
                signalFired.set(true);
                signalSource.set(source);
                pipe.quit();
            }
        };
        pipe.getBus().connect(signal);
        
        TagList tagList = new TagList();
        GSTELEMENT_API.gst_element_post_message(pipe.src, GstMessageAPI.GSTMESSAGE_API.gst_message_new_tag(pipe.src, tagList));
        pipe.play().run();
        pipe.getBus().disconnect(signal);
        pipe.dispose();
        assertTrue("TAG signal not received", signalFired.get());
        assertEquals("Incorrect source object on signal", pipe.src, signalSource.get());
    }
    
    @Test public void durationChanged() {
        final TestPipe pipe = new TestPipe("testDurationChanged");
        final AtomicBoolean signalFired = new AtomicBoolean(false);
        final AtomicReference<GstObject> signalSource = new AtomicReference<>(null);
        Bus.DURATION_CHANGED signal = new Bus.DURATION_CHANGED() {
            @Override
            public void durationChanged(GstObject source) {
                signalFired.set(true);
                signalSource.set(source);
                pipe.quit();
            }
        };
        pipe.getBus().connect(signal);
        GSTELEMENT_API.gst_element_post_message(pipe.src, GstMessageAPI.GSTMESSAGE_API.gst_message_new_duration_changed(pipe.src));
        pipe.play().run();
        pipe.getBus().disconnect(signal);
        pipe.dispose();
        assertTrue("DURATION signal not received", signalFired.get());
        assertEquals("Incorrect source object on signal", pipe.src, signalSource.get());
    }
    
    @Test public void anyMessage() {
        final TestPipe pipe = new TestPipe("anyMessage");
       
        
        final AtomicBoolean signalFired = new AtomicBoolean(false);
        final AtomicReference<GstObject> signalSource = new AtomicReference<GstObject>();
        Bus.MESSAGE listener = new Bus.MESSAGE() {

            public void busMessage(Bus bus, Message msg) {
                signalFired.set(true);
                signalSource.set(msg.getSource());
                pipe.quit();
            }
        };
        pipe.getBus().connect(listener);
        //
        // For the pipeline to post an EOS message, all sink elements must post it
        //
        for (Element elem : pipe.pipe.getSinks()) {
            GSTELEMENT_API.gst_element_post_message(elem, GstMessageAPI.GSTMESSAGE_API.gst_message_new_eos(elem));
        }
        pipe.play().run();
        pipe.getBus().disconnect(listener);

        assertTrue("EOS signal not received", signalFired.get());
        pipe.dispose();
    }
    @Test public void postMessage() {
        final TestPipe pipe = new TestPipe();
       
        
        final AtomicBoolean signalFired = new AtomicBoolean(false);
        final AtomicReference<GstObject> signalSource = new AtomicReference<GstObject>();
        Bus.MESSAGE listener = new Bus.MESSAGE() {

            public void busMessage(Bus bus, Message msg) {
                signalFired.set(true);
                signalSource.set(msg.getSource());
                pipe.quit();
            }
        };
        pipe.getBus().connect(listener);
        pipe.getBus().post(new EOSMessage(pipe.src));
        pipe.run();
        assertTrue("Message not posted", signalFired.get());
        assertEquals("Wrong source in message", pipe.src, signalSource.get());
    }
}
