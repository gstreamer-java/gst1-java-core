package org.freedesktop.gstreamer.meta;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.util.TestAssumptions;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Petr Lastovka
 */
public class MetaDataFactoryTest {


    private MetaDataFactory metaDataFactory;
    private Pointer mockPointer;

    @BeforeClass
    public static void beforeClass() throws Exception {
        Gst.init();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        Gst.deinit();
    }

    @Before
    public void setUp() {
        metaDataFactory = new MetaDataFactory();
        mockPointer = new Memory(1000000);
    }

    @Test
    public void testGetGType() {
        TestAssumptions.requireGstVersion(1, 10);
        GType gType = metaDataFactory.getGType(VideoTimeCodeMeta.class);
        assertNotNull("In factory should be videotimecode meta already registered", gType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGTypeUnknown() {
        Meta tempMeta = new Meta() {
        };
        metaDataFactory.getGType(tempMeta.getClass());
    }

    @Test
    public void testGetInstance() {
        TestAssumptions.requireGstVersion(1, 10);
        VideoTimeCodeMeta meta = metaDataFactory.getInstance(VideoTimeCodeMeta.class, mockPointer);
        assertNotNull("In factory should be videotimecode meta already registered", meta);
    }

    @Test
    public void testRegisterMetadata() {
        // register some dummy implementation to already existed factory
        MetaDataFactory.registerMetadata(DummyMeta.class, new MetaDataFactory.MetaDataCrate(() -> GType.OBJECT, mockPointer -> new DummyMeta()));

        // get GType
        GType gType = metaDataFactory.getGType(DummyMeta.class);
        assertNotNull("Returned value should not be null", gType);
        assertEquals("Registered GType should be GType.Object", GType.OBJECT, gType);

        // get instance
        DummyMeta dummyMeta = metaDataFactory.getInstance(DummyMeta.class, null);
        assertNotNull("Because DummyMeta is already registered return value should be not null", dummyMeta);
    }

    private static class DummyMeta implements Meta {
        public DummyMeta() {
        }
    }
}