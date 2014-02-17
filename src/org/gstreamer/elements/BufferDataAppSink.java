/*
 * Copyright (c) 2011 Andres Colubri
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

import java.nio.ByteOrder;

import org.gstreamer.Bin;
import org.gstreamer.Buffer;
import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.GhostPad;
import org.gstreamer.Pipeline;
import org.gstreamer.Structure;
import org.gstreamer.lowlevel.GstBinAPI;
import org.gstreamer.lowlevel.GstNative;

/**
 * Class that allows to pull out native buffers from the GStreamer pipeline into
 * the application. It is almost identical to BufferDataSink, the only
 * difference is that BufferDataSink uses a fakesink as the sink element,
 * while BufferDataAppSink uses an appsink.
 */
public class BufferDataAppSink extends Bin {
    private static final GstBinAPI gst = GstNative.load(GstBinAPI.class);
    private AppSink sink;    
    private Listener listener;
    private boolean autoDisposeBuffer = true;
    
    public static interface Listener {
        void bufferFrame(int width, int height, Buffer rgb);
    }

    public BufferDataAppSink(String name, Listener listener) {
      super(initializer(gst.ptr_gst_bin_new(name)));
      this.listener = listener;
      // JNA creates ByteBuffer using native byte order, set masks according to that.
      String mask;
      if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
        mask = "red_mask=(int)0xFF00, green_mask=(int)0xFF0000, blue_mask=(int)0xFF000000";        
      } else {
        mask = "red_mask=(int)0xFF0000, green_mask=(int)0xFF00, blue_mask=(int)0xFF";
      }
      initSink(name, mask);      
    }
    
    public BufferDataAppSink(String name, String mask, Listener listener) {
        super(initializer(gst.ptr_gst_bin_new(name)));
        this.listener = listener;       
        initSink(name, mask);        
    }

    public BufferDataAppSink(String name, Pipeline pipeline, Listener listener) {
        super(initializer(gst.ptr_gst_bin_new(name)));
        this.listener = listener;

        Element element = pipeline.getElementByName(name);
        if (element != null) {            
            // TODO: Fix. This doesn't work. getElementByName() returns a BaseSink which 
            // cannot be casted to AppSink.
            sink = (AppSink) element;
            sink.set("emit-signals", true);
            sink.connect(new AppSinkNewBufferListener());
        } else {
          sink = null;
          throw new RuntimeException("Element with name " + name + " not found in the pipeline");
        }        
    }

    private void initSink(String name, String mask) {
      sink = (AppSink) ElementFactory.make("appsink", name);
      sink.set("emit-signals", true);
      sink.connect(new AppSinkNewBufferListener());
      
      //
      // Convert the input into 32bit RGB so it can be fed directly to a BufferedImage
      //
      Element conv = ElementFactory.make("videoconvert", "ColorConverter");
      Element videofilter = ElementFactory.make("capsfilter", "ColorFilter");
      StringBuilder caps = new StringBuilder("video/x-raw, format=RGB, bpp=32, depth=24, endianness=(int)4321, ");
      caps.append(mask);
      videofilter.setCaps(new Caps(caps.toString()));
      addMany(conv, videofilter, sink);
      Element.linkMany(conv, videofilter, sink);

      //
      // Link the ghost pads on the bin to the sink pad on the convertor
      //
      addPad(new GhostPad("sink", conv.getStaticPad("sink")));      
    }
    
    /**
     * Sets the listener to null. This should be used when disposing 
     * the parent object that contains the listener method, to make sure
     * that no dangling references remain to the parent.
     */    
    public void removeListener() {
      this.listener = null;
    }
    
    /**
     * Indicate whether the the native buffer is disposed automatically by the sink object
     * or not. The later is useful if the application needs to store the buffers.
     */
    public void setAutoDisposeBuffer(boolean autoDispose) {
        this.autoDisposeBuffer = autoDispose;
    }

    /**
     * Gets the actual gstreamer sink element.
     *
     * @return a AppSink
     */
    public BaseSink getSinkElement() {
        return sink;
    }

    /**
     * Gets the <tt>Caps</tt> configured on this <tt>data sink</tt>
     *
     * @return The caps configured on this <tt>sink</tt>
     */
    public Caps getCaps() {
        return sink.getCaps();
    }

    /**
     * A listener class that handles the new-buffer signal from the AppSink element.
     *
     */
    class AppSinkNewBufferListener implements AppSink.NEW_SAMPLE {
        public void newBuffer(AppSink elem)
        {
            Buffer buffer = sink.pullSample().getBuffer();

//   TODO no caps in buffer now         Caps caps = buffer.getCaps();
//            Structure struct = caps.getStructure(0);
//
//            int width = struct.getInteger("width");
//            int height = struct.getInteger("height");
//            if (width < 1 || height < 1) {
//                return;
//            }
//            
//            listener.bufferFrame(width, height, buffer);
            
            //
            // Dispose of the gstreamer buffer immediately to avoid more being
            // allocated before the java GC kicks in
            if (autoDisposeBuffer) { 
                buffer.dispose();
            }
        }
    }
}
