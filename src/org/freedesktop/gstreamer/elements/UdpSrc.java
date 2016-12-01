package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Caps;

public class UdpSrc extends BaseSink {
	
	public static final String GST_NAME = "udpsrc";
    public static final String GTYPE_NAME = "GstUDPSrc";

    public UdpSrc() {
		this((String) null);
	}

    public UdpSrc(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public UdpSrc(Initializer init) {
		super(init);
	}

    public String getAddress() {
		return get("address");
	}
	
	public UdpSrc setHost(String address) {
		set("address", address);
		return this;
	}

    public Integer getPort() {
		return get("port");
	}
	
	public UdpSrc setPort(int port) {
		set("port", port);
		return this;
	}

	public Caps getCaps() {
		return get("caps");
	}
}
