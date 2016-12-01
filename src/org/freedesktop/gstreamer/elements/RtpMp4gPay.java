package org.freedesktop.gstreamer.elements;

public class RtpMp4gPay extends RtpBaseAudioPayload<RtpMp4gPay>{
	
	public static final String GST_NAME = "rtpmp4gpay";
    public static final String GTYPE_NAME = "GstRtpMP4GPay";

    public RtpMp4gPay() {
		this((String) null);
	}

    public RtpMp4gPay(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public RtpMp4gPay(Initializer init) {
		super(init);
	}


}
