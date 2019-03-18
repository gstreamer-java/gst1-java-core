/*
 * Copyright (c) 2017 Neil C Smith
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2008 Andres Colubri
 * Copyright (c) 2008 Wayne Meissner
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.freedesktop.gstreamer.glib.GObject;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.glib.Natives;

/**
 *
 * @author wayne
 */
@SuppressWarnings("serial")
public interface GValueAPI extends Library {

    public interface NoMapperAPI extends Library {

        Pointer g_value_get_object(GValue value);

        Pointer g_value_dup_object(GValue value);
    }

    NoMapperAPI GVALUE_NOMAPPER_API = GNative.loadLibrary("gobject-2.0", NoMapperAPI.class,
            new HashMap<String, Object>() {});

    GValueAPI GVALUE_API = GNative.loadLibrary("gobject-2.0", GValueAPI.class,
            new HashMap<String, Object>() {{
                put(Library.OPTION_TYPE_MAPPER, new GTypeMapper());
            }});

    public static final class GValue extends com.sun.jna.Structure {
    	public static final String GTYPE_NAME = "GValue";

        /*< private >*/
        public volatile GType g_type;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "g_type", "data"
            });
        }

        /* public for GTypeValueTable methods */
        public static final class GValueData extends com.sun.jna.Union {
            public volatile int v_int;
            public volatile long v_long;
            public volatile long v_int64;
            public volatile float v_float;
            public volatile double v_double;
            public volatile Pointer v_pointer;
        }
        public volatile GValueData data[] = new GValueData[2];

        public GValue(GType type) {
            this();
            GVALUE_API.g_value_init(this, type);
        }
        
        public GValue(GType type, Object val) {
            this(type);            
            setValue(val);
        }
        
        public GValue() {}
        
        public GValue(Pointer ptr) {
            useMemory(ptr);
            read();
        }
        
        public void reset() {
        	GValueAPI.GVALUE_API.g_value_reset(this);
        }

        private <T> T validateVal(Object val, Class<T> clazz) {
            return validateVal(val, clazz, false);
        }
        
        private <T> T validateVal(Object val, Class<T> clazz, boolean allowNull) {
            
            if (val == null) {
                if (allowNull) {
                    return null;
                } else {
                    throw new IllegalArgumentException("null value not allowed for GType." + g_type);
                }
            }
            
            return clazz.cast(val);

        }
        
        public void setValue(Object val) {
            
            if (g_type.equals(GType.INT)) { GVALUE_API.g_value_set_int(this, validateVal(val, Integer.class));
            } else if (g_type.equals(GType.UINT)) { GVALUE_API.g_value_set_uint(this, validateVal(val, Integer.class));
            } else if (g_type.equals(GType.CHAR)) { GVALUE_API.g_value_set_char(this, validateVal(val, Byte.class));
            } else if (g_type.equals(GType.UCHAR)) { GVALUE_API.g_value_set_uchar(this, validateVal(val, Byte.class));
            } else if (g_type.equals(GType.LONG)) { GVALUE_API.g_value_set_long(this, validateVal(val, NativeLong.class));
            } else if (g_type.equals(GType.ULONG)) { GVALUE_API.g_value_set_ulong(this, validateVal(val, NativeLong.class));
            } else if (g_type.equals(GType.INT64)) { GVALUE_API.g_value_set_int64(this, validateVal(val, Long.class));
            } else if (g_type.equals(GType.UINT64)) { GVALUE_API.g_value_set_uint64(this, validateVal(val, Long.class));
            } else if (g_type.equals(GType.BOOLEAN)) { GVALUE_API.g_value_set_boolean(this, validateVal(val, Boolean.class));
            } else if (g_type.equals(GType.FLOAT)) { GVALUE_API.g_value_set_float(this, validateVal(val, Float.class));
            } else if (g_type.equals(GType.DOUBLE)) { GVALUE_API.g_value_set_double(this, validateVal(val, Double.class));
            } else if (g_type.equals(GType.STRING)) { GVALUE_API.g_value_set_string(this, validateVal(val, String.class));
            } else if (g_type.equals(GType.OBJECT)) { GVALUE_API.g_value_set_object(this, validateVal(val, GObject.class, true));
            } else if (g_type.equals(GType.POINTER)) { GVALUE_API.g_value_set_pointer(this, validateVal(val, Pointer.class));            
            } else {
                throw new IllegalStateException("setValue() not supported yet for GType." + g_type);
            }            
        }
        
        public boolean checkHolds(GType type) {
        	return GVALUE_API.g_type_check_value_holds(this, type);
        }
        
        public GType getType() {
        	return g_type;
        }
        
        public Object getValue() {
            if (g_type.equals(GType.INT)) { return toInt();
            } else if (g_type.equals(GType.UINT)) { return toUInt();
            } else if (g_type.equals(GType.CHAR)) { return toChar();
            } else if (g_type.equals(GType.UCHAR)) { return toUChar();
            } else if (g_type.equals(GType.LONG)) { return toLong();
            } else if (g_type.equals(GType.ULONG)) { return toULong();
            } else if (g_type.equals(GType.INT64)) { return toInt64();
            } else if (g_type.equals(GType.UINT64)) { return toUInt64();
            } else if (g_type.equals(GType.BOOLEAN)) { return toBoolean();
            } else if (g_type.equals(GType.FLOAT)) { return toFloat();
            } else if (g_type.equals(GType.DOUBLE)) { return toDouble();
            } else if (g_type.equals(GType.STRING)) { return toJavaString();
//            } else if (g_type.equals(GType.OBJECT)) { return toObject();
            } else if (g_type.equals(GType.POINTER)) { return toPointer();
            } else if (g_type.equals(GValueArray.GTYPE)) {
                return new GValueArray(GVALUE_API.g_value_get_boxed(this));
            } else if (g_type.getParentType().equals(GType.BOXED)) {
                Class<? extends NativeObject> cls = GstTypes.classFor(g_type);
                if (cls != null) {
                    Pointer ptr = GVALUE_API.g_value_get_boxed(this);
//                    return NativeObject.objectFor(ptr, cls, 1, true);
                    return Natives.objectFor(ptr, cls, true, true);
                }
            }
//            return GVALUE_API.g_value_get_object(this);  
            return GVALUE_API.g_value_dup_object(this);  
        }
        
        public Integer toInt() {
        	return g_type.equals(GType.INT) ? new Integer(GVALUE_API.g_value_get_int(this)) : null; 
        }
        
        public Integer toUInt() {
        	return g_type.equals(GType.UINT) ? new Integer(GVALUE_API.g_value_get_uint(this)) : null; 
        }
        
        public Byte toChar() {
        	return g_type.equals(GType.CHAR) ? new Byte(GVALUE_API.g_value_get_char(this)) : null; 
        }
        
        public Byte toUChar() {
        	return g_type.equals(GType.UCHAR) ? new Byte(GVALUE_API.g_value_get_uchar(this)) : null;
        }
        
        public Long toLong() {
        	return g_type.equals(GType.LONG) ? new Long(GVALUE_API.g_value_get_long(this).longValue()) : null;
        }
        
        public Long toULong() {
        	return g_type.equals(GType.ULONG) ? new Long(GVALUE_API.g_value_get_ulong(this).longValue()) : null; 
        }
        
        public Long toInt64() {
        	return g_type.equals(GType.INT64)? new Long(GVALUE_API.g_value_get_int64(this)) : null; 
        }
        
        public Long toUInt64() {
        	return g_type.equals(GType.UINT64) ? new Long(GVALUE_API.g_value_get_uint64(this)) : null;
        }
        
        public Boolean toBoolean() {
        	return g_type.equals(GType.BOOLEAN) ? new Boolean(GVALUE_API.g_value_get_boolean(this)) : null;
        }
        
        public Float toFloat() {
        	return g_type.equals(GType.FLOAT) ? new Float(GVALUE_API.g_value_get_float(this)) : null;
        }
        
        public Double toDouble() {
        	return g_type.equals(GType.DOUBLE) ? new Double(GVALUE_API.g_value_get_double(this)) : null;
        }
        
        public String toJavaString() {
        	return g_type.equals(GType.STRING) ? GVALUE_API.g_value_get_string(this) : null;
        }
        
        public Object toObject() {
        	return g_type.equals(GType.OBJECT) ? GVALUE_API.g_value_get_object(this) : null;
        }
        
        public Pointer toPointer() {
        	return g_type.equals(GType.POINTER) ? GVALUE_API.g_value_get_pointer(this) : null;
        }
        
        public String toString() {
        	return GVALUE_API.g_strdup_value_contents(this);
        }
    }
    
    public static final class GValueArray extends com.sun.jna.Structure {
    	public static final String GTYPE_NAME = "GValueArray";
        static final GType GTYPE = GType.valueOf(GTYPE_NAME);

    	public volatile int n_values;
        public volatile Pointer values;
        //< private >
        public volatile int n_prealloced;

        private boolean ownsMemory;
        private static final Pointer NO_MEMORY_POINTER = new Pointer(0);

        public GValueArray() {
            this(0);
        }
        
        public GValueArray(int n_prealloced) {
            this(n_prealloced, true);
        }
        
        public GValueArray(int n_prealloced, boolean ownsMemory) {
            this(GVALUE_API.g_value_array_new(n_prealloced));
            this.ownsMemory = ownsMemory;
        }
        
        public GValueArray(Pointer pointer) {
            super(pointer);
            if (pointer != Pointer.NULL && !NO_MEMORY_POINTER.equals(pointer))
            	n_values = pointer.getInt(0);
        }
        
        @SuppressWarnings("unused")
        private static GValueArray valueOf(Pointer ptr) {
            return ptr != null ? new GValueArray(ptr) : null;
        }

        public int getNValues() {
            return n_values;
        }

        public GValueArray prepend(GValue value) {            
            GVALUE_API.g_value_array_prepend(this, value);
            return this;
        }
        
        public GValueArray append(GValue value) {            
            GVALUE_API.g_value_array_append(this, value);
            return this;
        }
        
        public GValueArray insert(int index, GValue value) {            
            GVALUE_API.g_value_array_insert(this, index, value);
            return this;
        }
        
        public GValueArray remove(int index) {            
            GVALUE_API.g_value_array_remove(this, index);
            return this;
        }
        
        public GValue nth(int i) {
            return GVALUE_API.g_value_array_get_nth(this, i);
        }
        
        public Object getValue(int i) {            
            GValue v = nth(i);            
            return v == null ? null : v.getValue();
        }
        
        @Override
        protected void finalize() throws Throwable {
            free();
        }

        public void free() {
            if (ownsMemory) {
                GVALUE_API.g_value_array_free(this);  
                ownsMemory = false;
            }
        }

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "n_values", "values", "n_prealloced"
            });
        }
    }
    
    GValue g_value_init(GValue value, GType g_type);
    GValue g_value_reset(GValue value);
    void g_value_unset(GValue value);
    void g_value_set_char(GValue value, byte v_char);
    byte g_value_get_char(GValue value);
    void g_value_set_uchar(GValue value, byte v_uchar);
    byte g_value_get_uchar(GValue value);
    void g_value_set_boolean(GValue value, boolean v_boolean);
    boolean g_value_get_boolean(GValue value);
    void g_value_set_int(GValue value, int v_int);
    int g_value_get_int(GValue value);
    void g_value_set_uint(GValue value, int v_int);
    int g_value_get_uint(GValue value);
    void g_value_set_long(GValue value, NativeLong v_long);
    NativeLong g_value_get_long(GValue value);
    void g_value_set_ulong(GValue value, NativeLong v_long);
    NativeLong g_value_get_ulong(GValue value);
    void g_value_set_int64(GValue value, long v_int64);
    long g_value_get_int64(GValue value);
    void g_value_set_uint64(GValue value, long v_uint64);
    long g_value_get_uint64(GValue value);
    void g_value_set_float(GValue value, float v_float);
    float g_value_get_float(GValue value);
    void g_value_set_double(GValue value, double v_double);
    double g_value_get_double(GValue value);
    void g_value_set_enum(GValue value, int v_enum);
    int g_value_get_enum(GValue value);
    void g_value_set_string(GValue value, String v_string);
    void g_value_set_static_string (GValue value, String v_string);
    String g_value_get_string(GValue value);
    Pointer g_value_get_pointer(GValue value);
    void g_value_set_pointer(GValue value, Pointer pointer);
    boolean g_value_type_compatible(GType src_type, GType dest_type);
    boolean g_value_type_transformable(GType src_type, GType dest_type);
    boolean g_value_transform(GValue src_value, GValue dest_value);
    
    @CallerOwnsReturn String g_strdup_value_contents(GValue value);
    
    void g_value_set_object(GValue value, GObject v_object);
    void g_value_take_object(GValue value, @Invalidate GObject v_object);
    GObject g_value_get_object(GValue value);
    @CallerOwnsReturn GObject g_value_dup_object(GValue value);
   
    Pointer g_value_get_boxed(GValue value);

    GValue g_value_array_get_nth(GValueArray value_array, int index);
    Pointer g_value_array_new(int n_prealloced);
    void g_value_array_free (GValueArray value_array);

    Pointer g_value_array_copy(GValueArray value_array);
    Pointer g_value_array_prepend(GValueArray value_array, GValue value);
    Pointer g_value_array_append(GValueArray value_array, GValue value);
    Pointer g_value_array_insert(GValueArray value_array, int index_, GValue value);
    Pointer g_value_array_remove(GValueArray value_array, int index);
    
    boolean g_type_check_value_holds(GValue value, GType type);

/*
 * GCompareDataFunc needs to be implemented.
Pointer g_value_array_sort(GValueArray value_array, GCompareFunc compare_func);
Pointer g_value_array_sort_with_data (GValueArray value_array,
        GCompareDataFunc compare_func, Pointer user_data);
 */
}
