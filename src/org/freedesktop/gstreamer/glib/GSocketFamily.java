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

import java.util.HashMap;
import java.util.Map;

import org.freedesktop.gstreamer.lowlevel.GlibAPI;

public enum GSocketFamily {
	
	INVALID	(0x00),
	UNIX		(GlibAPI.GLIB_SYSDEF_AF_UNIX),
	IPV4		(GlibAPI.GLIB_SYSDEF_AF_INET),
	IPV6		(GlibAPI.GLIB_SYSDEF_AF_INET6);

	private static final Map<Integer,GSocketFamily> fastResolveMap = new HashMap<Integer,GSocketFamily>();
	static {
		for(GSocketFamily dataUnitType : values()) {
			fastResolveMap.put(dataUnitType.toGioValue(), dataUnitType);
		}
	}

	private int gioValue;

	private GSocketFamily(int gioValue) {
		this.gioValue = gioValue;
	}

	public int toGioValue() {
		return gioValue;
	}
        
        public static GSocketFamily fromGioValue(int gioValue) {
            return fastResolveMap.get(gioValue);
        }

}
