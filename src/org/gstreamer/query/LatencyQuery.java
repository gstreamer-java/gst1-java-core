/* 
 * Copyright (C) 2008 Wayne Meissner
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

package org.gstreamer.query;

import org.gstreamer.ClockTime;
import org.gstreamer.Query;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 *
 * @author wayne
 */
public class LatencyQuery extends Query {
    private static interface API extends com.sun.jna.Library { 
        /* latency query */
        Pointer ptr_gst_query_new_latency();
        void gst_query_set_latency(Query query, boolean live, ClockTime min_latency,
             ClockTime max_latency);
        void gst_query_parse_latency(Query query, boolean[] live, ClockTime[] min_latency, 
                                                     ClockTime[] max_latency);
    }
    private static final API gst = GstNative.load(API.class);
    
    public LatencyQuery(Initializer init) {
        super(init);
    }
    
    /**
     * Constructs a new query stream position query object. A position query is 
     * used to query the current position of playback in the streams, in some format.
     */
    public LatencyQuery() {
        super(initializer(gst.ptr_gst_query_new_latency()));
    }
    
    /**
     * Answers a latency query.
     * 
     * @param live if there is a live element upstream
     * @param minLatency the minimal latency of the live element
     * @param maxLatency the maximal latency of the live element
     */
    public void setLatency(boolean live, ClockTime minLatency, ClockTime maxLatency) {
        gst.gst_query_set_latency(this, live, minLatency, maxLatency);
    }
    /**
     * Gets whether the element has a live element upstream or not.
     * 
     * @return true if the element has a live element upstream.
     */
    public boolean isLive() {
        boolean[] live = new boolean[1];
        gst.gst_query_parse_latency(this, live, null, null);
        return live[0];
    }
    
    /**
     * Gets the minimum latency of the live element.
     * 
     * @return The minimum latency of the live element.
     */
    public ClockTime getMinimumLatency() {
        ClockTime[] latency = new ClockTime[1];
        gst.gst_query_parse_latency(this, null, latency, null);
        return latency[0];
    }
    
    /**
     * Gets the maximum latency of the live element.
     * 
     * @return The maximum latency of the live element.
     */
    public ClockTime getMaximumLatency() {
        ClockTime[] latency = new ClockTime[1];
        gst.gst_query_parse_latency(this, null, null, latency);
        return latency[0];
    }
    
    /**
     * Gets the latency as a user-readable string.
     * 
     * @return A string representing the latency.
     */
    @Override
    public String toString() {
        return String.format("latency:[live=%b, min=%s, max=%s]", 
                isLive(), getMinimumLatency(), getMaximumLatency());
    }

}
