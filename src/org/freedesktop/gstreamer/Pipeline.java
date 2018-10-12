/* 
 * Copyright (c) 2018 Neil C Smith
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 2000 Erik Walthinsen <omega@cse.ogi.edu>
 *               2005 Wim Taymans <wim@fluendo.com>
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

import com.sun.jna.Pointer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freedesktop.gstreamer.lowlevel.GstAPI.GErrorStruct;

import static org.freedesktop.gstreamer.lowlevel.GstElementAPI.GSTELEMENT_API;
import static org.freedesktop.gstreamer.lowlevel.GstParseAPI.GSTPARSE_API;
import static org.freedesktop.gstreamer.lowlevel.GstPipelineAPI.GSTPIPELINE_API;
import static org.freedesktop.gstreamer.lowlevel.GstQueryAPI.GSTQUERY_API;

/**
 * A {@code Pipeline} is a special {@link Bin} used as the toplevel container for
 * the filter graph. The <tt>Pipeline</tt> will manage the selection and
 * distribution of a global {@link Clock} as well as provide a {@link Bus} to
 * the application. It will also implement a default behavour for managing
 * seek events (see {@link #seek seek}).
 *
 * <p>
 * Elements are added and removed from the pipeline using the {@code Bin}
 * methods like {@link Bin#add add} and {@link Bin#remove remove}.
 *
 * <p>
 * Before changing the state of the <tt>Pipeline</tt> (see {@link Element}) a 
 * {@link Bus} can be retrieved with {@link #getBus getBus}. This bus can then 
 * be used to receive {@link Message}s from the elements in the pipeline.
 *
 * <p>
 * By default, a <tt>Pipeline</tt> will automatically flush the pending <tt>Bus</tt>
 * messages when going to the NULL state to ensure that no circular
 * references exist when no messages are read from the <tt>Bus</tt>. This
 * behaviour can be changed with {@link #setAutoFlushBus setAutoFlushBus}
 *
 * <p>
 * When the <tt>Pipeline</tt> performs the PAUSED to PLAYING state change it will
 * select a clock for the elements. The clock selection algorithm will by
 * default select a clock provided by an element that is most upstream
 * (closest to the source). For live pipelines (ones that return
 * {@link StateChangeReturn#NO_PREROLL} from the 
 * {@link Element#setState setState} call) this will select the clock provided 
 * by the live source. For normal pipelines this will select a clock provided 
 * by the sinks (most likely the audio sink). If no element provides a clock,
 * a default <tt>SystemClock</tt> is used.
 * 
 * <p>
 * The clock selection can be controlled with the gst_pipeline_use_clock()
 * method, which will enforce a given clock on the pipeline. With
 * gst_pipeline_auto_clock() the default clock selection algorithm can be
 * restored.
 *
 * <p>
 * A <tt>Pipeline</tt> maintains a stream time for the elements. The stream
 * time is defined as the difference between the current clock time and
 * the base time. When the pipeline goes to READY or a flushing seek is
 * performed on it, the stream time is reset to 0. When the pipeline is
 * set from PLAYING to PAUSED, the current clock time is sampled and used to
 * configure the base time for the elements when the pipeline is set
 * to PLAYING again. This default behaviour can be changed with the
 * gst_pipeline_set_new_stream_time() method.
 *
 * When sending a flushing seek event to a GstPipeline (see
 * {@link #seek seek}), it will make sure that the pipeline is properly
 * PAUSED and resumed as well as set the new stream time to 0 when the
 * seek succeeded.
 */
public class Pipeline extends Bin {
    public static final String GST_NAME = "pipeline";
    public static final String GTYPE_NAME = "GstPipeline";

    private static Logger logger = Logger.getLogger(Pipeline.class.getName());
        
    public Pipeline(Initializer init) { 
        super(init);
    }
    
    /**
     * Creates a new instance of Pipeline with a unique name.
     */
    public Pipeline() {
        this(initializer(GSTPIPELINE_API.ptr_gst_pipeline_new(null), false));
        initBus();
    }
    
    /**
     * Creates a new instance of Pipeline with the given name.
     * 
     * @param name The name used to identify this pipeline.
     */
    public Pipeline(String name) {
        this(initializer(name));
        initBus();
    }

	private static Initializer initializer(String name) {
		Pointer new_pipeline = GSTPIPELINE_API.ptr_gst_pipeline_new(name);
		return initializer(new_pipeline, false);
	}
    
