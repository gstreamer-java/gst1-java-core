/*
 * Copyright (c) 2009 Levente Farkas
 * Copyright (C) 2009 Tamas Korodi <kotyo@zamba.fm> 
 * Copyright (c) 2008 Wayne Meissner
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

package org.gstreamer.interfaces;

import static org.gstreamer.lowlevel.GstTunerAPI.GSTTUNER_API;

import org.gstreamer.GObject;
import org.gstreamer.lowlevel.GstAPI.GstCallback;
import org.gstreamer.lowlevel.GstTunerAPI;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

/**
 *
 */
public class TunerChannel extends GObject {
    public static final String GTYPE_NAME = "GstTunerChannel";

    private final GstTunerAPI.TunerChannelStruct struct;
    private final Tuner tuner;
    
    public static final class Flags {
        public static final int INPUT     = (1<<0);
        public static final int OUTPUT    = (1<<1);
        public static final int FREQUENCY = (1<<2);
        public static final int AUDIO     = (1<<3);
    }
    
    /**
     * For internal gstreamer-java use only
     * 
     * @param init initialization data
     */
    public TunerChannel(Initializer init) {
        super(init);
        throw new IllegalArgumentException("Cannot instantiate");
    }
    TunerChannel(Tuner tuner, Pointer ptr, boolean needRef, boolean ownsHandle) {
        super(initializer(ptr, needRef, ownsHandle));
        struct = new GstTunerAPI.TunerChannelStruct(ptr);
        this.tuner = tuner;
    }
    
    /**
     * Checks if a flag is set on this channel.
     * 
     * @return true if the flag is set
     */
    public final boolean hasFlag(int flag) {
        return (struct.getFlags() & flag) != 0;
    }
    
    /**
     * Retrieves the current frequency from the given channel
     * 
     * @return the current frequency
     */
    public long getFrequency() {
        return GSTTUNER_API.gst_tuner_get_frequency(tuner, this).longValue();
    }
    

    /**
     * Retrieves the label from the given channel
     *
     * @return the label
     */
    public String getLabel() {
        return struct.getLabel();
    }

    /**
     * Sets a tuning frequency on the given tuner/channel. 
     * <p><b>Note:</b> this requires the given channel to be a "tuning" 
     * channel, which can be checked with {@link #isTuningChannel}
     */
    public void setFrequency(long frequency) {
        GSTTUNER_API.gst_tuner_set_frequency(tuner, this, new NativeLong(frequency));
    }
    
    /**
     * Checks if the frequency of this channel can be changed
     * 
     * @return true if this channel is a tuning channel
     */
    public boolean isTuningChannel() {
        return hasFlag(Flags.FREQUENCY);
    }
    
    /**
     * Gets the strength of the signal on this channel. 
     * <p><b>Note:</b> this requires the current channel to be a "tuning" 
     * channel, i.e. a channel on which frequency can be set. This can be 
     * checked using {@link #isTuningChannel()}
     * 
     * @return the current signal strength
     */
    public int getSignalStrength() {
        return GSTTUNER_API.gst_tuner_signal_strength(tuner, this);
    }
    
    /**
     * Signal emitted when the frequency on a channel changes
     *
     * @see #connect(FREQUENCY_CHANGED)
     * @see #disconnect(FREQUENCY_CHANGED)
     */
    public static interface FREQUENCY_CHANGED {
        /**
         * Called when the frequency on a {@link Tuner} changes
         * 
         * @param channel the channel which the frequency changed on
         * @param frequency the current frequency
         */
        public void frequencyChanged(TunerChannel channel, long frequency);
    }
    
    /**
     * Signal emitted when the signal strength on a channel changes
     *
     * @see #connect(SIGNAL_CHANGED)
     * @see #disconnect(SIGNAL_CHANGED)
     */
    public static interface SIGNAL_CHANGED {
        /**
         * Called when the signal strength on a {@link Tuner} changes
         * 
         * @param channel the channel which the signal strength changed on
         * @param signal the new signal strength
         */
        public void signalChanged(TunerChannel channel, int signal);
    }
    
    /**
     * Add a listener for frequency-changed messages.
     * 
     * @param listener The listener to be called when the frequency changes
     */
    public void connect(final FREQUENCY_CHANGED listener) {
        connect(FREQUENCY_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(long frequency) {
                listener.frequencyChanged(TunerChannel.this, frequency);
                return true;
            }
        });
    }
    
    /**
     * Disconnect the listener for frequency-changed messages.
     * 
     * @param listener The listener that was registered to receive the message.
     */
    public void disconnect(FREQUENCY_CHANGED listener) {
        super.disconnect(FREQUENCY_CHANGED.class, listener);
    }
    
    /**
     * Add a listener for signal-changed messages.
     * 
     * @param listener The listener to be called when the signal strength changes
     */
    public void connect(final SIGNAL_CHANGED listener) {
        connect(SIGNAL_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(int signal) {
                listener.signalChanged(TunerChannel.this, signal);
                return true;
            }
        });
    }
    
    /**
     * Disconnect the listener for signal-changed messages.
     * 
     * @param listener The listener that was registered to receive the message.
     */
    public void disconnect(SIGNAL_CHANGED listener) {
        super.disconnect(SIGNAL_CHANGED.class, listener);
    }
}
