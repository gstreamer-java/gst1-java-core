/* 
 * Copyright (c) 2020 Neil C Smith
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
package org.freedesktop.gstreamer.message;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.MiniObject;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.glib.NativeEnum;
import org.freedesktop.gstreamer.glib.Natives;
import org.freedesktop.gstreamer.lowlevel.GstMessagePtr;
import org.freedesktop.gstreamer.lowlevel.ReferenceManager;
import org.freedesktop.gstreamer.lowlevel.annotations.HasSubtype;

import static org.freedesktop.gstreamer.lowlevel.GstMessageAPI.GSTMESSAGE_API;

/**
 * Lightweight objects to signal the occurrence of pipeline events.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstMessage.html</a>
 * <p>
 * Messages are implemented as a subclass of {@link MiniObject} with a generic
 * {@link Structure} as the content. This allows for writing custom messages
 * without requiring an API change while allowing a wide range of different
 * types of messages.
 * <p>
 * Messages are posted by objects in the pipeline and are passed to the
 * application using the {@link Bus}.
 *
 * The basic use pattern of posting a message on a Bus is as follows:
 *
 * <example>
 * <title>Posting a Message</title>
 * <code>
 *    bus.post(new EOSMessage(source));
 * </code>
 * </example>
 *
 * An {@link Element} usually posts messages on the bus provided by the parent
 * container using {@link Element#postMessage postMessage()}.
 */
@HasSubtype
public class Message extends MiniObject {

    public static final String GTYPE_NAME = "GstMessage";

    private static final Map<MessageType, Function<Initializer, Message>> TYPE_MAP
            = new EnumMap<>(MessageType.class);

    static {
        TYPE_MAP.put(MessageType.EOS, EOSMessage::new);
        TYPE_MAP.put(MessageType.ERROR, ErrorMessage::new);
        TYPE_MAP.put(MessageType.BUFFERING, BufferingMessage::new);
        TYPE_MAP.put(MessageType.DURATION_CHANGED, DurationChangedMessage::new);
        TYPE_MAP.put(MessageType.INFO, InfoMessage::new);
        TYPE_MAP.put(MessageType.LATENCY, LatencyMessage::new);
        TYPE_MAP.put(MessageType.SEGMENT_DONE, SegmentDoneMessage::new);
        TYPE_MAP.put(MessageType.STATE_CHANGED, StateChangedMessage::new);
        TYPE_MAP.put(MessageType.TAG, TagMessage::new);
        TYPE_MAP.put(MessageType.WARNING, WarningMessage::new);
        TYPE_MAP.put(MessageType.NEED_CONTEXT, NeedContextMessage::new);
    }

    private final Handle handle;

    /**
     * Creates a new instance of Message.
     *
     * @param init internal initialization data.
     */
    Message(Initializer init) {
        this(new Handle(init.ptr.as(GstMessagePtr.class, GstMessagePtr::new), init.ownsHandle), init.needRef);
    }

    Message(Handle handle, boolean needRef) {
        super(handle, needRef);
        this.handle = handle;
    }

    /**
     * Gets the Element that posted this message.
     *
     * @return the element that posted the message.
     */
    public GstObject getSource() {
        return Natives.objectFor(handle.getPointer().getSource(), GstObject.class, true, true);
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
        return NativeEnum.fromInt(MessageType.class, MessageType.UNKNOWN,
                handle.getPointer().getMessageType());
    }

    private static Message create(Initializer init) {
        MessageType type = NativeEnum.fromInt(MessageType.class,
                MessageType.UNKNOWN,
                init.ptr.as(GstMessagePtr.class, GstMessagePtr::new).getMessageType()
        );
        return TYPE_MAP.getOrDefault(type, Message::new).apply(init);
    }

    public static class Types implements TypeProvider {

        @Override
        public Stream<TypeRegistration<?>> types() {
            return Stream.of(
                    Natives.registration(Message.class, GTYPE_NAME, Message::create)
            );
        }

    }

    static class Handle extends MiniObject.Handle {

        Handle(GstMessagePtr ptr, boolean ownsHandle) {
            super(ptr, ownsHandle);
        }

        @Override
        protected GstMessagePtr getPointer() {
            return (GstMessagePtr) super.getPointer();
        }

    }

}
