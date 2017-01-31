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

package org.freedesktop.gstreamer.elements;

import java.util.concurrent.TimeUnit;

import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.ClockReturn;
import org.freedesktop.gstreamer.ClockTime;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.FlowReturn;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.lowlevel.BaseSinkAPI;
import org.freedesktop.gstreamer.lowlevel.BaseSinkAPI.GstBaseSinkClass;
import org.freedesktop.gstreamer.lowlevel.BaseSinkAPI.GstBaseSinkStruct;
import org.freedesktop.gstreamer.lowlevel.BaseSinkAPI.ProposeAllocation;
import org.freedesktop.gstreamer.lowlevel.GstAPI;
import org.freedesktop.gstreamer.query.AllocationQuery;

import com.sun.jna.Pointer;

public class BaseSink extends Element {
    public static final String GTYPE_NAME = "GstBaseSink";

    private static final BaseSinkAPI gst() { return BaseSinkAPI.BASESINK_API; }
	
    public BaseSink(Initializer init) {
        super(init);
    }
    
    public FlowReturn preroll(MiniObject obj) {
    	return gst().gst_base_sink_do_preroll(this, obj);
    }
    public FlowReturn waitPreroll() {
    	return gst().gst_base_sink_wait_preroll(this);
    }
    public void setSync(boolean sync) {
    	gst().gst_base_sink_set_sync(this, sync);
    }
    public boolean isSync() {
        return gst().gst_base_sink_get_sync(this);
    }
    public void setMaximumLateness(long lateness, TimeUnit units) {
    	gst().gst_base_sink_set_max_lateness(this, units.toNanos(lateness));
    }
    public long getMaximumLateness(TimeUnit units) {
        return units.convert(gst().gst_base_sink_get_max_lateness(this),TimeUnit.NANOSECONDS);
    }
    public void setQOSEnabled(boolean qos) {
    	gst().gst_base_sink_set_qos_enabled(this, qos);
    }
    public boolean isQOSEnabled() {
        return gst().gst_base_sink_is_qos_enabled(this);
    }
    public void enableAsync(boolean enabled) {
    	gst().gst_base_sink_set_async_enabled(this, enabled);
    }
    public boolean isAsync() {
    	return gst().gst_base_sink_is_async_enabled(this);
    }
    public void setTsOffset(long offset) {
    	gst().gst_base_sink_set_ts_offset(this, offset);
    }
    public long getTsOffset() {
    	return gst().gst_base_sink_get_ts_offset(this);
    }
    public Buffer getLastBuffer() {
    	return gst().gst_base_sink_get_last_buffer(this);
    }
    public void enableLastBuffer(boolean enable) {
    	gst().gst_base_sink_set_last_buffer_enabled(this, enable);
    }
    public boolean isLastBufferEnabled() {
    	return gst().gst_base_sink_is_last_buffer_enabled(this);
    }
    public boolean queryLatency(boolean live, boolean upstream_live, ClockTime min_latency, ClockTime max_latency) {
    	return gst().gst_base_sink_query_latency(this, live, upstream_live, min_latency, max_latency);
    }
    public ClockTime getLatency() {
    	return gst().gst_base_sink_get_latency(this);
    }
    public void setRenderDelay(ClockTime delay) {
    	gst().gst_base_sink_set_render_delay(this, delay);
    }
    public ClockTime getRenderDelay() {
    	return gst().gst_base_sink_get_render_delay(this);
    }
    public void setBlocksize(int blocksize) {
    	gst().gst_base_sink_set_blocksize(this, blocksize);
    }
    public int getBlocksize() {
    	return gst().gst_base_sink_get_blocksize(this);
    }
    public ClockReturn waitClock(ClockTime time, /* GstClockTimeDiff */ Pointer jitter) {
    	return gst().gst_base_sink_wait_clock(this, time, jitter);
    }
    public FlowReturn waitEOS(ClockTime time, /* GstClockTimeDiff */ Pointer jitter) {
    	return gst().gst_base_sink_wait_eos(this, time, jitter);
    }
    
