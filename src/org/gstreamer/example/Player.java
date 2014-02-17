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

import java.io.File;

import org.gstreamer.Bus;
import org.gstreamer.Gst;
import org.gstreamer.GstObject;
import org.gstreamer.TagList;
import org.gstreamer.TagMergeMode;
import org.gstreamer.elements.PlayBin;

/**
 *
 */
public class Player {
    
    /** Creates a new instance of Player */
    public Player() {
    }
    static TagList tags;
    static PlayBin player;
    public static void main(String[] args) {
        for (String s : args) {
            System.out.println("cmdline arg=" + s);
        }
        
        args = Gst.init("Simple Player", args);
        for (String s : args) {
            System.out.println("Leftover arg=" + s);
        }
        player = new PlayBin("Example Player");
        player.setInputFile(new File(args[0]));
        Bus bus = player.getBus();
        tags = new TagList();
        bus.connect(new Bus.TAG() {
            public void tagsFound(GstObject source, TagList tagList) {
                System.out.println("Got TAG event");
                for (String tag : tagList.getTagNames()) {
                    System.out.println("Tag " + tag + " = " + tagList.getValue(tag, 0));
                }
                tags = tags.merge(tagList, TagMergeMode.APPEND);
            }
        });
        bus.connect(new Bus.ERROR() {
            public void errorMessage(GstObject source, int code, String message) {
                System.out.println("Error: code=" + code + " message=" + message);
            }
        });
        
        player.play();
    }
}
