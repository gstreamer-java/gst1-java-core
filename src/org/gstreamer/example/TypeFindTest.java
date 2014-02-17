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

import org.gstreamer.Caps;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.elements.TypeFind;

/**
 *
 */
public class TypeFindTest {
    
    /** Creates a new instance of TypeFindTest */
    public TypeFindTest() {
    }
    public static void main(String[] args) {
        args = Gst.init("TypeFind Test", args);
        /* create elements */
        Pipeline pipeline = new Pipeline("my_pipeline");
        Element source = ElementFactory.make("filesrc", "source");
        source.set("location", args[0]);
        TypeFind typefind = new TypeFind("typefinder");
        
        /* you would normally check that the elements were created properly */
        
        /* put together a pipeline */
        pipeline.addMany(source, typefind);
        Element.linkMany(source, typefind);
        
        /* listen for types found */
        typefind.connect(new TypeFind.HAVE_TYPE() {

            public void typeFound(Element elem, int probability, Caps caps) {
                System.out.printf("New type found: probability=%d caps=%s\n",
                        probability, caps.toString());
            }
        });
        
        /* start the pipeline */
        pipeline.play();
        
        Gst.main();
    }
}
