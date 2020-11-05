/*
 * Copyright (c) 2020 Christophe Lafolet
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
 */
package org.freedesktop.gstreamer.video;

import org.freedesktop.gstreamer.Meta;
import org.freedesktop.gstreamer.lowlevel.GstVideoAPI.GstVideoMetaStruct;

public class VideoMeta extends Meta {

    /**
     * Meta.API for VideoMeta.
     */
    public static final API<VideoMeta> API = new API<>(VideoMeta.class, "GstVideoMetaAPI");

    /**
     * Underlying GType name.
     */
    public static final String GTYPE_NAME = "GstVideoMeta";

    private final GstVideoMetaStruct struct;
    
    VideoMeta(Initializer init) {
        super(init);
        this.struct = new GstVideoMetaStruct(init.ptr.getPointer());
    }

    /**
     * Returns the width of the video frame 
     * @return the width in pixels
     */
    public int getWidth() {
    	return Integer.class.cast(this.struct.readField("width")).intValue();
    }
    
    /**
     * Returns the height of the video frame 
     * @return the height in pixels
     */
    public int getHeight() {
    	return Integer.class.cast(this.struct.readField("height")).intValue();
    }
    
//    /**
//     * Returns the video format of the video frame 
//     * @return the format
//     */
//    public VideoFormats getVideoFormat() {
//    	return VideoFormats.valueOf(Integer.class.cast(this.struct.readField("videoFormat")).intValue());
//    }

    /**
     * Returns the number of planes for the format
     * @return 
     */
    public int getNumberOfPlanes() {
    	return Integer.class.cast(this.struct.readField("n_planes")).intValue();
    }
    
    /**
     * Returns the identifier of the frame
     * @return 
     */
    public int getFrameId() {
    	return Integer.class.cast(this.struct.readField("id")).intValue();
    }
    
    /**
     * Return additional video flags
     * @return 
     */
    public int getFrameFlags() {
    	return Integer.class.cast(this.struct.readField("flags")).intValue();
    }
    
    /**
     * Return the stride of a plane
     * @param index select plane in range 0..3
     * @return 
     */
    public int getStride(int index) {
    	int[] strides = (int[])this.struct.readField("stride");
    	return strides[index];
    }

    /**
     * Return the offset of a plane
     * @param index select plane in range 0..3
     * @return 
     */
    public long getOffset(int index) {
    	long[] offsets = (long[])this.struct.readField("offset");
    	return offsets[index];
    }
}
