/*
 * Copyright (c) 2019 Neil C Smith
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
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;
import static org.freedesktop.gstreamer.lowlevel.AppAPI.APP_API;

import com.sun.jna.ptr.LongByReference;
import org.freedesktop.gstreamer.FlowReturn;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.glib.NativeEnum;

/**
 * A source {@link Element} that provides an easy way for applications to insert
 * data into a GStreamer pipeline.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-libs/html/gst-plugins-base-libs-appsrc.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-libs/html/gst-plugins-base-libs-appsrc.html</a>
 * <p>
 * Before operating appsrc, the {@link Caps} property must be set to fixed caps
 * describing the format of the data that will be pushed with appsrc. An
 * exception to this is when pushing buffers with unknown caps, in which case no
 * caps should be set. This is typically true of file-like sources that push raw
 * byte buffers. If you don't want to explicitly set the caps, you can use
 * gst_app_src_push_sample (<em>not yet mapped</em>). This method gets the caps
 * associated with the sample and sets them on the appsrc replacing any
 * previously set caps (if different from sample's caps).
 * <p>
 * The main way of handing data to the appsrc element is by calling the
 * {@link #pushBuffer(Buffer) } method or by emitting the push-buffer action
 * signal. This will put the buffer onto a queue from which appsrc will read
 * from in its streaming thread. It is important to note that data transport
 * will not happen from the thread that performed the push-buffer call.
 * <p>
 * The "max-bytes" property controls how much data can be queued in appsrc
 * before appsrc considers the queue full. A filled internal queue will always
 * signal the "enough-data" signal, which signals the application that it should
 * stop pushing data into appsrc. The "block" property will cause appsrc to
 * block the push-buffer method until free data becomes available again.
 * <p>
 * When the internal queue is running out of data, the "need-data" signal is
 * emitted, which signals the application that it should start pushing more data
 * into appsrc.
 * <p>
 * In addition to the "need-data" and "enough-data" signals, appsrc can emit the
 * "seek-data" signal when the "stream-mode" property is set to "seekable" or
 * "random-access". The signal argument will contain the new desired position in
 * the stream expressed in the unit set with the "format" property. After
 * receiving the seek-data signal, the application should push-buffers from the
 * new position.
 * <p>
 * These signals allow the application to operate the appsrc in two different
 * ways:
 * <p>
 * The push mode, in which the application repeatedly calls the
 * push-buffer/push-sample method with a new buffer/sample. Optionally, the
 * queue size in the appsrc can be controlled with the enough-data and need-data
 * signals by respectively stopping/starting the push-buffer/push-sample calls.
 * This is a typical mode of operation for the stream-type "stream" and
 * "seekable". Use this mode when implementing various network protocols or
 * hardware devices.
 * <p>
 * The pull mode, in which the need-data signal triggers the next push-buffer
 * call. This mode is typically used in the "random-access" stream-type. Use
 * this mode for file access or other randomly accessable sources. In this mode,
 * a buffer of exactly the amount of bytes given by the need-data signal should
 * be pushed into appsrc.
 * <p>
 * In all modes, the size property on appsrc should contain the total stream
 * size in bytes. Setting this property is mandatory in the random-access mode.
 * For the stream and seekable modes, setting this property is optional but
 * recommended.
 * <p>
 * When the application has finished pushing data into appsrc, it should call
 * {@link #endOfStream() } or emit the end-of-stream action signal. After this
 * call, no more buffers can be pushed into appsrc until a flushing seek occurs
 * or the state of the appsrc has gone through READY.
 */
public class AppSrc extends BaseSrc {

    public static final String GST_NAME = "appsrc";
    public static final String GTYPE_NAME = "GstAppSrc";

    AppSrc(Initializer init) {
        super(init);
    }

    public AppSrc(String name) {
        this(makeRawElement(GST_NAME, name));
    }

    /**
     * Set the capabilities on the appsrc element. This function takes a copy of
     * the caps structure. After calling this method, the source will only
     * produce caps that match caps . caps must be fixed and the caps on the
     * buffers must match the caps or left NULL.
     */
    @Override
    public void setCaps(Caps caps) {
        APP_API.gst_app_src_set_caps(this, caps);
    }

    /**
     * Get the configured Caps on the AppSrc.
     *
     * @return the caps
     */
    public Caps getCaps() {
        return APP_API.gst_app_src_get_caps(this);
    }

    /**
     * Set the size of the stream in bytes. A value of -1 means that the size is
     * not known.
     *
     * @param size the size to set, or -1 if not known
     */
    public void setSize(long size) {
        APP_API.gst_app_src_set_size(this, size);
    }

    /**
     * Get the size of the stream in bytes. A value of -1 means that the size is
     * not known.
     *
     * @return the size of the stream, or -1 if not specified
     */
    public long getSize() {
        return APP_API.gst_app_src_get_size(this);
    }

    /**
     * Set the stream type on appsrc . For seekable streams, the "seek" signal
     * must be connected to.
     *
     * @param type stream type
     */
    public void setStreamType(AppSrc.StreamType type) {
        APP_API.gst_app_src_set_stream_type(this, type);
    }

    /**
     * Get the stream type set on the appsrc.
     *
     * @return stream type
     */
    public AppSrc.StreamType getStreamType() {
        return APP_API.gst_app_src_get_stream_type(this);
    }

