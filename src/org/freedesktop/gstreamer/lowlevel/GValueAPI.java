/*
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

import org.freedesktop.gstreamer.GObject;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.Library;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

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
    			this.put(Library.OPTION_TYPE_MAPPER, new GTypeMapper());
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
            GValueAPI.GVALUE_API.g_value_init(this, type);
        }

        public GValue(GType type, Object val) {
            this(type);
            this.setValue(val);
        }

        public GValue() {}
        public GValue(Pointer ptr) {
            this.useMemory(ptr);
            this.read();
        }

        private <T> T validateVal(Object val, Class<T> clazz) {
            return this.validateVal(val, clazz, false);
        }

        private <T> T validateVal(Object val, Class<T> clazz, boolean allowNull) {

            if (val == null) {
                if (allowNull) {
                    return null;
                } else {
                    throw new IllegalArgumentException("null value not allowed for GType." + this.g_type);
                }
            }

            return clazz.cast(val);

        }

        public void setValue(Object val) {

            if (this.g_type.equals(GType.INT)) { GValueAPI.GVALUE_API.g_value_set_int(this, this.validateVal(val, Integer.class));
            } else if (this.g_type.equals(GType.UINT)) { GValueAPI.GVALUE_API.g_value_set_uint(this, this.validateVal(val, Integer.class));
            } else if (this.g_type.equals(GType.CHAR)) { GValueAPI.GVALUE_API.g_value_set_char(this, this.validateVal(val, Byte.class));
            } else if (this.g_type.equals(GType.UCHAR)) { GValueAPI.GVALUE_API.g_value_set_uchar(this, this.validateVal(val, Byte.class));
            } else if (this.g_type.equals(GType.LONG)) { GValueAPI.GVALUE_API.g_value_set_long(this, this.validateVal(val, NativeLong.class));
            } else if (this.g_type.equals(GType.ULONG)) { GValueAPI.GVALUE_API.g_value_set_ulong(this, this.validateVal(val, NativeLong.class));
            } else if (this.g_type.equals(GType.INT64)) { GValueAPI.GVALUE_API.g_value_set_int64(this, this.validateVal(val, Long.class));
            } else if (this.g_type.equals(GType.UINT64)) { GValueAPI.GVALUE_API.g_value_set_uint64(this, this.validateVal(val, Long.class));
            } else if (this.g_type.equals(GType.BOOLEAN)) { GValueAPI.GVALUE_API.g_value_set_boolean(this, this.validateVal(val, Boolean.class));
            } else if (this.g_type.equals(GType.FLOAT)) { GValueAPI.GVALUE_API.g_value_set_float(this, this.validateVal(val, Float.class));
            } else if (this.g_type.equals(GType.DOUBLE)) { GValueAPI.GVALUE_API.g_value_set_double(this, this.validateVal(val, Double.class));
            } else if (this.g_type.equals(GType.STRING)) { GValueAPI.GVALUE_API.g_value_set_string(this, this.validateVal(val, String.class));
            } else if (this.g_type.equals(GType.OBJECT)) { GValueAPI.GVALUE_API.g_value_set_object(this, this.validateVal(val, GObject.class, true));
            } else if (this.g_type.equals(GType.POINTER)) { GValueAPI.GVALUE_API.g_value_set_pointer(this, this.validateVal(val, Pointer.class));
            } else {
                throw new IllegalStateException("setValue() not supported yet for GType." + this.g_type);
            }
        }

        public boolean checkHolds(GType type) {
        	return GValueAPI.GVALUE_API.g_type_check_value_holds(this, type);
        }

        public GType getType() {
        	return this.g_type;
        }

        public Object getValue() {
            if (this.g_type.equals(GType.INT)) { return this.toInt();
            } else if (this.g_type.equals(GType.UINT)) { return this.toUInt();
            } else if (this.g_type.equals(GType.CHAR)) { return this.toChar();
            } else if (this.g_type.equals(GType.UCHAR)) { return this.toUChar();
            } else if (this.g_type.equals(GType.LONG)) { return this.toLong();
            } else if (this.g_type.equals(GType.ULONG)) { return this.toULong();
            } else if (this.g_type.equals(GType.INT64)) { return this.toInt64();
            } else if (this.g_type.equals(GType.UINT64)) { return this.toUInt64();
            } else if (this.g_type.equals(GType.BOOLEAN)) { return this.toBoolean();
            } else if (this.g_type.equals(GType.FLOAT)) { return this.toFloat();
            } else if (this.g_type.equals(GType.DOUBLE)) { return this.toDouble();
            } else if (this.g_type.equals(GType.STRING)) { return this.toJavaString();
            } else if (this.g_type.equals(GType.POINTER)) { return this.toPointer();
//          } else if (this.g_type.equals(GType.OBJECT)) { return this.toObject();
            } else {
            	// others are derived from object
            	return GValueAPI.GVALUE_API.g_value_get_object(this);
            }
//            return null;
        }

        public Integer toInt() {
        	return this.g_type.equals(GType.INT) ? new Integer(GValueAPI.GVALUE_API.g_value_get_int(this)) : null;
        }

        public Integer toUInt() {
        	return this.g_type.equals(GType.UINT) ? new Integer(GValueAPI.GVALUE_API.g_value_get_uint(this)) : null;
        }

        public Byte toChar() {
        	return this.g_type.equals(GType.CHAR) ? new Byte(GValueAPI.GVALUE_API.g_value_get_char(this)) : null;
        }

        public Byte toUChar() {
        	return this.g_type.equals(GType.UCHAR) ? new Byte(GValueAPI.GVALUE_API.g_value_get_uchar(this)) : null;
        }

        public Long toLong() {
        	return this.g_type.equals(GType.LONG) ? new Long(GValueAPI.GVALUE_API.g_value_get_long(this).longValue()) : null;
        }

        public Long toULong() {
        	return this.g_type.equals(GType.ULONG) ? new Long(GValueAPI.GVALUE_API.g_value_get_ulong(this).longValue()) : null;
        }

        public Long toInt64() {
        	return this.g_type.equals(GType.INT64)? new Long(GValueAPI.GVALUE_API.g_value_get_int64(this)) : null;
        }

        public Long toUInt64() {
        	return this.g_type.equals(GType.UINT64) ? new Long(GValueAPI.GVALUE_API.g_value_get_uint64(this)) : null;
        }

        public Boolean toBoolean() {
        	return this.g_type.equals(GType.BOOLEAN) ? new Boolean(GValueAPI.GVALUE_API.g_value_get_boolean(this)) : null;
        }

        public Float toFloat() {
        	return this.g_type.equals(GType.FLOAT) ? new Float(GValueAPI.GVALUE_API.g_value_get_float(this)) : null;
        }

        public Double toDouble() {
        	return this.g_type.equals(GType.DOUBLE) ? new Double(GValueAPI.GVALUE_API.g_value_get_double(this)) : null;
        }

        public String toJavaString() {
        	return this.g_type.equals(GType.STRING) ? GValueAPI.GVALUE_API.g_value_get_string(this) : null;
        }

        public Object toObject() {
        	return this.g_type.equals(GType.OBJECT) ? GValueAPI.GVALUE_API.g_value_get_object(this) : null;
        }

        public Pointer toPointer() {
        	return this.g_type.equals(GType.POINTER) ? GValueAPI.GVALUE_API.g_value_get_pointer(this) : null;
        }

        @Override
		public String toString() {
        	return GValueAPI.GVALUE_API.g_strdup_value_contents(this);
        }
    }

    public static final class GValueArray extends com.sun.jna.Structure {
    	public static final String GTYPE_NAME = "GValueArray";

    	public volatile int n_values;
        public volatile Pointer values;
        //< private >
        public volatile int n_prealloced;

        private boolean ownsMemory;

        public GValueArray() {
            this(0);
        }

        public GValueArray(int n_prealloced) {
            this(GValueAPI.GVALUE_API.g_value_array_new(n_prealloced));
            this.ownsMemory = true;
        }

        public GValueArray(Pointer pointer) {
            super(pointer);
            this.n_values = pointer.getInt(0);
        }

        @SuppressWarnings("unused")
        private static GValueArray valueOf(Pointer ptr) {
            return ptr != null ? new GValueArray(ptr) : null;
        }

        public int getNValues() {
            return this.n_values;
        }

        public GValueArray prepend(GValue value) {
            GValueAPI.GVALUE_API.g_value_array_prepend(this, value);
            return this;
        }

        public GValueArray append(GValue value) {
            GValueAPI.GVALUE_API.g_value_array_append(this, value);
            return this;
        }

        public GValueArray insert(int index, GValue value) {
            GValueAPI.GVALUE_API.g_value_array_insert(this, index, value);
            return this;
        }

        public GValueArray remove(int index) {
            GValueAPI.GVALUE_API.g_value_array_remove(this, index);
            return this;
        }

        public GValue nth(int i) {
            return GValueAPI.GVALUE_API.g_value_array_get_nth(this, i);
        }

        public Object getValue(int i) {
            GValue v = this.nth(i);
            return v == null ? null : v.getValue();
        }

        @Override
        protected void finalize() throws Throwable {
            this.free();
        }

        public void free() {
            if (this.ownsMemory) {
                GValueAPI.GVALUE_API.g_value_array_free(this);
                this.ownsMemory = false;
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
