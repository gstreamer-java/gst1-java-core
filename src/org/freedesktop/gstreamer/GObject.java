/*
 * Copyright (c) 2015 Christophe Lafolet
 * Copyright (c) 2015 Neil C Smith
 * Copyright (C) 2014 Tom Greenwood <tgreenwood@cafex.com>
 * Copyright (C) 2009 Levente Farkas
 * Copyright (C) 2009 Tamas Korodi <kotyo@zamba.fm>
 * Copyright (c) 2009 Andres Colubri
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

package org.freedesktop.gstreamer;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freedesktop.gstreamer.glib.GQuark;
import org.freedesktop.gstreamer.lowlevel.EnumMapper;
import org.freedesktop.gstreamer.lowlevel.GObjectAPI;
import org.freedesktop.gstreamer.lowlevel.GObjectAPI.GObjectStruct;
import org.freedesktop.gstreamer.lowlevel.GObjectAPI.GParamSpec;
import org.freedesktop.gstreamer.lowlevel.GSignalAPI;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GValueAPI;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;
import org.freedesktop.gstreamer.lowlevel.GstTypes;
import org.freedesktop.gstreamer.lowlevel.IntPtr;
import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.lowlevel.RefCountedObject;

import com.sun.jna.Callback;
import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * This is an abstract class providing some GObject-like facilities in a common
 * base class.  Not intended for direct use.
 */
public abstract class GObject extends RefCountedObject {
    private static final Logger logger = Logger.getLogger(GObject.class.getName());
    private static final Level LIFECYCLE = Level.FINE;

    private static final Map<GObject, Boolean> strongReferences = new ConcurrentHashMap<GObject, Boolean>();

    private Map<Class<?>, Map<Object, GCallback>> callbackListeners;
    private Map<String, Map<Closure, ClosureProxy>> signalClosures;

    private final IntPtr objectID = new IntPtr(System.identityHashCode(this));

    public GObject(Initializer init) {
        super(init.needRef ? NativeObject.initializer(init.ptr, false, init.ownsHandle) : init);
        if (GObject.logger.isLoggable(Level.FINER)) {
            GObject.logger.entering("GObject", "<init>", new Object[] { init });
        }
        if (init.ownsHandle) {
            final boolean is_floating = GObjectAPI.GOBJECT_API.g_object_is_floating(init.ptr);
            if (GObject.logger.isLoggable(Level.FINE)) {
                GObject.logger.fine("Initialising owned handle for " + this.getClass().getName()
                        + " FLOATING = " + is_floating
                        + " refs = " + this.getRefCount()
                        + " need ref = " + init.needRef);
            }

            if (!init.needRef) {
                // Toggle ref has created extra reference
                if (is_floating)
                {
                    // Sink floating ref
                    this.sink();
                }
            }

            // If only one ref then this is weak!  This would only be the case where we didn't own
            if (this.getRefCount() >= 1) {
                GObject.strongReferences.put(this, Boolean.TRUE);
            }

            GObjectAPI.GOBJECT_API.g_object_add_toggle_ref(init.ptr, GObject.toggle, this.objectID);
            if (!init.needRef) {
                // Toggle ref has created extra reference
                this.unref();
            }
        }
    }

    /**
     * Sink floating reference.  This will turn a floating
     * reference into a real one.
     */
    protected void sink()
    {
        GObjectAPI.GOBJECT_API.g_object_ref_sink(this.handle());
    }

    /**
     * Get the reference count for an object.  Should only really be used for debugging
     * @return The reference count for the object
     */
    public int getRefCount()
    {
        // Check if disposed as a disposed object may
        // have been free'd and we mustn't access it's
        // memory or we face possible SEGFAULT
        if (!this.isDisposed())
        {
            final GObjectStruct struct = new GObjectStruct(this);
            return struct.ref_count;
        }
        return 0;
    }

    /**
     * Gives the type value.
     */
    public GType getType() {
    	return GObject.getType(this.handle());
    }

