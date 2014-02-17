/* 
 * Copyright (c) 2007, 2008 Wayne Meissner
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.gstreamer.example;

import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pad;
import org.gstreamer.Pipeline;

/**
 *
 */
public class DynamicPadTest {
    
    /** Creates a new instance of DynamicPadTest */
    public DynamicPadTest() {
    }
    public static void main(String[] args) {
        args = Gst.init("Dynamic Pad Test", args);
        /* create elements */
        Pipeline pipeline = new Pipeline("my_pipeline");
        Element source = ElementFactory.make("filesrc", "source");
        source.set("location", args[0]);
        Element demux = ElementFactory.make("oggdemux", "demuxer");
        
        /* you would normally check that the elements were created properly */
        
        /* put together a pipeline */
        pipeline.addMany(source, demux);
        Element.linkPads(source, "src", demux, "sink");
        
        /* listen for newly created pads */
        demux.connect(new Element.PAD_ADDED() {
            public void padAdded(Element element, Pad pad) {
               System.out.println("New Pad " + pad.getName() + " was created");
            }
        });
        
        /* start the pipeline */
        pipeline.play();
        Gst.main();
    }
}
