package org.freedesktop.gstreamer.elements;

public class SpeexEnc extends AudioEncoder<SpeexEnc> {
	
	public static final String GST_NAME = "speexenc";
    public static final String GTYPE_NAME = "GstSpeexEnc";
    
	public static enum Mode {
		AUTO, ULTRA_WIDE_BAND, WIDE_BAND, NARROW_BAND;
	}


    public SpeexEnc() {
		this((String) null);
	}

    public SpeexEnc(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public SpeexEnc(Initializer init) {
		super(init);
	}

	public Float getQuality() {
		return get("quality");
	}
	
	public SpeexEnc setquality(float quality) {
		set("quality", quality);
		return this;
	}
	
	public Long getBitRate() {
		return get("bitrate");
	}
	
	public SpeexEnc setBitRate(long bitrateInBps) {
		set("bitrate",bitrateInBps);
		return this;
	}
	
	public Mode getMode() {
		return get("mode");
	}
	
	public SpeexEnc setMode(Mode mode) {
		set("mode", mode);
		return this;
	}
	
	public Boolean isVariableBitRateEnabled() {
		return get("vbr");
	}
	
	public SpeexEnc setVariableBitRateEnabled(boolean enabled) {
		set("vbr", enabled);
		return this;
	}
	
	public Boolean isAverageBitRateEnabled() {
		return get("abr");
	}
	
	public SpeexEnc setAverageBitRateEnabled(boolean enabled) {
		set("abr", enabled);
		return this;
	}

	public Boolean isDiscontinuousTransmissionEabled() {
		return get("dtx");
	}
	
	public SpeexEnc setDiscontinuousTransmissionEabled(boolean enabled) {
		set("dtx", enabled);
		return this;
	}
	
	public Integer getComplexity() {
		return get("complexity");
	}
	
	public SpeexEnc setComplexity(int complexity) {
		set("complexity",complexity);
		return this;
	}
	
	public Integer getNumberOfFramesPerBuffer() {
		return get("nframes");
	}
	
	public SpeexEnc setNumberOfFramesPerBuffer(int numberOfFramesPerBuffer) {
		set("nframes",numberOfFramesPerBuffer);
		return this;
	}


}
