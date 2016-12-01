package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Structure;

public class RtpBaseDepayload<Child> extends Element{
	
    public static final String GTYPE_NAME = "GstRTPBaseDepayload";
	
    public RtpBaseDepayload(Initializer init) {
		super(init);
	}

	public Structure getStats() {
		return get("stats");
	}

}
