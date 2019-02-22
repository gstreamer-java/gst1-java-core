/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer;

import java.util.concurrent.TimeUnit;

/**
 * Utility methods for working with clock time (ns) in GStreamer.
 */
public final class ClockTime {

    public final static long NONE = -1;
    public final static long ZERO = 0;

    
    private ClockTime() {}
    
    /**
     * Convert time in microseconds to GStreamer clocktime (nanoseconds)
     *
     * @param microseconds the microsecond value to represent.
     * @return time in nanoseconds
     */
    public static long fromMicros(long microseconds) {
        return TimeUnit.MICROSECONDS.toNanos(microseconds);
    }

    /**
     * Convert GStreamer clocktime (nanoseconds) to microseconds.
     *
     * @param clocktime nanosecond time
     * @return time in microseconds
     */
    public static long toMicros(long clocktime) {
        return TimeUnit.NANOSECONDS.toMicros(clocktime);
    }
    
    /**
     * Convert time in milliseconds to GStreamer clocktime (nanoseconds)
     *
     * @param milliseconds the millisecond value to represent.
     * @return time in nanoseconds
     */
    public static long fromMillis(long milliseconds) {
        return TimeUnit.MILLISECONDS.toNanos(milliseconds);
    }

    /**
     * Convert GStreamer clocktime (nanoseconds) to milliseconds.
     *
     * @param clocktime nanosecond time
     * @return time in milliseconds
     */
    public static long toMillis(long clocktime) {
        return TimeUnit.NANOSECONDS.toMillis(clocktime);
    }

    /**
     * Convert time in milliseconds to GStreamer clocktime (nanoseconds)
     *
     * @param seconds the seconds value to represent.
     * @return time in nanoseconds
     */
    public static long fromSeconds(long seconds) {
        return TimeUnit.SECONDS.toNanos(seconds);
    }

    /**
     * Convert GStreamer clocktime (nanoseconds) to seconds.
     *
     * @param clocktime nanosecond time
     * @return time in milliseconds
     */
    public static long toSeconds(long clocktime) {
        return TimeUnit.NANOSECONDS.toSeconds(clocktime);
    }

    /**
     * Get the hours component of the total time.
     *
     * @param clocktime GStreamer time in nanoseconds
     * @return The hours component of the total time.
     */
    public static long getHoursComponent(long clocktime) {
        return (toSeconds(clocktime) / 3600) % 24;
    }

    /**
     * Get the minutes component of the total time.
     *
     * @param clocktime GStreamer time in nanoseconds
     * @return The minutes component of the total time.
     */
    public static long getMinutesComponent(long clocktime) {
        return (toSeconds(clocktime) / 60) % 60;
    }

    /**
     * Get the seconds component of the total time.
     *
     * @param clocktime GStreamer time in nanoseconds
     * @return The seconds component of the total time.
     */
    public static long getSecondsComponent(long clocktime) {
        return toSeconds(clocktime) % 60;
    }

    /**
     * Determines if this ClockTime represents a valid time value.
     *
     * @param clocktime GStreamer time in nanoseconds
     * @return true if valid, else false
     */
    public static boolean isValid(long clocktime) {
        return clocktime != NONE;
    }

    /**
     * Returns a {@code String} representation of this {@code ClockTime}.
     *
     * @param clocktime GStreamer time in nanoseconds
     * @return a string representation of this {@code ClockTime}
     */
    public static String toString(long clocktime) {
        return String.format("%02d:%02d:%02d",
                getHoursComponent(clocktime),
                getMinutesComponent(clocktime),
                getSecondsComponent(clocktime));
    }

}
