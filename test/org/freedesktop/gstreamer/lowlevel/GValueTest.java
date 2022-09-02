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

package org.freedesktop.gstreamer.lowlevel;

import org.freedesktop.gstreamer.lowlevel.GNative;
import org.freedesktop.gstreamer.lowlevel.GValueAPI;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GstElementFactoryAPI;
import static org.junit.Assert.*;

import java.util.HashMap;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValueArray;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 *
 */
public class GValueTest {
    private static final GValueAPI api = GValueAPI.GVALUE_API;

    public interface GValueTestAPI extends Library {

        @SuppressWarnings("serial")
        GValueTestAPI API = GNative.loadLibrary("gobject-2.0", GValueTestAPI.class,
                new HashMap<String, Object>() {
        });

        void g_value_set_object(Pointer value, Pointer obj);

        Pointer g_value_get_object(Pointer pointer);

    }
	
    public GValueTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("GValueTest", new String[] {});
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

    @Test public void testGValueArray() throws Exception {
        testGValueArray(new GValueArray());
        testGValueArray(new GValueArray(2));
        testGValueArray(new GValueArray(5));
    }
    
    private void testGValueArray(GValueArray gva) throws Exception {
        
        gva.append(new GValue(GType.INT, 5));        
        gva.append(new GValue(GType.DOUBLE, 5.0));        
        gva.append(new GValue(GType.STRING, "omanipadmihoom"));
        
        assertEquals("vrong n_value", 3, gva.getNValues());
        
        assertEquals("value mismatch", 5, gva.getValue(0));
        assertEquals("value mismatch", 5.0, gva.getValue(1));
        assertEquals("value mismatch", "omanipadmihoom", gva.getValue(2));
        
        gva.free();
    }
    
    @Test public void testInitSet() throws Exception {
        
        GValue v = new GValue(GType.INT);
        
        assertEquals("type mismatch", GType.INT, v.getType());  
        
        try {
            v.setValue(null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
                
        try {
            v.setValue(0.2);
            fail("IllegalArgumentException should have been thrown");
        } catch (ClassCastException e) {}
        

        v.setValue(42);
        
        assertEquals("wrong value", 42, v.getValue());
        
    }

    @Test public void testInitValue() throws Exception {
        
        GValue v;
        
        
        try {
            v = new GValue(GType.INT, null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {}
                
        try {
            v = new GValue(GType.INT, 0.2);
            fail("IllegalArgumentException should have been thrown");
        } catch (ClassCastException e) {}
        
        v = new GValue(GType.DOUBLE, 42.0);
        
        assertEquals("type mismatch", GType.DOUBLE, v.getType());  

        
        assertEquals("wrong value", 42.0, v.getValue());
        
    }

    @Test public void testInt() throws Exception {
    	GValue v = new GValue();
    	api.g_value_init(v, GType.INT);
    	api.g_value_set_int(v, 5);
    	
    	assertEquals("int value mismatch", 5, v.getValue());
    	
    	api.g_value_set_int(v, 6);
    	
    	assertEquals("int value mismatch", 6, v.getValue());
    	
    	assertTrue("type mismatch", v.getValue() instanceof Integer);
    	
    	
    }

    /**
     * Test type conversion of object value when using
     * an object created 'the proper way'
     */
    @Test public void testObjectPtrRef() throws Exception {
	// the following probably puts 'e' into the object reference map

    	Element e = ElementFactory.make("fakesink", "fakesink");
    	
    	GValue v = new GValue();
    	api.g_value_init(v, GType.OBJECT);
    	api.g_value_set_object(v, e);
    	
    	Object obj = v.getValue();
    	
    	assertTrue("type mismatch", obj instanceof Element);

    	assertEquals("object mismatch", e, obj);
    }
    
    /**
     * Test type conversion of object value trying to bypass the object reference map
     */
     @Test public void testObjectTypeMap() throws Exception {

	 Pointer p;
	 
	 {
	     /*
	      * Not using ElementFactory.make() here probably prevents the element 
	      * from being placed in the object reference map and therefore forces
	      * type mapper conversion - what we want to test
	      */
	     
	     ElementFactory factory = GstElementFactoryAPI.GSTELEMENTFACTORY_API.gst_element_factory_find("videotestsrc");
	     p = GstElementFactoryAPI.GSTELEMENTFACTORY_API.ptr_gst_element_factory_create(factory, "videotestsrc");
	 }
    	    	
    	GValue v = new GValue();
    	api.g_value_init(v, GType.OBJECT);
    	
    	GValueTestAPI.API.g_value_set_object(v.getPointer(), p);
    	
    	Object obj = v.getValue();

    	assertTrue("type mismatch", obj instanceof Element);
    }
}
