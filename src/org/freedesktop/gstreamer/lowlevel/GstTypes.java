/*
 * Copyright (c) 2015 Christophe Lafolet
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2008 Andres Colubri
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

package org.freedesktop.gstreamer.lowlevel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freedesktop.gstreamer.GObject;
import org.freedesktop.gstreamer.MiniObject;

import com.sun.jna.Pointer;

/**
 *
 */
public class GstTypes {
    private static final Logger logger = Logger.getLogger(GstTypes.class.getName());

    private static final Map<String, Class<? extends NativeObject>> gTypeNameMap
        = new ConcurrentHashMap<String, Class<? extends NativeObject>>();

    // will be fill in lazy because some GType values are unknown at start-up
    private static final Map<GType, Class<? extends NativeObject>> gTypeMap
    	= new ConcurrentHashMap<GType, Class<? extends NativeObject>>();

    private static final GObjectAPI gst = GObjectAPI.GOBJECT_API;

    private GstTypes() {}
    /**
     * Register a new class into the gTypeNameMap.
     */
    public static void registerType(Class<? extends NativeObject> cls, String gTypeName) {
   		GstTypes.gTypeNameMap.put(gTypeName, cls);
    }
    /**
     * Retrieve the class of a GType
     *
     * @param gType The type of Class
     * @return The Class of the desired type.
     */
    public static Class<? extends NativeObject> classFor(GType gType) {
    	if (GstTypes.logger.isLoggable(Level.FINER)) {
            GstTypes.logger.entering("GstTypes", "classFor", gType);
    	}
        return GstTypes.gTypeNameMap.get(GstTypes.gst.g_type_name(gType));
    }

    /**
     * Retrieve the class of a GType Name
     *
     * @param gTypeName The type name of Class
     * @return The Class of the desired type.
     */
    public static Class<? extends NativeObject> classFor(String gTypeName) {
    	if (GstTypes.logger.isLoggable(Level.FINER)) {
            GstTypes.logger.entering("GstTypes", "classFor", gTypeName);
    	}
        return GstTypes.gTypeNameMap.get(gTypeName);
    }

    /**
     * Retrieve the class from a pointer
     *
     * @param defaultClass A parent Class
     * @return The Class of the desired type.
     */
    public static final Class<? extends NativeObject> classFor(Pointer ptr, Class<? extends NativeObject> defaultClass) {
    	String gTypeName = null;
    	GType gType =
    			GObject.class.isAssignableFrom(defaultClass) ? GObject.getType(ptr) :
    			MiniObject.class.isAssignableFrom(defaultClass) ? MiniObject.getType(ptr) :
    				null; // shall never appears

    	if (GstTypes.logger.isLoggable(Level.FINER)) {
    		GstTypes.logger.finer("Type of " + ptr + " = " + gType);
    	}

    	Class<? extends NativeObject> cls = GstTypes.gTypeMap.get(gType);
        if (cls == null) {
        	gTypeName = GstTypes.gst.g_type_name(gType);
        	cls = GstTypes.gTypeNameMap.get(GstTypes.gst.g_type_name(gType));
        	if (cls != null) GstTypes.gTypeMap.put(gType, cls);
        }

        if (cls == null) {

            GType type = gType;

            if (GstTypes.logger.isLoggable(Level.FINER)) {
                GstTypes.logger.finer("search for " + gType);
            }

            // Search if a parent class is registered
            do {
                type = GstTypes.gst.g_type_parent(type);

                cls = GstTypes.gTypeMap.get(type);
                if (cls == null) cls = GstTypes.gTypeNameMap.get(GstTypes.gst.g_type_name(type));
                if (cls != null) {
                    if (GstTypes.logger.isLoggable(Level.FINER)) {
                        GstTypes.logger.finer("Found type of " + gType + " = " + cls);
                    }
                    GstTypes.gTypeMap.put(gType, cls);
                    GstTypes.gTypeNameMap.put(gTypeName, cls);
                    return cls;
                }

            } while (!type.equals(GType.OBJECT) && !type.equals(GType.BOXED) && !type.equals(GType.INVALID));

            GstTypes.gTypeMap.put(gType, defaultClass);
            GstTypes.gTypeNameMap.put(gTypeName, defaultClass);

            return defaultClass;
        }
        return cls;
    }

    public static final GType typeFor(Class<? extends NativeObject> cls) {
        for (Map.Entry<String, Class<? extends NativeObject>> e : GstTypes.gTypeNameMap.entrySet()) {
            if (e.getValue().equals(cls)) {
                return GstTypes.gst.g_type_from_name(e.getKey());
            }
        }
        return GType.INVALID;
    }
}
