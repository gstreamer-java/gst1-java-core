/* 
 * Copyright (c) 2007 Wayne Meissner
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

import java.util.concurrent.TimeUnit;

/**
 * A representation of time in the gstreamer framework.
 */
public final class ClockTime implements Comparable<ClockTime> {
    //--------------------------------------------------------------------------
    // Static variables
    //
    public final static ClockTime NONE = new ClockTime(-1, TimeUnit.NANOSECONDS);
    public final static ClockTime ZERO = new ClockTime(0, TimeUnit.NANOSECONDS);
    
    //--------------------------------------------------------------------------
    // Instance variables
    //
    private final long value;
    
    /**
     * Creates a new instance of Time
     * 
     * @param time the length of time this object represents.
     * @param units the units <tt>time</tt> is expressed in.
     */
    private ClockTime(long time, TimeUnit units) {
        this.value = units.toNanos(time);
    }
    
    /**
     * Creates a new ClockTime object for a microsecond value.
     * @param microseconds The microsecond value to represent.
     * @return The new ClockTime object.
     */
    public static ClockTime fromMicros(long microseconds) {
        return valueOf(microseconds, TimeUnit.MICROSECONDS);
    }
    
    /**
     * Creates a new ClockTime object for a millisecond value.
     * @param milliseconds The millisecond value to represent.
     * @return The new ClockTime object.
     */
    public static ClockTime fromMillis(long milliseconds) {
        return valueOf(milliseconds, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Creates a new ClockTime object for a nanosecond value.
     * @param nanoseconds The nanosecond value to represent.
     * @return The new ClockTime object.
     */
    public static ClockTime fromNanos(long nanoseconds) {
        return valueOf(nanoseconds, TimeUnit.NANOSECONDS);
    }
    
    /**
     * Creates a new ClockTime object for a second value.
     * @param seconds The second value to represent.
     * @return The new ClockTime object.
     */
    public static ClockTime fromSeconds(long seconds) {
        return valueOf(seconds, TimeUnit.SECONDS);
    }
    
    /**
     * Returns a new ClockTime object that represents the <tt>time</tt> value.
     * 
     * @param time the length of time this object represents, in value.
     * @param units the units <tt>time</tt> is expressed in.
     * @return The new ClockTime object.
     */
    public static ClockTime valueOf(long time, TimeUnit units) {
        long nanoseconds = units.toNanos(time);
        if (nanoseconds == -1L) {
            return NONE;
        } else if (nanoseconds == 0L) {
            return ZERO;
        }
        return new ClockTime(time, units);
    }
    
    /**
     * Get the hours component of the total time.
     * 
     * @return The hours component of the total time.
     */
    public long getHours() {
        return (toSeconds() / 3600) % 24;
    }
    
    /**
     * Get the minutes component of the total time.
     * 
     * @return The minutes component of the total time.
     */
    public long getMinutes() {
        return (toSeconds() / 60) % 60;
    }
    
    /**
     * Get the seconds component of the total time.
     * 
     * @return The seconds component of the total time.
     */
    public long getSeconds() {
        return toSeconds() % 60;
    }
    
    /**
     * Get the nanosecond component of the total time.
     * 
     * @return The value component of the total time.
     */
    public long getNanoSeconds() {
        return value % TimeUnit.SECONDS.toNanos(1);
    }
    
    /**
     * Converts this ClockTime to a time value of <tt>unit</tt> units.
     * 
     * @param unit the {@link TimeUnit} to convertTo this time to.
     * @return the total time represented by this ClockTime.
     * @see TimeUnit#convert
     */
    public long convertTo(TimeUnit unit) {
        return unit.convert(value, TimeUnit.NANOSECONDS);
    }
    
    /**
     * Gets the total number of microseconds represented by this {@code ClockTime}.
     * <p> This is a convenience wrapper, equivalent to:
     * <p> {@code convertTo(TimeUnit.MICROSECONDS) }
     * 
     * @return The total microseconds represented by this {@code ClockTime}.
     */
    public long toMicros() {
        return convertTo(TimeUnit.MICROSECONDS);
    }
    
    /**
     * Gets the total number of milliseconds represented by this {@code ClockTime}.
     * <p> This is a convenience wrapper, equivalent to:
     * <p> {@code convertTo(TimeUnit.MILLISECONDS) }
     * 
     * @return The total milliseconds represented by this {@code ClockTime}.
     */
    public long toMillis() {
        return convertTo(TimeUnit.MILLISECONDS);
    }
    
    /**
     * Gets the total number of value represented by this {@code ClockTime}.
     * <p> This is a convenience wrapper, equivalent to:
     * <p> {@code convertTo(TimeUnit.NANOSECONDS) }
     * 
     * @return The total value represented by this {@code ClockTime}.
     */
    public long toNanos() {
        return convertTo(TimeUnit.NANOSECONDS);
    }
    
    /**
     * Gets the total number of seconds represented by this {@code ClockTime}.
     * <p> This is a convenience wrapper, equivalent to:
     * <p> {@code convertTo(TimeUnit.SECONDS) }
     * 
     * @return The total seconds represented by this {@code ClockTime}.
     */
    public long toSeconds() {
        return convertTo(TimeUnit.SECONDS);
    }
    
    /**
     * Determines if this ClockTime represents a valid time value.
     * 
     * @return true if valid, else false
     */
    public boolean isValid() {
        return value != NONE.value;
    }
    
    /**
     * Returns a {@code String} representation of this {@code ClockTime}.
     * 
     * @return a string representation of this {@code ClockTime}
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", getHours(), getMinutes(), getSeconds());
    }

    /**
     * Compares this {@code ClockTime} to the specified object.
     * <p> The result is {@code true} if and only if the argument is not
     * {@code null} and is a {@code ClockTime} object equivalent to this 
     * {@code ClockTime}
     * 
     * @param obj
     * @return <tt>true</tt> if the specified object is equivalent to this 
     * {@code ClockTime}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ClockTime && ((ClockTime) obj).value == value;
    }

    /**
     * Returns a hash code for this {@code ClockTime}.
     * 
     * @return a hash code value for this ClockTime.
     * @see java.lang.Long#hashCode
     */
    @Override
    public int hashCode() {
        return (int)(value ^ (value >>> 32));
    }
    

    /**
     * Compares this ClockTime to another.
     * 
     * @param time the other ClockTime to compare to.
     * @return {@code 0} if this {@code ClockTime} is equal to <tt>time</tt>.
     * A value less than zero if this {@code ClockTime} is numerically less than 
     * <tt>time</tt>.
     * A value greater than zero if this {@code ClockTime} is numerically 
     * greater than <tt>time</tt>.
     */
    public int compareTo(ClockTime time) {
        if (value < time.value) {
            return -1;
        } else if (value > time.value) {
            return 1;
        }
        return 0;
    }
}
