package org.freedesktop.gstreamer.elements;

public class MultiUdpSink extends BaseUdpSink<MultiUdpSink>{
	
	public static final String GST_NAME = "multiudpsink";
    public static final String GTYPE_NAME = "GstMultiUDPSink";

    public MultiUdpSink() {
		this((String) null);
	}

    public MultiUdpSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public MultiUdpSink(Initializer init) {
		super(init);
	}

}
