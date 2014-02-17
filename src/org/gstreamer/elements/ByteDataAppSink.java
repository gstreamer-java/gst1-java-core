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

import java.nio.ByteBuffer;

import org.gstreamer.Bin;
import org.gstreamer.Buffer;
import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.GhostPad;
import org.gstreamer.Pipeline;
import org.gstreamer.lowlevel.GstBinAPI;
import org.gstreamer.lowlevel.GstNative;

/**
 * Class that allows to pull out buffers from the GStreamer pipeline into
 * the application, using an appsink. The buffers are NOT converted into 
 * 32bit RGB. It uses an appsink as the sink element.
 */
public class ByteDataAppSink extends Bin {
    private static final GstBinAPI gst = GstNative.load(GstBinAPI.class);
    private final AppSink sink;
    private boolean passDirectBuffer = false;
    private Listener listener;
    
    public static interface Listener {
        void byteFrame(Caps caps, int size, ByteBuffer data);
    }

    public ByteDataAppSink(String name, Listener listener) {
        super(initializer(gst.ptr_gst_bin_new(name)));
        this.listener = listener;
       
        sink = (AppSink) ElementFactory.make("appsink", name);
        sink.set("emit-signals", true);
        sink.connect(new AppSinkNewBufferListener());
        
        //
        // Adding identity element
        //
        Element conv = ElementFactory.make("identity", "Data");        
        addMany(conv, sink);
        Element.linkMany(conv, sink);

        //
        // Link the ghost pads on the bin to the sink pad on the convertor
        //
        addPad(new GhostPad("sink", conv.getStaticPad("sink")));        
    }

    public ByteDataAppSink(String name, Pipeline pipeline, Listener listener) {
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

    /**
     * Sets the listener to null. This should be used when disposing 
     * the parent object that contains the listener method, to make sure
     * that no dangling references remain to the parent.
     */    
    public void removeListener() {
      this.listener = null;
    }
    
    /**
     * Indicate whether the {@link RGBDataAppSink} should pass the native {@link java.nio.IntBuffer}
     * to the listener, or should copy it to a heap buffer.  The default is to pass
     * a heap {@link java.nio.IntBuffer} copy of the data
     * @param passThru If true, pass through the native IntBuffer instead of
     * copying it to a heap IntBuffer.
     */
    public void setPassDirectBuffer(boolean passThru) {
        this.passDirectBuffer = passThru;
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

//  TODO no caps in buffer now          Caps caps = buffer.getCaps();
//            int n = buffer.getSize();
//            
//            if (n < 1) {
//                return;
//            }
//            
//            ByteBuffer data;
//            if (passDirectBuffer) {
//                data = buffer.getByteBuffer();
//            } else {
//                data = ByteBuffer.allocate(n);
//                data.put(buffer.getByteBuffer()).flip();
//            }
//            
//            listener.byteFrame(caps, n, data);

            //
            // Dispose of the gstreamer buffer immediately to avoid more being
            // allocated before the java GC kicks in
            //
            buffer.dispose();
        }
    }
}
