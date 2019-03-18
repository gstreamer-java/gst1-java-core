/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2016 Christophe Lafolet
 * Copyright (c) 2009 Levente Farkas
 * Copyright (C) 2007 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2004 Wim Taymans <wim@fluendo.com>
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
package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GstBinAPI.GSTBIN_API;

import com.sun.jna.Pointer;
import java.util.EnumSet;
import java.util.List;
import org.freedesktop.gstreamer.glib.NativeFlags;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;
import org.freedesktop.gstreamer.lowlevel.GstObjectPtr;

/**
 * Base class and element that can contain other elements.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstBin.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstBin.html</a>
 * <p>
 * Bin is an element that can contain other {@link Element}s, allowing them to
 * be managed as a group.
 * <p>
 * Pads from the child elements can be ghosted to the bin, see {@link GhostPad}.
 * This makes the bin look like any other elements and enables creation of
 * higher-level abstraction elements.
 * <p>
 * A new {@link Bin} is created with {@link Bin#Bin(String)}. Use a
 * {@link Pipeline} instead if you want to create a toplevel bin because a
 * normal bin doesn't have a bus or handle clock distribution of its own.
 * <p>
 * After the bin has been created you will typically add elements to it with
 * {@link Bin#add(Element)}. Elements can be removed with
 * {@link Bin#remove(Element)}
 * <p>
 * An element can be retrieved from a bin with
 * {@link Bin#getElementByName(String)}.
 * <p>
 * A list of elements contained in a bin can be retrieved with
 * {@link Bin#getElements}
 *
 * The {@link ELEMENT_ADDED} signal is fired whenever a new element is added to
 * the bin. Likewise the {@link ELEMENT_REMOVED} signal is fired whenever an
 * element is removed from the bin.
 *
 */
public class Bin extends Element {

    public static final String GST_NAME = "bin";
    public static final String GTYPE_NAME = "GstBin";

    protected Bin(Initializer init) {
        super(init);
    }

    Bin(Handle handle, boolean needRef) {
        super(handle, needRef);
    }
    
    /**
     * Creates a new Bin with a unique name.
     */
    public Bin() {
        this(Natives.initializer(GSTBIN_API.ptr_gst_bin_new(null), false, true));
    }

    /**
     * Creates a new Bin with the given name.
     *
     * @param name The Name to assign to the new Bin
     */
    public Bin(String name) {
        this(Natives.initializer(GSTBIN_API.ptr_gst_bin_new(name), false, true));
    }

    /**
     * Adds an Element to this Bin.
     * <p>
     * Sets the element's parent, and thus takes ownership of the element. An
     * element can only be added to one bin.
     * <p>
     * If the element's pads are linked to other pads, the pads will be unlinked
     * before the element is added to the bin.
     *
     * @param element The {@link Element} to add to this Bin.
     * @return true if the element was successfully added, false if the Bin will
     * not accept the element.
     */
    public boolean add(Element element) {
        return GSTBIN_API.gst_bin_add(this, element);
    }

    /**
     * Adds an array of Element objects to this Bin
     *
     * @param elements The array of {@link Element} to add to this Bin
     * @see Bin#add(Element)
     */
    public void addMany(Element... elements) {
        GSTBIN_API.gst_bin_add_many(this, elements);
    }

    /**
     * Removes a Element from this Bin
     * <p>
     * Removes the element from the bin, unparenting it as well.
     *
     * If the element's pads are linked to other pads, the pads will be unlinked
     * before the element is removed from the bin.
     *
     * @param element The {@link Element} to remove
     * @return true if the element was successfully removed
     */
    public boolean remove(Element element) {
        return GSTBIN_API.gst_bin_remove(this, element);
    }

    /**
     * Removes an array of {@link Element} objects from this Bin
     *
     * @param elements The list {@link Element} to remove
     */
    public void removeMany(Element... elements) {
        GSTBIN_API.gst_bin_remove_many(this, elements);
    }

