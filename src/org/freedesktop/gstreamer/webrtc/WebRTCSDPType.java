/* 
 * Copyright (c) 2018 Antonio Morales
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
package org.freedesktop.gstreamer.webrtc;

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.glib.NativeEnum;

/**
 * The type of a {@link WebRTCSessionDescription}
 * <p>
 * @see https://w3c.github.io/webrtc-pc/#rtcsdptype
 * Available since GStreamer 1.12
 */
@Gst.Since(minor = 12)
public enum WebRTCSDPType implements NativeEnum<WebRTCSDPType> {
    OFFER(1),
    PRANSWER(2),
    ANSWER(3),
    ROLLBACK(4);
    
    private final int value;

    private WebRTCSDPType(int value) {
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
