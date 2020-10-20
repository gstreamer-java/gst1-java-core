/*
 * Copyright (c) 2020 Neil C Smith
 * Copyright (c) 2020 Petr Lastovka
 *
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freedesktop.gstreamer.video;

import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.lowlevel.GPointer;
import org.freedesktop.gstreamer.lowlevel.GstVideoAPI;
import org.freedesktop.gstreamer.lowlevel.GstVideoAPI.GstVideoTimeCodeStruct;


/**
 * A representation of a SMPTE time code.
 *
 * See <a href="https://gstreamer.freedesktop.org/documentation/video/gstvideotimecode.html"
 * >https://gstreamer.freedesktop.org/documentation/video/gstvideotimecode.html</a>
 */
@Gst.Since(minor = 10)
public class VideoTimeCode extends NativeObject {

    private final GstVideoTimeCodeStruct timeCodeStruct;
    private final VideoTimeCodeConfig timeCodeConfig;

//    public VideoTimeCode(){
//        this(Natives.initializer(GstVideoAPI.GSTVIDEO_API.gst_video_time_code_new_empty()));
//    }

    VideoTimeCode(GstVideoTimeCodeStruct struct) {
        this(struct, new Handle(new GPointer(struct.getPointer()), false));
    }
    
    private VideoTimeCode(GstVideoTimeCodeStruct struct, Handle handle) {
        super(handle);
        this.timeCodeStruct = struct;
        timeCodeConfig = new VideoTimeCodeConfig(timeCodeStruct.config);
    }

    public VideoTimeCodeConfig getConfig() {
        return timeCodeConfig;
    }

    /**
     * Hours field, must be less than 24.
     *
     * @return number of hours
     */
    public int getHours() {
        return timeCodeStruct.hours;
    }

    /**
     * Minutes field, must be less than 60.
     *
     * @return number of minutes
     */
    public int getMinutes() {
        return timeCodeStruct.minutes;
    }

    /**
     * Second field, must be less than 60.
     *
     * @return number of seconds
     */
    public int getSeconds() {
        return timeCodeStruct.seconds;
    }

    /**
     * Frames field.
     *
     * @return number of seconds
     */
    public int getFrames() {
        return timeCodeStruct.frames;
    }

    @Override
    public String toString() {
        return "GstVideoTimeCode{" + getHours() + ":" + getMinutes() + ":" + getSeconds() + ":" + getFrames() + ", timeconfig=" + timeCodeConfig + "}";
    }

    @Override
    public void disown() {
        timeCodeConfig.disown();
        super.disown();
    }

    private static final class Handle extends NativeObject.Handle{

        /**
         * Construct a Handle for the supplied native reference.
         *
         * @param ptr           native reference
         * @param ownsReference whether the Handle owns the native reference and
         */
        public Handle(GPointer ptr, boolean ownsReference) {
            super(ptr, ownsReference);
        }

        @Override
        protected void disposeNativeHandle(GPointer ptr) {
            GstVideoAPI.GSTVIDEO_API.gst_video_time_code_free(ptr.getPointer());
        }

        @Override
        protected GPointer getPointer() {
            return super.getPointer();
        }
        
        
    }
}
