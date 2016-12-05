package org.freedesktop.gstreamer.elements;

public class UdpSink extends BaseUdpSink<UdpSink> {
	
	public static final String GST_NAME = "udpsink";
    public static final String GTYPE_NAME = "GstUDPSink";

    public UdpSink() {
		this((String) null);
	}

    public UdpSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public UdpSink(Initializer init) {
		super(init);
	}

    public String getHost() {
		return get("host");
	}
	
	public UdpSink setHost(String host) {
		set("host", host);
		return this;
	}

    public Integer getPort() {
		return get("port");
	}
	
	public UdpSink setPort(int port) {
		set("port", port);
		return this;
	}

   

}
