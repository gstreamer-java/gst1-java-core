/*
 * Copyright (c) 2009 Andres Colubri
 * Copyright (c) 2008 Wayne Meissner
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

package org.gstreamer;

import com.sun.jna.Pointer;

/**
 * Object containing specific meta information such as width/height/framerate of
 * video streams or samplerate/number of channels of audio. Each stream info object
 * has the following properties:
 * "object" (GstObject) (the decoder source pad usually)
 * "type" (enum) (if this is an audio/video/subtitle stream)
 * "decoder" (string) (name of decoder used to decode this stream)
 * "mute" (boolean) (to mute or unmute this stream)
 * "caps" (GstCaps) (caps of the decoded stream)
 * "language-code" (string) (ISO-639 language code for this stream, mostly used for audio/subtitle streams)
 * "codec" (string) (format this stream was encoded in)
 */
public class StreamInfo extends GObject {

	public enum StreamType {
		  UNKNOWN,
		  AUDIO,    /* an audio stream */
		  VIDEO,    /* a video stream */
		  TEXT,    /* a subtitle/text stream */
		  SUBPICTURE, /* a subtitle in picture-form */
		  ELEMENT    /* stream handled by an element */
	}
		
    /**
     * For internal gstreamer-java use only
     *
     * @param init initialization data
     */
    public StreamInfo(Initializer init) {
        super(init);
    }

    public StreamInfo(Pointer ptr, boolean needRef, boolean ownsHandle) {
        super(initializer(ptr, needRef, ownsHandle));
    }
    
    public StreamType getStreamType() {
    	
    	int typeVal = (Integer) get("type");
    	
    	assert typeVal < StreamType.values().length: "unknown value for GstStreamType enum - maybe gststreaminfo.h has changed";
    	
    	if (typeVal < StreamType.values().length) {
    		return StreamType.values()[typeVal];
    	}
    	
    	return StreamType.UNKNOWN;
    }
    
    /** Source Pad or object of the stream */
    public GstObject getObject() {
        Object object = get("object");
        assert object == null || object instanceof GstObject;
        return object instanceof GstObject ? (GstObject) object : null;
    }

    public String getDecoder() {
    	return (String) get("decoder");
    }
    
    public boolean getMute() {
        return (Boolean) get("mute");
    }

    public Caps getCaps() {
        return (Caps) get("caps");
    }

    public String getLanguageCode() {
        return (String) get("language-code");
    }

    public String getCodec() {
    	return (String) get("codec");
    }
}
