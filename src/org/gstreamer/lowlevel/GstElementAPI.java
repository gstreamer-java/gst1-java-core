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

package org.gstreamer.lowlevel;


import org.gstreamer.Bus;
import org.gstreamer.Caps;
import org.gstreamer.Clock;
import org.gstreamer.ClockTime;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Event;
import org.gstreamer.Format;
import org.gstreamer.Message;
import org.gstreamer.Pad;
import org.gstreamer.Query;
import org.gstreamer.SeekType;
import org.gstreamer.State;
import org.gstreamer.StateChangeReturn;
import org.gstreamer.lowlevel.GstAPI.GstCallback;
import org.gstreamer.lowlevel.GstObjectAPI.GstObjectClass;
import org.gstreamer.lowlevel.GstObjectAPI.GstObjectStruct;
import org.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.gstreamer.lowlevel.annotations.IncRef;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.List;

/**
 * GstElement methods
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
    boolean gst_element_query_duration(Element elem, Format[] fmt, long[] pos);
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
        public volatile State current_state;
        public volatile State next_state; 
        public volatile State pending_state;         
        public volatile StateChangeReturn last_return;
        public volatile Pointer bus;
        public volatile Pointer clock;
        public volatile long base_time;
        public volatile short numpads;
        public volatile Pointer pads;
        public volatile short numsrcpads;
        public volatile Pointer srcpads;
        public volatile short numsinkpads;
        public volatile Pointer sinkpads;
        public volatile int pads_cookie;
        // Use an array of byte as arrays of Pointer don't work
        public volatile Pointer[] _gst_reserved = new Pointer[GstAPI.GST_PADDING];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "object", "state_lock", "state_cond",
                "state_cookie", "current_state", "next_state",
                "pending_state", "last_return", "bus",
                "clock", "base_time", "numpads",
                "pads", "numsrcpads", "srcpads",
                "numsinkpads", "sinkpads", "pads_cookie",
                "_gst_reserved"
            });
        }
    }
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
        //
        // Actual data members
        //
        public GstObjectClass parent_class;
        public volatile GstElementDetails details;
        public volatile ElementFactory elementfactory;
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
        /* bus */
        public volatile Pointer set_bus;
        /* set/get clocks */
        public volatile Pointer provide_clock;
        public volatile Pointer set_clock;
        
        /* index */
        public volatile Pointer get_index;
        public volatile Pointer set_index;
        public volatile Pointer send_event;
        /* query functions */
        public volatile Pointer get_query_types;
        public volatile Pointer query;
      
        /*< private >*/  
        // Use an array of byte if arrays of Pointer don't work
        public volatile Pointer[] _gst_reserved = new Pointer[GstAPI.GST_PADDING];
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_class", "details", "elementfactory",
                "padtemplates", "numpadtemplates", "pad_templ_cookie",
                "pad_added", "pad_removed", "no_more_pads",
                "request_new_pad", "release_pad", "get_state",
                "set_state", "change_state", "set_bus",
                "provide_clock", "set_clock", "get_index",
                "set_index", "send_event", "get_query_types",
                "query", "_gst_reserved"
            });       
        }
        
    }
}
