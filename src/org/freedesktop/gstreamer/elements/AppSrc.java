/*
 * Copyright (c) 2009 Andres Colubri
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 2007 David Schleef <ds@schleef.org>
 *           (C) 2008 Wim Taymans <wim.taymans@gmail.com>
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

package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Buffer;
import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.FlowReturn;
import org.freedesktop.gstreamer.lowlevel.AppAPI;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

import com.sun.jna.ptr.LongByReference;

/**
 * Enables an application to feed buffers into a pipeline.
 */
public class AppSrc extends BaseSrc {
    public static final String GST_NAME = "appsrc";
    public static final String GTYPE_NAME = "GstAppSrc";

    private static final AppAPI gst = AppAPI.APP_API;

    public enum Type {
        STREAM,
        SEEKABLE,
        RANDOM_ACCESS;
    }

    public AppSrc(Initializer init) {
        super(init);
    }

    protected AppSrc(String name) {
    	this(Element.makeRawElement(AppSrc.GST_NAME, name));
    }

    @Override
    public void setCaps(Caps caps) {
        AppSrc.gst.gst_app_src_set_caps(this, caps);
    }
    public Caps getCaps() {
        return AppSrc.gst.gst_app_src_get_caps(this);
    }

    public void setSize(long size) {
        AppSrc.gst.gst_app_src_set_size(this, size);
    }
    public long getSize() {
        return AppSrc.gst.gst_app_src_get_size(this);
    }

    public void setStreamType(AppSrc.Type type) {
        AppSrc.gst.gst_app_src_set_stream_type(this, type);
    }
    AppSrc.Type getStreamType(AppSrc.Type type) {
        return AppSrc.gst.gst_app_src_get_stream_type(this);
    }

    public void setMaxBytes(long max) {
        AppSrc.gst.gst_app_src_set_max_bytes(this, max);
    }
    public long getMaxBytes() {
        return AppSrc.gst.gst_app_src_get_max_bytes(this);
    }

    public void setLatency(long min, long max) {
        AppSrc.gst.gst_app_src_set_latency(this, min, max);
    }
    public void getLatency(long[] minmax) {
        LongByReference minRef = new LongByReference();
        LongByReference maxRef = new LongByReference();
        AppSrc.gst.gst_app_src_get_latency(this, minRef, minRef);
        if (minmax == null || minmax.length != 2) minmax = new long[2];
        minmax[0] = minRef.getValue();
        minmax[1] = maxRef.getValue();
    }

    public void pushBuffer(Buffer buffer) {
    	AppSrc.gst.gst_app_src_push_buffer(this, buffer);
    }
    public void endOfStream() {
        AppSrc.gst.gst_app_src_end_of_stream(this);
    }

    /**
     * Signal emitted when this {@link AppSrc} when no more buffer are available.
     */
    public static interface END_OF_STREAM {
        /**
         *
         * @param elem
         */
        public FlowReturn endOfStream(AppSrc elem);
    }
    /**
     * Adds a listener for the <code>end-of-stream</code> signal
     *
     * @param listener Listener to be called this when appsrc has no more buffer are available.
     */
    public void connect(final END_OF_STREAM listener) {
        this.connect(END_OF_STREAM.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public FlowReturn callback(AppSrc elem) {
                return listener.endOfStream(elem);
            }
        });
    }
    /**
     * Removes a listener for the <code>end-of-stream</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(END_OF_STREAM listener) {
        this.disconnect(END_OF_STREAM.class, listener);
    }

    /**
     * Signal emitted when this {@link AppSrc} has enough data in the queue.
     */
    public static interface ENOUGH_DATA {
        /**
         *
         * @param elem
         */
        public void enoughData(AppSrc elem);
    }
    /**
     * Adds a listener for the <code>enough-data</code> signal
     *
     * @param listener Listener to be called this when appsrc fills its queue.
     */
    public void connect(final ENOUGH_DATA listener) {
        this.connect(ENOUGH_DATA.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(AppSrc elem) {
                listener.enoughData(elem);
            }
        });
    }
    /**
     * Removes a listener for the <code>enough-data</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(ENOUGH_DATA listener) {
        this.disconnect(ENOUGH_DATA.class, listener);
    }

    /**
     * Signal emitted when this {@link AppSrc} needs data.
     */
    public static interface NEED_DATA {
        /**
         *
         * @param elem
         * @param size
         */
        public void needData(AppSrc elem, int size);
    }
    /**
     * Adds a listener for the <code>need-data</code> signal
     *
     * @param listener Listener to be called when appsrc needs data.
     */
    public void connect(final NEED_DATA listener) {
        this.connect(NEED_DATA.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(AppSrc elem, int size) {
                listener.needData(elem, size);
            }
        });
    }
    /**
     * Removes a listener for the <code>need-data</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(NEED_DATA listener) {
        this.disconnect(NEED_DATA.class, listener);
    }

    /**
     * Signal emitted when adds a buffer to the queue of buffers that
     * this {@link AppSrc} element will push to its source pad.
     */
    public static interface PUSH_BUFFER {
        /**
         *
         * @param elem
         * @param buffer
         */
        public FlowReturn pushBuffer(AppSrc elem, Buffer buffer);
    }
    /**
     * Adds a listener for the <code>push-buffer</code> signal
     *
     * @param listener Listener to be called when appsrc push buffer.
     */
    public void connect(final PUSH_BUFFER listener) {
        this.connect(PUSH_BUFFER.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public FlowReturn callback(AppSrc elem, Buffer buffer) {
                return listener.pushBuffer(elem, buffer);
            }
        });
    }
    /**
     * Removes a listener for the <code>push-buffer</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(PUSH_BUFFER listener) {
        this.disconnect(PUSH_BUFFER.class, listener);
    }

    /**
     * Signal emitted when this {@link AppSrc} when it requires the application
     * to push buffers from a specific location in the input stream.
     */
    public static interface SEEK_DATA {
        /**
         *
         * @param elem
         * @param position
         */
        public boolean seekData(AppSrc elem, long position);
    }
    /**
     * Adds a listener for the <code>seek-data</code> signal
     *
     * @param listener Listener to be called when appsrc when its "stream-mode"
     * property is set to "seekable" or "random-access". The signal argument
     * will contain the new desired position in the stream expressed in the unit
     * set with the "format" property. After receiving the seek-data signal,
     * the application should push-buffers from the new position.
     */
    public void connect(final SEEK_DATA listener) {
        this.connect(SEEK_DATA.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public boolean callback(AppSrc elem, long position) {
                return listener.seekData(elem, position);
            }
        });
    }
    /**
     * Removes a listener for the <code>seek-data</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(SEEK_DATA listener) {
        this.disconnect(SEEK_DATA.class, listener);
    }
}
