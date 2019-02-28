/*
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2009 Levente Farkas
 * Copyright (C) 2009 Tamas Korodi <kotyo@zamba.fm> 
 * Copyright (C) 2007 Wayne Meissner
 * Copyright (C) 2003 David A. Schleef <ds@schleef.org>
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

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GValueAPI;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;

import static org.freedesktop.gstreamer.lowlevel.GstStructureAPI.GSTSTRUCTURE_API;
import static org.freedesktop.gstreamer.lowlevel.GstValueAPI.GSTVALUE_API;

/**
 * Generic structure containing fields of names and values.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstStructure.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstStructure.html</a>
 * <p>
 * A Structure is a collection of key/value pairs. The keys are expressed as
 * GQuarks and the values can be of any GType.
 * <p>
 * In addition to the key/value pairs, a Structure also has a name. The name
 * starts with a letter and can be followed by letters, numbers and any of
 * "/-_.:".
 * <p>
 * Structure is used by various GStreamer subsystems to store information in a
 * flexible and extensible way.
 * <p>
 * A Structure can be created with new {@link #Structure(String)} or
 * {@link #Structure(String, String, Object...)}, which both take a name and an
 * optional set of key/value pairs along with the types of the values.
 * <p>
 * Field values can be changed with set{Integer,String}() etc functions.
 * <p>
 * Field values can be retrieved with get{Integer,String}() etc functions.
 * <p>
 * Fields can be removed with {@link #removeField} or {@link #removeFields}
 *
 * @see Caps
 * @see Event
 */
public class Structure extends NativeObject {


    /**
     * Creates a new, empty #GstStructure with the given name.
     *
     * @param name The name of new structure.
     */
    public Structure(String name) {
        this(new Handle(new GPointer(GSTSTRUCTURE_API.ptr_gst_structure_new_empty(name)), true));
    }

    /**
     * Creates a new Structure with the given name. Parses the list of variable
     * arguments and sets fields to the values listed. Variable arguments should
     * be passed as field name, field type, and value.
     *
     * @param name The name of new structure.
     * @param firstFieldName The name of first field to set
     * @param data Additional arguments.
     */
    public Structure(String name, String firstFieldName, Object... data) {
        this(new Handle(new GPointer(GSTSTRUCTURE_API.ptr_gst_structure_new(name, firstFieldName, data)), true));
    }
    /**
     * Creates a new instance of Structure
     */
    Structure(Initializer init) {
        this(new Handle(init.ptr, init.ownsHandle));
    }
    
    private Structure(Handle handle) {
        super(handle);
    }

    public Structure copy() {
        return GSTSTRUCTURE_API.gst_structure_copy(this);
    }

    public boolean fixateNearestInteger(String field, Integer value) {
        return GSTSTRUCTURE_API.gst_structure_fixate_field_nearest_int(this, field, value);
    }
    /**
     * Get the value of the named field as a boolean. Throws
     * {@link InvalidFieldException} if the Structure does not contain the named
     * field or the named field is not a boolean.
     *
     * @param fieldName name of field
     * @return boolean value of the named field
     */
    public boolean getBoolean(String fieldName) {
        int[] val = {0};
        if (!GSTSTRUCTURE_API.gst_structure_get_boolean(this, fieldName, val)) {
            throw new InvalidFieldException("boolean", fieldName);
        }
        return val[0] != 0;
    }

    /**
     * Get the value of the named field as a double. Throws
     * {@link InvalidFieldException} if the Structure does not contain the named
     * field or the named field is not a double.
     *
     * @param fieldName name of field
     * @return double value of the named field
     */
    public double getDouble(String fieldName) {
        double[] val = {0d};
        if (!GSTSTRUCTURE_API.gst_structure_get_double(this, fieldName, val)) {
            throw new InvalidFieldException("double", fieldName);
        }
        return val[0];
    }

