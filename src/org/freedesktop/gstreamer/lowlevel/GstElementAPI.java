/* 
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


import java.util.Arrays;
import java.util.List;

import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Clock;
import org.freedesktop.gstreamer.ClockTime;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.Event;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.Message;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.Query;
import org.freedesktop.gstreamer.SeekType;
import org.freedesktop.gstreamer.State;
import org.freedesktop.gstreamer.StateChangeReturn;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;
import org.freedesktop.gstreamer.lowlevel.GstObjectAPI.GstObjectClass;
import org.freedesktop.gstreamer.lowlevel.GstObjectAPI.GstObjectStruct;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.IncRef;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

/**
 * GstElement methods and structures
 * @see https://github.com/GStreamer/gstreamer/blob/master/gst/gstelement.h
 */

public interface GstElementAPI extends com.sun.jna.Library {
    GstElementAPI GSTELEMENT_API = GstNative.load(GstElementAPI.class);

    GType gst_element_get_type();
    StateChangeReturn gst_element_set_state(Element elem, State state);
    StateChangeReturn gst_element_get_state(Element elem, State[] state, State[] pending, long timeout);
    StateChangeReturn gst_element_get_state(Element elem, State[] state, State[] pending, ClockTime timeout);
    boolean gst_element_set_locked_state(Element element, boolean locked_state);
    boolean gst_element_sync_state_with_parent(Element elem);
    boolean gst_element_query_position(Element elem, Format fmt, long[] pos);
    boolean gst_element_query_duration(Element elem, Format fmt, long[] pos);
    boolean gst_element_query(Element elem, Query query);
    boolean gst_element_seek(Element elem, double rate, Format format, int flags,
            SeekType cur_type, long cur, SeekType stop_type, long stop);
    boolean gst_element_seek_simple(Element elem, Format format, int flags, long pos);
    boolean gst_element_link(Element elem1, Element elem2);
    boolean gst_element_link_many(Element... elements);
    void gst_element_unlink_many(Element... elements);
    void gst_element_unlink(Element elem1, Element elem2);
    @CallerOwnsReturn Pad gst_element_get_pad(Element elem, String name);
    @CallerOwnsReturn Pad gst_element_get_static_pad(Element element, String name);
    // pads returned from get_request have to be freed via release_request_pad
    Pad gst_element_get_request_pad(Element element, String name);
    void gst_element_release_request_pad(Element element, Pad pad);
    boolean gst_element_add_pad(Element elem, Pad pad);
    boolean gst_element_remove_pad(Element elem, @IncRef Pad pad);
    boolean gst_element_link_pads(Element src, String srcpadname, Element dest, String destpadname);
    void gst_element_unlink_pads(Element src, String srcpadname, Element dest, String destpadname);
    boolean gst_element_link_pads_filtered(Element src, String srcpadname, Element dest, String destpadname,
            Caps filter);
    
    Pointer gst_element_iterate_pads(Element element);
    Pointer gst_element_iterate_src_pads(Element element);
    Pointer gst_element_iterate_sink_pads(Element element);
    /* factory management */
    ElementFactory gst_element_get_factory(Element element);
    @CallerOwnsReturn Bus gst_element_get_bus(Element element);
    boolean gst_element_send_event(Element element, @IncRef Event event);
    boolean gst_element_post_message(Element element, @IncRef Message message);

    boolean gst_element_implements_interface(Element element, NativeLong iface_type);
    /* clocking */
    Clock gst_element_get_clock(Element element);
    boolean gst_element_set_clock(Element element, Clock clock);
    void gst_element_set_base_time(Element element, ClockTime time);
    ClockTime gst_element_get_base_time(Element element);
    void gst_element_set_start_time(Element element, ClockTime time);
    ClockTime gst_element_get_start_time(Element element);
    
