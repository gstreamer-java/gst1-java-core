/*
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2018 Vinicius Tona
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
package org.freedesktop.gstreamer.webrtc;

import static org.freedesktop.gstreamer.lowlevel.GstWebRTCSessionDescriptionAPI.GSTWEBRTCSESSIONDESCRIPTION_API;

import org.freedesktop.gstreamer.lowlevel.GstWebRTCSessionDescriptionAPI;
import org.freedesktop.gstreamer.glib.NativeObject;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.SDPMessage;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import static org.freedesktop.gstreamer.lowlevel.GstSDPMessageAPI.GSTSDPMESSAGE_API;

/**
 * Wraps a GstWebRTCSessionDescription
 * <p>
 * See <a href="https://gitlab.freedesktop.org/gstreamer/gst-plugins-bad/blob/master/gst-libs/gst/webrtc/rtcsessiondescription.h"
 * >https://gitlab.freedesktop.org/gstreamer/gst-plugins-bad/blob/master/gst-libs/gst/webrtc/rtcsessiondescription.h</a>
 */
public class WebRTCSessionDescription extends NativeObject {

    public static final String GTYPE_NAME = "GstWebRTCSessionDescription";

    private GstWebRTCSessionDescriptionAPI.WebRTCSessionDescriptionStruct sessionDescriptionStruct;

    /**
     * Internally used constructor. Do not use.
     *
     * @param init internal initialization data.
     */
    WebRTCSessionDescription(Initializer init) {
        super(new Handle(init.ptr, init.ownsHandle));
        sessionDescriptionStruct = 
                new GstWebRTCSessionDescriptionAPI.WebRTCSessionDescriptionStruct(init.ptr.getPointer());
    }

    /**
     * Creates a new instance of WebRTCSessionDescription.
     *
     * @param type The {@link WebRTCSDPType} type of the session description
     * @param sdpMessage The {@link SDPMessage} of the session description
     */
    public WebRTCSessionDescription(WebRTCSDPType type, SDPMessage sdpMessage) {
        this(Natives.initializer(GSTWEBRTCSESSIONDESCRIPTION_API.ptr_gst_webrtc_session_description_new(type, sdpMessage)));
    }

    /**
     * Gets the SDPMessage from the WebRTCSessionDescription.
     *
     * @return the {@link SDPMessage} for the WebRTCSessionDescription
     */
    public SDPMessage getSDPMessage() {
        SDPMessage originalSDP = (SDPMessage) sessionDescriptionStruct.readField("sdp");
        // making a copy of the SDPMessage since the original SDPMessage in the struct belongs to WebRTCSessionDescription.
        // Once WebRTCSessionDescription is disposed it would also dispose of SDPMessage leading to any objects with a reference
        // to the original SDPMessage to be invalid and potentially lead to runtime errors.
        
        //    return originalSDP.copy(true);
        Pointer[] ptr = new Pointer[1];
        GSTSDPMESSAGE_API.gst_sdp_message_copy(originalSDP, ptr);
        originalSDP.invalidate();
        return Natives.objectFor(ptr[0], SDPMessage.class, false, true);
    }

    
    private static final class Handle extends NativeObject.Handle {

        public Handle(GPointer ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GSTWEBRTCSESSIONDESCRIPTION_API.gst_webrtc_session_description_free(ptr.getPointer());
        }
        
    }
}
