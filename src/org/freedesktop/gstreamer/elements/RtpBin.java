package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Bin;

public class RtpBin extends Bin {
	
	public static final String GST_NAME = "rtpbin";
    public static final String GTYPE_NAME = "GstRtpBin";

    public RtpBin() {
		this((String) null);
	}

    public RtpBin(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public RtpBin(Initializer init) {
		super(init);
	}
    
    

}