    public static final class GstElementDetails extends com.sun.jna.Structure {
         /*< public > */
        public volatile String longname;
        public volatile String klass;
        public volatile String description;
        public volatile String author;
        /*< private > */
        public volatile Pointer[] _gst_reserved = new Pointer[GstAPI.GST_PADDING];        

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "longname", "klass", "description",
                "author", "_gst_reserved"
            });
        }
    }
    
    public static final class GstElementStruct extends com.sun.jna.Structure {
        public GstObjectStruct object;
        public volatile Pointer state_lock;
        public volatile Pointer state_cond;
        public volatile int state_cookie;
        public volatile State target_state;
        public volatile State current_state;
        public volatile State next_state; 
        public volatile State pending_state;         
        public volatile StateChangeReturn last_return;
        public volatile Pointer bus;
        public volatile Pointer clock;
        public volatile long base_time;
        public volatile long start_time;
        public volatile short numpads;
        public volatile Pointer pads;
        public volatile short numsrcpads;
        public volatile Pointer srcpads;
        public volatile short numsinkpads;
        public volatile Pointer sinkpads;
        public volatile int pads_cookie;
        public volatile Pointer contexts;
        
        // Use an array of byte as arrays of Pointer don't work
        public volatile Pointer[] _gst_reserved = new Pointer[GstAPI.GST_PADDING-1];

        public GstElementStruct(Pointer handle) {
            super(handle);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "object", "state_lock", "state_cond",
                "state_cookie", "target_state", "current_state", "next_state",
                "pending_state", "last_return", "bus",
                "clock", "base_time", "start_time", "numpads",
                "pads", "numsrcpads", "srcpads",
                "numsinkpads", "sinkpads", "pads_cookie", "contexts",
                "_gst_reserved"
            });
        }
    }
    
    /**
     * @see https://github.com/GStreamer/gstreamer/blob/master/gst/gstelement.h
     */
    public static final class GstElementClass extends com.sun.jna.Structure {
        //
        // Callbacks for this class
        //
        public static interface RequestNewPad extends GstCallback {
            public Pad callback(Element element, /* PadTemplate */ Pointer templ, String name);
        }
        public static interface ReleasePad extends GstCallback {
            public void callback(Element element, Pad pad);
        }
        public static interface GetState extends GstCallback {
            public StateChangeReturn callback(Element element, Pointer p_state,
                    Pointer p_pending, long timeout);
        }
        public static interface SetState extends GstCallback {
            public StateChangeReturn callback(Element element, State state);
        }
        public static interface ChangeState extends GstCallback {
            public StateChangeReturn callback(Element element, int transition);
        }
        public static interface StateChanged extends GstCallback {
            public StateChangeReturn callback(Element element, State oldState, State newState, State pending);
        }
        public static interface QueryNotify extends GstCallback {
            boolean callback(Element element, Query query);
        }


        //
        // Actual data members
        //
        public GstObjectClass parent_class;
        /* the element metadata */
        public volatile Pointer metadata;
        /* factory that the element was created from */
        public volatile ElementFactory elementfactory;
        /* templates for our pads */
        public volatile Pointer padtemplates;
        public volatile int numpadtemplates;
        public volatile int pad_templ_cookie;
        /*< private >*/
        /* signal callbacks */
        public volatile Pointer pad_added;
        public volatile Pointer pad_removed;
        public volatile Pointer no_more_pads;
        /* request/release pads */
        public RequestNewPad request_new_pad;
        public ReleasePad release_pad;
        /* state changes */
        public GetState get_state;
        public SetState set_state;
        public ChangeState change_state;
        public StateChanged state_changed;
        /* bus */
        public volatile Pointer set_bus;
        /* set/get clocks */
        public volatile Pointer provide_clock;
        public volatile Pointer set_clock;

        /* query functions */
        public volatile Pointer send_event;
        public volatile QueryNotify query;
        public volatile Pointer post_message;

        public volatile Pointer set_context;

        /*< private >*/
        // Use an array of byte if arrays of Pointer don't work
        public volatile Pointer[] _gst_reserved = new Pointer[GstAPI.GST_PADDING_LARGE-2];

        public GstElementClass(Pointer ptr) {
            super(ptr);
            //this.read();
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_class", "metadata", "elementfactory",
                "padtemplates", "numpadtemplates", "pad_templ_cookie",
                "pad_added", "pad_removed", "no_more_pads",
                "request_new_pad", "release_pad", "get_state",
                "set_state", "change_state", "state_changed", "set_bus",
                "provide_clock", "set_clock",
                "send_event", "query", "post_message",
                "set_context",
                "_gst_reserved"
            });
        }

    }
}
