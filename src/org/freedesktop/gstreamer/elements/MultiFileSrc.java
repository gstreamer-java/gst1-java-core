package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Caps;

public class MultiFileSrc extends PushSrc {
	
	public static final String GST_NAME = "multifilesrc";
    public static final String GTYPE_NAME = "GstMultiFileSrc";

    public MultiFileSrc() {
		this((String) null);
	}

    public MultiFileSrc(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public MultiFileSrc(Initializer init) {
		super(init);
	}

    public Caps getCaps() {
    	return get("caps");
    }

    public Integer getIndex() {
    	return get("index");
    }
    
    public MultiFileSrc setIndex(int index) {
    	set("index",index);
    	return this;
    }

    public String getLocation() {
    	return get("location");
    }
    
    public MultiFileSrc setLocation(String location) {
    	set("location",location);
    	return this;
    }
    
    public Boolean isLoopEnabled() {
    	return get("loop");
    }
    
    public MultiFileSrc setLoopEnabled(boolean enabled) {
    	set("loop",enabled);
    	return this;
    }
    
    public Integer getStartIndex() {
    	return get("start-index");
    }
    
    public MultiFileSrc setStartIndex(int index) {
    	set("start-index",index);
    	return this;
    }

    public Integer getStopIndex() {
    	return get("stop-index");
    }
    
    public MultiFileSrc setStopIndex(int index) {
    	set("stop-index",index);
    	return this;
    }

}
