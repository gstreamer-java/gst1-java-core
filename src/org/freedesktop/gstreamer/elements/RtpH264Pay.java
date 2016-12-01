package org.freedesktop.gstreamer.elements;

public class RtpH264Pay extends RtpBasePayload<RtpH264Pay>{
	
	public static final String GST_NAME = "rtph264pay";
    public static final String GTYPE_NAME = "GstRtpH264Pay";

    public RtpH264Pay() {
		this((String) null);
	}

    public RtpH264Pay(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public RtpH264Pay(Initializer init) {
		super(init);
	}
    
    public Integer getConfigInterval() {
		return get("config-interval");
	}
	
	public RtpH264Pay setConfigInterval(int configIntervalInSeconds) {
		set("config-interval", configIntervalInSeconds);
		return this;
	}

	public String getSpropParameterSets() {
		return get("sprop-parameter-sets");
	}
	
	public RtpH264Pay setSpropParameterSets(String spropParametersSetInBase64) {
		set("sprop-parameter-sets", spropParametersSetInBase64);
		return this;
	}

}
