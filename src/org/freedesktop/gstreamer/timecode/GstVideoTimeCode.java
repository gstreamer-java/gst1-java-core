package org.freedesktop.gstreamer.timecode;

import com.sun.jna.Pointer;
import java.util.stream.Stream;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.glib.Natives.registration;
import static org.freedesktop.gstreamer.lowlevel.GstMetaApi.GstVideoTimeCodeStruct;

/*
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
 *
 * A representation of a SMPTE time code.
 *
 * @see <a href="https://docs.gstreamer.com/documentation/video/gstvideotimecode.html?gi-language=c#GstVideoTimeCode">GstVideoTimeCode</a>
 */
public class GstVideoTimeCode extends MiniObject {

    public static final String GTYPE_NAME = "GstVideoTimeCode";
    private final GstVideoTimeCodeStruct timeCodeStruct;
    private final GstVideoTimeCodeConfig timeCodeConfig;

    public GstVideoTimeCode(Pointer pointer) {
        this(Natives.initializer(pointer,false,false));
    }

    GstVideoTimeCode(Initializer init) {
        super(init);
        timeCodeStruct = new GstVideoTimeCodeStruct(getRawPointer());
        timeCodeConfig = new GstVideoTimeCodeConfig(timeCodeStruct.config.getPointer());
    }

    public GstVideoTimeCodeConfig getTCConfig() {
        return timeCodeConfig;
    }

    /**
     * Hours field, must be less than 24
     *
     * @return return number of hours
     */
    public int getHours() {
        return timeCodeStruct.hours;
    }

    /**
     * Minutes field, must be less than 60
     *
     * @return return number of minutes
     */
    public int getMinutes() {
        return timeCodeStruct.minutes;
    }

    /**
     * Second field, must be less than 60
     *
     * @return return number of seconds
     */
    public int getSeconds() {
        return timeCodeStruct.seconds;
    }

    /**
     * Frames field
     *
     * @return return number of seconds
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

    public static final class TimeTypes implements NativeObject.TypeProvider {

        @Override
        public Stream<TypeRegistration<?>> types() {
            return Stream.of(
                    registration(GstVideoTimeCode.class,
                            GstVideoTimeCode.GTYPE_NAME,
                            GstVideoTimeCode::new),
                    registration(GstVideoTimeCodeConfig.class,
                            GstVideoTimeCodeConfig.GTYPE_NAME,
                            GstVideoTimeCodeConfig::new));
        }
    }
}
