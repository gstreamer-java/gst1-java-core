package org.freedesktop.gstreamer.elements;

public class AacParse extends BaseParse<AacParse>{
	
	public static final String GST_NAME = "aacparse";
    public static final String GTYPE_NAME = "GstAacParse";

    public AacParse() {
		this((String) null);
	}

    public AacParse(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public AacParse(Initializer init) {
		super(init);
	}

}
