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

	private int gioValue;

	private GSocketType(int gioValue) {
		this.gioValue = gioValue;
	}

	public int toGioValue() {
		return gioValue;
	}
        
        public static GSocketType fromGioValue(int gioValue) {
            return fastResolveMap.get(gioValue);
        }

}
