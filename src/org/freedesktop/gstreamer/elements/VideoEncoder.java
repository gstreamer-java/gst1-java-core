package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;

public class VideoEncoder extends Element{
	
    public static final String GTYPE_NAME = "GstVideoEncoder";

	public VideoEncoder(Initializer init) {
		super(init);
	}

}
