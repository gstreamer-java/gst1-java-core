/*
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

package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GstWebRTCSessionDescriptionAPI.GSTWEBRTCSESSIONDESCRIPTION_API;

import org.freedesktop.gstreamer.lowlevel.GstWebRTCSessionDescriptionAPI;
import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.lowlevel.GType;

import com.sun.jna.Pointer;

public class WebRTCSessionDescription extends NativeObject {
  public static final String GTYPE_NAME = "GstWebRTCSessionDescription";
  public static final GType GTYPE = GSTWEBRTCSESSIONDESCRIPTION_API.gst_webrtc_session_description_get_type();

  protected GstWebRTCSessionDescriptionAPI.WebRTCSessionDescriptionStruct sessionDescriptionStruct;

  /**
   * Internally used constructor. Do not use.
   * 
   * @param init internal initialization data.
   */
  public WebRTCSessionDescription(Initializer init) {
    super(init);
    sessionDescriptionStruct = new GstWebRTCSessionDescriptionAPI.WebRTCSessionDescriptionStruct(handle());
  }

  /**
   * Creates a new instance of WebRTCSessionDescription.
   *
   * @param type The {@link WebRTCSDPType} type of the session description
   * @param sdpMessage The {@link SDPMessage} of the session description
   */
  public WebRTCSessionDescription(WebRTCSDPType type, SDPMessage sdpMessage) {
    this(initializer(GSTWEBRTCSESSIONDESCRIPTION_API.ptr_gst_webrtc_session_description_new(type, sdpMessage)));
  }

  /**
   * Get the SDPMessage from the WebRTCSessionDescription.
   *
   * @return the {@link SDPMessage} for the WebRTCSessionDescription
   */
  public SDPMessage getSDPMessage() {
    SDPMessage sdp = (SDPMessage)sessionDescriptionStruct.readField("sdp");
    return sdp;
  }

  @Override
  protected void disposeNativeHandle(Pointer ptr) {
    GSTWEBRTCSESSIONDESCRIPTION_API.gst_webrtc_session_description_free(ptr);
  }
}
