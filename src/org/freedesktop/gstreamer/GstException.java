/* 
 * Copyright (C) 2019 Neil C Smith
 * Copyright (C) 2008 Wayne Meissner
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

import org.freedesktop.gstreamer.glib.GError;
import org.freedesktop.gstreamer.glib.GLibException;

/**
 * Thrown when a GStreamer error occurs.
 */
public class GstException extends GLibException {

    private static final long serialVersionUID = -7413580400835548033L;

    /**
     * Creates a new instance of <code>GstException</code> without detail message.
     */
    public GstException() {
    }


    /**
     * Constructs an instance of <code>GstException</code> with the specified detail message.
     * 
     * @param msg the detail message.
     */
    public GstException(String msg) {
        super(msg);
    }
    
    public GstException(GError error) {
        super(error);
    }
}
