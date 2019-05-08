/*
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2018 Antonio Morales
 *
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freedesktop.gstreamer.webrtc;

import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Promise;
import org.freedesktop.gstreamer.Structure;

import org.freedesktop.gstreamer.glib.NativeEnum;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

/**
 * WebRTCBin is an abstraction over gstreamers webrtcbin element It is
 * structured to mimic the RTCPeerConnection API that is available in web
 * browsers
 *
 * @see https://developer.mozilla.org/en-US/docs/Web/API/RTCPeerConnection
 *
 * @see
 * https://gitlab.freedesktop.org/gstreamer/gst-plugins-bad/blob/master/ext/webrtc/gstwebrtcbin.c
 * available since Gstreamer 1.14
 */
@Gst.Since(minor = 14)
public class WebRTCBin extends Bin {

    public static final String GST_NAME = "webrtcbin";
    public static final String GTYPE_NAME = "GstWebRTCBin";

    WebRTCBin(Initializer init) {
        super(init);
    }

    public WebRTCBin(String name) {
        super(makeRawElement(GST_NAME, name));
    }

    /**
     * Signal emitted when this {@link WebRTCBin} is ready to do negotiation to
     * setup a WebRTC connection Good starting point to have the WebRTCBin send
     * an offer to potential clients
     */
    public static interface ON_NEGOTIATION_NEEDED {

        /**
         * @param elem the original webrtc bin that had the callback attached to
         */
        public void onNegotiationNeeded(Element elem);
    }

    /*
     * Signal emmited when this {@link WebRTCBin} gets a new ice candidate
     */
    public static interface ON_ICE_CANDIDATE {

        /**
         * @param sdpMLineIndex the zero-based index of the m-line attribute
         * within the SDP to which the candidate should be associated to
         * @param candidate the ICE candidate
         */
        public void onIceCandidate(int sdpMLineIndex, String candidate);
    }

    /**
     * Signal emitted when this {@link WebRTCBin} creates an offer
     */
    public static interface CREATE_OFFER {

        /**
         * @param a @WebRTCSessionDescription of the offer
         */
        public void onOfferCreated(WebRTCSessionDescription offer);
    }

    /**
     * Signal emitted when this {@link WebRTCBin} creates an answer
     */
    public static interface CREATE_ANSWER {

        /**
         * @param a @WebRTCSessionDescription of the answer
         */
        public void onAnswerCreated(WebRTCSessionDescription answer);
    }

