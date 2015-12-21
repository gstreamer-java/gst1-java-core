/*
 * Copyright (c) 2015 Christophe Lafolet
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

import org.freedesktop.gstreamer.glib.GQuark;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.MiniObjectStruct;
import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.RefCountedObject;

import com.sun.jna.FromNativeConverter;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.TypeMapper;

/**
 * Lightweight base class for the GStreamer object hierarchy
 *
 * MiniObject is a baseclass like {@link GObject}, but has been stripped down of
 * features to be fast and small.
 * It offers sub-classing and ref-counting in the same way as GObject does.
 * It has no properties and no signal-support though.
 */
public class MiniObject extends RefCountedObject {
    private static final GstMiniObjectAPI gst = GstNative.load(GstMiniObjectAPI.class);
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
    	return new GType(ptr.getNativeLong(0).longValue());
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
        return MiniObject.gst.gst_mini_object_is_writable(this);
    }

    /**
     * Makes a writable instance of this MiniObject.
     * <p> The result is cast to <tt>subclass</tt>.
     *
     * @return a writable version (possibly a duplicate) of this MiniObject.
     */
    protected <T extends MiniObject> T makeWritable() {
        MiniObject result = MiniObject.gst.gst_mini_object_make_writable(this);
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
    	MiniObject result = MiniObject.gst.gst_mini_object_copy(this);
        if (result == null) {
            throw new NullPointerException("Could not make a copy of " + this.getClass().getSimpleName());
        }
        return (T)result;
    }

    /**
     * This methods is the same as {@link MiniObject#copy copy()} but it do not create MiniObject.
     * @return a pointer on MiniObject.
     */
    protected Pointer ptr_copy() {
    	return MiniObject.gst.ptr_gst_mini_object_copy(this.handle());
    }

    /**
     * This function gets back user data stored via {@link MiniObject#setUserData setUserData()}.
     * @param quark A GQuark, naming the user data
     * @param dataClass
     * @return The user data set, or NULL.
     */
    public <T> T getUserData(GQuark quark, Class<T> dataClass) {
        TypeMapper mapper = Native.getTypeMapper(GstMiniObjectAPI.class);
        if (mapper != null) {
        	FromNativeConverter nc = mapper.getFromNativeConverter(dataClass);
            if (nc != null) {
            	Pointer ptr = MiniObject.gst.gst_mini_object_get_qdata(this, quark);
            	return (T)nc.fromNative(ptr, null);
            }
            throw new IllegalStateException("No converter found for " + dataClass);
        }
        throw new IllegalStateException("No TypeMapper found for " + dataClass + " : can't convert native pointer");
    }

    /**
     * This function store user data in a miniobject
     * @param quark A GQuark, naming the user data
     * @param data
     */
    public void setUserData(GQuark quark, Object data) {
    	MiniObject.gst.gst_mini_object_set_qdata(this, quark, data, null);
    }

    @Override
	protected void ref() {
        MiniObject.gst.gst_mini_object_ref(this);
    }
    @Override
	protected void unref() {
    	MiniObject.gst.gst_mini_object_unref(this);
    }

    public int getRefCount() {
    	final MiniObjectStruct struct = new MiniObjectStruct(this.handle());
    	return (Integer) struct.readField("refcount");
    }

    @Override
	protected void disposeNativeHandle(Pointer ptr) {
    	if (this.ownsHandle.get()) {
    		MiniObject.gst.gst_mini_object_unref(ptr);
    	}
    }

}
