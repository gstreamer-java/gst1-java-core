/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2016 Christophe Lafolet
 * Copyright (c) 2014 Tom Greenwood <tgreenwood@cafex.com>
 * Copyright (c) 2007 Wayne Meissner
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
package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GlibAPI.GLIB_API;
import static org.freedesktop.gstreamer.lowlevel.GstTagAPI.GSTTAG_API;
import static org.freedesktop.gstreamer.lowlevel.GstTagListAPI.GSTTAGLIST_API;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.freedesktop.gstreamer.glib.GDate;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GstTagListAPI;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import org.freedesktop.gstreamer.glib.NativeObject;
import org.freedesktop.gstreamer.glib.Natives;

/**
 * List of tags and values used to describe media metadata.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstTagList.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstTagList.html</a>
 * <p>
 */
//@TODO consider handling Sample fields.
public class TagList extends MiniObject {

    public static final String GTYPE_NAME = "GstTagList";
    
    /**
     * Creates a new instance of TagList
     *
     * @param init internal initialization data.
     */
    TagList(Initializer init) {
        super(init);
    }

    /**
     * Constructs a new empty tag list.
     */
    public TagList() {
        super(initializer());
    }

    private static Initializer initializer() {
        final Pointer ptr_new_tag_list = GSTTAGLIST_API.ptr_gst_tag_list_new_empty();
        return Natives.initializer(ptr_new_tag_list);
    }

    /**
     * Gets the number of values of type {@code tag} stored in the list.
     *
     * @param tag the name of the tag to get the size of.
     * @return the number of values for {@code tag} in this list.
     */
    public int getValueCount(String tag) {
        return GSTTAGLIST_API.gst_tag_list_get_tag_size(this, tag);
    }

    /**
     * Gets all data values for a tag contained in this list.
     *
     * @param tag the name of the tag to retrieve.
     * @return the data associated with {@code tag}.
     */
    public List<Object> getValues(final String tag) {
        final int size = getValueCount(tag);
        return new AbstractList<Object>() {
            public int size() {
                return size;
            }

            @Override
            public Object get(int index) {
                return getValue(tag, index);
            }
        };
    }

//    /**
//     * Gets all data values for a tag contained in this list.
//     *
//     * @param tag the name of the tag to retrieve.
//     * @return the data associated with {@code tag}.
//     */
//    public List<Object> getValues(Tag tag) {
//        return getValues(tag.getId());
//    }

    /**
     * Gets data for a tag from this list.
     *
     * @param tag the tag to retrieve.
     * @param index which element of the array of data for this tag to retrieve.
     * @return the data for the tag.
     */
    public Object getValue(String tag, int index) {
        TagGetter get = MapHolder.getterMap.get(getTagType(tag));
        return get != null ? get.get(this, tag, index) : "";
    }

//    /**
//     * Gets data for a tag from this list.
//     *
//     * @param tag the tag to retrieve.
//     * @param index which element of the array of data for this tag to retrieve.
//     * @return the data for the tag.
//     */
//    public Object getValue(Tag tag, int index) {
//        return getValue(tag.getId(), index);
//    }

    /**
     * Gets a string tag from this list.
     *
     * @param tag the tag to retrieve.
     * @param index which element of the array of data for this tag to retrieve.
     * @return the data for the tag.
     */
    public String getString(String tag, int index) {
        return getValue(tag, index).toString();
    }

//    /**
//     * Gets a string tag from this list.
//     *
//     * @param tag the tag to retrieve.
//     * @param index which element of the array of data for this tag to retrieve.
//     * @return the data for the tag.
//     */
//    public String getString(Tag tag, int index) {
//        return getString(tag.getId(), index);
//    }

    /**
     * Gets a numeric tag from this list.
     *
     * @param tag the tag to retrieve.
     * @param index which element of the array of data for this tag to retrieve.
     * @return the data for the tag.
     */
    public Number getNumber(String tag, int index) {
        Object data = getValue(tag, index);
        if (!(data instanceof Number)) {
            throw new IllegalArgumentException("Tag [" + tag + "] is not a number");
        }
        return (Number) data;
    }

//    /**
//     * Gets a numeric tag from this list.
//     *
//     * @param tag the tag to retrieve.
//     * @param index which element of the array of data for this tag to retrieve.
//     * @return the data for the tag.
//     */
//    public Number getNumber(Tag tag, int index) {
//        return getNumber(tag.getId(), index);
//    }

