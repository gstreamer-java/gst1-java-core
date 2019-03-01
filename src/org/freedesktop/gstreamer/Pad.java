/* 
 * Copyright (C) 2019 Neil C Smith
 * Copyright (C) 2018 Antonio Morales
 * Copyright (C) 2014 Tom Greenwood <tgreenwood@cafex.com>
 * Copyright (C) 2009 Tamas Korodi <kotyo@zamba.fm>
 * Copyright (C) 2007 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wtay@chello.be>
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

import org.freedesktop.gstreamer.event.Event;
import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.glib.Natives;

import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;
import org.freedesktop.gstreamer.lowlevel.GstPadProbeInfo;
import org.freedesktop.gstreamer.lowlevel.GstPadAPI;

import static org.freedesktop.gstreamer.lowlevel.GstPadAPI.GSTPAD_API;

/**
 * Object contained by elements that allows links to other elements.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPad.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPad.html</a>
 * <p>
 * An {@link Element} is linked to other elements via "pads", which are extremely
 * light-weight generic link points. After two pads are retrieved from an
 * element with {@link Element#getPad}, the pads can be link with {@link #link}.
 * (For quick links, you can also use {@link Element#link}, which will make the
 * obvious link for you if it's straightforward.)
 * <p>
 * Pads are typically created from a {@link PadTemplate} with
 * {@link #Pad(PadTemplate, String)}.
 * <p>
 * Pads have {@link Caps} attached to it to describe the media type they are
 * capable of dealing with. {@link #queryCaps} and {@link #setCaps} are used to
 * manipulate the caps of the pads. Pads created from a pad template cannot set
 * capabilities that are incompatible with the pad template capabilities.
 * <p>
 * Pads without pad templates can be created with gst_pad_new(), which takes a
 * direction and a name as an argument. If the name is NULL, then a guaranteed
 * unique name will be assigned to it.
 * <p>
 * {@link #getParentElement} will retrieve the Element that owns the pad.
 * <p>
 * An Element creating a pad will typically use the various
 * gst_pad_set_*_function() calls to register callbacks for various events on
 * the pads.
 * <p>
 * GstElements will use gst_pad_push() and gst_pad_pull_range() to push out or
 * pull in a buffer.
 * <p>
 * To send an Event on a pad, use {@link #sendEvent} and {@link #pushEvent}.
 *
 * @see PadTemplate
 * @see Element
 * @see Event
 */
public class Pad extends GstObject {

    public static final String GTYPE_NAME = "GstPad";

    /**
     * Creates a new instance of Pad
     */
    Pad(Initializer init) {
        super(init);
    }

    /**
     * Creates a new pad with the given name in the given direction. If name is
     * null, a guaranteed unique name (across all pads) will be assigned.
     *
     * @param name The name of the new pad.
     * @param direction The direction of the new pad.
     */
    public Pad(String name, PadDirection direction) {
        this(Natives.initializer(GSTPAD_API.ptr_gst_pad_new(name, direction), false));
    }

    /**
     * Creates a new pad with the given name from the given template.
     *
     * If name is null, a guaranteed unique name (across all pads) will be
     * assigned.
     *
     * @param template The pad template to use.
     * @param name The name of the new pad.
     */
    public Pad(PadTemplate template, String name) {
        this(Natives.initializer(GSTPAD_API.ptr_gst_pad_new_from_template(template, name), false));
    }

    /**
     * Gets the capabilities this pad can produce or consume. Note that this
     * method doesn't necessarily return the caps set by sending a
     * gst_event_new_caps() - use {@link #getCurrentCaps() } for that instead.
     * queryCaps returns all possible caps a pad can operate with,
     * using the pad's CAPS query function, If the query fails, this function
     * will return filter, if not NULL, otherwise ANY.
     * <p>
     * When called on sinkpads filter contains the caps that upstream could
     * produce in the order preferred by upstream. When called on srcpads filter
     * contains the caps accepted by downstream in the preferred order. filter
     * might be NULL but if it is not NULL the returned caps will be a subset of
     * filter .
     * <p>
     * Note that this function does not return writable Caps.
     *
     * @param filter suggested Caps or null
     * @return a newly allocated copy of the {@link Caps} of this pad.
     */
    public Caps queryCaps(Caps filter) {
        return GSTPAD_API.gst_pad_query_caps(this, filter);
    }

