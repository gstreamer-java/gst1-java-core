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

import java.nio.charset.StandardCharsets;

import static org.freedesktop.gstreamer.lowlevel.GstSDPMessageAPI.GSTSDPMESSAGE_API;

import org.freedesktop.gstreamer.glib.NativeObject;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.lowlevel.GPointer;

/**
 * Wrapping type and helper methods for dealing with SDP messages.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-libs/html/gst-plugins-base-libs-GstSDPMessage.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-libs/html/gst-plugins-base-libs-GstSDPMessage.html</a>
 */
public class SDPMessage extends NativeObject {
    public static final String GTYPE_NAME = "GstSDPMessage";

    /**
     * Internally used constructor. Do not use.
     *
     * @param init internal initialization data
     */
    SDPMessage(Initializer init) {
        this(new Handle(init.ptr, init.ownsHandle));
    }

    SDPMessage(Handle handle) {
        super(handle);
    }
    
    /**
     * Creates a new instance of SDPMessage
     */
    public SDPMessage() {
        this(initHandle());
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
        byte[] data = sdpString.getBytes(StandardCharsets.US_ASCII);
        int length = sdpString.length();
        GSTSDPMESSAGE_API.gst_sdp_message_parse_buffer(data, length, this);
    }

//    /**
//     * Creates a copy of this SDPMessage.
//     *
//     * @return a copy of SDPMessage.
//     */
//    SDPMessage copy(boolean shouldInvalidateOriginal) {
//        Pointer[] ptr = new Pointer[1];
//        GSTSDPMESSAGE_API.gst_sdp_message_copy(this, ptr);
//        if (shouldInvalidateOriginal) {
//            this.invalidate();
//        }
//        return new SDPMessage(initializer(ptr[0]));
//    }

    private static Handle initHandle() {
        Pointer[] ptr = new Pointer[1];
        GSTSDPMESSAGE_API.gst_sdp_message_new(ptr);
        return new Handle(new GPointer(ptr[0]), true);
    }

    private static final class Handle extends NativeObject.Handle {

        public Handle(GPointer ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GSTSDPMESSAGE_API.gst_sdp_message_free(ptr.getPointer());
        }
        
    }
}
