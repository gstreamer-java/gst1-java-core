/* 
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

package org.gstreamer;

/**
 * The possible return values from a state change function. 
 * <p>
 * Only {@link StateChangeReturn#FAILURE} is a real failure.
 */
public enum StateChangeReturn {
    /** The state change failed. */
    FAILURE,
    /** The state change succeeded. */
    SUCCESS,
    /** The state change will happen asynchronously. */
    ASYNC,
    /**
     * The state change succeeded but the {@link Element} cannot produce data in 
     * {@link State#PAUSED}. This typically happens with live sources.
     */
    NO_PREROLL;
}