    public static GType getType(Pointer ptr) {
    	return new GType(new GObjectStruct(ptr).g_type_instance.g_class.getNativeLong(0).longValue());
    }

    /**
     * Gives the type name.
     */
    public String getTypeName() {
    	return GObjectAPI.GOBJECT_API.g_type_name(this.getType());
    }
    /**
     * Sets the value of a <tt>GObject</tt> property.
     *
     * @param property The property to set.
     * @param data The value for the property.  This must be of the type expected
     * by gstreamer.
     */
    public void set(String property, Object data) {
        if (GObject.logger.isLoggable(Level.FINER)) {
            GObject.logger.entering("GObject", "set", new Object[] { property, data });
        }

        GObjectAPI.GParamSpec propertySpec = this.findProperty(property);
        if (propertySpec == null || data == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;

        GValue propValue = new GValue();
        GValueAPI.GVALUE_API.g_value_init(propValue, propType);
        if (propType.equals(GType.INT)) {
            GValueAPI.GVALUE_API.g_value_set_int(propValue, GObject.intValue(data));
        } else if (propType.equals(GType.UINT)) {
            GValueAPI.GVALUE_API.g_value_set_uint(propValue, GObject.intValue(data));
        } else if (propType.equals(GType.CHAR)) {
            GValueAPI.GVALUE_API.g_value_set_char(propValue, (byte) GObject.intValue(data));
        } else if (propType.equals(GType.UCHAR)) {
            GValueAPI.GVALUE_API.g_value_set_uchar(propValue, (byte) GObject.intValue(data));
        } else if (propType.equals(GType.LONG)) {
            GValueAPI.GVALUE_API.g_value_set_long(propValue, new NativeLong(GObject.longValue(data)));
        } else if (propType.equals(GType.ULONG)) {
            GValueAPI.GVALUE_API.g_value_set_ulong(propValue, new NativeLong(GObject.longValue(data)));
        } else if (propType.equals(GType.INT64)) {
            GValueAPI.GVALUE_API.g_value_set_int64(propValue, GObject.longValue(data));
        } else if (propType.equals(GType.UINT64)) {
            GValueAPI.GVALUE_API.g_value_set_uint64(propValue, GObject.longValue(data));
        } else if (propType.equals(GType.BOOLEAN)) {
            GValueAPI.GVALUE_API.g_value_set_boolean(propValue, GObject.booleanValue(data));
        } else if (propType.equals(GType.FLOAT)) {
            GValueAPI.GVALUE_API.g_value_set_float(propValue, GObject.floatValue(data));
        } else if (propType.equals(GType.DOUBLE)) {
            GValueAPI.GVALUE_API.g_value_set_double(propValue, GObject.doubleValue(data));
        } else if (propType.equals(GType.STRING)) {
            //
            // Special conversion of java URI to gstreamer compatible uri
            //
            if (data instanceof URI) {
                URI uri = (URI) data;
                String uriString = uri.toString();
                // Need to fixup file:/ to be file:/// for gstreamer
                if ("file".equals(uri.getScheme()) && uri.getHost() == null) {
                    final String path = uri.getRawPath();
                    uriString = "file://" + path;
                }
                GValueAPI.GVALUE_API.g_value_set_string(propValue, uriString);
            } else {
                GValueAPI.GVALUE_API.g_value_set_string(propValue, data.toString());
            }
        } else if (propType.equals(GType.OBJECT)) {
            GValueAPI.GVALUE_API.g_value_set_object(propValue, (GObject) data);
        } else if (GValueAPI.GVALUE_API.g_value_type_transformable(GType.INT64, propType)) {
            GObject.transform(data, GType.INT64, propValue);
        } else if (GValueAPI.GVALUE_API.g_value_type_transformable(GType.LONG, propType)) {
            GObject.transform(data, GType.LONG, propValue);
        } else if (GValueAPI.GVALUE_API.g_value_type_transformable(GType.INT, propType)) {
            GObject.transform(data, GType.INT, propValue);
        } else if (GValueAPI.GVALUE_API.g_value_type_transformable(GType.DOUBLE, propType)) {
            GObject.transform(data, GType.DOUBLE, propValue);
        } else if (GValueAPI.GVALUE_API.g_value_type_transformable(GType.FLOAT, propType)) {
            GObject.transform(data, GType.FLOAT, propValue);
        } else {
            // Old behaviour
            GObjectAPI.GOBJECT_API.g_object_set(this, property, data);
            return;
        }
        GObjectAPI.GOBJECT_API.g_param_value_validate(propertySpec, propValue);
        GObjectAPI.GOBJECT_API.g_object_set_property(this, property, propValue);
        GValueAPI.GVALUE_API.g_value_unset(propValue); // Release any memory
    }

    /**
     * Gets the default value set to <tt>GObject</tt> property.
     * @param property The name of the property.
     * @return A java value representing the <tt>GObject</tt> property's default value.
     */
    public Object getPropertyDefaultValue(String property) {
        GObjectAPI.GParamSpec propertySpec = this.findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;
        return this.findProperty(property, propType).getDefault();
    }

    /**
     * Gets the minimum value should be set to <tt>GObject</tt> property.
     * @param property The name of the property.
     * @return A java value representing the <tt>GObject</tt> property's minimum value.
     */
    public Object getPropertyMinimumValue(String property) {
        GObjectAPI.GParamSpec propertySpec = this.findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;
        return this.findProperty(property, propType).getMinimum();
    }

    /**
     * Gets the maximum value should be set to <tt>GObject</tt> property.
     * @param property The name of the property.
     * @return A java value representing the <tt>GObject</tt> property's maximum value.
     */
    public Object getPropertyMaximumValue(String property) {
        GObjectAPI.GParamSpec propertySpec = this.findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;
        return this.findProperty(property, propType).getMaximum();
    }

    /**
     * Gets the current value of a <tt>GObject</tt> property.
     *
     * @param property The name of the property to get.
     *
     * @return A java value representing the <tt>GObject</tt> property value.
     */
    public Object get(String property) {
        if (GObject.logger.isLoggable(Level.FINER)) {
            GObject.logger.entering("GObject", "get", new Object[] { property });
        }
        GObjectAPI.GParamSpec propertySpec = this.findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;
        GValue propValue = new GValue();
        GValueAPI.GVALUE_API.g_value_init(propValue, propType);
        GObjectAPI.GOBJECT_API.g_object_get_property(this, property, propValue);
        if (propType.equals(GType.INT)) {
            return GValueAPI.GVALUE_API.g_value_get_int(propValue);
        } else if (propType.equals(GType.UINT)) {
            return GValueAPI.GVALUE_API.g_value_get_uint(propValue);
        } else if (propType.equals(GType.CHAR)) {
            return Integer.valueOf(GValueAPI.GVALUE_API.g_value_get_char(propValue));
        } else if (propType.equals(GType.UCHAR)) {
            return Integer.valueOf(GValueAPI.GVALUE_API.g_value_get_uchar(propValue));
        } else if (propType.equals(GType.LONG)) {
            return GValueAPI.GVALUE_API.g_value_get_long(propValue).longValue();
        } else if (propType.equals(GType.ULONG)) {
            return GValueAPI.GVALUE_API.g_value_get_ulong(propValue).longValue();
        } else if (propType.equals(GType.INT64)) {
            return GValueAPI.GVALUE_API.g_value_get_int64(propValue);
        } else if (propType.equals(GType.UINT64)) {
            return GValueAPI.GVALUE_API.g_value_get_uint64(propValue);
        } else if (propType.equals(GType.BOOLEAN)) {
            return GValueAPI.GVALUE_API.g_value_get_boolean(propValue);
        } else if (propType.equals(GType.FLOAT)) {
            return GValueAPI.GVALUE_API.g_value_get_float(propValue);
        } else if (propType.equals(GType.DOUBLE)) {
            return GValueAPI.GVALUE_API.g_value_get_double(propValue);
        } else if (propType.equals(GType.STRING)) {
            return GValueAPI.GVALUE_API.g_value_get_string(propValue);
        } else if (propType.equals(GType.OBJECT)) {
            return GValueAPI.GVALUE_API.g_value_dup_object(propValue);
        } else if (GValueAPI.GVALUE_API.g_value_type_transformable(propType, GType.OBJECT)) {
            return GValueAPI.GVALUE_API.g_value_dup_object(GObject.transform(propValue, GType.OBJECT));
        } else if (GValueAPI.GVALUE_API.g_value_type_transformable(propType, GType.INT)) {
            return GValueAPI.GVALUE_API.g_value_get_int(GObject.transform(propValue, GType.INT));
        } else if (GValueAPI.GVALUE_API.g_value_type_transformable(propType, GType.INT64)) {
            return GValueAPI.GVALUE_API.g_value_get_int64(GObject.transform(propValue, GType.INT64));
        } else if (propValue.checkHolds(GType.BOXED)) {
                Class<? extends NativeObject> cls = GstTypes.classFor(propType);
                if (cls != null) {
                    Pointer ptr = GValueAPI.GVALUE_API.g_value_get_boxed(propValue);
                    return NativeObject.objectFor(ptr, cls, -1, true);
                }
        }
        throw new IllegalArgumentException("Unknown conversion from GType=" + propType);
    }

	public List<String> listPropertyNames() {
		GObjectAPI.GParamSpec[] lst = this.listProperties();
		List<String> result = new ArrayList<String>(lst.length);
		for (GParamSpec element : lst)
			result.add(element.g_name);
		return result;
	}

	private GObjectAPI.GParamSpec[] listProperties() {
		IntByReference len = new IntByReference();
		Pointer ptrs = GObjectAPI.GOBJECT_API.g_object_class_list_properties(this.handle().getPointer(0), len);
		if (ptrs == null)
			return null;

		GParamSpec[] props = new GParamSpec[len.getValue()];
		int offset = 0;
		for (int i = 0; i < len.getValue(); i++) {
			props[i] = new GObjectAPI.GParamSpec(ptrs.getPointer(offset));
			offset += Pointer.SIZE;
		}
		return props;
	}

    public GType getType(String property) {
    	if (GObject.logger.isLoggable(Level.FINER)) {
            GObject.logger.entering("GObject", "getType", new Object[] { property });
    	}
        GObjectAPI.GParamSpec propertySpec = this.findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;
        return propType;
    }

    /**
     * Gets the pointer to the the value of the specified property.
     *
     * @param property The name of the property to get.
     *
     * @return A java pointer.
     */
    public Pointer getPointer(String property) {
    	if (GObject.logger.isLoggable(Level.FINER)) {
            GObject.logger.entering("GObject", "getPointer", new Object[] { property });
    	}
        GObjectAPI.GParamSpec propertySpec = this.findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }

        PointerByReference refPtr = new PointerByReference();
        GObjectAPI.GOBJECT_API.g_object_get(this, property, refPtr, null);
        Pointer ptr = refPtr.getValue();
        return ptr;
    }

