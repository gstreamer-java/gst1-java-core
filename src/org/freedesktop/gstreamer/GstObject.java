/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2007, 2008 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wtay@chello.be>
 *                    2005 Wim Taymans <wim@fluendo.com>
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
import org.freedesktop.gstreamer.glib.GObject;
import java.util.EventListener;
import java.util.EventListenerProxy;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.sun.jna.Pointer;

import static org.freedesktop.gstreamer.lowlevel.GstObjectAPI.GSTOBJECT_API;

/**
 * Base class for the GStreamer object hierarchy
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstObject.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstObject.html</a>
 * <p>
 * GstObject provides a root for the object hierarchy tree filed in by the
 * GStreamer library.  It is currently a thin wrapper on top of
 * {@link GObject}. It is an abstract class that is not very usable on its own.
 *
 */
public class GstObject extends GObject {
    private static Logger logger = Logger.getLogger(GstObject.class.getName());
    private Map<Class<? extends EventListener>, Map<EventListener, EventListenerProxy>> listenerMap;

    /**
     * Wraps an underlying C GstObject with a Java proxy
     * 
     * @param init Initialization data
     */
    protected GstObject(Initializer init) {
        super(init);
    }
    
    /**
     * Sets the name of this object, or gives this object a guaranteed unique
     * name (if name is null).
     * 
     * Returns: TRUE if the name could be set. Since Objects that have
     * a parent cannot be renamed, this function returns FALSE in those
     * cases.
     *
     * MT safe.
     * 
     * @param name new name of object
     * @return true if the name was set.  Since Objects that have
     * a parent cannot be renamed, this function returns false in those
     * cases.
     */
    public boolean setName(String name) {
        logger.entering("GstObject", "setName", name);
        return GSTOBJECT_API.gst_object_set_name(this, name);
    }
    
    /**
     * Returns a copy of the name of this object.
     *
     * For a nameless object, this returns null.
     *
     * @return the name of this object.
     */
    public String getName() {
        logger.entering("GstObject", "getName");
        return GSTOBJECT_API.gst_object_get_name(this);
    }
    
    /**
     * Returns this object's parent, if there is one.
     * 
     * @return parent or <code>null</code>
     */
    public GstObject getParent() {
        return GSTOBJECT_API.gst_object_get_parent(this);
    }
    
    @Override
    public String toString() {
        return String.format("%s: [%s]", getClass().getSimpleName(), getName());
    }
    
    @Deprecated
    protected void ref() {    
        GSTOBJECT_API.gst_object_ref(this);
    }
    
    @Override
    @Deprecated
    protected void sink() {
        GSTOBJECT_API.gst_object_ref_sink(this);
    }
    
    @Deprecated
    protected void unref() {
        GSTOBJECT_API.gst_object_unref(this);
    }
    
    private Map<Class<? extends EventListener>, Map<EventListener, EventListenerProxy>> getListenerMap() {
        if (listenerMap == null) {
            listenerMap = new HashMap<Class<? extends EventListener>, Map<EventListener, EventListenerProxy>>();
        }
        return listenerMap;
    }
    
    protected static Initializer initializer(Pointer ptr) {
        return initializer(ptr, true, true);
    }
    
    protected static Initializer initializer(Pointer ptr, boolean needRef) {
        return initializer(ptr, needRef, true);
    }

}
