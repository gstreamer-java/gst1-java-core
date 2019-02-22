/*
 * Copyright (c) 2019 Neil C Smith
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

import org.freedesktop.gstreamer.glib.NativeEnum;
import org.freedesktop.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * Return values for SDP functions
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-libs/html/gst-plugins-base-libs-GstSDPMessage.html#GstSDPResult"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-libs/html/gst-plugins-base-libs-GstSDPMessage.html#GstSDPResult</a>
 * 
 * @see SDPMessage
 */
public enum SDPResult implements NativeEnum<SDPResult> {
    /** A successful return value*/
    OK(0),
    /** A function to SDPMessage was given invalid parameters */
    @DefaultEnumValue
    EINVAL(-1);

    private final int value;
    
    SDPResult(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value of the enum
     * @return the integer value for this enum.
     */
    @Override
    public int intValue() {
        return value;
    }

}
