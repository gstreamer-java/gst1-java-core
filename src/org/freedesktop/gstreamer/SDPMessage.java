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

import java.nio.charset.StandardCharsets;

import static org.freedesktop.gstreamer.lowlevel.GstSDPMessageAPI.GSTSDPMESSAGE_API;

import org.freedesktop.gstreamer.lowlevel.NativeObject;

import com.sun.jna.Pointer;

public class SDPMessage extends NativeObject {
    public static final String GTYPE_NAME = "GstSDPMessage";

    /**
     * Internally used constructor. Do not use.
     *
     * @param init internal initialization data
     */
    public SDPMessage(Initializer init) {
        super(init);
    }

    /**
     * Creates a new instance of SDPMessage
     */
    public SDPMessage() {
        this(initializer());
    }

    /**
     * A SDP formatted string representation of SDPMessage.
     *
     * Used for offer/answer exchanges for real time communicationse
     *
     * @return the SDP string representation of SDPMessage.
     */
    public String toString() {
        return GSTSDPMESSAGE_API.gst_sdp_message_as_text(this);
    }

    /**
     * Takes a SDP string and parses it and fills in all fields for SDPMessage.
     *
     * Look at https://tools.ietf.org/html/rfc4566 for more information on SDP
     *
     * @param sdpString the sdp string
     */
    public void parseBuffer(String sdpString) {
        if (sdpString != null && sdpString.length() != 0) {
            byte[] data = sdpString.getBytes(StandardCharsets.US_ASCII);
            int length = sdpString.length();
            GSTSDPMESSAGE_API.gst_sdp_message_parse_buffer(data, length, this);
        }
    }

    /**
     * Creates a copy of this SDPMessage.
     *
     * @return a copy of SDPMessage.
     */
    public SDPMessage copy(boolean shouldInvalidateOriginal) {
        Pointer[] ptr = new Pointer[1];
        GSTSDPMESSAGE_API.gst_sdp_message_copy(this, ptr);
        if (shouldInvalidateOriginal) {
            this.invalidate();
        }
        return new SDPMessage(initializer(ptr[0]));
    }

    private static Initializer initializer() {
        Pointer[] ptr = new Pointer[1];
        GSTSDPMESSAGE_API.gst_sdp_message_new(ptr);
        return initializer(ptr[0]);
    }

    protected void disposeNativeHandle(Pointer ptr) {
        GSTSDPMESSAGE_API.gst_sdp_message_free(ptr);
    }
}
