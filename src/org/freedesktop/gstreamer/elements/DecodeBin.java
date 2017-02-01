/*
 * Copyright (c) 2010 DHoyt <david.g.hoyt@gmail.com>
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

import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValueArray;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

/**
 * Utility {@link org.gstreamer.Element} to automatically identify media stream types and hook
 * up elements.
 */
public class DecodeBin extends Bin {
	public static final String GST_NAME = "decodebin";
    public static final String GTYPE_NAME = "GstDecodeBin";


    public DecodeBin() {
    	this((String) null);
    }
    
    /**
     * Creates a new DecodeBin.
     *
     * @param name The name used to identify this DecodeBin.
     */
    public DecodeBin(String name) {
        super(makeRawElement(GST_NAME, name));
    }

    public DecodeBin(Initializer init) {
        super(init);
    }

    /**
     * Signal is emitted when a pad for which there is no further possible decoding is added to the {@link DecodeBin}.
     */
    public static interface UNKNOWN_TYPE {
        /**
         * @param element The element which has the new Pad.
         * @param pad the new Pad.
         * @param caps the caps of the pad that cannot be resolved.
         */
        public void unknownType(DecodeBin element, Pad pad, Caps caps);
    }
    /**
     * Adds a listener for the <code>unknown-type</code> signal
     *
     * @param listener Listener to be called when a new {@link Pad} is encountered
     * on the {@link Element}
     */
    public void connect(final UNKNOWN_TYPE listener) {
        connect(UNKNOWN_TYPE.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(DecodeBin elem, Pad pad, Caps caps) {
                listener.unknownType(elem, pad, caps);
            }
        });
    }
    /**
     * Removes a listener for the <code>unknown-type</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(UNKNOWN_TYPE listener) {
        disconnect(UNKNOWN_TYPE.class, listener);
    }

    /**
     * Signal is emitted when a pad for which there is no further possible decoding is added to the {@link DecodeBin}.
     */
    public static interface AUTOPLUG_CONTINUE {
        /**
         * @param element The element which has the new Pad.
         * @param pad the new Pad.
         * @param caps the caps of the pad that cannot be resolved.
         */
        public boolean autoplugContinue(DecodeBin element, Pad pad, Caps caps);
    }
    /**
     * Adds a listener for the <code>autoplug-continue</code> signal
     *
     * @param listener Listener to be called when a new {@link Pad} is encountered
     * on the {@link Element}
     */
    public void connect(final AUTOPLUG_CONTINUE listener) {
        connect(AUTOPLUG_CONTINUE.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(DecodeBin elem, Pad pad, Caps caps) {
                return listener.autoplugContinue(elem, pad, caps);
            }
        });
    }
    /**
     * Removes a listener for the <code>autoplug-continue</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(AUTOPLUG_CONTINUE listener) {
        disconnect(AUTOPLUG_CONTINUE.class, listener);
    }

    /**
     * This function is emitted when an array of possible factories for caps on pad is needed.
     * {@link DecodeBin} will by default return an array with all compatible factories, sorted by rank.
     *
     * If this function returns NULL, pad will be exposed as a final caps.
     *
     * If this function returns an empty array, the pad will be considered as having an unhandled type media type.
     */
    public static interface AUTOPLUG_FACTORIES {
        /**
         * @param element The element which has the new Pad.
         * @param pad the new Pad.
         * @param caps the caps of the pad that cannot be resolved.
         */
        public GValueArray autoplugFactories(DecodeBin element, Pad pad, Caps caps);
    }
    /**
     * Adds a listener for the <code>autoplug-factories</code> signal
     *
     * @param listener Listener to be called when a new {@link Pad} is encountered
     * on the {@link Element}
     */
    public void connect(final AUTOPLUG_FACTORIES listener) {
        connect(AUTOPLUG_FACTORIES.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public GValueArray callback(DecodeBin elem, Pad pad, Caps caps) {
                return listener.autoplugFactories(elem, pad, caps);
            }
        });
    }
    /**
     * Removes a listener for the <code>autoplug-factories</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(AUTOPLUG_FACTORIES listener) {
        disconnect(AUTOPLUG_FACTORIES.class, listener);
    }

    /**
     * Once {@link DecodeBin} has found the possible ElementFactory objects to
     * try for caps on pad, this signal is emitted. The purpose of the signal is
     * for the application to perform additional sorting or filtering on the
     * element factory array.
     *
     * The callee should copy and modify factories.
     */
    public static interface AUTOPLUG_SORT {
        /**
         * @param element The element which has the new Pad.
         * @param pad the new Pad.
         * @param caps the caps of the pad that cannot be resolved.
         * @param factories A GValueArray of possible GstElementFactory to use.
         */
        public GValueArray autoplugSort(DecodeBin element, Pad pad, Caps caps, GValueArray factories);
    }
    /**
     * Adds a listener for the <code>autoplug-sort</code> signal
     *
     * @param listener Listener to be called when a new {@link Pad} is encountered
     * on the {@link Element}
     */
    public void connect(final AUTOPLUG_SORT listener) {
        connect(AUTOPLUG_SORT.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public GValueArray callback(DecodeBin elem, Pad pad, Caps caps, GValueArray factories) {
                return listener.autoplugSort(elem, pad, caps, factories);
            }
        });
    }
    /**
     * Removes a listener for the <code>autoplug-sort</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(AUTOPLUG_SORT listener) {
        disconnect(AUTOPLUG_SORT.class, listener);
    }

    /**
     * This signal is emitted once {@link DecodeBin} has finished decoding all the data.
     */
    public static interface DRAINED {
        /**
         * @param element The element
         */
        public GValueArray drained(DecodeBin element);
    }
    /**
     * Adds a listener for the <code>drained</code> signal
     *
     * @param listener Listener to be called when a new {@link Pad} is encountered
     * on the {@link Element}
     */
    public void connect(final DRAINED listener) {
        connect(DRAINED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public GValueArray callback(DecodeBin elem) {
                return listener.drained(elem);
            }
        });
    }
    /**
     * Removes a listener for the <code>drained</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(DRAINED listener) {
        disconnect(DRAINED.class, listener);
    }
}
