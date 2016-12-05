package org.freedesktop.gstreamer.glib;

import java.util.HashMap;
import java.util.Map;

public enum GSocketProtocol {
	UNKNOWN (-1),
	DEFAULT (0),
	TCP     (6),
	UDP     (17),
	SCTP 	(132);

	private static final Map<Integer,GSocketProtocol> fastResolveMap = new HashMap<Integer,GSocketProtocol>();
	static {
		for(GSocketProtocol dataUnitType : values()) {
			fastResolveMap.put(dataUnitType.toGioValue(), dataUnitType);
		}
	}

	public static GSocketProtocol fromGioValue(int gioValue) {
		return fastResolveMap.get(gioValue);
	}

	private int gioValue;

	private GSocketProtocol(int gioValue) {
		this.gioValue = gioValue;
	}

	public int toGioValue() {
		return gioValue;
	}

}
