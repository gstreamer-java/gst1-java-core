package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.VideoScalingMethod;

public class VideoScale extends VideoFilter {

	public static final String GST_NAME = "videoscale";
	public static final String GTYPE_NAME = "GstVideoScale";

	public VideoScale() {
		this((String) null);
	}

	public VideoScale(String name) {
		this(makeRawElement(GST_NAME, name));
	}

	public VideoScale(Initializer init) {
		super(init);
	}

	public VideoScalingMethod getScalingMethod() {
		return VideoScalingMethod.valueOf( (Integer)get("method"));
	}

	public VideoScale setScalingMethod(VideoScalingMethod ditheringMethod) {
		set("dither", ditheringMethod.intValue());
		return this;
	}

	public Boolean isAddBordersEnabled() {
		return get("add-borders");
	}

	public VideoScale setAddBordersEnabled(boolean enabled) {
		set("add-borders", enabled);
		return this;
	}

	public Double getSharpness() {
		return get("sharpness");
	}

	public VideoScale setSharpness(double sharnpess) {
		set("sharpness",sharnpess);
		return this;
	}

	public Double getSharpen() {
		return get("sharpen");
	}

	public VideoScale setSharpen(double sharpen) {
		set("sharpen",sharpen);
		return this;
	}

	public Boolean isDitheringEnabled() {
		return get("dither");
	}

	public VideoScale setDitheringEnabled(boolean enabled) {
		set("dither", enabled);
		return this;
	}

	public Double getFilterEnvelopeSize() {
		return get("envelope");
	}

	public VideoScale setFilterEnvelopeSize(double envelope) {
		set("envelope",envelope);
		return this;
	}

	public Boolean isGammaDecodeEnabled() {
		return get("gamma-decode");
	}

	public VideoScale setGammaDecodeEnabled(boolean enabled) {
		set("gamma-decode", enabled);
		return this;
	}

}
