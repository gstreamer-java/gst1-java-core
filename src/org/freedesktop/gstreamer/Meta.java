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
package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GlibAPI;
import org.freedesktop.gstreamer.lowlevel.GstMetaAPI.GstMetaStruct;
import org.freedesktop.gstreamer.lowlevel.NativeObject;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * 
 * Base class for Meta
 *
 */
public class Meta extends NativeObject {

	private final GstMetaStruct struct;
	
    /**
     * This constructor is for internal use only.
     * @param init initialization data.
     */
	public Meta(Initializer init) {
		super(init);
		this.struct = new GstMetaStruct(this.handle());
	}

    /**
     * Gives the type value.
     */
    public static GType getType(Pointer ptr) {
    	// Quick getter for GType without allocation
    	// same as : new GstMetaStruct(ptr).info.type
    	Pointer info = ptr.getPointer(Native.POINTER_SIZE);
    	return GType.valueOf(info.getNativeLong(Native.SIZE_T_SIZE).longValue());
    }

    /**
     * Tag identifying the metadata structure and api
     * @return
     */
    public GType getAPIType() {
    	return this.struct.info.api;  	
    }

    /**
     * Type identifying the implementor of the api
     * @return 
     */
    public GType getType() {
    	return this.struct.info.type;  	
    }

	@Override
	protected void disposeNativeHandle(Pointer ptr) {
		if (this.ownsHandle.get()) {
			GlibAPI.GLIB_API.g_free(ptr);
		}
	}
	
	public String toString() {
		return "[meta : gType=" + getType() + "]";
	}

}
