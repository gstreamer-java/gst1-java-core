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

package org.gstreamer.lowlevel;

import java.util.HashMap;
import java.util.Map;

import org.gstreamer.Event;
import org.gstreamer.EventType;
import org.gstreamer.Message;
import org.gstreamer.MessageType;
import org.gstreamer.Query;
import org.gstreamer.QueryType;
import org.gstreamer.event.BufferSizeEvent;
import org.gstreamer.event.EOSEvent;
import org.gstreamer.event.FlushStartEvent;
import org.gstreamer.event.FlushStopEvent;
import org.gstreamer.event.LatencyEvent;
import org.gstreamer.event.NavigationEvent;
import org.gstreamer.event.NewSegmentEvent;
import org.gstreamer.event.QOSEvent;
import org.gstreamer.event.SeekEvent;
import org.gstreamer.event.TagEvent;
import org.gstreamer.message.BufferingMessage;
import org.gstreamer.message.DurationMessage;
import org.gstreamer.message.EOSMessage;
import org.gstreamer.message.ErrorMessage;
import org.gstreamer.message.InfoMessage;
import org.gstreamer.message.LatencyMessage;
import org.gstreamer.message.SegmentDoneMessage;
import org.gstreamer.message.StateChangedMessage;
import org.gstreamer.message.TagMessage;
import org.gstreamer.message.WarningMessage;
import org.gstreamer.query.ConvertQuery;
import org.gstreamer.query.DurationQuery;
import org.gstreamer.query.FormatsQuery;
import org.gstreamer.query.LatencyQuery;
import org.gstreamer.query.PositionQuery;
import org.gstreamer.query.SeekingQuery;
import org.gstreamer.query.SegmentQuery;

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
           put(Event.class, new EventMapper());
           put(Message.class, new MessageMapper());
           put(Query.class, new QueryMapper());
        }};
    }
    private static interface Mapper {
        public Class<? extends NativeObject> subtypeFor(Pointer ptr);
    }
    private static class EventMapper implements Mapper {
        static class MapHolder {
            private static final Map<EventType, Class<? extends Event>> typeMap
                = new HashMap<EventType, Class<? extends Event>>() {{
                put(EventType.BUFFERSIZE, BufferSizeEvent.class);
                put(EventType.EOS, EOSEvent.class);
                put(EventType.LATENCY, LatencyEvent.class);
                put(EventType.FLUSH_START, FlushStartEvent.class);
                put(EventType.FLUSH_STOP, FlushStopEvent.class);
                put(EventType.NAVIGATION, NavigationEvent.class);
                put(EventType.NEWSEGMENT, NewSegmentEvent.class);
                put(EventType.SEEK, SeekEvent.class);
                put(EventType.TAG, TagEvent.class);
                put(EventType.QOS, QOSEvent.class);
            }};
            public static Class<? extends NativeObject> subtypeFor(Pointer ptr) {
                GstEventAPI.EventStruct struct = new GstEventAPI.EventStruct(ptr);
                EventType type = EventType.valueOf((Integer) struct.readField("type"));
                Class<? extends Event> eventClass = MapHolder.typeMap.get(type);
                return eventClass != null ? eventClass : Event.class;
            }
        }
        public Class<? extends NativeObject> subtypeFor(Pointer ptr) {
            return MapHolder.subtypeFor(ptr);
        }
    }
    private static class MessageMapper implements Mapper {
        static class MapHolder {
            private static final Map<MessageType, Class<? extends Message>> typeMap
                    = new HashMap<MessageType, Class<? extends Message>>() {{
                put(MessageType.EOS, EOSMessage.class);
                put(MessageType.ERROR, ErrorMessage.class);
                put(MessageType.BUFFERING, BufferingMessage.class);
                put(MessageType.DURATION, DurationMessage.class);
                put(MessageType.INFO, InfoMessage.class);
                put(MessageType.LATENCY, LatencyMessage.class);
                put(MessageType.SEGMENT_DONE, SegmentDoneMessage.class);
                put(MessageType.STATE_CHANGED, StateChangedMessage.class);
                put(MessageType.TAG, TagMessage.class);
                put(MessageType.WARNING, WarningMessage.class);
            }};
            public static Class<? extends NativeObject> subtypeFor(Pointer ptr) {
                GstMessageAPI.MessageStruct struct = new GstMessageAPI.MessageStruct(ptr);
                MessageType type = (MessageType) struct.readField("type");
                Class<? extends Message> messageClass = MapHolder.typeMap.get(type);
                return messageClass != null ? messageClass : Message.class;
            }
        }
        public Class<? extends NativeObject> subtypeFor(Pointer ptr) {
            return MapHolder.subtypeFor(ptr);
        }
    }
    private static class QueryMapper implements Mapper {
        static class MapHolder {
            private static final Map<QueryType, Class<? extends Query>> typeMap
                = new HashMap<QueryType, Class<? extends Query>>() {{
                put(QueryType.CONVERT, ConvertQuery.class);
                put(QueryType.DURATION, DurationQuery.class);
                put(QueryType.FORMATS, FormatsQuery.class);
                put(QueryType.LATENCY, LatencyQuery.class);
                put(QueryType.POSITION, PositionQuery.class);
                put(QueryType.SEEKING, SeekingQuery.class);
                put(QueryType.SEGMENT, SegmentQuery.class);
            }};
            public static Class<? extends NativeObject> subtypeFor(Pointer ptr) {
                GstQueryAPI.QueryStruct struct = new GstQueryAPI.QueryStruct(ptr);
                QueryType type = QueryType.valueOf((Integer) struct.readField("type"));
                Class<? extends Query> queryClass = typeMap.get(type);
                return queryClass != null ? queryClass : Query.class;
            }
        }
        public Class<? extends NativeObject> subtypeFor(Pointer ptr) {
            return MapHolder.subtypeFor(ptr);
        }
    }
}
