package org.freedesktop.gstreamer;

import com.sun.jna.Pointer;

import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstCapsAPI;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI;
import org.freedesktop.gstreamer.lowlevel.GstStructureAPI;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.stream.IntStream;

public class NativeObjectTest {
    @BeforeClass
    public static void setUpClass() {
        Gst.init(Gst.getVersion(), "NativeObjectTest");
    }

    @AfterClass
    public static void tearDownClass() {
        Gst.deinit();
    }

    private interface NativeResourceFactory<T> {
        String name();

        Pointer getNativePointer(T object);

        T newNativeObject();

        void disownNativeObject(T object);

        void freeNativeObject(T object);
    }

    private static abstract class AbstractNativeObjectFactory<T extends NativeObject> implements NativeResourceFactory<T> {
        @Override
        public final Pointer getNativePointer(NativeObject object) {
            return Natives.getRawPointer(object);
        }

        @Override
        public final void disownNativeObject(NativeObject object) {
            object.disown();
        }
    }

    private static abstract class AbstractNativeMiniObjectFactory<T extends MiniObject> extends AbstractNativeObjectFactory<T> {
        @Override
        public final void freeNativeObject(T object) {
            GstMiniObjectAPI.GSTMINIOBJECT_API.gst_mini_object_unref(getNativePointer(object));
        }
    }

    private static class StructureNativeObjectFactory extends AbstractNativeObjectFactory<Structure> {
        @Override
        public String name() {
            return "structure";
        }

        @Override
        public Structure newNativeObject() {
            return GstStructureAPI.GSTSTRUCTURE_API.gst_structure_new_empty("name");
        }

        @Override
        public void freeNativeObject(Structure object) {
            GstStructureAPI.GSTSTRUCTURE_API.gst_structure_free(getNativePointer(object));
        }
    }

    private static class CapsNativeObjectFactory extends AbstractNativeMiniObjectFactory<Caps> {
        @Override
        public String name() {
            return "caps";
        }

        @Override
        public Caps newNativeObject() {
            return GstCapsAPI.GSTCAPS_API.gst_caps_new_empty();
        }
    }

    private <T extends NativeObject> void testNativeObjectReuse(NativeResourceFactory<T> resourceFactory) {
        IntStream.range(0, 10000).sequential().forEach(i -> {
            T object = resourceFactory.newNativeObject();
            int refCount = object instanceof MiniObject ? ((MiniObject) object).getRefCount() : 1;
            String message = String.format("New %s should have only 1 owner", resourceFactory.name());
            Assert.assertEquals(message, 1, refCount);
            resourceFactory.disownNativeObject(object);
            resourceFactory.freeNativeObject(object);
        });
    }

    @Test
    public void testNativeObjectStructureReuse() {
        testNativeObjectReuse(new StructureNativeObjectFactory());
    }

    @Test
    public void testNativeObjectCapsReuse() {
        testNativeObjectReuse(new CapsNativeObjectFactory());
    }
}
