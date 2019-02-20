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

package org.freedesktop.gstreamer.message;

import org.freedesktop.gstreamer.lowlevel.GstAPI.GErrorStruct;
import static org.freedesktop.gstreamer.lowlevel.GstMessageAPI.GSTMESSAGE_API;

/**
 * This message is posted by element when a fatal event occurs.  
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-error"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-error</a>
 * <p>
 * The pipeline will probably (partially) stop. The application
 * receiving this message should stop the pipeline.
 */
public class ErrorMessage extends GErrorMessage {

    /**
     * Creates a new error message.
     * 
     * @param init internal initialization data.
     */
    ErrorMessage(Initializer init) {
        super(init);
    }
    
    /**
     * Retrieves the GError structure contained in this message.
     * 
     * @return the GError contained in this message.
     */
    @Override
    GErrorStruct parseMessage() {
        GErrorStruct[] err = { null };
        GSTMESSAGE_API.gst_message_parse_error(this, err, null);
        return err[0];
    }
}
