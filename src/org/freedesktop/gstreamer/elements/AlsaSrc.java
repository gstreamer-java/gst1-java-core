package org.freedesktop.gstreamer.elements;

public class AlsaSrc extends AudioBaseSrc<AlsaSrc>{
	
	public static final String GST_NAME = "alsasrc";
    public static final String GTYPE_NAME = "GstAlsaSrc";

    public AlsaSrc() {
		this((String) null);
	}

    public AlsaSrc(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public AlsaSrc(Initializer init) {
		super(init);
	}

    public String getCardName() {
		return get("card-name");
	}

	public String getDevice() {
		return get("device");
	}

	public AlsaSrc setDevice(String device) {
		set("device", device);
		return this;
	}

	public String getDeviceName() {
		return get("device-name");
	}
}
