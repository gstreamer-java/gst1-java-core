package org.freedesktop.gstreamer.elements;

public class H264Parse extends BaseParse<H264Parse>{
	
	public static final String GST_NAME = "h264parse";
    public static final String GTYPE_NAME = "GstH264Parses";

    public H264Parse() {
		this((String) null);
	}

    public H264Parse(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public H264Parse(Initializer init) {
		super(init);
	}
    
    public Integer getConfigInterval() {
		return get("config-interval");
	}
	
	public H264Parse setConfigInterval(int configIntervalInSeconds) {
		set("config-interval", configIntervalInSeconds);
		return this;
	}

}
