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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.ref.WeakReference;
import java.util.List;

import org.freedesktop.gstreamer.ElementFactory.ListType;
import org.freedesktop.gstreamer.PluginFeature.Rank;
import org.freedesktop.gstreamer.elements.DecodeBin;
import org.freedesktop.gstreamer.elements.PlayBin;
import org.freedesktop.gstreamer.elements.URIDecodeBin;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author wayne
 */
@SuppressWarnings("deprecation")
public class ElementFactoryTest {
    
    public ElementFactoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("test", new String[] {});
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
    public void testMakeFakesink() {
        Element e = ElementFactory.make("fakesink", "sink");
        assertNotNull("Failed to create fakesink", e);
    }
    @Test
    public void testMakeFakesrc() {
        Element e = ElementFactory.make("fakesrc", "source");
        assertNotNull("Failed to create fakesrc", e);
    }
    @Test
    public void testMakeFilesink() {
        Element e = ElementFactory.make("filesink", "sink");
        assertNotNull("Failed to create filesink", e);
    }
    @Test
    public void testMakeFilesrc() {
        Element e = ElementFactory.make("filesrc", "source");
        assertNotNull("Failed to create filesrc", e);
    }
    @Test
    public void testMakeBin() {
        Element e = ElementFactory.make("bin", "bin");
        assertNotNull("Failed to create bin", e);
        assertTrue("Element not a subclass of Bin", e instanceof Bin);
    }
    @Test
    public void testMakePipeline() {
        Element e = ElementFactory.make(Pipeline.GST_NAME, "bin");
        assertNotNull("Failed to create " + Pipeline.GST_NAME, e);
        assertTrue("Element not a subclass of Bin", e instanceof Bin);
        assertTrue("Element not a subclass of Pipeline", e instanceof Pipeline);
    }
    @Test
    public void testMakePlaybin() {
        Element e = ElementFactory.make(PlayBin.GST_NAME, "bin");
        assertNotNull("Failed to create " + PlayBin.GST_NAME, e);
        assertTrue("Element not a subclass of Bin", e instanceof Bin);
        assertTrue("Element not a subclass of Pipeline", e instanceof Pipeline);
        assertTrue("Element not a subclass of PlayBin", e instanceof PlayBin);
    }
    @Test
    public void testMakeDecodeBin() {
        Element e = ElementFactory.make(DecodeBin.GST_NAME, "bin");
        assertNotNull("Failed to create " + DecodeBin.GST_NAME, e);
        assertTrue("Element not a subclass of Bin", e instanceof Bin);
        assertTrue("Element not a subclass of DecodeBin", e instanceof DecodeBin);
    }
    @Test
    public void testMakeURIDecodeBin() {
        Element e = ElementFactory.make(URIDecodeBin.GST_NAME, "bin");
        assertNotNull("Failed to create " + URIDecodeBin.GST_NAME, e);
        assertTrue("Element not a subclass of Bin", e instanceof Bin);
        assertTrue("Element not a subclass of DecodeBin", e instanceof URIDecodeBin);
    }
    @Test
    public void testCreateFakesrc() {
        ElementFactory factory = ElementFactory.find("fakesrc");
        assertNotNull("Could not locate fakesrc factory", factory);
        Element e = factory.create("source");
        assertNotNull("Failed to create fakesrc", e);
    }
    @Test
    public void testCreateBin() {
        ElementFactory factory = ElementFactory.find("bin");
        assertNotNull("Could not locate bin factory", factory);
        Element e = factory.create("bin");
        assertNotNull("Failed to create bin", e);
        assertTrue("Element not a subclass of Bin", e instanceof Bin);
    }
    @Test
    public void testCreatePipeline() {
        ElementFactory factory = ElementFactory.find("pipeline");
        assertNotNull("Could not locate pipeline factory", factory);
        Element e = factory.create("bin");
        assertNotNull("Failed to create pipeline", e);
        assertTrue("Element not a subclass of Bin", e instanceof Bin);
        assertTrue("Element not a subclass of Pipeline", e instanceof Pipeline);
    }
    @Test
    public void testCreatePlaybin() {
        ElementFactory factory = ElementFactory.find("playbin");
        assertNotNull("Could not locate pipeline factory", factory);
        System.out.println("PlayBin factory name=" + factory.getName());
        Element e = factory.create("bin");
        assertNotNull("Failed to create playbin", e);
        assertTrue("Element not a subclass of Bin", e instanceof Bin);
        assertTrue("Element not a subclass of Pipeline", e instanceof Pipeline);
        assertTrue("Element not a subclass of PlayBin", e instanceof PlayBin);
    }
    @Test
    public void testGarbageCollection() throws Throwable {
        ElementFactory factory = ElementFactory.find("fakesrc");
        assertNotNull("Could not locate fakesrc factory", factory);
        WeakReference<ElementFactory> ref = new WeakReference<ElementFactory>(factory);
        factory = null;
        assertTrue("Factory not garbage collected", GCTracker.waitGC(ref));
    }
    @Test
    public void testMakeGarbageCollection() throws Throwable {
        Element e = ElementFactory.make("fakesrc", "test");
        WeakReference<Element> ref = new WeakReference<Element>(e);
        e = null;
        assertTrue("Element not garbage collected", GCTracker.waitGC(ref));
        
    }
    @Test
    public void testCreateGarbageCollection() throws Throwable {
        ElementFactory factory = ElementFactory.find("fakesrc");
        assertNotNull("Could not locate fakesrc factory", factory);
        Element e = factory.create("bin");
        WeakReference<Element> ref = new WeakReference<Element>(e);
        e = null;
        assertTrue("Element not garbage collected", GCTracker.waitGC(ref));
    }
    @Test
    public void getStaticPadTemplates() {
        ElementFactory f = ElementFactory.find("fakesink");
        List<StaticPadTemplate> templates = f.getStaticPadTemplates();
        assertTrue("No static pad templates found", !templates.isEmpty());
        StaticPadTemplate t = templates.get(0);
        assertEquals("Not a sink", "sink", t.getName());
        assertEquals("Not a sink", PadDirection.SINK, t.getDirection());
    }

