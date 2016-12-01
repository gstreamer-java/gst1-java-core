package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Caps;

public class CapsSetter extends BaseTransform {
	
	public static final String GST_NAME = "capssetter";
    public static final String GTYPE_NAME = "GstCapsSetter";

    public CapsSetter() {
		this((String) null);
	}

    public CapsSetter(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public CapsSetter(Initializer init) {
		super(init);
	}

    public Boolean isQosHandlingEnabled() {
    	return get("qos");
    }
    
    public CapsSetter setQosHandlingEnabled(boolean enabled) {
    	set("qos", enabled);
    	return this;
    }

    public Caps getCaps() {
    	return get("caps");
    }
    
    public void setCaps(Caps caps) {
    	set("caps", caps);
    }
    
    public Boolean isJoinEnabled() {
    	return get("join");
    }
    
    public CapsSetter setJoinEnabled(boolean enabled) {
    	set("join", enabled);
    	return this;
    }

    public Boolean isReplaceEnabled() {
    	return get("replace");
    }
    
    public CapsSetter setReplaceEnabled(boolean enabled) {
    	set("replace", enabled);
    	return this;
    }

}
