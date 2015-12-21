package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.GstContextAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

public class Context extends MiniObject {

	private static interface API extends GstContextAPI {
		@CallerOwnsReturn Pointer ptr_gst_context_new(String context_type, boolean persistent);
	}
    private static final API gst = GstNative.load(API.class);


    /**
     * Creates a new context
     *
     * @param init internal initialization data.
     */
    public Context(Initializer init) {
        super(init);
    }

    /**
     * Creates a new context
     * @param context_type The context type that is needed
     * @param persistent
     *
     */
    public Context(String context_type, boolean persistent) {
        this(NativeObject.initializer(Context.gst.ptr_gst_context_new(context_type, persistent)));
    }

    public Structure getWritableStructure() {
    	return Context.gst.gst_context_writable_structure (this);
    }
}
