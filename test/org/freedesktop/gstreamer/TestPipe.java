/* 
 * Copyright (c) 2007, 2008 Wayne Meissner
 * 
 * This file is part of gstreamer-java.
 *
 * gstreamer-java is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * gstreamer-java is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with gstreamer-java.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Pipeline;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class TestPipe {

    public final Pipeline pipe = new Pipeline("pipe");
    public final Element src = ElementFactory.make("fakesrc", "src");
    public final Element sink = ElementFactory.make("fakesink", "sink");
    public final String name;
    private final CountDownLatch latch = new CountDownLatch(1);

    public TestPipe() {
        this(getInvokingMethod());
    }
    private static String getInvokingMethod() {
        try {
            throw new Exception();
        } catch (Exception ex) {
            return ex.getStackTrace()[2].getMethodName();
        }
    }
    public TestPipe(String name) {
        this.name = name;
        pipe.addMany(src, sink);
        Element.linkMany(src, sink);
    }

    public TestPipe run() {
        try {
            latch.await(250, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
        }
        return this;
    }

    public TestPipe play() {
        pipe.play();
        return this;
    }

    public Bus getBus() {
        return pipe.getBus();
    }

    public void quit() {
        latch.countDown();
    }

    public void dispose() {
        pipe.stop();
    }

    protected void finalize() {
        dispose();
    }
}
