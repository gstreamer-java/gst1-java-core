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
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.gstreamer.Buffer;
import org.gstreamer.Caps;
import org.gstreamer.FlowReturn;
import org.gstreamer.PadDirection;
import org.gstreamer.PadTemplate;
import org.gstreamer.lowlevel.BaseSinkAPI;
import org.gstreamer.lowlevel.GType;
import org.gstreamer.lowlevel.GstPadTemplateAPI;
import org.gstreamer.lowlevel.GObjectAPI.GBaseInitFunc;
import org.gstreamer.lowlevel.GObjectAPI.GClassInitFunc;
import org.gstreamer.lowlevel.GObjectAPI.GTypeInfo;

import com.sun.jna.Pointer;

/**
 *
 * @author wayne
 */
abstract public class CustomSink extends BaseSink {
    private final static Logger logger = Logger.getLogger(CustomSink.class.getName());
    
    @SuppressWarnings("unused")
    private static class CustomSinkInfo {
        GType type;
        PadTemplate template;
        Caps caps;
        
        // Per-class callbacks used by gstreamer to initialize the subclass
        GClassInitFunc classInit;
        GBaseInitFunc baseInit;
        
        // Per-instance callback functions
        BaseSinkAPI.Render render;
        BaseSinkAPI.Render preroll;
        BaseSinkAPI.BooleanFunc1 start;
        BaseSinkAPI.BooleanFunc1 stop;
        BaseSinkAPI.GetCaps getCaps;
        BaseSinkAPI.SetCaps setCaps;
    }
    private static final Map<Class<? extends CustomSink>, CustomSinkInfo>  customSubclasses = new ConcurrentHashMap<Class<? extends CustomSink>, CustomSinkInfo>();
    protected CustomSink(Class<? extends CustomSink> subClass, String name) {
        super(initializer(GOBJECT_API.g_object_new(getSubclassType(subClass), "name", name)));
    }
    private static CustomSinkInfo getSubclassInfo(Class<? extends CustomSink> subClass) {
       synchronized (subClass) {
            CustomSinkInfo info = customSubclasses.get(subClass);
            if (info == null) {
                init(subClass);
                info = customSubclasses.get(subClass);
            }
            return info;
        } 
    }
    private static GType getSubclassType(Class<? extends CustomSink> subClass) {
        return getSubclassInfo(subClass).type;
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    private @interface SinkCallback {
    }
    @SinkCallback
    protected FlowReturn sinkRender(Buffer buffer) throws IOException {
        logger.info(getClass().getSimpleName() + ".sinkRender");
        return FlowReturn.WRONG_STATE;
    }
    @SinkCallback
    protected FlowReturn sinkPreRoll(Buffer buffer) throws IOException {
        logger.info(getClass().getSimpleName() + ".sinkPreRoll");
        return FlowReturn.WRONG_STATE;
    }
    @SinkCallback
    protected boolean sinkStart() { 
        logger.info(getClass().getSimpleName() + ".sinkStart");
        return false; 
    }
    
    @SinkCallback
    protected boolean sinkStop() { 
        logger.info(getClass().getSimpleName() + ".sinkStop");
        return false; 
    }
    @SinkCallback
    protected Caps sinkGetCaps() { 
        logger.info(getClass().getSimpleName() + ".sinkGetCaps");
        return null; 
    }
    
    @SinkCallback
    protected boolean sinkSetCaps(Caps caps) { 
        logger.info(getClass().getSimpleName() + ".sinkSetCaps");
        return false; 
    }
    private static class BooleanFunc1 implements BaseSinkAPI.BooleanFunc1 {
        private Method method;
        public BooleanFunc1(String methodName) {
            try {
                method = CustomSink.class.getDeclaredMethod(methodName, new Class[0]);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        public boolean callback(BaseSink element) {
            try {
                return ((Boolean) method.invoke(element)).booleanValue();
            } catch (Throwable ex) {
                return false;
            }
        }
        
    }
    private static final BooleanFunc1 startCallback = new BooleanFunc1("sinkStart");
    private static final BooleanFunc1 stopCallback = new BooleanFunc1("sinkStop");
    private static final BaseSinkAPI.Render renderCallback = new BaseSinkAPI.Render() {
        
        public FlowReturn callback(BaseSink sink, Buffer buffer) {
            try {
                return ((CustomSink) sink).sinkRender(buffer);
            } catch (Throwable ex) {
                ex.printStackTrace();
                return FlowReturn.ERROR;
            }
        }
    };
    private static final BaseSinkAPI.Render prerollCallback = new BaseSinkAPI.Render() {
        
        public FlowReturn callback(BaseSink sink, Buffer buffer) {
            try {
                return ((CustomSink) sink).sinkPreRoll(buffer);
            } catch (Throwable ex) {
                return FlowReturn.ERROR;
            }
        }
    };
    private static final BaseSinkAPI.GetCaps getCapsCallback = new BaseSinkAPI.GetCaps() {

        public Caps callback(BaseSink element) {
            try {
                return ((CustomSink) element).sinkGetCaps();
            } catch (Throwable ex) {
                return null;
            }
        }
    };
    private static final BaseSinkAPI.SetCaps setCapsCallback = new BaseSinkAPI.SetCaps() {

        public boolean callback(BaseSink element, Caps caps) {
            try {
                return ((CustomSink) element).sinkSetCaps(caps);
            } catch (Throwable ex) {
                return false;
            }
        }
    };
    private static void init(Class<? extends CustomSink> sinkClass) {
        final CustomSinkInfo info = new CustomSinkInfo();
        customSubclasses.put(sinkClass, info);
        
        //
        // Trawl through all the methods in the subclass, looking for ones that 
        // over-ride the ones in CustomSink
        //
        for (Method m : CustomSink.class.getDeclaredMethods()) {
            SinkCallback cb = m.getAnnotation(SinkCallback.class);
            if (cb == null) {
                continue;
            }
            try {
                Method sinkMethod = sinkClass.getDeclaredMethod(m.getName(), m.getParameterTypes());
                if (sinkMethod.equals(m)) {
                    // Skip it if it is the same as the method in CustomSink
                    continue;
                }
                String name = m.getName().toLowerCase().substring("sink".length());
                if (name.equals("render")) {
                    info.render = renderCallback;
                } else if (name.equals("preroll")) {
                    info.preroll = prerollCallback;
                } else if (name.equals("start")) {
                    info.start = startCallback;
                } else if (name.equals("stop")) {
                    info.stop = stopCallback;
                } else if (name.equals("getcaps")) {
                    info.getCaps = getCapsCallback;
                } else if (name.equals("setcaps")) {
                    info.setCaps = setCapsCallback;
                } 
            } catch (NoSuchMethodException ex) { 
//            } catch (NoSuchFieldException ex) {
//            } catch (IllegalAccessException ex) {                
            }
            
            
        }
        info.classInit = new GClassInitFunc() {
            public void callback(Pointer g_class, Pointer class_data) {
                BaseSinkAPI.GstBaseSinkClass base = new BaseSinkAPI.GstBaseSinkClass(g_class);
                base.render = info.render;
                base.preroll = info.preroll;
                base.start = info.start;
                base.stop = info.stop;
                base.write();            
            }
        };
        info.baseInit = new GBaseInitFunc() {

            public void callback(Pointer g_class) {
                info.caps = Caps.anyCaps();
                info.template = new PadTemplate("sink", PadDirection.SINK, info.caps);
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
        ginfo.class_size = (short)new BaseSinkAPI.GstBaseSinkClass().size();
        ginfo.instance_size = (short)new BaseSinkAPI.GstBaseSinkStruct().size();
        
        info.type = GOBJECT_API.g_type_register_static(BaseSinkAPI.BASESINK_API.gst_base_sink_get_type(), 
                sinkClass.getSimpleName(), ginfo, 0);
    }
}
