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

import java.util.Arrays;
import java.util.List;
//import org.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import org.freedesktop.gstreamer.ActivateMode;
import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.ClockTime;
import org.freedesktop.gstreamer.Event;
import org.freedesktop.gstreamer.FlowReturn;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.elements.BaseSrc;
import org.freedesktop.gstreamer.lowlevel.GstElementAPI.GstElementClass;
import org.freedesktop.gstreamer.lowlevel.GstElementAPI.GstElementStruct;
import org.freedesktop.gstreamer.lowlevel.GstSegmentAPI.GstSegmentStruct;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.Union;
import com.sun.jna.ptr.LongByReference;

public interface BaseSrcAPI extends Library {
	BaseSrcAPI BASESRC_API = GstNative.load("gstbase", BaseSrcAPI.class);
    int GST_PADDING = GstAPI.GST_PADDING;
    int GST_PADDING_LARGE = GstAPI.GST_PADDING_LARGE;

    public static final class GstBaseSrcStruct extends com.sun.jna.Structure {
        public GstElementStruct element;

        /*< protected >*/
        public volatile Pad srcpad;

        /* available to subclass implementations */
        /* MT-protected (with LIVE_LOCK) */
        public volatile /* GMutex */ Pointer live_lock;
        public volatile /* GCond */ Pointer live_cond;
        public volatile boolean is_live;
        public volatile boolean live_running;

        /* MT-protected (with LOCK) */
        public volatile int blocksize;	/* size of buffers when operating push based */
        public volatile boolean can_activate_push;	/* some scheduling properties */
        public volatile ActivateMode pad_mode;
        public volatile boolean seekable;
        public volatile boolean random_access;

        public volatile /* GstClockID */ Pointer clock_id;	/* for syncing */
        public volatile /* GstClockTime */ long  end_time;

        /* MT-protected (with STREAM_LOCK) */
        public volatile GstSegmentStruct segment;
        public volatile boolean	 need_newsegment;

        public volatile /* guint64 */ long offset;	/* current offset in the resource, unused */
        public volatile /* guint64 */ long size;        /* total size of the resource, unused */

        public volatile int num_buffers;
        public volatile int num_buffers_left;