    private static GValue transform(GValue src, GType dstType) {
        GValue dst = new GValue();
        GValueAPI.GVALUE_API.g_value_init(dst, dstType);
        GValueAPI.GVALUE_API.g_value_transform(src, dst);
        return dst;
    }
    private static void transform(Object data, GType type, GValue dst) {
        GValue src = new GValue();
        GValueAPI.GVALUE_API.g_value_init(src, type);
        GObject.setGValue(src, type, data);
        GValueAPI.GVALUE_API.g_value_transform(src, dst);
    }
    private static boolean setGValue(GValue value, GType type, Object data) {
        if (type.equals(GType.INT)) {
            GValueAPI.GVALUE_API.g_value_set_int(value, GObject.intValue(data));
        } else if (type.equals(GType.UINT)) {
            GValueAPI.GVALUE_API.g_value_set_uint(value, GObject.intValue(data));
        } else if (type.equals(GType.CHAR)) {
            GValueAPI.GVALUE_API.g_value_set_char(value, (byte) GObject.intValue(data));
        } else if (type.equals(GType.UCHAR)) {
            GValueAPI.GVALUE_API.g_value_set_uchar(value, (byte) GObject.intValue(data));
        } else if (type.equals(GType.LONG)) {
            GValueAPI.GVALUE_API.g_value_set_long(value, new NativeLong(GObject.longValue(data)));
        } else if (type.equals(GType.ULONG)) {
            GValueAPI.GVALUE_API.g_value_set_ulong(value, new NativeLong(GObject.longValue(data)));
        } else if (type.equals(GType.INT64)) {
            GValueAPI.GVALUE_API.g_value_set_int64(value, GObject.longValue(data));
        } else if (type.equals(GType.UINT64)) {
            GValueAPI.GVALUE_API.g_value_set_uint64(value, GObject.longValue(data));
        } else if (type.equals(GType.BOOLEAN)) {
            GValueAPI.GVALUE_API.g_value_set_boolean(value, GObject.booleanValue(data));
        } else if (type.equals(GType.FLOAT)) {
            GValueAPI.GVALUE_API.g_value_set_float(value, GObject.floatValue(data));
        } else if (type.equals(GType.DOUBLE)) {
            GValueAPI.GVALUE_API.g_value_set_double(value, GObject.doubleValue(data));
        } else {
            return false;
        }
        return true;
    }
    private static boolean booleanValue(Object value) {
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        } else if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        } else if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        throw new IllegalArgumentException("Expected boolean value, not " + value.getClass());
    }
    private static int intValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        throw new IllegalArgumentException("Expected integer value, not " + value.getClass());
    }
    private static long longValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            return Long.parseLong((String) value);
        }
        throw new IllegalArgumentException("Expected long value, not " + value.getClass());
    }
    private static float floatValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else if (value instanceof String) {
            return Float.parseFloat((String) value);
        }
        throw new IllegalArgumentException("Expected float value, not " + value.getClass());
    }
    private static double doubleValue(Object value) {
        if (value instanceof Number) {
            return  ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        }
        throw new IllegalArgumentException("Expected double value, not " + value.getClass());
    }

    @Override
    protected void disposeNativeHandle(Pointer ptr) {
    	if (GObject.logger.isLoggable(GObject.LIFECYCLE)) {
            GObject.logger.log(GObject.LIFECYCLE, "Removing toggle ref " + this.getClass().getSimpleName() + " (" + ptr + ")");
    	}
        GObjectAPI.GOBJECT_API.g_object_remove_toggle_ref(ptr, GObject.toggle, this.objectID);
        GObject.strongReferences.remove(this);
    }
    @Override
    protected void ref() {
        GObjectAPI.GOBJECT_API.g_object_ref(this);
    }

    @Override
    protected void unref() {
        GObjectAPI.GOBJECT_API.g_object_unref(this);
    }
    @Override
    protected void invalidate() {
        try {
            // Need to increase the ref count before removing the toggle ref, so
            // ensure the native object is not destroyed.
            if (this.ownsHandle.get()) {
                this.ref();

                // Disconnect the callback.
                GObjectAPI.GOBJECT_API.g_object_remove_toggle_ref(this.handle(), GObject.toggle, this.objectID);
            }
            GObject.strongReferences.remove(this);
        } finally {
            super.invalidate();
        }
    }

    protected NativeLong g_signal_connect(String signal, Callback callback) {
    	if (GObject.logger.isLoggable(Level.FINER)) {
            GObject.logger.entering("GObject", "g_signal_connect", new Object[] { signal, callback });
    	}
        return GObjectAPI.GOBJECT_API.g_signal_connect_data(this, signal, callback, null, null, 0);
    }

    private final static CallbackThreadInitializer cbi = new CallbackThreadInitializer(true, false, "GCallback");

    abstract protected class GCallback {
        protected final Callback cb;
        protected final NativeLong id;
        volatile boolean connected = false;

        protected GCallback(NativeLong id, Callback cb) {
            this.id = id != null ? id : new NativeLong(0);
            this.cb = cb;
            this.connected = this.id.intValue() != 0;
        }
        void remove() {
            if (this.connected) {
                this.disconnect();
                this.connected = false;
            }
        }
        abstract protected void disconnect();
        @Override
        protected final void finalize() {
            // Ensure the native callback is removed
            this.remove();
        }
    }
    private final class SignalCallback extends GCallback {
        protected SignalCallback(String signal, Callback cb) {
            super(GObject.this.g_signal_connect(signal, cb), cb);
            if (!this.connected) {
                throw new IllegalArgumentException(String.format("Failed to connect signal '%s'", signal));
            }
        }
        @Override
        synchronized protected void disconnect() {
            GObjectAPI.GOBJECT_API.g_signal_handler_disconnect(GObject.this, this.id);
        }
    }
    private synchronized final Map<Class<?>, Map<Object, GCallback>> getCallbackMap() {
        if (this.callbackListeners == null) {
            this.callbackListeners = new ConcurrentHashMap<Class<?>, Map<Object, GCallback>>();
        }
        return this.callbackListeners;
    }
    private synchronized final Map<String, Map<Closure, ClosureProxy>> getClosureMap() {
        if (this.signalClosures == null) {
            this.signalClosures = new ConcurrentHashMap<String, Map<Closure, ClosureProxy>>();
        }
        return this.signalClosures;
    }

    protected synchronized <T> void  addCallback(Class<T> listenerClass, T listener, GCallback cb) {
        final Map<Class<?>, Map<Object, GCallback>> signals = this.getCallbackMap();
        Map<Object, GCallback> map = signals.get(listenerClass);
        if (map == null) {
            map = new HashMap<Object, GCallback>();
            signals.put(listenerClass, map);
        }
        map.put(listener, cb);
    }

    public synchronized <T> void removeCallback(Class<T> listenerClass, T listener) {
        final Map<Class<?>, Map<Object, GCallback>> signals = this.getCallbackMap();
        Map<Object, GCallback> map = signals.get(listenerClass);
        if (map != null) {
            GCallback cb = map.remove(listener);
            if (cb != null) {
                cb.remove();
            }
            if (map.isEmpty()) {
                signals.remove(listenerClass);
                if (this.callbackListeners.isEmpty()) {
                    this.callbackListeners = null;
                }
            }
        }
    }
    public <T> void connect(Class<T> listenerClass, T listener, Callback cb) {
        String signal = listenerClass.getSimpleName().toLowerCase().replaceAll("_", "-");
        this.connect(signal, listenerClass, listener, cb);
    }

    public synchronized <T> void connect(String signal, Class<T> listenerClass, T listener, Callback cb) {
        Native.setCallbackThreadInitializer(cb, GObject.cbi);
        this.addCallback(listenerClass, listener, new SignalCallback(signal, cb));
    }

    public synchronized <T> void disconnect(Class<T> listenerClass, T listener) {
        this.removeCallback(listenerClass, listener);
    }
    private final class ClosureProxy implements GSignalAPI.GSignalCallbackProxy {
        private final Closure closure;
        private final Method method;
        private final Class<?>[] parameterTypes;
        NativeLong id;

        protected ClosureProxy(String signal, Closure closure) {
            this.closure = closure;

            Method invoke = null;
            for (Method m : closure.getClass().getDeclaredMethods()) {
                if (m.getName().equals(Closure.METHOD_NAME)) {
                    invoke = m;
                    break;
                }
            }
            if (invoke == null) {
                throw new IllegalArgumentException(closure.getClass()
                        + " does not have an invoke method");
            }
            invoke.setAccessible(true);
            this.method = invoke;
            //
            // The closure does not have a 'user_data' pointer, so push it in as the
            // last arg.  The last arg will be dropped later in callback()
            //
            this.parameterTypes = new Class[this.method.getParameterTypes().length + 1];
            this.parameterTypes[this.parameterTypes.length - 1] = Pointer.class;
            for (int i = 0; i < this.method.getParameterTypes().length; ++i) {
                Class<?> paramType = this.method.getParameterTypes()[i];
                Class<?> nativeType = paramType;
                if (ClockTime.class.isAssignableFrom(paramType)) {
                    nativeType = long.class;
                } else if (NativeObject.class.isAssignableFrom(paramType)) {
                    nativeType = Pointer.class;
                } else if (Enum.class.isAssignableFrom(paramType)) {
                    nativeType = int.class;
                } else if (String.class.isAssignableFrom(paramType)) {
                    nativeType = Pointer.class;
                } else if (Boolean.class.isAssignableFrom(paramType)) {
                    nativeType = int.class;
                }
                this.parameterTypes[i] = nativeType;
            }
            NativeLong connectID = GSignalAPI.GSIGNAL_API.g_signal_connect_data(GObject.this,
                    signal, this, null, null, 0);
            if (connectID.intValue() == 0) {
                throw new IllegalArgumentException(String.format("Failed to connect signal '%s'", signal));
            }
            this.id = connectID;
        }
        synchronized protected void disconnect() {
            if (this.id != null && this.id.intValue() != 0) {
                GObjectAPI.GOBJECT_API.g_signal_handler_disconnect(GObject.this, this.id);
                this.id = null;
            }
        }
        @Override
        protected void finalize() {
            // Ensure the native callback is removed
            this.disconnect();
        }
        @Override
		@SuppressWarnings({"unchecked","rawtypes"})
        public Object callback(Object[] parameters) {

            try {
                // Drop the last arg - it is the 'user_data' pointer
                Object[] methodParameters = new Object[parameters.length - 1];

                for (int i = 0; i < methodParameters.length; ++i) {
                    Class paramType = this.method.getParameterTypes()[i];
                    Object nativeParam = parameters[i];
                    Object javaParam = nativeParam;
                    if (nativeParam == null) {
                        continue;
                    }
                    if (ClockTime.class.isAssignableFrom(paramType)) {
                        javaParam = ClockTime.valueOf((Long) nativeParam,
                                TimeUnit.NANOSECONDS);
                    } else if (NativeObject.class.isAssignableFrom(paramType)) {
                        javaParam = NativeObject.objectFor((Pointer) nativeParam,
                                paramType, 1, true);
                    } else if (Enum.class.isAssignableFrom(paramType)) {
                        javaParam = EnumMapper.getInstance().valueOf((Integer) nativeParam,
                                paramType);
                    } else if (String.class.isAssignableFrom(paramType)) {
                        javaParam = ((Pointer) nativeParam).getString(0);
                    } else if (Boolean.class.isAssignableFrom(paramType)) {
                        javaParam = Boolean.valueOf(((Integer) nativeParam).intValue() != 0);
                    } else {
                        javaParam = nativeParam;
                    }
                    methodParameters[i] = javaParam;
                }

                return this.method.invoke(this.closure, methodParameters);
            } catch (Throwable t) {
                return Integer.valueOf(0);
            }
        }

        @Override
		public Class<?>[] getParameterTypes() {
            return this.parameterTypes;
        }

        @Override
		public Class<?> getReturnType() {
            return this.method.getReturnType();
        }
    }
    public synchronized void connect(String signal, Closure closure) {
        final Map<String, Map<Closure, ClosureProxy>> signals = this.getClosureMap();
        Map<Closure, ClosureProxy> m = signals.get(signal);
        if (m == null) {
            m = new HashMap<Closure, ClosureProxy>();
            signals.put(signal, m);
        }
        m.put(closure, new ClosureProxy(signal, closure));
    }
    public synchronized void disconnect(String signal, Closure closure) {
        final Map<String, Map<Closure, ClosureProxy>> signals = this.signalClosures;
        if (signals == null) {
            return;
        }
        Map<Closure, ClosureProxy> map = signals.get(signal);
        if (map != null) {
            ClosureProxy cb = map.remove(closure);
            if (cb != null) {
                cb.disconnect();
            }
            if (map.isEmpty()) {
                signals.remove(signal);
                if (this.signalClosures.isEmpty()) {
                    this.signalClosures = null;
                }
            }
        }
    }

    public synchronized void emit(int signal_id, GQuark detail, Object... arguments) {
    	GSignalAPI.GSIGNAL_API.g_signal_emit(this, signal_id, detail, arguments);
    }

    public synchronized void emit(String signal, Object... arguments) {
    	GSignalAPI.GSIGNAL_API.g_signal_emit_by_name(this, signal, arguments);
    }

