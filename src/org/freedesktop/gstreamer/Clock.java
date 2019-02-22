/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wtay@chello.be>
 *                    2004 Wim Taymans <wim@fluendo.com>
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

import static org.freedesktop.gstreamer.lowlevel.GstClockAPI.GSTCLOCK_API;

/**
 * Abstract class for global clocks.
 * <p>
 * GStreamer uses a global clock to synchronize the plugins in a pipeline.
 * Different clock implementations are possible by implementing this abstract
 * base class.
 * <p>
 * The {@link Clock} returns a monotonically increasing time with the method
 * gst_clock_get_time(). Its accuracy and base time depend on the specific
 * clock implementation but time is always expressed in nanoseconds. Since the
 * baseline of the clock is undefined, the clock time returned is not
 * meaningful in itself, what matters are the deltas between two clock times.
 * The time returned by a clock is called the absolute time.
 * <p>
 * The pipeline uses the clock to calculate the stream time. Usually all
 * renderers synchronize to the global clock using the buffer timestamps, the
 * newsegment events and the element's base time, see #GstPipeline.
 * <p>
 * A clock implementation can support periodic and single shot clock
 * notifications both synchronous and asynchronous.
 * <p>
 * One first needs to create a {@link ClockID} for the periodic or single shot
 * notification using {@link #newSingleShotID} or {@link #newPeriodicID}.
 * <p>
 * To perform a blocking wait for the specific time of the {@link ClockID} use the
 * gst_clock_id_wait(). To receive a callback when the specific time is reached
 * in the clock use gst_clock_id_wait_async(). Both these calls can be
 * interrupted with the gst_clock_id_unschedule() call. If the blocking wait is
 * unscheduled a return value of GST_CLOCK_UNSCHEDULED is returned.
 * <p>
 * Periodic callbacks scheduled async will be repeatedly called automatically
 * until it is unscheduled. To schedule a sync periodic callback,
 * gst_clock_id_wait() should be called repeatedly.
 * <p>
 * The async callbacks can happen from any thread, either provided by the core
 * or from a streaming thread. The application should be prepared for this.
 * <p>
 * A {@link ClockID} that has been unscheduled cannot be used again for any wait
 * operation, a new ClockID should be created.
 * <p>
 * It is possible to perform a blocking wait on the same ClockID from
 * multiple threads. However, registering the same ClockID for multiple
 * async notifications is not possible, the callback will only be called for
 * the thread registering the entry last.
 * <p>
 * These clock operations do not operate on the stream time, so the callbacks
 * will also occur when not in {@link State#PLAYING} state as if the clock just keeps on
 * running. Some clocks however do not progress when the element that provided
 * the clock is not {@link State#PLAYING}.
 * <p>
 * When a clock has the GST_CLOCK_FLAG_CAN_SET_MASTER flag set, it can be
 * slaved to another #GstClock with the gst_clock_set_master(). The clock will
 * then automatically be synchronized to this master clock by repeatedly
 * sampling the master clock and the slave clock and recalibrating the slave
 * clock with {@link #setCalibration}. This feature is mostly useful for
 * plugins that have an internal clock but must operate with another clock
 * selected by the {@link Pipeline}.  They can track the offset and rate difference
 * of their internal clock relative to the master clock by using the
 * gst_clock_get_calibration() function. 
 * <p>
 * The master/slave synchronisation can be tuned with the "timeout", "window-size"
 * and "window-threshold" properties. The "timeout" property defines the interval
 * to sample the master clock and run the calibration functions. 
 * "window-size" defines the number of samples to use when calibrating and
 * "window-threshold" defines the minimum number of samples before the 
 * calibration is performed.
 */
// @TODO finish off API after removing ClockTime from mappings.
public class Clock extends GstObject {
    public static final String GTYPE_NAME = "GstClock";

    public Clock(Initializer init) {
        super(init); 
    }
    
    /**
     * Sets the accuracy of the clock. 
     * <p>
     * Some clocks have the possibility to operate with different accuracy at 
     * the expense of more resource usage. There is normally no need to change 
     * the default resolution of a clock. The resolution of a clock can only be 
     * changed if the clock has the GST_CLOCK_FLAG_CAN_SET_RESOLUTION flag set.
     *
     * @param resolution the new resolution of the clock.
     * @return the new resolution of the clock.
     */
    public long setResolution(long resolution) {
        return GSTCLOCK_API.gst_clock_set_resolution(this, resolution);
    }
    
    /**
     * Gets the accuracy of the clock. The accuracy of the clock is the granularity
     * of the values returned by {@link #getTime}.
     *
     * @return the resolution of the clock in nanoseconds.
     */
    public long getResolution() {
        return GSTCLOCK_API.gst_clock_get_resolution(this);
    }
    
