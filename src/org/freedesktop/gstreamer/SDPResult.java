/*
 * Copyright (c) 2018 Antonio Morales
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License version 3 only, as published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3 along with
 * this work. If not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.IntegerEnum;
import org.freedesktop.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * The possible results for {@link SDPMessage} functions
 */
public enum SDPResult implements IntegerEnum {
    /** A successful return value*/
    OK(0),
    /** A function to SDPMessage was given invalid paramters */
    EINVAL(-1),
    /** An unknown result */
    @DefaultEnumValue
    __UNKNWOND_NATIVE_VALUE(~0);

    SDPResult(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value of the enum
     * @return the integer value for this enum.
     */
    public int intValue() {
        return value;
    }

    private int value;
}
