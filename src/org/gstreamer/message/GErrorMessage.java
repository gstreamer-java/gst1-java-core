/* 
 * Copyright (C) 2008 Wayne Meissner
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


package org.gstreamer.message;

import org.gstreamer.Message;
import org.gstreamer.lowlevel.GlibAPI;
import org.gstreamer.lowlevel.GstAPI;
import org.gstreamer.lowlevel.GstAPI.GErrorStruct;

/**
 * Base class for ERROR, WARNING and INFO messages.
 */
abstract public class GErrorMessage extends Message {
    
    /**
     * Creates a new GError message.
     * @param init internal initialization data.
     */
    GErrorMessage(Initializer init) {
        super(init);
    }
    abstract GstAPI.GErrorStruct parseMessage();
    
    /**
     * Gets the error code from this message.
     * 
     * @return the error code.
     */
    public int getCode() {
        GErrorStruct err = parseMessage();
        if (err == null) {
            throw new NullPointerException("Could not parse message");
        }
        int code = err.code;
        GlibAPI.GLIB_API.g_error_free(err);
        return code;
    }
    
    /**
     * Gets the message contained in this message.
     * 
     * @return the message contained in this message.
     */
    public String getMessage() {
        GErrorStruct err = parseMessage();
        if (err == null) {
            throw new NullPointerException("Could not parse message");
        }
        String message = err.getMessage();
        GlibAPI.GLIB_API.g_error_free(err);
        return message;
    }

}
