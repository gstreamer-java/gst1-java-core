package org.freedesktop.gstreamer.elements;

public class WebRtcEchoProbe extends AudioFilter{
	
	public static final String GST_NAME = "webrtcechoprobe";
    public static final String GTYPE_NAME = "GstWebrtcEchoProbe";
    

    public WebRtcEchoProbe() {
		this((String) null);
	}

    public WebRtcEchoProbe(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public WebRtcEchoProbe(Initializer init) {
		super(init);
	}

}
