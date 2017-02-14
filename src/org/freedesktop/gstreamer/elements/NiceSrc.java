package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.nice.NiceAgent;

public class NiceSrc extends PushSrc {
	
	public static final String GST_NAME = "nicesrc";
    public static final String GTYPE_NAME = "GstNiceSrc";

    public NiceSrc() {
		this((String) null);
	}

    public NiceSrc(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public NiceSrc(Initializer init) {
		super(init);
	}

    public NiceAgent getAgent() {
    	return get("agent");
    }
 
    public NiceSrc setAgent(NiceAgent agent) {
    	set("agent", agent);
    	return this;
    }

    public Integer getStream() {
    	return get("stream");
    }
    
    public NiceSrc setStream(int stream) {
    	set("stream", stream);
    	return this;
    }
    
    public Integer getComponent() {
    	return get("component");
    }
    
    public NiceSrc setComponent(int component) {
    	set("component", component);
    	return this;
    }
    
}