//    public static <T extends GObject> T objectFor(Pointer ptr, Class<T> defaultClass) {
//        return objectFor(ptr, defaultClass, true);
//    }

    private GObjectAPI.GParamSpec findProperty(String propertyName) {
        Pointer ptr = GObjectAPI.GOBJECT_API.g_object_class_find_property(this.handle().getPointer(0), propertyName);
        if (ptr == null)
            return null;
        return new GObjectAPI.GParamSpec(ptr);
    }

    private GObjectAPI.GParamSpecTypeSpecific findProperty(String propertyName, GType type) {
    	Pointer ptr = GObjectAPI.GOBJECT_API.g_object_class_find_property(this.handle().getPointer(0), propertyName);
    	if (type.equals(GType.INT))
    		return new GObjectAPI.GParamSpecInt(ptr);
    	else if(type.equals(GType.UINT))
    		return new GObjectAPI.GParamSpecUInt(ptr);
    	else if(type.equals(GType.CHAR))
    		return new GObjectAPI.GParamSpecChar(ptr);
    	else if(type.equals(GType.UCHAR))
    		return new GObjectAPI.GParamSpecUChar(ptr);
    	else if(type.equals(GType.BOOLEAN))
    		return new GObjectAPI.GParamSpecBoolean(ptr);
    	else if(type.equals(GType.LONG))
    		return new GObjectAPI.GParamSpecLong(ptr);
    	else if(type.equals(GType.ULONG))
        return new GObjectAPI.GParamSpecLong(ptr);
    	else if(type.equals(GType.INT64))
    		return new GObjectAPI.GParamSpecInt64(ptr);
      else if(type.equals(GType.UINT64))
        return new GObjectAPI.GParamSpecInt64(ptr);
    	else if(type.equals(GType.FLOAT))
    		return new GObjectAPI.GParamSpecFloat(ptr);
    	else if(type.equals(GType.DOUBLE))
    		return new GObjectAPI.GParamSpecDouble(ptr);
    	else if(type.equals(GType.STRING))
    		return new GObjectAPI.GParamSpecString(ptr);
    	throw new IllegalArgumentException("Unknown conversion from GType=" + type);
    }
    /*
     * Hooks to/from native disposal
     */
    private static final GObjectAPI.GToggleNotify toggle = new GObjectAPI.GToggleNotify() {
        @Override
		public void callback(Pointer data, Pointer ptr, boolean is_last_ref) {

            /*
             * Manage the strong reference to this instance.  When this is the last
             * reference to the underlying object, remove the strong reference so
             * it can be garbage collected.  If it is owned by someone else, then make
             * it a strong ref, so the java GObject for the underlying C object can
             * be retained for later retrieval
             */
            GObject o = (GObject) NativeObject.instanceFor(ptr);
            if (o == null) {
                return;
            }
            GObject.logger.log(GObject.LIFECYCLE, "toggle_ref " + o.getClass().getSimpleName() +
                    " (" +  ptr + ")" + " last_ref=" + is_last_ref);
            if (is_last_ref) {
                GObject.strongReferences.remove(o);
            } else {
                GObject.strongReferences.put(o, Boolean.TRUE);
            }
        }
    };
}