    /**
     * Gets the capabilities of the allowed media types that can flow through
     * this pad and its peer.
     * <p>
     * The allowed capabilities is calculated as the intersection of the results
     * of calling {@link #queryCaps} on this pad and its peer.
     * <p>
     * MT safe.
     *
     * @return The allowed {@link Caps} of the pad link, or null if this pad has
     * no peer.
     */
    public Caps getAllowedCaps() {
        return GSTPAD_API.gst_pad_get_allowed_caps(this);
    }

    /**
     * Gets the capabilities currently configured on pad with the last
     * GST_EVENT_CAPS event.
     *
     * @return the negotiated #GstCaps or null if this pad has
     * no current caps
     *
     */
    public Caps getCurrentCaps() {
        return GSTPAD_API.gst_pad_get_current_caps(this);
    }

    /**
     * Get the peer of this pad.
     *
     * MT safe.
     *
     * @return The peer Pad of this Pad.
     */
    public Pad getPeer() {
        return GSTPAD_API.gst_pad_get_peer(this);
    }

    /**
     * Get the capabilities of the peer connected to this pad.
     *<p>
     * When called on srcpads filter contains the caps that upstream could
     * produce in the order preferred by upstream. When called on sinkpads
     * filter contains the caps accepted by downstream in the preferred order.
     * filter might be NULL but if it is not NULL the returned caps will be a
     * subset of filter .
     * 
     * @param filter Caps to filter by, or null
     * @return the {@link Caps} of the peer pad, or null if there is no peer
     * pad.
     */
    public Caps peerQueryCaps(Caps filter) {
        return GSTPAD_API.gst_pad_peer_query_caps(this, filter);
    }

    /**
     * Check if the pad accepts the given caps.
     *
     * @param caps a {@link Caps} to check on the pad.
     * @return true if the pad can accept the caps.
     */
    public boolean queryAcceptCaps(Caps caps) {
        return GSTPAD_API.gst_pad_query_accept_caps(this, caps);
    }

    /**
     * Check if the peer of this pad accepts the caps. If this pad has no peer,
     * this method returns true.
     *
     * @param caps {@link Caps} to check on the pad
     * @return true if the peer pad can accept the caps or this pad no peer.
     */
    public boolean peerQueryAcceptCaps(Caps caps) {
        return GSTPAD_API.gst_pad_peer_query_accept_caps(this, caps);
    }

    /**
     * Links this source pad and a sink pad.
     *
     * MT Safe.
     *
     * @param sink the sink Pad to link.
     * @throws PadLinkException if pads cannot be linked.
     */
    public void link(Pad sink) throws PadLinkException {
        PadLinkReturn result = GSTPAD_API.gst_pad_link(this, sink);
        if (result != PadLinkReturn.OK) {
            throw new PadLinkException(result);
        }
    }

    /**
     *
     * Unlinks the source pad from the sink pad. Will emit the "unlinked" signal
     * on both pads.
     *
     * MT safe.
     *
     * @param pad the sink Pad to unlink.
     * @return true if the pads were unlinked. This function returns false if
     * the pads were not linked together.
     */
    public boolean unlink(Pad pad) {
        return GSTPAD_API.gst_pad_unlink(this, pad);
    }

    /**
     * Check if this pad is linked to another pad or not.
     *
     * @return true if the pad is linked, else false.
     */
    public boolean isLinked() {
        return GSTPAD_API.gst_pad_is_linked(this);
    }

    /**
     * Get the direction of the pad. The direction of the pad is decided at
     * construction time so this function does not take the LOCK.
     *
     * @return The {@link PadDirection} of the pad.
     */
    public PadDirection getDirection() {
        return GSTPAD_API.gst_pad_get_direction(this);
    }

    /**
     * Get the parent of this pad, cast to an {@link Element}. If this pad has no
     * parent or its parent is not an element, returns null.
     *
     * @return The parent of the pad.
     */
    public Element getParentElement() {
        return GSTPAD_API.gst_pad_get_parent_element(this);
    }

