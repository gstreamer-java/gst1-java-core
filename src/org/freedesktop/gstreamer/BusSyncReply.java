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

import org.freedesktop.gstreamer.glib.NativeEnum;

/**
 * The result values for a GstBusSyncHandler.
 */
public enum BusSyncReply implements NativeEnum<BusSyncReply> {
    
    /** Drop the {@link Message} */
    DROP(0),
    /** Pass the {@link Message} to the async queue */
    PASS(1),
    /** Pass {@link Message} to async queue, continue if message is handled */
    ASYNC(2);
    
    private final int value;
    
    BusSyncReply(int value) {
        this.value = value;
    }
    
    /**
     * Gets the integer value of the enum.
     * @return The integer value for this enum.
     */
    @Override
    public int intValue() {
        return value;
    }
}
