/*
 * Copyright (c) 2010 Levente Farkas
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

package org.freedesktop.gstreamer.controller;

import org.freedesktop.gstreamer.GObject;

import com.sun.jna.Pointer;

/**
 * The ControlSource is a base class for control value sources that could be used by Controller to get timestamp-value pairs.
 */
public class ControlSource extends GObject {
	//private static Logger logger = Logger.getLogger(ControlSource.class.getName());
	
    /**
     * For internal gstreamer-java use only
     *
     * @param init initialization data
     */
    public ControlSource(Initializer init) {
        super(init);
        throw new IllegalArgumentException("Cannot instantiate this class");
    }

    public ControlSource(Pointer ptr, boolean needRef, boolean ownsHandle) {
        super(initializer(ptr, needRef, ownsHandle));
    }
}
