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

/**
 *
 */
public class ElementTest {
    
    /** Creates a new instance of GstElementTest */
    public ElementTest() {
    }
    public static void main(String[] args) {
        // Load some gstreamer dependencies
        args = Gst.init("foo", args);
        System.out.println("Creating fakesrc element");
        Element fakesrc = ElementFactory.make("fakesrc", "fakesrc");
        System.out.println("fakesrc element created");
        System.out.println("Element name = " + fakesrc.getName());
        System.out.println("Creating fakesink element");
        Element fakesink = ElementFactory.make("fakesink", "fakesink");
        System.out.println("fakesink element created");
        System.out.println("Element name = " + fakesink.getName());
    }
}
