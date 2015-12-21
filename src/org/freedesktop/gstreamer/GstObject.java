/*
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
import java.util.EventListener;
import java.util.EventListenerProxy;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freedesktop.gstreamer.lowlevel.GstNative;
import org.freedesktop.gstreamer.lowlevel.GstObjectAPI;
import org.freedesktop.gstreamer.lowlevel.NativeObject;

import com.sun.jna.Pointer;

/**
 * Base class for the GStreamer object hierarchy
 *
 * GstObject provides a root for the object hierarchy tree filed in by the
 * GStreamer library.  It is currently a thin wrapper on top of
 * {@link GObject}. It is an abstract class that is not very usable on its own.
 *
 */
public class GstObject extends GObject {
    private static Logger logger = Logger.getLogger(GstObject.class.getName());

    private static final GstObjectAPI gst = GstNative.load(GstObjectAPI.class);

    /**
     * Wraps an underlying C GstObject with a Java proxy
     *
     * @param init Initialization data
     */
    public GstObject(Initializer init) {
        super(init);
    }
    protected static Initializer initializer(Pointer ptr) {
        return NativeObject.initializer(ptr, true, true);
    }
    protected static Initializer initializer(Pointer ptr, boolean needRef) {
        return NativeObject.initializer(ptr, needRef, true);
    }

    /**
     * Steal the native peer from another GstObject.
     * After calling this, the victim object is disconnected from the native object
     * and any attempt to use it will throw an exception.
     *
     * @param victim The GstObject to takeover.
     * @return An Initializer that can be passed to {@link #GstObject}
     */
    protected static Initializer steal(GstObject victim) {
        Initializer init = new Initializer(victim.handle(), false, true);
        victim.invalidate();
        return init;
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
        if (GstObject.logger.isLoggable(Level.FINER)) {
            GstObject.logger.entering("GstObject", "setName", name);
        }
        return GstObject.gst.gst_object_set_name(this, name);
    }

    /**
     * Returns a copy of the name of this object.
     *
     * For a nameless object, this returns null.
     *
     * @return the name of this object.
     */
    public String getName() {
        if (GstObject.logger.isLoggable(Level.FINER)) {
            GstObject.logger.entering("GstObject", "getName");
        }
        return GstObject.gst.gst_object_get_name(this);
    }

    @Override
    public String toString() {
        return String.format("%s: [%s]", this.getClass().getSimpleName(), this.getName());
    }
    @Override
	protected void ref() {
        GstObject.gst.gst_object_ref(this);
    }
    @Override
	protected void unref() {
        GstObject.gst.gst_object_unref(this);
    }

    @Override
    protected void sink() {
        GstObject.gst.gst_object_ref_sink(this);
    }

    public GstObject getParent() {
    	return GstObject.gst.gst_object_get_parent(this);
    }

    /**
     * Adds an {@link EventListenerProxy} on this object.
     * This is used by subclasses that wish to map between java style event listeners
     * and gstreamer signals.
     *
     * @param listenerClass Class of the listener being added.
     * @param listener The listener being added.
     * @param proxy Proxy for the event listener.
     */
    protected synchronized void addListenerProxy(Class<? extends EventListener> listenerClass, EventListener listener, EventListenerProxy proxy) {
        Map<EventListener, EventListenerProxy> map = this.getListenerMap().get(listenerClass);
        /*
         * Create the map for this class if it doesn't exist
         */
        if (map == null) {
            map = new HashMap<EventListener, EventListenerProxy>();
            this.getListenerMap().put(listenerClass, map);
        }
        map.put(listener, proxy);
    }

    /**
     * Removes an {@link EventListenerProxy} from this object.
     * This is used by subclasses that wish to map between java style event listeners
     * and gstreamer signals.
     *
     * @param listenerClass The class of listener the proxy was added for.
     * @param listener The listener the proxy was added for.
     * @return The proxy that was removed, or null if no proxy was found.
     */
    protected synchronized EventListenerProxy removeListenerProxy(Class<? extends EventListener> listenerClass, EventListener listener) {
        Map<EventListener, EventListenerProxy> map = this.getListenerMap().get(listenerClass);
        if (map == null) {
            return null;
        }
        EventListenerProxy proxy = map.remove(listener);

        /*
         * Reclaim memory if this listener class is no longer used
         */
        if (map.isEmpty()) {
            this.listenerMap.remove(listenerClass);
            if (this.listenerMap.isEmpty()) {
                this.listenerMap = null;
            }
        }
        return proxy;
    }
    private Map<Class<? extends EventListener>, Map<EventListener, EventListenerProxy>> getListenerMap() {
        if (this.listenerMap == null) {
            this.listenerMap = new HashMap<Class<? extends EventListener>, Map<EventListener, EventListenerProxy>>();
        }
        return this.listenerMap;
    }

    private Map<Class<? extends EventListener>, Map<EventListener, EventListenerProxy>> listenerMap;

}
