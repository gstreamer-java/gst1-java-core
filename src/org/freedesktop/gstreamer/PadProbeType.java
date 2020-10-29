/* 
 * Copyright (c) 2020 Neil C Smith
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
package org.freedesktop.gstreamer;

import java.util.Collections;
import java.util.Set;
import org.freedesktop.gstreamer.glib.NativeFlags;
import org.freedesktop.gstreamer.lowlevel.GstPadAPI;

/**
 * The different probing types that can occur. When either one of {@link #IDLE}
 * or {@link #BLOCK} is used, the probe will be a blocking probe.
 * <p>
 * For convenience, the various flag combinations are provided as immutable
 * Sets.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/documentation/gstreamer/gstpad.html#GstPadProbeType"
 * >https://gstreamer.freedesktop.org/documentation/gstreamer/gstpad.html#GstPadProbeType</a>
 */
public enum PadProbeType implements NativeFlags<PadProbeType> {

    /* flags to control blocking */
    /**
     * Probe idle pads and block while the callback is called.
     */
    IDLE(GstPadAPI.GST_PAD_PROBE_TYPE_IDLE),
    /**
     * Probe and block pads.
     */
    BLOCK(GstPadAPI.GST_PAD_PROBE_TYPE_BLOCK),
    /* flags to select datatypes */
    /**
     * Probe buffers.
     */
    BUFFER(GstPadAPI.GST_PAD_PROBE_TYPE_BUFFER),
    /**
     * Probe buffer lists.
     */
    BUFFER_LIST(GstPadAPI.GST_PAD_PROBE_TYPE_BUFFER_LIST),
    /**
     * Probe downstream events.
     */
    EVENT_DOWNSTREAM(GstPadAPI.GST_PAD_PROBE_TYPE_EVENT_DOWNSTREAM),
    /**
     * Probe upstream events.
     */
    EVENT_UPSTREAM(GstPadAPI.GST_PAD_PROBE_TYPE_EVENT_UPSTREAM),
    /**
     * Probe flush events. This probe has to be explicitly enabled and is not
     * included in the DOWNSTREAM or UPSTREAM probe types.
     */
    EVENT_FLUSH(GstPadAPI.GST_PAD_PROBE_TYPE_EVENT_FLUSH),
    /**
     * Probe downstream queries.
     */
    QUERY_DOWNSTREAM(GstPadAPI.GST_PAD_PROBE_TYPE_QUERY_DOWNSTREAM),
    /**
     * Probe upstream queries.
     */
    QUERY_UPSTREAM(GstPadAPI.GST_PAD_PROBE_TYPE_QUERY_UPSTREAM),
    /* flags to select scheduling mode */
    /**
     * Probe push.
     */
    PUSH(GstPadAPI.GST_PAD_PROBE_TYPE_PUSH),
    /**
     * Probe pull.
     */
    PULL(GstPadAPI.GST_PAD_PROBE_TYPE_PULL);

    /**
     * Probe and block at the next opportunity, at data flow or when idle.
     */
    public static final Set<PadProbeType> BLOCKING
            = Collections.unmodifiableSet(NativeFlags.fromInt(PadProbeType.class,
                    GstPadAPI.GST_PAD_PROBE_TYPE_BLOCKING));

    /**
     * Probe downstream data (buffers, buffer lists and events).
     */
    public static final Set<PadProbeType> DATA_DOWNSTREAM
            = Collections.unmodifiableSet(NativeFlags.fromInt(PadProbeType.class,
                    GstPadAPI.GST_PAD_PROBE_TYPE_DATA_DOWNSTREAM));

    /**
     * Probe upstream data (events).
     */
    public static final Set<PadProbeType> DATA_UPSTREAM
            = Collections.unmodifiableSet(NativeFlags.fromInt(PadProbeType.class,
                    GstPadAPI.GST_PAD_PROBE_TYPE_DATA_UPSTREAM));

    /**
     * Probe upstream and downstream data (buffers, buffer lists and events).
     */
    public static final Set<PadProbeType> DATA_BOTH
            = Collections.unmodifiableSet(NativeFlags.fromInt(PadProbeType.class,
                    GstPadAPI.GST_PAD_PROBE_TYPE_DATA_BOTH));

    /**
     * Probe and block downstream data (buffers, buffer lists and events).
     */
    public static final Set<PadProbeType> BLOCK_DOWNSTREAM
            = Collections.unmodifiableSet(NativeFlags.fromInt(PadProbeType.class,
                    GstPadAPI.GST_PAD_PROBE_TYPE_BLOCK_DOWNSTREAM));

    /**
     * Probe and block upstream data (events).
     */
    public static final Set<PadProbeType> BLOCK_UPSTREAM
            = Collections.unmodifiableSet(NativeFlags.fromInt(PadProbeType.class,
                    GstPadAPI.GST_PAD_PROBE_TYPE_BLOCK_UPSTREAM));

    /**
     * Probe upstream and downstream events.
     */
    public static final Set<PadProbeType> EVENT_BOTH
            = Collections.unmodifiableSet(NativeFlags.fromInt(PadProbeType.class,
                    GstPadAPI.GST_PAD_PROBE_TYPE_EVENT_BOTH));

    /**
     * Probe upstream and downstream queries.
     */
    public static final Set<PadProbeType> QUERY_BOTH
            = Collections.unmodifiableSet(NativeFlags.fromInt(PadProbeType.class,
                    GstPadAPI.GST_PAD_PROBE_TYPE_QUERY_BOTH));

    /**
     * Probe upstream events and queries and downstream buffers, buffer lists,
     * events and queries.
     */
    public static final Set<PadProbeType> ALL_BOTH
            = Collections.unmodifiableSet(NativeFlags.fromInt(PadProbeType.class,
                    GstPadAPI.GST_PAD_PROBE_TYPE_ALL_BOTH));

    /**
     * Probe push and pull.
     */
    public static final Set<PadProbeType> SCHEDULING
            = Collections.unmodifiableSet(NativeFlags.fromInt(PadProbeType.class,
                    GstPadAPI.GST_PAD_PROBE_TYPE_SCHEDULING));

    private final int value;

    private PadProbeType(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }

}
