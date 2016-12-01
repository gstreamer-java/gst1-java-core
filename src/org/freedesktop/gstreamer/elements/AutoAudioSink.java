package org.freedesktop.gstreamer.elements;

public class AutoAudioSink extends AutoDetect {
	
	public static final String GST_NAME = "autoaudiosink";
    public static final String GTYPE_NAME = "GstAutoAudioSink";

	
    public AutoAudioSink() {
		this((String) null);
	}

    public AutoAudioSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public AutoAudioSink(Initializer init) {
		super(init);
	}

    public Boolean isSync() {
    	return get("sync");
    }
    
    public AutoAudioSink setSync(boolean sync) {
    	set("sync", sync);
    	return this;
    }

    public Long getTsOffset() {
    	return get("ts-offset");
    }
    
    public AutoAudioSink setTsOffset(long tsOffsetInNanos) {
    	set("ts-offset", tsOffsetInNanos);
    	return this;
    }

}