    /**
     * Adds a listener for the <code>on-negotiation-needed</code> signal.
     *
     * @param listener
     */
    public void connect(final ON_NEGOTIATION_NEEDED listener) {
        connect(ON_NEGOTIATION_NEEDED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Element elem) {
                listener.onNegotiationNeeded(elem);
            }
        });
    }

    /**
     * Adds a listener for the <code>on-ice-candidate</code> signal.
     *
     * @param listener
     */
    public void connect(final ON_ICE_CANDIDATE listener) {
        connect(ON_ICE_CANDIDATE.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Element elem, int sdpMLineIndex, String candidate) {
                listener.onIceCandidate(sdpMLineIndex, candidate);
            }
        });
    }

    /**
     * Create an offer that can be sent to other clients to setup a WebRTC
     * connection.
     * <p>
     * In most cases {@link #setLocalDescription} should be called after an
     * answer is created
     *
     * @param listener callback that is called when a offer is created
     */
    public void createOffer(final CREATE_OFFER listener) {
        Promise promise = new Promise(new Promise.PROMISE_CHANGE() {
            @SuppressWarnings("unused")
            public void onChange(Promise promise) {
                Structure reply = promise.getReply();
                WebRTCSessionDescription description = (WebRTCSessionDescription) reply.getValue("offer");
                listener.onOfferCreated(description);
                promise.dispose();
            }
        });
        emit("create-offer", null, promise);
    }

    /**
     * Create an answer in response to an offer received in order for the WebRTC
     * signaling protocol to start.
     * <p>
     * Should be called after {@link #setRemoteDescription} is called
     * <p>
     * In most cases {@link #setLocalDescription} should be called after an
     * answer is created
     *
     * @param listener callback that is called when an answer is created.
     */
    public void createAnswer(final CREATE_ANSWER listener) {
        Promise promise = new Promise(new Promise.PROMISE_CHANGE() {
            @SuppressWarnings("unused")
            public void onChange(Promise promise) {
                Structure reply = promise.getReply();
                WebRTCSessionDescription description = (WebRTCSessionDescription) reply.getValue("answer");
                listener.onAnswerCreated(description);
                promise.dispose();
            }
        });
        emit("create-answer", null, promise);
    }

    /**
     * Adds a remote ice candidate to the bin
     *
     * @param sdpMLineIndex the zero-based index of the m-line attribute within
     * the SDP to which the candidate should be associated to
     * @param candidate the ICE candidate
     */
    public void addIceCandidate(int sdpMLineIndex, String candidate) {
        emit("add-ice-candidate", sdpMLineIndex, candidate);
    }

    /**
     * Sets the local description for the WebRTC connection. Should be called
     * after {@link #createOffer} or {@link #createAnser} is called.
     *
     * @param description the {@link WebRTCSessionDescription} to set for the
     * local description
     */
    public void setLocalDescription(WebRTCSessionDescription description) {
        Promise promise = new Promise();
        // the raw WebRTCBin element gets ownership of the description so it must be disown in order to prevent it from being deallocated
        description.disown();
        emit("set-local-description", description, promise);
        promise.interrupt();
        promise.dispose();
    }

    /**
     * Sets the remote description for the WebRTC connection. Shoud be called
     * after receiving an offer or answer from other clients.
     *
     * @param description the {@link WebRTCSessionDescription} to set for the
     * remote description
     */
    public void setRemoteDescription(WebRTCSessionDescription description) {
        Promise promise = new Promise();
        // the raw WebRTCBin element gets ownership of the description so it must be disown in order to prevent it from being deallocated
        description.disown();
        emit("set-remote-description", description, promise);
        promise.interrupt();
        promise.dispose();
    }

    /**
     * Sets the <code>stun-server</code> property for this {@link WebRTCBin}
     * which is use to gather ICE data
     *
     * @param server STUN server url
     */
    public void setStunServer(String server) {
        set("stun-server", server);
    }

    /**
     * Retrieves the STUN server that is used.
     *
     * @return the url for the STUN server
     */
    public String getStunServer() {
        return (String) get("stun-server");
    }

    /**
     * Sets the <code>turn-server</code> property for this {@link WebRTCBin}
     * which is used whenever a direct peer-to-peer connection can be
     * established
     *
     * @param server TURN server url
     */
    public void setTurnServer(String server) {
        set("turn-server", server);
    }

    /**
     * Retrieves the TURN server that is used.
     *
     * @return the url for the TURN server
     */
    public String getTurnServer() {
        return (String) get("turn-server");
    }

    /**
     * Retrieve the connection state this {@link WebRTCBin} is currently in
     *
     * @return a {@link WebRTCPeerConnectionState} describing the connection
     * state
     */
    public WebRTCPeerConnectionState getConnectionState() {
        return NativeEnum.fromInt(WebRTCPeerConnectionState.class, (Integer) get("connection-state"));
    }

    /**
     * Retrieve the local description for this {@link WebRTCBin}
     *
     * @return the local {@link WebRTCSessionDescription}
     */
    public WebRTCSessionDescription getLocalDescription() {
        WebRTCSessionDescription description = (WebRTCSessionDescription) get("local-description");
        description.disown();
        return description;
    }

    /**
     * Retrieve the remote description for this {@link WebRTCBin}
     *
     * @return the remote {@link WebRTCSessionDescription}
     */
    public WebRTCSessionDescription getRemoteDescription() {
        WebRTCSessionDescription description = (WebRTCSessionDescription) get("remote-description");
        description.disown();
        return description;
    }
}
