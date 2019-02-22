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
package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;

/**
 * A base class for sink elements.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstBaseSink.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstBaseSink.html</a>
 * <p>
 */
public class BaseSink extends Element {

    public static final String GTYPE_NAME = "GstBaseSink";

    protected BaseSink(Initializer init) {
        super(init);
    }

    // @TODO review - all properties are available by set / get mechanism
    // some other methods require the preroll lock, but we don't have a way to
    // access. Methods for subclasses should probably be protected.
    
//    public FlowReturn doPreroll(MiniObject obj) {
//        return BASESINK_API.gst_base_sink_do_preroll(this, obj);
//    }
//
//    public FlowReturn waitPreroll() {
//        return BASESINK_API.gst_base_sink_wait_preroll(this);
//    }
//
//    public void setSync(boolean sync) {
//        BASESINK_API.gst_base_sink_set_sync(this, sync);
//    }
//
//    public boolean isSync() {
//        return BASESINK_API.gst_base_sink_get_sync(this);
//    }
//
//    public void setMaximumLateness(long lateness, TimeUnit units) {
//        BASESINK_API.gst_base_sink_set_max_lateness(this, units.toNanos(lateness));
//    }
//
//    public long getMaximumLateness(TimeUnit units) {
//        return units.convert(BASESINK_API.gst_base_sink_get_max_lateness(this), TimeUnit.NANOSECONDS);
//    }
//
//    public void setQOSEnabled(boolean qos) {
//        BASESINK_API.gst_base_sink_set_qos_enabled(this, qos);
//    }
//
//    public boolean isQOSEnabled() {
//        return BASESINK_API.gst_base_sink_is_qos_enabled(this);
//    }
//
//    public void enableAsync(boolean enabled) {
//        BASESINK_API.gst_base_sink_set_async_enabled(this, enabled);
//    }
//
//    public boolean isAsync() {
//        return BASESINK_API.gst_base_sink_is_async_enabled(this);
//    }
//
//    public void setTsOffset(long offset) {
//        BASESINK_API.gst_base_sink_set_ts_offset(this, offset);
//    }
//
//    public long getTsOffset() {
//        return BASESINK_API.gst_base_sink_get_ts_offset(this);
//    }
//
//    public Buffer getLastBuffer() {
//        return BASESINK_API.gst_base_sink_get_last_buffer(this);
//    }
//
//    public void enableLastBuffer(boolean enable) {
//        BASESINK_API.gst_base_sink_set_last_buffer_enabled(this, enable);
//    }
//
//    public boolean isLastBufferEnabled() {
//        return BASESINK_API.gst_base_sink_is_last_buffer_enabled(this);
//    }
//
//    public boolean queryLatency(boolean live, boolean upstream_live, ClockTime min_latency, ClockTime max_latency) {
//        return BASESINK_API.gst_base_sink_query_latency(this, live, upstream_live, min_latency, max_latency);
//    }
//
//    public ClockTime getLatency() {
//        return BASESINK_API.gst_base_sink_get_latency(this);
//    }
//
//    public void setRenderDelay(ClockTime delay) {
//        BASESINK_API.gst_base_sink_set_render_delay(this, delay);
//    }
//
//    public ClockTime getRenderDelay() {
//        return BASESINK_API.gst_base_sink_get_render_delay(this);
//    }
//
//    public void setBlocksize(int blocksize) {
//        BASESINK_API.gst_base_sink_set_blocksize(this, blocksize);
//    }
//
//    public int getBlocksize() {
//        return BASESINK_API.gst_base_sink_get_blocksize(this);
//    }
//
//    public ClockReturn waitClock(ClockTime time, /* GstClockTimeDiff */ Pointer jitter) {
//        return BASESINK_API.gst_base_sink_wait_clock(this, time, jitter);
//    }
//
//    public FlowReturn waitEOS(ClockTime time, /* GstClockTimeDiff */ Pointer jitter) {
//        return BASESINK_API.gst_base_sink_wait_eos(this, time, jitter);
//    }

//    @TODO work out strategy for overriding methods
//    
//    /**
//     * Signal emitted when this {@link BaseSink} received a {@link ProposeAllocation} query.
//     *
//     * @see #setProposeAllocationHandler(ProposeAllocationHandler)
//     */
//    public static interface ProposeAllocationHandler {
//    	public boolean proposeAllocation(BaseSink sink, AllocationQuery query);    	
//    }
//    
//    /**
//     * Set a handler for the {@link ProposeAllocation} query on this sink
//     *
//     * @param handler The handler to be called when a {@link ProposeAllocation} is received.
//     */
//    public void setProposeAllocationHandler(final ProposeAllocationHandler handler) {
//        GstBaseSinkStruct struct = new GstBaseSinkStruct(handle());
//        struct.readField("element");
//        GstBaseSinkClass basesinkClass = new GstBaseSinkClass(struct.element.object.object.g_type_instance.g_class.getPointer());
//        basesinkClass.propose_allocation = new ProposeAllocation() {
//			@Override
//			public boolean callback(BaseSink sink, AllocationQuery query) {
//				return handler.proposeAllocation(sink, query);
//			}
//        };
//        basesinkClass.writeField("propose_allocation");
//    }
}
