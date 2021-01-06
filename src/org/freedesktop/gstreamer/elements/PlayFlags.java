/* 
 * Copyright (c) 2021 Neil C Smith
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
package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.glib.NativeFlags;

/**
 * Extra flags to configure the behaviour of the {@link PlayBin} sinks.
 */
public enum PlayFlags implements NativeFlags<PlayFlags> {

    /**
     * Enable rendering of the video stream.
     */
    VIDEO(1 << 0),
    /**
     * Enable rendering of the audio stream.
     */
    AUDIO(1 << 1),
    /**
     * Enable rendering of subtitles.
     */
    TEXT(1 << 2),
    /**
     * Enable rendering of visualisations when there is no video stream.
     */
    VIS(1 << 3),
    /**
     * Use software volume.
     */
    SOFT_VOLUME(1 << 4),
    /**
     * Only allow native audio formats, this omits configuration of audioconvert
     * and audioresample.
     */
    NATIVE_AUDIO(1 << 5),
    /**
     * Only allow native video formats, this omits configuration of videoconvert
     * and videoscale.
     */
    NATIVE_VIDEO(1 << 6),
    /**
     * Enable progressive download buffering for selected formats.
     */
    DOWNLOAD(1 << 7),
    /**
     * Enable buffering of the demuxed or parsed data.
     */
    BUFFERING(1 << 8),
    /**
     * Deinterlace raw video (if native not forced).
     */
    DEINTERLACE(1 << 9),
    /**
     * Use a software filter for colour balance.
     */
    SOFT_COLORBALANCE(1 << 10),
    /**
     * Force audio/video filters to be applied if set.
     */
    FORCE_FILTERS(1 << 11),
    /**
     * Force to use only software-based decoders ignoring those with hardware
     * class.
     */
    FORCE_SW_DECODERS(1 << 12);

    private final int value;

    private PlayFlags(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }

}
