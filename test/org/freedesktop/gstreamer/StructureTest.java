package org.freedesktop.gstreamer;

import java.util.List;
import static org.junit.Assert.*;

import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GValueAPI;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StructureTest {
	private Structure structure;
	
    @BeforeClass
    public static void setUpClass() throws Exception {
        Gst.init("StructureTest", new String[] {});
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        Gst.deinit();
    }

    @Before
    public void setUp() {
    	structure = new Structure("nazgul");
    }
    
	@Test
	public void testGetName() {
		assertEquals("nazgul", structure.getName());
	}

	@Test
	public void testGetValue() {
		structure.setValue("uint", GType.UINT, 9); 
		assertEquals(9, structure.getValue("uint"));	
		
		try {
			structure.getValue("noexist");
			fail("Structure.InvalidFieldException should have been thrown");
		} catch (Structure.InvalidFieldException e) {}
		
		structure.setDouble("double", 9.0);		
		assertEquals(9.0, structure.getValue("double"));		

		structure.setValue("bool", GType.BOOLEAN, true); 
		assertEquals(true, structure.getValue("bool"));
		
	}
    
    @Test
    public void testGetValues() {
        GValueAPI.GValueArray ar = new GValueAPI.GValueArray(2);
        ar.append(new GValueAPI.GValue(GType.DOUBLE, 7.5));
        ar.append(new GValueAPI.GValue(GType.DOUBLE, 14.3));
        structure.setValue("valuearray", GType.valueOf(GValueAPI.GValueArray.GTYPE_NAME), ar);
        List<Double> doubles = structure.getValues(Double.class, "valuearray");
        assertEquals(7.5, doubles.get(0), 0.001);
        assertEquals(14.3, doubles.get(1), 0.001);
        try {
            List<String> strings = structure.getValues(String.class, "valuearray");
            fail("Trying to extract the wrong type from GValueArray not throwing exception");
        } catch (Structure.InvalidFieldException ex) {
        }
        try {
            List<Double> strings = structure.getValues(Double.class, "non_existent");
            fail("Trying to extract a non-existent GValueArray field");
        } catch (Structure.InvalidFieldException ex) {
        }
    }
    
	@Test
	public void testGetInteger() {
		structure.setInteger("int", 9);		
		assertEquals(9, structure.getInteger("int"));
		
		structure.setInteger("int", -9);		
		assertEquals(-9, structure.getInteger("int"));				
	}
    
    @Test
    public void testGetIntegers() {
        GValueAPI.GValueArray ar = new GValueAPI.GValueArray(2);
        ar.append(new GValueAPI.GValue(GType.INT, 32));
        ar.append(new GValueAPI.GValue(GType.INT, -49));
        structure.setValue("integers", GType.valueOf(GValueAPI.GValueArray.GTYPE_NAME), ar);
        int[] in = new int[2];
        int[] ints = structure.getIntegers("integers", in);
        assertTrue(in == ints);
        assertEquals(32, ints[0]);
        assertEquals(-49, ints[1]);
        
        in = new int[1];
        ints = structure.getIntegers("integers", in);
        assertFalse(in == ints);
        assertEquals(32, ints[0]);
        assertEquals(-49, ints[1]);
        
        structure.setInteger("single_integer", 18);
        int[] single = structure.getIntegers("single_integer", in);
        assertTrue(in == single);
        assertEquals(18, single[0]);
    }

	@Test
	public void testGetDouble() {
		structure.setDouble("double", 9.0);		
		assertEquals(9.0, structure.getDouble("double"), 0);
		
		structure.setDouble("double", -9.0);		
		assertEquals(-9.0, structure.getDouble("double"), 0);				
	}
    
    @Test
    public void testGetDoubles() {
        GValueAPI.GValueArray ar = new GValueAPI.GValueArray(2);
        ar.append(new GValueAPI.GValue(GType.DOUBLE, 3.25));
        ar.append(new GValueAPI.GValue(GType.DOUBLE, 79.6));
        structure.setValue("doubles", GType.valueOf(GValueAPI.GValueArray.GTYPE_NAME), ar);
        double[] in = new double[2];
        double[] doubles = structure.getDoubles("doubles", in);
        assertTrue(in == doubles);
        assertEquals(3.25, doubles[0], 0.001);
        assertEquals(79.6, doubles[1], 0.001);
        
        in = new double[1];
        doubles = structure.getDoubles("doubles", in);
        assertFalse(in == doubles);
        assertEquals(3.25, doubles[0], 0.001);
        assertEquals(79.6, doubles[1], 0.001);
        
        structure.setDouble("single_double", 18.2);
        double[] single = structure.getDoubles("single_double", in);
        assertTrue(in == single);
        assertEquals(18.2, single[0], 0.001);
    }

	@Test
	public void testFraction() {
		structure.setFraction("fraction", 10, 1);

		assertEquals(true, structure.hasField("fraction"));

		assertEquals(10, structure.getFraction("fraction").getNumerator());
		assertEquals(1, structure.getFraction("fraction").getDenominator());

		structure.setFraction("fraction", 17, 10);
		assertEquals(17, structure.getFraction("fraction").getNumerator());
		assertEquals(10, structure.getFraction("fraction").getDenominator());
	}
}
