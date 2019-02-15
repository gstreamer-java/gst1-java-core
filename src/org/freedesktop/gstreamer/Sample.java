/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2014 Tom Greenwood <tgreenwood@cafex.com>
 * Copyright (c) 2007, 2008 Wayne Meissner
 * Copyright (C) 2004 Wim Taymans <wim@fluendo.com>
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

import static org.freedesktop.gstreamer.lowlevel.GstSampleAPI.GSTMESSAGE_API;

/**
 * A Sample is a small object containing data, a type, timing and extra
 * arbitrary information.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstSample.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstSample.html</a>
 *
 */
public class Sample extends MiniObject {

    public static final String GTYPE_NAME = "GstSample";

    Sample(Initializer init) {
        super(init);
    }

    /**
     * Get the {@link Caps} associated with sample, or NULL when there is no
     * caps. The caps remain valid as long as sample is valid.
     *
     * @return caps of sample or NULL when there is no caps.
     */
    public Caps getCaps() {
        return GSTMESSAGE_API.gst_sample_get_caps(this);
    }

    /**
     * Get the {@link Buffer} associated with sample, or NULL when there is no
     * buffer.
     * <b>The buffer remains valid as long as sample is valid.</b>
     *
     * @return buffer of sample or NULL when there is no buffer.
     */
    public Buffer getBuffer() {
        return GSTMESSAGE_API.gst_sample_get_buffer(this);
    }

}
