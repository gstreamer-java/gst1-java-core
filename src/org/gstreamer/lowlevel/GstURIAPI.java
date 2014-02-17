/* 
 * Copyright (c) 2010 DHoyt <david.g.hoyt@gmail.com>
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

package org.gstreamer.lowlevel;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

import org.gstreamer.URIType;
import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.annotations.CallerOwnsReturn;

/**
 * The URIHandler is an interface that is implemented by Source and Sink GstElement to simplify then handling of URI.
 * An application can use the following functions to quickly get an element that handles the given URI for reading or 
 * writing (gst_element_make_from_uri()).
 * Source and Sink plugins should implement this interface when possible.
 */
public interface GstURIAPI extends Library {
	GstURIAPI GSTURI_API = GstNative.load(GstURIAPI.class);
	
	boolean gst_uri_protocol_is_valid(String protocol);

	boolean gst_uri_protocol_is_supported(URIType type, String protocol);

	boolean gst_uri_is_valid(String uri);

	boolean gst_uri_has_protocol(String uri, String protocol);

	@CallerOwnsReturn
	Pointer gst_element_make_from_uri(URIType type, String uri, String name);
}
