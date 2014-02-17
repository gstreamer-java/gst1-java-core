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
import java.util.concurrent.CountDownLatch;

import org.gstreamer.Buffer;
import org.gstreamer.Bus;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.GstObject;
import org.gstreamer.Pad;
import org.gstreamer.TagList;
import org.gstreamer.elements.BaseSink;
import org.gstreamer.elements.FakeSink;
import org.gstreamer.elements.PlayBin;

/**
 * A simple pipeline, demonstrating media tag detection
 */
public class TagFinder {
    private static final String progname = "TagFinder";
    public static void main(String[] args) {
        final CountDownLatch done = new CountDownLatch(1);
        
        //
        // Initialize the gstreamer framework, and let it interpret any command
        // line flags it is interested in.
        //
        args = Gst.init(progname, args);
        
        if (args.length < 0) {
            System.out.println("Usage: " + progname + " <filename>");
            System.exit(1);
        }
        //
        // Instead of using a playbin, it would be possible to use a pipe
        // a typefind element and a demux and wire them up manually.
        // 
        final PlayBin pipe = new PlayBin(progname);
        pipe.setInputFile(new File(args[0]));
        FakeSink audio = (FakeSink) ElementFactory.make("fakesink", "audio-sink");
        FakeSink video = (FakeSink) ElementFactory.make("fakesink", "video-sink");
        pipe.setAudioSink(audio);
        pipe.setVideoSink(video);
        
        pipe.getBus().connect(new Bus.TAG() {
            public void tagsFound(GstObject source, TagList tagList) {
                for (String tag : tagList.getTagNames()) {
                    System.out.println("Found tag " + tag + " = "
                            + tagList.getValue(tag, 0));
                }
            }
        });
        
        //
        // In theory, an ASYNC_DONE from the pipeline corresponds with the demux
        // completing parsing the media file
        //
        pipe.getBus().connect(new Bus.ASYNC_DONE() {
            public void asyncDone(GstObject source) {
                pipe.stop();
                done.countDown();
            }
        });
        audio.set("signal-handoffs", true);
        video.set("signal-handoffs", true);
        
        //
        // As soon as data starts to flow, it means all tags have been found
        //
        BaseSink.HANDOFF handoff = new BaseSink.HANDOFF() {
            public void handoff(BaseSink sink, Buffer buffer, Pad pad) {
                pipe.stop();
                done.countDown();
            }
        };
        audio.connect(handoff);
        video.connect(handoff);
        
        // Start the pipeline playing
        pipe.pause();
        try {
            done.await();
        } catch (InterruptedException ex) {
        }
        pipe.stop();
    }
}
