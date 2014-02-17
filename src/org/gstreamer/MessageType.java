/* 
 * Copyright (C) 2009 Levente Farkas
 * Copyright (C) 2008 Wayne Meissner
 * Copyright (C) 2004 Wim Taymans <wim@fluendo.com>
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

import java.util.HashMap;
import java.util.Map;

import org.gstreamer.lowlevel.EnumMapper;
import org.gstreamer.lowlevel.GstMessageAPI;
import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.IntegerEnum;

/**
 * The different message types that are available.
 */
public enum MessageType implements IntegerEnum {
    /** An undefined message */
    UNKNOWN(0),
    /** 
     * end-of-stream reached in a pipeline. The application will
     * only receive this message in the PLAYING state and every time it sets a
     * pipeline to PLAYING that is in the EOS state. The application can perform a
     * flushing seek in the pipeline, which will undo the EOS state again. 
     */
    EOS(1 << 0),
    /** 
     * An error occured. Whe the application receives an error
     * message it should stop playback of the pipeline and not assume that more
     * data will be played.
     */
    ERROR(1 << 1),
    
    /** A warning occured. */
    WARNING(1 << 2),
    /** An info message occured. */
    INFO(1 << 3),
    /** A tag was found. */
    TAG(1 << 4),
    /**
     * The pipeline is buffering. When the application
     * receives a buffering message in the PLAYING state for a non-live pipeline it
     * must PAUSE the pipeline until the buffering completes, when the percentage
     * field in the message is 100%. For live pipelines, no action must be
     * performed and the buffering percentage can be used to inform the user about
     * the progress.
     */
    BUFFERING(1 << 5),
    /** A state change happened */
    STATE_CHANGED(1 << 6),
    /** an element changed state in a streaming thread. This message is deprecated.*/
    STATE_DIRTY(1 << 7),
    /** a framestep finished. This message is not yet implemented. */
    STEP_DONE(1 << 8),
    /**
     * an element notifies its capability of providing a clock. This message is 
     * used internally and never forwarded to the application.
     */
    CLOCK_PROVIDE(1 << 9),
    /** 
     * The current clock as selected by the pipeline became unusable. The pipeline 
     * will select a new clock on the next PLAYING state change.
     */
    CLOCK_LOST(1 << 10),
    /** A new clock was selected in the pipeline. */
    NEW_CLOCK(1 << 11),
    /** The structure of the pipeline changed. Not implemented yet. */
    STRUCTURE_CHANGE(1 << 12),
    /**
     * Status about a stream, emitted when it starts, stops, errors, etc.. Not implemented yet.
     */
    STREAM_STATUS(1 << 13),
    /** Message posted by the application, possibly via an application-specific element. */
    APPLICATION(1 << 14),
    /** Element specific message, see the specific element's documentation */
    ELEMENT(1 << 15),
    /**
     * Pipeline started playback of a segment. This message is used internally and 
     * never forwarded to the application.
     */
    SEGMENT_START(1 << 16),
    /**
     * Pipeline completed playback of a segment. This message is forwarded to the 
     * application after all elements that posted {@link #SEGMENT_START}
     * have posted a SEGMENT_DONE message.
     */
    SEGMENT_DONE(1 << 17),
    /**
     * The duration of a pipeline changed. The application can get the new duration 
     * with a duration query.
     */
    DURATION(1 << 18),
    /**
     * Posted by elements when their latency changes. The pipeline will calculate 
     * and distribute a new latency. Since: 0.10.12
     */
    LATENCY(1 << 19),
    /**
     * Posted by elements when they start an ASYNC state
     * change. This message is not forwarded to the application but is used
     * internally. Since: 0.10.13. 
     */
    ASYNC_START(1 << 20),
    /**
     * Posted by elements when they complete an ASYNC state change. The application 
     * will only receive this message from the toplevel pipeline. Since: 0.10.13
     */
    ASYNC_DONE(1 << 21),
    /**
     * Posted by elements when they want the pipeline to change state. This message 
     * is a suggestion to the application which can decide to perform the 
     * state change on (part of) the pipeline. Since: 0.10.23. 
     */
    REQUEST_STATE(1 << 22),
    /**
     * A stepping operation was started. 
     */
    STEP_START(1 << 23),
    /** 
     * A buffer was dropped or an element changed its processing strategy for
     * Quality of Service reasons.
     */
    QOS(1 << 24),
    /**
     * A progress message. Since: 0.10.33 
     */
    PROGRESS(1 << 25),
    /**
     * A new table of contents (TOC) was found or previously found TOC was updated. Since: 0.10.37
     */
    TOC(1 << 26),
    /**
     * 
     */
    RESET_TIME(1 << 27),
    /**
     * 
     */
    STREAM_START(1 << 28),

    ANY(~0);
    
    MessageType(int type) {
        this.type = type;
        GstMessageAPI messageAPI = GstNative.load(GstMessageAPI.class);
        this.name = messageAPI.gst_message_type_get_name(this);
    }
    
    /**
     * Gets the native integer value for this type.
     * 
     * @return the native gstreamer value.
     */
    public int intValue() {
        return type;
    }
    
    /**
     * Gets the name of this message type.
     * 
     * @return the name of the message type.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets a MessageType that corresponds to the native integer value.
     * 
     * @param type the native value of the type.
     * @return a MessageType.
     */
    public static final MessageType valueOf(int type) {
        return EnumMapper.getInstance().valueOf(type, MessageType.class);
    }
    
    /**
     * Gets a MessageType that corresponds to the name
     * 
     * @param name the gstreamer name of the type.
     * @return a MessageType.
     */
    public static final MessageType forName(String name) {
        MessageType type = MapHolder.typeMap.get(name);
        return type != null ? type : UNKNOWN;
    }
    private final int type;
    private final String name;
    private static final class MapHolder {
        private static final Map<String, MessageType> typeMap = new HashMap<String, MessageType>();
        static {
            for (MessageType t : MessageType.values()) {
                typeMap.put(t.getName(), t);
            }
        }
    }
}
