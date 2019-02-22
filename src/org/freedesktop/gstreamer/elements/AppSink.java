/*
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2016 Christophe Lafolet
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2009 Wayne Meissner
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

import org.freedesktop.gstreamer.Caps;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.FlowReturn;
import org.freedesktop.gstreamer.Sample;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;
import static org.freedesktop.gstreamer.lowlevel.AppAPI.APP_API;

/**
 * A sink {@link Element} that provides an easy way for applications to extract
 * samples from a pipeline.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-libs/html/gst-plugins-base-libs-appsink.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gst-plugins-base-libs/html/gst-plugins-base-libs-appsink.html</a>
 * <p>
 * Appsink is a sink plugin that supports many different methods for making the
 * application get a handle on the GStreamer data in a pipeline. Unlike most
 * GStreamer elements, Appsink provides external API functions.
 * <p>
 * The normal way of retrieving samples from appsink is by using the
 * {@link #pullSample()} and {@link #pullPreroll()} methods. These methods block
 * until a sample becomes available in the sink or when the sink is shut down or
 * reaches EOS. (<em>TODO - not currently mapped in the Java bindings.</em>
 * There are also timed variants of these methods,
 * gst_app_sink_try_pull_sample() and gst_app_sink_try_pull_preroll(), which
 * accept a timeout parameter to limit the amount of time to wait).
 * <p>
 * Appsink will internally use a queue to collect buffers from the streaming
 * thread. If the application is not pulling samples fast enough, this queue
 * will consume a lot of memory over time. The "max-buffers" property can be
 * used to limit the queue size. The "drop" property controls whether the
 * streaming thread blocks or if older buffers are dropped when the maximum
 * queue size is reached. Note that blocking the streaming thread can negatively
 * affect real-time performance and should be avoided.
 * <p>
 * If a blocking behaviour is not desirable, setting the "emit-signals" property
 * to TRUE will make appsink emit the "new-sample" and "new-preroll" signals
 * when a sample can be pulled without blocking.
 * <p>
 * The "caps" property on appsink can be used to control the formats that
 * appsink can receive. This property can contain non-fixed caps, the format of
 * the pulled samples can be obtained by getting the sample caps.
 * <p>
 * If one of the pull-preroll or pull-sample methods return NULL, the appsink is
 * stopped or in the EOS state. You can check for the EOS state with the "eos"
 * property or with the {@link #isEOS()} method.
 * <p>
 * The eos signal can also be used to be informed when the EOS state is reached
 * to avoid polling.
 */
public class AppSink extends BaseSink {

    public static final String GST_NAME = "appsink";
    public static final String GTYPE_NAME = "GstAppSink";

    AppSink(Initializer init) {
        super(init);
    }

    public AppSink(String name) {
        this(makeRawElement(GST_NAME, name));
    }

    /**
     * Sets the {@link Caps} on the AppSink.
     * <p>
     * After calling this method, the sink will only accept Caps that match
     * <tt>caps</tt>. If <tt>caps</tt> is non-fixed, you must check the caps on
     * the buffers to get the actual used caps.
     *
     * @param caps the Caps to set.
     */
    @Override
    public void setCaps(Caps caps) {
        APP_API.gst_app_sink_set_caps(this, caps);
    }

    /**
     * Gets the {@link Caps} configured on this AppSink
     *
     * @return the Caps accepted by this sink
     */
    public Caps getCaps() {
        return APP_API.gst_app_sink_get_caps(this);
    }

    /**
     * Check if this AppSink is EOS, which is when no more samples can be pulled
     * because an EOS event was received.
     * <p>
     * This function also returns TRUE when the AppSink is not in the PAUSED or
     * PLAYING state.
     *
     * @return true if no more buffers can be pulled and this AppSink is EOS.
     */
    public boolean isEOS() {
        return APP_API.gst_app_sink_is_eos(this);
    }

