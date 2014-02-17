/* 
 * Copyright (C) 2007 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wim.taymans@chello.be>
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

import org.gstreamer.lowlevel.IntegerEnum;
import org.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * The result of passing data to a pad.
 */
public enum FlowReturn implements IntegerEnum {
    /** Resend buffer, possibly with new caps (not send yet). */
    RESEND(1),
    /** Data passing was ok. */
    OK(0),

    /** {@link Pad} is not linked. */
    NOT_LINKED(-1),
    /** {@link Pad} is in wrong state. */
    WRONG_STATE(-2),
    /** Did not expect anything, like after EOS. */
    UNEXPECTED(-3),
    /** {@link Pad} is in not negotiated. */
    NOT_NEGOTIATED(-4),
    
    /**
     * Some (fatal) error occured. Element generating this error should post 
     * an error message with more details.
     */
    ERROR(-5),
    
    /** This operation is not supported. */
    NOT_SUPPORTED(-6),
    
    /** The default enum value used when no other value matches the native value */
    @DefaultEnumValue
    __UNKNOWN_NATIVE_VALUE(~0);

    FlowReturn(int value) {
        this.value = value;
    }
    
    /**
     * Gets the integer value of the enum.
     * @return The integer value for this enum.
     */
    public int intValue() {
        return value;
    }
    private int value;
}
