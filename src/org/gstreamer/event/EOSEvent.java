/* 
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

package org.gstreamer.event;

import org.gstreamer.Event;
import org.gstreamer.lowlevel.GstNative;

import com.sun.jna.Pointer;

/**
 * End-Of-Stream. No more data is to be expected to follow without a {@link NewSegmentEvent}.
 * <p>
 * The eos event can only travel downstream
 * synchronized with the buffer flow. Elements that receive the EOS
 * event on a pad can return {@link org.gstreamer.FlowReturn#UNEXPECTED} when data after 
 * the EOS event arrives.
 * <p>
 * The EOS event will travel down to the sink elements in the pipeline
 * which will then post the {@link org.gstreamer.Bus.EOS} message on the bus after they have
 * finished playing any buffered data.
 * <p>
 * When all sinks have posted an EOS message, an EOS message is
 * forwarded to the application.
 */
public class EOSEvent extends Event {
    private static interface API extends com.sun.jna.Library {
        Pointer ptr_gst_event_new_eos();
    }
    private static final API gst = GstNative.load(API.class);
    
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
    public EOSEvent(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new EOS event. 
     */
    public EOSEvent() {
        super(initializer(gst.ptr_gst_event_new_eos()));
    }
}