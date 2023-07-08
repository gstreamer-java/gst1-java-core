/*
 * Copyright (c) 2018 Antonio Morales
 *
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License version 3 only, as published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3 along with
 * this work. If not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.gstreamer.webrtc;

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.glib.GBytes;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

/**
 *
 * @see https://gitlab.freedesktop.org/gstreamer/gst-plugins-bad/blob/master/ext/webrtc/webrtcdatachannel.h
 * available since Gstreamer 1.15
 */
@Gst.Since(minor = 15)
public class WebRTCDataChannel extends GstObject {
    public static final String GTYPE_NAME = "GstWebRTCDataChannel";

    public WebRTCDataChannel(Initializer init) {
        super(init);
    }

    /**
     * Signal emitted when this {@link WebRTCDataChannel} has an error.
     */
    public static interface ON_ERROR {
        public void onError(WebRTCDataChannel channel);
    }

    /**
     * Signal emitted when this {@link WebRTCDataChannel} is opened.
     */
    public static interface ON_OPEN {
        public void onOpen(WebRTCDataChannel channel);
    }

    /**
     * Signal emitted when this {@link WebRTCDataChannel} is closed.
     */
    public static interface ON_CLOSE {
        public void onClose(WebRTCDataChannel channel);
    }

    /**
     * Signal emitted when this {@link WebRTCDataChannel} receives a string message from a remote peer.
     */
    public static interface ON_MESSAGE_STRING {
        public void onMessage(WebRTCDataChannel channel, String message);
    }

    public void connect(final ON_ERROR listener) {
        connect(ON_ERROR.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(GstObject channel) {
                listener.onError((WebRTCDataChannel) channel);
            }
        });
    }

    public void connect(final ON_OPEN listener) {
        connect(ON_OPEN.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(GstObject channel) {
                listener.onOpen((WebRTCDataChannel) channel);
            }
        });
    }

    public void connect(final ON_CLOSE listener) {
        connect(ON_CLOSE.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(GstObject channel) {
                listener.onClose((WebRTCDataChannel) channel);
            }
        });
    }

    public void connect(final ON_MESSAGE_STRING listener) {
        connect(ON_MESSAGE_STRING.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(GstObject channel, String message) {
                listener.onMessage((WebRTCDataChannel) channel, message);
            }
        });
    }

    /**
     * Sends a string through this {@link WebRTCDataChannel} which will be received by connected remote peers.
     * @param message that should be sent over the WebRTC data-channel connection.
     */
    public void sendMessage(String message) {
        emit("send-string", message);
    }


    /**
     * Signal emitted when this {@link WebRTCDataChannel} receives binary data from a remote peer.
     */
    public interface ON_MESSAGE_DATA {
        void onMessage(WebRTCDataChannel channel, byte[] data);
    }

    public void connect(final ON_MESSAGE_DATA listener) {
        connect(ON_MESSAGE_DATA.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(GstObject channel, GBytes gbytes) {
                listener.onMessage((WebRTCDataChannel) channel, gbytes.getBytes());
                gbytes.dispose();
            }
        });
    }

    /**
     * Sends binary data through this {@link WebRTCDataChannel} which will be received by connected remote peers.
     * @param bytes that should be sent over the WebRTC data-channel connection.
     */
    public void sendMessage(byte[] bytes) {
        GBytes gbytes = GBytes.createInstance(bytes);
        gbytes.disown();
        emit("send-data", gbytes);
    }

}