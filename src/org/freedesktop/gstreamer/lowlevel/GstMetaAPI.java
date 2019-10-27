/*
 * Copyright (c) 2016 Christophe Lafolet
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

import java.util.Arrays;
import java.util.List;

import org.freedesktop.gstreamer.glib.GQuark;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.Structure.ByReference;

public interface GstMetaAPI extends Library {

	GstMetaAPI GSTMETA_API = GstNative.load(GstMetaAPI.class);

	public static final class GstMetaStruct extends com.sun.jna.Structure {

		public volatile int flags;
		public volatile GstMetaInfoByReference info;

		public GstMetaStruct(Pointer ptr) {
			super(ptr);
			read();
		}

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { "flags", "info" });
		}
	}

	public static class GstMetaInfo extends com.sun.jna.Structure {

		public volatile GType api; // ex: GstVideoMetaAPI, GstVideoGLTextureUploadMetaAPI, ...
		public volatile GType type; // ex: GstVideoMeta, GstVideoGLTextureUploadMeta, ...
		public volatile int size;
		public volatile Pointer init_func;
		public volatile Pointer free_func;
		public volatile Pointer transform_func;

		/* < private > */
		public volatile Pointer[] _gst_reserved = new Pointer[GstAPI.GST_PADDING];

		public GstMetaInfo(Pointer ptr) {
			super(ptr);
		}

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { "api", "type", "size", "init_func", "free_func", "transform_func",
					"_gst_reserved" });
		}
	}

	public static final class GstMetaInfoByReference extends GstMetaInfo implements ByReference {
		public GstMetaInfoByReference(Pointer ptr) {
			super(ptr);
		}

	}

	boolean gst_meta_api_type_has_tag(GType api, GQuark tag);
}
