/*
 * Copyright (c) 2015 Christophe Lafolet
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

import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.Message;
import org.freedesktop.gstreamer.lowlevel.GstMessageAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

public class NeedContextMessage extends Message {

    private static interface API extends GstMessageAPI {
    	@CallerOwnsReturn Pointer ptr_gst_message_new_need_context(GstObject source, String context_type);
    }
    private static final API gst = GstNative.load(API.class);

    /**
     * Creates a new need context message.
     *
     * @param init internal initialization data.
     */
    public NeedContextMessage(Initializer init) {
        super(init);
    }

    /**
     * Creates a need context message.
     * @param src the object originating the message.
     * @param context_type The context type that is needed
     *
     */
    public NeedContextMessage(GstObject src, String context_type) {
        this(NativeObject.initializer(NeedContextMessage.gst.ptr_gst_message_new_need_context(src, context_type)));
    }

    public String getContextType() {
    	String[] contextType = new String[1];
    	boolean isOk = NeedContextMessage.gst.gst_message_parse_context_type(this, contextType);
    	return isOk ? contextType[0] : null;
    }
}
