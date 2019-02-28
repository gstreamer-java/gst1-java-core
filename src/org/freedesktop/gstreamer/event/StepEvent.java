/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer.event;

import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstEventAPI.GSTEVENT_API;

/**
 * StepEvent.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-step"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-step</a>
 * <p>
 * The purpose of the step event is to instruct a sink to skip amount (expressed
 * in format) of media. It can be used to implement stepping through the video
 * frame by frame or for doing fast trick modes. A rate of {@literal <= 0.0} is
 * not allowed, pause the pipeline or reverse the playback direction of the
 * pipeline to get the same effect.
 * <p>
 * The flush flag will clear any pending data in the pipeline before starting
 * the step operation.
 * <p>
 * The intermediate flag instructs the pipeline that this step operation is part
 * of a larger step operation.
 */
public class StepEvent extends Event {

    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    StepEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new StepEvent event.
     *
     * @param format the format of amount
     * @param amount the amount of data to step
     * @param rate the step rate
     * @param flush flushing steps
     * @param intermediate intermediate steps
     */
    public StepEvent(Format format, long amount, double rate, boolean flush, boolean intermediate) {
        super(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_step(format, amount, rate, flush, intermediate)));
    }
}
