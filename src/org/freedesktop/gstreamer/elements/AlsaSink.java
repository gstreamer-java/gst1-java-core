package org.freedesktop.gstreamer.elements;

public class AlsaSink extends AudioBaseSink<AlsaSink>{

	public static final String GST_NAME = "alsasink";
	public static final String GTYPE_NAME = "GstAlsaSink";


	public AlsaSink() {
		this((String) null);
	}

	public AlsaSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

	public AlsaSink(Initializer init) {
		super(init);
	}

	public String getCardName() {
		return get("card-name");
	}

	public String getDevice() {
		return get("device");
	}

	public AlsaSink setDevice(String device) {
		set("device", device);
		return this;
	}

	public String getDeviceName() {
		return get("device-name");
	}

}
