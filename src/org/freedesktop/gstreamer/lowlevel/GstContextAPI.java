package org.freedesktop.gstreamer.lowlevel;

import java.util.Arrays;
import java.util.List;

import org.freedesktop.gstreamer.Context;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

public interface GstContextAPI extends com.sun.jna.Library {

	GstContextAPI GSTCONTEXT_API = GstNative.load(GstContextAPI.class);

	Pointer ptr_gst_context_new(String context_type, boolean persistent);

	@CallerOwnsReturn Context gst_context_new(String context_type, boolean persistent);

	String gst_context_get_context_type(Context context);

	boolean gst_context_has_context_type(Context context, String context_type);

	Structure gst_context_get_structure(Context context);

	Structure gst_context_writable_structure(Context context);

	boolean gst_context_is_persistent(Context context);

	public static final class GstContextStruct extends com.sun.jna.Structure {
		
		public MiniObject mini_object;

		/* < private > */
		public volatile String context_type;
		public volatile Structure structure;
		public volatile boolean persistent;

		public GstContextStruct(Pointer handle) {
			super(handle);
		}

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("mini_object", "context_type", "structure", "persistent");
		}
	}
}
