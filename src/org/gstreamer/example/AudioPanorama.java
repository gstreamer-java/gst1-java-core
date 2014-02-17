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
import java.util.concurrent.TimeUnit;

import org.gstreamer.Bin;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.GhostPad;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.elements.PlayBin;

/**
 * An audiopanorama element example.
 * <p>
 * This example just creates a basic audio source and makes the output appear to
 * continuously sweep from left to right and back again.
 */
public class AudioPanorama {
    private final static int PERIOD = 1000; // milliseconds for sweep time
    
    public static void main(String[] args) {
        //
        // Initialize the gstreamer framework, and let it interpret any command
        // line flags it is interested in.
        //
        args = Gst.init("AudioPanorama", args);
        PanoramaSink sink = new PanoramaSink("panorama sink");
        Pipeline pipe;
        if (args.length > 0) {
            PlayBin playbin = new PlayBin("AudioPanorama");

            playbin.setInputFile(new File(args[0]));
            
            playbin.setAudioSink(sink);
            pipe = playbin;
        } else {
            pipe = new Pipeline("AudioPanorama");

            Element src = ElementFactory.make("audiotestsrc", "src");
            src.set("wave", 2);
            Element convert = ElementFactory.make("audioconvert", "convert");
            pipe.addMany(src, convert, sink);
            Element.linkMany(src, convert, sink);
        }
        Gst.getScheduledExecutorService().scheduleAtFixedRate(new Panner(sink),
                100, PERIOD / 100, TimeUnit.MILLISECONDS);
        // Start the pipeline playing
        pipe.play();

        Gst.main();

        // Clean up (gstreamer requires elements to be in State.NULL before disposal)
        pipe.stop();
    }
    
    private static final class PanoramaSink extends Bin {
        private final Element audiopanorama;
        private final Element convert;
        private final Element sink;
        public PanoramaSink(String name) {
            super(name);
            audiopanorama = ElementFactory.make("audiopanorama", "panorama");
            convert = ElementFactory.make("audioconvert", "convert");
            sink = ElementFactory.make("autoaudiosink", "audiosink");
            addMany(audiopanorama, convert, sink);
            linkMany(audiopanorama, convert, sink);
            addPad(new GhostPad("sink", audiopanorama.getStaticPad("sink")));
        }
        public void setPanorama(float pan) {
            audiopanorama.set("panorama", pan);
        }
        @SuppressWarnings("unused")
        public float getPanorama() {
            return ((Number) audiopanorama.get("panorama")).floatValue();
        }
    }
    private static final class Panner implements Runnable {
        private float pan = -1.0f;
        private float incr = 0.01f;
        private final PanoramaSink sink;
        private Panner(PanoramaSink sink) {
            this.sink = sink;
        }
        public void run() {
            sink.setPanorama(pan);
            pan += incr;
            if (pan > 1.0f || pan < -1.0f) {
                // start sweeping in the opposite direction
                incr = 0f - incr;
                pan += incr;
            }
        }
    }
}
