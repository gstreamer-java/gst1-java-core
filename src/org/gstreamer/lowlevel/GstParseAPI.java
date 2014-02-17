/* 
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007, 2008 Wayne Meissner
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

import org.gstreamer.Bin;
import org.gstreamer.Pipeline;
import org.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

/**
 * gstparse functions
 */
public interface GstParseAPI extends com.sun.jna.Library {
	GstParseAPI GSTPARSE_API = GstNative.load(GstParseAPI.class);

    @CallerOwnsReturn Pipeline gst_parse_launch(String pipeline_description, Pointer[] error);
    @CallerOwnsReturn Pipeline gst_parse_launchv(String[] pipeline_description, Pointer[] error);
    @CallerOwnsReturn Pipeline gst_parse_launch(String pipeline_description, GstAPI.GErrorStruct[] error);
    @CallerOwnsReturn Pipeline gst_parse_launchv(String[] pipeline_description, GstAPI.GErrorStruct[] error);
    @CallerOwnsReturn Bin gst_parse_bin_from_description(String bin_description, boolean ghost_unlinked_pads,Pointer[] error);
}