    /**
     * Extract the values of the named field as an array of doubles. If the
     * native GType of the field is a GValueArray then this method will return
     * an array of the contained values, assuming all contained values are of
     * G_TYPE_DOUBLE. Else if the field is of G_TYPE_DOUBLE a single value array
     * will be returned.
     * <p>
     * This method will create a new array each time. If you are repeatedly
     * calling this method consider using
     * {@link #getDoubles(java.lang.String, double[])}.
     * <p>
     * Throws {@link InvalidFieldException} if the field does not exist, or the
     * field values contained are not of type G_TYPE_DOUBLE.
     * <p>
     * This method only currently supports lists of values inside a GValueArray
     * - other native list types will be supported in future.
     *
     * @param fieldName name of field
     * @return List of values from the named field
     */
    public double[] getDoubles(String fieldName) {
        return getDoubles(fieldName, null);
    }

    /**
     * Extract the values of the named field as an array of doubles. If the
     * native GType of the field is a GValueArray then this method will return
     * an array of the contained values, assuming all contained values are of
     * G_TYPE_DOUBLE. Else if the field is of G_TYPE_DOUBLE a single value array
     * will be returned.
     * <p>
     * An array may be passed into this method to contain the result. A new
     * array will be created if the array is null or not of the correct length.
     * <p>
     * Throws {@link InvalidFieldException} if the field does not exist, or the
     * field values contained are not of type G_TYPE_DOUBLE.
     * <p>
     * This method only currently supports lists of values inside a GValueArray
     * - other native list types will be supported in future.
     *
     * @param fieldName name of field
     * @param array an array to hold values, or null
     * @return List of values from the named field
     */
    public double[] getDoubles(String fieldName, double[] array) {
        Object val = getValue(fieldName);
        if (val instanceof GValueAPI.GValueArray) {
            GValueAPI.GValueArray arr = (GValueAPI.GValueArray) val;
            int count = arr.getNValues();
            double[] values = array == null || array.length != count
                    ? new double[count] : array;
            for (int i = 0; i < count; i++) {
                GValue gval = arr.nth(i);
                if (gval.checkHolds(GType.DOUBLE)) {
                    values[i] = GValueAPI.GVALUE_API.g_value_get_double(gval);
                } else {
                    throw new InvalidFieldException("doubles", fieldName);
                }
            }
            return values;
        } else {
            if (Double.class.isInstance(val)) {
                double[] values = array == null || array.length != 1
                        ? new double[1] : array;
                values[0] = ((Double) val);
                return values;
            } else {
                throw new InvalidFieldException("double", fieldName);
            }
        }
    }
    
    /**
     * Get the number of fields in the {@link Structure}.
     *
     * @return the structure's filed number.
     */
    public int getFields() {
        return GSTSTRUCTURE_API.gst_structure_n_fields(this);
    }


    /**
     * Gets FOURCC field int representation
     *
     * @param fieldName The name of the field.
     * @return FOURCC field as a 4 byte integer
     */
    public int getFourcc(String fieldName) {
        int[] val = {0};
        if (!GSTSTRUCTURE_API.gst_structure_get_fourcc(this, fieldName, val)) {
            throw new InvalidFieldException("FOURCC", fieldName);
        }
        return val[0];
    }

    /**
     * Gets FOURCC field String representation
     *
     * @param fieldName The name of the field.
     * @return FOURCC field as a String
     */
    public String getFourccString(String fieldName) {
        int f = getFourcc(fieldName);
        byte[] b = {(byte) ((f >> 0) & 0xff), (byte) ((f >> 8) & 0xff),
            (byte) ((f >> 16) & 0xff), (byte) ((f >> 24) & 0xff)};
        return new String(b);
    }
    
