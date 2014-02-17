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

import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.elements.PlayBin;

/**
 * A basic, non-gui player
 */
public class AudioPlayer {
    public static void main(String[] args) {
        //
        // Initialize the gstreamer framework, and let it interpret any command
        // line flags it is interested in.
        //
        args = Gst.init("AudioPlayer", args);
        
        if (args.length < 1) {
            System.out.println("Usage: AudioPlayer <file to play>");
            System.exit(1);
        }
        //
        // Create a PlayBin to play the media file.  A PlayBin is a Pipeline that
        // creates all the needed elements and automatically links them together.
        //
        PlayBin playbin = new PlayBin("AudioPlayer");
        
        // Make sure a video window does not appear.
        playbin.setVideoSink(ElementFactory.make("fakesink", "videosink"));
        
        // Set the file to play
        playbin.setInputFile(new File(args[0]));
        
        // Start the pipeline playing
        playbin.play();
        Gst.main();
        
        // Clean up (gstreamer requires elements to be in State.NULL before disposal)
        playbin.stop();
    }
}
