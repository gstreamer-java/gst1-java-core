package org.gstreamer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.gstreamer.lowlevel.GType;
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
	public void testGetInteger() {
		structure.setInteger("int", 9);		
		assertEquals(9, structure.getInteger("int"));
		
		structure.setInteger("int", -9);		
		assertEquals(-9, structure.getInteger("int"));				
	}


	@Test
	public void testGetDouble() {
		structure.setDouble("double", 9.0);		
		assertEquals(9.0, structure.getDouble("double"), 0);
		
		structure.setDouble("double", -9.0);		
		assertEquals(-9.0, structure.getDouble("double"), 0);				
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
