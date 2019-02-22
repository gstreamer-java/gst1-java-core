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

import org.freedesktop.gstreamer.glib.GError;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.freedesktop.gstreamer.lowlevel.GstBinAPI;
import org.freedesktop.gstreamer.lowlevel.GstPipelineAPI;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author wayne
 */
public class BinTest {    
    public BinTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("BinTest", new String[] {});
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
    public void testGetElements() {
        Bin bin = new Bin("test");
        Element e1 = ElementFactory.make("fakesrc", "source");
        Element e2 = ElementFactory.make("fakesink", "sink");
        bin.addMany(e1, e2);
        List<Element> elements = bin.getElements();
        assertFalse("Bin returned empty list from getElements", elements.isEmpty());
        assertTrue("Element list does not contain e1", elements.contains(e1));
        assertTrue("Element list does not contain e2", elements.contains(e2));
    }
    @Test
    public void testGetSinks() throws Exception {
        Bin bin = new Bin("test");
        Element e1 = ElementFactory.make("fakesrc", "source");
        Element e2 = ElementFactory.make("fakesink", "sink");
        bin.addMany(e1, e2);
        List<Element> elements = bin.getSinks();
        assertFalse("Bin returned empty list from getElements", elements.isEmpty());
        assertTrue("Element list does not contain sink", elements.contains(e2));
    }
    
    @Test
    public void testGetSources() throws Exception {
        Bin bin = new Bin("test");
        Element e1 = ElementFactory.make("fakesrc", "source");
        Element e2 = ElementFactory.make("fakesink", "sink");
        bin.addMany(e1, e2);
        List<Element> elements = bin.getSources();
        assertFalse("Bin returned empty list from getElements", elements.isEmpty());
        assertTrue("Element list does not contain source", elements.contains(e1));
    }
    @Test
    public void testGetElementByName() throws Exception {
        Bin bin = new Bin("test");
        Element e1 = ElementFactory.make("fakesrc", "source");
        Element e2 = ElementFactory.make("fakesink", "sink");
        bin.addMany(e1, e2);
        
        assertEquals("source not returned", e1, bin.getElementByName("source"));
        assertEquals("sink not returned", e2, bin.getElementByName("sink"));
    }
    
