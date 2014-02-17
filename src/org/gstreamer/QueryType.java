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

package org.gstreamer;

import java.util.ArrayList;
import java.util.List;

import org.gstreamer.lowlevel.GstNative;

/**
 * Standard predefined Query types
 */
public final class QueryType implements Comparable<QueryType> {
    private static int queryMax = 0;
    private static final List<QueryType> cache = new ArrayList<QueryType>();
    private static interface API extends com.sun.jna.Library {
        String gst_query_type_get_name(QueryType query);
        QueryType gst_query_type_get_by_nick(String nick);
    }
    private static final API gst = GstNative.load(API.class);
    
    /** invalid query type */
    public static final QueryType NONE = init();
    /** current position in stream */
    public static final QueryType POSITION = init();
    /** total duration of the stream */
    public static final QueryType DURATION = init();
    /** latency of stream */
    public static final QueryType LATENCY = init();
    /** current jitter of stream */
    public static final QueryType JITTER = init();
    /** current rate of the stream */
    public static final QueryType RATE = init();
    /** seeking capabilities */
    public static final QueryType SEEKING = init();
    /** segment start/stop positions */
    public static final QueryType SEGMENT = init();
    /** convert values between formats */
    public static final QueryType CONVERT = init();
    /** query supported formats for convert */
    public static final QueryType FORMATS = init();
    
   
    private final Integer value;

    private static final QueryType init() {
        QueryType type = new QueryType(queryMax++);
        cache.add(type.value, type);
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
        if (value >= 0 && value < cache.size()) {
            return cache.get(value);
        }
        return new QueryType(value);
    }
    
    /**
     * Looks up a query type by its gstreamer nick.
     * 
     * @param nick the gstreamer nick.
     * @return the query type.
     */
    public static QueryType fromNick(String nick) {
        return gst.gst_query_type_get_by_nick(nick);
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
        return gst.gst_query_type_get_name(this);
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
