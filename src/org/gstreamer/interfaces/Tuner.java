/* 
 * Copyright (C) 2009 Tamas Korodi <kotyo@zamba.fm> 
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 2003 Ronald Bultje <rbultje@ronald.bitfreak.net>
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

import java.util.List;

import org.gstreamer.Element;
import org.gstreamer.lowlevel.GstAPI.GstCallback;
import org.gstreamer.lowlevel.NativeObject;

import com.sun.jna.Pointer;

/**
 * Interface for elements providing tuner operations
 */
public class Tuner extends GstInterface {
    /**
     * Wraps the {@link Element} in a <tt>Tuner</tt> interface
     * 
     * @param element the element to use as a <tt>Tuner</tt>
     * @return a <tt>Tuner</tt> for the element
     */
    public static final Tuner wrap(Element element) {
        return new Tuner(element);
    }
    
    /**
     * Creates a new Tuner instance
     * 
     * @param element the element that implements the tuner interface
     */
    private Tuner(Element element) {
        super(element, GSTTUNER_API.gst_tuner_get_type());
    }
    
    /**
     * Retrieves a list of channels (e.g. 'composite', 's-video') from the tuner
     * 
     * @return a list of channels available on this tuner
     */
    public List<TunerChannel> getChannelList() {
        return objectList(GSTTUNER_API.gst_tuner_list_channels(this), new ListElementCreator<TunerChannel>() {
            public TunerChannel create(Pointer pointer) {
                return channelFor(pointer, true);
            } 
        });
    }
    private final TunerChannel channelFor(Pointer pointer, boolean needRef) {
        return new TunerChannel(Tuner.this, pointer, needRef, true);
    }
    /**
     * Retrieves a list of available norms on the currently tuned channel.
     *
     * @return a list of norms available on the current channel 
     */
    public List<TunerNorm> getNormList() {
        return objectList(GSTTUNER_API.gst_tuner_list_norms(this), new ListElementCreator<TunerNorm>() {
            public TunerNorm create(Pointer pointer) {
                return NativeObject.objectFor(pointer, TunerNorm.class, true);
            }
        });
    }
    /**
     * Tunes this tuner to the given channel.
     * 
     * @param channel the channel to tune to
     */
    public void setChannel(TunerChannel channel) {
        GSTTUNER_API.gst_tuner_set_channel(this, channel);
    }
    
    /**
     * Retrieves the current channel from the tuner.
     * 
     * @return the current channel of the tuner
     */
    public TunerChannel getChannel() {
        Pointer ptr = GSTTUNER_API.gst_tuner_get_channel(this);
        if (ptr == null) {
            return null;
        }
        return new TunerChannel(this, ptr, false, true);
    }
    
    /**
     * Finds a channel with the specified name
     * 
     * @param name the name of the channel to find
     * 
     * @return the <tt>TunerChannel</tt> if found, else null
     */
    public TunerChannel getChannelByName(String name) {
        Pointer ptr = GSTTUNER_API.gst_tuner_find_channel_by_name(this, name);
        if (ptr == null) {
            return null;
        }
        return new TunerChannel(this, ptr, false, true);
    }
    
    /**
     * Changes the video norm on this tuner to the given norm
     * 
     * @param norm the norm to use for the current channel
     */
    public void setNorm(TunerNorm norm) {
        GSTTUNER_API.gst_tuner_set_norm(this, norm);
    }
    
    /**
     * Gets the current video norm from the given tuner object for the 
     * currently selected channel.
     * 
     * @return the current norm
     */
    public TunerNorm getNorm() {
        return GSTTUNER_API.gst_tuner_get_norm(this);
    }
    
    /**
     * Finds a <tt>TunerNorm</tt> with the specified name
     * 
     * @param name the name of the norm to find
     * 
     * @return the <tt>TunerNorm</tt> if found, else null
     */
    public TunerNorm getNormByName(String name) {
        return GSTTUNER_API.gst_tuner_find_norm_by_name(this, name);
    }
    
    /**
     * Signal emitted when the norm on a tuner changes
     *
     * @see #connect(NORM_CHANGED)
     * @see #disconnect(NORM_CHANGED)
     */
    public static interface NORM_CHANGED {
        /**
         * Called when the channel on a {@link Tuner} changes
         * 
         * @param tuner the tuner
         * @param norm the new norm
         */
        public void normChanged(Tuner tuner, TunerNorm norm);
    }
    
    /**
     * Signal emitted when the channel on a tuner changes
     *
     * @see #connect(CHANNEL_CHANGED)
     * @see #disconnect(CHANNEL_CHANGED)
     */
    public static interface CHANNEL_CHANGED {
        /**
         * Called when the channel on a {@link Tuner} changes
         * 
         * @param tuner the tuner
         * @param channel the new channel
         */
        public void channelChanged(Tuner tuner, TunerChannel channel);
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
         * @param tuner the tuner
         * @param channel the channel which the frequency changed on
         * @param frequency the new frequency
         */
        public void frequencyChanged(Tuner tuner, TunerChannel channel, long frequency);
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
         * @param tuner the tuner
         * @param channel the channel which the signal strength changed on
         * @param signal the new signal strength
         */
        public void signalChanged(Tuner tuner, TunerChannel channel, int signal);
    }
    
    /**
     * Add a listener for norm-changed messages.
     * 
     * @param listener the listener to be called when the norm changes
     */
    public void connect(final NORM_CHANGED listener) {
        element.connect(NORM_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(Pointer tuner, TunerNorm norm) {
                listener.normChanged(Tuner.this, norm);
                return true;
            }
        });
    }
    
    /**
     * Disconnect the listener for norm-changed messages.
     * 
     * @param listener the listener that was registered to receive the message.
     */
    public void disconnect(NORM_CHANGED listener) {
        element.disconnect(NORM_CHANGED.class, listener);
    }
    
    /**
     * Add a listener for channel-changed messages.
     * 
     * @param listener The listener to be called when the channel changes
     */
    public void connect(final CHANNEL_CHANGED listener) {
        element.connect(CHANNEL_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(Pointer tuner, Pointer channel) {
                listener.channelChanged(Tuner.this, channelFor(channel, false));
                return true;
            }
        });
    }
    
    /**
     * Disconnect the listener for channel-changed messages.
     * 
     * @param listener The listener that was registered to receive the message.
     */
    public void disconnect(CHANNEL_CHANGED listener) {
        element.disconnect(CHANNEL_CHANGED.class, listener);
    }
    
    /**
     * Add a listener for frequency-changed messages.
     * 
     * @param listener The listener to be called when the frequency changes
     */
    public void connect(final FREQUENCY_CHANGED listener) {
        element.connect(FREQUENCY_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(Pointer tuner, Pointer channel, long frequency) {
                listener.frequencyChanged(Tuner.this, channelFor(channel, false), frequency);
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
        element.disconnect(FREQUENCY_CHANGED.class, listener);
    }
    
    /**
     * Add a listener for signal-changed messages.
     * 
     * @param listener The listener to be called when the signal strength changes
     */
    public void connect(final SIGNAL_CHANGED listener) {
        element.connect(SIGNAL_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(Pointer tuner, Pointer channel, int signal) {
                listener.signalChanged(Tuner.this, channelFor(channel, false), signal);
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
        element.disconnect(SIGNAL_CHANGED.class, listener);
    }
}
