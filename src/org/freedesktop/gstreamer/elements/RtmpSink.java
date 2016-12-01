package org.freedesktop.gstreamer.elements;

public class RtmpSink extends BaseSink {
	
	public static final String GST_NAME = "rtmpsink";
    public static final String GTYPE_NAME = "GstRTMPSink";

    public RtmpSink() {
		this((String) null);
	}

    public RtmpSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public RtmpSink(Initializer init) {
		super(init);
	}

    public String getLocation() {
    	return get("location");
    }
    
    public RtmpSink setLocation(String location) {
    	set("location", location);
    	return this;
    }


}
