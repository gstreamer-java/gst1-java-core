/* 
 * Copyright (c) 2018 Antonio Morales
 * Copyright (c) 2014 Tom Greenwood <tgreenwood@cafex.com>
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007, 2008 Wayne Meissner
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

package org.freedesktop.gstreamer.lowlevel;

import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.event.Event;
import org.freedesktop.gstreamer.FlowReturn;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.PadDirection;
import org.freedesktop.gstreamer.PadLinkReturn;
import org.freedesktop.gstreamer.PadProbeReturn;
import org.freedesktop.gstreamer.PadTemplate;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GDestroyNotify;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.FreeReturnValue;
import org.freedesktop.gstreamer.lowlevel.annotations.IncRef;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

/**
 * GstPad functions
 */
public interface GstPadAPI extends com.sun.jna.Library {
	public static final int GST_PAD_PROBE_TYPE_INVALID		 	= 0;
	/* flags to control blocking */
	public static final int GST_PAD_PROBE_TYPE_IDLE 			= (1 << 0);
	public static final int GST_PAD_PROBE_TYPE_BLOCK 			= (1 << 1);
	/* flags to select datatypes */
	public static final int GST_PAD_PROBE_TYPE_BUFFER 			= (1 << 4);
	public static final int GST_PAD_PROBE_TYPE_BUFFER_LIST      = (1 << 5);
	public static final int GST_PAD_PROBE_TYPE_EVENT_DOWNSTREAM = (1 << 6);
	public static final int GST_PAD_PROBE_TYPE_EVENT_UPSTREAM   = (1 << 7);
	public static final int GST_PAD_PROBE_TYPE_EVENT_FLUSH      = (1 << 8);
	public static final int GST_PAD_PROBE_TYPE_QUERY_DOWNSTREAM = (1 << 9);
	public static final int GST_PAD_PROBE_TYPE_QUERY_UPSTREAM   = (1 << 10);
	/* flags to select scheduling mode */
	public static final int GST_PAD_PROBE_TYPE_PUSH             = (1 << 12);
	public static final int GST_PAD_PROBE_TYPE_PULL             = (1 << 13);
	public static final int GST_PAD_PROBE_TYPE_BLOCKING         = GST_PAD_PROBE_TYPE_IDLE | GST_PAD_PROBE_TYPE_BLOCK;
	public static final int GST_PAD_PROBE_TYPE_DATA_DOWNSTREAM  = GST_PAD_PROBE_TYPE_BUFFER | GST_PAD_PROBE_TYPE_BUFFER_LIST | GST_PAD_PROBE_TYPE_EVENT_DOWNSTREAM;
	public static final int GST_PAD_PROBE_TYPE_DATA_UPSTREAM    = GST_PAD_PROBE_TYPE_EVENT_UPSTREAM;
	public static final int GST_PAD_PROBE_TYPE_DATA_BOTH        = GST_PAD_PROBE_TYPE_DATA_DOWNSTREAM | GST_PAD_PROBE_TYPE_DATA_UPSTREAM;
	public static final int GST_PAD_PROBE_TYPE_BLOCK_DOWNSTREAM = GST_PAD_PROBE_TYPE_BLOCK | GST_PAD_PROBE_TYPE_DATA_DOWNSTREAM;
	public static final int GST_PAD_PROBE_TYPE_BLOCK_UPSTREAM   = GST_PAD_PROBE_TYPE_BLOCK | GST_PAD_PROBE_TYPE_DATA_UPSTREAM;
	public static final int GST_PAD_PROBE_TYPE_EVENT_BOTH       = GST_PAD_PROBE_TYPE_EVENT_DOWNSTREAM | GST_PAD_PROBE_TYPE_EVENT_UPSTREAM;		  
	public static final int GST_PAD_PROBE_TYPE_QUERY_BOTH       = GST_PAD_PROBE_TYPE_QUERY_DOWNSTREAM | GST_PAD_PROBE_TYPE_QUERY_UPSTREAM;
	public static final int GST_PAD_PROBE_TYPE_ALL_BOTH         = GST_PAD_PROBE_TYPE_DATA_BOTH | GST_PAD_PROBE_TYPE_QUERY_BOTH;
	public static final int GST_PAD_PROBE_TYPE_SCHEDULING       = GST_PAD_PROBE_TYPE_PUSH | GST_PAD_PROBE_TYPE_PULL;
	
    static GstPadAPI GSTPAD_API = GstNative.load(GstPadAPI.class);
    
    GType gst_pad_get_type();
    GType gst_ghost_pad_get_type();
    @CallerOwnsReturn Pad gst_pad_new(String name, PadDirection direction);
    @CallerOwnsReturn Pad gst_pad_new_from_template(PadTemplate templ, String name);
    @CallerOwnsReturn Pointer ptr_gst_pad_new(String name, PadDirection direction);
    @CallerOwnsReturn Pointer ptr_gst_pad_new_from_template(PadTemplate templ, String name);
    @FreeReturnValue String gst_pad_get_name(Pad pad);
    PadLinkReturn gst_pad_link(Pad src, Pad sink);
    boolean gst_pad_unlink(Pad src, Pad sink);
    boolean gst_pad_is_linked(Pad pad);
    @CallerOwnsReturn Pad gst_pad_get_peer(Pad pad);
    PadDirection gst_pad_get_direction(Pad pad);
    /* pad functions from gstutils.h */
    boolean gst_pad_can_link(Pad srcpad, Pad sinkpad);