    /**
     * Get the value of the named field as a Fraction. Throws
     * {@link InvalidFieldException} if the Structure does not contain the named
     * field or the named field is not a Fraction.
     *
     * @param fieldName name of field
     * @return boolean value of the named field
     */
    public Fraction getFraction(String fieldName) {
        int[] numerator = {0};
        int[] denominator = {0};
        if (!GSTSTRUCTURE_API.gst_structure_get_fraction(this, fieldName, numerator, denominator)) {
            throw new InvalidFieldException("fraction", fieldName);
        }
        return new Fraction(numerator[0], denominator[0]);
    }
    /**
     * Get the value of the named field as an int. Throws
     * {@link InvalidFieldException} if the Structure does not contain the named
     * field or the named field is not an integer.
     *
     * @param fieldName name of field
     * @return int value of the named field
     */
    public int getInteger(String fieldName) {
        int[] val = {0};
        if (!GSTSTRUCTURE_API.gst_structure_get_int(this, fieldName, val)) {
            throw new InvalidFieldException("integer", fieldName);
        }
        return val[0];
    }

    /**
     * Extract the values of the named field as an array of integers. If the
     * native GType of the field is a GValueArray then this method will return
     * an array of the contained values, assuming all contained values are of
     * G_TYPE_INT. Else if the field is of G_TYPE_INT a single value array will
     * be returned.
     * <p>
     * This method will create a new array each time. If you are repeatedly
     * calling this method consider using
     * {@link #getIntegers(java.lang.String, int[])}.
     * <p>
     * Throws {@link InvalidFieldException} if the field does not exist, or the
     * field values contained are not of type G_TYPE_INT.
     * <p>
     * This method only currently supports lists of values inside a GValueArray
     * - other native list types will be supported in future.
     *
     * @param fieldName name of field
     * @return List of values from the named field
     */
    public int[] getIntegers(String fieldName) {
        return getIntegers(fieldName, null);
    }
    /**
     * Extract the values of the named field as an array of integers. If the
     * native GType of the field is a GValueArray then this method will return
     * an array of the contained values, assuming all contained values are of
     * G_TYPE_INT. Else if the field is of G_TYPE_INT a single value array will
     * be returned.
     * <p>
     * An array may be passed into this method to contain the result. A new
     * array will be created if the array is null or not of the correct length.
     * <p>
     * Throws {@link InvalidFieldException} if the field does not exist, or the
     * field values contained are not of type G_TYPE_INT.
     * <p>
     * This method only currently supports lists of values inside a GValueArray
     * - other native list types will be supported in future.
     *
     * @param fieldName name of field
     * @param array an array to hold values, or null
     * @return List of values from the named field
     */
    public int[] getIntegers(String fieldName, int[] array) {
        Object val = getValue(fieldName);
        if (val instanceof GValueAPI.GValueArray) {
            GValueAPI.GValueArray arr = (GValueAPI.GValueArray) val;
            int count = arr.getNValues();
            int[] values = array == null || array.length != count
                    ? new int[count] : array;
            for (int i = 0; i < count; i++) {
                GValue gval = arr.nth(i);
                if (gval.checkHolds(GType.INT)) {
                    values[i] = GValueAPI.GVALUE_API.g_value_get_int(gval);
                } else {
                    throw new InvalidFieldException("integers", fieldName);
                }
            }
            return values;
        } else {
            if (Integer.class.isInstance(val)) {
                int[] values = array == null || array.length != 1
                        ? new int[1] : array;
                values[0] = ((Integer) val);
                return values;
            } else {
                throw new InvalidFieldException("integer", fieldName);
            }
        }
    }

    /**
     * Sets the name of the structure to the given name.
     *
     * The name must not be empty, must start with a letter and can be followed
     * by letters, numbers and any of "/-_.:".
     *
     * @param name The new name of the structure.
     */
    public void setName(String name) {
        GSTSTRUCTURE_API.gst_structure_set_name(this, name);
    }

    /**
     * Get the name of @structure as a string.
     *
     * @return The name of the structure.
     */
    public String getName() {
        return GSTSTRUCTURE_API.gst_structure_get_name(this);
    }
    
    /**
     * Get the @structure's ith field name as a string.
     *
     * @param i the requested filed number
     * @return The name of the structure.
     */
    public String getName(int i) {
        return GSTSTRUCTURE_API.gst_structure_nth_field_name(this, i);
    }

