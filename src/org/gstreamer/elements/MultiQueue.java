/* 
 * Copyright (c) 2008 Levente Farkas
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

import org.gstreamer.Element;
import org.gstreamer.lowlevel.GstAPI.GstCallback;

/**
 * A gstreamer element for Multiple data queue.
 */
public class MultiQueue extends Element {
	public static final String GST_NAME = "multiqueue";
    public static final String GTYPE_NAME = "GstMultiQueue";

    public MultiQueue(String name) {
        this(makeRawElement(GST_NAME, name));
    }  

    public MultiQueue(Initializer init) {
        super(init);
    }

    /**
	 * This signal is emitted from the streaming thread when there is no 
	 * data in any of the queues inside the multiqueue instance (underrun).
	 * 
	 * This indicates either starvation or EOS from the upstream data sources.
     * 
     * @see #connect(UNDERRUN)
     * @see #disconnect(UNDERRUN)
     */
    public static interface UNDERRUN {
        /**
         * @param mq the object which received the signal
         */
        public void underrun(MultiQueue mq);
    }
    /**
     * Add a listener for the <code>underrun</code> signal on this MultiQueue
     * 
     * @param listener The listener to be called.
     */
    public void connect(final UNDERRUN listener) {
        connect(UNDERRUN.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(MultiQueue mq) {
                listener.underrun(mq);
            }
        });
    }    
    /**
     * Disconnect the listener for the <code>underrun</code> signal on this MultiQueue
     * 
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(UNDERRUN listener) {
        disconnect(UNDERRUN.class, listener);
    }

	/**
     * Signal emitted when one of the queues in the multiqueue is full (overrun). 
     * A queue is full if the total amount of data inside it (num-buffers, time, size) 
     * is higher than the boundary values which can be set through the GObject properties.
     * 
     * This can be used as an indicator of pre-roll.
     * 
     * @see #connect(OVERRUN)
     * @see #disconnect(OVERRUN)
     */
    public static interface OVERRUN {
        /**
         * @param mq the object which received the signal
         */
        public void overrun(MultiQueue mq);
    }
    /**
     * Add a listener for the <code>overrun</code> signal on this MultiQueue
     * 
     * @param listener The listener to be called.
     */
    public void connect(final OVERRUN listener) {
        connect(OVERRUN.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(MultiQueue mq) {
                listener.overrun(mq);
            }
        });
    }    
    /**
     * Disconnect the listener for the <code>overrun</code> signal on this MultiQueue
     * 
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(OVERRUN listener) {
        disconnect(OVERRUN.class, listener);
    }
}
