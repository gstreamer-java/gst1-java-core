/* 
 * Copyright (c) 2015 Neil C Smith
 * Copyright (c) 2014 Tom Greenwood <tgreenwood@cafex.com>
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007, 2008 Wayne Meissner
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

package org.freedesktop.gstreamer.lowlevel;

import org.freedesktop.gstreamer.Sample;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.MiniObjectStruct;

import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.List;
import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstSegmentStruct;

/**
 * GstSampleAPI functions and structure
 * @see https://cgit.freedesktop.org/gstreamer/gstreamer/tree/gst/gstsample.c?h=1.8
 */
public interface GstSampleAPI extends com.sun.jna.Library {
    GstSampleAPI GSTMESSAGE_API = GstNative.load(GstSampleAPI.class);

    public static final class SampleStruct extends com.sun.jna.Structure {
    	public volatile MiniObjectStruct mini_object;
        
        public volatile Pointer /* GstBuffer* */buffer;             // to Buffer
        public volatile Pointer /* GstCaps* */ caps;                // to Caps
        public volatile GstSegmentStruct segment;                   // Segment
        public volatile Pointer /* GstStructure* */ info;           // to Strucutre
        public volatile Pointer /* GstBufferList* */ buffer_list;   // to buffer_list
        
        /**
         * Creates a new instance of MessageStruct
         */
        public SampleStruct() {
        }
        public SampleStruct(Pointer ptr) {
            useMemory(ptr);
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "mini_object", "buffer", "caps", "segment", "info", "buffer_list"
            });
        }
    }
    
    /*@CallerOwnsReturn*/ Caps gst_sample_get_caps(Sample sample);
    /*@CallerOwnsReturn*/ Buffer gst_sample_get_buffer(Sample sample);
    
}
