/* 
 * Copyright (c) 2009 Tal Shalif
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

import org.gstreamer.Gst;
import org.gstreamer.Pipeline;

/**
 * A simple command-line pipeline launching utility.
 * 
 * @see Pipeline#launch(java.lang.String)
 */
public class PipelineLauncher {
    /**
     * Launches a pipeline from command-line pipeline description.
     * You can find examples for command-line pipeline syntax in the manual page
     * for the Gstreamer <code>gst-launch</code> utility.
     *
     * For example: <pre>
     * java org.gstreamer.example.PipelineLauncher videotestsrc ! autovideosink
     * </pre>
     *
     * @param args pipline definition
     */
    public static void main(String[] args) {
        //
        // Initialize the gstreamer framework, and let it interpret any command
        // line flags it is interested in.
        //
        args = Gst.init("PipelineLauncher", args);

        if (args.length == 0) {
            args = new String[]{"videotestsrc", "!", "autovideosink"};
        }

        StringBuilder sb = new StringBuilder();

        for (String s: args) {
            sb.append(" ");
            sb.append(s);
        }
        
        Pipeline pipe = Pipeline.launch(sb.substring(1));

        pipe.play();

        Gst.main();

        pipe.stop();
    }
}