    /**
     * Create a new pipeline based on command line syntax.
     * 
     * Please note that you might get a return value that is not NULL even 
     * though the error is set. 
     * In this case there was a recoverable parsing error and you can try 
     * to play the pipeline.
     * 
     * @param pipelineDecription  the command line describing the pipeline
     * @return The new Pipeline.
     */
    @Deprecated
    public static Pipeline launch(String pipelineDecription) {
        Pointer[] err = { null };
        Pipeline pipeline = (Pipeline) GSTPARSE_API.gst_parse_launch(pipelineDecription, err);
        if (pipeline == null) {
            throw new GstException(new GError(new GErrorStruct(err[0])));
        }
        
        // check for error
        if (err[0] != null) { 
            logger.log(Level.WARNING, new GError(new GErrorStruct(err[0])).getMessage());
        }

        pipeline.initBus();
        return pipeline;
    }
    
    /**
     * Create a new element based on command line syntax.
     * 
     * error will contain an error message if an erroneous pipeline is specified. 
     * An error does not mean that the pipeline could not be constructed.
     * 
     * @param pipelineDecription An array of strings containing the command line describing the pipeline.
     * @return The new Pipeline.
     */
    @Deprecated
    public static Pipeline launch(String... pipelineDecription) {
        Pointer[] err = { null };
        Pipeline pipeline = (Pipeline) GSTPARSE_API.gst_parse_launchv(pipelineDecription, err);
        if (pipeline == null) {
            throw new GstException(new GError(new GErrorStruct(err[0])));
        }
        
        // check for error
        if (err[0] != null) {
            // log error!            
            logger.log(Level.WARNING, new GError(new GErrorStruct(err[0])).getMessage());
        }

        pipeline.initBus();
        return pipeline;
    }
    private void initBus() {
        Bus bus = getBus();
        // TODO started to work around with add_signal_watch - when working can
        // remove this comment!
        // This is mostly a hack to get the Bus to install a sync message handler
        //
//        bus.setFlushing(true);
//        bus.setFlushing(false);
    }
    /**
     * Sets this pipeline to automatically flush {@link Bus} messages or not.
     *
     * @param flush true if automatic flushing is desired, else false.
     */
    public void setAutoFlushBus(boolean flush) {
        GSTPIPELINE_API.gst_pipeline_set_auto_flush_bus(this, flush);
    }
    
    /**
     * Checks if the pipeline will automatically flush messages when going to the NULL state.
     * 
     * @return true if the pipeline automatically flushes messages.
     */     
    public boolean getAutoFlushBus() {
        return GSTPIPELINE_API.gst_pipeline_get_auto_flush_bus(this);
    }
    
    /**
     * Set the clock for pipeline. 
     * The clock will be distributed to all the elements managed by the pipeline.
     * <p>MT safe
     * 
     * @param clock The {@link Clock} to use
     * @return true if the clock could be set on the pipeline, false if some 
     * element did not accept the clock. 
     *
     */
    public boolean setClock(Clock clock) {
        return GSTPIPELINE_API.gst_pipeline_set_clock(this, clock);
    }
    
    /**
     * Return the current {@link Clock} used by the pipeline.
     * 
     * @return The {@link Clock} currently in use.
     */
    public Clock getClock() {
        return GSTPIPELINE_API.gst_pipeline_get_clock(this);
    }
    
    /**
     * Force the Pipeline to use the a specific clock.
     * The pipeline will always use the given clock even if new clock 
     * providers are added to this pipeline.
     * <p>MT safe
     * 
     * @param clock The {@link Clock} to use.  If clock is null, all clocking is
     * disabled, and the pipeline will run as fast as possible.
     *      
     */
    public void useClock(Clock clock) {
        GSTPIPELINE_API.gst_pipeline_use_clock(this, clock);
    }
    
    /**
     * Gets the {@link Bus} this pipeline uses for messages.
     * 
     * @return The {@link Bus} that this pipeline uses.
     */
    @Override
    public Bus getBus() {
        return GSTPIPELINE_API.gst_pipeline_get_bus(this);
    }
    