    @Test
    public void testElementAddedCallback() throws Exception {
        Bin bin = new Bin("test");
        final Element e1 = ElementFactory.make("fakesrc", "source");
        final Element e2 = ElementFactory.make("fakesink", "sink");
        final AtomicInteger added = new AtomicInteger(0);
        
        bin.connect(new Bin.ELEMENT_ADDED() {
            public void elementAdded(Bin bin, Element elem) {
                if (elem == e1 || elem == e2) {
                    added.incrementAndGet();
                }
            }
        });
        bin.addMany(e1, e2);
        
        assertEquals("Callback not called", 2, added.get());
    }
    @Test
    public void testElementRemovedCallback() {
        Bin bin = new Bin("test");
        final Element e1 = ElementFactory.make("fakesrc", "source");
        final Element e2 = ElementFactory.make("fakesink", "sink");
        final AtomicInteger removed = new AtomicInteger(0);
        
        bin.connect(new Bin.ELEMENT_ADDED() {
            public void elementAdded(Bin bin, Element elem) {
                if (elem == e1 || elem == e2) {
                    removed.incrementAndGet();
                }
            }
        });
        bin.addMany(e1, e2);
        
        assertEquals("Callback not called", 2, removed.get());
    }
    @Test 
    public void addLinked()
        throws PadLinkException
    {
        /* adding an element with linked pads to a bin unlinks the pads */
        Pipeline pipeline = new Pipeline((String) null);
        assertNotNull("Could not create pipeline", pipeline);

        Element src = ElementFactory.make("fakesrc", null);
        assertNotNull("Could not create fakesrc", src);
        Element sink = ElementFactory.make("fakesink", null);
        assertNotNull("Could not create fakesink", sink);

        Pad srcpad = src.getStaticPad("src");
        assertNotNull("Could not get src pad", srcpad);
        Pad sinkpad = sink.getStaticPad("sink");
        assertNotNull("Could not get sink pad", sinkpad);
        
        srcpad.link(sinkpad);

        /* pads are linked now */
        assertTrue("srcpad not linked", srcpad.isLinked());
        assertTrue("sinkpad not linked", sinkpad.isLinked());
        
        /* adding element to bin voids hierarchy so pads are unlinked */
        pipeline.add(src);

        /* check if pads really are unlinked */
        assertFalse("srcpad is still linked after being added to bin", srcpad.isLinked());
        assertFalse("sinkpad is still linked after being added to bin", sinkpad.isLinked());
        
        /* cannot link pads in wrong hierarchy */
        try {
            srcpad.link(sinkpad);
            fail("Should not be able to link pads in different hierarchy");
        } catch (PadLinkException e) {
            assertEquals("Should not be able to link pads in different hierarchy",
                PadLinkReturn.WRONG_HIERARCHY, e.getLinkResult());
        }

        /* adding other element to bin as well */
        pipeline.add(sink);

        /* now we can link again */
        srcpad.link(sinkpad);

        /* check if pads really are linked */
        assertTrue("srcpad not linked", srcpad.isLinked());
        assertTrue("sinkpad not linked", sinkpad.isLinked());
        
        // Force disposal to flush out any refcounting bugs.
        pipeline.dispose(); src.dispose(); sink.dispose(); srcpad.dispose(); sinkpad.dispose();
    }
    @Test
    public void addSelf() {
        Bin bin = new Bin("");
        // Enable the line below once we know how to avoid gstreamer spitting out warnings
        //assertFalse("Should not be able to add bin to itself", bin.add(bin));
        bin.dispose();
    }
    // This test doesn't work correctly on older gstreamer?
    //@Test 
    public void iterateSorted() {
        Pipeline pipeline = GstPipelineAPI.GSTPIPELINE_API.gst_pipeline_new(null);
        assertNotNull("Failed to create Pipeline", pipeline);
        Bin bin = GstBinAPI.GSTBIN_API.gst_bin_new(null);
        assertNotNull("Failed to create bin", bin);

        Element src = ElementFactory.make("fakesrc", null);
        assertNotNull("Failed to create fakesrc", src);

        Element tee = ElementFactory.make("tee", null);
        assertNotNull("Failed to create tee", tee);

        Element sink1 = ElementFactory.make("fakesink", null);
        assertNotNull("Failed to create fakesink", sink1);

        bin.addMany(src, tee, sink1);
        assertTrue("Could not link fakesrc to tee", src.link(tee));
        assertTrue("Could not link tee to fakesink", tee.link(sink1));

        Element identity = ElementFactory.make("identity", null);
        assertNotNull("Failed to create identity", identity);
        

        Element sink2 = ElementFactory.make("fakesink", null);
        assertNotNull("Failed to create fakesink", sink2);
        pipeline.addMany(bin, identity, sink2);
//  gst_bin_add_many (GST_BIN (pipeline), bin, identity, sink2, NULL);
        assertTrue("Could not link tee to identity", tee.link(identity));
        assertTrue("Could not link identity to second fakesink", identity.link(sink2));
        Iterator<Element> it = pipeline.getElementsSorted().iterator();
        
        assertEquals("First sorted element should be sink2", sink2, it.next());
        assertEquals("Second sorted element should be identity", identity, it.next());
        assertEquals("Third sorted element should be bin", bin, it.next());
        pipeline.dispose();
    }
    