    void gst_pad_use_fixed_caps(Pad pad);
    @CallerOwnsReturn Caps gst_pad_get_fixed_caps_func(Pad pad);
    @CallerOwnsReturn Caps gst_pad_proxy_getcaps(Pad pad);
    boolean gst_pad_proxy_setcaps(Pad pad, Caps caps);
    @CallerOwnsReturn Element gst_pad_get_parent_element(Pad pad);
    

    boolean gst_pad_set_active(Pad pad, boolean active);
    boolean gst_pad_is_active(Pad pad);
    boolean gst_pad_activate_pull(Pad pad, boolean active);
    boolean gst_pad_activate_push(Pad pad, boolean active);
    boolean gst_pad_is_blocked(Pad pad);
    boolean gst_pad_is_blocking(Pad pad);
    boolean gst_pad_has_current_caps(Pad pad);
    /* get_pad_template returns a non-refcounted PadTemplate */
    PadTemplate gst_pad_get_pad_template(Pad pad);
    
    /* capsnego function for connected/unconnected pads */
    @CallerOwnsReturn Caps gst_pad_query_caps(Pad pad, Caps caps);
    void gst_pad_fixate_caps(Pad pad, Caps caps);
    boolean gst_pad_query_accept_caps(Pad pad, Caps caps);
//    boolean gst_pad_set_caps(Pad pad, Caps caps);
    @CallerOwnsReturn Caps gst_pad_peer_query_caps(Pad pad, Caps caps);
    boolean gst_pad_peer_query_accept_caps(Pad pad, Caps caps);
    
    /* capsnego for connected pads */
    @CallerOwnsReturn Caps gst_pad_get_allowed_caps(Pad pad);
    @CallerOwnsReturn Caps gst_pad_get_current_caps(Pad pad);

    /* data passing functions to peer */
    FlowReturn gst_pad_push(Pad pad, @IncRef Buffer buffer);
    boolean gst_pad_check_pull_range(Pad pad);
    FlowReturn gst_pad_pull_range(Pad pad, /* guint64 */ long offset, /* guint */ int size,
            Buffer[] buffer);
    boolean gst_pad_push_event(Pad pad, @IncRef Event event);
    boolean gst_pad_event_default(Pad pad, Event event);
    /* data passing functions on pad */
    FlowReturn gst_pad_chain(Pad pad, @IncRef Buffer buffer);
    FlowReturn gst_pad_get_range(Pad pad, /* guint64 */ long offset, /* guint */ int size,
        Buffer[] buffer);
    boolean gst_pad_send_event(Pad pad, @IncRef Event event);
    public static interface PadFixateCaps extends GstCallback {
        void callback(Pad pad, Caps caps);
    }
    void gst_pad_set_fixatecaps_function(Pad pad, PadFixateCaps fixate);
    
    /* probes */
//    public static interface PadDataProbe extends GstCallback {
//        void callback(Pad pad, Buffer buffer, Pointer user_data);
//    }
//    public static interface PadEventProbe extends GstCallback {
//        boolean callback(Pad pad, Event ev, Pointer user_data);
//    }
    
    public static interface PadProbeCallback extends GstCallback {
        public PadProbeReturn callback(Pad pad, GstPadProbeInfo probeInfo, Pointer user_data);
    }
    
    /**
     * 
     * @param pad
     * @param mask The type comes from the constants in GstPadProbeType
     * @param callback
     * @param user_data
     * @param destroy_data
     * @return
     */
    NativeLong gst_pad_add_probe(Pad pad, int mask, PadProbeCallback callback, 
    		Pointer user_data, GDestroyNotify destroy_data);
    void gst_pad_remove_probe(Pad pad, NativeLong id);
    
    Event gst_pad_probe_info_get_event(GstPadProbeInfo probeInfo);

    Buffer gst_pad_probe_info_get_buffer(GstPadProbeInfo probeInfo);

//    NativeLong /* gulong */ gst_pad_add_data_probe(Pad pad, PadDataProbe handler, Pointer data);
//
//    void gst_pad_remove_data_probe(Pad pad, NativeLong handler_id);
//
//    NativeLong /* gulong */ gst_pad_add_event_probe(Pad pad, PadEventProbe handler, Pointer data);
//
//    void gst_pad_remove_event_probe(Pad pad, NativeLong handler_id);
//
//    NativeLong /* gulong */ gst_pad_add_buffer_probe(Pad pad, GstCallback handler, Pointer data);
//
//    void gst_pad_remove_buffer_probe(Pad pad, NativeLong handler_id);
}
