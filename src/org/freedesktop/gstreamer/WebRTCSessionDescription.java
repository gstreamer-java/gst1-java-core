package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GstWebRTCSessionDescriptionAPI.*;

import com.sun.jna.Pointer;

public class WebRTCSessionDescription extends MiniObject
{
  public static final String GTYPE_NAME = "GstWebRTCSessionDescription";

  public WebRTCSessionDescription(final Initializer init)
  {
    super(init);
  }

  public WebRTCSessionDescription(final Pointer type, final Pointer sdp)
  {
    this(initializer(
        GSTWEBRTCSESSIONDESCRIPTION_API.ptr_gst_webrtc_session_description_new(type, sdp)));
  }

  protected static Initializer initializer(final Pointer ptr)
  {
    return new Initializer(ptr, false, true);
  }
}