    private List<Element> elementList(Pointer iter) {
        return new GstIterator<Element>(iter, Element.class).asList();
    }

    /**
     * Retrieve a list of the {@link Element}s contained in the Bin.
     *
     * @return The List of {@link Element}s.
     */
    public List<Element> getElements() {
        return elementList(GSTBIN_API.gst_bin_iterate_elements(this));
    }

    /**
     * Gets an a list of the elements in this bin in topologically sorted order.
     * This means that the elements are returned from the most downstream
     * elements (sinks) to the sources.
     *
     * @return The List of {@link Element}s.
     */
    public List<Element> getElementsSorted() {
        return elementList(GSTBIN_API.gst_bin_iterate_sorted(this));
    }

    /**
     * Retrieve a list of the {@link Element}s contained in the Bin and its Bin
     * children.
     *
     * This differs from {@link #getElements()} as it will also return
     * {@link Element}s that are in any Bin elements contained in this Bin, also
     * recursing down those Bins.
     *
     * @return The List of {@link Element}s.
     */
    public List<Element> getElementsRecursive() {
        return elementList(GSTBIN_API.gst_bin_iterate_recurse(this));
    }

    /**
     * Retrieve a list of the sink {@link Element}s contained in the Bin.
     *
     * @return The List of sink {@link Element}s.
     */
    public List<Element> getSinks() {
        return elementList(GSTBIN_API.gst_bin_iterate_sinks(this));
    }

    /**
     * Retrieve a list of the source {@link Element}s contained in the Bin.
     *
     * @return The List of source {@link Element}s.
     */
    public List<Element> getSources() {
        return elementList(GSTBIN_API.gst_bin_iterate_sources(this));
    }

    /**
     * Gets the {@link Element} with the given name from the bin. This function
     * recurses into child bins.
     *
     * @param name The name of the {@link Element} to find.
     * @return The {@link Element} if found, else null.
     */
    public Element getElementByName(String name) {
        return GSTBIN_API.gst_bin_get_by_name(this, name);
    }

    /**
     * Gets the element with the given name from this bin. If the element is not
     * found, a recursion is performed on the parent bin.
     *
     * @param name The name of the {@link Element} to find.
     * @return The {@link Element} if found, else null.
     */
    public Element getElementByNameRecurseUp(String name) {
        return GSTBIN_API.gst_bin_get_by_name_recurse_up(this, name);
    }

//    /**
//     * Looks for an element inside the bin that implements the given interface.
//     * If such an element is found, it returns the element.
//     *
//     * @param iface The class of the {@link Element} to search for.
//     * @return The {@link Element} that implements the interface.
//     */
//    public <T extends Element> T getElementByInterface(Class<T> iface) {
//        return iface.cast(GSTBIN_API.gst_bin_get_by_interface(this, GstTypes.typeFor(iface)));
//    }
    /**
     * To aid debugging applications one can use this method to write out the
     * whole network of gstreamer elements that form the pipeline into a dot
     * file. This file can be processed with graphviz to get an image. e.g. dot
     * -Tpng -oimage.png graph_lowlevel.dot
     * <p>
     * The function is only active if gstreamer is configured with
     * "--gst-enable-gst-debug" and the environment variable
     * GST_DEBUG_DUMP_DOT_DIR is set to a basepath (e.g. /tmp).
     *
     * @param details to show in the graph
     * @param fileName output base filename (e.g. "myplayer")
     */
    public void debugToDotFile(EnumSet<DebugGraphDetails> details, String fileName) {
        GSTBIN_API.gst_debug_bin_to_dot_file(
                this, NativeFlags.toInt(details), fileName);
    }

