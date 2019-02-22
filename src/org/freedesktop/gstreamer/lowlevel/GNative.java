/*
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;

/**
 *
 */
public final class GNative {
    // gstreamer on win32 names the dll files one of foo.dll, libfoo.dll and libfoo-0.dll
    // private static String[] windowsNameFormats = { "%s", "lib%s", "lib%s-0" };
            
    private final static String[] nameFormats;
    
    static {
        String defFormats = "%s";
        if (Platform.isWindows()) {
            defFormats = "%s|lib%s|lib%s-0";
        } else if (Platform.isMac()) {
            defFormats = "%s.0|%s";
        }
        nameFormats = System.getProperty("gstreamer.GNative.nameFormats", defFormats).split("\\|");
    }

    private GNative() {}

    public static synchronized <T extends Library> T loadLibrary(String name, Class<T> interfaceClass, Map<String, ?> options) {
        for (String format : nameFormats)
            try {
                return loadNativeLibrary(String.format(format, name), interfaceClass, options);
            } catch (UnsatisfiedLinkError ex) {
                continue;
            }
        throw new UnsatisfiedLinkError("Could not load library: " + name);
    }

    private static <T extends Library> T loadNativeLibrary(String name, Class<T> interfaceClass, Map<String, ?> options) {
        T library = interfaceClass.cast(Native.loadLibrary(name, interfaceClass, options));
        boolean needCustom = false;
    search:
        for (Method m : interfaceClass.getMethods())
            for (Class<?> cls : m.getParameterTypes())
                if (cls.isArray() && getConverter(cls.getComponentType()) != null) {
                    needCustom = true;
                    break search;
                }
        if (!needCustom)
            return library;
//        System.out.println("Using custom library proxy for " + interfaceClass.getName());
        return interfaceClass.cast(
        		Proxy.newProxyInstance(interfaceClass.getClassLoader(),
        				new Class[]{ interfaceClass }, 
        				new Handler<T>(library, options)));
    }

    public static synchronized NativeLibrary getNativeLibrary(String name) {
        for (String format : nameFormats)
            try {
                return NativeLibrary.getInstance(String.format(format, name));
            } catch (UnsatisfiedLinkError ex) {
                continue;
            }
        throw new UnsatisfiedLinkError("Could not load library: " + name);
    }

    private static interface Converter {
        Class<?> nativeType();
        Object toNative(Object value);
        Object fromNative(Object value, Class<?> javaType);
    }

    private static final Converter enumConverter = new Converter() {
        public Class<?> nativeType() {
            return int.class;
        }

        public Object toNative(Object value) {
            return value != null ? EnumMapper.getInstance().intValue((Enum<?>) value) : 0;
        }

        @SuppressWarnings({"unchecked","rawtypes"})
        public Object fromNative(Object value, Class javaType) {
            return EnumMapper.getInstance().valueOf((Integer) value, javaType);
        }
    };

    private static final Converter booleanConverter = new Converter() {
        public Class<?> nativeType() {
            return int.class;
        }

        public Object toNative(Object value) {
            return value != null ? Boolean.TRUE.equals(value) ? 1 : 0 : 0;
        }
        
        @SuppressWarnings("rawtypes")
        public Object fromNative(Object value, Class javaType) {
            return value != null ? ((Integer) value).intValue() != 0 : 0;
        }
    };


    private static Converter getConverter(Class<?> javaType) {
        if (Enum.class.isAssignableFrom(javaType))
            return enumConverter;
        else if (boolean.class == javaType || Boolean.class == javaType)
            return booleanConverter;
        return null;
    }

    private static class Handler<T> implements InvocationHandler {
        private final InvocationHandler proxy;
        @SuppressWarnings("unused") // Keep a reference to stop underlying Library being GC'd
        private final T library;
        
        public Handler(T library, Map<String, ?> options) {
            this.library = library;
            this.proxy = Proxy.getInvocationHandler(library);
        }
        
        @SuppressWarnings("null")
        public Object invoke(Object self, Method method, Object[] args) throws Throwable {
            int lastArg = args != null ? args.length : 0;
            if (method.isVarArgs())
                --lastArg;
            Runnable[] postInvoke = null;
            int postCount = 0;
            for (int i = 0; i < lastArg; ++i) {
                if (args[i] == null)
                    continue;
                final Class<?> cls = args[i].getClass();
                if (!cls.isArray() || cls.getComponentType().isPrimitive() || cls.getComponentType() == String.class)
                    continue;
                final Converter converter = getConverter(cls.getComponentType());
                if (converter != null) {
                    final Object[] src = (Object[]) args[i];
                    final Object dst = java.lang.reflect.Array.newInstance(converter.nativeType(), src.length);
                    final ArrayIO io = getArrayIO(converter.nativeType());
                    for (int a = 0; a < src.length; ++a)
                        io.set(dst, a, converter.toNative(src[a]));
                    if (postInvoke == null)
                        postInvoke = new Runnable[lastArg];
                    postInvoke[postCount++] = new Runnable() {
                        public void run() {
                            for (int a = 0; a < src.length; ++a)
                                src[a] = converter.fromNative(io.get(dst, a), cls.getComponentType());
                        }
                    };
                    args[i] = dst;
                }
            }
            Object retval = proxy.invoke(self, method, args);
            //
            // Reload any native arrays into java arrays
            //
            for (int i = 0; i < postCount; ++i)
                postInvoke[i].run();
            return retval;
        }
        
        @SuppressWarnings("unused")
        Class<?> getNativeClass(Class<?> cls) {
            if (cls == Integer.class)
                return int.class;
            else if (cls == Long.class)
                return long.class;
            return cls;
        }

        private static interface ArrayIO {
            public void set(Object array, int index, Object data);
            public Object get(Object array, int index);
        }

        private static final ArrayIO intArrayIO = new ArrayIO() {
            public void set(Object array, int index, Object data) {
                java.lang.reflect.Array.setInt(array, index, data != null ? (Integer) data : 0);
            }
            public Object get(Object array, int index) {
                return java.lang.reflect.Array.getInt(array, index);
            }
        };

        private static final ArrayIO longArrayIO = new ArrayIO() {
            public void set(Object array, int index, Object data) {
                java.lang.reflect.Array.setLong(array, index, data != null ? (Long) data : 0);
            }
            public Object get(Object array, int index) {
                return java.lang.reflect.Array.getLong(array, index);
            }
        };

        private static ArrayIO getArrayIO(final Class<?> cls) {
            if (cls == int.class || cls == Integer.class)
                return intArrayIO;
            else if (cls == long.class || cls == Long.class)
                return longArrayIO;
            throw new IllegalArgumentException("No such conversion");
        }
    }
}
