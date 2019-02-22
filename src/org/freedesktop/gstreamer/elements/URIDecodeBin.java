/* 
 * Copyright (c) 2019 Neil C Smith
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.glib.NativeEnum;
import org.freedesktop.gstreamer.query.Query;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValueArray;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GValueAPI;

/**
 * A {@link Bin} that decodes data from a URI into raw media. It selects a
 * source element that can handle the given “uri” scheme and connects it to a
 * decodebin.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-plugins/html/gst-plugins-base-plugins-uridecodebin.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-plugins/html/gst-plugins-base-plugins-uridecodebin.html</a>
 * <p>
 */
public class URIDecodeBin extends Bin {

    public static final String GST_NAME = "uridecodebin";
    public static final String GTYPE_NAME = "GstURIDecodeBin";

    public URIDecodeBin(final String name) {
        this(makeRawElement(GST_NAME, name));
    }

    URIDecodeBin(final Initializer init) {
        super(init);
    }

    /**
     * Signal is emitted when a pad for which there is no further possible
     * decoding is added to the {@link URIDecodeBin}.
     */
    public static interface UNKNOWN_TYPE {

        /**
         * @param element The element which has the new Pad.
         * @param pad the new Pad.
         * @param caps the caps of the pad that cannot be resolved.
         */
        public void unknownType(URIDecodeBin element, Pad pad, Caps caps);
    }