    @Test
    public void testParseBin() {
        ArrayList<GError> errors = new ArrayList<GError>();
        Bin bin = Gst.parseBinFromDescription("fakesrc ! fakesink", false, errors);
        assertNotNull("Bin not created", bin);
        assertEquals("parseBinFromDescription with error!", errors.size(), 0);
    }   
    @Test
    public void testParseBinElementCount() {
        ArrayList<GError> errors = new ArrayList<GError>();
        Bin bin = Gst.parseBinFromDescription("fakesrc ! fakesink", false, errors);
        assertEquals("Number of elements in pipeline incorrect", 2, bin.getElements().size());
        assertEquals("parseBinFromDescription with error!", errors.size(), 0);
    }
    @Test
    public void testParseBinSrcElement() {
        ArrayList<GError> errors = new ArrayList<GError>();
    	Bin bin = Gst.parseBinFromDescription("fakesrc ! fakesink", false, errors);
        assertEquals("First element not a fakesrc", "fakesrc", bin.getSources().get(0).getFactory().getName());
        assertEquals("parseBinFromDescription with error!", errors.size(), 0);
    }
    @Test
    public void testParseBinSinkElement() {
        ArrayList<GError> errors = new ArrayList<GError>();
    	Bin bin = Gst.parseBinFromDescription("fakesrc ! fakesink", false, errors);
        assertEquals("First element not a fakesink", "fakesink", bin.getSinks().get(0).getFactory().getName());
        assertEquals("parseBinFromDescription with error!", errors.size(), 0);
    }
    @Test
    public void testParseBinDisabledGhostPadsForSource() {
        ArrayList<GError> errors = new ArrayList<GError>();
    	Bin bin = Gst.parseBinFromDescription("fakesrc", false, errors);
    	assertEquals("Number of src pads incorrect", 0, bin.getSrcPads().size());
        assertEquals("parseBinFromDescription with error!", errors.size(), 0);
    }
    @Test
    public void testParseBinDisabledGhostPadsForSink() {
        ArrayList<GError> errors = new ArrayList<GError>();
    	Bin bin = Gst.parseBinFromDescription("fakesink", false, errors);
    	assertEquals("Number of sink pads incorrect", 0, bin.getSinkPads().size());
        assertEquals("parseBinFromDescription with error!", errors.size(), 0);
    }
    @Test
    public void testParseBinEnabledGhostPadsForSource() {
        ArrayList<GError> errors = new ArrayList<GError>();
    	Bin bin = Gst.parseBinFromDescription("fakesrc", true, errors);
    	assertEquals("Number of src pads incorrect", 1, bin.getSrcPads().size());
        assertEquals("parseBinFromDescription with error!", errors.size(), 0);
    }
    @Test
    public void testParseBinEnabledGhostPadsForSink() {
        ArrayList<GError> errors = new ArrayList<GError>();
    	Bin bin = Gst.parseBinFromDescription("fakesink", true, errors);
    	assertEquals("Number of sink pads incorrect", 1, bin.getSinkPads().size());
        assertEquals("parseBinFromDescription with error!", errors.size(), 0);
    }
    @Test
    public void testParseBinEnabledGhostPadsForSourceWithNoUsablePads() {
        ArrayList<GError> errors = new ArrayList<GError>();
    	Bin bin = Gst.parseBinFromDescription("fakesrc ! fakesink", true, errors);
    	assertEquals("Number of src pads incorrect", 0, bin.getSrcPads().size());
        assertEquals("parseBinFromDescription with error!", errors.size(), 0);
    }
    @Test
    public void testParseBinEnabledGhostPadsForSinkWithNoUsablePads() {
        ArrayList<GError> errors = new ArrayList<GError>();
    	Bin bin = Gst.parseBinFromDescription("fakesrc ! fakesink", true, errors);
    	assertEquals("Number of sink pads incorrect", 0, bin.getSinkPads().size());
        assertEquals("parseBinFromDescription with error!", errors.size(), 0);
    }
    @Test
    public void testParseBinEnabledGhostPadsWithNoUsablePads() {
        ArrayList<GError> errors = new ArrayList<GError>();
    	Bin bin = Gst.parseBinFromDescription("fakesrc ! fakesink", true, errors);
    	assertEquals("Number of pads incorrect", 0, bin.getPads().size());
        assertEquals("parseBinFromDescription with error!", errors.size(), 0);
    }
}
