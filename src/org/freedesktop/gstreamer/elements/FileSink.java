package org.freedesktop.gstreamer.elements;

public class FileSink extends BaseSink{
	
	public static final String GST_NAME = "filesink";
    public static final String GTYPE_NAME = "GstFileSink";

    public FileSink() {
		this((String) null);
	}

    public FileSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public FileSink(Initializer init) {
		super(init);
	}

    public String getLocation() {
    	return get("location");
    }
    
    public FileSink setLocation(String location) {
    	set("location", location);
    	return this;
    }


}
