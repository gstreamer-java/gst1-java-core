package org.freedesktop.gstreamer.elements;

public class RtpPcmuDepay extends RtpBaseDepayload<RtpPcmuDepay>{
	
	public static final String GST_NAME = "rtppcmudepay";
    public static final String GTYPE_NAME = "GstRtpMP4GDepay";

    public RtpPcmuDepay() {
		this((String) null);
	}

    public RtpPcmuDepay(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public RtpPcmuDepay(Initializer init) {
		super(init);
	}
    
}
