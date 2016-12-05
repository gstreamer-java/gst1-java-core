package org.freedesktop.gstreamer.glib;

import org.freedesktop.gstreamer.GObject;
import org.freedesktop.gstreamer.lowlevel.GioAPI;

public class GInetAddress extends GObject{

	public static final String GTYPE_NAME = "GInetAddress";

	public GInetAddress(Initializer init) {
		super(init);
	}

	public String getAddress() {
		return GioAPI.g_inet_address_to_string(getNativeAddress());
	}
	
}
