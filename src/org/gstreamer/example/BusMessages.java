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

import java.io.File;

import org.gstreamer.Bus;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.GstObject;
import org.gstreamer.State;
import org.gstreamer.TagList;
import org.gstreamer.elements.PlayBin;

/**
 * Demonstrates capturing messages posted on the pipeline bus by elements.
 */
public class BusMessages {
    public static void main(String[] args) {
        //
        // Initialize the gstreamer framework, and let it interpret any command
        // line flags it is interested in.
        //
        args = Gst.init("BusMessages", args);
        
        if (args.length < 1) {
            System.out.println("Usage: BusMessages <file to play>");
            System.exit(1);
        }
        //
        // Create a PlayBin to play the media file.  A PlayBin is a Pipeline that
        // creates all the needed elements and automatically links them together.
        //
        final PlayBin playbin = new PlayBin("BusMessages");
        
        // Make sure a video window does not appear.
        playbin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
        
        // Set the file to play
        playbin.setInputFile(new File(args[0]));
        
        // Listen for end-of-stream (i.e. when the media has finished)
        playbin.getBus().connect(new Bus.EOS() {

            public void endOfStream(GstObject source) {
                System.out.println("Finished playing file");
                Gst.quit();
            }
        });
        
        // Listen for any error conditions
        playbin.getBus().connect(new Bus.ERROR() {
            public void errorMessage(GstObject source, int code, String message) {
                System.out.println("Error occurred: " + message);
                Gst.quit();
            }
        });
        
        // Listen for metadata (tags)
        playbin.getBus().connect(new Bus.TAG() {

            public void tagsFound(GstObject source, TagList tagList) {
                for (String tagName : tagList.getTagNames()) {
                    // Each tag can have multiple values, so print them all.
                    for (Object tagData : tagList.getValues(tagName)) {
                        System.out.printf("[%s]=%s\n", tagName, tagData);
                    }
                }
            }
        });
        
        // Listen for state-changed messages
        playbin.getBus().connect(new Bus.STATE_CHANGED() {

            public void stateChanged(GstObject source, State old, State current, State pending) {
                if (source == playbin) {
                    System.out.println("Pipeline state changed from " + old + " to " + current);
                }
            }
        });
        
        // Start the pipeline playing
        playbin.play();
        Gst.main();
        // Clean up (gstreamer requires elements to be in State.NULL before disposal)
        playbin.stop();
        Gst.deinit();
    }
}
