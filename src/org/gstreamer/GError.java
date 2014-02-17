/* 
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

package org.gstreamer;

import org.gstreamer.lowlevel.GstAPI.GErrorStruct;

import static org.gstreamer.lowlevel.GlibAPI.GLIB_API;

/**
 * Base gstreamer error type.
 */
public class GError {
    /** 
     * Creates a new instance of GError
     * <p>
     * <b> Note: </b> This takes ownership of the passed in GErrorStruct.
     * @param error 
     */
    GError(GErrorStruct error) {
        code = error.getCode();
        message = error.getMessage();
        GLIB_API.g_error_free(error);
    }
    
    /**
     * Gets a string representation of this error.
     * 
     * @return a string representing the error.
     */
    public String getMessage() {
        return message;
    }
    /**
     * Gets a numeric code representing this error.
     * 
     * @return an integer code.
     */
    public final int getCode() {
        return code;
    }
    
    private final int code;
    private final String message;
}
