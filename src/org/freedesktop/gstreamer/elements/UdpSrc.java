package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.GSocket;

public class UdpSrc extends BaseSrc {

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

	public UdpSrc setAddress(String address) {
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
	
	public Boolean isCloseSocketEnabled() {
		return get("close-socket");
	}

	public UdpSrc setCloseSocketEnabled(boolean enabled) {
		set("close-socket", enabled);
		return this;
	}

	public GSocket getUsedSocket() {
		return get("used-socket");
	}

	public GSocket getSocket() {
		return get("socket");
	}

	public UdpSrc setSocket(GSocket socket) {
		set("socket", socket);
		return this;
	}

}
