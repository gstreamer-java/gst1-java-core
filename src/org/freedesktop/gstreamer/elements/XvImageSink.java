package org.freedesktop.gstreamer.elements;

public class XvImageSink extends VideoSink<XvImageSink>{
	
	public static final String GST_NAME = "xvimagesink";
    public static final String GTYPE_NAME = "GstXvImageSink";

    public XvImageSink() {
		this((String) null);
	}

    public XvImageSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public XvImageSink(Initializer init) {
		super(init);
	}

   

}
