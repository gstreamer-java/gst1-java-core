/* 
 * Copyright (C) 2019 Neil C Smith
 * Copyright (C) 2008 Wayne Meissner
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
package org.freedesktop.gstreamer.query;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstQueryAPI;
import org.freedesktop.gstreamer.lowlevel.ReferenceManager;
import org.freedesktop.gstreamer.lowlevel.annotations.HasSubtype;

import static org.freedesktop.gstreamer.lowlevel.GstQueryAPI.GSTQUERY_API;

/**
 * Base query type. Queries can be performed on {@link Pad} and {@link Element}.
 * Some queries might need a running pipeline to work.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstQuery.html</a>
 */
@HasSubtype
public class Query extends MiniObject {

    public static final String GTYPE_NAME = "GstQuery";

    private static final Map<QueryType, Function<Initializer, Query>> TYPE_MAP
            = new EnumMap<>(QueryType.class);

    static {
        TYPE_MAP.put(QueryType.ALLOCATION, AllocationQuery::new);
        TYPE_MAP.put(QueryType.CONVERT, ConvertQuery::new);
        TYPE_MAP.put(QueryType.DURATION, DurationQuery::new);
        TYPE_MAP.put(QueryType.FORMATS, FormatsQuery::new);
        TYPE_MAP.put(QueryType.LATENCY, LatencyQuery::new);
        TYPE_MAP.put(QueryType.POSITION, PositionQuery::new);
        TYPE_MAP.put(QueryType.SEEKING, SeekingQuery::new);
        TYPE_MAP.put(QueryType.SEGMENT, SegmentQuery::new);
    }

    /**
     * Internally used constructor. Do not use.
     *
     * @param init internal initialization data.
     */
    Query(Initializer init) {
        super(init);
    }

    /**
     * Get the structure of this query.
     *
     * @return The structure of this Query.
     */
    public Structure getStructure() {
        return ReferenceManager.addKeepAliveReference(GSTQUERY_API.gst_query_get_structure(this), this);
    }

    private static Query create(Initializer init) {
        GstQueryAPI.QueryStruct struct = new GstQueryAPI.QueryStruct(init.ptr.getPointer());
        QueryType type = (QueryType) struct.readField("type");
        return TYPE_MAP.getOrDefault(type, Query::new).apply(init);
    }
    
    public static class Types implements TypeProvider {

        @Override
        public Stream<TypeRegistration<?>> types() {
            return Stream.of(
                    Natives.registration(Query.class, GTYPE_NAME, Query::create)
            );
        }

    }

}
