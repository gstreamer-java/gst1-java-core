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

package org.freedesktop.gstreamer.lowlevel;

import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.interfaces.Navigation;

import com.sun.jna.Library;

public interface GstNavigationAPI extends Library {
	GstNavigationAPI GSTNAVIGATION_API = GstNative.load("gstvideo", GstNavigationAPI.class);

	GType gst_navigation_get_type();

	/* vitrual class functions */
	void gst_navigation_send_event(Navigation navigation, Structure structure);

	void gst_navigation_send_key_event(Navigation navigation, String event, String key);

	void gst_navigation_send_mouse_event(Navigation navigation, String event, 
						int button, double x, double y);
}
