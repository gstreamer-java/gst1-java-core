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
//import org.freedesktop.gstreamer.lowlevel.EnumMapper;

/**
 * The possible states an element can be in.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstElement.html#GstState"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstElement.html#GstState</a>
 * <p>
 */
public enum State implements NativeEnum<State> {
    /**
     * No pending state.
     */
    VOID_PENDING(0),
    /**
     * The initial state of an {@link Element}.
     */
    NULL(1),
    /**
     * The {@link Element} is ready to go to PAUSED.
     */
    READY(2),
    /**
     * The {@link Element} is PAUSED
     */
    PAUSED(3),
    /**
     * The {@link Element} is PLAYING
     */
    PLAYING(4);

    private final int value;
    
    private State(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value of the enum.
     *
     * @return The integer value for this enum.
     */
    @Override
    public int intValue() {
        return value;
    }

//    /**
//     * Returns the enum constant of this type with the specified integer value.
//     *
//     * @param state integer value.
//     * @return Enum constant.
//     * @throws java.lang.IllegalArgumentException if the enum type has no
//     * constant with the specified value.
//     */
//    public static final State valueOf(int state) {
//        return EnumMapper.getInstance().valueOf(state, State.class);
//    }
}