    /**
     * To aid debugging applications one can use this method to write out the
     * whole network of gstreamer elements that form the pipeline into a dot
     * file. This file can be processed with graphviz to get an image. e.g. dot
     * -Tpng -oimage.png graph_lowlevel.dot
     * <p>
     * The function is only active if gstreamer is configured with
     * "--gst-enable-gst-debug" and the environment variable
     * GST_DEBUG_DUMP_DOT_DIR is set to a basepath (e.g. /tmp).
     * <p>
     * Unlike {@link #debugToDotFile(java.util.EnumSet, java.lang.String)} this
     * method adds the current timestamp to the filename, so that it can be
     * used to take multiple snapshots.
     * 
     * @param details to show in the graph
     * @param fileName output base filename (e.g. "myplayer")
     */
    public void debugToDotFileWithTS(EnumSet<DebugGraphDetails> details, String fileName) {
        GSTBIN_API.gst_debug_bin_to_dot_file_with_ts(
                this, NativeFlags.toInt(details), fileName);
    }

    /**
     * Signal emitted when an {@link Element} is added to this Bin
     *
     * @see #connect(ELEMENT_ADDED)
     * @see #disconnect(ELEMENT_ADDED)
     */
    public static interface ELEMENT_ADDED {

        /**
         * Called when an {@link Element} is added to a {@link Bin}
         *
         * @param bin the Bin the element was added to.
         * @param element the {@link Element} that was added.
         */
        public void elementAdded(Bin bin, Element element);
    }

