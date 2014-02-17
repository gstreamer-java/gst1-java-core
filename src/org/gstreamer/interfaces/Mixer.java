/* 
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2008 Wayne Meissner
 * Copyright (C) 2003 Ronald Bultje <rbultje@ronald.bitfreak.net>
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

package org.gstreamer.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.gstreamer.Element;
import org.gstreamer.lowlevel.GlibAPI.GList;

import static org.gstreamer.lowlevel.GstMixerAPI.GSTMIXER_API;

/**
 * Interface for elements that provide mixer operations
 */
public class Mixer extends GstInterface {
    /**
     * Wraps the {@link Element} in a <tt>Mixer</tt> interface
     * 
     * @param element the element to use as a <tt>Mixer</tt>
     * @return a <tt>Mixer</tt> for the element
     */
    public static final Mixer wrap(Element element) {
        return new Mixer(element);
    }
    
    /**
     * Creates a new Mixer instance
     * 
     * @param element the element that implements the mixer interface
     */
    private Mixer(Element element) {
        super(element, GSTMIXER_API.gst_mixer_get_type());
    }
    
    /**
     * Gets a list of available tracks for this mixer/element.
     * <p>
     * Note that it is allowed for sink (output) elements to only provide
     * the output tracks in this list. Likewise, for sources (inputs),
     * it is allowed to only provide input elements in this list.
     * </p>
     * 
     * @return a list of MixerTrack instances
     */
    public List<MixerTrack> getTracks() {
        return trackList(GSTMIXER_API.gst_mixer_list_tracks(this), true, true);
    }
    
    /**
     * Build a {@link java.util.List} of {@link Object} from the native GList.
     * @param glist The native list to get the objects from.
     * @param objectClass The proxy class to wrap the list elements in.
     * @return The converted list.
     */
    private List<MixerTrack> trackList(GList glist, boolean needRef, boolean ownsHandle) {
        List<MixerTrack> list = new ArrayList<MixerTrack>();
        GList next = glist;
        while (next != null) {
            if (next.data != null) {
                list.add(new MixerTrack(this, next.data, needRef, ownsHandle));
            }
            next = next.next();   
        }
        return list;
    }
}
