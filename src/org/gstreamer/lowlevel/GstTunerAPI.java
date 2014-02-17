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

import org.gstreamer.interfaces.Tuner;
import org.gstreamer.interfaces.TunerChannel;
import org.gstreamer.interfaces.TunerNorm;
import org.gstreamer.lowlevel.GlibAPI.GList;
import org.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.List;

public interface GstTunerAPI extends Library {
    GstTunerAPI GSTTUNER_API = GstNative.load("gstinterfaces", GstTunerAPI.class);
    
    GType gst_tuner_get_type();
    GType gst_tuner_channel_get_type();
    GType gst_tuner_norm_get_type();

    /* virtual class function wrappers */
    void gst_tuner_set_channel(Tuner tuner, TunerChannel channel);
    void gst_tuner_set_norm(Tuner tuner, TunerNorm norm);
    @CallerOwnsReturn TunerNorm  gst_tuner_get_norm(Tuner tuner);

    void gst_tuner_set_frequency(Tuner tuner, TunerChannel channel, NativeLong frequency);
    NativeLong gst_tuner_get_frequency(Tuner tuner, TunerChannel channel);
    int gst_tuner_signal_strength(Tuner tuner, TunerChannel channel);
    
    Pointer gst_tuner_get_channel(Tuner tuner);
    GList gst_tuner_list_channels(Tuner tuner);
    GList gst_tuner_list_norms(Tuner tuner);
    Pointer gst_tuner_find_channel_by_name(Tuner tuner, String channel);
    
    /* helper functions */
    @CallerOwnsReturn TunerNorm gst_tuner_find_norm_by_name(Tuner tuner, String norm);
    
    
    public static final class TunerChannelStruct extends com.sun.jna.Structure {
        public volatile GObjectAPI.GObjectStruct parent;
        public volatile String label;
        public volatile int flags;
        public volatile float freq_multiplicator;
        public volatile NativeLong min_frequency;
        public volatile NativeLong max_frequency;
        public volatile int min_signal;
        public volatile int max_signal;
        
        public float getFrequencyMultiplicator() {
            return (Float) readField("freq_multiplicator");
        }
        public int getMinimumSignal() {
            return (Integer) readField("min_signal");
        }
        public int getMaximumSignal() {
            return (Integer) readField("max_signal");
        }
        public NativeLong getMinimumFrequency() {
            return (NativeLong) readField("min_frequency");
        }
        public NativeLong getMaximumFrequency() {
            return (NativeLong) readField("max_frequency");
        }
        public int getFlags() {
            return (Integer) readField("flags");
        }
        public String getLabel() {
            return (String) readField("label");
        }
        public void read() {}
        public void write() {}
        public TunerChannelStruct(Pointer ptr) {
            useMemory(ptr);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent", "label", "flags",
                "freq_multiplicator", "min_frequency", "max_frequency",
                "min_signal", "max_signal"
            });
        }
    }
    public static final class TunerNormStruct extends com.sun.jna.Structure {
        public volatile GObjectAPI.GObjectStruct parent;
        public volatile String label;
        public volatile long framerate; // really a GValue
//        public int getFlags() {
//            return (Integer) readField("flags");
//        }
        public String getLabel() {
            return (String) readField("label");
        }
        public void read() {}
        public void write() {}
        public TunerNormStruct(Pointer ptr) {
            useMemory(ptr);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent", "label", "framerate"
            });
        }
    }
}
