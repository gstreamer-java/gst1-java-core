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
package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

public class Identity extends BaseTransform {
    public static final String GST_NAME = "identity";
    public static final String GTYPE_NAME = "GstIdentity";

    public Identity(String name) {
        this(makeRawElement(GST_NAME, name));
    }  

    public Identity(Initializer init) {
        super(init);
    }
    
    /**
     * Signal emitted when this {@link Identity} has a {@link Buffer} ready.
     * 
     * @see #connect(HANDOFF)
     * @see #disconnect(HANDOFF)
     */
    public static interface HANDOFF {
        /**
         * Called when an {@link BaseSrc} has a {@link Buffer} ready.
         * 
         * @param identity the identity instance.
         * @param buffer the buffer for the data.
         */
        public void handoff(Identity identity, Buffer buffer);
    }
    /**
     * Add a listener for the <code>handoff</code> signal on this element
     * 
     * @param listener The listener to be called when a {@link Buffer} is ready.
     */
    public void connect(final HANDOFF listener) {
        connect(HANDOFF.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Identity identity, Buffer buffer) {
                listener.handoff(identity, buffer);
            }
        });
    }
    
    /**
     * Remove a listener for the <code>handoff</code> signal
     * 
     * @param listener The listener that was previously added.
     */
    public void disconnect(HANDOFF listener) {
        disconnect(HANDOFF.class, listener);
    }
}
