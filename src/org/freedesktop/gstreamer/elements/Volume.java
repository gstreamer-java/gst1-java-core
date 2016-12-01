package org.freedesktop.gstreamer.elements;

public class Volume extends AudioFilter{
	
	public static final String GST_NAME = "volume";
    public static final String GTYPE_NAME = "GstVolume";
    
    public Volume() {
		this((String) null);
	}

    public Volume(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public Volume(Initializer init) {
		super(init);
	}

    public Boolean isMuteEnabled() {
		return get("mute");
	}
	
	public Volume setMuteEnabledd(boolean enabled) {
		set("mute", enabled);
		return this;
	}

	public Double getVolume() {
		return get("volume");
	}
	
	public Volume setVolume(double normalizedVolume) {
		set("volume", normalizedVolume);
		return this;
	}



}
