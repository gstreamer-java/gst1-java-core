/*
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer.glib;

import static org.freedesktop.gstreamer.lowlevel.GObjectAPI.GOBJECT_API;
import static org.freedesktop.gstreamer.lowlevel.GSignalAPI.GSIGNAL_API;
import static org.freedesktop.gstreamer.lowlevel.GValueAPI.GVALUE_API;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freedesktop.gstreamer.lowlevel.GObjectAPI;
import org.freedesktop.gstreamer.lowlevel.GObjectAPI.GParamSpec;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValue;
import org.freedesktop.gstreamer.lowlevel.GstTypes;
import org.freedesktop.gstreamer.lowlevel.IntPtr;

import com.sun.jna.Callback;
import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.lowlevel.GObjectPtr;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectPtr;

/**
 * GObject is the fundamental type providing the common attributes and methods
 * for all object types in libraries based on the GObject system.
 * <p>
 * See upstream documentation at <a href="https://developer.gnome.org/gobject/stable/gobject-The-Base-Object-Type.html"
 * >https://developer.gnome.org/gobject/stable/gobject-The-Base-Object-Type.html</a>
 *
 */
public abstract class GObject extends RefCountedObject {

    private static final Level LIFECYCLE = Level.FINE;
    private static final Logger LOG = Logger.getLogger(GObject.class.getName());
    private static final CallbackThreadInitializer GCALLBACK_THREAD_INIT
            = new CallbackThreadInitializer(true,
                    Boolean.getBoolean("glib.detachCallbackThreads"),
                    "GCallback");
    private static final Map<GObject, Boolean> STRONG_REFS
            = new ConcurrentHashMap<GObject, Boolean>();
    private static final GObjectAPI.GToggleNotify TOGGLE_NOTIFY = new ToggleNotify();

    private final Handle handle;
    private Map<Class<?>, Map<Object, GCallback>> callbackListeners;

    protected GObject(Initializer init) {
        this(new Handle(init.ptr.as(GObjectPtr.class, GObjectPtr::new), init.ownsHandle), init.needRef);
    }

    protected GObject(Handle handle, boolean needRef) {
        super(handle);
        this.handle = handle;
        if (handle.ownsReference()) {
            final boolean is_floating = GOBJECT_API.g_object_is_floating(handle.getPointer());
            LOG.log(LIFECYCLE, () -> String.format(
                    "Initialising owned handle for %s floating = %b refs = %d need ref = %b",
                    this.getClass().getName(),
                    is_floating,
                    getRefCount(),
                    needRef));

            if (!needRef) {
                if (is_floating) {
                    // Sink floating ref
                    handle.sink();
                }
            }

            // If only one ref then this is weak!  This would only be the case where we didn't own
            if (getRefCount() >= 1) {
                STRONG_REFS.put(this, Boolean.TRUE);
            }

            GOBJECT_API.g_object_add_toggle_ref(handle.getPointer(), TOGGLE_NOTIFY, handle.objectID);
            if (!needRef) {
                // Toggle ref has created extra reference
                handle.unref();
            }
        }
    }

    public <T> void connect(Class<T> listenerClass, T listener, Callback cb) {
        String signal = listenerClass.getSimpleName().toLowerCase().replaceAll("_", "-");
        connect(signal, listenerClass, listener, cb);
    }

    public synchronized <T> void connect(String signal, Class<T> listenerClass, T listener, Callback cb) {
        Native.setCallbackThreadInitializer(cb, GCALLBACK_THREAD_INIT);
        addCallback(listenerClass, listener, new SignalCallback(signal, cb));
    }

    public synchronized <T> void disconnect(Class<T> listenerClass, T listener) {
        removeCallback(listenerClass, listener);
    }

    public synchronized void emit(String signal, Object... arguments) {
        GSIGNAL_API.g_signal_emit_by_name(this, signal, arguments);
    }