    /**
     * Set the maximum amount of bytes that can be queued in appsrc . After the
     * maximum amount of bytes are queued, appsrc will emit the "enough-data"
     * signal.
     *
     * @param max number of bytes to queue
     */
    public void setMaxBytes(long max) {
        APP_API.gst_app_src_set_max_bytes(this, max);
    }

    /**
     * Get the maximum number of bytes that can be queued.
     *
     * @return maximum number of bytes
     */
    public long getMaxBytes() {
        return APP_API.gst_app_src_get_max_bytes(this);
    }

    /**
     * Configure the min and max latency in src . If min is set to -1, the
     * default latency calculations for pseudo-live sources will be used.
     *
     * @param min the minimum latency
     * @param max the maximum latency
     */
    public void setLatency(long min, long max) {
        APP_API.gst_app_src_set_latency(this, min, max);
    }

    /**
     * Get the latency. The returned value is a long array of length two, with
     * min latency at index 0 and max latency at index 1.
     *
     * @return values as {@code long[]{min, max} }
     */
    public long[] getLatency() {
        LongByReference minRef = new LongByReference();
        LongByReference maxRef = new LongByReference();
        APP_API.gst_app_src_get_latency(this, minRef, minRef);
        return new long[]{minRef.getValue(), maxRef.getValue()};
    }

    /**
     * Adds a buffer to the queue of buffers that the appsrc element will push
     * to its source pad. This function takes ownership of the buffer.
     * <p>
     * When the block property is TRUE, this function can block until free space
     * becomes available in the queue.
     * 
     * @param buffer a {@link Buffer} to push
     * @return GST_FLOW_OK when the buffer was successfully queued.
     * GST_FLOW_FLUSHING when appsrc is not PAUSED or PLAYING. GST_FLOW_EOS when
     * EOS occurred.
     */
    public FlowReturn pushBuffer(Buffer buffer) {
        return APP_API.gst_app_src_push_buffer(this, buffer);
    }

    /**
     * Indicates to the appsrc element that the last buffer queued in the
     * element is the last buffer of the stream.
     * 
     * @return GST_FLOW_OK when the EOS was successfuly queued. GST_FLOW_FLUSHING when
     * appsrc is not PAUSED or PLAYING.
     */
    public FlowReturn endOfStream() {
        return APP_API.gst_app_src_end_of_stream(this);
    }

    /**
     * Signal that the source has enough data. It is recommended that the
     * application stops calling push-buffer until the need-data signal is
     * emitted again to avoid excessive buffer queueing.
     */
    public static interface ENOUGH_DATA {

        /**
         * Enough data signal
         *
         * @param elem the appsrc element that emitted the signal
         */
        public void enoughData(AppSrc elem);
    }

    /**
     * Adds a listener for the <code>enough-data</code> signal
     *
     * @param listener Listener to be called this when appsrc fills its queue.
     */
    public void connect(final ENOUGH_DATA listener) {
        connect(ENOUGH_DATA.class, listener, new GstCallback() {
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
        disconnect(ENOUGH_DATA.class, listener);
    }

    /**
     * Signal that the source needs more data. In the callback or from another
     * thread you should call push-buffer or end-of-stream.
     * <p>
     * length is just a hint and when it is set to -1, any number of bytes can
     * be pushed into appsrc .
     * <p>
     * You can call push-buffer multiple times until the enough-data signal is
     * fired.
     */
    public static interface NEED_DATA {

        /**
         * Need data signal
         *
         * @param elem the appsrc element that emitted the signal
         * @param size the amount of bytes needed, or -1. This is just a hint.
         */
        public void needData(AppSrc elem, int size);
    }

    /**
     * Adds a listener for the <code>need-data</code> signal
     *
     * @param listener Listener to be called when appsrc needs data.
     */
    public void connect(final NEED_DATA listener) {
        connect(NEED_DATA.class, listener, new GstCallback() {
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
        disconnect(NEED_DATA.class, listener);
    }

    /**
     * Seek to the given offset, expressed in terms of the {@link Format} set on
     * the AppSrc. The next push-buffer should produce buffers from the new
     * offset . This callback is only called for seekable stream types.
     */
    public static interface SEEK_DATA {

        /**
         * Seek data signal.
         *
         * @param elem the appsrc element that emitted the signal
         * @param position the offset to seek to, expressed in the
         * {@link Format} set on the AppSrc
         * @return true if the seek succeeded
         */
        public boolean seekData(AppSrc elem, long position);
    }

    /**
     * Adds a listener for the <code>seek-data</code> signal
     *
     * @param listener listener to be called when a seek is requested
     */
    public void connect(final SEEK_DATA listener) {
        connect(SEEK_DATA.class, listener, new GstCallback() {
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
        disconnect(SEEK_DATA.class, listener);
    }

    /**
     * The stream type.
     * <p>
     * See upstream documentation at
     * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-libs/html/gst-plugins-base-libs-appsrc.html#GstAppStreamType"
     * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-libs/html/gst-plugins-base-libs-appsrc.html#GstAppStreamType</a>
     * <p>
     */
    public enum StreamType implements NativeEnum<StreamType> {
        /**
         * No seeking is supported in the stream, such as a live stream.
         */
        STREAM(0),
        /**
         * The stream is seekable but seeking might not be very fast, such as
         * data from a webserver.
         */
        SEEKABLE(1),
        /**
         * The stream is seekable and seeking is fast, such as in a local file.
         */
        RANDOM_ACCESS(2);

        private final int value;

        private StreamType(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }

    }
}
