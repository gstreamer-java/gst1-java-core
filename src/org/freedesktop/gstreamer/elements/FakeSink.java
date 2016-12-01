package org.freedesktop.gstreamer.elements;

public class FakeSink extends BaseSink{
	
	public static final String GST_NAME = "fakesink";
    public static final String GTYPE_NAME = "GstFakeSink";

    public FakeSink() {
		this((String) null);
	}

    public FakeSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public FakeSink(Initializer init) {
		super(init);
	}

    public Integer getBlockSize() {
		return get("blocksize");
	}
	
	public FakeSink setBlockSize(int blockSize) {
		set("blocksize", blockSize);
		return this;
	}

    public Integer getNumBuffers() {
		return get("num-buffers");
	}
	
	public FakeSink setNumBuffers(int numberPfBuffers) {
		set("num-buffers", numberPfBuffers);
		return this;
	}
	
    public Boolean isTypeFindEnabled() {
    	return get("typefind");
    }
    
    public FakeSink setTypeFindEnabled(boolean enabled) {
    	set("typefind", enabled);
    	return this;
    }

    public Boolean isDoTimestampEnabled() {
    	return get("do-timestamp");
    }
    
    public FakeSink setDoTimestampEnabled(boolean enabled) {
    	set("do-timestamp", enabled);
    	return this;
    }

    public String getLocation() {
    	return get("location");
    }
    
    public FakeSink setLocation(String location) {
    	set("location", location);
    	return this;
    }


}