    public synchronized <T extends NativeObject> T emit(Class<T> resultType, String signal, Object... arguments) {
        PointerByReference pointerToResult = new PointerByReference(null);
        Object[] fullArguments = Arrays.copyOf(arguments, arguments.length + 1);
        fullArguments[arguments.length] = pointerToResult;
        emit(signal, fullArguments);
        Pointer result = pointerToResult.getValue();
        if (result == null) {
            return null;
        } else {
            return Natives.objectFor(result, resultType, false, true);
        }
    }

    /**
     * Gets the current value of a <tt>GObject</tt> property.
     *
     * @param property The name of the property to get.
     *
     * @return A java value representing the <tt>GObject</tt> property value.
     */
    public Object get(String property) {
        LOG.entering("GObject", "get", new Object[]{property});
        GObjectAPI.GParamSpec propertySpec = findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;
        GValue propValue = new GValue();
        GVALUE_API.g_value_init(propValue, propType);
        GOBJECT_API.g_object_get_property(this, property, propValue);
        if (propType.equals(GType.INT)) {
            return GVALUE_API.g_value_get_int(propValue);
        } else if (propType.equals(GType.UINT)) {
            return GVALUE_API.g_value_get_uint(propValue);
        } else if (propType.equals(GType.CHAR)) {
            return Integer.valueOf(GVALUE_API.g_value_get_char(propValue));
        } else if (propType.equals(GType.UCHAR)) {
            return Integer.valueOf(GVALUE_API.g_value_get_uchar(propValue));
        } else if (propType.equals(GType.LONG)) {
            return GVALUE_API.g_value_get_long(propValue).longValue();
        } else if (propType.equals(GType.ULONG)) {
            return GVALUE_API.g_value_get_ulong(propValue).longValue();
        } else if (propType.equals(GType.INT64)) {
            return GVALUE_API.g_value_get_int64(propValue);
        } else if (propType.equals(GType.UINT64)) {
            return GVALUE_API.g_value_get_uint64(propValue);
        } else if (propType.equals(GType.BOOLEAN)) {
            return GVALUE_API.g_value_get_boolean(propValue);
        } else if (propType.equals(GType.FLOAT)) {
            return GVALUE_API.g_value_get_float(propValue);
        } else if (propType.equals(GType.DOUBLE)) {
            return GVALUE_API.g_value_get_double(propValue);
        } else if (propType.equals(GType.STRING)) {
            return GVALUE_API.g_value_get_string(propValue);
        } else if (propType.equals(GType.OBJECT)) {
            return GVALUE_API.g_value_dup_object(propValue);
        } else if (GVALUE_API.g_value_type_transformable(propType, GType.OBJECT)) {
            return GVALUE_API.g_value_dup_object(transform(propValue, GType.OBJECT));
        } else if (GVALUE_API.g_value_type_transformable(propType, GType.INT)) {
            return GVALUE_API.g_value_get_int(transform(propValue, GType.INT));
        } else if (GVALUE_API.g_value_type_transformable(propType, GType.INT64)) {
            return GVALUE_API.g_value_get_int64(transform(propValue, GType.INT64));
        } else if (propValue.checkHolds(GType.BOXED)) {
            // @TODO we already know the GType here - optimise this!
            Class<? extends NativeObject> cls = GstTypes.classFor(propType);
            if (cls != null) {
                Pointer ptr = GVALUE_API.g_value_get_boxed(propValue);
                final GPointer gptr = GObject.class.isAssignableFrom(cls) ? new GObjectPtr(ptr)
                        : MiniObject.class.isAssignableFrom(cls) ? new GstMiniObjectPtr(ptr)
                        : new GPointer(ptr);
                return objectFor(gptr, cls, -1, true);
            }
        }
        throw new IllegalArgumentException("Unknown conversion from GType=" + propType);
    }

    /**
     * Gets the default value set to <tt>GObject</tt> property.
     *
     * @param property The name of the property.
     * @return A java value representing the <tt>GObject</tt> property's default
     * value.
     */
    public Object getPropertyDefaultValue(String property) {
        GObjectAPI.GParamSpec propertySpec = findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;
        return findProperty(property, propType).getDefault();
    }

