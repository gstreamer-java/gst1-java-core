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

import org.freedesktop.gstreamer.ControlSource;
import org.freedesktop.gstreamer.glib.NativeEnum;
import static org.freedesktop.gstreamer.lowlevel.GstControllerAPI.GSTCONTROLLER_API;
import org.freedesktop.gstreamer.lowlevel.GstLFOControlSourcePtr;

/**
 * LFO control source.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstLFOControlSource.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer-libs/html/GstLFOControlSource.html</a>
 * <p>
 * LFOControlSource is a {@link ControlSource}, that provides several periodic
 * waveforms as control values.
 * <p>
 * To use InterpolationControlSource create a new instance, bind it to a
 * GParamSpec and set the relevant properties.
 * <p>
 * All functions are MT-safe.
 */
public class LFOControlSource extends ControlSource {

    public static final String GTYPE_NAME = "GstLFOControlSource";
    
    /**
     * Create a new, unbound LFOControlSource.
     */
    public LFOControlSource() {
        this(new Handle(GSTCONTROLLER_API.gst_lfo_control_source_new(), true), false);
    }

    LFOControlSource(Initializer init) {
        this(new Handle(
                init.ptr.as(GstLFOControlSourcePtr.class,
                        GstLFOControlSourcePtr::new),
                init.ownsHandle),
                init.needRef);
    }

    private LFOControlSource(Handle handle, boolean needRef) {
        super(handle, needRef);
    }

    /**
     * Specifies the amplitude for the waveform of this LFOControlSource.
     * <p>
     * Allowed values: [0,1]
     * <p>
     * Default value: 1
     *
     * @param value amplitude between 0 and 1
     * @return this
     */
    public LFOControlSource setAmplitude(double value) {
        set("amplitude", value);
        return this;
    }

    /**
     * Get the amplitude of the waveform.
     *
     * @return amplitude
     */
    public double getAmplitude() {
        Object val = get("amplitude");
        if (val instanceof Double) {
            return (double) val;
        }
        return 1;
    }

    /**
     * Specifies the frequency that should be used for the waveform of this
     * LFOControlSource. It should be large enough so that the period is longer
     * than one nanosecond.
     * <p>
     * Allowed values: >= {@link Double#MIN_VALUE }
     * <p>
     * Default value: 1
     *
     * @param value frequency >= {@link Double#MIN_VALUE }
     * @return this
     */
    public LFOControlSource setFrequency(double value) {
        set("frequency", value);
        return this;
    }

    /**
     * Get the frequency of the waveform.
     *
     * @return amplitude
     */
    public double getFrequency() {
        Object val = get("frequency");
        if (val instanceof Double) {
            return (double) val;
        }
        return 1;
    }

    /**
     * Specifies the value offset for the waveform of this LFOControlSource.
     * <p>
     * Allowed values: [0,1]
     * <p>
     * Default value: 1
     *
     * @param value offset between 0 and 1
     * @return this
     */
    public LFOControlSource setOffset(double value) {
        set("offset", value);
        return this;
    }

    /**
     * Get the value offset of the waveform.
     *
     * @return offset
     */
    public double getOffset() {
        Object val = get("offset");
        if (val instanceof Double) {
            return (double) val;
        }
        return 1;
    }

    /**
     * Specifies the timeshift to the right that should be used for the waveform
     * of this LFOControlSource in nanoseconds.
     * <p>
     * Default value: 0
     *
     * @param value timeshift in nanoseconds
     * @return this
     */
    public LFOControlSource setTimeshift(long value) {
        set("timeshift", value);
        return this;
    }

    /**
     * Get the timeshift of the waveform.
     *
     * @return timeshift
     */
    public long getTimeshift() {
        Object val = get("timeshift");
        if (val instanceof Long) {
            return (long) val;
        }
        return 1;
    }

    /**
     * Specifies the waveform that should be used for this LFOControlSource.
     * <p>
     * Default value: {@link LFOWaveform#SINE }
     *
     * @param value waveform
     * @return this
     */
    public LFOControlSource setWaveform(LFOWaveform value) {
        set("waveform", value.intValue());
        return this;
    }

    /**
     * Get the waveform.
     *
     * @return waveform
     */
    public LFOWaveform getWaveform() {
        Object val = get("waveform");
        if (val instanceof Integer) {
            int nativeInt = (Integer) val;
            return NativeEnum.fromInt(LFOWaveform.class, nativeInt);
        }
        return LFOWaveform.SINE;
    }

    private static class Handle extends ControlSource.Handle {

        public Handle(GstLFOControlSourcePtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

    }

}
