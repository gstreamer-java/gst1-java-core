package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.glib.GCancellable;
import org.freedesktop.gstreamer.glib.GSocketFamily;
import org.freedesktop.gstreamer.glib.GSocketProtocol;
import org.freedesktop.gstreamer.glib.GSocketType;
import org.freedesktop.gstreamer.lowlevel.GioAPI;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GErrorStruct;

import com.sun.jna.Pointer;

public class GSocket extends GObject{

	public static final String GTYPE_NAME = "GSocket";
	
	public static GSocket create(GSocketFamily family, GSocketType type, GSocketProtocol protocol) throws GstException {
		GErrorStruct reference = new GErrorStruct();
		GErrorStruct[] errorArray = (GErrorStruct[]) reference.toArray(1);
		Pointer socketPointer = GioAPI.g_socket_new(family.toGioValue(), type.toGioValue(), protocol.toGioValue(), reference.getPointer());
		if (socketPointer == null) {
			errorArray[0].read();
			throw new GstException(new GError(errorArray[0]));
		}
		return new GSocket(initializer(socketPointer));
	}
	
	public static GSocket createDefaultUdpSocket() {
		return create(GSocketFamily.IPV4, GSocketType.DATAGRAM, GSocketProtocol.UDP);
	}

	public static GSocket createDefaultUdpSocket(String bindAddress, int bindPort) {
		return create(GSocketFamily.IPV4, GSocketType.DATAGRAM, GSocketProtocol.UDP).bind(bindAddress, bindPort);
	}

	public GSocket(Initializer init) {
		super(init);
	}
	
	public GSocket bind(String address, int port) {
		GInetSocketAddress boundAddress = GInetSocketAddress.create(address, port);
		GErrorStruct reference = new GErrorStruct();
		GErrorStruct[] errorArray = (GErrorStruct[]) reference.toArray(1);
		if ( ! GioAPI.g_socket_bind(getNativeAddress(), boundAddress.getNativeAddress(), true, reference.getPointer()) ) {
			errorArray[0].read();
			throw new GstException(new GError(errorArray[0]));
		}
		return this;
	}
	
	public void connect(String address, int port) {
		GInetSocketAddress connectedAddress = GInetSocketAddress.create(address, port);
		GErrorStruct reference = new GErrorStruct();
		GErrorStruct[] errorArray = (GErrorStruct[]) reference.toArray(1);
		if ( ! GioAPI.g_socket_connect(getNativeAddress(), connectedAddress.getNativeAddress(), new GCancellable().getNativeAddress(), reference.getPointer()) ) {
			errorArray[0].read();
			throw new GstException(new GError(errorArray[0]));
		}
	}
	
	public GInetSocketAddress getLocalAddress() {
		return (GInetSocketAddress) get("local-address");
	}

	public GInetSocketAddress getRemoteAddress() {
		return (GInetSocketAddress) get("remote-address");
	}

	public GSocketType getSocketType() {
		return GSocketType.fromGioValue((Integer) get("type"));
	}

	public GSocketFamily getSocketFamily() {
		return GSocketFamily.fromGioValue((Integer) get("family"));
	}

	public GSocketProtocol getSocketProtocol() {
		return GSocketProtocol.fromGioValue((Integer) get("protocol"));
	}

	public boolean isBlocking() {
		return (Boolean) get("blocking");
	}
	
	public Integer getFD() {
		return (Integer) get("fd");
	}
	
	
}
