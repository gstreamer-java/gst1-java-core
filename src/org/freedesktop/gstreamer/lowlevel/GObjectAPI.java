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

package org.freedesktop.gstreamer.lowlevel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.freedesktop.gstreamer.glib.GObject;
import org.freedesktop.gstreamer.glib.GQuark;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure.ByReference;
import com.sun.jna.ptr.IntByReference;

/**
 *
 */
@SuppressWarnings("serial")
public interface GObjectAPI extends Library {
    GObjectAPI GOBJECT_API = GNative.loadLibrary("gobject-2.0", GObjectAPI.class,
        new HashMap<String, Object>() {{
            put(Library.OPTION_TYPE_MAPPER, new GTypeMapper());
        }});

    GType g_object_get_type();
    void g_param_value_validate(GParamSpec spec, GValue data);
    void g_object_set_property(GObject obj, String property, GValue data);
    void g_object_get_property(GObject obj, String property, GValue data);
    void g_object_set(GObject obj, String propertyName, Object... data);
    void g_object_get(GObject obj, String propertyName, Object... data);
    Pointer g_object_class_list_properties(Pointer oclass, IntByReference size);
    
    Pointer g_object_new(GType object_type, Object... args);
    
    interface GClosureNotify extends Callback {
        void callback(Pointer data, Pointer closure);
    }
//    NativeLong g_signal_connect_data(GObject obj, String signal, Callback callback, Pointer data,
//            GClosureNotify destroy_data, int connect_flags);
//    void g_signal_handler_disconnect(GObject obj, NativeLong id);
    NativeLong g_signal_connect_data(GObjectPtr obj, String signal, Callback callback, Pointer data,
            GClosureNotify destroy_data, int connect_flags);
    void g_signal_handler_disconnect(GObjectPtr obj, NativeLong id);
    boolean g_object_is_floating(GObjectPtr obj);
    /** Sink floating ref 
     * https://developer.gnome.org/gobject/stable/gobject-The-Base-Object-Type.html#g-object-ref-sink 
     */
    Pointer g_object_ref_sink(Pointer ptr);
    GObjectPtr g_object_ref_sink(GObjectPtr ptr);
    interface GToggleNotify extends Callback {
        void callback(Pointer data, Pointer obj, boolean is_last_ref);
    }
    void g_object_add_toggle_ref(Pointer object, GToggleNotify notify, Pointer data);
    void g_object_remove_toggle_ref(Pointer object, GToggleNotify notify, Pointer data);
    void g_object_add_toggle_ref(GObjectPtr object, GToggleNotify notify, IntPtr data);
    void g_object_remove_toggle_ref(GObjectPtr object, GToggleNotify notify, IntPtr data);
    interface GWeakNotify extends Callback {
        void callback(IntPtr data, Pointer obj);
    }
    void g_object_weak_ref(GObject object, GWeakNotify notify, IntPtr data);
    void g_object_weak_unref(GObject object, GWeakNotify notify, IntPtr data);
    Pointer g_object_ref(GObjectPtr object);
    void g_object_unref(GObjectPtr object);

    GParamSpec g_object_class_find_property(GObjectClass oclass, String property_name);
    Pointer g_object_class_find_property(Pointer oclass, String property_name);
    GQuark g_quark_try_string(String string);
    GQuark g_quark_from_static_string(String string);
    GQuark g_quark_from_string(String string);
    String g_quark_to_string(GQuark quark);

    String g_intern_string(String string);
    String g_intern_static_string(String string);
    
    void g_type_init();
    void g_type_init_with_debug_flags(int flags);
    String g_type_name(GType type);
    //GQuark                g_type_qname                   (GType            type);
    GType g_type_from_name(String name);
    GType g_type_parent(GType type);
    int g_type_depth(GType type);
    Pointer g_type_create_instance(GType type);
    void g_type_free_instance(Pointer instance);
    boolean g_type_is_a(GType type, GType is_a_type);
    
    GType g_type_register_static(GType parent_type, String type_name,
        GTypeInfo info, /* GTypeFlags */ int flags);
    GType g_type_register_static(GType parent_type, Pointer type_name,
        GTypeInfo info, /* GTypeFlags */ int flags);
    GType g_type_register_static_simple(GType parent_type, String type_name,
        int class_size, GClassInitFunc class_init, int instance_size,
        GInstanceInitFunc instance_init, /* GTypeFlags */ int flags);
    GType g_type_register_static_simple(GType parent_type, Pointer type_name,
        int class_size, GClassInitFunc class_init, int instance_size,
        GInstanceInitFunc instance_init, /* GTypeFlags */ int flags);
    /* 
     * Basic Type Structures
     */
    public static class GTypeClass extends com.sun.jna.Structure {

        /*< private >*/
        public volatile GType g_type;

        @Override
        protected List<String> getFieldOrder() {
            return Collections.singletonList("g_type");
        }
    }
    
    public static final class GTypeClassByReference extends GTypeClass implements ByReference {
    	//
    }


    public static final class GTypeInstance extends com.sun.jna.Structure {

        /*< private >*/
    	public volatile GTypeClassByReference g_class; 
        
