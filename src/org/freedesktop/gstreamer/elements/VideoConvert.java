package org.freedesktop.gstreamer.elements;

public class VideoConvert extends VideoFilter {
	
	public static final String GST_NAME = "videoconvert";
    public static final String GTYPE_NAME = "GstVideoConvert";

    public VideoConvert() {
		this((String) null);
	}

    public VideoConvert(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public VideoConvert(Initializer init) {
		super(init);
	}
  
}
