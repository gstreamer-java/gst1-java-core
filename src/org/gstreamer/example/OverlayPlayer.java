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

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.gstreamer.Bus;
import org.gstreamer.BusSyncReply;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.GstObject;
import org.gstreamer.Message;
import org.gstreamer.Structure;
import org.gstreamer.elements.PlayBin;
import org.gstreamer.event.BusSyncHandler;
import org.gstreamer.interfaces.XOverlay;

import com.sun.jna.Platform;

public class OverlayPlayer {

    /** Creates a new instance of SwingPlayer */
    public OverlayPlayer() {
    }
    private static Bus bus;
    
    public static void main(String[] args) {
        //System.setProperty("sun.java2d.opengl", "True");
        
        args = Gst.init("Swing Player", args);
        if (args.length < 1) {
            System.err.println("Usage: SwingPlayer <filename>");
            System.exit(1);
        }
        final String file = args[0];
        final String overlayFactory = Platform.isWindows() ? "directdrawsink" : "xvimagesink";
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                JFrame frame = new JFrame("Overlay Test");
                final Canvas canvas = new Canvas();
                canvas.setPreferredSize(new Dimension(640, 480));
                frame.add(canvas, BorderLayout.CENTER);                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
                                
                PlayBin player = new PlayBin("Overlay Player");                
                player.setInputFile(new File(file));
                bus = player.getBus();
                
                bus.connect(new Bus.ERROR() {
                    public void errorMessage(GstObject source, int code, String message) {
                        System.out.println("Error: code=" + code + " message=" + message);
                    }
                });
                final Element videoSink = ElementFactory.make(overlayFactory, "overlay video sink");
                player.setVideoSink(videoSink);
                //
                // Setting the overlay window ID is supposed to be done from a sync handler
                // but that doesn't work on windows
                //
                if (!Platform.isWindows()) { 
                    bus.setSyncHandler(new BusSyncHandler() {

                        public BusSyncReply syncMessage(Message msg) {
                            Structure s = msg.getStructure();
                            if (s == null || !s.hasName("prepare-xwindow-id")) {
                                return BusSyncReply.PASS;
                            }
                            XOverlay.wrap(videoSink).setWindowHandle(canvas);
                            return BusSyncReply.DROP;
                        }
                    });
                } else {
                    XOverlay.wrap(videoSink).setWindowHandle(canvas);
                } 
                player.play();       
            }  
        });
    }
}
