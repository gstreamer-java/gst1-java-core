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

import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.TagList;
import com.sun.jna.ptr.PointerByReference;
import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstMessageAPI.GSTMESSAGE_API;

/**
 * This message is posted by elements that have discovered new tags.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-tag"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html#gst-message-new-tag</a>
 * <p>
 */
public class TagMessage extends Message {

    /**
     * Creates a new Tag message.
     *
     * @param init internal initialization data.
     */
    TagMessage(Initializer init) {
        super(init);
    }

    /**
     * Creates a new Tag message.
     *
     * @param src The object originating the message.
     * @param tagList the tag list for this message.
     * <p>
     * <b> Note: </b> the message takes ownership of the taglist, so do not use
     * it again after adding it to this message.
     */
    public TagMessage(GstObject src, TagList tagList) {
        this(Natives.initializer(GSTMESSAGE_API.ptr_gst_message_new_tag(src, tagList)));
    }

    /**
     * Gets the list of tags contained in this message.
     *
     * @return the list of tags in this message.
     */
    public TagList getTagList() {
        PointerByReference list = new PointerByReference();
        GSTMESSAGE_API.gst_message_parse_tag(this, list);
        return Natives.objectFor(list.getValue(), TagList.class, false, true);
    }
}
