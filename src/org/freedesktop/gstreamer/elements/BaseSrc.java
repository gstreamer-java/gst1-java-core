/* 
 * Copyright (c) 2019 Neil C Smith
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

import org.freedesktop.gstreamer.Element;

/**
 * A base class for source elements.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstBaseSrc.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstBaseSrc.html</a>
 * <p>
 */
public class BaseSrc extends Element {
    
    public static final String GTYPE_NAME = "GstBaseSrc";

    protected BaseSrc(Initializer init) {
        super(init);
    }

    // @TODO review - all properties are available by set / get mechanism
    // some other methods require the stream lock, but we don't have a way to
    // access. Methods for subclasses should probably be protected.
    
//    public FlowReturn waitPlaying() {
//    	return gst().gst_base_src_wait_playing(this);
//    }
//    public void setLive(boolean live) {
//    	gst().gst_base_src_set_live(this, live);
//    }
//    public boolean isLive() {
//        return gst().gst_base_src_is_live(this);
//    }
//    public void setFormat(Format format) {
//    	gst().gst_base_src_set_format(this, format);
//    }
//    public boolean queryLatency(boolean[] live, ClockTime[] min_latency, ClockTime[] max_latency) {
//    	return gst().gst_base_src_query_latency(this, live, min_latency, max_latency);
//    }
//    public void setBlocksize(long blocksize) {
//    	gst().gst_base_src_set_blocksize(this, blocksize);
//    }
//    public long getBlocksize() {
//    	return gst().gst_base_src_get_blocksize(this);
//    }
//    public void setTimestamp(boolean timestamp) {
//    	gst().gst_base_src_set_do_timestamp(this, timestamp);
//    }
//    public boolean getTimestamp() {
//    	return gst().gst_base_src_get_do_timestamp(this);
//    }
//    public boolean newSeamlessSegment(long start, long stop, long position) {
//    	return gst().gst_base_src_new_seamless_segment(this, start, stop, position);
//    }
//    
}
