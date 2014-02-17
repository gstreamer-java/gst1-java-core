/* 
 * Copyright (c) 2010 DHoyt <david.g.hoyt@gmail.com>
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

package org.gstreamer;

import org.gstreamer.lowlevel.EnumMapper;
import org.gstreamer.lowlevel.IntegerEnum;

/**
 * The different types of URI direction.
 */
public enum URIType implements IntegerEnum {
    /** The URI direction is unknown */
    UNKNOWN(0),
    /** The URI is a consumer. */
    SINK(1),
    /** The URI is a producer. */
    SRC(2);

    private final int value;
    
    URIType(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value of the enum.
     * @return The integer value for this enum.
     */
	public int intValue() {
        return value;
    }

    /**
     * Returns the enum constant of this type with the specified integer value.
     * @param uriType integer value.
     * @return Enum constant.
     * @throws java.lang.IllegalArgumentException if the enum type has no constant with the specified value.
     */
    public static final URIType valueOf(int uriType) {
        return EnumMapper.getInstance().valueOf(uriType, URIType.class);
    }
}
