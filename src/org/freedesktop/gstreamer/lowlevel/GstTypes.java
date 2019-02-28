/*
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2016 Christophe Lafolet
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
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.glib.NativeObject.TypeRegistration;

/**
 *
 */
public class GstTypes {

    private static final Logger logger = Logger.getLogger(GstTypes.class.getName());

//    private static final Map<String, Class<? extends NativeObject>> gtypeNameMap
//        = new ConcurrentHashMap<String, Class<? extends NativeObject>>();
    private static final Map<String, TypeRegistration<?>> TYPES
            = new ConcurrentHashMap<>();

    private GstTypes() {
    }

    /**
     * Register a class with its GType name
     */
//    public static void registerType(Class<? extends NativeObject> cls, String gTypeName) {
//   		gtypeNameMap.put(gTypeName, cls);
//    }
    public static void register(TypeRegistration<?> registration) {
        TYPES.putIfAbsent(registration.getGTypeName(), registration);
    }

    /**
     * Retrieve the class of a GType
     *
     * @param gType The type of Class
     * @return The Class of the desired type or null.
     */
    public static final TypeRegistration<?> registrationFor(final GType gType) {
        final String gTypeName = gType.getTypeName();

        // Is this GType still registered in the map ? 
        TypeRegistration<?> reg = TYPES.get(gTypeName);
        if (reg != null) {
            return reg;
        }

        // Search for a parent class registration
        GType type = gType.getParentType();
        while (!type.equals(GType.OBJECT) && !type.equals(GType.POINTER) && !type.equals(GType.INVALID)) {
            reg = TYPES.get(type.getTypeName());
            if (reg != null) {
                if (GstTypes.logger.isLoggable(Level.FINER)) {
                    GstTypes.logger.finer("Found type of " + gType + " = " + reg.getJavaType());
                }

                // The following line is an optimisation but not compatible with current implementation of GstTypes.typeFor()
                // Uncomment the following line after refactoring of GstTypes.typeFor()
                // gtypeNameMap.put(gTypeName, cls);
                return reg;
            }
            type = type.getParentType();
        }

        // No registered class found for this gType
        return null;
    }
    
    public static final Class<? extends NativeObject> classFor(final GType gType) {
    	TypeRegistration<?> reg = registrationFor(gType);
        return reg != null ? reg.getJavaType() : null;
    }

    //TODO : need refactoring to take into account derived class
    public static final GType typeFor(Class<? extends NativeObject> cls) {
        for (Map.Entry<String, TypeRegistration<?>> e : TYPES.entrySet()) {
            if (e.getValue().getJavaType().equals(cls)) {
                return GType.valueOf(e.getKey());
            }
        }
        return GType.INVALID;
    }
}