    /**
     * Add a listener for the <code>element-added</code> signal on this Bin
     *
     * @param listener The listener to be called when an {@link Element} is
     * added.
     */
    public void connect(final ELEMENT_ADDED listener) {
        connect(ELEMENT_ADDED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Bin bin, Element elem) {
                listener.elementAdded(bin, elem);
            }
        });
    }

    /**
     * Disconnect the listener for the <code>element-added</code> signal
     *
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(ELEMENT_ADDED listener) {
        disconnect(ELEMENT_ADDED.class, listener);
    }

    /**
     * Signal emitted when an {@link Element} is removed from this Bin
     *
     * @see #connect(ELEMENT_REMOVED)
     * @see #disconnect(ELEMENT_REMOVED)
     */
    public static interface ELEMENT_REMOVED {

        /**
         * Called when an {@link Element} is removed from a {@link Bin}
         *
         * @param bin the Bin the element was removed from.
         * @param element the {@link Element} that was removed.
         */
        public void elementRemoved(Bin bin, Element element);
    }

    /**
     * Add a listener for the <code>element-removed</code> signal on this Bin
     *
     * @param listener The listener to be called when an {@link Element} is
     * removed.
     */
    public void connect(final ELEMENT_REMOVED listener) {
        connect(ELEMENT_REMOVED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Bin bin, Element elem) {
                listener.elementRemoved(bin, elem);
            }
        });
    }

    /**
     * Disconnect the listener for the <code>element-removed</code> signal
     *
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(ELEMENT_REMOVED listener) {
        disconnect(ELEMENT_REMOVED.class, listener);
    }

    /**
     * Signal emitted when an {@link Element} is added to sub-bin of this
     * {@link Bin}
     *
     * @see #connect(DEEP_ELEMENT_ADDED)
     * @see #disconnect(DEEP_ELEMENT_ADDED)
     */
    @Gst.Since(minor = 10)
    public static interface DEEP_ELEMENT_ADDED {

        /**
         * Called when an {@link Element} is added to a {@link Bin}
         *
         * Since GStreamer 1.10
         *
         * @param bin the Bin
         * @param sub_bin the Bin the element was added to.
         * @param element the {@link Element} that was added.
         */
        public void elementAdded(Bin bin, Bin sub_bin, Element element);
    }

    /**
     * Add a listener for the <code>deep-element-added</code> signal on this Bin
     *
     * @param listener The listener to be called when an {@link Element} is
     * added.
     */
    @Gst.Since(minor = 10)
    public void connect(final DEEP_ELEMENT_ADDED listener) {
        Gst.checkVersion(1, 10);
        connect(DEEP_ELEMENT_ADDED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Bin bin, Bin sub_bin, Element elem) {
                listener.elementAdded(bin, sub_bin, elem);
            }
        });
    }

    /**
     * Disconnect the listener for the <code>deep-element-added</code> signal
     *
     * @param listener The listener that was registered to receive the signal.
     */
    @Gst.Since(minor = 10)
    public void disconnect(DEEP_ELEMENT_ADDED listener) {
        disconnect(DEEP_ELEMENT_ADDED.class, listener);
    }

    /**
     * Signal emitted when an {@link Element} is removed from sub-bin of this
     * {@link Bin}
     *
     * @see #connect(ELEMENT_REMOVED)
     * @see #disconnect(ELEMENT_REMOVED)
     */
    @Gst.Since(minor = 10)
    public static interface DEEP_ELEMENT_REMOVED {

        /**
         * Called when an {@link Element} is removed from a {@link Bin}
         *
         * Since GStreamer 1.10
         *
         * @param bin the Bin
         * @param sub_bin the Bin the element was removed from.
         * @param element the {@link Element} that was removed.
         */
        public void elementRemoved(Bin bin, Bin sub_bin, Element element);
    }

    /**
     * Add a listener for the <code>deep-element-removed</code> signal on this
     * Bin
     *
     * @param listener The listener to be called when an {@link Element} is
     * removed.
     */
    @Gst.Since(minor = 10)
    public void connect(final DEEP_ELEMENT_REMOVED listener) {
        Gst.checkVersion(1, 10);
        connect(DEEP_ELEMENT_REMOVED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Bin bin, Bin sub_bin, Element elem) {
                listener.elementRemoved(bin, sub_bin, elem);
            }
        });
    }

    /**
     * Disconnect the listener for the <code>deep-element-removed</code> signal
     *
     * @param listener The listener that was registered to receive the signal.
     */
    @Gst.Since(minor = 10)
    public void disconnect(DEEP_ELEMENT_REMOVED listener) {
        disconnect(DEEP_ELEMENT_REMOVED.class, listener);
    }

    /**
     * Signal emitted when an {@link Element} has latency
     *
     * @see #connect(DO_LATENCY)
     * @see #disconnect(DO_LATENCY)
     */
    public static interface DO_LATENCY {

        /**
         * Called when an {@link Element} is removed from a {@link Bin}
         *
         * @param bin the Bin the element was removed from.
         */
        public void doLatency(Bin bin);
    }

    /**
     * Add a listener for the <code>do-latency</code> signal on this Bin
     *
     * @param listener The listener to be called when an {@link Element} is
     * removed.
     */
    public void connect(final DO_LATENCY listener) {
        connect(DO_LATENCY.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(Bin bin) {
                listener.doLatency(bin);
            }
        });
    }

    /**
     * Disconnect the listener for the <code>do-latency</code> signal
     *
     * @param listener The listener that was registered to receive the signal.
     */
    public void disconnect(DO_LATENCY listener) {
        disconnect(DO_LATENCY.class, listener);
    }

    /**
     * Available details for pipeline graphs produced by
     * {@link #debugToDotFile(int, java.lang.String, boolean)}
     */
    public static enum DebugGraphDetails implements NativeFlags<DebugGraphDetails> {

        /**
         * Show caps-name on edges.
         */
        SHOW_MEDIA_TYPE(1 << 0),
        /**
         * Show caps-details on edges.
         */
        SHOW_CAPS_DETAILS(1 << 1),
        /**
         * Show modified parameters on elements.
         */
        SHOW_NON_DEFAULT_PARAMS(1 << 2),
        /**
         * Show element states.
         */
        SHOW_STATES(1 << 3);

        /**
         * A convenience EnumSet with all values.
         */
        public final static EnumSet<DebugGraphDetails> SHOW_ALL
                = EnumSet.allOf(DebugGraphDetails.class);
        
        private final int value;

        private DebugGraphDetails(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }

    }
    
    static class Handle extends Element.Handle {
        
        public Handle(GstObjectPtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }
        
    }

}
