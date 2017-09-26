package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Bin;

public class HlsDemux extends Bin {
	
	public static final String GST_NAME = "hlsdemux";
    public static final String GTYPE_NAME = "GstHLSDemux";

    public HlsDemux() {
		this((String) null);
	}

    public HlsDemux(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public HlsDemux(Initializer init) {
		super(init);
	}
    
    public int getConnectionSpeed() {
    	return get("connection-speed");
    }

    public HlsDemux setConnectionSpeed(int speedInKbps) {
    	set("connection-speed", speedInKbps);
    	return this;
    }
    
    public boolean isAsyncHandlingEnabled() {
    	return get("async-handling");
    }
    
    public HlsDemux setAsyncHandlingEnabled(boolean enabled) {
    	set("async-handling", enabled);
    	return this;
    }
    
    public boolean isMessageForwardEnabled() {
    	return get("message-forward");
    }
    
    public HlsDemux setMessageForwardEnabled(boolean enabled) {
    	set("message-forward", enabled);
    	return this;
    }

    public double getBitrateLimit() {
    	return get("bitrate-limit");
    }
    
    public HlsDemux setBitrateLimit(double normalizedBitrateLimit) {
    	set("bitrate-limit", normalizedBitrateLimit);
    	return this;
    }
    
    
}
