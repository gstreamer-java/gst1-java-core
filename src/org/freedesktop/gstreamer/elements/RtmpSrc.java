package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;

public class RtmpSrc extends Element{
	
	public static final String GST_NAME = "rtmpsrc";
    public static final String GTYPE_NAME = "GstRTMPSrc";

    public RtmpSrc() {
		this((String) null);
	}

    public RtmpSrc(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public RtmpSrc(Initializer init) {
		super(init);
	}

    public Integer getBlockSize() {
		return get("blocksize");
	}
	
	public RtmpSrc setBlockSize(int blockSize) {
		set("blocksize", blockSize);
		return this;
	}

    public Integer getNumBuffers() {
		return get("num-buffers");
	}
	
	public RtmpSrc setNumBuffers(int numberPfBuffers) {
		set("num-buffers", numberPfBuffers);
		return this;
	}
	
    public Boolean isTypeFindEnabled() {
    	return get("typefind");
    }
    
    public RtmpSrc setTypeFindEnabled(boolean enabled) {
    	set("typefind", enabled);
    	return this;
    }

    public Boolean isDoTimestampEnabled() {
    	return get("do-timestamp");
    }
    
    public RtmpSrc setDoTimestampEnabled(boolean enabled) {
    	set("do-timestamp", enabled);
    	return this;
    }

    public String getLocation() {
    	return get("location");
    }
    
    public RtmpSrc setLocation(String location) {
    	set("location", location);
    	return this;
    }


}
