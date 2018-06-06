
package org.freedesktop.gstreamer;

import java.nio.charset.StandardCharsets;

import static org.freedesktop.gstreamer.lowlevel.GstSDPMessageAPI.GSTSDPMESSAGE_API;

import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.lowlevel.GstSDPMessageAPI;

import com.sun.jna.Pointer;

public class SDPMessage extends NativeObject {
    public static final String GTYPE_NAME = "GstSDPMessage";

    public SDPMessage(Initializer init) {
        super(init);
    }

    public SDPMessage() {
        this(initializer());
    }

    protected static Initializer initializer(final Pointer ptr) {
        return new Initializer(ptr, false, true);
    }

    private static Initializer initializer() {
        Pointer[] ptr = new Pointer[1];
        GSTSDPMESSAGE_API.gst_sdp_message_new(ptr);
        return initializer(ptr[0], false, true);
    }

    protected void disposeNativeHandle(Pointer ptr) {
        GSTSDPMESSAGE_API.gst_sdp_message_free(ptr);
    }

    public String toString() {
        return GSTSDPMESSAGE_API.gst_sdp_message_as_text(this);
    }

    public void parseBuffer(String sdpString) {
        if (sdpString != null && sdpString.length() != 0) {
            byte[] data = sdpString.getBytes(StandardCharsets.US_ASCII);
            int length = sdpString.length();
            GSTSDPMESSAGE_API.gst_sdp_message_parse_buffer(data, length, this);
        }
    }
}
