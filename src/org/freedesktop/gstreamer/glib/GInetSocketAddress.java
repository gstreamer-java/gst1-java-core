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

import com.sun.jna.Pointer;

public class GInetSocketAddress extends GSocketAddress {

	public static final String GTYPE_NAME = "GInetSocketAddress";
	
	public GInetSocketAddress(String address, int port)  {
		this(createRawAddress(address, port));
	}
	
	GInetSocketAddress(Initializer init) {
		super(init);
	}
        
        public GInetAddress getAddress() {
            return (GInetAddress) get("address");
        }

	public int getPort() {
		return (Integer) get("port");
	}
        
        private static Initializer createRawAddress(String address, int port) {
            Pointer nativePointer = GioAPI.g_inet_socket_address_new_from_string(address, port);
            if (nativePointer == null) {
                throw new GLibException("Can not create "+GInetSocketAddress.class.getSimpleName()+" for "+address+":"+port+", please check that the IP address is valid, with format x.x.x.x");
            }
            return Natives.initializer(nativePointer);
        }
}