    /**
     * Sets the position in the media stream to time.
     * 
     * @param time The time to change the position to.
     * @return true if seek is successful
     */
    public boolean seek(ClockTime time) {
        return seek(time.convertTo(TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS);
    }
    
    /**
     * Sets the current position in the media stream.
     * 
     * @param time the time to change the position to.
     * @param unit the {@code TimeUnit} the <tt>time</tt> is expressed in.
     * @return true if seek is successful
     */
    public boolean seek(long time, TimeUnit unit) {
        return seek(1.0, Format.TIME, SeekFlags.FLUSH | SeekFlags.KEY_UNIT, 
            SeekType.SET, TimeUnit.NANOSECONDS.convert(time, unit), 
            SeekType.NONE, -1);
    }
    
    /**
     * Seeks to a new position in the media stream.
     * 
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
     * <ttstartType</tt> and <tt>stopType</tt> specify how to adjust the currently configured 
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
     * {@link org.gstreamer.Pipeline#queryPosition queryPosition} and update the playback segment 
     * current position with a {@link SeekType#SET} to the desired position.
     * 
     * @param rate the new playback rate
     * @param format the format of the seek values
     * @param flags the optional seek flags
     * @param startType the type and flags for the new start position
     * @param start the value of the new start position
     * @param stopType the type and flags for the new stop position
     * @param stop the value of the new stop position
     * @return true if seek is successful
     */
    public boolean seek(double rate, Format format, int flags,
            SeekType startType, long start, SeekType stopType, long stop) {
        
        return GSTELEMENT_API.gst_element_seek(this, rate, format, flags, 
            startType, start, stopType, stop);
    }
    
    /**
     * Gets the current position in the media stream in units of time.
     * 
     * @return the ClockTime representing the current position.
     */
    public ClockTime queryPosition() {
        return ClockTime.valueOf(queryPosition(Format.TIME), TimeUnit.NANOSECONDS);
    }
    
    /**
     * Gets the current position in the media stream.
     * 
     * @param unit the {@code TimeUnit} to return the position in terms of.
     * @return a time value representing the current position in units of <tt>unit</tt>.
     */
    public long queryPosition(TimeUnit unit) {
        return unit.convert(queryPosition(Format.TIME), TimeUnit.NANOSECONDS);
    }
    
    /**
     * Gets the current position in terms of the specified {@link Format}.
     * 
     * @param format The {@link Format} to return the position in.
     * @return The current position or -1 if the query failed. 
     */
    public long queryPosition(Format format) {
        long[] pos = { 0 };
        return GSTELEMENT_API.gst_element_query_position(this, format, pos) ? pos[0] : -1L;
    }
    
    /**
     * Gets the time duration of the current media stream.
     * 
     * @return The total duration of the current media stream.
     */
    public ClockTime queryDuration() {
        return ClockTime.valueOf(queryDuration(Format.TIME), TimeUnit.NANOSECONDS);
    }
    
    /**
     * Gets the time duration of the current media stream.
     * 
     * @param unit the {@code TimeUnit} to return the position in.
     * @return The total duration of the current media stream.
     */
    public long queryDuration(TimeUnit unit) {
        return unit.convert(queryDuration(Format.TIME), TimeUnit.NANOSECONDS);
    }

    /**
     * Gets the duration of the current media stream in terms of the specified {@link Format}.
     * 
     * @param format the {@code Format} to return the duration in.
     * @return The total duration of the current media stream.
     */
    public long queryDuration(Format format) {
        long[] dur = { 0 };
        return GSTELEMENT_API.gst_element_query_duration(this, format, dur) ? dur[0] : -1L;
    }

    /**
     * Gets the {@link Segment} for the current media stream in terms of the specified {@link Format}.
     *
     * @return The information regarding the current {@code Segment}.
     */
    public Segment querySegment() {
        return querySegment(Format.TIME);
    }

    /**
     * Gets the {@link Segment} for the current media stream in terms of the specified {@link Format}.
     *
     * @param format the {@code Format} to return the segment in.
     * @return The information regarding the current {@code Segment}.
     */
    public Segment querySegment(Format format) {
        Query qry = GSTQUERY_API.gst_query_new_segment(format);
        GSTELEMENT_API.gst_element_query(this, qry);
        double[] rate = { 0.0D };
        Format[] fmt = { Format.UNDEFINED };
        long[] start_value = { 0 };
        long[] stop_value = { 0 };
        GSTQUERY_API.gst_query_parse_segment(qry, rate, fmt, start_value, stop_value);
        return new Segment(rate[0], fmt[0], start_value[0], stop_value[0]);
    }
}
