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

import java.util.EnumSet;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.glib.NativeFlags;
import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstEventAPI.GSTEVENT_API;

/**
 * A request for a new playback position and rate.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-seek"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-seek</a>
 * <p>
 * The seek event configures playback of the pipeline between <tt>start</tt> to <tt>stop</tt>
 * at the speed given in <tt>rate</tt>, also called a playback segment.
 * <p>
 * The <tt>start</tt> and <tt>stop</tt> values are expressed in <tt>format</tt>.
 * <p>
 * A <tt>rate</tt> of 1.0 means normal playback rate, 2.0 means double speed.
 * Negative values means backwards playback. A value of 0.0 for the
 * rate is not allowed and should be accomplished instead by PAUSING the
 * pipeline.
 * <p>
 * A pipeline has a default playback segment configured with a start
 * position of 0, a stop position of -1 and a rate of 1.0. The currently
 * configured playback segment can be queried with #GST_QUERY_SEGMENT. 
 * <p>
 * <tt>startType</tt> and <tt>stopType</tt> specify how to adjust the currently configured 
 * start and stop fields in <tt>segment</tt>. Adjustments can be made relative or
 * absolute to the last configured values. A type of {@link SeekType#NONE} means
 * that the position should not be updated.
 * <p>
 * When the rate is positive and <tt>start</tt> has been updated, playback will start
 * from the newly configured start position. 
 * <p>
 * For negative rates, playback will start from the newly configured <tt>stop<tt>
 * position (if any). If the stop position if updated, it must be different from
 * -1 for negative rates.
 * <p>
 * It is not possible to seek relative to the current playback position, to do
 * this, PAUSE the pipeline, query the current playback position with
 * {@link org.gstreamer.Pipeline#queryPosition getPosition} and update the playback segment 
 * current position with a {@link SeekType#SET} to the desired position.
 */
public class SeekEvent extends Event {
    
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    SeekEvent(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new seek event.
     * 
     * @param rate the new playback rate
     * @param format the format of the seek values
     * @param flags the optional seek flags
     * @param startType the type and flags for the new start position
     * @param start the value of the new start position
     * @param stopType the type and flags for the new stop position
     * @param stop the value of the new stop position
     */
    public SeekEvent(double rate, Format format, EnumSet<SeekFlags> flags, 
            SeekType startType, long start, SeekType stopType, long stop) {
        super(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_seek(sanitizeRate(rate), format, 
                NativeFlags.toInt(flags), startType, start, stopType, stop)));
    }
    
    private static double sanitizeRate(double rate) {
        if (rate == 0d) {
            throw new IllegalArgumentException("Cannot have rate == 0.0");
        }
        return rate;
    }
    /**
     * Gets the playback rate.
     * 
     * A <tt>rate</tt> of 1.0 means normal playback rate, 2.0 means double speed.
     * Negative values means backwards playback. A value of 0.0 for the
     * rate is not allowed and should be accomplished instead by PAUSING the
     * pipeline.
     * 
     * @return the playback rate.
     */
    public double getRate() {
        double[] rate = { 0d };
        GSTEVENT_API.gst_event_parse_seek(this, rate, null, null, null, null, null, null);
        return rate[0];
    }
    
    /**
     * Gets the {@link Format} of the start and stop seek values.
     * 
     * @return the format.
     */
    public Format getFormat() {
        Format[] format = new Format[1];
        GSTEVENT_API.gst_event_parse_seek(this, null, format, null, null, null, null, null);
        return format[0];
    }
    
    /**
     * Gets the {@link SeekFlags} of this seek event.
     * 
     * @return the seek flags.
     */
    public EnumSet<SeekFlags> getFlags() {
        int[] flags = { 0 };
        GSTEVENT_API.gst_event_parse_seek(this, null, null, flags, null, null, null, null);
        return NativeFlags.fromInt(SeekFlags.class, flags[0]);
    }
    
    /**
     * Gets the SeekType of the start value.
     * 
     * @return the SeekType.
     */
    public SeekType getStartType() {
        SeekType[] type = new SeekType[1];
        GSTEVENT_API.gst_event_parse_seek(this, null, null, null, type, null, null, null);
        return type[0];
    }
    
    /**
     * Gets the start of the seek segment.
     * 
     * @return the start of the seek.
     */
    public long getStart() {
        long[] value = { 0 };
        GSTEVENT_API.gst_event_parse_seek(this, null, null, null, null, value, null, null);
        return value[0];
    }
    
    /**
     * Gets the SeekType of the start value.
     * 
     * @return the SeekType.
     */
    public SeekType getStopType() {
        SeekType[] type = new SeekType[1];
        GSTEVENT_API.gst_event_parse_seek(this, null, null, null, null, null, type, null);
        return type[0];
    }
    
    /**
     * Gets the stop position of the seek.
     * 
     * @return the stop position.
     */
    public long getStop() {
        long[] value = { 0 };
        GSTEVENT_API.gst_event_parse_seek(this, null, null, null, null, null, null, value);
        return value[0];
    }
}
