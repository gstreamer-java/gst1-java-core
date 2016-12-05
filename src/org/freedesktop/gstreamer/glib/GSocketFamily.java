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
	
	public static GSocketFamily fromGioValue(int gioValue) {
		return fastResolveMap.get(gioValue);
	}

	private int gioValue;

	private GSocketFamily(int gioValue) {
		this.gioValue = gioValue;
	}

	public int toGioValue() {
		return gioValue;
	}

}
