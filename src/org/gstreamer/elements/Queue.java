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
 * A gstreamer element for data queue.
 */
public class Queue extends Element {
    public static final String GST_NAME = "queue";
    public static final String GTYPE_NAME = "GstQueue";

    public Queue(String name) {
        this(makeRawElement(GST_NAME, name));
    }  

    public Queue(Initializer init) {
        super(init);
    }

	/**
	 * Reports that the buffer became full (overrun). A buffer is full if the total 
	 * amount of data inside it (num-buffers, time, size) is higher than the boundary 
	 * values which can be set through the GObject properties.
     * 
     * @see #connect(OVERRUN)
     * @see #disconnect(OVERRUN)
     */
    public static interface OVERRUN {
        /**
         * @param queue the object which received the signal
         */
        public void overrun(Queue queue);
    }
    /**
     * Add a listener for the <code>overrun</code> signal on this Queue
     * 
     * @param listener The listener to be called.
     */
    public void connect(final OVERRUN listener) {
        connect(OVERRUN.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Queue queue) {
                listener.overrun(queue);
            }
        });
    }    
    /**
     * Disconnect the listener for the <code>overrun</code> signal on this Queue
     * 
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(OVERRUN listener) {
        disconnect(OVERRUN.class, listener);
    }

	/**
	 * Reports that enough (min-threshold) data is in the queue. Use this signal 
	 * together with the underrun signal to pause the pipeline on underrun and 
	 * wait for the queue to fill-up before resume playback.
     * 
     * @see #connect(RUNNING)
     * @see #disconnect(RUNNING)
     */
    public static interface RUNNING {
        /**
         * @param queue the object which received the signal
         */
        public void running(Queue queue);
    }
    /**
     * Add a listener for the <code>running</code> signal on this Queue
     * 
     * @param listener The listener to be called.
     */
    public void connect(final RUNNING listener) {
        connect(RUNNING.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Queue queue) {
                listener.running(queue);
            }
        });
    }    
    /**
     * Disconnect the listener for the <code>running</code> signal on this Queue
     * 
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(RUNNING listener) {
        disconnect(RUNNING.class, listener);
    }

	/**
	 * Reports that the buffer became empty (underrun). A buffer is empty 
	 * if the total amount of data inside it (num-buffers, time, size) is 
	 * lower than the boundary values which can be set through the GObject properties.
     * 
     * @see #connect(UNDERRUN)
     * @see #disconnect(UNDERRUN)
     */
    public static interface UNDERRUN {
        /**
         * @param queue the object which received the signal
         */
        public void underrun(Queue queue);
    }
    /**
     * Add a listener for the <code>underrun</code> signal on this Queue
     * 
     * @param listener The listener to be called.
     */
    public void connect(final UNDERRUN listener) {
        connect(UNDERRUN.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Queue queue) {
                listener.underrun(queue);
            }
        });
    }    
    /**
     * Disconnect the listener for the <code>underrun</code> signal on this Queue
     * 
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(UNDERRUN listener) {
        disconnect(UNDERRUN.class, listener);
    }

    /**
     *  Reports when the queue has enough data to start pushing data again on the source pad.
     *  
     * @see #connect(PUSHING)
     * @see #disconnect(PUSHING)
     */
    public static interface PUSHING {
        /**
         * @param queue the object which received the signal
         */
        public void pushing(Queue queue);
    }
    /**
     * Add a listener for the <code>pushing</code> signal on this Queue
     * 
     * @param listener The listener to be called.
     */
    public void connect(final PUSHING listener) {
        connect(PUSHING.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Queue queue) {
                listener.pushing(queue);
            }
        });
    }    
    /**
     * Disconnect the listener for the <code>pushing</code> signal on this Queue
     * 
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(PUSHING listener) {
        disconnect(PUSHING.class, listener);
    }
}
