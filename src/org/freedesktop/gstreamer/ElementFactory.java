/* 
 * Copyright (c) 2019 Neil C Smith
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

import static org.freedesktop.gstreamer.lowlevel.GstElementFactoryAPI.GSTELEMENTFACTORY_API;
import static org.freedesktop.gstreamer.lowlevel.GstPadTemplateAPI.GSTPADTEMPLATE_API;
import static org.freedesktop.gstreamer.lowlevel.GstPluginAPI.GSTPLUGIN_API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;
import org.freedesktop.gstreamer.lowlevel.GstPadTemplateAPI.GstStaticPadTemplate;

import com.sun.jna.Pointer;
import org.freedesktop.gstreamer.glib.Natives;

/**
 * ElementFactory is used to create instances of elements.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstElementFactory.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstElementFactory.html</a>
 * <p>
 * Use the {@link #find} and {@link #create} methods to create element instances
 * or use {@link #make} as a convenient shortcut.
 *
 */
public class ElementFactory extends PluginFeature {

    public static final String GTYPE_NAME = "GstElementFactory";
    
    private static final Level DEBUG = Level.FINE;
    private static final Logger LOG = Logger.getLogger(ElementFactory.class.getName());

    /**
     * Creates a new instance of ElementFactory
     *
     * @param init internal initialization data.
     */
    ElementFactory(Initializer init) {
        super(init);
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("ElementFactory", "<init>", new Object[]{init});
        }
    }

    /**
     * Creates a new element from the factory.
     *
     * @param name the name to assign to the created Element
     * @return A new {@link Element}
     */
    public Element create(String name) {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("ElementFactory", "create", name);
        }
        Pointer elem = GSTELEMENTFACTORY_API.ptr_gst_element_factory_create(this, name);
        LOG.log(DEBUG, "gst_element_factory_create returned: " + elem);
        if (elem == null) {
            throw new IllegalArgumentException("Cannot create GstElement");
        }
        return elementFor(elem, getName());
    }

    /**
     * Returns the name of the person who wrote the factory.
     *
     * @return The name of the author
     */
    public String getAuthor() {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("ElementFactory", "getAuthor");
        }
        return GSTELEMENTFACTORY_API.gst_element_factory_get_metadata(this, "author");
    }

    /**
     * Returns a description of the factory.
     *
     * @return A brief description of the factory.
     */
    public String getDescription() {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("ElementFactory", "getDescription");
        }
        return GSTELEMENTFACTORY_API.gst_element_factory_get_metadata(this, "description");
    }

    /**
     * Returns a string describing the type of factory. This is an unordered
     * list separated with slashes ('/').
     *
     * @return The description of the type of factory.
     */
    public String getKlass() {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("ElementFactory", "getKlass");
        }
        return GSTELEMENTFACTORY_API.gst_element_factory_get_metadata(this, "klass");
    }

    /**
     * Returns the long, English name for the factory.
     *
     * @return The long, English name for the factory.
     */
    public String getLongName() {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("ElementFactory", "getLongName");
        }
        return GSTELEMENTFACTORY_API.gst_element_factory_get_metadata(this, "long-name");
    }

    /**
     * Gets the list of {@link StaticPadTemplate} for this factory.
     *
     * @return The list of {@link StaticPadTemplate}
     */
    public List<StaticPadTemplate> getStaticPadTemplates() {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("ElementFactory", "getStaticPadTemplates");
        }
        GList glist = GSTELEMENTFACTORY_API.gst_element_factory_get_static_pad_templates(this);
        LOG.log(DEBUG, "GSTELEMENTFACTORY_API.gst_element_factory_get_static_pad_templates returned: " + glist);
        List<StaticPadTemplate> templates = new ArrayList<StaticPadTemplate>();
        GList next = glist;
        while (next != null) {
            if (next.data != null) {
                GstStaticPadTemplate temp = new GstStaticPadTemplate(next.data);
                templates.add(new StaticPadTemplate(temp.getName(), temp.getPadDirection(),
                        temp.getPadPresence(), GSTPADTEMPLATE_API.gst_static_pad_template_get_caps(temp)));
            }
            next = next.next();
        }
        return templates;
    }

    /**
     * Retrieve an instance of a factory that can produce {@link Element}s
     *
     * @param name The type of {@link Element} to produce.
     * @return An ElementFactory that will produce {@link Element}s of the
     * desired type.
     */
    public static ElementFactory find(String name) {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("ElementFactory", "find", name);
        }
        ElementFactory factory = GSTELEMENTFACTORY_API.gst_element_factory_find(name);
        if (factory == null) {
            throw new IllegalArgumentException("No such Gstreamer factory: " + name);
        }
        return factory;
    }

