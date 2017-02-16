package org.freedesktop.gstreamer.lowlevel;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class GioAPI {
	
	static {
		Native.register("gio-2.0");
	}

	// GInetAddress
	public static native String g_inet_address_to_string(Pointer gInetAddress);

	// GstSocketAddress
	public static native Pointer g_inet_socket_address_new_from_string(String address, int port);
	
	// GstSocket
	public static native Pointer g_socket_new(int gSocketFamilyEnumValue, int gSocketTypeEnumValue, int gSocketProtcolEnumValue, Pointer gErrorStructArrayPointer);
	public static native boolean g_socket_bind(Pointer gSocketPointer, Pointer gSocketAddressPointer, boolean allowReuse, Pointer gErrorStructArrayPointer);
	public static native boolean g_socket_connect(Pointer gSocketPointer, Pointer gSocketAddressPointer, Pointer gCancellablePointer, Pointer gErrorStructArrayPointer);
	
	// GCancellable
	public static native Pointer g_cancellable_new();


	
	
	
}
