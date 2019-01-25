package org.freedesktop.gstreamer.lowlevel;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jna.Structure;
import org.freedesktop.gstreamer.Gst;

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
        initStructList();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        if (untestable == null) {
            untestable = new ArrayList<Class<? extends Structure>>();
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void runTest() {

        for (Class<? extends Structure> struct : structs) {
            testStruct(struct);
        }

        if (!untestable.isEmpty()) {
            StringBuilder builder = new StringBuilder("UNTESTABLE:\n");
            for (Class<? extends Structure> struct : untestable) {
                builder.append(struct.getName());
                builder.append("\n");
            }
            LOG.log(Level.WARNING, builder.toString());
        }

    }

    @SuppressWarnings("unchecked")
    private void testStruct(Class<? extends Structure> struct) {
        LOG.log(Level.INFO, "Testing {0}", struct.getName());
        Structure inst = null;
        List<String> fields = null;
        try {
            inst = struct.newInstance();
        } catch (Exception ex) {
//            try {
//                Constructor<? extends Structure> con = struct.getConstructor(Pointer.class);
//                inst = con.newInstance(Pointer.NULL);
//            } catch (Exception ex1) {
            untestable.add(struct);
//                assertTrue(false);
            return;

        }
        try {
            Method getFieldOrder = inst.getClass().getDeclaredMethod("getFieldOrder");
            getFieldOrder.setAccessible(true);
            fields = (List<String>) getFieldOrder.invoke(inst);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Can't find getFieldOrder() method", ex);
            assertTrue(false);
        }
        testFields(inst, fields);
    }

    private void testFields(Structure inst, List<String> expectedFields) {
        Field[] fields = inst.getClass().getFields();
        List<String> fieldNames = new ArrayList<String>();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldNames.add(field.getName());
            }
        }
        assertTrue(expectedFields.equals(fieldNames));
    }

    private static void initStructList() {
        structs = new ArrayList<Class<? extends Structure>>();

        structs.add(BaseSinkAPI.GstBaseSinkStruct.class);
        structs.add(BaseSinkAPI.GstBaseSinkClass.class);

        structs.add(BaseSrcAPI.GstBaseSrcStruct.class);
        structs.add(BaseSrcAPI.GstBaseSrcAbi.class);
        structs.add(BaseSrcAPI.GstBaseSrcClass.class);

        structs.add(BaseTransformAPI.GstBaseTransformStruct.class);
        structs.add(BaseTransformAPI.GstBaseTransformClass.class);

        structs.add(GObjectAPI.GTypeClass.class);
        structs.add(GObjectAPI.GTypeInstance.class);
        structs.add(GObjectAPI.GObjectStruct.class);
        structs.add(GObjectAPI.GObjectClass.class);
        structs.add(GObjectAPI.GTypeInfo.class);
        structs.add(GObjectAPI.GParamSpec.class);
        structs.add(GObjectAPI.GParamSpecBoolean.class);
        structs.add(GObjectAPI.GParamSpecChar.class);
        structs.add(GObjectAPI.GParamSpecDouble.class);
        structs.add(GObjectAPI.GParamSpecFloat.class);
        structs.add(GObjectAPI.GParamSpecInt.class);
        structs.add(GObjectAPI.GParamSpecInt64.class);
        structs.add(GObjectAPI.GParamSpecLong.class);
        structs.add(GObjectAPI.GParamSpecString.class);
        structs.add(GObjectAPI.GParamSpecUChar.class);
        structs.add(GObjectAPI.GParamSpecUInt.class);

        structs.add(GSignalAPI.GSignalQuery.class);

        structs.add(GValueAPI.GValue.class);
        structs.add(GValueAPI.GValueArray.class);

        structs.add(GValueStruct.class);

        structs.add(GlibAPI.GList.class);
        structs.add(GlibAPI.GSList.class);

        structs.add(GstAPI.GstSegmentStruct.class);
        structs.add(GstAPI.GErrorStruct.class);

        structs.add(GstBufferAPI.BufferStruct.class);

        structs.add(GstColorBalanceAPI.ColorBalanceChannelStruct.class);

        structs.add(GstControlSourceAPI.TimedValue.class);
        structs.add(GstControlSourceAPI.GstControlSourceStruct.class);
        structs.add(GstControlSourceAPI.GstControlSourceClass.class);

        structs.add(GstElementAPI.GstElementStruct.class);
        structs.add(GstElementAPI.GstElementClass.class);

        structs.add(GstEventAPI.EventStruct.class);

        structs.add(GstInterpolationControlSourceAPI.GstInterpolationControlSourceStruct.class);
        structs.add(GstInterpolationControlSourceAPI.GstInterpolationControlSourceClass.class);

        structs.add(GstLFOControlSourceAPI.GstLFOControlSourceStruct.class);
        structs.add(GstLFOControlSourceAPI.GstLFOControlSourceClass.class);

        structs.add(GstMessageAPI.MessageStruct.class);

        structs.add(GstMiniObjectAPI.MiniObjectStruct.class);

        structs.add(GstObjectAPI.GstObjectStruct.class);
        structs.add(GstObjectAPI.GstObjectClass.class);

        structs.add(GstQueryAPI.QueryStruct.class);

        if (Gst.getVersion().getMinor() >= 14) {
            structs.add(GstWebRTCSessionDescriptionAPI.WebRTCSessionDescriptionStruct.class);
            structs.add(GstSDPMessageAPI.SDPMessageStruct.class);
            structs.add(GstPromiseAPI.PromiseStruct.class);
        }

    }
}