    /**
     * Activates or deactivates the given pad. Normally called from within core
     * state change functions.
     * <p>
     * If active is true, makes sure the pad is active. If it is already active,
     * either in push or pull mode, just return. Otherwise dispatches to the
     * pad's activate function to perform the actual activation.
     * <p>
     * If not @active, checks the pad's current mode and calls
     * gst_pad_activate_push() or gst_pad_activate_pull(), as appropriate, with
     * a FALSE argument.
     *
     * @param active whether or not the pad should be active.
     * @return true if the operation was successful.
     */
    public boolean setActive(boolean active) {
        return GSTPAD_API.gst_pad_set_active(this, active);
    }

    /**
     * Checks if the pad is blocked or not. This function returns the last
     * requested state of the pad. It is not certain that the pad is actually
     * blocking at this point (see {@link #isBlocking}).
     *
     * @return true if the pad is blocked.
     */
    public boolean isBlocked() {
        return GSTPAD_API.gst_pad_is_blocked(this);
    }

    /**
     * Run a runnable under a blocked state
     *
     * @param callback The code to run when pad is blocked
     */
    public void block(final Runnable callback) {
        addEventProbe(new EVENT_PROBE() {
            public PadProbeReturn eventReceived(Pad pad, Event event) {
                callback.run();
                pad.removeCallback(EVENT_PROBE.class, this);
                return PadProbeReturn.DROP;
            }
        }, GstPadAPI.GST_PAD_PROBE_TYPE_BLOCKING | GstPadAPI.GST_PAD_PROBE_TYPE_EVENT_DOWNSTREAM);
    }

    /**
     * Checks if the pad is blocking or not. This is a guaranteed state of
     * whether the pad is actually blocking on a {@link Buffer} or an
     * {@link Event}.
     *
     * @return true if the pad is blocking.
     */
    public boolean isBlocking() {
        return GSTPAD_API.gst_pad_is_blocking(this);
    }

