/*
 * Copyright (c) 2019 Neil C Smith
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

import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.MiniObjectStruct;
import org.freedesktop.gstreamer.glib.RefCountedObject;
import org.freedesktop.gstreamer.lowlevel.GPointer;

import static org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.GSTMINIOBJECT_API;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectPtr;

/**
 * Lightweight base class for the GStreamer object hierarchy
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/gstreamer-GstMiniObject.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/gstreamer-GstMiniObject.html</a>
 * <p>
 * MiniObject is a simple structure that can be used to implement refcounted
 * types.
 */
public abstract class MiniObject extends RefCountedObject {

    /**
     * Creates a new instance of MiniObject
     */
    protected MiniObject(Initializer init) {
        this(new Handle(init.ptr.as(GstMiniObjectPtr.class, GstMiniObjectPtr::new),
                init.ownsHandle), init.needRef);
    }
    
    protected MiniObject(Handle handle, boolean needRef) {
        super(handle, needRef);
    }

    /**
     * If mini_object has the LOCKABLE flag set, check if the current EXCLUSIVE
     * lock on object is the only one, this means that changes to the object
     * will not be visible to any other object.
     *
     * <p>
     * </p>If the LOCKABLE flag is not set, check if the refcount of mini_object
     * is exactly 1, meaning that no other reference exists to the object and
     * that the object is therefore writable.
     *
     * <p>
     * </p>Modification of a mini-object should only be done after verifying
     * that it is writable.
     *
     * @return true if the object is writable.
     */
    public boolean isWritable() {
        return GSTMINIOBJECT_API.gst_mini_object_is_writable(this);
    }

    /**
     * Makes a writable instance of this MiniObject.
     * <p>
     * The result is cast to <tt>subclass</tt>.
     *
     * @return a writable version (possibly a duplicate) of this MiniObject.
     */
    protected <T extends MiniObject> T makeWritable() {
        MiniObject result = GSTMINIOBJECT_API.gst_mini_object_make_writable(this);
        if (result == null) {
            throw new NullPointerException("Could not make " + this.getClass().getSimpleName() + " writable");
        }
        return (T) result;
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
        return (T) result;
    }

    public int getRefCount() {
        final MiniObjectStruct struct = new MiniObjectStruct(getRawPointer());
        return (Integer) struct.readField("refcount");
    }

    private static final class Handle extends RefCountedObject.Handle {

        public Handle(GstMiniObjectPtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GSTMINIOBJECT_API.gst_mini_object_unref(
                    ptr.as(GstMiniObjectPtr.class, GstMiniObjectPtr::new));
        }

        @Override
        protected void ref() {
            GSTMINIOBJECT_API.gst_mini_object_ref(getPointer());
        }

        @Override
        protected void unref() {
            GSTMINIOBJECT_API.gst_mini_object_unref(getPointer());
        }

        @Override
        protected GstMiniObjectPtr getPointer() {
            return (GstMiniObjectPtr) super.getPointer();
        }

    }
}
