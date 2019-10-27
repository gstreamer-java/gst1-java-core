/*
 * Copyright (c) 2019 Christophe Lafolet
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

import org.freedesktop.gstreamer.Allocator;
import org.freedesktop.gstreamer.Memory;
import org.freedesktop.gstreamer.lowlevel.GstBufferAPI.MapInfoStruct;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.MiniObjectStruct;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public interface GstMemoryAPI extends com.sun.jna.Library {

	GstMemoryAPI GSTMEMORY_API = GstNative.load(GstMemoryAPI.class);

	public static final class GstMemoryStruct extends com.sun.jna.Structure {

		public volatile MiniObjectStruct mini_object;

        public volatile Allocator /* GstAllocator */ allocator;
		public volatile Pointer /* GstMemory */ parent;

		public volatile NativeLong maxSize;
		public volatile NativeLong align;
		public volatile NativeLong offset;
		public volatile NativeLong size;

        /**
         * Creates a new instance of GstMemoryStruct
         */
		public GstMemoryStruct() {
		}

		public GstMemoryStruct(Pointer ptr) {
			useMemory(ptr);
		}

		@Override
		protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "mini_object", 
                "allocator", "parent", 
                "maxSize", "align", "offset", "size"
            });
		}
	}

	GType gst_memory_get_type();

	boolean gst_memory_is_type(Memory mem, String mem_type);

	boolean gst_memory_map(Memory mem, MapInfoStruct info, int flags);

	void gst_memory_unmap(Memory mem, MapInfoStruct info);
}
