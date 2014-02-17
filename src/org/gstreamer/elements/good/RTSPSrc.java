/* 
 * Copyright (c) 2010 Tibor Kocsis
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
package org.gstreamer.elements.good;

import org.gstreamer.Bin;

/**
 * Java representation of gstreamer rtspsrc 
 */
public class RTSPSrc extends Bin {
	public static final String GST_NAME = "rtspsrc";
	public static final String GTYPE_NAME = "GstRTSPSrc";
	
	public RTSPSrc(String name) {
		this(makeRawElement(GST_NAME, name));
	}

	public RTSPSrc(Initializer init) {
		super(init);
	}
}
