/* 
 * Copyright (c) 2009 Levente Farkas
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

import static org.gstreamer.lowlevel.GstMixerAPI.GSTMIXER_API;

import org.gstreamer.GObject;
import org.gstreamer.lowlevel.GstMixerAPI;

import com.sun.jna.Pointer;

/**
 *
 */
public class MixerTrack extends GObject {
    public static final String GTYPE_NAME = "GstMixerTrack";

    private final GstMixerAPI.MixerTrackStruct struct;
    private final Mixer mixer;
    
    public static final class Flags {
        public static final int INPUT    = 1 << 0;
        public static final int OUTPUT   = 1 << 1;
        public static final int MUTE     = 1 << 2;
        public static final int RECORD   = 1 << 3;
        public static final int MASTER   = 1 << 4;
        public static final int SOFTWARE = 1 << 5;
    }
    
    /**
     * For internal gstreamer-java use only
     * 
     * @param init initialization data
     */
    public MixerTrack(Initializer init) {
        super(init);
        throw new IllegalArgumentException("Cannot instantiate this class");
    }
    
    MixerTrack(Mixer mixer, Pointer ptr, boolean needRef, boolean ownsHandle) {
        super(initializer(ptr, needRef, ownsHandle));
        struct = new GstMixerAPI.MixerTrackStruct(ptr);
        this.mixer = mixer;
    }
    
    /**
     * Gets the number of channels in this track
     * 
     * @return the number of channels
     */
    public int getChannelCount() {
        return struct.getChannelCount();
    }
    
    /**
     * Checks if a flag is set on this track.
     * 
     * @return true if the flag is set
     */
    public final boolean hasFlag(int flag) {
        return (struct.getFlags() & flag) != 0;
    }
    
    /**
     * Checks if this track is an input track.
     * 
     * @return true if this track is an input track
     */
    public boolean isInput() {
        return hasFlag(Flags.INPUT);
    }
    
    /**
     * Checks if this track is an output track.
     * 
     * @return true if this track is an output track
     */
    public boolean isOutput() {
        return hasFlag(Flags.OUTPUT);
    }
    
    /**
     * Checks if this track is currently muted.
     * 
     * @return true if this track is muted
     */
    public boolean isMuted() {
        return hasFlag(Flags.MUTE);
    }
    
    /**
     * Checks if this track is currently recording.
     * 
     * @return true if this track is recording
     */
    public boolean isRecording() {
        return hasFlag(Flags.RECORD);
    }
    
    /**
     * Sets the volume on each channel in a track. 
     * <p>
     * Short note about naming: a track is defined as one separate stream owned
     * by the mixer/element, such as 'Line-in' or 'Microphone'. A channel is 
     * said to be a mono-stream inside this track. A stereo track thus contains 
     * two channels.
     * </p>
     * 
     * @param volumes an array of integers (of size track.getChannelCount())
     *           that gives the wanted volume for each channel in
     *           this track.
     */
    public void setVolume(int[] volumes) {
        GSTMIXER_API.gst_mixer_set_volume(mixer, this, volumes);
    }
    
    /**
     * Gets the current volume(s) on the given track.
     * 

     * @return an array of int values containing the current volume for each 
     * channel
     */
    public int[] getVolume() {
        int[] volume = new int[getChannelCount()];
        GSTMIXER_API.gst_mixer_get_volume(mixer, this, volume);
        return volume;
    }
    
    /**
     * Mutes or unmutes the given channel.
     * 
     * @param mute true to mute, false to unmute the track
     */
    public void setMuted(boolean mute) {
        GSTMIXER_API.gst_mixer_set_mute(mixer, this, mute);
    }
    
    /**
     * Enables or disables recording on the given track. Note that
     * this is only possible on input tracks,
     * 
     * @param record true to record, false to stop recording
     */
    public void setRecording(boolean record) {
        GSTMIXER_API.gst_mixer_set_record(mixer, this, record);
    }
}
