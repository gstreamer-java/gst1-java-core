package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.glib.GInetAddress;
import org.freedesktop.gstreamer.glib.GSocketAddress;
import org.freedesktop.gstreamer.lowlevel.GioAPI;

import com.sun.jna.Pointer;

public class GInetSocketAddress extends GSocketAddress{

	public static final String GTYPE_NAME = "GInetSocketAddress";
	
	
	protected static Initializer createRawAddress(String address, int port) {
		Pointer nativePointer = GioAPI.g_inet_socket_address_new_from_string(address, port);
		if (nativePointer == null) {
			throw new GstException("Can not create "+GInetSocketAddress.class.getSimpleName()+" for "+address+":"+port+", please check that the IP address is valid, with format x.x.x.x");
		}
		return initializer(nativePointer);
	}
	
	public GInetSocketAddress(String address, int port)  {
		this(createRawAddress(address, port));
	}
	
	public GInetSocketAddress(Initializer init) {
		super(init);
	}

	public int getPort() {
		return (Integer) get("port");
	}
	
	public GInetAddress getAddress() {
		return (GInetAddress) get("address");
	}
}
