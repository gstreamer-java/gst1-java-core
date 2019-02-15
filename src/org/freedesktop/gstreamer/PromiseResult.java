/*
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2018 Antonio Morales
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License version 3 only, as published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3 along with
 * this work. If not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.annotations.DefaultEnumValue;

/**
 * The result of a {@link Promise}
 * Available since GStreamer 1.14
 */
@Gst.Since(minor = 14)
public enum PromiseResult {
    /** Initial state. Waiting for transition to any other state. */
    @DefaultEnumValue
    PENDING,
    /** Interrupted by the consumer as it doesn't want the value anymore. */
    INTERRUPTED,
    /** A producer marked a reply. */
    REPLIED,
    /** The promise expired (the carrying object lost all refs) and the promise
     * will never be fulfilled. */
    EXPIRED
}
