package org.freedesktop.gstreamer.elements;

public class RtpPcmuPay extends RtpBaseAudioPayload<RtpPcmuPay>{
	
	public static final String GST_NAME = "rtppcmupay";
    public static final String GTYPE_NAME = "GstRtpPcmuPay";

    public RtpPcmuPay() {
		this((String) null);
	}

    public RtpPcmuPay(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public RtpPcmuPay(Initializer init) {
		super(init);
	}


}
