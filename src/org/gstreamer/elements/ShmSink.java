/* 
 * Copyright (c) 2009 Levente Farkas
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
package org.gstreamer.elements;

import org.gstreamer.lowlevel.GstAPI.GstCallback;

/**
 * Send data over shared memory to the matching source
 *
 */
public class ShmSink extends BaseSink {
    public static final String GST_NAME = "shmsink";
    public static final String GTYPE_NAME = "GstShmSink";

    public ShmSink(String name) {
        this(makeRawElement(GST_NAME, name));
    }

    public ShmSink(Initializer init) {
        super(init);
    }

    
    public static interface CLIENT_CONNECTED {
        public void client_connected(ShmSink element, int i);
    }
    
    public void connect(final CLIENT_CONNECTED listener) {
        connect(CLIENT_CONNECTED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(ShmSink element, int i) {
                listener.client_connected(element, i);
            }
        });
    }
    
    public void disconnect(CLIENT_CONNECTED listener) {
        disconnect(CLIENT_CONNECTED.class, listener);
    }

    
    public static interface CLIENT_DISCONNECTED {
        public void client_disconnected(ShmSink element, int i);
    }
    
    public void connect(final CLIENT_DISCONNECTED listener) {
        connect(CLIENT_DISCONNECTED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(ShmSink element, int i) {
                listener.client_disconnected(element, i);
            }
        });
    }
    
    public void disconnect(CLIENT_DISCONNECTED listener) {
        disconnect(CLIENT_DISCONNECTED.class, listener);
    }
}
