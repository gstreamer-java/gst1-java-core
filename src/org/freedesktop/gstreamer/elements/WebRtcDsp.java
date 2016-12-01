package org.freedesktop.gstreamer.elements;

public class WebRtcDsp extends AudioFilter{
	
	public static final String GST_NAME = "webrtcdsp";
    public static final String GTYPE_NAME = "GstWebrtcDsp";
    
	public static enum EchoSupressionLevel {
		LOW, MODERATE, HIGH;
	}
	
	public static enum NoiseSupressionLevel {
		LOW, MODERATE, HIGH;
	}


    public WebRtcDsp() {
		this((String) null);
	}

    public WebRtcDsp(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public WebRtcDsp(Initializer init) {
		super(init);
	}

    public Boolean isQosHandlingEnabled() {
		return get("qos");
	}
	
	public WebRtcDsp setQosHandlingEnabled(boolean enabled) {
		set("qos", enabled);
		return this;
	}

	public Boolean isHighPassFilterEnabled() {
		return get("high-pass-filter");
	}
	
	public WebRtcDsp setHighPassFilterEnabled(boolean enabled) {
		set("high-pass-filter", enabled);
		return this;
	}

	public String getEchoProbeName() {
		return get("probe");
	}
	
	public WebRtcDsp setEchoProbeName(String probeName) {
		set("probe", probeName);
		return this;
	}

	public Boolean isEchoCancellationEnabled() {
		return get("echo-cancel");
	}
	
	public WebRtcDsp setEchoCancellationEnabled(boolean enabled) {
		set("echo-cancel", enabled);
		return this;
	}
	
	public EchoSupressionLevel getEchoSupressionLevel() {
		return get("echo-suppression-level");
	}
	
	public WebRtcDsp setEchoSupressionLevel(EchoSupressionLevel level) {
		set("echo-suppression-level", level);
		return this;
	}

	public Boolean isNoiseSupressionEnabled() {
		return get("noise-suppression");
	}
	
	public WebRtcDsp setNoiseSupressionEnabled(boolean enabled) {
		set("noise-suppression", enabled);
		return this;
	}
	
	public NoiseSupressionLevel getNoiseSupressionLevel() {
		return get("noise-suppression-level");
	}
	
	public WebRtcDsp setNoiseSupressionLevel(NoiseSupressionLevel level) {
		set("noise-suppression-level", level);
		return this;
	}

	public Boolean isGainControlEnabled() {
		return get("gain-control");
	}
	
	public WebRtcDsp setGainControlEnabled(boolean enabled) {
		set("gain-control", enabled);
		return this;
	}

	public Boolean isExperimentalAutomaticGainControlEnabled() {
		return get("experimental-agc");
	}
	
	public WebRtcDsp setExperimentalAutomaticGainControlEnabled(boolean enabled) {
		set("experimental-agc", enabled);
		return this;
	}

	public Boolean isExtendedFilterEnabled() {
		return get("extended-filter");
	}
	
	public WebRtcDsp setExtendedFilterEnabled(boolean enabled) {
		set("extended-filter", enabled);
		return this;
	}
	
	public Boolean isDelayAgnotiscModeEnabled() {
		return get("delay-agnostic");
	}
	
	public WebRtcDsp setDelayAgnosticModeEnabled(boolean enabled) {
		set("delay-agnostic", enabled);
		return this;
	}


}
