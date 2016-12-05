package org.freedesktop.gstreamer.glib;

import java.util.HashMap;
import java.util.Map;

public enum GSocketType {
	INVALID		(0),
	STREAM		(1),
	DATAGRAM	(2),
	SEQPACKET	(3);

	private static final Map<Integer,GSocketType> fastResolveMap = new HashMap<Integer,GSocketType>();
	static {
		for(GSocketType dataUnitType : values()) {
			fastResolveMap.put(dataUnitType.toGioValue(), dataUnitType);
		}
	}
	
	public static GSocketType fromGioValue(int gioValue) {
		return fastResolveMap.get(gioValue);
	}

	private int gioValue;

	private GSocketType(int gioValue) {
		this.gioValue = gioValue;
	}

	public int toGioValue() {
		return gioValue;
	}

}
