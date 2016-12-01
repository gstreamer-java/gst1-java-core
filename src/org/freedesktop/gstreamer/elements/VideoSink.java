package org.freedesktop.gstreamer.elements;

public class VideoSink<Child extends VideoSink<?>> extends BaseSink {
	
    public static final String GTYPE_NAME = "GstvideoSink";

    public VideoSink(Initializer init) {
		super(init);
	}

    public Boolean isShowPrerollFrameEnabled() {
    	return get("show-preroll-frame");
    }
    
    @SuppressWarnings("unchecked")
	public Child setShowPrerollFrameEnabled(boolean enabled) {
    	set("show-preroll-frame", enabled);
    	return (Child) this;
    }
    
    
}
