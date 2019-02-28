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

import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.glib.Natives;

import static org.freedesktop.gstreamer.lowlevel.GstEventAPI.GSTEVENT_API;

/**
 * A buffersize event. The event is sent downstream and notifies elements that
 * they should provide a buffer of the specified dimensions.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-buffer-size"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-buffer-size</a>
 * <p>
 * When the async flag is set, a thread boundary is preferred.
 */
public class BufferSizeEvent extends Event {

    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    BufferSizeEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new buffersize event.
     * <p>
     * The event is sent downstream and notifies elements that they should
     * provide a buffer of the specified dimensions.
     * <p>
     * When the <tt>async</tt> flag is set, a thread boundary is preferred.
     *
     * @param format buffer format
     * @param minsize minimum buffer size
     * @param maxsize maximum buffer size
     * @param async thread behaviour
     */
    public BufferSizeEvent(Format format, long minsize, long maxsize, boolean async) {
        super(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_buffer_size(format, minsize, maxsize, async)));
    }

    /**
     * Gets the format of the buffersize event.
     *
     * @return the format.
     */
    public Format getFormat() {
        Format[] format = new Format[1];
        GSTEVENT_API.gst_event_parse_buffer_size(this, format, null, null, null);
        return format[0];
    }

    /**
     * Gets the minimum buffer size.
     *
     * @return the minimum buffer size.
     */
    public long getMinimumSize() {
        long[] size = {0};
        GSTEVENT_API.gst_event_parse_buffer_size(this, null, size, null, null);
        return size[0];
    }

    /**
     * Gets the maximum buffer size.
     *
     * @return the maximum buffer size.
     */
    public long getMaximumSize() {
        long[] size = {0};
        GSTEVENT_API.gst_event_parse_buffer_size(this, null, null, size, null);
        return size[0];
    }

    /**
     * Gets the preferred thread behaviour.
     *
     * @return <tt>true</tt> if a thread boundary is preferred.
     */
    public boolean isAsync() {
        boolean[] async = {false};
        GSTEVENT_API.gst_event_parse_buffer_size(this, null, null, null, async);
        return async[0];
    }
}