    /**
     * Get the value of the named field as a Range. Throws
     * {@link InvalidFieldException} if the Structure does not contain the named
     * field.
     *
     * @param fieldName name of field
     * @return Range value of the named field
     */
    public Range getRange(String fieldName) {
        GValue val = GSTSTRUCTURE_API.gst_structure_get_value(this, fieldName);
        if (val == null) {
            throw new InvalidFieldException("Range", fieldName);
        }
        return new Range(val);
    }
    /**
     * Get the value of the named field as a String.
     *
     * @param fieldName name of field
     * @return String value of the named field
     */
    public String getString(String fieldName) {
        return GSTSTRUCTURE_API.gst_structure_get_string(this, fieldName);
    }
    
    /**
     * Get the value of the named field. Throws {@link InvalidFieldException} if
     * the Structure does not contain the named field.
     *
     * @param fieldName name of field
     * @return Object representation of the named field
     */
    public Object getValue(String fieldName) {
        GValue val = GSTSTRUCTURE_API.gst_structure_get_value(this, fieldName);
        
        if (val == null) {
            throw new InvalidFieldException("Object", fieldName);
        }

        return val.getValue();
    }

    /**
     * Extract the values of the named field as a List of the provided type. If
     * the native GType of the field is a GValueArray then this method will
     * return a list of the contained values, assuming all contained values can
     * be converted to the provided Java type T. Else if the field GType can be
     * directly converted to the provided Java type T, a singleton List will be
     * returned.
     * <p>
     * Throws {@link InvalidFieldException} if the field does not exist, or the
     * field values cannot be converted to type T.
     * <p>
     * This method only currently supports lists of values inside a GValueArray
     * - other native list types will be supported in future.
     *
     * @param <T>
     * @param type type of values
     * @param fieldName name of field
     * @return List of values from the named field
     */
    public <T> List<T> getValues(Class<T> type, String fieldName) {
        Object val = getValue(fieldName);
        if (val instanceof GValueAPI.GValueArray) {
            GValueAPI.GValueArray arr = (GValueAPI.GValueArray) val;
            int count = arr.getNValues();
            List<T> values = new ArrayList<T>(count);
            for (int i = 0; i < count; i++) {
                Object o = arr.getValue(i);
                if (type.isInstance(o)) {
                    values.add(type.cast(o));
                } else {
                    throw new InvalidFieldException(type.getSimpleName(), fieldName);
                }
            }
            return values;
        } else {
            if (type.isInstance(val)) {
                return Collections.singletonList(type.cast(val));
            } else {
                throw new InvalidFieldException(type.getSimpleName(), fieldName);
            }
        }
    }
    
    /**
     * Check if the {@link Structure} contains a double field named fieldName.
     *
     * @param fieldName The name of the field to check.
     * @return true if the structure contains a double field named fieldName
     */
    public boolean hasDoubleField(String fieldName) {
        return hasField(fieldName, GType.DOUBLE);
    }

    /**
     * Check if the {@link Structure} contains a field named fieldName.
     *
     * @param fieldName The name of the field to check.
     * @return true if the structure contains a field with the given name.
     */
    public boolean hasField(String fieldName) {
        return GSTSTRUCTURE_API.gst_structure_has_field(this, fieldName);
    }


    /**
     * Check if the {@link Structure} contains a field named fieldName.
     *
     * @param fieldName The name of the field to check.
     * @param fieldType The type of the field.
     * @return true if the structure contains a field named fieldName and of
     * type fieldType
     */
    boolean hasField(String fieldName, GType fieldType) {
        return GSTSTRUCTURE_API.gst_structure_has_field_typed(this, fieldName, fieldType);
    }

    /**
     * Check if the {@link Structure} contains a field named fieldName.
     *
     * @param fieldName The name of the field to check.
     * @param fieldType The type of the field.
     * @return true if the structure contains a field named fieldName and of
     * type fieldType
     */
    public boolean hasField(String fieldName, Class<?> fieldType) {
        return GSTSTRUCTURE_API.gst_structure_has_field_typed(this, fieldName, GType.valueOf(fieldType));
    }

