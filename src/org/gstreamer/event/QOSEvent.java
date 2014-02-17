/* 
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wim.taymans@chello.be>
 *                    2005 Wim Taymans <wim@fluendo.com>
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

package org.gstreamer.event;

import org.gstreamer.ClockTime;
import org.gstreamer.Event;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * A quality message. Used to indicate to upstream elements that the downstream 
 * elements are being starved of or flooded with data.
 * 
 * <p>The QOS event is generated in an element that wants an upstream
 * element to either reduce or increase its rate because of
 * high/low CPU load or other resource usage such as network performance.
 * Typically sinks generate these events for each buffer they receive.
 *
 */
public class QOSEvent extends Event {
    private static interface API extends com.sun.jna.Library {
        Pointer ptr_gst_event_new_qos(double proportion, long diff, ClockTime timestamp);
        void gst_event_parse_qos(Event event, double[] proportion, long[] diff, ClockTime[] timestamp);
    }
    private static final API gst = GstNative.load(API.class);
    
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    public QOSEvent(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new quality-of-service event.
     * <p>
     * <tt>proportion</tt> indicates the real-time performance of the streaming in the
     * element that generated the QoS event (usually the sink). The value is
     * generally computed based on more long term statistics about the streams
     * timestamps compared to the clock.
     * <p> 
     * A value &lt; 1.0 indicates that the upstream element is producing data faster
     * than real-time. A value &gt; 1.0 indicates that the upstream element is not
     * producing data fast enough. 1.0 is the ideal <tt>proportion</tt> value. The
     * proportion value can safely be used to lower or increase the quality of
     * the element.
     * <p>
     * <tt>difference</tt> is the difference against the clock in running time of the last
     * buffer that caused the element to generate the QOS event. A negative value
     * means that the buffer with <tt>timestamp</tt> arrived in time. A positive value
     * indicates how late the buffer with <tt>timestamp</tt> was.
     * <p>
     * <tt>timestamp</tt> is the timestamp of the last buffer that cause the element
     * to generate the QOS event. It is expressed in running time and thus an ever
     * increasing value.
     * <p>
     * The upstream element can use the <tt>diff</tt> and <tt>timestamp</tt> values to decide
     * whether to process more buffers. For positive <tt>difference</tt>, all buffers with
     * timestamp <= <tt>timestamp</tt> + <tt>difference</tt> will certainly arrive late in the sink
     * as well. 
     * <p>
     * The application can use general event probes to intercept the QoS
     * event and implement custom application specific QoS handling.
     * 
     * @param proportion the proportion of the qos message
     * @param difference the time difference of the last Clock sync
     * @param timestamp the timestamp of the buffer
     */
    public QOSEvent(double proportion, long difference, ClockTime timestamp) {
        super(initializer(gst.ptr_gst_event_new_qos(proportion, difference, timestamp)));
    }
    
    /**
     * Gets the proportion value of this event.
     * <p>
     * The proportion indicates the real-time performance of the streaming in the
     * element that generated the QoS event (usually the sink). The value is
     * generally computed based on more long term statistics about the streams
     * timestamps compared to the clock.
     * 
     * @return the proportion.
     */
    public double getProportion() {
        double[] p = { 0d };
        gst.gst_event_parse_qos(this, p, null, null);
        return p[0];
    }
    
    /**
     * Gets the difference value of this event.
     * <p>
     * This is the difference against the clock in running time of the last
     * buffer that caused the element to generate the QOS event. A negative value
     * means that the buffer with <tt>timestamp</tt> arrived in time. A positive value
     * indicates how late the buffer with <tt>timestamp</tt> was.
     * 
     * @return the difference.
     */
    public long getDifference() {
        long[] diff = { 0 };
        gst.gst_event_parse_qos(this, null, diff, null);
        return diff[0];
    }
    
    /**
     * Gets the timestamp from this event.
     * <p>
     * This is the timestamp of the last buffer that caused the element to generate the 
     * QOS event. It is expressed in running time and thus an ever increasing value.
     * 
     * @return the timestamp
     */
    public ClockTime getTimestamp() {
        ClockTime[] timestamp = new ClockTime[1];
        gst.gst_event_parse_qos(this, null, null, timestamp);
        return timestamp[0];
    }
}   
