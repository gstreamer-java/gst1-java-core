/* 
 * Copyright (c) 2019 Neil C Smith
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import org.freedesktop.gstreamer.glib.GQuark;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;
import org.freedesktop.gstreamer.lowlevel.annotations.ConstField;
import org.freedesktop.gstreamer.lowlevel.annotations.FreeReturnValue;
import org.freedesktop.gstreamer.lowlevel.annotations.IncRef;
import org.freedesktop.gstreamer.lowlevel.annotations.Invalidate;

import com.sun.jna.CallbackParameterContext;
import com.sun.jna.FromNativeContext;
import com.sun.jna.FromNativeConverter;
import com.sun.jna.MethodParameterContext;
import com.sun.jna.MethodResultContext;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.StructureReadContext;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeConverter;
import org.freedesktop.gstreamer.glib.GObject;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.glib.RefCountedObject;

/**
 *
 * @author wayne
 */
public class GTypeMapper extends com.sun.jna.DefaultTypeMapper {

    public GTypeMapper() {
        addToNativeConverter(URI.class, uriConverter);
    }
    private static ToNativeConverter interfaceConverter = new ToNativeConverter() {

        public Object toNative(Object arg, ToNativeContext context) {
            return arg != null ? Natives.getRawPointer(((GObject.GInterface) arg).getGObject()) : null;
        }

        public Class<?> nativeType() {
            return Void.class; // not really correct, but not used in this instance
        }        
    };
    
    private static TypeConverter nativeObjectConverter = new TypeConverter() {
        public Object toNative(Object arg, ToNativeContext context) {
            if (arg == null) {
                return null;
            }
            Pointer ptr = Natives.getRawPointer((NativeObject) arg);
            
            //
            // Deal with any adjustments to the proxy neccessitated by gstreamer
            // breaking their reference-counting idiom with special cases
            //
            if (context instanceof MethodParameterContext) {
                MethodParameterContext mcontext = (MethodParameterContext) context;
                Method method = mcontext.getMethod();
                int index = mcontext.getParameterIndex();
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                if (index < parameterAnnotations.length) {
                    Annotation[] annotations = parameterAnnotations[index];
                    for (int i = 0; i < annotations.length; ++i) {
                        if (annotations[i] instanceof Invalidate) {
                            ((NativeObject) arg).invalidate();
                            break;
                        } else if (annotations[i] instanceof IncRef) {
                            Natives.ref((RefCountedObject) arg);
                        }
                    }
                }
            }
            return ptr;
        }
 
        @SuppressWarnings(value = "unchecked")
        public Object fromNative(Object result, FromNativeContext context) {
            if (result == null) {
                return null;
            }
            if (context instanceof MethodResultContext) {
                //
                // By default, gstreamer increments the refcount on objects 
                // returned from functions, so drop a ref here
                //
                boolean ownsHandle = ((MethodResultContext) context).getMethod().isAnnotationPresent(CallerOwnsReturn.class);
//                int refadj = ownsHandle ? -1 : 0;
//                return NativeObject.objectFor((Pointer) result, (Class<? extends NativeObject>) context.getTargetType(), refadj, ownsHandle);
                if (ownsHandle) {
                    return Natives.callerOwnsReturn((Pointer) result, (Class<? extends NativeObject>) context.getTargetType());
                } else {
                    return Natives.objectFor((Pointer) result, (Class<? extends NativeObject>) context.getTargetType(), false, false);
                }
            }
            if (context instanceof CallbackParameterContext) {
//                return NativeObject.objectFor((Pointer) result, (Class<? extends NativeObject>) context.getTargetType(), 1, true);
                return Natives.objectFor((Pointer) result, (Class<? extends NativeObject>) context.getTargetType(), true, true);
            }
            if (context instanceof StructureReadContext) {
                StructureReadContext sctx = (StructureReadContext) context;
                boolean ownsHandle = sctx.getField().getAnnotation(ConstField.class) == null;
//                return NativeObject.objectFor((Pointer) result, (Class<? extends NativeObject>) context.getTargetType(), 1, ownsHandle);
                return Natives.objectFor((Pointer) result, (Class<? extends NativeObject>) context.getTargetType(), true, true);
            }
            throw new IllegalStateException("Cannot convert to NativeObject from " + context);
        }
        
        public Class<?> nativeType() {
            return Pointer.class;
        }
    };
    private static TypeConverter enumConverter = new TypeConverter() {

        @SuppressWarnings(value = "unchecked")
        public Object fromNative(Object value, FromNativeContext context) {
            return EnumMapper.getInstance().valueOf((Integer) value, (Class<? extends Enum>) context.getTargetType());
        }

        public Class<?> nativeType() {
            return Integer.class;
        }

        @SuppressWarnings("rawtypes")
        public Object toNative(Object arg, ToNativeContext context) {
            if (arg == null) {
                return null;
            }
            return EnumMapper.getInstance().intValue((Enum) arg);
        }
    };

