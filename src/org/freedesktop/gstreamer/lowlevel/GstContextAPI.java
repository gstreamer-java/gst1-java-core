package org.freedesktop.gstreamer.lowlevel;

import org.freedesktop.gstreamer.Context;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

/**
 * GstContext functions
 */
public interface GstContextAPI extends com.sun.jna.Library {
	GstContextAPI GSTCONTEXT_API = GstNative.load(GstContextAPI.class);

	@CallerOwnsReturn Context gst_context_new(String context_type, boolean persistent);

	String gst_context_get_context_type(Context context);
	boolean gst_context_has_context_type(Context context, String context_type);

	Structure gst_context_get_structure(Context context);
	Structure gst_context_writable_structure(Context context);
	boolean gst_context_is_persistent(Context context);

}
