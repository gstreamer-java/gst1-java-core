/*
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

package org.freedesktop.gstreamer.lowlevel;

import java.util.HashMap;
import java.util.Map;

import org.freedesktop.gstreamer.Event;
import org.freedesktop.gstreamer.EventType;
import org.freedesktop.gstreamer.Message;
import org.freedesktop.gstreamer.MessageType;
import org.freedesktop.gstreamer.Query;
import org.freedesktop.gstreamer.QueryType;
import org.freedesktop.gstreamer.event.BufferSizeEvent;
import org.freedesktop.gstreamer.event.CapsEvent;
import org.freedesktop.gstreamer.event.EOSEvent;
import org.freedesktop.gstreamer.event.FlushStartEvent;
import org.freedesktop.gstreamer.event.FlushStopEvent;
import org.freedesktop.gstreamer.event.LatencyEvent;
import org.freedesktop.gstreamer.event.NavigationEvent;
import org.freedesktop.gstreamer.event.QOSEvent;
import org.freedesktop.gstreamer.event.ReconfigureEvent;
import org.freedesktop.gstreamer.event.SeekEvent;
import org.freedesktop.gstreamer.event.SegmentEvent;
import org.freedesktop.gstreamer.event.StepEvent;
import org.freedesktop.gstreamer.event.StreamStartEvent;
import org.freedesktop.gstreamer.event.TagEvent;
import org.freedesktop.gstreamer.message.AsyncDoneMessage;
import org.freedesktop.gstreamer.message.AsyncStartMessage;
import org.freedesktop.gstreamer.message.BufferingMessage;
import org.freedesktop.gstreamer.message.DurationChangedMessage;
import org.freedesktop.gstreamer.message.EOSMessage;
import org.freedesktop.gstreamer.message.ErrorMessage;
import org.freedesktop.gstreamer.message.InfoMessage;
import org.freedesktop.gstreamer.message.LatencyMessage;
import org.freedesktop.gstreamer.message.NeedContextMessage;
import org.freedesktop.gstreamer.message.NewClockMessage;
import org.freedesktop.gstreamer.message.QosMessage;
import org.freedesktop.gstreamer.message.SegmentDoneMessage;
import org.freedesktop.gstreamer.message.StateChangedMessage;
import org.freedesktop.gstreamer.message.StreamStartMessage;
import org.freedesktop.gstreamer.message.StreamStatusMessage;
import org.freedesktop.gstreamer.message.TagMessage;
import org.freedesktop.gstreamer.message.WarningMessage;
import org.freedesktop.gstreamer.query.ContextQuery;
import org.freedesktop.gstreamer.query.ConvertQuery;
import org.freedesktop.gstreamer.query.CustomQuery;
import org.freedesktop.gstreamer.query.DurationQuery;
import org.freedesktop.gstreamer.query.FormatsQuery;
import org.freedesktop.gstreamer.query.LatencyQuery;
import org.freedesktop.gstreamer.query.PositionQuery;
import org.freedesktop.gstreamer.query.SeekingQuery;
import org.freedesktop.gstreamer.query.SegmentQuery;

import com.sun.jna.Pointer;

/**
 * Mapper for classes which have subtypes (e.g. Event, Message, Query).
 * <p>
 * This class will return the subtype of the super class that best matches the
 * raw pointer passed in.
 */
