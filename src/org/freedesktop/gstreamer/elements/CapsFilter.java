package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Caps;

public class CapsFilter extends BaseTransform {
	
	public static final String GST_NAME = "capsfilter";
    public static final String GTYPE_NAME = "GstCapsFilter";

    public CapsFilter() {
		this((String) null);
	}

    public CapsFilter(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public CapsFilter(Initializer init) {
		super(init);
	}

    public Caps getCaps() {
    	return get("caps");
    }
    
    public void setCaps(Caps caps) {
    	set("caps", caps);
    }

}