    /**
     * Gets the maximum value should be set to <tt>GObject</tt> property.
     *
     * @param property The name of the property.
     * @return A java value representing the <tt>GObject</tt> property's maximum
     * value.
     */
    public Object getPropertyMaximumValue(String property) {
        GObjectAPI.GParamSpec propertySpec = findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;
        return findProperty(property, propType).getMaximum();
    }

    /**
     * Gets the minimum value should be set to <tt>GObject</tt> property.
     *
     * @param property The name of the property.
     * @return A java value representing the <tt>GObject</tt> property's minimum
     * value.
     */
    public Object getPropertyMinimumValue(String property) {
        GObjectAPI.GParamSpec propertySpec = findProperty(property);
        if (propertySpec == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;
        return findProperty(property, propType).getMinimum();
    }

    /**
     * Get the reference count for an object. Should only really be used for
     * debugging
     *
     * @return The reference count for the object
     */
    public int getRefCount() {
        // Check if disposed as a disposed object may
        // have been free'd and we mustn't access it's
        // memory or we face possible SEGFAULT
        GPointer ptr = handle.getPointer();

        if (ptr != null) {
//            final GObjectStruct struct = new GObjectStruct(this);
//            return struct.ref_count;

// ref count is after pointer to GTypeInstance
            int count = ptr.getPointer().getInt(Native.POINTER_SIZE);
            return count;
        }
        return 0;
    }

    /**
     * Get the native GType type name.
     * @return GType type name
     */
    public String getTypeName() {
        return handle.getPointer().getGType().getTypeName();
    }

    public List<String> listPropertyNames() {
        GObjectAPI.GParamSpec[] lst = listProperties();
        List<String> result = new ArrayList<String>(lst.length);
        for (int i = 0; i < lst.length; i++) {
            result.add(lst[i].g_name);
        }
        return result;
    }

    /**
     * Sets the value of a <tt>GObject</tt> property.
     *
     * @param property The property to set.
     * @param data The value for the property. This must be of the type expected
     * by gstreamer.
     */
    public void set(String property, Object data) {
        LOG.entering("GObject", "set", new Object[]{property, data});
        GObjectAPI.GParamSpec propertySpec = findProperty(property);
        if (propertySpec == null || data == null) {
            throw new IllegalArgumentException("Unknown property: " + property);
        }
        final GType propType = propertySpec.value_type;

        GValue propValue = new GValue();
        GVALUE_API.g_value_init(propValue, propType);
        if (propType.equals(GType.INT)) {
            GVALUE_API.g_value_set_int(propValue, intValue(data));
        } else if (propType.equals(GType.UINT)) {
            GVALUE_API.g_value_set_uint(propValue, intValue(data));
        } else if (propType.equals(GType.CHAR)) {
            GVALUE_API.g_value_set_char(propValue, (byte) intValue(data));
        } else if (propType.equals(GType.UCHAR)) {
            GVALUE_API.g_value_set_uchar(propValue, (byte) intValue(data));
        } else if (propType.equals(GType.LONG)) {
            GVALUE_API.g_value_set_long(propValue, new NativeLong(longValue(data)));
        } else if (propType.equals(GType.ULONG)) {
            GVALUE_API.g_value_set_ulong(propValue, new NativeLong(longValue(data)));
        } else if (propType.equals(GType.INT64)) {
            GVALUE_API.g_value_set_int64(propValue, longValue(data));
        } else if (propType.equals(GType.UINT64)) {
            GVALUE_API.g_value_set_uint64(propValue, longValue(data));
        } else if (propType.equals(GType.BOOLEAN)) {
            GVALUE_API.g_value_set_boolean(propValue, booleanValue(data));
        } else if (propType.equals(GType.FLOAT)) {
            GVALUE_API.g_value_set_float(propValue, floatValue(data));
        } else if (propType.equals(GType.DOUBLE)) {
            GVALUE_API.g_value_set_double(propValue, doubleValue(data));
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
                GVALUE_API.g_value_set_string(propValue, uriString);
            } else {
                GVALUE_API.g_value_set_string(propValue, data.toString());
            }
        } else if (propType.equals(GType.OBJECT)) {
            GVALUE_API.g_value_set_object(propValue, (GObject) data);
        } else if (GVALUE_API.g_value_type_transformable(GType.INT64, propType)) {
            transform(data, GType.INT64, propValue);
        } else if (GVALUE_API.g_value_type_transformable(GType.LONG, propType)) {
            transform(data, GType.LONG, propValue);
        } else if (GVALUE_API.g_value_type_transformable(GType.INT, propType)) {
            transform(data, GType.INT, propValue);
        } else if (GVALUE_API.g_value_type_transformable(GType.DOUBLE, propType)) {
            transform(data, GType.DOUBLE, propValue);
        } else if (GVALUE_API.g_value_type_transformable(GType.FLOAT, propType)) {
            transform(data, GType.FLOAT, propValue);
        } else {
            // Old behaviour
            GOBJECT_API.g_object_set(this, property, data);
            return;
        }
        GOBJECT_API.g_param_value_validate(propertySpec, propValue);
        GOBJECT_API.g_object_set_property(this, property, propValue);
        GVALUE_API.g_value_unset(propValue); // Release any memory
    }