@SuppressWarnings("serial")
class SubtypeMapper {
    static <T extends NativeObject> Class<?> subtypeFor(final Class<T> defaultClass, final Pointer ptr) {
        Mapper mapper = MapHolder.mappers.get(defaultClass);
        Class<?> cls = mapper != null ? mapper.subtypeFor(ptr) : null;
        return cls != null ? cls : defaultClass;
    }
    private static final class MapHolder {
        public static final Map<Class<?>, Mapper> mappers = new HashMap<Class<?>, Mapper>() {{
           this.put(Event.class, new EventMapper());
           this.put(Message.class, new MessageMapper());
           this.put(Query.class, new QueryMapper());
        }};
    }
    private static interface Mapper {
        public Class<? extends NativeObject> subtypeFor(Pointer ptr);
    }
    private static class EventMapper implements Mapper {
        static class MapHolder {
            private static final Map<EventType, Class<? extends Event>> typeMap
                = new HashMap<EventType, Class<? extends Event>>() {{
                    this.put(EventType.BUFFERSIZE, BufferSizeEvent.class);
                    this.put(EventType.EOS, EOSEvent.class);
                    this.put(EventType.CAPS, CapsEvent.class);
                    this.put(EventType.RECONFIGURE, ReconfigureEvent.class);
                    this.put(EventType.STREAM_START, StreamStartEvent.class);
                    this.put(EventType.LATENCY, LatencyEvent.class);
                    this.put(EventType.FLUSH_START, FlushStartEvent.class);
                    this.put(EventType.FLUSH_STOP, FlushStopEvent.class);
                    this.put(EventType.NAVIGATION, NavigationEvent.class);
                    this.put(EventType.SEGMENT, SegmentEvent.class);
                    this.put(EventType.SEEK, SeekEvent.class);
                    this.put(EventType.TAG, TagEvent.class);
                    this.put(EventType.QOS, QOSEvent.class);
                    this.put(EventType.STEP, StepEvent.class);
            }};
            public static Class<? extends NativeObject> subtypeFor(Pointer ptr) {
                GstEventAPI.EventStruct struct = new GstEventAPI.EventStruct(ptr);
                EventType type = EventType.valueOf((Integer) struct.readField("type"));
                Class<? extends Event> eventClass = MapHolder.typeMap.get(type);
                return eventClass != null ? eventClass : Event.class;
            }
        }
        @Override
		public Class<? extends NativeObject> subtypeFor(Pointer ptr) {
            return MapHolder.subtypeFor(ptr);
        }
    }
    private static class MessageMapper implements Mapper {
        static class MapHolder {
            private static final Map<MessageType, Class<? extends Message>> typeMap
                    = new HashMap<MessageType, Class<? extends Message>>() {{
                        this.put(MessageType.ASYNC_DONE, AsyncDoneMessage.class);
                        this.put(MessageType.ASYNC_START, AsyncStartMessage.class);
                        this.put(MessageType.EOS, EOSMessage.class);
                        this.put(MessageType.ERROR, ErrorMessage.class);
                        this.put(MessageType.BUFFERING, BufferingMessage.class);
                        this.put(MessageType.DURATION_CHANGED, DurationChangedMessage.class);
                        this.put(MessageType.INFO, InfoMessage.class);
                        this.put(MessageType.LATENCY, LatencyMessage.class);
                        this.put(MessageType.NEW_CLOCK, NewClockMessage.class);
                        this.put(MessageType.QOS, QosMessage.class);
                        this.put(MessageType.SEGMENT_DONE, SegmentDoneMessage.class);
                        this.put(MessageType.STREAM_STATUS, StreamStatusMessage.class);
                        this.put(MessageType.STREAM_START, StreamStartMessage.class);
                        this.put(MessageType.STATE_CHANGED, StateChangedMessage.class);
                        this.put(MessageType.TAG, TagMessage.class);
                        this.put(MessageType.WARNING, WarningMessage.class);
                        this.put(MessageType.NEED_CONTEXT, NeedContextMessage.class);
            }};
            public static Class<? extends NativeObject> subtypeFor(Pointer ptr) {
                GstMessageAPI.MessageStruct struct = new GstMessageAPI.MessageStruct(ptr);
                MessageType type = (MessageType) struct.readField("type");
                Class<? extends Message> messageClass = MapHolder.typeMap.get(type);
                return messageClass != null ? messageClass : Message.class;
            }
        }
        @Override
		public Class<? extends NativeObject> subtypeFor(Pointer ptr) {
            return MapHolder.subtypeFor(ptr);
        }
    }
    private static class QueryMapper implements Mapper {
        static class MapHolder {
            private static final Map<QueryType, Class<? extends Query>> typeMap
                = new HashMap<QueryType, Class<? extends Query>>() {{
                    this.put(QueryType.CONVERT, ConvertQuery.class);
                    this.put(QueryType.DURATION, DurationQuery.class);
                    this.put(QueryType.CUSTOM, CustomQuery.class);
                    this.put(QueryType.FORMATS, FormatsQuery.class);
                    this.put(QueryType.LATENCY, LatencyQuery.class);
                    this.put(QueryType.POSITION, PositionQuery.class);
                    this.put(QueryType.SEEKING, SeekingQuery.class);
                    this.put(QueryType.SEGMENT, SegmentQuery.class);
                    this.put(QueryType.CONTEXT, ContextQuery.class);
            }};
            public static Class<? extends NativeObject> subtypeFor(Pointer ptr) {
                GstQueryAPI.QueryStruct struct = new GstQueryAPI.QueryStruct(ptr);
                QueryType type = QueryType.valueOf((Integer) struct.readField("type"));
                Class<? extends Query> queryClass = MapHolder.typeMap.get(type);
                return queryClass != null ? queryClass : Query.class;
            }
        }
        @Override
		public Class<? extends NativeObject> subtypeFor(Pointer ptr) {
            return MapHolder.subtypeFor(ptr);
        }
    }
}
