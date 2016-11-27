/* 
 * Copyright (c) 2007, 2008 Wayne Meissner
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

package org.freedesktop.gstreamer;

import org.freedesktop.gstreamer.lowlevel.GstMessageAPI;
import org.freedesktop.gstreamer.lowlevel.ReferenceManager;
import org.freedesktop.gstreamer.lowlevel.annotations.HasSubtype;

import static org.freedesktop.gstreamer.lowlevel.GstMessageAPI.GSTMESSAGE_API;

/**
 * Lightweight objects to signal the occurrence of pipeline events.
 * 
 * <p> Messages are implemented as a subclass of {@link MiniObject} with a generic
 * {@link Structure} as the content. This allows for writing custom messages without
 * requiring an API change while allowing a wide range of different types
 * of messages.
 *
 * <p> Messages are posted by objects in the pipeline and are passed to the
 * application using the {@link Bus}.

 * The basic use pattern of posting a message on a Bus is as follows:
 *
 * <example>
 * <title>Posting a Message</title>
 *   <code>
 *    bus.post(new EOSMessage(source));
 *   </code>
 * </example>
 *
 * An {@link Element} usually posts messages on the bus provided by the parent
 * container using {@link Element#postMessage postMessage()}.
 */
@HasSubtype
public class Message extends MiniObject {
    public static final String GTYPE_NAME = "GstMessage";

    protected GstMessageAPI.MessageStruct messageStruct;
    
    /**
     * Creates a new instance of Message.
     * 
     * @param init internal initialization data.
     */
    public Message(Initializer init) {
        super(init);
        messageStruct = new GstMessageAPI.MessageStruct(handle());
    }
    
    /**
     * Gets the Element that posted this message.
     * 
     * @return the element that posted the message.
     */
    public GstObject getSource() {
        return (GstObject)messageStruct.readField("src");
    }
    
    /**
     * Gets the structure containing the data in this message.
     * 
     * @return a structure.
     */
    public Structure getStructure() {
        return ReferenceManager.addKeepAliveReference(GSTMESSAGE_API.gst_message_get_structure(this), this);
    }
    
    /**
     * Gets the type of this message.
     * 
     * @return the message type.
     */
    public MessageType getType() {
        MessageType type = (MessageType)messageStruct.readField("type");
		return type;
    }
    
}
