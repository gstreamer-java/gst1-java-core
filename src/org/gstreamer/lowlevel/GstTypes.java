/* 
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

package org.gstreamer.lowlevel;

import static org.gstreamer.lowlevel.GObjectAPI.GOBJECT_API;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.sun.jna.Pointer;

/**
 *
 */
public class GstTypes {
    private static final Logger logger = Logger.getLogger(GstTypes.class.getName());
    
    private static final Map<String, Class<? extends NativeObject>> gTypeNameMap
        = new HashMap<String, Class<? extends NativeObject>>();
    private static final Map<Pointer, Class<? extends NativeObject>> gTypeInstanceMap
        = new ConcurrentHashMap<Pointer, Class<? extends NativeObject>>();
    
    private GstTypes() {}
    /**
     * Register a new class into the gTypeNameMap.
     */
    public static void registerType(Class<? extends NativeObject> cls, String gTypeName) {
   		gTypeNameMap.put(gTypeName, cls);
    }
    /**
     * Retrieve the class of a GType
     * 
     * @param gType The type of Class
     * @return The Class of the desired type.
     */
    public static Class<? extends NativeObject> find(GType gType) {
        logger.entering("GstTypes", "find", gType);
        return gTypeNameMap.get(GOBJECT_API.g_type_name(gType));
    }
    /**
     * Retrieve the class of a GType Name
     * 
     * @param gTypeName The type name of Class
     * @return The Class of the desired type.
     */
    public static Class<? extends NativeObject> find(String gTypeName) {
        logger.entering("GstTypes", "find", gTypeName);
        return gTypeNameMap.get(gTypeName);
    }

    public static final boolean isGType(Pointer p, long type) {
        return getGType(p).longValue() == type;
    }
    public static final GType getGType(Pointer ptr) {        
        // Retrieve ptr->g_class
        Pointer g_class = ptr.getPointer(0);
        // Now return g_class->gtype
        return GType.valueOf(g_class.getNativeLong(0).longValue());
    }
    public static final Class<? extends NativeObject> classFor(Pointer ptr) {
        Pointer g_class = ptr.getPointer(0);
        Class<? extends NativeObject> cls = gTypeInstanceMap.get(g_class);
        if (cls != null) {
            return cls;
        }

        GType type = GType.valueOf(g_class.getNativeLong(0).longValue());
        logger.finer("Type of " + ptr + " = " + type);
        while (cls == null && !type.equals(GType.OBJECT) && !type.equals(GType.INVALID)) {
            cls = find(type);
            if (cls != null) {
                logger.finer("Found type of " + ptr + " = " + cls);
                gTypeInstanceMap.put(g_class, cls);
                break;
            }
            type = GOBJECT_API.g_type_parent(type);
        }
        return cls;
    }
    public static final Class<? extends NativeObject> classFor(GType type) {
        return find(type);
    }
    public static final GType typeFor(Class<? extends NativeObject> cls) {
        for (Map.Entry<String, Class<? extends NativeObject>> e : gTypeNameMap.entrySet()) {
            if (e.getValue().equals(cls)) {
                return GOBJECT_API.g_type_from_name(e.getKey());
            }
        }
        return GType.INVALID;
    }
}