    private TypeConverter stringConverter = new TypeConverter() {

        public Object fromNative(Object result, FromNativeContext context) {
            if (result == null) {
                return null;
            }
            if (context instanceof MethodResultContext) {
                MethodResultContext functionContext = (MethodResultContext) context;
                Method method = functionContext.getMethod();
                Pointer ptr = (Pointer) result;
                String s = ptr.getString(0);
                if (method.isAnnotationPresent(FreeReturnValue.class)
                    || method.isAnnotationPresent(CallerOwnsReturn.class)) {
                    GlibAPI.GLIB_API.g_free(ptr);
                }
                return s;
            } else {
                return ((Pointer) result).getString(0);
            }           
        }

        public Class<?> nativeType() {
            return Pointer.class;
        }

        public Object toNative(Object arg, ToNativeContext context) {
            // Let the default String -> native conversion handle it
            return arg;            
        }
    };

    private TypeConverter booleanConverter = new TypeConverter() {
        public Object toNative(Object arg, ToNativeContext context) {
            return Integer.valueOf(Boolean.TRUE.equals(arg) ? 1 : 0);
        }

        public Object fromNative(Object arg0, FromNativeContext arg1) {
            return Boolean.valueOf(((Integer)arg0).intValue() != 0);
        }

        public Class<?> nativeType() {
            return Integer.class;
        }
    };
    private TypeConverter gquarkConverter = new TypeConverter() {

        public Object fromNative(Object arg0, FromNativeContext arg1) {
            return new GQuark((Integer) arg0);
        }

        public Class<?> nativeType() {
            return Integer.class;
        }

        public Object toNative(Object arg0, ToNativeContext arg1) {
            return ((GQuark) arg0).intValue();
        }
    };
    
    private TypeConverter intptrConverter = new TypeConverter() {
        
        public Object toNative(Object arg, ToNativeContext context) {
            return ((IntPtr)arg).value;            
        }

        public Object fromNative(Object arg0, FromNativeContext arg1) {
            return new IntPtr(((Number) arg0).intValue());            
        }

        public Class<?> nativeType() {
            return Native.POINTER_SIZE == 8 ? Long.class : Integer.class;
        }
    };
//    private TypeConverter querytypeConverter = new TypeConverter() {
//        
//        public Object toNative(Object arg, ToNativeContext context) {
//            return ((QueryType)arg).intValue();
//        }
//
//        public Object fromNative(Object arg0, FromNativeContext arg1) {
//            return QueryType.valueOf(((Number) arg0).intValue());            
//        }
//
//        public Class<?> nativeType() {
//            return Integer.class;
//        }
//    };
    private static ToNativeConverter uriConverter = new ToNativeConverter() {

        public Object toNative(Object arg0, ToNativeContext arg1) {
            URI uri = (URI) arg0;
            String uriString = uri.toString();
            // Need to fixup file:/ to be file:/// for gstreamer
            if ("file".equals(uri.getScheme()) && uri.getHost() == null) {
                final String path = uri.getRawPath();
                if (com.sun.jna.Platform.isWindows()) {
                    uriString = "file:/" + path;
                } else {
                    uriString = "file://" + path;
                }
            }
            return uriString;
        }

        public Class<?> nativeType() {
            return String.class;
        }
    };
    @SuppressWarnings("rawtypes")
	public FromNativeConverter getFromNativeConverter(Class type) {
        if (Enum.class.isAssignableFrom(type)) {
            return enumConverter;              
        } else if (NativeObject.class.isAssignableFrom(type)) {
            return nativeObjectConverter;
        } else if (Boolean.class == type || boolean.class == type) {
            return booleanConverter;
        } else if (String.class == type) {
            return stringConverter;
        } else if (IntPtr.class == type) {
            return intptrConverter;
        } else if (GQuark.class == type) {
            return gquarkConverter;
        }
        return super.getFromNativeConverter(type);
    }

    @SuppressWarnings("rawtypes")
	public ToNativeConverter getToNativeConverter(Class type) {
        if (NativeObject.class.isAssignableFrom(type)) {
            return nativeObjectConverter;
        } else if (GObject.GInterface.class.isAssignableFrom(type)) {
            return interfaceConverter;
        } else if (Enum.class.isAssignableFrom(type)) {
            return enumConverter;
        } else if (Boolean.class == type || boolean.class == type) {
            return booleanConverter;
        } else if (String.class == type) {
            return stringConverter;        
        } else if (IntPtr.class == type) {
            return intptrConverter;
        } else if (GQuark.class == type) {
            return gquarkConverter;
        }
        return super.getToNativeConverter(type);
    }
}
