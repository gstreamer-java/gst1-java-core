package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Fraction;

public class SubParse extends Element {

	public static final String GST_NAME = "subparse";
	public static final String GTYPE_NAME = "GstSubParse";

	public SubParse() {
		this((String) null);
	}

	public SubParse(String name) {
		this(makeRawElement(GST_NAME, name));
	}

	public SubParse(Initializer init) {
		super(init);
	}

	public String getSubtitleEncoding() {
		return get("subtitle-encoding");
	}
	
	public SubParse setSubtitleEncoding(String subtitleEncoding) {
		set("subtitle-encoding", subtitleEncoding);
		return this;
	}
	
	public Fraction getVideoFramesPerSeconds() {
		return get("video-fps");
	}
	
	public SubParse setVideoFramesPerSecond(Fraction videoFps) {
		set("video-fps", videoFps);
		return this;
	}
	

}
