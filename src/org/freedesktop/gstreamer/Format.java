/* 
 * Copyright (c) 2019 Neil C Smith
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

import org.freedesktop.gstreamer.glib.NativeEnum;
import org.freedesktop.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * Standard predefined formats.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/gstreamer-GstFormat.html#GstFormat"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/gstreamer-GstFormat.html#GstFormat</a>
 * <p>
 */
// @TODO this won't handle custom registered formats - do we need to?
public enum Format implements NativeEnum<Format> {
    /** Undefined format */
    @DefaultEnumValue
    UNDEFINED(0),
    /**
     * The default format of the pad/element. This can be samples for raw audio,
     * frames/fields for raw video (some, but not all, elements support this;
     * use {@link #TIME } if you don't have a good reason to query for
     * samples/frames)
     */
    DEFAULT(1),
    /** Bytes */
    BYTES(2),
    /** Time in nanoseconds */
    TIME(3),
    
    /** {@link Buffer}s (few, if any, elements implement this as of May 2009) */
    BUFFERS(4),
    /** Percentage of stream (few, if any, elements implement this as of May 2009) */
    PERCENT(5);

    private final int value;
    
    Format(int value) {
        this.value = value;
    }
    
    @Override
    public int intValue() {
        return value;
    }
}
