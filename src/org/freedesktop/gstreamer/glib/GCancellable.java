package org.freedesktop.gstreamer.glib;

import org.freedesktop.gstreamer.GObject;
import org.freedesktop.gstreamer.lowlevel.GioAPI;

public class GCancellable extends GObject{

	public static final String GTYPE_NAME = "GCancellable";

	public GCancellable() {
		this(initializer(GioAPI.g_cancellable_new()));
	}
	
	public GCancellable(Initializer init) {
		super(init);
	}

}
