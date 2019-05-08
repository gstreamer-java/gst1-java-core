package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.webrtc.WebRTCPeerConnectionState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WebRTCBinTest {
    @Test
    public void connectionStateTest() {
        assertEquals(WebRTCPeerConnectionState.fromInt(0), WebRTCPeerConnectionState.NEW);
        assertEquals(WebRTCPeerConnectionState.fromInt(1), WebRTCPeerConnectionState.CONNECTING);
        assertEquals(WebRTCPeerConnectionState.fromInt(2), WebRTCPeerConnectionState.CONNECTED);
        assertEquals(WebRTCPeerConnectionState.fromInt(3), WebRTCPeerConnectionState.DISCONNECTED);
        assertEquals(WebRTCPeerConnectionState.fromInt(4), WebRTCPeerConnectionState.FAILED);
        assertEquals(WebRTCPeerConnectionState.fromInt(5), WebRTCPeerConnectionState.CLOSED);
    }
}
