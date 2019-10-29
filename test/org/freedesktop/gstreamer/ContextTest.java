package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.glib.GCancellable;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstContextAPI;
import org.freedesktop.gstreamer.lowlevel.GstContextPtr;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ContextTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		Gst.init("test");
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		Gst.deinit();
	}

	@Test
	public void testConstruction() {
		GstContextAPI contextApi = GstContextAPI.GSTCONTEXT_API;
		String contextType = "whatever";
		try (Context context = new Context(contextType)) {
			GstContextPtr gstContextPtr = Natives.getPointer(context).as(GstContextPtr.class, GstContextPtr::new);

			// Context type.
			Assert.assertEquals(contextType, context.getContextType());
			Assert.assertTrue(contextApi.gst_context_has_context_type(gstContextPtr, contextType));
			Assert.assertFalse(contextApi.gst_context_has_context_type(gstContextPtr, contextType + ".something-else"));

			// Default is persistent.
			Assert.assertTrue(contextApi.gst_context_is_persistent(gstContextPtr));

			// Generic field value.
			GCancellable anyKindOfObject = new GCancellable();
			context.set("circular-field", GCancellable.GTYPE_NAME, anyKindOfObject);
			Structure structure = contextApi.gst_context_get_structure(gstContextPtr);
			Object value = structure.getValue("circular-field");
			Assert.assertSame(anyKindOfObject, value);
		}
	}

}
