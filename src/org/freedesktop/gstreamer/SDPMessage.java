
package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GstSDPMessageAPI.GSTSDPMESSAGE_API;

import org.freedesktop.gstreamer.lowlevel.NativeObject;

import com.sun.jna.Pointer;

public class SDPMessage extends NativeObject {
    public static final String GTYPE_NAME = "GstSDPMessage";

    public SDPMessage(Initializer init) {
        super(init);
    }

    @Override 
    protected void disposeNativeHandle(Pointer ptr) {
        GSTSDPMESSAGE_API.gst_sdp_message_free(ptr);
    }
}
