package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.glib.NativeEnum;
import org.freedesktop.gstreamer.webrtc.WebRTCPeerConnectionState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WebRTCBinTest {
    @Test
    public void connectionStateTest() {
        assertEquals(NativeEnum.fromInt(WebRTCPeerConnectionState.class, 0), WebRTCPeerConnectionState.NEW);
        assertEquals(NativeEnum.fromInt(WebRTCPeerConnectionState.class, 1), WebRTCPeerConnectionState.CONNECTING);
        assertEquals(NativeEnum.fromInt(WebRTCPeerConnectionState.class, 2), WebRTCPeerConnectionState.CONNECTED);
        assertEquals(NativeEnum.fromInt(WebRTCPeerConnectionState.class, 3), WebRTCPeerConnectionState.DISCONNECTED);
        assertEquals(NativeEnum.fromInt(WebRTCPeerConnectionState.class, 4), WebRTCPeerConnectionState.FAILED);
        assertEquals(NativeEnum.fromInt(WebRTCPeerConnectionState.class, 5), WebRTCPeerConnectionState.CLOSED);
    }
}
