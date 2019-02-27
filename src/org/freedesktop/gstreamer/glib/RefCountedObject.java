/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer.glib;

import org.freedesktop.gstreamer.lowlevel.GPointer;

/**
 * A {@link NativeObject} that has an associated reference count
 */
public abstract class RefCountedObject extends NativeObject {

//    /**
//     * Creates a new instance of RefCountedObject
//     */
//    protected RefCountedObject(Initializer init) {
//        this(new Handle())
//        if (init.ownsHandle && init.needRef) {
//            ref();
//        }
//    }

    protected RefCountedObject(Handle handle) {
        super(handle);

    }
    
    protected RefCountedObject(Handle handle, boolean needRef) {
        super(handle);
        if (needRef) {
            handle.ref();
        }
    }
    


    // overridden in subclasses
//    protected abstract void ref();
//
//    protected abstract void unref();

    protected static abstract class Handle extends NativeObject.Handle {

        public Handle(GPointer ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        protected abstract void ref();

        protected abstract void unref();

    }
}
