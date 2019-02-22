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
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.event.Event;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.message.Message;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.query.Query;
import org.freedesktop.gstreamer.event.SeekType;
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
 * @see https://cgit.freedesktop.org/gstreamer/gstreamer/tree/gst/gstelement.h?h=1.8
 */

public interface GstElementAPI extends com.sun.jna.Library {
    
    GstElementAPI GSTELEMENT_API = GstNative.load(GstElementAPI.class);

    GType gst_element_get_type();
    StateChangeReturn gst_element_set_state(Element elem, State state);
    StateChangeReturn gst_element_get_state(Element elem, State[] state, State[] pending, long timeout);
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
    void gst_element_set_base_time(Element element, long time);
    long gst_element_get_base_time(Element element);
    void gst_element_set_start_time(Element element, long time);
    long gst_element_get_start_time(Element element);
    
    /**
    * GstElement:
    * @state_lock: Used to serialize execution of gst_element_set_state()
    * @state_cond: Used to signal completion of a state change
    * @state_cookie: Used to detect concurrent execution of
    * gst_element_set_state() and gst_element_get_state()
    * @target_state: the target state of an element as set by the application
    * @current_state: the current state of an element
    * @next_state: the next state of an element, can be #GST_STATE_VOID_PENDING if
    * the element is in the correct state.
    * @pending_state: the final state the element should go to, can be
    * #GST_STATE_VOID_PENDING if the element is in the correct state
    * @last_return: the last return value of an element state change
    * @bus: the bus of the element. This bus is provided to the element by the
    * parent element or the application. A #GstPipeline has a bus of its own.
    * @clock: the clock of the element. This clock is usually provided to the
    * element by the toplevel #GstPipeline.
    * @base_time: the time of the clock right before the element is set to
    * PLAYING. Subtracting @base_time from the current clock time in the PLAYING
    * state will yield the running_time against the clock.
    * @start_time: the running_time of the last PAUSED state
    * @numpads: number of pads of the element, includes both source and sink pads.
    * @pads: (element-type Gst.Pad): list of pads
    * @numsrcpads: number of source pads of the element.
    * @srcpads: (element-type Gst.Pad): list of source pads
    * @numsinkpads: number of sink pads of the element.
    * @sinkpads: (element-type Gst.Pad): list of sink pads
    * @pads_cookie: updated whenever the a pad is added or removed
    *
    * GStreamer element abstract base class.
    */
    public static final class GstElementStruct extends com.sun.jna.Structure {
        public GstObjectStruct object;
        
        /*< public >*/ /* with LOCK */
        public volatile Pointer /* GRecMutex */ state_lock;
        
        /* element state */
        public volatile Pointer /* GCond */ state_cond;
        public volatile int state_cookie;
        public volatile State target_state;
        public volatile State current_state;
        public volatile State next_state; 
        public volatile State pending_state;         
        public volatile StateChangeReturn last_return;
        
        public volatile Pointer /* GstBus */ bus;
        
        /* allocated clock */
        public volatile Pointer /* GstClock */ clock;
        public volatile long base_time;
        public volatile long start_time;
        
        /* element pads, these lists can only be iterated while holding
        * the LOCK or checking the cookie after each LOCK. */
        public volatile short numpads;
        public volatile Pointer /* GList */pads;
        public volatile short numsrcpads;
        public volatile Pointer /* GList */ srcpads;
        public volatile short numsinkpads;
        public volatile Pointer /* GList */sinkpads;
        public volatile int pads_cookie;
        
        /* with object LOCK */
        public volatile Pointer /* GList */contexts;
        
        /*< private >*/
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
    * GstElementClass:
    * @parent_class: the parent class structure
    * @metadata: metadata for elements of this class
    * @elementfactory: the #GstElementFactory that creates these elements
    * @padtemplates: a #GList of #GstPadTemplate
    * @numpadtemplates: the number of padtemplates
    * @pad_templ_cookie: changed whenever the padtemplates change
    * @request_new_pad: called when a new pad is requested
    * @release_pad: called when a request pad is to be released
    * @get_state: get the state of the element
    * @set_state: set a new state on the element
    * @change_state: called by @set_state to perform an incremental state change
    * @set_bus: set a #GstBus on the element
    * @provide_clock: gets the #GstClock provided by the element
    * @set_clock: set the #GstClock on the element
    * @send_event: send a #GstEvent to the element
    * @query: perform a #GstQuery on the element
    * @state_changed: called immediately after a new state was set.
    * @post_message: called when a message is posted on the element. Chain up to
    *                the parent class' handler to have it posted on the bus.
    * @set_context: set a #GstContext on the element
    *
    * GStreamer element class. Override the vmethods to implement the element
    * functionality.
    */
    public static final class GstElementClass extends com.sun.jna.Structure {
        //
        // Callbacks for this class
        //
        public static interface PadAdded extends GstCallback {
            public void callback(Element element, Pad pad);
        }
        public static interface PadRemoved extends GstCallback {
            public void callback(Element element, Pad pad);
        }
        public static interface NoMorePads extends GstCallback {
            public void callback(Element element);
        }
        public static interface RequestNewPad extends GstCallback {
            public Pad callback(Element element, /* PadTemplate */ Pointer templ, 
                    String name, Caps caps);
        }
        public static interface ReleasePad extends GstCallback {
            public void callback(Element element, Pad pad);
        }
        public static interface GetState extends GstCallback {
            public StateChangeReturn callback(Element element, Pointer /* GstState */ state,
                    Pointer /* GstState */ pending, long timeout);
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
        public static interface SetBus extends GstCallback {
            public void callback(Element element, Bus bus);
        }
        public static interface ProvideClock extends GstCallback {
            public void callback(Element element);
        }
        public static interface SetClock extends GstCallback {
            public void callback(Element element, Clock clock);
        }
        public static interface SendEvent extends GstCallback {
            boolean callback(Element element, Event event);
        }
        public static interface QueryNotify extends GstCallback {
            boolean callback(Element element, Query query);
        }
        public static interface PostMessage extends GstCallback {
            boolean callback(Element element, Message message);
        }


        //
        // Actual data members
        //
        public GstObjectClass parent_class;
        
        /*< public >*/
        /* the element metadata */
        public volatile Pointer metadata;
        
        /* factory that the element was created from */
        public volatile ElementFactory elementfactory;
        
        /* templates for our pads */
        public volatile Pointer /* GList */ padtemplates;
        public volatile int numpadtemplates;
        public volatile int pad_templ_cookie;
        
        /*< private >*/
        /* signal callbacks */
        public PadAdded pad_added;
        public PadRemoved pad_removed;
        public NoMorePads no_more_pads;
        
        /* request/release pads */
        public RequestNewPad request_new_pad;
        public ReleasePad release_pad;
        
        /* state changes */
        public GetState get_state;
        public SetState set_state;
        public ChangeState change_state;
        public StateChanged state_changed;
        
        /* bus */
        public volatile SetBus set_bus;
        
        /* set/get clocks */
        public volatile ProvideClock provide_clock;
        public volatile SetClock set_clock;

        /* query functions */
        public volatile SendEvent send_event;
        public volatile QueryNotify query;
        public volatile PostMessage post_message;

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
