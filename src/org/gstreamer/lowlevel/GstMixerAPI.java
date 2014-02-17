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

package org.gstreamer.lowlevel;

import org.gstreamer.interfaces.Mixer;
import org.gstreamer.interfaces.MixerTrack;
import org.gstreamer.lowlevel.GlibAPI.GList;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.List;

public interface GstMixerAPI extends Library {
	GstMixerAPI GSTMIXER_API = GstNative.load("gstinterfaces", GstMixerAPI.class);
    GType gst_mixer_get_type();
    GType gst_mixer_track_get_type();

    /* virtual class function wrappers */
    GList gst_mixer_list_tracks(Mixer mixer);
    void gst_mixer_set_volume(Mixer mixer, MixerTrack track, int[] volumes);
    void gst_mixer_get_volume(Mixer mixer, MixerTrack track, int[] volumes);
    void gst_mixer_set_mute(Mixer mixer, MixerTrack track, boolean mute);
    void gst_mixer_set_record(Mixer mixer, MixerTrack track, boolean record);
//    void gst_mixer_set_option(Mixer mixer, MixerOptions opts, byte value);
//    const gchar* gst_mixer_get_option(Mixer* mixer, MixerOptions *opts);

    /* trigger bus messages */
    void gst_mixer_mute_toggled(Mixer mixer, MixerTrack track, boolean mute);
    void gst_mixer_record_toggled(Mixer mixer, MixerTrack track, boolean record);
    void gst_mixer_volume_changed(Mixer mixer, MixerTrack track, int[] volumes);
//    void gst_mixer_option_changed(Mixer mixer, MixerOptions[] opts, byte[] value);

    void gst_mixer_mixer_changed(Mixer mixer);

//    void gst_mixer_options_list_changed(Mixer mixer, MixerOptions[] opts);
    int gst_mixer_get_mixer_flags(Mixer mixer);

    public static final class MixerTrackStruct extends com.sun.jna.Structure {
        public volatile GObjectAPI.GObjectStruct parent;
        public volatile String label;
        public volatile int flags;
        public volatile int num_channels;
        public volatile int min_volume;
        public volatile int max_volume;
        
        public int getChannelCount() {
            return (Integer) readField("num_channels");
        }
        public int getMinimumVolume() {
            return (Integer) readField("min_volume");
        }
        public int getMaximumVolume() {
            return (Integer) readField("max_volume");
        }
        public int getFlags() {
            return (Integer) readField("flags");
        }
        public void read() {}
        public void write() {}
        public MixerTrackStruct(Pointer ptr) {
            useMemory(ptr);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent", "label", "flags",
                "num_channels", "min_volume", "max_volume"
            });
        }
    }
}
