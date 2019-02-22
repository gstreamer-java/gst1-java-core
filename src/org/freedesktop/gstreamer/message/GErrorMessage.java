/* 
 * Copyright (C) 2019 Neil C Smith
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


package org.freedesktop.gstreamer.message;

import org.freedesktop.gstreamer.lowlevel.GstAPI;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GErrorStruct;
import static org.freedesktop.gstreamer.lowlevel.GlibAPI.GLIB_API;

/**
 * Package private base class for ERROR, WARNING and INFO messages.
 */
abstract class GErrorMessage extends Message {
    
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
        GLIB_API.g_error_free(err);
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
        GLIB_API.g_error_free(err);
        return message;
    }

}
