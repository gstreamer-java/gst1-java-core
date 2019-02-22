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

import org.freedesktop.gstreamer.message.Message;
import org.freedesktop.gstreamer.interfaces.VideoOverlay;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public interface GstVideoOverlayAPI extends Library {
	GstVideoOverlayAPI GSTVIDEOOVERLAY_API = GstNative.load("gstvideo", GstVideoOverlayAPI.class);

	GType gst_video_overlay_get_type();

	void gst_video_overlay_set_window_handle(VideoOverlay overlay, Pointer xwindow_id);

	void gst_video_overlay_got_window_handle(VideoOverlay overlay, NativeLong xwindow_id);

	void gst_video_overlay_prepare_xwindow_id(VideoOverlay overlay);

	void gst_video_overlay_expose(VideoOverlay overlay);

	void gst_video_overlay_handle_events(VideoOverlay overlay, boolean handle_events);
	
	boolean	gst_video_overlay_set_render_rectangle(VideoOverlay overlay, int x, int y, int width, int height);

	boolean gst_is_video_overlay_prepare_window_handle_message(Message message);
}
