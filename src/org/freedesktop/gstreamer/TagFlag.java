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
 * Extra tag flags used when registering tags.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstTagList.html#GstTagFlag"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstTagList.html#GstTagFlag</a>
 * <p>
 */
public enum TagFlag implements NativeEnum<TagFlag> {
    /** Undefined flag. */
    @DefaultEnumValue
    UNDEFINED(0),
    /** Tag is meta data. */
    META(1),
    /** Tag is encoded. */
    ENCODED(2),
    /** Tag is decoded. */
    DECODED(3),
    /** Number of tag flags. */
    COUNT(4);
    
    private final int value;
    
    private TagFlag(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }
}
