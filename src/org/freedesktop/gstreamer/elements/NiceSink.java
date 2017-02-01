package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.nice.NiceAgent;

public class NiceSink extends BaseSink {
	
	public static final String GST_NAME = "nicesink";
    public static final String GTYPE_NAME = "GstNiceSink";

    public NiceSink() {
		this((String) null);
	}

    public NiceSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public NiceSink(Initializer init) {
		super(init);
	}

    public NiceAgent getAgent() {
    	return get("agent");
    }
 
    public NiceSink setAgent(NiceAgent agent) {
    	set("agent", agent);
    	return this;
    }

    public Integer getStream() {
    	return get("stream");
    }
    
    public NiceSink setStream(int stream) {
    	set("stream", stream);
    	return this;
    }
    
    public Integer getComponent() {
    	return get("component");
    }
    
    public NiceSink setComponent(int component) {
    	set("component", component);
    	return this;
    }
    
    
}
