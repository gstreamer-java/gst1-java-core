/*
 * Copyright (c) 2009 Levente Farkas
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

import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.ClockTime;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.FlowReturn;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.lowlevel.BaseSrcAPI;
import org.freedesktop.gstreamer.lowlevel.GstAPI;

public class BaseSrc extends Element {
    public static final String GTYPE_NAME = "GstBaseSrc";

    private static final BaseSrcAPI gst = BaseSrcAPI.BASESRC_API;

    public BaseSrc(Initializer init) {
        super(init);
    }

    public FlowReturn waitPlaying() {
    	return BaseSrc.gst.gst_base_src_wait_playing(this);
    }
    public void setLive(boolean live) {
    	BaseSrc.gst.gst_base_src_set_live(this, live);
    }
    public boolean isLive() {
        return BaseSrc.gst.gst_base_src_is_live(this);
    }
    public void setFormat(Format format) {
    	BaseSrc.gst.gst_base_src_set_format(this, format);
    }
    public boolean queryLatency(boolean[] live, ClockTime[] min_latency, ClockTime[] max_latency) {
    	return BaseSrc.gst.gst_base_src_query_latency(this, live, min_latency, max_latency);
    }
    public void setBlocksize(long blocksize) {
    	BaseSrc.gst.gst_base_src_set_blocksize(this, blocksize);
    }
    public long getBlocksize() {
    	return BaseSrc.gst.gst_base_src_get_blocksize(this);
    }
    public void setTimestamp(boolean timestamp) {
    	BaseSrc.gst.gst_base_src_set_do_timestamp(this, timestamp);
    }
    public boolean getTimestamp() {
    	return BaseSrc.gst.gst_base_src_get_do_timestamp(this);
    }
    public boolean newSeamlessSegment(long start, long stop, long position) {
    	return BaseSrc.gst.gst_base_src_new_seamless_segment(this, start, stop, position);
    }

    /**
     * Signal emitted when this {@link BaseSrc} has a {@link Buffer} ready.
     *
     * @see #connect(HANDOFF)
     * @see #disconnect(HANDOFF)
     */
    public static interface HANDOFF {
        /**
         * Called when an {@link BaseSrc} has a {@link Buffer} ready.
         *
         * @param src the source which has a buffer ready.
         * @param buffer the buffer for the data.
         * @param pad the pad on the element.
         */
        public void handoff(BaseSrc src, Buffer buffer, Pad pad);
    }

    /**
     * Add a listener for the <code>handoff</code> signal on this source
     *
     * @param listener The listener to be called when a {@link Buffer} is ready.
     */
    public void connect(final HANDOFF listener) {
        this.connect(HANDOFF.class, listener, new GstAPI.GstCallback() {
            @SuppressWarnings("unused")
            public void callback(BaseSrc src, Buffer buffer, Pad pad) {
                listener.handoff(src, buffer, pad);
            }
        });
    }

    /**
     * Remove a listener for the <code>handoff</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(HANDOFF listener) {
        this.disconnect(HANDOFF.class, listener);
    }
}
