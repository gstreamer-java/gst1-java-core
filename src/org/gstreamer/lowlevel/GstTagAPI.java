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

import org.gstreamer.TagFlag;

public interface GstTagAPI extends com.sun.jna.Library {
	GstTagAPI GSTTAG_API = GstNative.load(GstTagAPI.class);

    boolean gst_tag_exists(String tag);
    GType gst_tag_get_type(String tag);
    String gst_tag_get_nick(String tag);
    String gst_tag_get_description(String tag);
    TagFlag gst_tag_get_flag(String tag);
    boolean gst_tag_is_fixed(String tag);
   
}
