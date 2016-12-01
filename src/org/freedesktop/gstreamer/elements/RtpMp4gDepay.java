package org.freedesktop.gstreamer.elements;

public class RtpMp4gDepay extends RtpBaseDepayload<RtpMp4gDepay>{
	
	public static final String GST_NAME = "rtpmp4gdepay";
    public static final String GTYPE_NAME = "GstRtpPcmuDepay";

    public RtpMp4gDepay() {
		this((String) null);
	}

    public RtpMp4gDepay(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public RtpMp4gDepay(Initializer init) {
		super(init);
	}
    
}
