/*
 * Copyright (c) 2016 Christophe Lafolet
 * Copyright (c) 2014 Tom Greenwood <tgreenwood@cafex.com>
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007 Wayne Meissner
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

import com.sun.jna.Pointer;

import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.MiniObjectStruct;
import org.freedesktop.gstreamer.lowlevel.RefCountedObject;

import static org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.GSTMINIOBJECT_API;

/**
 * Lightweight base class for the GStreamer object hierarchy
 *
 * MiniObject is a baseclass like {@link GObject}, but has been stripped down of
 * features to be fast and small.
 * It offers sub-classing and ref-counting in the same way as GObject does.
 * It has no properties and no signal-support though.
 */
public class MiniObject extends RefCountedObject {
    /**
     * Creates a new instance of MiniObject
     */
    public MiniObject(Initializer init) {
        super(init);
    }

    /**
     * Gives the type value.
     */
    public static GType getType(Pointer ptr) {
    	// Quick getter for GType without allocation
    	// same as : new MiniObjectStruct(ptr).type
    	return GType.valueOf(ptr.getNativeLong(0).longValue());
    }

    /**
     * Checks if a mini-object is writable.  A mini-object is writable
     * if the reference count is one and the {@link MiniObjectFlags#READONLY}
     * flag is not set.  Modification of a mini-object should only be
     * done after verifying that it is writable.
     *
     * @return true if the object is writable.
     */
    public boolean isWritable() {
        return GSTMINIOBJECT_API.gst_mini_object_is_writable(this);
    }

    /**
     * Makes a writable instance of this MiniObject.
     * <p> The result is cast to <tt>subclass</tt>.
     *
     * @return a writable version (possibly a duplicate) of this MiniObject.
     */
    protected <T extends MiniObject> T makeWritable() {
        MiniObject result = GSTMINIOBJECT_API.gst_mini_object_make_writable(this);
        if (result == null) {
            throw new NullPointerException("Could not make " + this.getClass().getSimpleName() + " writable");
        }
        return (T)result;
    }

    /**
     * Create a new MiniObject as a copy of the this instance.
     *
     * @return the new MiniObject.
     */
    public <T extends MiniObject> T copy() {
    	MiniObject result = GSTMINIOBJECT_API.gst_mini_object_copy(this);
        if (result == null) {
            throw new NullPointerException("Could not make a copy of " + this.getClass().getSimpleName());
        }
        return (T)result;
    }

    @Override
	protected void ref() {
        GSTMINIOBJECT_API.gst_mini_object_ref(this);
    }
    
    @Override
	protected void unref() {
    	GSTMINIOBJECT_API.gst_mini_object_unref(this);
    }

    public int getRefCount() {
    	final MiniObjectStruct struct = new MiniObjectStruct(handle());
    	return (Integer) struct.readField("refcount");
    }

    @Override
	protected void disposeNativeHandle(Pointer ptr) {
    	if (ownsHandle.get()) {
    		GSTMINIOBJECT_API.gst_mini_object_unref(ptr);
    	}
    }    
}