    /**
     * Get the last preroll {@link Sample}. This was the Sample that caused the
     * AppSink to preroll in the PAUSED state.
     * <p>
     * This function is typically used when dealing with a pipeline in the
     * PAUSED state. Calling this function after doing a seek will give the
     * sample right after the seek position.
     * <p>
     * Calling this function will clear the internal reference to the preroll
     * buffer.
     * <p>
     * Note that the preroll sample will also be returned as the first sample
     * when calling {@link #pullSample() }
     * <p>
     * If an EOS event was received before any buffers, this function returns
     * NULL. Use {@link #isEOS() } to check for the EOS condition.
     * <p>
     * This function blocks until a preroll sample or EOS is received or the
     * AppSink element is set to the READY/NULL state.
     *
     * @return a Sample, or null if the AppSink is stopped or EOS
     */
    public Sample pullPreroll() {
        return APP_API.gst_app_sink_pull_preroll(this);
    }

    /**
     * This function blocks until a {@link Sample} or EOS becomes available or
     * the AppSink element is set to the READY/NULL state.
     * <p>
     * This function will only return samples when the AppSink is in the PLAYING
     * state. All rendered buffers will be put in a queue so that the
     * application can pull samples at its own rate. Note that when the
     * application does not pull samples fast enough, the queued buffers could
     * consume a lot of memory, especially when dealing with raw video frames.
     * <p>
     * If an EOS event was received before any buffers, this function returns
     * NULL. Use {@link #isEOS() } to check for the EOS condition.
     *
     * @return a Sample, or null if the AppSink is stopped or EOS
     */
    public Sample pullSample() {
        return APP_API.gst_app_sink_pull_sample(this);
    }

    /**
     * Signal emitted when this {@link AppSink} got EOS.
     */
    public static interface EOS {

        /**
         * EOS signal
         *
         * @param elem AppSink
         */
        public void eos(AppSink elem);
    }

    /**
     * Adds a listener for the <code>eos</code> signal.
     *
     * @param listener
     */
    public void connect(final EOS listener) {
        connect(EOS.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(AppSink elem) {
                listener.eos(elem);
            }
        });
    }

    /**
     * Removes a listener for the <code>eos</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(EOS listener) {
        disconnect(EOS.class, listener);
    }

    /**
     * Signal emitted when this {@link AppSink} when a new Sample is ready.
     */
    public static interface NEW_SAMPLE {

        /**
         * New Sample signal
         *
         * @param elem AppSink
         * @return FlowReturn
         */
        public FlowReturn newSample(AppSink elem);
    }

    /**
     * Adds a listener for the <code>new-sample</code> signal. If a blocking
     * behaviour is not desirable, setting the "emit-signals" property to TRUE
     * will make appsink emit the "new-sample" and "new-preroll" signals when a
     * buffer can be pulled without blocking.
     *
     * @param listener
     */
    public void connect(final NEW_SAMPLE listener) {
        connect(NEW_SAMPLE.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public FlowReturn callback(AppSink elem) {
                return listener.newSample(elem);
            }
        });
    }

    /**
     * Removes a listener for the <code>new-sample</code> signal
     *
     * @param listener a listener that was previously added.
     */
    public void disconnect(NEW_SAMPLE listener) {
        disconnect(NEW_SAMPLE.class, listener);
    }

    /**
     * Signal emitted when this {@link AppSink} when a new buffer is ready.
     */
    public static interface NEW_PREROLL {

        /**
         * New preroll signal
         *
         * @param elem AppSink
         * @return FlowReturn
         */
        public FlowReturn newPreroll(AppSink elem);
    }

    /**
     * Adds a listener for the <code>new-preroll</code> signal. If a blocking
     * behaviour is not desirable, setting the "emit-signals" property to TRUE
     * will make appsink emit the "new-buffer" and "new-preroll" signals when a
     * buffer can be pulled without blocking.
     *
     * @param listener
     */
    public void connect(final NEW_PREROLL listener) {
        connect(NEW_PREROLL.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public FlowReturn callback(AppSink elem) {
                return listener.newPreroll(elem);
            }
        });
    }

    /**
     * Removes a listener for the <code>new-preroll</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(NEW_PREROLL listener) {
        disconnect(NEW_PREROLL.class, listener);
    }

}