    @Test
    public void listGetElement() {
        List<ElementFactory> list = ElementFactory.listGetElements(ListType.ANY,
                Rank.NONE);
        assertNotNull("List of factories is null", list);
        assertTrue("No factories found", !list.isEmpty());
//        System.out.println("Factories >>>");
//        for (ElementFactory fact : list) {
//            System.out.println(fact.getName());
//        }
//        System.out.println("<<<");
    }

//    @Test
//    public void filterList() {
//        List<ElementFactory> list = ElementFactory.listGetElements(ListType.ENCODER,
//                Rank.NONE);
//        assertNotNull("List of factories is null", list);
//        assertTrue("No factories found", !list.isEmpty());
//        List<ElementFactory> filterList = ElementFactory.listFilter(list, new Caps("video/x-h263"),
//                PadDirection.SRC, false);
//
//        assertNotNull("List of factories is null", filterList);
//        assertTrue("No factories found", !filterList.isEmpty());
////        System.out.println("Filtered factories >>>");
////        for (ElementFactory fact : filterList) {
////            System.out.println(fact.getName());
////        }
////        System.out.println("<<<");
//    }

    @Test
    public void filterList2() {
        List<ElementFactory> list = ElementFactory.listGetElementsFilter(ListType.ENCODER, Rank.NONE, new Caps("video/x-h263"),
                PadDirection.SRC, false);
        assertNotNull("List of factories is null", list);
        assertTrue("No factories found", !list.isEmpty());

//        System.out.println("Factories >>>");
//        for (ElementFactory fact : list) {
//            System.out.println(fact.getName());
//        }
//        System.out.println("<<<");
    }
    
    @Test
    public void testMetaData() {
        ElementFactory f = ElementFactory.find("fakesink");
        String klass = f.getKlass();
        String longName = f.getLongName();
        String description = f.getDescription();
        String author = f.getAuthor();
        assertNotNull("Klass is null", klass);
        assertNotNull("Long name is null", longName);
        assertNotNull("Description is null", description);
        assertNotNull("Author is null", author);
        System.out.println("FakeSink MetaData");
        System.out.println("Klass : " + f.getKlass());
        System.out.println("Long Name : " + f.getLongName());
        System.out.println("Description : " + f.getDescription());
        System.out.println("Author : " + f.getAuthor());
    }
}
