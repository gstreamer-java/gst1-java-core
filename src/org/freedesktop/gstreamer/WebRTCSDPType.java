package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

public enum WebRTCSDPType implements IntegerEnum {
    GST_WEBRTC_SDP_TYPE_OFFER(1),
    GST_WEBRTC_SDP_TYPE_PRANSWER(2),
    GST_WEBRTC_SDP_TYPE_ANSER(3),
    GST_WEBRTC_SDP_TYPE_ROLLBACK(4);

    WebRTCSDPType(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
    private final int value;
}
