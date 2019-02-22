/* 
 * Copyright (C) 2019 Neil C Smith
 * Copyright (C) 2008 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wtay@chello.be>
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

package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * The return value of a clock operation.
 */
public enum ClockReturn {
    /** The operation succeeded. */
    OK,
    /** The operation was scheduled too late. */
    EARLY,
    /** The clockID was unscheduled */
    UNSCHEDULED,
    /** The ClockID is busy */
    BUSY,
    /** A bad time was provided to a function. */
    BADTIME,
    /** An error occured */
    ERROR,
    /** Operation is not supported */
    @DefaultEnumValue
    UNSUPPORTED,
    /** The ClockID is done waiting */
    DONE
}
