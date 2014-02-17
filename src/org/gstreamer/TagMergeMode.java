/* 
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

package org.gstreamer;

/**
 * The different tag merging modes are basically replace, overwrite and append,
 * but they can be seen from two directions.
 * <p>
 * Given two taglists: A - the one that are supplied to
 * gst_tag_setter_merge_tags() or gst_tag_setter_add_tags() and B - the tags
 * already in the element, how are the tags merged? In the table below this is
 * shown for the cases that a tag exists in the list (A) or does not exists (!A)
 * and combination thereof.
 */
public enum TagMergeMode {
    /** Undefined merge mode. */
    UNDEFINED,
    /** Replace all tags (clear list and append). */
    REPLACE_ALL,
    /** Replace tags */
    REPLACE,
    /** Append tags */
    APPEND,
    /** Prepend tags */
    PREPEND,
    /** Keep existing tags */
    KEEP,
    /** Keep all existing tags */
    KEEP_ALL;
}
