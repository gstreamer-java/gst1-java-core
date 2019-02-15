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

package org.freedesktop.gstreamer.query;

import java.util.HashMap;
import java.util.Map;

import org.freedesktop.gstreamer.lowlevel.GstQueryAPI;
import org.freedesktop.gstreamer.lowlevel.GstQueryAPI.GstQueryTypeFlags;

import static org.freedesktop.gstreamer.lowlevel.GstQueryAPI.GSTQUERY_API;

/**
 * Standard predefined Query types
 */
public final class QueryType implements Comparable<QueryType> {

    private static final Map<Integer,String> nameCache = new HashMap<Integer,String>();
    
    /** invalid query type */
    public static final QueryType UNKNOWN = makeType(0, 0);
    /** current position in stream */
    public static final QueryType POSITION = makeType(10, GstQueryTypeFlags.BOTH);
    /** total duration of the stream */
    public static final QueryType DURATION = makeType(20, GstQueryTypeFlags.BOTH);
    /** latency of stream */
    public static final QueryType LATENCY = makeType(30, GstQueryTypeFlags.BOTH);
    /** current jitter of stream */
    public static final QueryType JITTER = makeType(40, GstQueryTypeFlags.BOTH);
    /** current rate of the stream */
    public static final QueryType RATE = makeType(50, GstQueryTypeFlags.BOTH);
    /** seeking capabilities */
    public static final QueryType SEEKING = makeType(60, GstQueryTypeFlags.BOTH);
    /** segment start/stop positions */
    public static final QueryType SEGMENT = makeType(70, GstQueryTypeFlags.BOTH);
    /** convert values between formats */
    public static final QueryType CONVERT = makeType(80, GstQueryTypeFlags.BOTH);
    /** query supported formats for convert */
    public static final QueryType FORMATS = makeType(90, GstQueryTypeFlags.BOTH);
    /** query available media for efficient seeking */
    public static final QueryType BUFFERING = makeType(110, GstQueryTypeFlags.BOTH);
    /** a custom application or element defined query */
    public static final QueryType CUSTOM = makeType(120, GstQueryTypeFlags.BOTH);
    /** query the URI of the source or sink */
    public static final QueryType URI = makeType(130, GstQueryTypeFlags.BOTH);
    /** the buffer allocation properties */
    public static final QueryType ALLOCATION = makeType(140, GstQueryTypeFlags.DOWNSTREAM | GstQueryTypeFlags.SERIALIZED);
    /** the scheduling properties */
    public static final QueryType SCHEDULING = makeType(150, GstQueryTypeFlags.UPSTREAM);
    /** the accept caps query */
    public static final QueryType ACCEPT_CAPS = makeType(160, GstQueryTypeFlags.BOTH);
    /** the caps query */
    public static final QueryType CAPS = makeType(170, GstQueryTypeFlags.BOTH);
    /** wait till all serialized data is consumed downstream */
    public static final QueryType DRAIN = makeType(180, GstQueryTypeFlags.DOWNSTREAM | GstQueryTypeFlags.SERIALIZED);
    /** query the pipeline-local context from downstream or upstream (since 1.2) */
    public static final QueryType CONTEXT = makeType(190, GstQueryTypeFlags.BOTH);
   
    private final Integer value;

    private static QueryType makeType(int num, int flags) {
        int value = (num << GstQueryAPI.GST_QUERY_NUM_SHIFT) | flags;
        QueryType type = new QueryType(value);
        return type; 
    }
    
    /**
     * Returns the QueryType with the specified integer value.
     * @param value integer value.
     * @return QueryType constant.
     * @throws java.lang.IllegalArgumentException if there is no QueryType
     * with the specified value.
     */
    public static QueryType valueOf(int value) {
        return new QueryType(value);
    }
    
    private QueryType(int value) {
        this.value = value;
    }
    
    /**
     * Gets the integer value of the enum.
     * 
     * @return the integer value for this enum.
     */
    public int intValue() {
        return value;
    }
    
    /**
     * gets the name of this type.
     * 
     * @return the gstreamer name for this type.
     */
    public String getName() {
        String cachedName = nameCache.get(value);
        if(cachedName == null) {
            cachedName = GSTQUERY_API.gst_query_type_get_name(this);
            nameCache.put(value, cachedName);
        }
        return cachedName;
    }

    /**
     * Compares this {@code QueryType} to the specified object.
     * <p> The result is {@code true} if and only if the argument is not
     * {@code null} and is a {@code QueryType} object equivalent to this 
     * {@code QueryType}
     * 
     * @param obj
     * @return <tt>true</tt> if the specified object is equivalent to this 
     * {@code QueryType}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof QueryType && ((QueryType) obj).value.equals(value);
    }

    /**
     * Returns a hash code for this {@code QueryType}.
     * 
     * @return a hash code value for this QueryType.
     * @see java.lang.Integer#hashCode
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    /**
     * Compares this QueryType to another.
     * 
     * @param queryType the other QueryType to compare to.
     * @return {@code 0} if this {@code QueryType} is equal to <tt>queryType</tt>.
     * A value less than zero if this {@code QueryType} is numerically less than 
     * <tt>queryType</tt>.
     * A value greater than zero if this {@code QueryType} is numerically 
     * greater than <tt>queryType</tt>.
     */
    public int compareTo(QueryType queryType) { 
        return value.compareTo(queryType.value);
    }
}
