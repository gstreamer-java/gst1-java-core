/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer.controller;

import org.freedesktop.gstreamer.glib.NativeEnum;

/**
 * The various waveform modes available for use with {@link LFOControlSource}
 */
// https://gitlab.freedesktop.org/gstreamer/gstreamer/blob/master/libs/gst/controller/gstlfocontrolsource.h#L60
public enum LFOWaveform implements NativeEnum<LFOWaveform> {

    /**
     * Sine waveform.
     */
    SINE(0),
    
    /**
     * Square waveform.
     */
    SQUARE(1),

    /**
     * Saw waveform.
     */
    SAW(2),

    /**
     * Reverse saw waveform.
     */
    REVERSE_SAW(3),

    /**
     * Triangle waveform.
     */
    TRIANGLE(4)
    ;

        
    private final int value;
    
    private LFOWaveform(int value) {
        this.value = value;
    }
        
    @Override
    public int intValue() {
        return value;
    }
    
}
