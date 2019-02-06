/* 
 * Copyright (c) 2019 Neil c Smith
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

import static org.freedesktop.gstreamer.lowlevel.GstVideoOrientationAPI.GSTVIDEOORIENTATION_API;

public class VideoOrientation extends GstInterface {
	/**
	 * Wraps the {@link Element} in a <tt>VideoOrientation</tt> interface
	 * 
	 * @param element
	 *            the element to use as a <tt>VideoOrientation</tt>
	 * @return a <tt>VideoOrientation</tt> for the element
	 */
	public static final VideoOrientation wrap(Element element) {
		return new VideoOrientation(element);
	}

	/**
	 * Creates a new VideoOrientation instance
	 * 
	 * @param element
	 *            the element that implements the VideoOrientation interface
	 */
	private VideoOrientation(Element element) {
		super(element, GSTVIDEOORIENTATION_API.gst_video_orientation_get_type());
	}

//	public boolean getHflip(boolean flip) {
//		return GSTVIDEOORIENTATION_API.gst_video_orientation_get_hflip(this, flip);
//	}
//
//	public boolean getVflip(boolean flip) {
//		return GSTVIDEOORIENTATION_API.gst_video_orientation_get_vflip(this, flip);
//	}
//
//	public boolean getHcenter(int center) {
//		return GSTVIDEOORIENTATION_API.gst_video_orientation_get_hcenter(this, center);
//	}
//
//	public boolean getVcenter(int center) {
//		return GSTVIDEOORIENTATION_API.gst_video_orientation_get_vcenter(this, center);
//	}

	public boolean setHflip(boolean flip) {
		return GSTVIDEOORIENTATION_API.gst_video_orientation_set_hflip(this, flip);
	}

	public boolean setVflip(boolean flip) {
		return GSTVIDEOORIENTATION_API.gst_video_orientation_set_vflip(this, flip);
	}

	public boolean setHcenter(int center) {
		return GSTVIDEOORIENTATION_API.gst_video_orientation_set_hcenter(this, center);
	}

	public boolean setVcenter(int center) {
		return GSTVIDEOORIENTATION_API.gst_video_orientation_set_vcenter(this, center);
	}
}
