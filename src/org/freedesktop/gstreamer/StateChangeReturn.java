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

/**
 * The possible return values from a state change function. 
 * <p>
 * Only {@link StateChangeReturn#FAILURE} is a real failure.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstElement.html#GstStateChangeReturn"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstElement.html#GstStateChangeReturn</a>
 * <p>
 */
public enum StateChangeReturn implements NativeEnum<StateChangeReturn> {
    /** The state change failed. */
    FAILURE(0),
    /** The state change succeeded. */
    SUCCESS(1),
    /** The state change will happen asynchronously. */
    ASYNC(2),
    /**
     * The state change succeeded but the {@link Element} cannot produce data in 
     * {@link State#PAUSED}. This typically happens with live sources.
     */
    NO_PREROLL(3);
    
    private final int value;
    
    private StateChangeReturn(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }
}
