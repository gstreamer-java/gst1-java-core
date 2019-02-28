/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer.event;

import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstEventAPI.GSTEVENT_API;

/**
 * A quality message. Used to indicate to upstream elements that the downstream
 * elements are being starved of or flooded with data.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-qos"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-qos</a>
 * <p>
 * <tt>proportion</tt> indicates the real-time performance of the streaming in
 * the element that generated the QoS event (usually the sink). The value is
 * generally computed based on more long term statistics about the streams
 * timestamps compared to the clock.
 * <p>
 * A value &lt; 1.0 indicates that the upstream element is producing data faster
 * than real-time. A value &gt; 1.0 indicates that the upstream element is not
 * producing data fast enough. 1.0 is the ideal <tt>proportion</tt> value. The
 * proportion value can safely be used to lower or increase the quality of the
 * element.
 * <p>
 * <tt>difference</tt> is the difference against the clock in running time of
 * the last buffer that caused the element to generate the QOS event. A negative
 * value means that the buffer with <tt>timestamp</tt> arrived in time. A
 * positive value indicates how late the buffer with <tt>timestamp</tt> was.
 * <p>
 * <tt>timestamp</tt> is the timestamp of the last buffer that cause the element
 * to generate the QOS event. It is expressed in running time and thus an ever
 * increasing value.
 * <p>
 * The upstream element can use the <tt>diff</tt> and <tt>timestamp</tt> values
 * to decide whether to process more buffers. For positive <tt>difference</tt>,
 * all buffers with timestamp <= <tt>timestamp</tt> + <tt>difference</tt> will
 * certainly arrive late in the sink as well.
 * <p>
 * The application can use general event probes to intercept the QoS event and
 * implement custom application specific QoS handling.
 *
 */
public class QOSEvent extends Event {

    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    QOSEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new quality-of-service event.
     *
     *
     * @param proportion the proportion of the qos message
     * @param difference the time difference of the last Clock sync
     * @param timestamp the timestamp of the buffer
     */
    public QOSEvent(QOSType type, double proportion, long difference, long timestamp) {
        super(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_qos(type, proportion, difference, timestamp)));
    }

    /**
     * @return the proportion.
     */
    public QOSType getType() {
        QOSType[] type = {null};
        GSTEVENT_API.gst_event_parse_qos(this, type, null, null, (long[]) null);
        return type[0];
    }

    /**
     * Gets the proportion value of this event.
     * <p>
     * The proportion indicates the real-time performance of the streaming in
     * the element that generated the QoS event (usually the sink). The value is
     * generally computed based on more long term statistics about the streams
     * timestamps compared to the clock.
     *
     * @return the proportion.
     */
    public double getProportion() {
        double[] p = {0d};
        GSTEVENT_API.gst_event_parse_qos(this, null, p, null, (long[]) null);
        return p[0];
    }

    /**
     * Gets the difference value of this event.
     * <p>
     * This is the difference against the clock in running time of the last
     * buffer that caused the element to generate the QOS event. A negative
     * value means that the buffer with <tt>timestamp</tt> arrived in time. A
     * positive value indicates how late the buffer with <tt>timestamp</tt> was.
     *
     * @return the difference.
     */
    public long getDifference() {
        long[] diff = {0};
        GSTEVENT_API.gst_event_parse_qos(this, null, null, diff, (long[]) null);
        return diff[0];
    }

    /**
     * Gets the timestamp from this event.
     * <p>
     * This is the timestamp of the last buffer that caused the element to
     * generate the QOS event. It is expressed in running time and thus an ever
     * increasing value.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        long[] timestamp = new long[1];
        GSTEVENT_API.gst_event_parse_qos(this, null, null, null, timestamp);
        return timestamp[0];
    }
}
