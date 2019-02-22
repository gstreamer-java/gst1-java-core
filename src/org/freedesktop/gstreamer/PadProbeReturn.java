/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer;

/**
 * Different return values for Pad probe callbacks
 */
public enum PadProbeReturn {
    /**
     * Drop data in data probes. For push mode this means that the data item is
     * not sent downstream. For pull mode, it means that the data item is not
     * passed upstream. In both cases, no other probes are called for this item
     * and %GST_FLOW_OK or %TRUE is returned to the caller.
     */
    DROP,
    /**
     * Normal probe return value. This leaves the probe in place, and defers
     * decisions about dropping or passing data to other probes, if any. If
     * there are no other probes, the default behaviour for the probe type
     * applies ('block' for blocking probes, and 'pass' for non-blocking
     * probes).
     */
    OK,
    /**
     * Remove the probe
     */
    REMOVE,
    /**
     * Pass the data item in the block probe and block on the next item.
     */
    PASS,
    /**
     * Data has been handled in the probe and will not be forwarded further. For
     * events and buffers this is the same behaviour as %GST_PAD_PROBE_DROP
     * (except that in this case you need to unref the buffer or event
     * yourself). For queries it will also return %TRUE to the caller. The probe
     * can also modify the #GstFlowReturn value by using the
     * #GST_PAD_PROBE_INFO_FLOW_RETURN() accessor. Note that the resulting query
     * must contain valid entries. Since: 1.6
     */
    HANDLED;
}
