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

import org.gstreamer.*;
import org.gstreamer.interfaces.*;

public class PropertyProber {
	public static void main(String[] args) {
        args = Gst.init("foo", args);
		String[] deviceListing = new String[0];
		Element videoSource = ElementFactory.make("v4l2src", "Source");
		PropertyProbe probe = PropertyProbe.wrap(videoSource);
		if (probe != null) {
			Property property = probe.getProperty("device");
			if (property != null) {
				Object[] values = probe.getValues(property);
				if (values != null) {
					deviceListing = new String[values.length];
					for (int i = 0; i < values.length; i++)
						if (values[i] instanceof String)
							deviceListing[i] = (String) values[i];
				}
			}
		}
		System.out.println(deviceListing);
	}
}
