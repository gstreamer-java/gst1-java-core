package org.freedesktop.gstreamer.elements;

public class RtpH264Depay extends RtpBaseDepayload<RtpH264Depay>{
	
	public static final String GST_NAME = "rtph264depay";
    public static final String GTYPE_NAME = "GstRtpH264Depay";

    public RtpH264Depay() {
		this((String) null);
	}

    public RtpH264Depay(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public RtpH264Depay(Initializer init) {
		super(init);
	}
    
}
