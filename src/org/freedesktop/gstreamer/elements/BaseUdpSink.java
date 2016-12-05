package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.GSocket;

public class BaseUdpSink<Child extends BaseUdpSink<?>> extends BaseSink {
	
    public BaseUdpSink(Initializer init) {
		super(init);
	}

    public Boolean isCloseSocketEnabled() {
    	return get("close-socket");
    }
    
    @SuppressWarnings("unchecked")
	public Child setCloseSocketEnabled(boolean enabled) {
    	set("close-socket", enabled);
    	return (Child) this;
    }

    public GSocket getUsedSocket() {
    	return get("used-socket");
    }
    
    public GSocket getSocket() {
    	return get("socket");
    }
    
    @SuppressWarnings("unchecked")
	public Child setSocket(GSocket socket) {
    	set("socket", socket);
    	return (Child) this;
    }
    

}
