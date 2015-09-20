/*
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2008 Andres Colubri
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 2003 Ronald Bultje <rbultje@ronald.bitfreak.net>
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

package org.freedesktop.gstreamer.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValueArray;

import com.sun.jna.Pointer;

import static org.freedesktop.gstreamer.lowlevel.GstPropertyProbeAPI.GSTPROPERTYPROBE_API;

/**
 * Interface for elements that provide mixer operations
 */
public class PropertyProbe extends GstInterface {
    /**
     * Wraps the {@link Element} in a <tt>PropertyProbe</tt> interface
     *
     * @param element the element to use as a <tt>PropertyProbe</tt>
     * @return a <tt>PropertyProbe</tt> for the element
     */
    public static final PropertyProbe wrap(Element element) {
        return new PropertyProbe(element);
    }

    /**
     * Creates a new PropertyProbe instance
     *
     * @param element the element that implements the PropertyProbe interface
     */
    private PropertyProbe(Element element) {
        super(element, GSTPROPERTYPROBE_API.gst_property_probe_get_type());
    }

    /**
     * Gets a list of available properties for this property probe/element.
     *
     * @return a list of Property instances
     */
    public List<Property> getProperties() {
        return propertiesList(GSTPROPERTYPROBE_API.gst_property_probe_get_properties(this), true, true);
    }

    public Property getProperty(String name) {
        Pointer ptr = GSTPROPERTYPROBE_API.gst_property_probe_get_property(this, name);
        if (ptr == null) {
            return null;
        }
        Property p = new Property(ptr, false, false);
        return p;
    }

    public void probeProperty(Property property) {
        if (property != null)
        {
            GSTPROPERTYPROBE_API.gst_property_probe_probe_property(this, property.getSpec());
        }
    }
   
    public void probeProperty(String name) {
        GSTPROPERTYPROBE_API.gst_property_probe_probe_property_name(this, name);
    }

    public boolean needsProbe(Property property) {
        if (property != null)
        {
            return GSTPROPERTYPROBE_API.gst_property_probe_needs_probe(this, property.getSpec());
        }
        else return false;
    }

    public boolean needsProbe(String name) {
        return GSTPROPERTYPROBE_API.gst_property_probe_needs_probe_name(this, name);
    }

    public Object[] getValues(Property property) {
        if (property != null)
        {
            return valuesArray(GSTPROPERTYPROBE_API.gst_property_probe_get_values(this, property.getSpec()));
        }
        else return null;
    }

    public Object[] getValues(String name) {
        return valuesArray(GSTPROPERTYPROBE_API.gst_property_probe_get_values_name(this, name));
    }

    public Object[] probeAndGetValues(Property property) {
        if (property != null)
        {
            return valuesArray(GSTPROPERTYPROBE_API.gst_property_probe_probe_and_get_values(this, property.getSpec()));
        }
        else return null;
    }

    public Object[] probeAndGetValues(String name) {
        return valuesArray(GSTPROPERTYPROBE_API.gst_property_probe_probe_and_get_values_name(this, name));
    }

    /**
     * Build a {@link java.util.List} of {@link Object} from the native GList.
     * @param glist The native list to get the objects from.
     * @param objectClass The proxy class to wrap the list elements in.
     * @return The converted list.
     */
    private List<Property> propertiesList(GList glist, boolean needRef, boolean ownsHandle) {
        List<Property> list = new ArrayList<Property>();
        GList next = glist;
        while (next != null) {
            if (next.data != null) {
                list.add(new Property(next.data, needRef, ownsHandle));
            }
            next = next.next();
        }
        return list;
    }

    private Object[] valuesArray(Pointer ptr) {
        if (ptr == null) return null;
        GValueArray valueArray = null;
        try
        {
            valueArray = new GValueArray(ptr);
        }
        catch (NullPointerException ex)
        {
            // This probably means that there are not values available for the
            // specified property.
            return null;
        }
        Object[] objectArray = new Object[valueArray.n_values];
        for (int i = 0; i < valueArray.n_values; i++)
        {
            objectArray[i] = valueArray.getValue(i);
        }
        return objectArray;
    }
}
