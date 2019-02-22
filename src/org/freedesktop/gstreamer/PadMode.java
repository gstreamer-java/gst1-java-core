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

import org.freedesktop.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * The status of a {@link Pad}. After activating a pad, which usually happens when 
 * the parent element goes from {@link State#READY} to {@link State#PAUSED}, the 
 * PadMode defines if the {@link Pad} operates in push or pull mode.
 */
public enum PadMode {
    /** 
     * Pad will not handle dataflow
     */
    @DefaultEnumValue
    NONE,
    /**
     * Pad handles dataflow in downstream push mode
     */
    PUSH,
    /**
     * Pad handles dataflow in upstream pull mode
     */
    PULL;
}
