/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.glib.NativeFlags;

/**
 * Flags for {@link MiniObject }
 */
public enum MiniObjectFlags implements NativeFlags<MiniObjectFlags> {
    /**
     * the object can be locked and unlocked with gst_mini_object_lock() and
     * gst_mini_object_unlock()
     *
     */
    LOCKABLE(1 << 0),
    /**
     * the object is permanently locked in READONLY mode. Only read locks can be
     * performed on the object.
     */
    LOCK_READONLY(1 << 1),
    /**
     * the object is expected to stay alive even after gst_deinit() has been
     * called and so should be ignored by leak detection tools. (Since 1.10)
     */
    MAY_BE_LEAKED(1 << 2),
    /**
     * The last valid MiniObject flag
     */
    LAST(1 << 4);

    private final int value;
    
    private MiniObjectFlags(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }
}
