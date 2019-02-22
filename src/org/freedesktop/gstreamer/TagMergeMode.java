/* 
 * Copyright (C) 2019 Neil C Smith
 * Copyright (C) 2007 Wayne Meissner
 * Copyright (C) 2003 Benjamin Otte
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

import org.freedesktop.gstreamer.glib.NativeEnum;
import org.freedesktop.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * The different tag merging modes are basically replace, overwrite and append,
 * but they can be seen from two directions.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstTagList.html#GstTagMergeMode"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstTagList.html#GstTagMergeMode</a>
 * <p>
 * Given two taglists: A - the one that are supplied to
 * gst_tag_setter_merge_tags() or gst_tag_setter_add_tags() and B - the tags
 * already in the element, how are the tags merged? In the table below this is
 * shown for the cases that a tag exists in the list (A) or does not exists (!A)
 * and combination thereof.
 */
public enum TagMergeMode implements NativeEnum<TagMergeMode> {
    /** Undefined merge mode. */
    @DefaultEnumValue
    UNDEFINED(0),
    /** Replace all tags (clear list and append). */
    REPLACE_ALL(1),
    /** Replace tags */
    REPLACE(2),
    /** Append tags */
    APPEND(3),
    /** Prepend tags */
    PREPEND(4),
    /** Keep existing tags */
    KEEP(5),
    /** Keep all existing tags */
    KEEP_ALL(6);

    private final int value;
    
    private TagMergeMode(int value) {
        this.value = value;
    }
    
    @Override
    public int intValue() {
        return value;
    }
}
