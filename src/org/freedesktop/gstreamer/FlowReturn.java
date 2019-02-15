/* 
 * Copyright (C) 2019 Neil C Smith
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

package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.glib.NativeEnum;
import org.freedesktop.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * The result of passing data to a pad.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPad.html#GstFlowReturn"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPad.html#GstFlowReturn</a>
 * <p>
 */
public enum FlowReturn implements NativeEnum<FlowReturn> {

    /** Data passing was ok. */
    // @TODO need to map custom success and custom error OK and ERROR?
    @DefaultEnumValue
    OK(0),

    /** {@link Pad} is not linked. */
    NOT_LINKED(-1),
    /** {@link Pad} is in wrong state. */
    FLUSHING(-2),
    /** Did not expect anything, like after EOS. */
    EOS(-3),
    /** {@link Pad} is in not negotiated. */
    NOT_NEGOTIATED(-4),
    
    /**
     * Some (fatal) error occured. Element generating this error should post 
     * an error message with more details.
     */
    ERROR(-5),
    
    /** This operation is not supported. */
    NOT_SUPPORTED(-6);
    
    private final int value;
    
    FlowReturn(int value) {
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
