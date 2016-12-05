package org.freedesktop.gstreamer.elements;

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


}
