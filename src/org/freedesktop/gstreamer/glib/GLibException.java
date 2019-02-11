/* 
 * Copyright (c) 2019 Neil C Smith
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

package org.freedesktop.gstreamer.glib;

/**
 * Thrown when a gstreamer error occurs.
 */
public class GLibException extends RuntimeException {

    /**
     * Creates a new instance of <code>GLibException</code> without detail message.
     */
    public GLibException() {
    }

    /**
     * Constructs an instance of <code>GLibException</code> with the specified detail message.
     * 
     * @param msg the detail message.
     */
    public GLibException(String msg) {
        super(msg);
    }
    public GLibException(GError error) {
        super(error.getMessage());
    }
}