    protected synchronized <T> void addCallback(Class<T> listenerClass, T listener, GCallback cb) {
        final Map<Class<?>, Map<Object, GCallback>> signals = getCallbackMap();
        Map<Object, GCallback> map = signals.get(listenerClass);
        if (map == null) {
            map = new HashMap<Object, GCallback>();
            signals.put(listenerClass, map);
        }
        map.put(listener, cb);
    }

    @Override
    public void dispose() {
        super.dispose();
        STRONG_REFS.remove(this);
    }

    @Override
    public void invalidate() {
        try {
            // Need to increase the ref count before removing the toggle ref, so
            // ensure the native object is not destroyed.
            if (handle.ownsReference()) {
                handle.ref();

                // Disconnect the callback.
                GOBJECT_API.g_object_remove_toggle_ref(handle.getPointer(), TOGGLE_NOTIFY, handle.objectID);
            }
            STRONG_REFS.remove(this);
        } finally {
            super.invalidate();
        }
    }

    protected synchronized <T> void removeCallback(Class<T> listenerClass, T listener) {
        final Map<Class<?>, Map<Object, GCallback>> signals = getCallbackMap();
        Map<Object, GCallback> map = signals.get(listenerClass);
        if (map != null) {
            GCallback cb = map.remove(listener);
            if (cb != null) {
                cb.remove();
            }
            if (map.isEmpty()) {
                signals.remove(listenerClass);
                if (callbackListeners.isEmpty()) {
                    callbackListeners = null;
                }
            }
        }
    }

    //    public static <T extends GObject> T objectFor(Pointer ptr, Class<T> defaultClass) {
//        return objectFor(ptr, defaultClass, true);
//    }
    private GObjectAPI.GParamSpec findProperty(String propertyName) {
        Pointer ptr = GOBJECT_API.g_object_class_find_property(getRawPointer().getPointer(0), propertyName);
        if (ptr == null) {
            return null;
        }
        return new GObjectAPI.GParamSpec(ptr);
    }

