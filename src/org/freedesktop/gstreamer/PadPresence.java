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

/**
 * Indicates when a {@link Pad} will become available, for use in {@link PadTemplate}
 */
public enum PadPresence {
    /**
     * The Pad is always available.
     */
    ALWAYS,
    /**
     * The pad will become available depending on the media stream.
     */
    SOMETIMES,
    /**
     * The pad is only available on request with {@link Element#getRequestPad(java.lang.String) }
     */
    REQUEST;
}