    /**
     * Check if the {@link Structure} contains an integer field named fieldName.
     *
     * @param fieldName The name of the field to check.
     * @return true if the structure contains an integer field named fieldName
     */
    public boolean hasIntField(String fieldName) {
        return hasField(fieldName, GType.INT);
    }
    /**
     * Checks if the structure has the given name.
     *
     * @param name structure name to check for
     * @return true if @name matches the name of the structure.
     */
    public boolean hasName(String name) {
        return GSTSTRUCTURE_API.gst_structure_has_name(this, name);
    }
    /**
     * Checks that two structures are equal
     *
     * @param structure the structure to check if it's equal to this structure
     * @return true if both structures are equal
     */
    public boolean isEqual(Structure structure) {
        return GSTSTRUCTURE_API.gst_structure_is_equal(this, structure);
    }

    /**
     * Removes the field with the given name from the structure. If the field
     * with the given name does not exist, the structure is unchanged.
     *
     * @param fieldName The name of the field to remove.
     */
    public void removeField(String fieldName) {
        GSTSTRUCTURE_API.gst_structure_remove_field(this, fieldName);
    }

    /**
     * Removes the fields with the given names. If a field does not exist, the
     * argument is ignored.
     *
     * @param fieldNames A list of field names to remove.
     */
    public void removeFields(String... fieldNames) {
        GSTSTRUCTURE_API.gst_structure_remove_fields(this, fieldNames);
    }
    
    public void setDouble(String field, Double value) {
        GSTSTRUCTURE_API.gst_structure_set(this, field, GType.DOUBLE, value);
    }
    
    public void setDoubleRange(String field, Double min, Double max) {
        GSTSTRUCTURE_API.gst_structure_set(this, field,
                GSTVALUE_API.gst_double_range_get_type(), min, max);
    }
    
    public void setFraction(String field, Integer numerator, Integer denominator) {
        GSTSTRUCTURE_API.gst_structure_set(this, field,
                GSTVALUE_API.gst_fraction_get_type(), numerator, denominator);
    }
    /**
     * Sets an integer field in the structure.
     *
     * @param field the name of the field to set.
     * @param value the value to set for the field.
     */
    public void setInteger(String field, Integer value) {
        GSTSTRUCTURE_API.gst_structure_set(this, field, GType.INT, value);
    }
    
    public void setIntegerRange(String field, Integer min, Integer max) {
        GSTSTRUCTURE_API.gst_structure_set(this, field,
                GSTVALUE_API.gst_int_range_get_type(), min, max);
    }
    
    void setPointer(String field, Pointer value) {
        GSTSTRUCTURE_API.gst_structure_set(this, field, GType.POINTER, value);
    }
    void setValue(String field, GType type, Object value) {
        GSTSTRUCTURE_API.gst_structure_set(this, field, type, value);
    }

    @Override
    public String toString() {
        return GSTSTRUCTURE_API.gst_structure_to_string(this);
    }

    /**
     * Creates a Structure from a string representation.
     *
     * @param data A string representation of a Structure.
     * @return A new Structure or null when the string could not be parsed.
     */
    public static Structure fromString(String data) {
        return new Structure(
                new Handle(
                        new GPointer(GSTSTRUCTURE_API.ptr_gst_structure_from_string(data, new PointerByReference()))
                , true));
    }
    
    static Structure objectFor(Pointer ptr, boolean needRef, boolean ownsHandle) {
        return Natives.objectFor(ptr, Structure.class, needRef, ownsHandle);
    }
    
    public class InvalidFieldException extends RuntimeException {
        
        private static final long serialVersionUID = 864118748304334069L;
        
        public InvalidFieldException(String type, String fieldName) {
            super(String.format("Structure does not contain %s field '%s'", type, fieldName));
        }
    }
    
    private static final class Handle extends NativeObject.Handle {

        public Handle(GPointer ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GSTSTRUCTURE_API.gst_structure_free(ptr.getPointer());
        }
        
    }
}
