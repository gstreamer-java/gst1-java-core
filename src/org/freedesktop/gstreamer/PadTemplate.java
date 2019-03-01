/* 
 * Copyright (c) 2019 Neil C Smith
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
package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstPadTemplateAPI.GSTPADTEMPLATE_API;

/**
 * Padtemplates describe the possible media types a {@link Pad} or an
 * {@link ElementFactory} can handle. This allows for both inspection of handled
 * types before loading the element plugin as well as identifying pads on
 * elements that are not yet created (request or sometimes pads).
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPadTemplate.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstPadTemplate.html</a>
 * <p>
 * Pad and PadTemplates have {@code Caps} attached to it to describe the media
 * type they are capable of dealing with. {@link #getCaps} is used to get the
 * caps of a PadTemplate. It is not possible to modify the caps of a PadTemplate
 * after creation.
 */
public class PadTemplate extends GstObject {

    public static final String GTYPE_NAME = "GstPadTemplate";

    /**
     * Creates a new proxy for PadTemplate.
     * <p>
     * This is only for internal use.
     *
     * @param init internal initialization data.
     */
    PadTemplate(Initializer init) {
        super(init);
    }

    /**
     * Creates a new pad template with a name according to the given template,
     * with the given arguments and {@link PadPresence#ALWAYS }
     *
     * @param nameTemplate the name template.
     * @param direction the direction of the template.
     * @param caps a {@code Caps} set for the template.
     */
    public PadTemplate(String nameTemplate, PadDirection direction, Caps caps) {
        this(Natives.initializer(GSTPADTEMPLATE_API.ptr_gst_pad_template_new(nameTemplate, direction, PadPresence.ALWAYS, caps)));
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
        this(Natives.initializer(GSTPADTEMPLATE_API.ptr_gst_pad_template_new(nameTemplate, direction, presence, caps)));
    }

    /**
     * Gets the {@link Caps} set on this {@code PadTemplate}
     *
     * @return the media type on this template.
     */
    public Caps getCaps() {
        return GSTPADTEMPLATE_API.gst_pad_template_get_caps(this);
    }

    /**
     * Gets the name of this template
     *
     * @return the name of this template
     */
    public String getTemplateName() {
        return get("name-template").toString();
    }

    /**
     * Gets the direction of this template
     *
     * @return {@link PadDirection}
     */
    public PadDirection getDirection() {
        Object d = get("direction");
        if (d instanceof Number) {
            return PadDirection.values()[((Number) d).intValue()];
        }
        return null;
    }

    /**
     * Gets presence of this template
     *
     * @return {@link PadPresence}
     */
    public PadPresence getPresence() {
        Object p = get("presence");
        if (p instanceof Number) {
            return PadPresence.values()[((Number) p).intValue()];
        }
        return null;
    }
}
