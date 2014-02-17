/* 
 * Copyright (c) 2009 Levente Farkas
 * Copyright (C) 2007, 2008 Wayne Meissner
 * Copyright (C) 1999,2000 Erik Walthinsen <omega@cse.ogi.edu>
 *                    2000 Wim Taymans <wtay@chello.be>
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

import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.GstPadTemplateAPI;

/**
 * Padtemplates describe the possible media types a {@link Pad} or an 
 * {@link ElementFactory} can handle. This allows for both inspection of 
 * handled types before loading the element plugin as well as identifying pads 
 * on elements that are not yet created (request or sometimes pads).
 *
 * Pad and PadTemplates have {@code Caps} attached to it to describe the media type
 * they are capable of dealing with. {@link #getCaps} is used to get the caps of
 * a padtemplate. It is not possible to modify the caps of a padtemplate after 
 * creation.
 */
public class PadTemplate extends GstObject {
    public static final String GTYPE_NAME = "GstPadTemplate";
    
    private static final GstPadTemplateAPI gst = GstNative.load(GstPadTemplateAPI.class);

    /** 
     * Creates a new proxy for PadTemplate.
     * <p> This is only for internal use.
     * 
     * @param init internal initialization data.
     */
    public PadTemplate(Initializer init) {
        super(init);
    }
    
    /**
     * Creates a new pad template with a name according to the given template
     * and with the given arguments.
     * 
     * @param nameTemplate the name template.
     * @param direction the direction of the template.
     * @param caps a {@code Caps} set for the template.
     */
    public PadTemplate(String nameTemplate, PadDirection direction, Caps caps) {
        this(initializer(gst.ptr_gst_pad_template_new(nameTemplate, direction, PadPresence.ALWAYS, caps)));
    }
    /**
     * Creates a new pad template with a name according to the given template
     * and with the given arguments.
     * 
     * @param nameTemplate the name template.
     * @param direction the direction of the template.
     * @param presence the presence of the pad, which controls the lifetime.
     * @param caps a {@code Caps} set for the template.
     */
    public PadTemplate(String nameTemplate, PadDirection direction, PadPresence presence, Caps caps) {
        this(initializer(gst.ptr_gst_pad_template_new(nameTemplate, direction, presence, caps)));
    }
    
    /**
     * Gets the {@link Caps} set on this {@code PadTemplate}
     * 
     * @return the media type on this template.
     */
    public Caps getCaps() {
        return gst.gst_pad_template_get_caps(this);
    }
}
