package org.freedesktop.gstreamer.elements;

public class FileSrc extends BaseSrc{
	
	public static final String GST_NAME = "filesrc";
    public static final String GTYPE_NAME = "GstFileSrc";

    public FileSrc() {
		this((String) null);
	}

    public FileSrc(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public FileSrc(Initializer init) {
		super(init);
	}

    public String getLocation() {
    	return get("location");
    }
    
    public FileSrc setLocation(String location) {
    	set("location", location);
    	return this;
    }


}
