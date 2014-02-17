/* 
 * Copyright (c) 2010 DHoyt <david.g.hoyt@gmail.com>
 * Copyright (c) 2010 Levente Farkas
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

package org.gstreamer.event;

import org.gstreamer.Event;
import org.gstreamer.Format;
import org.gstreamer.Structure;
import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.Pointer;

/**
 * StepEvent. The purpose of the step event is to instruct a sink to skip amount (expressed in format) of media. 
 * It can be used to implement stepping through the video frame by frame or for doing fast trick modes.
 * A rate of <= 0.0 is not allowed, pause the pipeline or reverse the playback direction of the pipeline 
 * to get the same effect.
 * The flush flag will clear any pending data in the pipeline before starting the step operation.
 * The intermediate flag instructs the pipeline that this step operation is part of a larger step operation.
 */
public class StepEvent extends Event {
    private static interface API extends com.sun.jna.Library {
    	Pointer gst_event_new_custom(int type, @Invalidate Structure structure);
    	Pointer ptr_gst_event_new_step(Format format, long amount, double rate, boolean flush, boolean intermediate);
    }
    private static final API gst = GstNative.load(API.class);
    
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    public StepEvent(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new StepEvent event. 
	 * @param format the format of amount
	 * @param amount the amount of data to step
	 * @param rate the step rate
	 * @param flush flushing steps
	 * @param intermediate intermediate steps
     */
    public StepEvent(Format format, long amount, double rate, boolean flush, boolean intermediate) {
    	super(initializer(gst.ptr_gst_event_new_step(format, amount, rate, flush, intermediate)));
    }
}