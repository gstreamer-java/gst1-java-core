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

  public WebRTCSessionDescription(WebRTCSDPType type, SDPMessage sdpMessage) {
    this(initializer(GSTWEBRTCSESSIONDESCRIPTION_API.ptr_gst_webrtc_session_description_new(type, sdpMessage)));
  }

  protected void disposeNativeHandle(Pointer ptr) {
    GSTWEBRTCSESSIONDESCRIPTION_API.gst_webrtc_session_description_free(ptr);
  }

  public SDPMessage getSDPMessage() {
    SDPMessage sdp = (SDPMessage)sessionDescriptionStruct.readField("sdp");
    return sdp;
  }

}