        /*< private >*/
        public volatile GstBaseSrcAbiData abidata;
        public volatile Pointer /* GstBaseSrcPrivate */ priv;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[] {
                "element", "srcpad", "live_lock",
                "live_cond", "is_live", "live_running",
                "blocksize", "can_activate_push", "pad_mode",
                "seekable", "random_access", "clock_id",
                "end_time", "segment", "need_newsegment",
                "offset", "size", "num_buffers",
                "num_buffers_left", "abidata", "priv"
            });
        }
    }

    public static final class GstBaseSrcAbiData extends Union {
        public volatile GstBaseSrcAbi abi;
        public volatile Pointer[] _gst_reserved = new Pointer[BaseSrcAPI.GST_PADDING_LARGE - 1];
    }

    public static final class GstBaseSrcAbi extends com.sun.jna.Structure {
        public volatile boolean typefind;
        public volatile boolean running;
        public volatile Pointer /* GstEvent */ pending_seek;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "typefind", "running", "pending_seek"
            });
        }
    }

    // -------------- Callbacks -----------------
    public static interface GetCaps extends Callback {
        public Caps callback(BaseSrc src);
    }
    public static interface SetCaps extends Callback {
        public boolean callback(BaseSrc src, Caps caps);
    }
    public static interface BooleanFunc1 extends Callback {
        public boolean callback(BaseSrc src);
    }

    public static interface GetTimes extends Callback {
        public void callback(BaseSrc src, Buffer buffer,
                Pointer start, Pointer end);
    }
    public static interface GetSize extends Callback {
        boolean callback(BaseSrc src, LongByReference size);
    }
    public static interface EventNotify extends Callback {
        boolean callback(BaseSrc src, Event event);
    }
    public static interface Create extends Callback {
        public FlowReturn callback(BaseSrc src, long offset, int size,
                /* GstBuffer ** */ Pointer bufRef);
    }
    public static interface Seek extends Callback {
        boolean callback(BaseSrc src, GstSegmentStruct segment);
    }
    public static interface Query extends Callback {
        boolean callback(BaseSrc src, Query query);
    }
    public static interface Fixate extends Callback {
        public void callback(BaseSrc src, Caps caps);
    }
    public static interface PrepareSeek extends Callback {
        boolean callback(BaseSrc src, Event seek, GstSegmentStruct segment);
    }
    public static final class GstBaseSrcClass extends com.sun.jna.Structure {
        public GstBaseSrcClass() {}
        public GstBaseSrcClass(Pointer ptr) {
            this.useMemory(ptr);
            this.read();
        }

        //
        // Actual data members
        //
        public GstElementClass parent_class;

        /*< public >*/
        /* virtual methods for subclasses */

        /* get caps from subclass */
        public GetCaps get_caps;
        /* notify the subclass of new caps */
        public SetCaps set_caps;
        /* decide on caps */
        public BooleanFunc1 negotiate;
        /* generate and send a newsegment (UNUSED) */
        public BooleanFunc1 newsegment;


        /* start and stop processing, ideal for opening/closing the resource */
        public BooleanFunc1 start;
        public BooleanFunc1 stop;

        /*
         * Given a buffer, return start and stop time when it should be pushed
         * out. The base class will sync on the clock using these times.
         */
        public GetTimes get_times;

        /* get the total size of the resource in bytes */
        public GetSize get_size;

        /* check if the resource is seekable */
        public BooleanFunc1 is_seekable;

        /* unlock any pending access to the resource. subclasses should unlock
        * any function ASAP. */
        public BooleanFunc1 unlock;


        /* notify subclasses of an event */
        public EventNotify event;

        /* ask the subclass to create a buffer with offset and size */
        public Create create;

        /* additions that change padding... */
        /* notify subclasses of a seek */
        public Seek seek;
        /* notify subclasses of a query */
        public Query query;

        /* check whether the source would support pull-based operation if
        * it were to be opened now. This vfunc is optional, but should be
        * implemented if possible to avoid unnecessary start/stop cycles.
        * The default implementation will open and close the resource to
        * find out whether get_range is supported and that is usually
        * undesirable. */
        public BooleanFunc1 check_get_range;

        /* called if, in negotation, caps need fixating */
        public Fixate fixate;

        /* Clear any pending unlock request, as we succeeded in unlocking */
        public BooleanFunc1 unlock_stop;

        /* Prepare the segment on which to perform do_seek(), converting to the
         * current basesrc format. */
        public PrepareSeek prepare_seek_segment;

        /*< private >*/
        public volatile byte[] _gst_reserved = new byte[Pointer.SIZE * (BaseSrcAPI.GST_PADDING_LARGE - 6)];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[] {
                "parent_class", "get_caps", "set_caps",
                "negotiate", "newsegment", "start",
                "stop", "get_times", "get_size",
                "is_seekable", "unlock", "event",
                "create", "seek", "query",
                "check_get_range", "fixate", "unlock_stop",
                "prepare_seek_segment", "_gst_reserved"

            });
        }

    }

    GType gst_base_src_get_type();

    FlowReturn gst_base_src_wait_playing(BaseSrc src);

    void gst_base_src_set_live(BaseSrc src, boolean live);
    boolean gst_base_src_is_live(BaseSrc src);

    void gst_base_src_set_format(BaseSrc src, Format format);

    boolean gst_base_src_query_latency(BaseSrc src, boolean[] live, ClockTime[] min_latency, ClockTime[] max_latency);

    void gst_base_src_set_blocksize(BaseSrc src, long blocksize);
    long gst_base_src_get_blocksize(BaseSrc src);

    void gst_base_src_set_do_timestamp(BaseSrc src, boolean timestamp);
    boolean gst_base_src_get_do_timestamp(BaseSrc src);

    boolean gst_base_src_new_seamless_segment(BaseSrc src, long start, long stop, long position);
}
