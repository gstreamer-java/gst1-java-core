package org.freedesktop.gstreamer.elements;

public class AutoVideoSink extends AutoDetect {
	
	public static final String GST_NAME = "autovideosink";
    public static final String GTYPE_NAME = "GstAutoVideoSink";

	
    public AutoVideoSink() {
		this((String) null);
	}

    public AutoVideoSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public AutoVideoSink(Initializer init) {
		super(init);
	}

    public Boolean isSync() {
    	return get("sync");
    }
    
    public AutoVideoSink setSync(boolean sync) {
    	set("sync", sync);
    	return this;
    }

    public Long getTsOffset() {
    	return get("ts-offset");
    }
    
    public AutoVideoSink setTsOffset(long tsOffsetInNanos) {
    	set("ts-offset", tsOffsetInNanos);
    	return this;
    }

}
