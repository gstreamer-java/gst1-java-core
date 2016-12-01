package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;

public class FlvDemux extends Element{
	
	public static final String GST_NAME = "flvdemux";
    public static final String GTYPE_NAME = "GstFlvDemux";

    public FlvDemux() {
		this((String) null);
	}

    public FlvDemux(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public FlvDemux(Initializer init) {
		super(init);
	}

}