    /**
     * Signal emitted when this {@link BaseSink} has a {@link Buffer} ready.
     * 
     * @see #connect(HANDOFF)
     * @see #disconnect(HANDOFF)
     */
    public static interface HANDOFF {
        /**
         * Called when an {@link BaseSink} has a {@link Buffer} ready.
         * 
         * @param sink the sink which has a buffer ready.
         * @param buffer the buffer for the data.
         * @param pad the pad on the element.
         */
        public void handoff(BaseSink sink, Buffer buffer, Pad pad);
    }

    private static class HANDOFFCallback implements GstAPI.GstCallback {
    	final HANDOFF listener;
    	public HANDOFFCallback(final HANDOFF listener) {
    		this.listener = listener;
    		//Native.setCallbackThreadInitializer(this, new CallbackThreadInitializer(true, false, "BaseSink Handoff"));
    	}
        @SuppressWarnings("unused")
        public void callback(BaseSink sink, Buffer buffer, Pad pad) {
            listener.handoff(sink, buffer, pad);
//          if (last)
//        	Native.detach(true);
        }    	
    }
    /**
     * Add a listener for the <code>handoff</code> signal on this sink
     * 
     * @param listener The listener to be called when a {@link Buffer} is ready.
     */
    public void connect(final HANDOFF listener) {
        connect(HANDOFF.class, listener, new HANDOFFCallback(listener));
    }
    /**
     * Remove a listener for the <code>handoff</code> signal
     * 
     * @param listener The listener that was previously added.
     */
    public void disconnect(HANDOFF listener) {
        disconnect(HANDOFF.class, listener);
    }   
    
    private static class PrerollHandoff implements GstAPI.GstCallback {
    	final PREROLL_HANDOFF listener;
    	public PrerollHandoff(final PREROLL_HANDOFF listener) {
    		this.listener = listener;
    		//Native.setCallbackThreadInitializer(this, new CallbackThreadInitializer(true, false, "BaseSink Preroll Handoff"));
    	}
        @SuppressWarnings("unused")
        public void callback(BaseSink sink, Buffer buffer, Pad pad) {
            listener.prerollHandoff(sink, buffer, pad);
//          if (last)
//        	Native.detach(true);
        }
    }
    /**
     * Signal emitted when this {@link BaseSink} has a {@link Buffer} ready.
     *
     * @see #connect(PREROLL_HANDOFF)
     * @see #disconnect(PREROLL_HANDOFF)
     */
    public static interface PREROLL_HANDOFF {
        /**
         * Called when a {@link BaseSink} has a {@link Buffer} ready.
         *
         * @param sink the sink instance.
         * @param buffer the buffer that just has been received.
         * @param pad the pad that received it.
         */
        public void prerollHandoff(BaseSink sink, Buffer buffer, Pad pad);
    }
    /**
     * Add a listener for the <code>preroll-handoff</code> signal.
     *
     * @param listener The listener to be called when a {@link Buffer} is ready.
     */
    public void connect(final PREROLL_HANDOFF listener) {
        connect(PREROLL_HANDOFF.class, listener, new PrerollHandoff(listener));
    }
    /**
     * Remove a listener for the <code>preroll-handoff</code> signal.
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(PREROLL_HANDOFF listener) {
        disconnect(PREROLL_HANDOFF.class, listener);
    }
    
    /**
     * Signal emitted when this {@link BaseSink} received a {@link ProposeAllocation} query.
     *
     * @see #setProposeAllocationHandler(ProposeAllocationHandler)
     */
    public static interface ProposeAllocationHandler {
    	public boolean proposeAllocation(BaseSink sink, AllocationQuery query);    	
    }
    
    /**
     * Set a handler for the {@link ProposeAllocation} query on this sink
     *
     * @param handler The handler to be called when a {@link ProposeAllocation} is received.
     */
    public void setProposeAllocationHandler(final ProposeAllocationHandler handler) {
        GstBaseSinkStruct struct = new GstBaseSinkStruct(handle());
        struct.readField("element");
        GstBaseSinkClass basesinkClass = new GstBaseSinkClass(struct.element.object.object.g_type_instance.g_class.getPointer());
        basesinkClass.propose_allocation = new ProposeAllocation() {
			@Override
			public boolean callback(BaseSink sink, AllocationQuery query) {
				return handler.proposeAllocation(sink, query);
			}
        };
        basesinkClass.writeField("propose_allocation");
    }

}
