/* 
 * Copyright (c) 2009 Tamas Korodi <kotyo@zamba.fm>
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

import java.util.List;

import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Gst;
import org.gstreamer.Pipeline;
import org.gstreamer.interfaces.Tuner;
import org.gstreamer.interfaces.TunerChannel;
import org.gstreamer.interfaces.TunerNorm;

public class TunerTest {
	public static void main(String[] args) {
		args = Gst.init("ColorBalance video test", args);

		Pipeline pipe = new Pipeline("pipeline");
		final Element videosrc = ElementFactory.make("v4l2src", "source");
		videosrc.set("device", "/dev/video0");
		final Element videosink = ElementFactory.make("xvimagesink", "xv");

		pipe.addMany(videosrc, videosink);
		Element.linkMany(videosrc, videosink);
						
		pipe.play();

		Tuner tun = Tuner.wrap(videosrc);

		List<TunerNorm> normList = tun.getNormList();
		for (TunerNorm n : normList) {
			System.out.println("Available norm: " + n.getLabel());
		}

		List<TunerChannel> chList = tun.getChannelList();

		for (TunerChannel ch: chList) {
			System.out.println("Channel ["+ch.getLabel()+"]: "+ch.isTuningChannel());
		}
		
		for (int i=0;;i++) {
			tun.setChannel(chList.get(i%chList.size()));
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}			
		}
		
		
	}

}
