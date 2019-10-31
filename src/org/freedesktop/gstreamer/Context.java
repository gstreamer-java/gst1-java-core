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

import org.freedesktop.gstreamer.lowlevel.GstContextAPI;
import org.freedesktop.gstreamer.lowlevel.GstContextPtr;

/**
 * Lightweight objects to represent element contexts.
 * <p>
 * See upstream documentation at <a href=
 * "https://gstreamer.freedesktop.org/documentation/gstreamer/gstcontext.html"
 * >https://gstreamer.freedesktop.org/documentation/gstreamer/gstcontext.html</a>
 * <p>
 * Context is a container object used to store contexts like a device context, a
 * display server connection and similar concepts that should be shared between
 * multiple elements.
 * <p>
 * Applications can set a context on a complete pipeline by using
 * {@link Element#setContext(Context)}, which will then be propagated to all
 * child elements. Elements can handle these in
 * {@link Element#setContext(Context)} and merge them with the context
 * information they already have.
 * <p>
 * When an element needs a context it will do the following actions in this
 * order until one step succeeds:
 * <ol>
 * <li>Check if the element already has a context</li>
 * <li>Query downstream with GST_QUERY_CONTEXT for the context</li>
 * <li>Query upstream* with GST_QUERY_CONTEXT for the context</li>
 * <li>Post a GST_MESSAGE_NEED_CONTEXT message on the bus with the required
 * context types and afterwards check if a usable context was set now</li>
 * <li>Create a context by itself and post a GST_MESSAGE_HAVE_CONTEXT message on
 * the bus.</li>
 * </ol>
 * <p>
 * Bins will catch GST_MESSAGE_NEED_CONTEXT messages and will set any previously
 * known context on the element that asks for it if possible. Otherwise the
 * application should provide one if it can.
 * <p>
 * Contexts can be persistent. A persistent context is kept in elements when
 * they reach {@lin State#NULL}, non-persistent ones will be removed. Also, a
 * non-persistent context won't override a previous persistent context set to an
 * element.
 */
public class Context extends MiniObject {

    public static final String GTYPE_NAME = "GstContext";

    private final Handle handle;

    public Context(String contextType) {
        this(contextType, true);
    }

    /**
     * Create a new context.
     */
    public Context(String context_type, boolean persistent) {
        this(new Handle(GstContextAPI.GSTCONTEXT_API.gst_context_new(context_type, persistent), true), false);
    }

    Context(Handle handle, boolean needRef) {
        super(handle, needRef);
        this.handle = handle;
    }

    Context(Initializer init) {
        this(new Handle(init.ptr.as(GstContextPtr.class, GstContextPtr::new), init.ownsHandle), init.needRef);
    }

    /**
     * Access the structure of the context.
     *
     * @return The structure of this context. The structure is still owned by this
     *         context, which means that you should not modify it and not dispose of
     *         it.
     */
    public Structure getStructure() {
        return GstContextAPI.GSTCONTEXT_API.gst_context_get_structure(handle.getPointer());
    }

    /**
     * Get a writable version of the structure.
     *
     * @return The structure of this context. The structure is still owned by the
     *         context, which means that you should not dispose of it.
     */
    public Structure getWritableStructure() {
        return GstContextAPI.GSTCONTEXT_API.gst_context_writable_structure(handle.getPointer());
    }

    /**
     * Get the type of this context.
     *
     * @return The type of this context.
     */
    public String getContextType() {
        return GstContextAPI.GSTCONTEXT_API.gst_context_get_context_type(handle.getPointer());
    }

    protected static class Handle extends MiniObject.Handle {

        public Handle(GstContextPtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected GstContextPtr getPointer() {
            return (GstContextPtr) super.getPointer();
        }

    }

}