    /**
     * Gets the current time of the given clock. The time is always
     * monotonically increasing and adjusted according to the current
     * offset and rate.
     *
     * Returns: the time of the clock. Or GST_CLOCK_TIME_NONE when
     * giving wrong input.
     * @return the time of the clock. Or {@link ClockTime#NONE} when
     * given incorrect input.
     */
    public long getTime() {
        return GSTCLOCK_API.gst_clock_get_time(this);
    }
    /**
     * Gets the current internal time of this clock. The time is returned
     * unadjusted for the offset and the rate.
     *
     * Thread safe.
     * 
     * @return the internal time of the clock. Or {@link ClockTime#NONE} when given wrong input.
     */
    public long getInternalTime() {
        return GSTCLOCK_API.gst_clock_get_internal_time(this);
    }
    
    /**
     * Gets the master clock that this clock is slaved to or null when the clock is
     * not slaved to any master clock.
     *
     * @return A master Clock or null when this clock is not slaved to a master
     * clock.
     */
    public Clock getMaster() {
        return GSTCLOCK_API.gst_clock_get_master(this);
    }
    
    /**
     * Set master as the master clock for this clock. This clock will be automatically
     * calibrated so that {@link #getTime} reports the same time as the
     * master clock.  
     * 
     * A clock provider that slaves its clock to a master can get the current
     * calibration values with {@link #getCalibration}.
     *
     * master can be null in which case clock will not be slaved anymore. It will
     * however keep reporting its time adjusted with the last configured rate 
     * and time offsets.
     *
     * @param master a master Clock 
     * @return true if the clock is capable of being slaved to a master clock. 
     * Trying to set a master on a clock without the CAN_SET_MASTER flag will make 
     * this function return false.
     */
    public boolean setMaster(Clock master) {
        return GSTCLOCK_API.gst_clock_set_master(this, master);
    }
    
    /**
     * Gets the internal rate and reference time of clock. See {@link #setCalibration} for more information.
     * <p>
     * internal, external, rate_num, and rate_denom can be left NULL if the caller is not interested in the values.
     *
     * Thread safe.
     * @param internal a reference internal time
     * @param external a reference external time
     * @param rateNumerator the numerator of the rate of the clock relative to its internal time
     * @param rateDenominator the denominator of the rate of the clock
     */
    public void getCalibration(long internal, long external, long rateNumerator, long rateDenominator) {
        GSTCLOCK_API.gst_clock_set_calibration(this, internal, external, rateNumerator, rateDenominator);
    }
    
    /**
     *  Adjusts the rate and time of this clock. A rate of 1/1 is the normal speed of
     * the clock. Values bigger than 1/1 make the clock go faster.
     * <p>
     * internal and external are calibration parameters that arrange that
     * {@link #getTime} should have been external at internal time internal.
     * This internal time should not be in the future; that is, it should be less
     * than the value of {@link #getInternalTime} when this function is called.
     * <p>
     * Subsequent calls to gst_clock_get_time() will return clock times computed as
     * follows:
     * <p>
     * <code>
     *   time = (internal_time - internal) * rateNumerator/ rateDenominator + external
     * </code>
     * <p>
     * This formula is implemented in gst_clock_adjust_unlocked(). Of course, it
     * tries to do the integer arithmetic as precisely as possible.
     * <p>
     * Note that {@link #getTime} always returns increasing values so when you
     * move the clock backwards, getTime() will report the previous value
     * until the clock catches up.
     *
     * Thread safe.
     * @param internal a reference internal time
     * @param external a reference external time
     * @param rateNumerator the numerator of the rate of the clock relative to its internal time
     * @param rateDenominator the denominator of the rate of the clock
     */
    public void setCalibration(long internal, long external, long rateNumerator, long rateDenominator) {
        GSTCLOCK_API.gst_clock_set_calibration(this, internal, external, rateNumerator, rateDenominator);
    }
    
    /**
     * Gets a {@link ClockID} from this clock to trigger a single shot
     * notification at the requested time.
     * <p>
     * Thread safe.
     * 
     * @param time The requested time
     * @return A {@link ClockID} that can be used to request the time notification.
     */
    public ClockID newSingleShotID(long time) {
        return GSTCLOCK_API.gst_clock_new_single_shot_id(this, time);
    }
    
    /**
     * Gets an ID from this clock to trigger a periodic notification.
     * The periodeic notifications will be start at time start_time and
     * will then be fired with the given interval.
     * <p>
     * Thread safe.
     * 
     * @param startTime The requested start time.
     * @param interval The requested interval.
     * @return A {@link ClockID} that can be used to request the time notification.
     */
    public ClockID newPeriodicID(long startTime, long interval) {
        return GSTCLOCK_API.gst_clock_new_periodic_id(this, startTime, interval);
    }
}
