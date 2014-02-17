/* 
 * Copyright (c) 2009 Levente Farkas
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

package org.gstreamer.elements;

import static org.gstreamer.lowlevel.GObjectAPI.GOBJECT_API;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.gstreamer.Buffer;
import org.gstreamer.Caps;
import org.gstreamer.Event;
import org.gstreamer.FlowReturn;
import org.gstreamer.PadDirection;
import org.gstreamer.PadTemplate;
import org.gstreamer.lowlevel.BaseSrcAPI;
import org.gstreamer.lowlevel.GType;
import org.gstreamer.lowlevel.GstPadTemplateAPI;
import org.gstreamer.lowlevel.GObjectAPI.GBaseInitFunc;
import org.gstreamer.lowlevel.GObjectAPI.GClassInitFunc;
import org.gstreamer.lowlevel.GObjectAPI.GTypeInfo;
import org.gstreamer.lowlevel.GstAPI.GstSegmentStruct;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;

abstract public class CustomSrc extends BaseSrc {
    private final static Logger logger = Logger.getLogger(CustomSrc.class.getName());
    
    private static final Map<Class<? extends CustomSrc>, CustomSrcInfo>  customSubclasses 
    	= new ConcurrentHashMap<Class<? extends CustomSrc>, CustomSrcInfo>();

    @SuppressWarnings("unused")
    private static class CustomSrcInfo {
        GType type;
        PadTemplate template;
        Caps caps;
        
        // Per-class callbacks used by gstreamer to initialize the subclass
        GClassInitFunc classInit;
        GBaseInitFunc baseInit;
        
        // Per-instance callback functions - names must match GstBaseSrcClass
        BaseSrcAPI.Create create;
        BaseSrcAPI.Seek seek;
        BooleanFunc1 is_seekable;
        BooleanFunc1 start;
        BooleanFunc1 stop;
        BooleanFunc1 negotiate;
        BaseSrcAPI.GetCaps get_caps;
        BaseSrcAPI.SetCaps set_caps;
        BaseSrcAPI.GetSize get_size;
        BaseSrcAPI.GetTimes get_times;
        BaseSrcAPI.Fixate fixate;
        BaseSrcAPI.EventNotify event;
    }
    protected CustomSrc(Class<? extends CustomSrc> subClass, String name) {
        super(initializer(GOBJECT_API.g_object_new(getSubclassType(subClass), "name", name)));
    }
    private static CustomSrcInfo getSubclassInfo(Class<? extends CustomSrc> subClass) {
       synchronized (subClass) {
            CustomSrcInfo info = customSubclasses.get(subClass);
            if (info == null) {
                init(subClass);
                info = customSubclasses.get(subClass);
            }
            return info;
        } 
    }
    private static GType getSubclassType(Class<? extends CustomSrc> subClass) {
        return getSubclassInfo(subClass).type;
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    protected @interface SrcCallback {
        public String value();
    }
    
    /**
     * Used when more control of Buffer creation is desired than fillBuffer() affords.
     * 
     * @param offset 
     * @param size
     * @param bufRef
     */
    @SrcCallback("create")
    protected FlowReturn srcCreateBuffer(long offset, int size, Buffer[] bufRef) throws IOException {
//        System.out.println("CustomSrc.createBuffer");
        return FlowReturn.NOT_SUPPORTED;
    } 
    
    /**
     * Used when you just want to fill a Buffer with data.  The Buffer
     * will be allocated and initialized by gstreamer.
     * @param offset
     * @param size
     * @param buffer
     */
    @SrcCallback("create")
    protected FlowReturn srcFillBuffer(long offset, int size, Buffer buffer) throws IOException {
        logger.info("CustomSrc.srcFillBuffer");
        return FlowReturn.NOT_SUPPORTED;
    }
    @SrcCallback("is_seekable")
    protected boolean srcIsSeekable() {
        logger.info("CustomSrc.srcIsSeekable");
        return false;
    }
    @SrcCallback("seek")
    protected boolean srcSeek(GstSegmentStruct segment) throws IOException {
        logger.info("CustomSrc.srcSeek");
        return false;
    }
    @SrcCallback("start")
    protected boolean srcStart() { 
        logger.info("CustomSrc.srcStart");
        return true; 
    }
    
    @SrcCallback("stop")
    protected boolean srcStop() { 
        logger.info("CustomSrc.srcStop");
        return true; 
    }
    
    @SrcCallback("negotiate")
    protected boolean srcNegotiate() { 
        logger.info("CustomSrc.srcNegotiate");
        return false; 
    }
    
    @SrcCallback("get_caps")
    protected Caps srcGetCaps() { 
        logger.info("CustomSrc.srcGetCaps");
        return null; 
    }
    
    @SrcCallback("set_caps")
    protected boolean srcSetCaps(Caps caps) { 
        logger.info("CustomSrc.srcSetCaps");
        return false; 
    }
    
    @SrcCallback("get_size")
    protected long srcGetSize() { 
        logger.info("CustomSrc.srcGetSize");
        return -1; 
    }
    
    @SrcCallback("event")
    protected boolean srcEvent(Event ev) { 
        logger.info("CustomSrc.srcEvent");
        return true; 
    }
    @SrcCallback("get_times")
    protected void srcGetTimes(Buffer buffer, long[] start, long[] end) { 
        logger.info("CustomSrc.srcGetTimes");
    }
    
    @SrcCallback("fixate")
    protected void srcFixate(Caps caps) { 
        logger.info("CustomSrc.srcFixate");
    }
    
    private static final BaseSrcAPI.Create fillBufferCallback = new BaseSrcAPI.Create() {

        public FlowReturn callback(BaseSrc element, long offset, int size, Pointer bufRef) {                  
            try {   
            	throw new NotImplementedException();
//                Buffer buffer = new Buffer(size);
//                //System.out.println("Sending buf=" + buf);
//                FlowReturn retVal = ((CustomSrc) element).srcFillBuffer(offset, size, buffer);
//                bufRef.setPointer(0, buffer.getAddress());
//                buffer.disown();

//                return retVal;
            } catch (Exception ex) {
                return FlowReturn.UNEXPECTED;
            }                    
        }
        
    };
    private static final BaseSrcAPI.Create createBufferCallback = new BaseSrcAPI.Create() {

        public FlowReturn callback(BaseSrc element, long offset, int size, Pointer bufRef) {                  
            try {
            	throw new NotImplementedException();
//                Buffer[] buffers = new Buffer[1];
//                FlowReturn retVal = ((CustomSrc) element).srcCreateBuffer(offset, size, buffers);
//                if (buffers[0] != null) {
//                    Buffer buffer = buffers[0];
//                    bufRef.setPointer(0, buffer.getAddress());
//                    buffer.disown();
//                }                
//                return retVal;
            } catch (Exception ex) {
                return FlowReturn.UNEXPECTED;
            }                    
        }
        
    };
    private static class BooleanFunc1 implements BaseSrcAPI.BooleanFunc1 {
        private Method method;
        public BooleanFunc1(String methodName) {
            try {
                method = CustomSrc.class.getDeclaredMethod(methodName, new Class[0]);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        public boolean callback(BaseSrc element) {
            try {
                return ((Boolean) method.invoke(element)).booleanValue();
            } catch (Exception ex) {
                return false;
            }
        }
        
    }
    private static final BooleanFunc1 isSeekableCallback = new BooleanFunc1("srcIsSeekable");
    private static final BooleanFunc1 startCallback = new BooleanFunc1("srcStart");
    private static final BooleanFunc1 stopCallback = new BooleanFunc1("srcStop");
    private static final BooleanFunc1 negotiateCallback = new BooleanFunc1("srcNegotiate");
    private static final BaseSrcAPI.Seek seekCallback = new BaseSrcAPI.Seek() {
       
        public boolean callback(BaseSrc element, GstSegmentStruct segment) {
            try {
                return ((CustomSrc) element).srcSeek(segment);
            } catch (Exception ex) {
                return false;
            }
        }        
    };
    private static final BaseSrcAPI.SetCaps setCapsCallback = new BaseSrcAPI.SetCaps() {

        public boolean callback(BaseSrc element, Caps caps) {
            try {
                return ((CustomSrc) element).srcSetCaps(caps);
            } catch (Exception ex) {
                return false;
            }
        }
    };
    private static final BaseSrcAPI.GetCaps getCapsCallback = new BaseSrcAPI.GetCaps() {

        public Caps callback(BaseSrc element) {
            try {
                Caps caps = ((CustomSrc) element).srcGetCaps().copy();
                caps.disown();
                return caps;
            } catch (Exception ex) {
                Caps caps = Caps.emptyCaps();
                caps.disown();
                return caps;
            }
        }
    };
    private static final BaseSrcAPI.GetTimes getTimesCallback = new BaseSrcAPI.GetTimes() {

        public void callback(BaseSrc src, Buffer buffer, Pointer startRef, Pointer endRef) {
            try {
                long[] start = {-1}, end = {-1};
                ((CustomSrc) src).srcGetTimes(buffer, start, end);
                startRef.setLong(0, start[0]);
                endRef.setLong(0, end[0]);
            } catch (Exception ex) { }
        }
    };
    private static final BaseSrcAPI.Fixate fixateCallback = new BaseSrcAPI.Fixate() {

        public void callback(BaseSrc src, Caps caps) {
            try {
                ((CustomSrc) src).srcFixate(caps);
            } catch (Exception ex) {}
        }
    };
    private static final BaseSrcAPI.EventNotify eventCallback = new BaseSrcAPI.EventNotify() {

        public boolean callback(BaseSrc src, Event ev) {
            try {
                return ((CustomSrc) src).srcEvent(ev);
            } catch (Exception ex) {
                return false;
            }
        }
    };
    private static final BaseSrcAPI.GetSize getSizeCallback = new BaseSrcAPI.GetSize() {

        public boolean callback(BaseSrc element, LongByReference sizeRef) {
            try {
                long size = ((CustomSrc) element).srcGetSize();
                if (size < 0) {
                    return false;
                }
                sizeRef.setValue(size);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    };
    
    /**
     * Checks if a method over-rides another method.
     * @param m1
     * @param m2
     * @return
     */
    private static final boolean isOverridingMethod(Method m1, Method m2) {
        return m1.getDeclaringClass().isAssignableFrom(m2.getDeclaringClass())
                && m1.getDeclaringClass().isAssignableFrom(m2.getDeclaringClass())
                && m1.getName().equals(m2.getName()) 
                && m1.getReturnType().equals(m2.getReturnType())
                && (m1.getParameterTypes() != null 
                    && m2.getParameterTypes() != null 
                    && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes()));
    }
    
    /**
     * Finds a method in the Class or any of its parent classes.
     * 
     * @param cls
     * @param method
     * @return The method or null if not found.
     */
    private static final Method findOverridingMethod(final Class<?> cls, final Method method) {
        for (Class<?> next = cls; next != null; next = next.getSuperclass()) {
            for (Method m : next.getDeclaredMethods()) {
                if (isOverridingMethod(method, m)) {
                    return m;
                }
            }
        }
        return null;
    }
    private static void init(Class<? extends CustomSrc> srcClass) {
        final CustomSrcInfo info = new CustomSrcInfo();
        customSubclasses.put(srcClass, info);
        
        //
        // Trawl through all the methods in the subclass, looking for ones that 
        // over-ride the ones in CustomSrc
        //
        for (Method m : CustomSrc.class.getDeclaredMethods()) {
            SrcCallback cb = m.getAnnotation(SrcCallback.class);
            if (cb == null) {
                continue;
            }
            Method srcMethod = findOverridingMethod(srcClass, m);
            if (srcMethod == null) {
                continue;
            }
            if (srcMethod.equals(m)) {
                // Skip it if it is the same as the method in CustomSrc
//                System.out.println(srcMethod + " is the same as " + m);
                continue;
            }
//            System.out.println("Setting callback for " + m.getName());
            if (m.getName().equals("srcSeek")) {
                info.seek = seekCallback;
            } else if (m.getName().equals("srcIsSeekable")) {
                info.is_seekable = isSeekableCallback;                            
            } else if (m.getName().equals("srcFillBuffer")) {
                info.create = fillBufferCallback;
            } else if (m.getName().equals("srcCreateBuffer")) {
                info.create = createBufferCallback;
            } else if (m.getName().equals("srcStart")) {
                info.start = startCallback;
            } else if (m.getName().equals("srcStop")) {
                info.stop = stopCallback;
            } else if (m.getName().equals("srcNegotiate")) {
                info.negotiate = negotiateCallback;
            } else if (m.getName().equals("srcSetCaps")) {
                info.set_caps = setCapsCallback;
            } else if (m.getName().equals("srcGetCaps")) {
                info.get_caps = getCapsCallback;
            } else if (m.getName().equals("srcGetSize")) {
                info.get_size = getSizeCallback;
            } else if (m.getName().equals("srcGetTimes")) {
                info.get_times = getTimesCallback;
            } else if (m.getName().equals("srcFixate")) {
                info.fixate = fixateCallback;
            } else if (m.getName().equals("srcEvent")) {
                info.event = eventCallback;
            }
        }
        info.classInit = new GClassInitFunc() {
            public void callback(Pointer g_class, Pointer class_data) {
                BaseSrcAPI.GstBaseSrcClass base = new BaseSrcAPI.GstBaseSrcClass(g_class);
                // Copy all the callback fields over
                for (Field f : base.getClass().getDeclaredFields()) {
                    try {
                        Field infoField = info.getClass().getDeclaredField(f.getName());
                        if (f.getType().isAssignableFrom(infoField.getType())) {
                            f.set(base, infoField.get(info));
//                            System.out.println("Copied field " + f.getName());
                        }
                    } catch (Exception ex) {}
                }
                base.write();
            }
        };
        info.baseInit = new GBaseInitFunc() {

            public void callback(Pointer g_class) {
                info.caps = Caps.anyCaps();
                info.template = new PadTemplate("src", PadDirection.SRC, info.caps);
                GstPadTemplateAPI.GSTPADTEMPLATE_API.gst_element_class_add_pad_template(g_class, info.template);
            }
        };
        
        //
        // gstreamer boilerplate to hook the plugin in
        //
        GTypeInfo ginfo = new GTypeInfo();
        ginfo.class_init = info.classInit;
        ginfo.base_init = info.baseInit;
        ginfo.instance_init = null;
        ginfo.class_size = (short)new BaseSrcAPI.GstBaseSrcClass().size();
        ginfo.instance_size = (short)new BaseSrcAPI.GstBaseSrcStruct().size();
        
        GType type = GOBJECT_API.g_type_register_static(BaseSrcAPI.BASESRC_API.gst_base_src_get_type(), 
                srcClass.getSimpleName(), ginfo, 0);
        info.type = type;
    }
}