    /**
     * Adds a listener for the <code>unknown-type</code> signal
     *
     * @param listener Listener to be called
     */
    public void connect(final UNKNOWN_TYPE listener) {
        connect(UNKNOWN_TYPE.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(URIDecodeBin elem, Pad pad, Caps caps) {
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
     * Signal is emitted when a pad for which there is no further possible
     * decoding is added to the {@link URIDecodeBin}.
     */
    public static interface AUTOPLUG_CONTINUE {

        /**
         * Autoplug continue signal.
         *
         * @param element The element which has the new Pad.
         * @param pad the new Pad.
         * @param caps the caps of the pad that cannot be resolved.
         * @return TRUE if you wish decodebin to look for elements that can
         * handle the given caps . If FALSE, those caps will be considered as
         * final and the pad will be exposed as such (see 'pad-added' signal of
         * {@link Element}).
         */
        public boolean autoplugContinue(URIDecodeBin element, Pad pad, Caps caps);
    }

    /**
     * Adds a listener for the <code>autoplug-continue</code> signal
     *
     * @param listener Listener to be called
     */
    public void connect(final AUTOPLUG_CONTINUE listener) {
        connect(AUTOPLUG_CONTINUE.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(URIDecodeBin elem, Pad pad, Caps caps) {
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
     * This function is emitted when an array of possible factories for caps on
     * pad is needed. {@link URIDecodeBin} will by default return an array with all
     * compatible factories, sorted by rank.
     * <p>
     * If this function returns {@link Optional#EMPTY}, pad will be exposed as a
     * final caps.
     * <p>
     * If this function returns an empty list, the pad will be considered as
     * having an unhandled type media type.
     */
    public static interface AUTOPLUG_FACTORIES {

        /**
         * @param element The element which has the new Pad.
         * @param pad the new Pad.
         * @param caps the caps of the pad that cannot be resolved.
         * @return
         */
        public Optional<List<ElementFactory>> autoplugFactories(URIDecodeBin element, Pad pad, Caps caps);
    }

    /**
     * Adds a listener for the <code>autoplug-factories</code> signal
     *
     * @param listener Listener to be called
     */
    public void connect(final AUTOPLUG_FACTORIES listener) {
        connect(AUTOPLUG_FACTORIES.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public GValueArray callback(URIDecodeBin elem, Pad pad, Caps caps) {
                Optional<List<ElementFactory>> factories
                        = listener.autoplugFactories(elem, pad, caps);
                return factories.map(list -> {
                    GValueArray array = new GValueArray(list.size(), false);
                    list.forEach(factory -> array.append(new GValueAPI.GValue(GType.OBJECT, factory)));
                    return array;
                }).orElse(null);
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
     * Once {@link URIDecodeBin} has found the possible ElementFactory objects to
     * try for caps on pad, this signal is emitted. The purpose of the signal is
     * for the application to perform additional sorting or filtering on the
     * element factory array.
     *
     * The callee should copy and modify factories.
     */
    public static interface AUTOPLUG_SORT {

        /**
         * Autoplug sort
         *
         * @param element The element which has the new Pad
         * @param pad the new Pad
         * @param caps the caps of the pad that cannot be resolved
         * @param factories A List of possible {@link ElementFactory} to use
         * @return resorted list or {@link Optional#EMPTY}
         */
        public Optional<List<ElementFactory>> autoplugSort(
                URIDecodeBin element, Pad pad, Caps caps, List<ElementFactory> factories);
    }

    /**
     * Adds a listener for the <code>autoplug-sort</code> signal
     *
     * @param listener Listener to be called
     */
    public void connect(final AUTOPLUG_SORT listener) {
        connect(AUTOPLUG_SORT.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public GValueArray callback(URIDecodeBin elem, Pad pad, Caps caps, GValueArray factories) {
                int size = factories.getNValues();
                List<ElementFactory> list = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    Object factory = factories.getValue(i);
                    if (factory instanceof ElementFactory) {
                        list.add((ElementFactory) factory);
                    }
                }
                Optional<List<ElementFactory>> response
                        = listener.autoplugSort(elem, pad, caps, list);
                return response.map(l -> {
                    GValueArray array = new GValueArray(l.size(), false);
                    list.forEach(factory -> array.append(new GValueAPI.GValue(GType.OBJECT, factory)));
                    return array;
                }).orElse(null);
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

    public enum AutoplugSelectResult implements NativeEnum<AutoplugSelectResult> {
        TRY(0),
        EXPOSE(1),
        SKIP(2);

        AutoplugSelectResult(final int value) {
            this.value = value;
        }

        /**
         * Gets the integer value of the enum.
         *
         * @return The integer value for this enum.
         */
        @Override
        public int intValue() {
            return value;
        }
        private final int value;
    }

    /**
     * Once {@link URIDecodeBin} has found the possible ElementFactory objects to
     * try for caps on pad, this signal is emitted. The purpose of the signal is
     * for the application to perform additional filtering on the element
     * factory array.
     * <p>
     * The signal handler should return a {@link AutoplugSelectResult} value
     * indicating what decodebin should do next.
     * <p>
     * A value of {@link AutoplugSelectResult#TRY} will try to autoplug an
     * element from factory.
     * <p>
     * A value of {@link AutoplugSelectResult#EXPOSE} will expose pad without
     * plugging any element to it.
     * <p>
     * A value of {@link AutoplugSelectResult#SKIP} will skip factory and move
     * to the next factory.
     */
    public static interface AUTOPLUG_SELECT {

        /**
         * @param element The decodebin
         * @param pad the new Pad
         * @param caps the caps of the pad
         * @param factory the {@link ElementFactory} to use
         * @return an {@link AutoplugSelectResult} that indicates the required
         * operation. the default handler will always return
         * {@link AutoplugSelectResult#TRY}
         */
        public AutoplugSelectResult autoplugSelect(final URIDecodeBin element, final Pad pad, final Caps caps, final ElementFactory factory);
    }

    /**
     * Adds a listener for the <code>autoplug-select</code> signal
     *
     * @param listener Listener to be called
     */
    public void connect(final AUTOPLUG_SELECT listener) {
        connect(AUTOPLUG_SELECT.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public AutoplugSelectResult callback(final URIDecodeBin elem, final Pad pad, final Caps caps, final ElementFactory factory) {
                return listener.autoplugSelect(elem, pad, caps, factory);
            }
        });
    }

    /**
     * Removes a listener for the <code>autoplug-select</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(final AUTOPLUG_SELECT listener) {
        disconnect(AUTOPLUG_SELECT.class, listener);
    }

    /**
     * This signal is emitted whenever an autoplugged element that is not linked
     * downstream yet and not exposed does a query. It can be used to tell the
     * element about the downstream supported caps for example..
     */
    public static interface AUTOPLUG_QUERY {

        /**
         * @param element the decodebin.
         * @param pad the pad.
         * @param child the child element doing the query
         * @param query the query.
         */
        public boolean autoplugQuery(URIDecodeBin element, Pad pad, Element child, Query query);
    }

    /**
     * Adds a listener for the <code>autoplug-query</code> signal
     *
     * @param listener Listener to be called
     */
    public void connect(final AUTOPLUG_QUERY listener) {
        connect(AUTOPLUG_QUERY.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(URIDecodeBin elem, Pad pad, Element child, Query query) {
                return listener.autoplugQuery(elem, pad, child, query);
            }
        });
    }

    /**
     * Removes a listener for the <code>autoplug-query</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(AUTOPLUG_QUERY listener) {
        disconnect(AUTOPLUG_QUERY.class, listener);
    }

    /**
     * This signal is emitted once {@link URIDecodeBin} has finished decoding all
     * the data.
     */
    public static interface DRAINED {

        /**
         * @param element The element
         */
        public void drained(URIDecodeBin element);
    }

    /**
     * Adds a listener for the <code>drained</code> signal
     *
     * @param listener Listener to be called
     */
    public void connect(final DRAINED listener) {
        connect(DRAINED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(URIDecodeBin elem) {
                listener.drained(elem);
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

    /**
     * Signal is emitted after the source has been created, so it can be
     * configured by setting additionnal properties.
     */
    public static interface SOURCE_SETUP {

        /**
         *
         * @param bin the container.
         * @param elem the created source
         */
        public void sourceSetup(URIDecodeBin bin, Element elem);
    }

    /**
     * Adds a listener for the <code>source-setup</code> signal
     *
     * @param source-setup Listener to be called
     */
    public void connect(final SOURCE_SETUP listener) {
        this.connect(SOURCE_SETUP.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(final URIDecodeBin bin, final Element elem) {
                listener.sourceSetup(bin, elem);
            }
        });
    }

    /**
     * Removes a listener for the <code>source-setup</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(final SOURCE_SETUP listener) {
        this.disconnect(SOURCE_SETUP.class, listener);
    }

}
