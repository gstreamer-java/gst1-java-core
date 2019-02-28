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

import org.freedesktop.gstreamer.TagList;
import org.freedesktop.gstreamer.lowlevel.ReferenceManager;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstEventAPI.GSTEVENT_API;

/**
 * A metadata tag event.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-tag"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstEvent.html#gst-event-new-tag</a>
 * <p>
 * The scope of the taglist specifies if the taglist applies to the complete
 * medium or only to this specific stream. As the tag event is a sticky event,
 * elements should merge tags received from upstream with a given scope with
 * their own tags with the same scope and create a new tag event from it.
 */
public class TagEvent extends Event {

    /**
     * This constructor is for internal use only.
     *
     * @param init initialization data.
     */
    TagEvent(Initializer init) {
        super(init);
    }

    /**
     * Creates a new TagEvent.
     * <p>
     * <b>Note:</b> This constructor takes ownership of the TagList. Attempts to
     * access the TagList after passing it to this constructor will throw an
     * exception.
     *
     * @param taglist the taglist to transmit with the event.
     */
    public TagEvent(TagList taglist) {
        this(Natives.initializer(GSTEVENT_API.ptr_gst_event_new_tag(taglist)));
    }

    /**
     * Gets the {@link TagList} stored in this event.
     * <p>
     * <b>Note:</b> The TagList is owned by the event, so it should only be
     * accessed whilst holding a reference to this TagEvent.
     *
     * @return the TagList stored in this event.
     */
    public TagList getTagList() {
        Pointer[] taglist = new Pointer[1];
        GSTEVENT_API.gst_event_parse_tag(this, taglist);
//        TagList tl = new TagList(taglistInitializer(taglist[0], false, false));
        TagList tl = Natives.objectFor(taglist[0], TagList.class, false, false);
        ReferenceManager.addKeepAliveReference(tl, this);
        return tl;
    }
//    private static Initializer taglistInitializer(Pointer ptr, boolean needRef, boolean ownsHandle) {
//        if (ptr == null) {
//            throw new IllegalArgumentException("Invalid native pointer");
//        }
//        return new Initializer(ptr, needRef, ownsHandle);
//    }
}
