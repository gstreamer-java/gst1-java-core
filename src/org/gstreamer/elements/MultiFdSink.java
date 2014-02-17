/*
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2009 Wayne Meissner
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 2007 David Schleef <ds@schleef.org>
 *           (C) 2008 Wim Taymans <wim.taymans@gmail.com>
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
import org.gstreamer.lowlevel.IntegerEnum;
import org.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * A sink {@link org.gstreamer.Element} that send data to multiple filedescriptors.
 */
public class MultiFdSink extends BaseSink {
    public static final String GST_NAME = "multifdsink";
    public static final String GTYPE_NAME = "GstMultiFdSink";

    public enum ClientStatus implements IntegerEnum {
        /** Client is ok */
    	OK(0),
        /** Client is closed the socket */
    	CLOSED(1),
        /** Client is removed */
    	REMOVED(2),
        /** Client is too slow */
    	SLOW(3),
        /** Client is in error */
    	ERROR(4),
        /** Same client added twice */
    	DUPLICATE(5),
        /** Client is flushing out the remaining buffers */
    	FLUSHING(6),
        /** Unknown reply value */
        @DefaultEnumValue
        UNKNOWN(~0);
        
    	ClientStatus (int value) {
            this.value = value;
        }
        
        /**
         * Gets the integer value of the enum.
         * @return The integer value for this enum.
         */
        public int intValue() {
            return value;
        }
        private final int value;
    }    
    
    public MultiFdSink(String name) {
        this(makeRawElement(GST_NAME, name));
    }  

    public MultiFdSink(Initializer init) {
        super(init);
    }

    /**
     * The given file descriptor was added to {@link MultiFdSink}. This signal will be 
     * emitted from the streaming thread so application should be prepared for that.
     */
    public static interface CLIENT_ADDED {
        /**
         * @param fd the file descriptor to add to multifdsink
         */
        public void clientAdded(MultiFdSink elem, int fd);
    }
    /**
     * Adds a listener for the <code>client-added</code> signal.
     *
     * @param listener
     */
    public void connect(final CLIENT_ADDED listener) {
        connect(CLIENT_ADDED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(MultiFdSink elem, int fd) {
                listener.clientAdded(elem, fd);
            }
        });
    }
    /**
     * Removes a listener for the <code>client-added</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(CLIENT_ADDED listener) {
        disconnect(CLIENT_ADDED.class, listener);
    }

    /**
     * The given file descriptor is about to be removed from {@link MultiFdSink}. 
     * This signal will be emitted from the streaming thread so applications should be prepared for that.
     */
    public static interface CLIENT_REMOVED {
        /**
         * @param fd the file descriptor to add to multifdsink
         */
        public void clientRemoved(MultiFdSink elem, int fd, ClientStatus status);
    }
    /**
     * Adds a listener for the <code>client-removed</code> signal.
     *
     * @param listener
     */
    public void connect(final CLIENT_REMOVED listener) {
        connect(CLIENT_REMOVED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(MultiFdSink elem, int fd, ClientStatus status) {
                listener.clientRemoved(elem, fd, status);
            }
        });
    }
    /**
     * Removes a listener for the <code>client-removed</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(CLIENT_REMOVED listener) {
        disconnect(CLIENT_REMOVED.class, listener);
    }

    /**
     * The given file descriptor is about to be removed from {@link MultiFdSink}. 
     * This signal will be emitted from the streaming thread so applications should be prepared for that.
     */
    public static interface CLIENT_FD_REMOVED {
        /**
         * @param fd the file descriptor to add to multifdsink
         */
        public void clientRemoved(MultiFdSink elem, int fd);
    }
    /**
     * Adds a listener for the <code>client-removed</code> signal.
     *
     * @param listener
     */
    public void connect(final CLIENT_FD_REMOVED listener) {
        connect(CLIENT_FD_REMOVED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(MultiFdSink elem, int fd) {
                listener.clientRemoved(elem, fd);
            }
        });
    }
    /**
     * Removes a listener for the <code>client-removed</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(CLIENT_FD_REMOVED listener) {
        disconnect(CLIENT_FD_REMOVED.class, listener);
    }
}
