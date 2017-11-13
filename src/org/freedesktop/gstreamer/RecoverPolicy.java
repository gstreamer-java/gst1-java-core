/* 
 * Copyright (c) 2007 Wayne Meissner
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

package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.EnumMapper;
import org.freedesktop.gstreamer.lowlevel.IntegerEnum;
import org.freedesktop.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * Standard predefined formats.
 */
public enum RecoverPolicy implements IntegerEnum {
	// Do not try to recover
    NONE(0),
    // Resync client to latest buffer
    LATEST(1),
    // Resync client to soft limit
    SOFT_LIMIT(2),
    // Resync client to most recent keyframe
    KEYFRAME(3),
    
    
    /** The default enum value used when no other value matches the native value */
    @DefaultEnumValue
    __UNKNOWN_NATIVE_VALUE(~0);
    RecoverPolicy(int value) {
        this.value = value;
    }
    /**
     * Gets the integer value of the enum.
     * @return The integer value for this enum.
     */
    public final int intValue() {
        return value;
    }
    
    /**
     * Returns the enum constant of this type with the specified integer value.
     * @param format integer value.
     * @return Enum constant.
     */
    public final static RecoverPolicy valueOf(int value) {
        return EnumMapper.getInstance().valueOf(value, RecoverPolicy.class);
    }
    public final int value;
}
