package org.freedesktop.gstreamer.glib;

import org.freedesktop.gstreamer.GObject;

public class GSocketAddress extends GObject{

	public static final String GTYPE_NAME = "GSocketAddress";

	public GSocketAddress(Initializer init) {
		super(init);
	}

	public GSocketFamily getFamily() {
		return GSocketFamily.fromGioValue((Integer) get("family"));
	}

	
}
