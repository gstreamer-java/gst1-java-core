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
package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GstMemoryAPI.GSTMEMORY_API;

import java.util.stream.Stream;

import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstBufferAPI;
import org.freedesktop.gstreamer.lowlevel.GstBufferAPI.MapInfoStruct;
import org.freedesktop.gstreamer.lowlevel.GstMemoryAPI;

public class Memory extends MiniObject {

	public static final String GTYPE_NAME = "GstMemory";

	protected final MapInfoStruct mapInfo;

	/**
	 * This constructor is for internal use only.
	 * 
	 * @param init initialization data.
	 */
	protected Memory(Initializer init) {
		super(init);
		mapInfo = new MapInfoStruct();
	}

	public boolean map(boolean writable) {
    	boolean isOk = 
         GSTMEMORY_API.gst_memory_map(this, mapInfo,
                writable ? getWriteFlags() : getReadFlags());
    	return isOk;
	}

	public void unmap() {
		GSTMEMORY_API.gst_memory_unmap(this, mapInfo);
	}

    protected int getReadFlags() {
		return GstBufferAPI.GST_MAP_READ;
	}

    protected int getWriteFlags() {
		return GstBufferAPI.GST_MAP_WRITE;
	}

	private static Memory create(final Initializer init) {
		GstMemoryAPI.GstMemoryStruct struct = new GstMemoryAPI.GstMemoryStruct(init.ptr.getPointer());
		Allocator allocator = (Allocator) struct.readField("allocator");
		return allocator.getFactory().apply(init);
	}

	public static class Types implements TypeProvider {

		@Override
		public Stream<TypeRegistration<?>> types() {
            return Stream.of(
                    Natives.registration(Memory.class, GTYPE_NAME, Memory::create)
            );
		}

	}

}