    private GObjectAPI.GParamSpecTypeSpecific findProperty(String propertyName, GType type) {
        Pointer ptr = GOBJECT_API.g_object_class_find_property(getRawPointer().getPointer(0), propertyName);
        if (type.equals(GType.INT)) {
            return new GObjectAPI.GParamSpecInt(ptr);
        } else if (type.equals(GType.UINT)) {
            return new GObjectAPI.GParamSpecUInt(ptr);
        } else if (type.equals(GType.CHAR)) {
            return new GObjectAPI.GParamSpecChar(ptr);
        } else if (type.equals(GType.UCHAR)) {
            return new GObjectAPI.GParamSpecUChar(ptr);
        } else if (type.equals(GType.BOOLEAN)) {
            return new GObjectAPI.GParamSpecBoolean(ptr);
        } else if (type.equals(GType.LONG)) {
            return new GObjectAPI.GParamSpecLong(ptr);
        } else if (type.equals(GType.ULONG)) {
            return new GObjectAPI.GParamSpecLong(ptr);
        } else if (type.equals(GType.INT64)) {
            return new GObjectAPI.GParamSpecInt64(ptr);
        } else if (type.equals(GType.UINT64)) {
            return new GObjectAPI.GParamSpecInt64(ptr);
        } else if (type.equals(GType.FLOAT)) {
            return new GObjectAPI.GParamSpecFloat(ptr);
        } else if (type.equals(GType.DOUBLE)) {
            return new GObjectAPI.GParamSpecDouble(ptr);
        } else if (type.equals(GType.STRING)) {
            return new GObjectAPI.GParamSpecString(ptr);
        }
        throw new IllegalArgumentException("Unknown conversion from GType=" + type);
    }

    private synchronized final Map<Class<?>, Map<Object, GCallback>> getCallbackMap() {
        if (callbackListeners == null) {
            callbackListeners = new ConcurrentHashMap<Class<?>, Map<Object, GCallback>>();
        }
        return callbackListeners;
    }

