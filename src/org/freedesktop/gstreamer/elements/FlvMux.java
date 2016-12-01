package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;

public class FlvMux extends Element{
	
	public static final String GST_NAME = "flvmux";
    public static final String GTYPE_NAME = "GstFlvMux";

    public FlvMux() {
		this((String) null);
	}

    public FlvMux(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public FlvMux(Initializer init) {
		super(init);
	}

    public Boolean isStreamable() {
    	return get("streamable");
    }
    
    public FlvMux setStreamable(boolean streamable) {
    	set("streamable", streamable);
    	return this;
    }
}
