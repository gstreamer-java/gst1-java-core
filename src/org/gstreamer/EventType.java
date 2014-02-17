/* 
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wim.taymans@chello.be>
 *                    2005 Wim Taymans <wim@fluendo.com>
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

package org.gstreamer;

import org.gstreamer.lowlevel.EnumMapper;
import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.IntegerEnum;
import org.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * The standard event types that can be sent in a pipeline.
 * <p>
 * The custom event types can be used for private messages between elements
 * that can't be expressed using normal GStreamer buffer passing semantics. 
 * <p>
 * Custom events carry an arbitrary {@link Structure}.  Specific custom events 
 * are distinguished by the name of the structure.
 */
public enum EventType implements IntegerEnum {
    /** Unknown event */
    @DefaultEnumValue UNKNOWN(0, 0),
    /** Start a flush operation */
    FLUSH_START(1, Flags.BOTH),
    /** Stop a flush operation */
    FLUSH_STOP(2, Flags.BOTH | Flags.SERIALIZED),
    /**
     * End-Of-Stream. No more data is to be expected to follow without a 
     * NEWSEGMENT event.
     */
    EOS(5, Flags.DOWNSTREAM | Flags.SERIALIZED),
    /** A new media segment follows in the dataflow */
    NEWSEGMENT(6, Flags.DOWNSTREAM | Flags.SERIALIZED),
    /** A new set of metadata tags has been found in the stream */
    TAG(7, Flags.DOWNSTREAM | Flags.SERIALIZED),
    /** Notification of buffering requirements */
    BUFFERSIZE(8, Flags.DOWNSTREAM | Flags.SERIALIZED),
    /** 
     * A quality message. Used to indicate to upstream elements that the 
     * downstream elements are being starved of or flooded with data.
     */
    QOS(15, Flags.UPSTREAM),
    /** A request for a new playback position and rate. */
    SEEK(16, Flags.UPSTREAM),
    /**
     * Navigation events are usually used for communicating user requests, such 
     * as mouse or keyboard movements, to upstream elements.
     */
    NAVIGATION(17, Flags.UPSTREAM),
    /** Notification of new latency adjustment */
    LATENCY(18, Flags.UPSTREAM),

    /** Upstream custom event */
    CUSTOM_UPSTREAM(32, Flags.UPSTREAM),
    /** Downstream custom event that travels in the data flow. */
    CUSTOM_DOWNSTREAM(32, Flags.DOWNSTREAM | Flags.SERIALIZED),
    /** Custom out-of-band downstream event. */
    CUSTOM_DOWNSTREAM_OOB(32, Flags.DOWNSTREAM),
    /** Custom upstream or downstream event.  In-band when travelling downstream.*/
    CUSTOM_BOTH(32, Flags.BOTH | Flags.SERIALIZED),
    /** Custom upstream or downstream out-of-band event. */
    CUSTOM_BOTH_OOB(32, Flags.BOTH);
    
    private static interface API extends com.sun.jna.Library {
        String gst_event_type_get_name(EventType type);
    }
    private static final API gst = GstNative.load(API.class);
    EventType(int num, int flags) {
        this.value = (num << SHIFT) | flags;
    }
    
    /** Gets the native value of this enum */
    public int intValue() {
        return value;
    }
    /** Gets a human-readable name for this value */
    public String getName() {
        return gst.gst_event_type_get_name(this);
    }
    
    /** Gets the Enum for a native value */
    public static final EventType valueOf(int type) {
        return EnumMapper.getInstance().valueOf(type, EventType.class);
    }
    
    private static final int SHIFT = 4;
    private static final class Flags {
        public static final int UPSTREAM = 1 << 0;
        public static final int DOWNSTREAM	= 1 << 1;
        public static final int SERIALIZED	= 1 << 2;
        public static final int BOTH = UPSTREAM | DOWNSTREAM;
    }
    private final int value;
}