        @Override
        protected List<String> getFieldOrder() {
            return Collections.singletonList("g_class");
        }
    }                  
    
    public static final class GObjectStruct extends com.sun.jna.Structure {
        public volatile GTypeInstance g_type_instance;
        public volatile int ref_count;
        public volatile Pointer qdata;
        public GObjectStruct() {}
        public GObjectStruct(GObjectPtr ptr) {
            super(ptr.getPointer());
            read();
        }
        

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "g_type_instance", "ref_count", "qdata"
            });
        }
           
    }
    public static final class GObjectConstructParam {
        public volatile Pointer spec;
        public volatile Pointer value;
    }
    public static final class GObjectClass extends com.sun.jna.Structure {
        public volatile GTypeClass g_type_class;
        public volatile Pointer construct_properties;
        public Constructor constructor;
        public SetProperty set_property;
        public GetProperty get_property;
        public Dispose dispose;
        public Finalize finalize;
        public volatile Pointer dispatch_properties_changed;
        public Notify notify;
        public volatile byte[] p_dummy = new byte[8 * Native.POINTER_SIZE];
        
        public static interface Constructor extends Callback {
            public Pointer callback(GType type, int n_construct_properties, 
                    GObjectConstructParam properties);
        };
        public static interface SetProperty extends Callback {
            public void callback(GObject object, int property_id, Pointer value, Pointer spec);
        }
        public static interface GetProperty extends Callback {
            public void callback(GObject object, int property_id, Pointer value, Pointer spec);
        }
        public static interface Dispose extends Callback {
            public void callback(GObject object);
        }
        public static interface Finalize extends Callback {
            public void callback(GObject object);
        }
        public static interface Notify extends Callback {
            public void callback(GObject object, Pointer spec);
        }

        public GObjectClass() {}
        public GObjectClass(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
               "g_type_class", "construct_properties", "constructor",
               "set_property", "get_property", "dispose",
               "finalize", "dispatch_properties_changed", "notify",
               "p_dummy"
            });
        }
        
    }
    
    
    public static interface GBaseInitFunc extends Callback {
        public void callback(Pointer g_class);
    }

    public static interface GBaseFinalizeFunc extends Callback {
        public void callback(Pointer g_class);
    }

    public static interface GClassInitFunc extends Callback {
        public void callback(Pointer g_class, Pointer class_data);
    }

    public static interface GClassFinalizeFunc extends Callback {
        public void callback(Pointer g_class, Pointer class_data);
    }
    public static interface GInstanceInitFunc extends Callback {
        void callback(GTypeInstance instance, Pointer g_class);
    }    
    public static final class GTypeInfo extends com.sun.jna.Structure {
        public GTypeInfo() { 
            clear();
        }
        public GTypeInfo(Pointer ptr) { 
            super(ptr); 
            read();
        }
        /* interface types, classed types, instantiated types */
        public short class_size;
        public GBaseInitFunc base_init;
        public GBaseFinalizeFunc base_finalize;
        /* interface types, classed types, instantiated types */
        public GClassInitFunc class_init;
        public GClassFinalizeFunc class_finalize;
        public Pointer class_data;
        /* instantiated types */
        public short instance_size;
        public short n_preallocs;
        
        public GInstanceInitFunc instance_init;

        /* value handling */
        public volatile /* GTypeValueTable */ Pointer value_table;                

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "class_size", "base_init", "base_finalize",
                "class_init", "class_finalize", "class_data",
                "instance_size", "n_preallocs", "instance_init",
                "value_table"
            });
        }
    }

    public static abstract class GParamSpecTypeSpecific extends com.sun.jna.Structure {
    	public abstract Object getMinimum();
    	public abstract Object getMaximum();
    	public abstract Object getDefault();
    	
        public GParamSpecTypeSpecific() { 
        	clear(); 
        }
        public GParamSpecTypeSpecific(Pointer ptr) {
        	super(ptr);
        }
    }
    public static final class GParamSpecBoolean extends GParamSpecTypeSpecific {
    	public volatile GParamSpec parent_instance;
    	public volatile boolean default_value;
    	
    	public Object getMinimum() { return null; }
    	public Object getMaximum() { return null; }
    	public Object getDefault() { return default_value; }
    	
        public GParamSpecBoolean(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_instance", "default_value"
            });           
        }
    }
    public static final class GParamSpecInt extends GParamSpecTypeSpecific {
    	public volatile GParamSpec parent_instance;
    	public volatile int minimum;
    	public volatile int maximum;
    	public volatile int default_value;
    	
    	public Object getMinimum() { return minimum; }
    	public Object getMaximum() { return maximum; }
    	public Object getDefault() { return default_value; }
    	
        public GParamSpecInt(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_instance", "minimum", "maximum",
                "default_value"
            });           
        }
        
    }
    public static final class GParamSpecUInt extends GParamSpecTypeSpecific {
    	public volatile GParamSpec parent_instance;
    	public volatile int minimum;
    	public volatile int maximum;
    	public volatile int default_value;
    	
    	public Object getMinimum() { return ((long)minimum)&0xffffff; }
    	public Object getMaximum() { return ((long)maximum)&0xffffff; }
    	public Object getDefault() { return ((long)default_value)&0xffffff; }
    	
        public GParamSpecUInt(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_instance", "minimum", "maximum",
                "default_value"
            });           
        }
    }
    public static final class GParamSpecChar extends GParamSpecTypeSpecific {
    	public volatile GParamSpec parent_instance;
    	public volatile byte minimum;
    	public volatile byte maximum;
    	public volatile byte default_value;
    	
    	public Object getMinimum() { return minimum; }
    	public Object getMaximum() { return maximum; }
    	public Object getDefault() { return default_value; }
    	
        public GParamSpecChar(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_instance", "minimum", "maximum",
                "default_value"
            });           
        }
    }
    public static final class GParamSpecUChar extends GParamSpecTypeSpecific {
    	public volatile GParamSpec parent_instance;
    	public volatile byte minimum;
    	public volatile byte maximum;
    	public volatile byte default_value;
    	
    	public Object getMinimum() { return ((short)minimum)&0xff; }
    	public Object getMaximum() { return ((short)maximum)&0xff; }
    	public Object getDefault() { return ((short)default_value)&0xff; }
    	
        public GParamSpecUChar(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_instance", "minimum", "maximum",
                "default_value"
            });           
        }
    }
    public static final class GParamSpecLong extends GParamSpecTypeSpecific {
    	public volatile GParamSpec parent_instance;
    	public volatile NativeLong minimum;
    	public volatile NativeLong maximum;
    	public volatile NativeLong default_value;
    	
    	public Object getMinimum() { return minimum; }
    	public Object getMaximum() { return maximum; }
    	public Object getDefault() { return default_value; }
    	
        public GParamSpecLong(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_instance", "minimum", "maximum",
                "default_value"
            });           
        }
    }
    public static final class GParamSpecInt64 extends GParamSpecTypeSpecific {
    	public volatile GParamSpec parent_instance;
    	public volatile long minimum;
    	public volatile long maximum;
    	public volatile long default_value;
    	
    	public Object getMinimum() { return minimum; }
    	public Object getMaximum() { return maximum; }
    	public Object getDefault() { return default_value; }
    	
        public GParamSpecInt64(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_instance", "minimum", "maximum",
                "default_value"
            });           
        }
    }
    public static final class GParamSpecFloat extends GParamSpecTypeSpecific {
    	public volatile GParamSpec parent_instance;
    	public volatile float minimum;
    	public volatile float maximum;
    	public volatile float default_value;
    	public volatile float epsilon;
    	
    	public Object getMinimum() { return minimum; }
    	public Object getMaximum() { return maximum; }
    	public Object getDefault() { return default_value; }
    	
        public GParamSpecFloat(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_instance", "minimum", "maximum",
                "default_value", "epsilon"
            });           
        }
    }
    public static final class GParamSpecDouble extends GParamSpecTypeSpecific {
    	public volatile GParamSpec parent_instance;
    	public volatile double minimum;
    	public volatile double maximum;
    	public volatile double default_value;
    	public volatile double epsilon;
    	
    	public Object getMinimum() { return minimum; }
    	public Object getMaximum() { return maximum; }
    	public Object getDefault() { return default_value; }
    	
        public GParamSpecDouble(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_instance", "minimum", "maximum",
                "default_value", "epsilon"
            });           
        }
    }
    public static final class GParamSpecString extends GParamSpecTypeSpecific {
    	public volatile GParamSpec parent_instance;
    	public volatile String default_value;
    	public volatile String cset_first;
    	public volatile String cset_nth;
    	public volatile byte substitutor;
    	public volatile int null_fold_if_empty_ensure_non_null;

    	
    	public Object getMinimum() { return null; }
    	public Object getMaximum() { return null; }
    	public Object getDefault() { return default_value; }
    	
        public GParamSpecString(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "parent_instance", "default_value", "cset_first",
                "cset_nth", "substitutor", "null_fold_if_empty_ensure_non_null"
            });           
        }
    }
    
    public static final class GParamSpec extends com.sun.jna.Structure {
        public volatile GTypeInstance g_type_instance;
        public volatile String g_name;
        public volatile /* GParamFlags */ int g_flags;
        public volatile GType value_type;
        public volatile GType owner_type;
        /*< private >*/
        public volatile Pointer _nick;
        public volatile Pointer _blurb;
        public volatile Pointer qdata;
        public volatile int ref_count;
        public volatile int param_id;      /* sort-criteria */
        
        public GParamSpec() {
            clear();
        }
        public GParamSpec(Pointer ptr) {
            super(ptr);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(new String[]{
                "g_type_instance", "g_name", "g_flags",
                "value_type", "owner_type", "_nick",
                "_blurb", "qdata", "ref_count",
                "param_id"
            });           
        }
        
/*
        public String getName() {
            return (String) readField("g_name");
        }
        public int getFlags() {
            return (Integer) readField("g_flags");
        }
        public GType getValueType() {
            return (GType) readField("value_type");
        }
        public GType getOwnerType() {
            return (GType) readField("owner_type");
        }

        public void read() {}
        public void write() {}
                 */
    }
 }