    private GObjectAPI.GParamSpec[] listProperties() {
        IntByReference len = new IntByReference();
        Pointer ptrs = GOBJECT_API.g_object_class_list_properties(getRawPointer().getPointer(0), len);
        if (ptrs == null) {
            return null;
        }

        GParamSpec[] props = new GParamSpec[len.getValue()];
        int offset = 0;
        for (int i = 0; i < len.getValue(); i++) {
            props[i] = new GObjectAPI.GParamSpec(ptrs.getPointer(offset));
            offset += Native.POINTER_SIZE;
        }
        return props;
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

    private static double doubleValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        }
        throw new IllegalArgumentException("Expected double value, not " + value.getClass());
    }

    private static float floatValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else if (value instanceof String) {
            return Float.parseFloat((String) value);
        }
        throw new IllegalArgumentException("Expected float value, not " + value.getClass());
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

    private static boolean setGValue(GValue value, GType type, Object data) {
        if (type.equals(GType.INT)) {
            GVALUE_API.g_value_set_int(value, intValue(data));
        } else if (type.equals(GType.UINT)) {
            GVALUE_API.g_value_set_uint(value, intValue(data));
        } else if (type.equals(GType.CHAR)) {
            GVALUE_API.g_value_set_char(value, (byte) intValue(data));
        } else if (type.equals(GType.UCHAR)) {
            GVALUE_API.g_value_set_uchar(value, (byte) intValue(data));
        } else if (type.equals(GType.LONG)) {
            GVALUE_API.g_value_set_long(value, new NativeLong(longValue(data)));
        } else if (type.equals(GType.ULONG)) {
            GVALUE_API.g_value_set_ulong(value, new NativeLong(longValue(data)));
        } else if (type.equals(GType.INT64)) {
            GVALUE_API.g_value_set_int64(value, longValue(data));
        } else if (type.equals(GType.UINT64)) {
            GVALUE_API.g_value_set_uint64(value, longValue(data));
        } else if (type.equals(GType.BOOLEAN)) {
            GVALUE_API.g_value_set_boolean(value, booleanValue(data));
        } else if (type.equals(GType.FLOAT)) {
            GVALUE_API.g_value_set_float(value, floatValue(data));
        } else if (type.equals(GType.DOUBLE)) {
            GVALUE_API.g_value_set_double(value, doubleValue(data));
        } else {
            return false;
        }
        return true;
    }

    private static GValue transform(GValue src, GType dstType) {
        GValue dst = new GValue();
        GVALUE_API.g_value_init(dst, dstType);
        GVALUE_API.g_value_transform(src, dst);
        return dst;
    }

    private static void transform(Object data, GType type, GValue dst) {
        GValue src = new GValue();
        GVALUE_API.g_value_init(src, type);
        setGValue(src, type, data);
        GVALUE_API.g_value_transform(src, dst);
    }

    protected abstract class GCallback {

        protected final Callback cb;
        protected final NativeLong id;
        volatile boolean connected = false;

        protected GCallback(NativeLong id, Callback cb) {
            this.id = id != null ? id : new NativeLong(0);
            this.cb = cb;
            this.connected = this.id.intValue() != 0;
        }

        void remove() {
            if (connected) {
                disconnect();
                connected = false;
            }
        }

        abstract protected void disconnect();

//        @Override
//        protected final void finalize() {
//            // Ensure the native callback is removed
//            remove();
//        }
    }

    /**
     * Base interface for classes that implement a GInterface
     */
    public static interface GInterface {

        /**
         * Get the GObject implementing this interface.
         *
         * @return implementing GObject
         */
        public GObject getGObject();

    }

    private final class SignalCallback extends GCallback {

        protected SignalCallback(String signal, Callback cb) {
            super(handle.connectSignal(signal, cb), cb);
            if (!connected) {
                throw new IllegalArgumentException(String.format("Failed to connect signal '%s'", signal));
            }
        }

        @Override
        synchronized protected void disconnect() {
            handle.disconnectSignal(id);
        }
    }

    protected static class Handle extends RefCountedObject.Handle {

        private final IntPtr objectID;
        private final Set<NativeLong> signals;

        public Handle(GObjectPtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
            this.objectID = new IntPtr(System.identityHashCode(this));
            signals = new HashSet<>();
        }

        private synchronized NativeLong connectSignal(String signal, Callback cb) {
            NativeLong id = GOBJECT_API.g_signal_connect_data(getPointer(), signal, cb, null, null, 0);
            if (id.longValue() != 0) {
                signals.add(id);
            }
            return id;
        }

        private synchronized void disconnectSignal(NativeLong id) {
            if (signals.remove(id)) {
                GOBJECT_API.g_signal_handler_disconnect(getPointer(), id);
            }
        }

        private synchronized void clearSignals() {
            signals.forEach(id -> GOBJECT_API.g_signal_handler_disconnect(getPointer(), id));
            signals.clear();
        }

        @Override
        public void invalidate() {
            clearSignals();
            super.dispose();
        }

        @Override
        public void dispose() {
            clearSignals();
            super.dispose();
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GOBJECT_API.g_object_remove_toggle_ref((GObjectPtr) ptr, TOGGLE_NOTIFY, objectID);
        }

        @Override
        protected void ref() {
            GOBJECT_API.g_object_ref(getPointer());
        }

        /**
         * Sink floating reference. This will turn a floating reference into a
         * real one.
         */
        protected void sink() {
            GOBJECT_API.g_object_ref_sink(getPointer());
        }

        @Override
        protected void unref() {
            GOBJECT_API.g_object_unref(getPointer());
        }

        @Override
        protected GObjectPtr getPointer() {
            return (GObjectPtr) super.getPointer();
        }

        @Override
        public String toString() {
            GObjectPtr ptr = getPointer();
            if (ptr != null) {
                return ptr.getGType().getTypeName() + " : " + objectID;
            } else {
                return "Disposed handle";
            }
        }

    }

    private static final class ToggleNotify implements GObjectAPI.GToggleNotify {

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
            LOG.log(LIFECYCLE, "toggle_ref " + o.getClass().getSimpleName()
                    + " (" + ptr + ")" + " last_ref=" + is_last_ref);
            if (is_last_ref) {
                STRONG_REFS.remove(o);
            } else {
                STRONG_REFS.put(o, Boolean.TRUE);
            }
        }
    }

}