    /**
     * Gets a list of all the tags contained in this list.
     *
     * @return a list of tag names.
     */
    public List<String> getTagNames() {
        final List<String> list = new LinkedList<String>();
        GSTTAGLIST_API.gst_tag_list_foreach(this, new GstTagListAPI.TagForeachFunc() {
            public void callback(Pointer ptr, String tag, Pointer user_data) {
                list.add(tag);
            }
        }, null);
        return list;
    }

    /**
     * Merges this tag list and {@code list2} into a new list. If {@code list2}
     * is null, a copy of this list is returned.
     *
     * @param list2 the other tag list to merge with this one.
     * @param mode the {@link TagMergeMode}.
     * @return a new tag list.
     */
    public TagList merge(TagList list2, TagMergeMode mode) {
        return GSTTAGLIST_API.gst_tag_list_merge(this, list2, mode);
    }

    /**
     * Gets the low level type for the tag.
     *
     * @param tag the tag
     * @return the type of {@code tag}
     */
    private static GType getTagType(String tag) {

        GType type = MapHolder.tagTypeMap.get(tag);
        if (type != null) {
            return type;
        }
        MapHolder.tagTypeMap.put(tag, type = GSTTAG_API.gst_tag_get_type(tag));
        return type;
    }

    private static interface TagGetter {

        Object get(TagList tl, String tag, int index);
    }

    //
    // Put the maps in a holder class so they don't get initialized until used.
    // This helps avoid calling gstreamer methods before Gst.init().
    //
    private static final class MapHolder {

        private static final Map<GType, TagGetter> getterMap = new HashMap<GType, TagGetter>() {
            {
                put(GType.INT, new TagGetter() {
                    public Object get(TagList tl, String tag, int index) {
                        int[] value = {0};
                        GSTTAGLIST_API.gst_tag_list_get_int_index(tl, tag, index, value);
                        return value[0];
                    }
                });
                put(GType.UINT, new TagGetter() {
                    public Object get(TagList tl, String tag, int index) {
                        int[] value = {0};
                        GSTTAGLIST_API.gst_tag_list_get_uint_index(tl, tag, index, value);
                        return value[0];
                    }
                });
                put(GType.INT64, new TagGetter() {
                    public Object get(TagList tl, String tag, int index) {
                        long[] value = {0};
                        GSTTAGLIST_API.gst_tag_list_get_int64_index(tl, tag, index, value);
                        return value[0];
                    }
                });
                put(GType.DOUBLE, new TagGetter() {
                    public Object get(TagList tl, String tag, int index) {
                        double[] value = {0d};
                        GSTTAGLIST_API.gst_tag_list_get_double_index(tl, tag, index, value);
                        return value[0];
                    }
                });
                put(GType.STRING, new TagGetter() {
                    public Object get(TagList tl, String tag, int index) {
                        Pointer[] value = {null};
                        GSTTAGLIST_API.gst_tag_list_get_string_index(tl, tag, index, value);
                        if (value[0] == null) {
                            return null;
                        }
                        String ret = value[0].getString(0);
                        GLIB_API.g_free(value[0]);
                        return ret;
                    }
                });
                put(GType.valueOf(GDate.GTYPE_NAME), new TagGetter() {
                    public Object get(TagList tl, String tag, int index) {
                        PointerByReference value = new PointerByReference();
                        GSTTAGLIST_API.gst_tag_list_get_date_index(tl, tag, index, value);
                        if (value.getValue() == null) {
                            return null;
                        }
                        return Natives.objectFor(value.getValue(), GDate.class, false, true);
                    }
                });
                put(GType.valueOf(DateTime.GTYPE_NAME), new TagGetter() {
                    public Object get(TagList tl, String tag, int index) {
                        PointerByReference value = new PointerByReference();
                        GSTTAGLIST_API.gst_tag_list_get_date_time_index(tl, tag, index, value);
                        if (value.getValue() == null) {
                            return null;
                        }
                        return new DateTime(value.getValue(), false, true);
                    }
                });

            }
        };
        static private final Map<String, GType> tagTypeMap = new ConcurrentHashMap<String, GType>();
    }
}