//    /**
//     * Filter out all the elementfactories in list that can handle caps in the
//     * given direction.
//     *
//     * If subsetonly is true, then only the elements whose pads templates are a
//     * complete superset of caps will be returned. Else any element whose pad
//     * templates caps can intersect with caps will be returned.
//     *
//     * @param list a {@link List} of {@link ElementFactory} to filter
//     * @param caps a {@link Caps}
//     * @param direction a {@link PadDirection} to filter on
//     * @param subsetonly whether to filter on caps subsets or not.
//     * @return a {@link List} of {@link ElementFactory} elements that match the
//     * given requisits.
//     */
//    public static List<ElementFactory> listFilter(List<ElementFactory> list, Caps caps,
//            PadDirection direction, boolean subsetonly) {
//        GList glist = null;
//        List<ElementFactory> filterList = new ArrayList<ElementFactory>();
//
//        for (ElementFactory fact : list) {
//            fact.ref();
//            glist = GLIB_API.g_list_append(glist, fact.handle());
//        }
//
//        GList gFilterList = GSTELEMENTFACTORY_API.gst_element_factory_list_filter(glist, caps, direction, subsetonly);
//
//        GList next = gFilterList;
//        while (next != null) {
//            if (next.data != null) {
//                ElementFactory fact = new ElementFactory(initializer(next.data, true, true));
//                filterList.add(fact);
//            }
//            next = next.next();
//        }
//
//        GSTPLUGIN_API.gst_plugin_list_free(glist);
//        GSTPLUGIN_API.gst_plugin_list_free(gFilterList);
//
//        return filterList;
//    }

    /**
     * Get a list of factories that match the given type. Only elements with a
     * rank greater or equal to minrank will be returned. The list of factories
     * is returned by decreasing rank.
     *
     * @param type a {@link ListType}
     * @param minrank Minimum rank
     * @return a List of ElementFactory elements.
     */
    public static List<ElementFactory> listGetElements(ListType type, Rank minrank) {
        GList glist = GSTELEMENTFACTORY_API.gst_element_factory_list_get_elements(type.getValue(), minrank.intValue());
        List<ElementFactory> list = new ArrayList<ElementFactory>();

        GList next = glist;
        while (next != null) {
            if (next.data != null) {
                ElementFactory fact = new ElementFactory(Natives.initializer(next.data, true, true));
                list.add(fact);
            }
            next = next.next();
        }

        GSTPLUGIN_API.gst_plugin_list_free(glist);

        return list;
    }

    /**
     * Get a list of factories that match the given parameter.
     *
     * It is a combination of listGetElement and listFilter passing all the
     * results of the first call to the second.
     *
     * This method improves performance because there is no need to map to java
     * list the elements returned by the first call.
     *
     * @param type a {@link ListType}
     * @param minrank Minimum rank
     * @param caps a {@link Caps}
     * @param direction a {@link PadDirection} to filter on
     * @param subsetonly whether to filter on caps subsets or not.
     * @return a {@link List} of {@link ElementFactory} elements that match the
     * given requisits.
     */
    public static List<ElementFactory> listGetElementsFilter(ListType type, Rank minrank,
            Caps caps, PadDirection direction, boolean subsetonly) {
        List<ElementFactory> filterList = new ArrayList<ElementFactory>();

        GList glist = GSTELEMENTFACTORY_API.gst_element_factory_list_get_elements(type.getValue(), minrank.intValue());

        GList gFilterList = GSTELEMENTFACTORY_API.gst_element_factory_list_filter(glist, caps, direction, subsetonly);

        GList next = gFilterList;
        while (next != null) {
            if (next.data != null) {
                ElementFactory fact = new ElementFactory(Natives.initializer(next.data, true, true));
                filterList.add(fact);
            }
            next = next.next();
        }

        GSTPLUGIN_API.gst_plugin_list_free(glist);
        GSTPLUGIN_API.gst_plugin_list_free(gFilterList);

        return filterList;
    }

    /**
     * Creates a new Element from the specified factory.
     *
     * @param factoryName The name of the factory to use to produce the Element
     * @param name The name to assign to the created Element
     * @return A new GstElemElement
     */
    public static Element make(String factoryName, String name) {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("ElementFactory", "make", new Object[]{factoryName, name});
        }
        return elementFor(makeRawElement(factoryName, name), factoryName);
    }

    static Pointer makeRawElement(String factoryName, String name) {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.entering("ElementFactory", "makeRawElement", new Object[]{factoryName, name});
        }
        Pointer elem = GSTELEMENTFACTORY_API.ptr_gst_element_factory_make(factoryName, name);
        LOG.log(DEBUG, "Return from gst_element_factory_make=" + elem);
        if (elem == null) {
            throw new IllegalArgumentException("No such Gstreamer factory: "
                    + factoryName);
        }
        return elem;
    }

    private static Element elementFor(Pointer ptr, String factoryName) {
        return Natives.objectFor(ptr, Element.class, false, true);
    }

    /**
     * The type of ElementFactory to filter.
     */
    public enum ListType {

        /** Decoder elements */
        DECODER((long) 1 << 0),
        /** Encoder elements */
        ENCODER((long) 1 << 1),
        /** Sink elements */
        SINK((long) 1 << 2),
        /** Source elements */
        SRC((long) 1 << 3),
        /** Muxer elements */
        MUXER((long) 1 << 4),
        /** Demuxer elements */
        DEMUXER((long) 1 << 5),
        /** Parser elements */        
        PARSER((long) 1 << 6),
        /** Payloader elements */
        PAYLOADER((long) 1 << 7),
        /** Depayloader elements */
        DEPAYLOADER((long) 1 << 8),
        /** Formatter elements */
        FORMATTER((long) 1 << 9),
        /** Decryptor elements */
        DECRYPTOR((long) 1 << 10),
        /** Encryptor elements */
        ENCRYPTOR((long) 1 << 11),
//        /** Private, do not use */
//        MAX_ELEMENTS((long) 1 << 48),
        /** Any elements */
        ANY((((long) 1) << 49) - 1),
        /** Any of the defined media element types */
        MEDIA_ANY(~((long) 0) << 48),
        /** Elements handling video media types */
        MEDIA_VIDEO((long) 1 << 49),
        /** Elements handling audio media types */
        MEDIA_AUDIO((long) 1 << 50),
        /** Elements handling image media types */
        MEDIA_IMAGE((long) 1 << 51),
        /** Elements handling subtitle media types */
        MEDIA_SUBTITLE((long) 1 << 52),
        /** Elements handling metadata media types */
        MEDIA_METADATA((long) 1 << 53),
        /** All encoders handling video or image media types */
        VIDEO_ENCODER(ENCODER.getValue() | MEDIA_VIDEO.getValue() | MEDIA_IMAGE.getValue()),
        /** All encoders handling audio media types */
        AUDIO_ENCODER(ENCODER.getValue() | MEDIA_AUDIO.getValue()),
        /** All sinks handling audio, video or image media types */
        AUDIOVIDEO_SINKS(SINK.getValue() | MEDIA_AUDIO.getValue() | MEDIA_VIDEO.getValue() | MEDIA_IMAGE.getValue()),
        /** All elements used to 'decode' streams (decoders, demuxers, parsers, depayloaders) */
        DECODABLE(DECODER.getValue() | DEMUXER.getValue() | DEPAYLOADER.getValue() | PARSER.getValue());

        private final long value;

        private ListType(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }
    }
}
