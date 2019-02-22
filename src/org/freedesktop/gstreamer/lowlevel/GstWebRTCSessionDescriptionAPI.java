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

package org.freedesktop.gstreamer.lowlevel;

import java.util.Arrays;
import java.util.List;

import org.freedesktop.gstreamer.webrtc.WebRTCSessionDescription;
import org.freedesktop.gstreamer.webrtc.WebRTCSDPType;
import org.freedesktop.gstreamer.SDPMessage;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValueArray;

import com.sun.jna.Pointer;

/**
 * GstWebRTCSessionDescription methods and structures
 * 
 * @see https://github.com/GStreamer/gst-plugins-bad/blob/master/gst-libs/gst/webrtc/rtcsessiondescription.h
 * Available since GStreamer 1.14
 */
public interface GstWebRTCSessionDescriptionAPI extends com.sun.jna.Library {
  GstWebRTCSessionDescriptionAPI GSTWEBRTCSESSIONDESCRIPTION_API =
      GstNative.load("gstwebrtc", GstWebRTCSessionDescriptionAPI.class);

  public static final class WebRTCSessionDescriptionStruct extends com.sun.jna.Structure {
    public volatile WebRTCSDPType type;
    public volatile SDPMessage sdp;

    public WebRTCSessionDescriptionStruct(final Pointer ptr) {
      useMemory(ptr);
    }

    @Override
    protected List<String> getFieldOrder() {
      return Arrays.asList(new String[] { "type", "sdp" });
    }
  }

  GType gst_webrtc_session_description_get_type();

  @CallerOwnsReturn WebRTCSessionDescription gst_webrtc_session_description_new(WebRTCSDPType type, SDPMessage sdp);
  @CallerOwnsReturn Pointer ptr_gst_webrtc_session_description_new(WebRTCSDPType type, SDPMessage sdp);

  @CallerOwnsReturn WebRTCSessionDescription gst_webrtc_session_description_copy(WebRTCSessionDescription src);
  @CallerOwnsReturn Pointer ptr_gst_webrtc_session_description_copy(WebRTCSessionDescription src);

  void gst_webrtc_session_description_free(Pointer desc);
}
