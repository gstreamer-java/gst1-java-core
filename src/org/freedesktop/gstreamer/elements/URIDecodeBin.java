/* 
 * Copyright (c) 2016 Christophe Lafolet
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
import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValueArray;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;
import org.freedesktop.gstreamer.lowlevel.IntegerEnum;

/**
 * Java representation of gstreamer uridecodebin
 */

public class URIDecodeBin extends Bin {

	public static final String GST_NAME = "uridecodebin";
    public static final String GTYPE_NAME = "GstURIDecodeBin";

    public URIDecodeBin(final String name) {
        this(makeRawElement(GST_NAME, name));
    }

    public URIDecodeBin(final Initializer init) {
        super(init);
    }

    /**
     * Signal is emitted when a pad for which there is no further possible decoding is added to the {@link URIDecodeBin}.
     */
    public static interface AUTOPLUG_CONTINUE {
        /**
         * @param element The element which has the new Pad.
         * @param pad the new Pad.
         * @param caps the caps of the pad that cannot be resolved.
         */
        public boolean autoplugContinue(final URIDecodeBin element, final Pad pad, final Caps caps);
    }
    /**
     * Adds a listener for the <code>autoplug-continue</code> signal
     *
     * @param autoplug_CONTINUE Listener to be called when a new {@link Pad} is encountered
     * on the {@link Element}
     */
    public void connect(final AUTOPLUG_CONTINUE autoplug_CONTINUE) {
        connect(AUTOPLUG_CONTINUE.class, autoplug_CONTINUE, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(final URIDecodeBin elem, final Pad pad, final Caps caps) {
                return autoplug_CONTINUE.autoplugContinue(elem, pad, caps);
            }
        });
    }
    /**
     * Removes a listener for the <code>autoplug-continue</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(final AUTOPLUG_CONTINUE listener) {
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
        public GValueArray autoplugFactories(final URIDecodeBin element, final Pad pad, final Caps caps);
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
            public GValueArray callback(final URIDecodeBin elem, final Pad pad, final Caps caps) {
                return listener.autoplugFactories(elem, pad, caps);
            }
        });
    }
    /**
     * Removes a listener for the <code>autoplug-factories</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(final AUTOPLUG_FACTORIES listener) {
        disconnect(AUTOPLUG_FACTORIES.class, listener);
    }

    public enum GstAutoplugSelectResult implements IntegerEnum {
    	GST_AUTOPLUG_SELECT_TRY(0),
    	GST_AUTOPLUG_SELECT_EXPOSE(1),
    	GST_AUTOPLUG_SELECT_SKIP(2);

    	GstAutoplugSelectResult (final int value) {
            this.value = value;
        }
        /**
         * Gets the integer value of the enum.
         * @return The integer value for this enum.
         */
        @Override
		public int intValue() {
            return value;
        }
        private final int value;
    }

    /**
     * Once {@link URIdecodebin} has found the possible ElementFactory objects to
     * try for caps on pad, this signal is emitted. The purpose of the signal is
     * for the application to perform additional filtering on the
     * element factory array.
     *
     * The callee should copy and modify factories.
     */
    public static interface AUTOPLUG_SELECT {
        /**
         * @param element The element which has the new Pad.
         * @param pad the new Pad.
         * @param caps the caps of the pad that cannot be resolved.
         * @param factories A GValueArray of possible GstElementFactory to use.
         */
        public GstAutoplugSelectResult autoplugSelect(final URIDecodeBin element, final Pad pad, final Caps caps, final ElementFactory factory);
    }
    /**
     * Adds a listener for the <code>autoplug-select</code> signal
     *
     * @param listener Listener to be called when a new {@link Pad} is encountered
     * on the {@link Element}
     */
    public void connect(final AUTOPLUG_SELECT listener) {
        connect(AUTOPLUG_SELECT.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public GstAutoplugSelectResult callback(final URIDecodeBin elem, final Pad pad, final Caps caps, final ElementFactory factory) {
                return listener.autoplugSelect(elem, pad, caps, factory);
            }
        });
    }
    /**
     * Removes a listener for the <code>autoplug-sort</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(final AUTOPLUG_SELECT listener) {
        disconnect(AUTOPLUG_SELECT.class, listener);
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
        public GValueArray autoplugSort(URIDecodeBin element, Pad pad, Caps caps, GValueArray factories);
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
            public GValueArray callback(URIDecodeBin elem, Pad pad, Caps caps, GValueArray factories) {
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



}
