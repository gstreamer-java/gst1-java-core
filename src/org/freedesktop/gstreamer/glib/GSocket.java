/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2016 Isaac Raño Jares
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License 
 * version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freedesktop.gstreamer.glib;

import org.freedesktop.gstreamer.lowlevel.GioAPI;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GErrorStruct;

import com.sun.jna.Pointer;
import static org.freedesktop.gstreamer.lowlevel.GlibAPI.GLIB_API;

public class GSocket extends GObject {

    public static final String GTYPE_NAME = "GSocket";

    public GSocket(GSocketFamily family, GSocketType type, GSocketProtocol protocol) throws GLibException {
        this(makeRawSocket(family, type, protocol));
    }

    GSocket(Initializer init) {
        super(init);
    }

    public GSocket bind(String address, int port) {
        GInetSocketAddress boundAddress = new GInetSocketAddress(address, port);
        GErrorStruct reference = new GErrorStruct();
        GErrorStruct[] errorArray = (GErrorStruct[]) reference.toArray(1);
        if (!GioAPI.g_socket_bind(getRawPointer(),
                Natives.getRawPointer(boundAddress),
                true,
                reference.getPointer())) {
            throw new GLibException(extractAndClearError(errorArray[0]));
        }
        return this;
    }

    public void connect(String address, int port) {
        GInetSocketAddress connectedAddress = new GInetSocketAddress(address, port);
        GErrorStruct reference = new GErrorStruct();
        GErrorStruct[] errorArray = (GErrorStruct[]) reference.toArray(1);
        if (!GioAPI.g_socket_connect(getRawPointer(),
                Natives.getRawPointer(connectedAddress),
                Natives.getRawPointer(new GCancellable()),
                reference.getPointer())) {
            throw new GLibException(extractAndClearError(errorArray[0]));
        }
    }

    public int getFD() {
        return (Integer) get("fd");
    }

    public GInetSocketAddress getLocalAddress() {
        return (GInetSocketAddress) get("local-address");
    }

    public GInetSocketAddress getRemoteAddress() {
        return (GInetSocketAddress) get("remote-address");
    }

    public GSocketFamily getSocketFamily() {
        return GSocketFamily.fromGioValue((Integer) get("family"));
    }

    public GSocketProtocol getSocketProtocol() {
        return GSocketProtocol.fromGioValue((Integer) get("protocol"));
    }

    public GSocketType getSocketType() {
        return GSocketType.fromGioValue((Integer) get("type"));
    }

    public boolean isBlocking() {
        return (Boolean) get("blocking");
    }

    private static String extractAndClearError(GErrorStruct struct) {
        struct.read();
        String err = struct.getMessage();
        GLIB_API.g_error_free(struct);
        return err;
    }

    private static Initializer makeRawSocket(GSocketFamily family, GSocketType type, GSocketProtocol protocol) throws GLibException {
        GErrorStruct reference = new GErrorStruct();
        GErrorStruct[] errorArray = (GErrorStruct[]) reference.toArray(1);
        Pointer socketPointer = GioAPI.g_socket_new(family.toGioValue(), type.toGioValue(), protocol.toGioValue(), reference.getPointer());
        if (socketPointer == null) {
            throw new GLibException(extractAndClearError(errorArray[0]));
        }
        return Natives.initializer(socketPointer);
    }

}
