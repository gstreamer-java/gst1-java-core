package org.freedesktop.gstreamer.lowlevel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jna.Structure;

/**
 *
 * @author Neil C Smith
 */
public class LowLevelStructureTest {

    private final static Logger LOG = Logger.getLogger(LowLevelStructureTest.class.getName());
    private static List<Class<? extends Structure>> structs;
    private static List<Class<? extends Structure>> untestable;

    public LowLevelStructureTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        LowLevelStructureTest.initStructList();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        if (LowLevelStructureTest.untestable == null) {
            LowLevelStructureTest.untestable = new ArrayList<Class<? extends Structure>>();
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void runTest() {

        for (Class<? extends Structure> struct : LowLevelStructureTest.structs) {
            this.testStruct(struct);
        }

        if (!LowLevelStructureTest.untestable.isEmpty()) {
            StringBuilder builder = new StringBuilder("UNTESTABLE:\n");
            for (Class<? extends Structure> struct : LowLevelStructureTest.untestable) {
                builder.append(struct.getName());
                builder.append("\n");
            }
            LowLevelStructureTest.LOG.log(Level.WARNING, builder.toString());
        }

    }

    @SuppressWarnings("unchecked")
    private void testStruct(Class<? extends Structure> struct) {
        LowLevelStructureTest.LOG.log(Level.INFO, "Testing {0}", struct.getName());
        Structure inst = null;
        List<String> fields = null;
        try {
            inst = struct.newInstance();
        } catch (Exception ex) {
//            try {
//                Constructor<? extends Structure> con = struct.getConstructor(Pointer.class);
//                inst = con.newInstance(Pointer.NULL);
//            } catch (Exception ex1) {
            LowLevelStructureTest.untestable.add(struct);
//                assertTrue(false);
            return;

        }
        try {
            Method getFieldOrder = inst.getClass().getDeclaredMethod("getFieldOrder");
            getFieldOrder.setAccessible(true);
            fields = (List<String>) getFieldOrder.invoke(inst);
        } catch (Exception ex) {
            LowLevelStructureTest.LOG.log(Level.SEVERE, "Can't find getFieldOrder() method", ex);
            Assert.assertTrue(false);
        }
        this.testFields(inst, fields);
    }

    private void testFields(Structure inst, List<String> expectedFields) {
        Field[] fields = inst.getClass().getFields();
        List<String> fieldNames = new ArrayList<String>();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldNames.add(field.getName());
            }
        }
        Assert.assertTrue(expectedFields.equals(fieldNames));
    }

    private static void initStructList() {
        LowLevelStructureTest.structs = new ArrayList<Class<? extends Structure>>();

        LowLevelStructureTest.structs.add(BaseSinkAPI.GstBaseSinkStruct.class);
        LowLevelStructureTest.structs.add(BaseSinkAPI.GstBaseSinkAbi.class);
        LowLevelStructureTest.structs.add(BaseSinkAPI.GstBaseSinkClass.class);

        LowLevelStructureTest.structs.add(BaseSrcAPI.GstBaseSrcStruct.class);
        LowLevelStructureTest.structs.add(BaseSrcAPI.GstBaseSrcAbi.class);
        LowLevelStructureTest.structs.add(BaseSrcAPI.GstBaseSrcClass.class);

        LowLevelStructureTest.structs.add(BaseTransformAPI.GstBaseTransformStruct.class);
        LowLevelStructureTest.structs.add(BaseTransformAPI.GstBaseTransformClass.class);

        LowLevelStructureTest.structs.add(GObjectAPI.GTypeClass.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GTypeInstance.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GObjectStruct.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GObjectClass.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GTypeInfo.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GParamSpec.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GParamSpecBoolean.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GParamSpecChar.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GParamSpecDouble.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GParamSpecFloat.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GParamSpecInt.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GParamSpecInt64.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GParamSpecLong.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GParamSpecString.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GParamSpecUChar.class);
        LowLevelStructureTest.structs.add(GObjectAPI.GParamSpecUInt.class);

        LowLevelStructureTest.structs.add(GSignalAPI.GSignalQuery.class);

        LowLevelStructureTest.structs.add(GValueAPI.GValue.class);
        LowLevelStructureTest.structs.add(GValueAPI.GValueArray.class);

        LowLevelStructureTest.structs.add(GValueStruct.class);

        LowLevelStructureTest.structs.add(GlibAPI.GList.class);
        LowLevelStructureTest.structs.add(GlibAPI.GSList.class);

        LowLevelStructureTest.structs.add(GstSegmentAPI.GstSegmentStruct.class);
        LowLevelStructureTest.structs.add(GstAPI.GErrorStruct.class);

        LowLevelStructureTest.structs.add(GstBufferAPI.BufferStruct.class);

        LowLevelStructureTest.structs.add(GstCapsAPI.GPtrArray.class);
        LowLevelStructureTest.structs.add(GstCapsAPI.GstCapsStruct.class);
        LowLevelStructureTest.structs.add(GstCapsAPI.GstStaticCapsStruct.class);

        LowLevelStructureTest.structs.add(GstColorBalanceAPI.ColorBalanceChannelStruct.class);

        LowLevelStructureTest.structs.add(GstControlSourceAPI.TimedValue.class);
        LowLevelStructureTest.structs.add(GstControlSourceAPI.ValueArray.class);
        LowLevelStructureTest.structs.add(GstControlSourceAPI.GstControlSourceStruct.class);
        LowLevelStructureTest.structs.add(GstControlSourceAPI.GstControlSourceClass.class);

        // error loading native lib?
        //structs.add(GstControllerAPI.GstControllerStruct.class);
        //structs.add(GstControllerAPI.GstControllerClass.class);

        LowLevelStructureTest.structs.add(GstElementAPI.GstElementDetails.class);
        LowLevelStructureTest.structs.add(GstElementAPI.GstElementStruct.class);
        LowLevelStructureTest.structs.add(GstElementAPI.GstElementClass.class);

        LowLevelStructureTest.structs.add(GstEventAPI.EventStruct.class);

        LowLevelStructureTest.structs.add(GstInterpolationControlSourceAPI.GstInterpolationControlSourceStruct.class);
        LowLevelStructureTest.structs.add(GstInterpolationControlSourceAPI.GstInterpolationControlSourceClass.class);

        LowLevelStructureTest.structs.add(GstLFOControlSourceAPI.GstLFOControlSourceStruct.class);
        LowLevelStructureTest.structs.add(GstLFOControlSourceAPI.GstLFOControlSourceClass.class);

        LowLevelStructureTest.structs.add(GstMessageAPI.MessageStruct.class);

        LowLevelStructureTest.structs.add(GstMiniObjectAPI.MiniObjectStruct.class);

        LowLevelStructureTest.structs.add(GstObjectAPI.GstObjectStruct.class);
        LowLevelStructureTest.structs.add(GstObjectAPI.GstObjectClass.class);

        LowLevelStructureTest.structs.add(GstPadTemplateAPI.GstStaticPadTemplate.class);

        LowLevelStructureTest.structs.add(GstPluginFeatureAPI.TypeNameData.class);

        LowLevelStructureTest.structs.add(GstQueryAPI.QueryStruct.class);



    }
}
