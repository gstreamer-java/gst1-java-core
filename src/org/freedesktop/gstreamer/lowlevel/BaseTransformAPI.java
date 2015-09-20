/* 
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007 Wayne Meissner
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

import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.ClockTime;
import org.freedesktop.gstreamer.Event;
import org.freedesktop.gstreamer.FlowReturn;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.PadDirection;
import org.freedesktop.gstreamer.elements.BaseTransform;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstSegmentStruct;
import org.freedesktop.gstreamer.lowlevel.GstElementAPI.GstElementClass;
import org.freedesktop.gstreamer.lowlevel.GstElementAPI.GstElementStruct;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.util.Arrays;
import java.util.List;

public interface BaseTransformAPI extends Library {
	BaseTransformAPI BASETRANSFORM_API = GstNative.load("gstbase", BaseTransformAPI.class);
    int GST_PADDING = GstAPI.GST_PADDING;
    int GST_PADDING_LARGE = GstAPI.GST_PADDING_LARGE;
    
    public static final class GstBaseTransformStruct extends com.sun.jna.Structure {
        public GstElementStruct element;

        /*< protected >*/
        public volatile Pad sinkpad;
        public volatile Pad srcpad;

        /* Set by sub-class */
        public volatile boolean passthrough;
        public volatile boolean always_in_place;

        public volatile Caps    cache_caps1;
        public volatile int     cache_caps1_size;
        public volatile Caps    cache_caps2;
        public volatile int     cache_caps2_size;
        public volatile boolean have_same_caps;

        public volatile boolean delay_configure;
        public volatile boolean pending_configure;
        public volatile boolean negotiated;

        public volatile boolean have_newsegment;

        /* MT-protected (with STREAM_LOCK) */
        public volatile GstSegmentStruct segment;

        public volatile /* GMutex */ Pointer transform_lock;

        /*< private >*/
        public volatile /* GstBaseTransformPrivate */ Pointer priv;

        public volatile Pointer[] _gst_reserved = new Pointer[GST_PADDING_LARGE - 1];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "element", "sinkpad", "srcpad",
                "passthrough", "always_in_place", "cache_caps1",
                "cache_caps1_size", "cache_caps2", "cache_caps2_size",
                "have_same_caps", "delay_configure", "pending_configure",
                "negotiated", "have_newsegment", "segment",
                "transform_lock", "priv", "_gst_reserved"
            });
        }
    
        
    
    }

    
    public static interface TransformCaps extends Callback {
        public Caps callback(BaseTransform trans, PadDirection direction, Caps caps);
    }
    public static interface FixateCaps extends Callback {
        public void callback(BaseTransform trans, PadDirection direction, Caps caps, Caps othercaps);
    }
    public static interface TransformSize extends Callback {
        public boolean callback(BaseTransform trans, PadDirection direction, Caps caps, 
        						int size, Caps othercaps, IntByReference othersize);
    }
    public static interface GetUnitSize extends Callback {
        public boolean callback(BaseTransform trans, Caps caps, IntByReference size);
    }
    public static interface SetCaps extends Callback {
        public boolean callback(BaseTransform trans, Caps caps, Caps outcaps);
    }
    public static interface BooleanFunc1 extends Callback {
        public boolean callback(BaseTransform sink);
    }
    public static interface EventNotify extends Callback {
        public boolean callback(BaseTransform trans, Event event);
    }
    public static interface Transform extends Callback {
        public FlowReturn callback(BaseTransform trans, Buffer inbuf, Buffer outbuf);
    }
    public static interface TransformIp extends Callback {
        public FlowReturn callback(BaseTransform trans, Buffer inbuf);
    }
    public static interface PrepareOutput extends Callback {
        public FlowReturn callback(BaseTransform trans, Buffer input, int size, Caps caps, 
        						/*GstBuffer ** */ Pointer buf);
    }
    public static interface BeforeTransform extends Callback {
        public void callback(BaseTransform trans, Buffer inbuf);
    }
    public static interface AcceptCaps extends Callback {
        public boolean callback(BaseTransform trans, PadDirection direction, Caps caps);
    }
    
    public static final class GstBaseTransformClass extends com.sun.jna.Structure {
        public GstBaseTransformClass() {}
        public GstBaseTransformClass(Pointer ptr) {
            useMemory(ptr);
            read();
        }
        
        //
        // Actual data members
        //
        public GstElementClass parent_class;
        
        /*< public >*/
        public TransformCaps transform_caps;

        public FixateCaps fixate_caps;
        
        public TransformSize transform_size;
        
        public GetUnitSize get_unit_size;

        public SetCaps set_caps;

        public BooleanFunc1 start;
        public BooleanFunc1 stop;
        
        public EventNotify event;
        
        public Transform transform;
        
        public TransformIp transform_ip;

        public volatile boolean passthrough_on_same_caps;

        public PrepareOutput prepare_output_buffer;

        public EventNotify src_event;
        
        public BeforeTransform before_transform;
        
        public AcceptCaps accept_caps;

        /*< private >*/
        public volatile byte[] _gst_reserved = new byte[Pointer.SIZE * (GST_PADDING_LARGE - 3)];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_class", "transform_caps", "fixate_caps",
                "transform_size", "get_unit_size", "set_caps",
                "start", "stop", "event",
                "transform", "transform_ip", "passthrough_on_same_caps",
                "prepare_output_buffer", "src_event", "before_transform",
                "accept_caps", "_gst_reserved"
            });            
        }
    }
    
    GType gst_base_transform_get_type();

    void gst_base_transform_set_passthrough(BaseTransform trans, boolean passthrough);
    boolean gst_base_transform_is_passthrough(BaseTransform trans);

    void gst_base_transform_set_in_place(BaseTransform trans, boolean in_place);
    boolean gst_base_transform_is_in_place(BaseTransform trans);

    void gst_base_transform_update_qos(BaseTransform trans, double proportion, long diff, ClockTime timestamp);
    void gst_base_transform_set_qos_enabled(BaseTransform trans, boolean enabled);
    boolean gst_base_transform_is_qos_enabled(BaseTransform trans);

    void gst_base_transform_set_gap_aware(BaseTransform trans, boolean gap_aware);

    void gst_base_transform_suggest(BaseTransform trans, Caps caps, int size);
    void gst_base_transform_reconfigure(BaseTransform trans);
}
