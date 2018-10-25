package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GstWebRTCSessionDescriptionAPI.GSTWEBRTCSESSIONDESCRIPTION_API;

import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.lowlevel.GType;

import com.sun.jna.Pointer;

public class WebRTCSessionDescription extends NativeObject {
  public static final String GTYPE_NAME = "GstWebRTCSessionDescription";
  public static final GType GTYPE = GSTWEBRTCSESSIONDESCRIPTION_API.gst_webrtc_session_description_get_type();

  /**
   * Internally used constructor.  Do not use.
   * 
   * @param init internal initialization data.
   */
  public WebRTCSessionDescription(Initializer init) {
    super(init);
  }

  @Override
  protected void disposeNativeHandle(Pointer ptr) {
    GSTWEBRTCSESSIONDESCRIPTION_API.gst_webrtc_session_description_free(ptr);
  }
}
