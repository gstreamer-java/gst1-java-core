/*
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
 */
public enum PromiseResult {
    /** The initial state of a promise */
    PENDING,
    /** The promise was interrupted */
    INTERRUPTED,
    /** The promise has been resolved and it has a value */
    REPLIED,
    /** The promise is expired and won't return a result */
    EXPIRED,
    /** Unknown result */
    @DefaultEnumValue UNKNOWN;
}