    /**
     * Add a listener for the <code>linked</code> signal on this {@link Pad}
     *
     * @param listener The listener to be called when a peer {@link Pad} is
     * linked.
     */
    public void connect(final LINKED listener) {
        connect(LINKED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(Pad pad, Pad peer) {
                listener.linked(pad, peer);
                return true;
            }
        });
    }

    /**
     * Remove a listener for the <code>linked</code> signal on this {@link Pad}
     *
     * @param listener The listener previously added for this signal.
     */
    public void disconnect(LINKED listener) {
        disconnect(LINKED.class, listener);
    }

    /**
     * Add a listener for the <code>unlinked</code> signal on this {@link Pad}
     *
     * @param listener The listener to be called when when a peer {@link Pad} is
     * unlinked.
     */
    public void connect(final UNLINKED listener) {
        connect(UNLINKED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(Pad pad, Pad peer) {
                listener.unlinked(pad, peer);
                return true;
            }
        });
    }

    /**
     * Remove a listener for the <code>unlinked</code> signal on this
     * {@link Pad}
     *
     * @param listener The listener previously added for this signal.
     */
    public void disconnect(UNLINKED listener) {
        disconnect(UNLINKED.class, listener);
    }

    public void addEventProbe(final EVENT_PROBE listener) {
        final int mask = GstPadAPI.GST_PAD_PROBE_TYPE_EVENT_BOTH | GstPadAPI.GST_PAD_PROBE_TYPE_EVENT_FLUSH;
        addEventProbe(listener, mask);
    }

    public void addEventProbe(final EVENT_PROBE listener, final int mask) {
        final GstPadAPI.PadProbeCallback probe = new GstPadAPI.PadProbeCallback() {
            public PadProbeReturn callback(Pad pad, GstPadProbeInfo probeInfo, Pointer user_data) {
//        	    System.out.println("CALLBACK " + probeInfo.padProbeType);
                if ((probeInfo.padProbeType & mask) != 0) {
                    Event event = GSTPAD_API.gst_pad_probe_info_get_event(probeInfo);
                    return listener.eventReceived(pad, event);
                }

                //We have to negate the return value to keep consistency with gstreamer's API
                return PadProbeReturn.OK;
            }
        };

        GCallback cb = new GCallback(GSTPAD_API.gst_pad_add_probe(this, mask, probe, null, null), probe) {
            @Override
            protected void disconnect() {
                GSTPAD_API.gst_pad_remove_probe(Pad.this, id);
            }
        };
        addCallback(EVENT_PROBE.class, listener, cb);
    }

    public void removeEventProbe(EVENT_PROBE listener) {
        removeCallback(EVENT_PROBE.class, listener);
    }

    public synchronized void addDataProbe(final DATA_PROBE listener) {

        final GstPadAPI.PadProbeCallback probe = new GstPadAPI.PadProbeCallback() {
            public PadProbeReturn callback(Pad pad, GstPadProbeInfo probeInfo, Pointer user_data) {
                if ((probeInfo.padProbeType & GstPadAPI.GST_PAD_PROBE_TYPE_BUFFER) != 0) {
                    Buffer buffer = GSTPAD_API.gst_pad_probe_info_get_buffer(probeInfo);
                    return listener.dataReceived(pad, buffer);
                }

                //We have to negate the return value to keep consistency with gstreamer's API
                return PadProbeReturn.OK;
            }
        };

        GCallback cb = new GCallback(GSTPAD_API.gst_pad_add_probe(this, GstPadAPI.GST_PAD_PROBE_TYPE_BUFFER, probe, null, null), probe) {
            @Override
            protected void disconnect() {
                GSTPAD_API.gst_pad_remove_probe(Pad.this, id);
            }
        };

        addCallback(DATA_PROBE.class, listener, cb);
    }

    public void removeDataProbe(DATA_PROBE listener) {
        removeCallback(DATA_PROBE.class, listener);
    }

    /**
     * Sends the event to this pad.
     * <p>
     * This function can be used by applications to send events in the pipeline.
     *
     * <p>
     * If this pad is a source pad, <tt>event</tt> should be an upstream event.
     * If this pad is a sink pad, <tt>event<tt> should be a downstream event.
     * <p>
     * For example, you would not send a {@link EventType#EOS} on a src pad; EOS
     * events only propagate downstream.
     *
     * <p>
     * Furthermore, some downstream events have to be serialized with data flow,
     * like EOS, while some can travel out-of-band, like
     * {@link EventType#FLUSH_START}. If the event needs to be serialized with
     * data flow, this function will take the pad's stream lock while calling
     * its event function.
     *
     * @param event the event to send.
     * @return <tt>true</tt> if the event was handled.
     */
    public boolean sendEvent(Event event) {
        return GSTPAD_API.gst_pad_send_event(this, event);
    }

    /**
     * Sends the event to the peer of this pad.
     *
     * <p>
     * This function is mainly used by elements to send events to their peer
     * elements.
     *
     * @param event the event to send
     * @return true if the event was handled
     */
    public boolean pushEvent(Event event) {
        return GSTPAD_API.gst_pad_push_event(this, event);
    }

    /**
     * Chain a buffer to pad.
     * <p>
     * The function returns {@link FlowReturn#FLUSHING} if the
     * pad was flushing.
     * <p>
     * If the caps on buffer are different from the current caps on pad, this
     * function will call any function installed on pad (see
     * gst_pad_set_setcaps_function()). If the new caps are not acceptable for
     * pad, this function returns
     * {@link FlowReturn#NOT_NEGOTIATED}.
     * <p>
     * The function proceeds calling the chain function installed on pad and the
     * return value of that function is returned to the caller.
     * {@link FlowReturn#NOT_SUPPORTED} is returned if pad has no
     * chain function.
     * <p>
     * In all cases, success or failure, the caller loses its reference to
     * buffer after calling this function.
     *
     * @param buffer the Buffer, returns {@link FlowReturn#ERROR}
     * if NULL.
     * @return a org.gstreamer.FlowReturn
     */
    public FlowReturn chain(Buffer buffer) {
        return GSTPAD_API.gst_pad_chain(this, buffer);
    }

    /**
     * When pad is flushing this function returns
     * {@link FlowReturn#FLUSHING} immediately.
     * <p>
     * Calls the getRange function of pad, see GstPadGetRangeFunction for a
     * description of a getRange function. If pad has no getRange function
     * installed (see gst_pad_set_getrange_function()) this function returns
     * {@link FlowReturn#NOT_SUPPORTED}.
     * <p>
     * This is a lowlevel function. Usualy {@link Pad#pullRange} is used.
     *
     * @param offset The start offset of the buffer
     * @param size The length of the buffer
     * @param buffer the Buffer, returns {@link FlowReturn#ERROR} if NULL.
     * @return a FlowReturn from the peer pad. When this function returns OK,
     * buffer will contain a valid Buffer.
     */
    public FlowReturn getRange(long offset, int size, Buffer[] buffer) {
        return GSTPAD_API.gst_pad_get_range(this, offset, size, buffer);
    }

    /**
     * Pulls a buffer from the peer pad.
     * <p>
     * This function will first trigger the pad block signal if it was
     * installed.
     * <p>
     * When pad is not linked {@link FlowReturn#NOT_LINKED} is returned else
     * this function returns the result of {@link Pad#getRange} on the peer pad.
     * See {@link Pad#getRange} for a list of return values and for the
     * semantics of the arguments of this function.
     * <p>
     * buffer's caps must either be unset or the same as what is already
     * configured on pad. Renegotiation within a running pull-mode pipeline is
     * not supported.
     *
     * @param offset The start offset of the buffer
     * @param size The length of the buffer
     * @param buffer the Buffer, returns {@link FlowReturn#ERROR} if NULL.
     * @return a FlowReturn from the peer pad. When this function returns OK,
     * buffer will contain a valid Buffer. MT safe.
     */
    public FlowReturn pullRange(long offset, int size, Buffer[] buffer) {
        return GSTPAD_API.gst_pad_pull_range(this, offset, size, buffer);
    }

    /**
     * Pushes a buffer to the peer of pad . This function will call installed
     * block probes before triggering any installed data probes.
     * <p>
     * The function proceeds calling gst_pad_chain() on the peer pad and returns
     * the value from that function. If pad has no peer, {@link FlowReturn#NOT_LINKED}
     * will be returned.
     * <p>
     * In all cases, success or failure, the caller loses its reference to
     * buffer after calling this function.
     *
     * @param buffer the GstBuffer to push returns GST_FLOW_ERROR if not.
     * [transfer full]
     * @return a GstFlowReturn from the peer pad.
     *
     * MT safe.
     */
    public FlowReturn push(final Buffer buffer) {
        return GSTPAD_API.gst_pad_push(this, buffer);
    }

    /**
     * Gets the template for pad.
     *
     * @return the GstPadTemplate from which this pad was instantiated, or NULL
     * if this pad has no template.
     */
    public PadTemplate getTemplate() {
        return GSTPAD_API.gst_pad_get_pad_template(this);
    }

    /**
     * Check if the pad has caps set on it with a GST_EVENT_CAPS events
     *
     * @return true if the pad has caps set
     */
    public boolean hasCurrentCaps() {
        return GSTPAD_API.gst_pad_has_current_caps(this);
    }
    

    /**
     * Signal emitted when new this {@link Pad} is linked to another {@link Pad}
     *
     * @see #connect(LINKED)
     * @see #disconnect(LINKED)
     */
    public static interface LINKED {

        /**
         * Called when a {@link Pad} is linked to another Pad.
         *
         * @param pad the pad that emitted the signal.
         * @param peer the peer pad that has been connected.
         */
        public void linked(Pad pad, Pad peer);
    }

    /**
     * Signal emitted when new this {@link Pad} is disconnected from a peer
     * {@link Pad}
     *
     * @see #connect(UNLINKED)
     * @see #disconnect(UNLINKED)
     */
    public static interface UNLINKED {

        /**
         * Called when a {@link Pad} is unlinked from another Pad.
         *
         * @param pad the pad that emitted the signal.
         * @param peer the peer pad that has been connected.
         */
        public void unlinked(Pad pad, Pad peer);
    }

    /**
     * Signal emitted when an event passes through this <tt>Pad</tt>.
     *
     * @see #addEventProbe(EVENT_PROBE)
     * @see #removeEventProbe(EVENT_PROBE)
     */
    public static interface EVENT_PROBE {

        public PadProbeReturn eventReceived(Pad pad, Event event);
    }

    /**
     * Signal emitted when new data is available on the {@link Pad}
     *
     * @see #addDataProbe(DATA_PROBE)
     * @see #removeDataProbe(DATA_PROBE)
     */
    public static interface DATA_PROBE {

        public PadProbeReturn dataReceived(Pad pad, Buffer buffer);
    }
}
