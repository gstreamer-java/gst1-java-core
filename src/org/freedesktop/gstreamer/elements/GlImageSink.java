package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Bin;

public class GlImageSink extends Bin {

	public static final String GST_NAME = "glimagesink";
	public static final String GTYPE_NAME = "GstGLImageSinkBin";

	public GlImageSink() {
		this((String) null);
	}

	public GlImageSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

	public GlImageSink(Initializer init) {
		super(init);
	}


	public Boolean isShowPrerollFrameEnabled() {
		return get("show-preroll-frame");
	}

	public GlImageSink setShowPrerollFrameEnabled(boolean enabled) {
		set("show-preroll-frame", enabled);
		return this;
	}

	public Boolean isSync() {
		return get("sync");
	}

	public GlImageSink setSync(boolean sync) {
		set("sync", sync);
		return this;
	}



}
