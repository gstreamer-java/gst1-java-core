/*
 * Copyright (c) 2008 Wayne Meissner
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

import java.util.concurrent.TimeUnit;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;

/**
 * Test consequtive Gst.main/Gst.quit call sequences
 */
public class DoubleQuit {
    private static Pipeline makePipe() {
        Pipeline pipe = new Pipeline("AudioPanorama");

        Element src = ElementFactory.make("audiotestsrc", "src");
        src.set("wave", 2);
        Element convert = ElementFactory.make("audioconvert", "convert");
        Element sink = ElementFactory.make("fakesink", "sink");
        pipe.addMany(src, convert, sink);
        Element.linkMany(src, convert, sink);
        return pipe;
    }
    public static void main(String[] args) {
        //
        // Initialize the gstreamer framework, and let it interpret any command
        // line flags it is interested in.
        //
        args = Gst.init("DoubleQuit", args);
        
        for (int i = 0; i < 2; ++i) {
            Pipeline pipe = makePipe();
            Gst.getScheduledExecutorService().schedule(new Runnable() {

                public void run() {
                    Gst.quit();
                }
            }, 1, TimeUnit.SECONDS);
            // Start the pipeline playing
            pipe.play();
            System.out.println("Running main loop " + i);
            Gst.main();
            // Clean up (gstreamer requires elements to be in State.NULL before disposal)
            pipe.stop();
        }
    }
}
