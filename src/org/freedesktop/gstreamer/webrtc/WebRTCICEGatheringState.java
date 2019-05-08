package org.freedesktop.gstreamer.webrtc;

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.glib.NativeEnum;

/**
 * The ICE gathering state of WebRTC peer
 * Available since GStreamer 1.14
 */
@Gst.Since(minor = 14)
public enum  WebRTCICEGatheringState implements NativeEnum<WebRTCICEGatheringState> {
    /** New gathering */
    NEW(0),
    /** Gathering in progress */
    GATHERING(1),
    /** Gathering completed */
    COMPLETE(2);

    private final int value;

    private WebRTCICEGatheringState(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return this.value;
    }
}
