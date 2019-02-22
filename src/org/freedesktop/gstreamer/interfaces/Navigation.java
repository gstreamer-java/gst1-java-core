/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2009 Tamas Korodi <kotyo@zamba.fm>
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

package org.freedesktop.gstreamer.interfaces;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Structure;

import static org.freedesktop.gstreamer.lowlevel.GstNavigationAPI.GSTNAVIGATION_API;

public class Navigation extends GstInterface {
	/**
	 * Wraps the {@link Element} in a <tt>Navigation</tt> interface
	 * 
	 * @param element
	 *            the element to use as a <tt>Navigation</tt>
	 * @return a <tt>Navigation</tt> for the element
	 */
	public static final Navigation wrap(Element element) {
		return new Navigation(element);
	}

	/**
	 * Creates a new Navigation instance
	 * 
	 * @param element
	 *            the element that implements the Navigation interface
	 */
	private Navigation(Element element) {
		super(element, GSTNAVIGATION_API.gst_navigation_get_type());
	}

	public void sendEvent(Structure structure) {
		GSTNAVIGATION_API.gst_navigation_send_event(this, structure);
	}

	public void sendKeyEvent(String event, String key) {
		GSTNAVIGATION_API.gst_navigation_send_key_event(this, event, key);
	}

	public void sendMouseEvent(String event, int button, double x, double y) {
		GSTNAVIGATION_API.gst_navigation_send_mouse_event(this, event, button, x, y);
	}
}
