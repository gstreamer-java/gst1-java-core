package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

import com.sun.jna.Pointer;

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

    public static interface REQUEST_PT_MAP {
    	
    	public Caps onRtpMapRequest(RtpBin rtpBin, int session, int pt);
    	
    }

    public void connect(final REQUEST_PT_MAP listener) {
        connect(REQUEST_PT_MAP.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public Caps callback(Pointer rtpBin, int session, int pt, Pointer data) {
               return listener.onRtpMapRequest(RtpBin.this, session, pt);
            }
        });
    }

    public void disconnect(final REQUEST_PT_MAP listener) {
    	disconnect(REQUEST_PT_MAP.class, listener);
    }
    
    public static interface ON_NEW_SSRC {
    	
    	public void onNewSsrc(RtpBin rtpBin, int session, int ssrc);
    	
    }

    
    public void connect(final ON_NEW_SSRC listener) {
        connect(ON_NEW_SSRC.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Pointer rtpBin, int session, int ssrc, Pointer data) {
                listener.onNewSsrc(RtpBin.this, session, ssrc);
            }
        });
    }

    public void disconnect(final ON_NEW_SSRC listener) {
    	disconnect(ON_NEW_SSRC.class, listener);
    }
    

}
