/*
 * Copyright (c) 2021 Neil C Smith
 * Copyright (c) 2014 Tom Greenwood <tgreenwood@cafex.com>
 * Copyright (c) 2009 Andres Colubri
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
package org.freedesktop.gstreamer.elements;

import java.io.File;
import java.net.URI;
import java.util.EnumSet;
import java.util.Set;
import org.freedesktop.gstreamer.Bus;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.TagList;
import org.freedesktop.gstreamer.glib.NativeFlags;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

/**
 * <p>
 * Playbin provides a stand-alone everything-in-one abstraction for an audio
 * and/or video player.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/documentation/playback/playbin.html"
 * >https://gstreamer.freedesktop.org/documentation/playback/playbin.html</a>
 * <p>
 * <p>
 * It can handle both audio and video files and features
 * </p>
 * <ul>
 * <li>
 * automatic file type recognition and based on that automatic selection and
 * usage of the right audio/video/subtitle demuxers/decoders
 * </li>
 * <li>
 * visualisations for audio files
 * </li>
 * <li>
 * subtitle support for video files. Subtitles can be store in external files.
 * </li>
 * <li>
 * stream selection between different video/audio/subtitles streams
 * </li>
 * <li>
 * meta info (tag) extraction
 * </li>
 * <li>
 * easy access to the last video frame
 * </li>
 * <li>
 * buffering when playing streams over a network
 * </li>
 * <li>
 * volume control with mute option
 * </li>
 * </ul>
 * <p>
 * playbin is a {@link Pipeline}. It will notify the application of everything
 * that's happening (errors, end of stream, tags found, state changes, etc.) by
 * posting messages on its {@link Bus}. The application needs to watch the bus.
 * <p>
 * Playback can be initiated by setting the playbin to PLAYING state using
 * {@link #setState setState} or {@link #play play}. Note that the state change
 * will take place in the background in a separate thread, when the function
 * returns playback is probably not happening yet and any errors might not have
 * occured yet. Applications using playbin should ideally be written to deal
 * with things completely asynchroneous.
 * <p>
 * When playback has finished (an EOS message has been received on the bus) or
 * an error has occured (an ERROR message has been received on the bus) or the
 * user wants to play a different track, playbin should be set back to READY or
 * NULL state, then the input file/URI should be set to the new location and
 * then playbin be set to PLAYING state again.
 * <p>
 * Seeking can be done using {@link #seek seek} on the playbin element. Again,
 * the seek will not be executed instantaneously, but will be done in a
 * background thread. When the seek call returns the seek will most likely still
 * be in process. An application may wait for the seek to finish (or fail) using
 * {@link #getState(long)} with -1 as the timeout, but this will block the user
 * interface and is not recommended at all.
 * <p>
 * Applications may query the current position and duration of the stream via
 * {@link #queryPosition} and {@link #queryDuration} and setting the format
 * passed to {@link Format#TIME}. If the query was successful, the duration or
 * position will have been returned in units of nanoseconds.
 */
public class PlayBin extends Pipeline {

    public static final String GST_NAME = "playbin";
    public static final String GTYPE_NAME = "GstPlayBin";

    /**
     * Creates a new PlayBin.
     *
     * @param name The name used to identify this pipeline.
     */
    public PlayBin(String name) {
        this(makeRawElement(GST_NAME, name));
    }

    /**
     * Creates a new PlayBin.
     *
     * @param name The name used to identify this pipeline.
     * @param uri The URI of the media file to load.
     */
    public PlayBin(String name, URI uri) {
        this(name);
        setURI(uri);
    }

    /**
     * Creates a new PlayBin proxy.
     *
     * @param init proxy initialization args
     *
     */
    PlayBin(Initializer init) {
        super(init);
    }

    /**
     * Sets the media file to play.
     *
     * @param file The {@link java.io.File} to play.
     */
    public void setInputFile(File file) {
        setURI(file.toURI());
    }

    /**
     * Sets the media URI to play.
     *
     * @param uri The {@link java.net.URI} to play.
     */
    public void setURI(URI uri) {
        set("uri", uri);
    }

    /**
     * Sets the audio output Element.
     * <p>
     * To disable audio output, call this method with a <tt>null</tt> argument.
     *
     * @param element The element to use for audio output.
     */
    public void setAudioSink(Element element) {
        setElement("audio-sink", element);
    }

    /**
     * Sets the video output Element.
     * <p>
     * To disable video output, call this method with a <tt>null</tt> argument.
     *
     * @param element The element to use for video output.
     */
    public void setVideoSink(Element element) {
        setElement("video-sink", element);
    }

    /**
     * Sets the text output Element.
     * <p>
     * To disable text output, call this method with a <tt>null</tt> argument.
     *
     * @param element The element to use for text output.
     */
    public void setTextSink(Element element) {
        setElement("text-sink", element);
    }

    /**
     * Sets the visualization output Element.
     *
     * @param element The element to use for visualization.
     */
    public void setVisualization(Element element) {
        setElement("vis-plugin", element);
    }

    /**
     * Sets an output {@link Element} on the PlayBin.
     *
     * @param key The name of the output to change.
     * @param element The Element to set as the output.
     */
    private void setElement(String key, Element element) {
        set(key, element);
    }

    /**
     * Set the flags to control the behaviour of this PlayBin.
     * <p>
     * The default value is
     * soft-colorbalance+deinterlace+soft-volume+text+audio+video
     * <p>
     * See individual {@link PlayFlags} value descriptions for more information.
     *
     * @param flags set of {@link PlayFlags}
     */
    public void setFlags(Set<PlayFlags> flags) {
        set("flags", NativeFlags.toInt(flags));
    }

    /**
     * Get the current flags set on this PlayBin. The returned set will be
     * writable so that it can be altered and passed back to
     * {@link #setFlags(java.util.Set)}.
     *
     * @return set of current {@link PlayFlags}
     */
    public Set<PlayFlags> getFlags() {
        Object flags = get("flags");
        if (flags instanceof Number) {
            return NativeFlags.fromInt(PlayFlags.class,
                    ((Number) flags).intValue());
        } else {
            return EnumSet.noneOf(PlayFlags.class);
        }
    }

    /**
     * Sets the audio playback volume.
     *
     * @param volume value between 0.0 and 1.0 with 1.0 being full volume.
     */
    public void setVolume(double volume) {
        set("volume", Math.max(Math.min(volume, 1d), 0d));
    }

    /**
     * Gets the current volume.
     *
     * @return The current volume as a percentage between 0 and 100 of the max
     * volume.
     */
    public double getVolume() {
        return ((Number) get("volume")).doubleValue();
    }

    /**
     * Get the currently playing audio stream. By default the first audio stream
     * with data is played. Default value: -1
     *
     * @return the currently playing audio stream index (index is zero based).
     */
    public int getCurrentAudio() {
        return (Integer) get("current-audio");
    }

    /**
     * Set the currently playing audio stream. By default the first audio stream
     * with data is played.
     *
     * @param n audio stream index to switch to (index is zero based).
     */
    public void setCurrentAudio(int n) {
        set("current-audio", n);
    }

    /**
     * Get the total number of available audio streams.
     *
     * @return total number of available audio streams
     */
    public int getNAudio() {
        return (Integer) get("n-audio");
    }

    /**
     * Retrieve the stream-combiner sinkpad for a specific text stream. This pad
     * can be used for notifications of caps changes, stream-specific queries,
     * etc.
     *
     * @param audioStreamIndex a text stream number
     * @return a GstPad, or NULL when the stream number does not exist.
     */
    public Pad getAudioPad(int audioStreamIndex) {
        return emit(Pad.class, "get-audio-pad", audioStreamIndex);
    }

    /**
     * Retrieve the tags of a specific audio stream number. This information can
     * be used to select a stream.
     *
     * @param audioStreamIndex an audio stream number
     * @return a GstTagList with tags or {@code null} when the stream number
     * does not exist.
     */
    public TagList getAudioTags(int audioStreamIndex) {
        return emit(TagList.class, "get-audio-tags", audioStreamIndex);
    }

    /**
     * Get the currently playing text stream. By default the first text stream
     * with data is played. Default value: -1
     *
     * @return the currently playing text stream index (index is zero based).
     */
    public int getCurrentText() {
        return (Integer) get("current-text");
    }

    /**
     * Set the currently playing text stream. By default the first text stream
     * with data is played.
     *
     * @param n text stream index to switch to (index is zero based).
     */
    public void setCurrentText(int n) {
        set("current-text", n);
    }

    /**
     * Get the total number of available text streams.
     *
     * @return total number of available text streams
     */
    public int getNText() {
        return (Integer) get("n-text");
    }

    /**
     * Retrieve the stream-combiner sinkpad for a specific text stream. This pad
     * can be used for notifications of caps changes, stream-specific queries,
     * etc.
     *
     * @param textStreamIndex a text stream number
     * @return a GstPad, or {@code null} when the stream number does not exist.
     */
    public Pad getTextPad(int textStreamIndex) {
        return emit(Pad.class, "get-text-pad", textStreamIndex);
    }

    /**
     * Retrieve the tags of a specific text stream number. This information can
     * be used to select a stream.
     *
     * @param textStreamIndex a text stream number
     * @return a GstTagList with tags or {@code null} when the stream number
     * does not exist.
     */
    public TagList getTextTags(int textStreamIndex) {
        return emit(TagList.class, "get-text-tags", textStreamIndex);
    }

    /**
     * Adds a listener for the <code>about-to-finish</code> signal
     */
    public void connect(final ABOUT_TO_FINISH listener) {
        connect(ABOUT_TO_FINISH.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(PlayBin elem) {
                listener.aboutToFinish(elem);
            }
        });
    }

    /**
     * Removes a listener for the <code>about-to-finish</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(ABOUT_TO_FINISH listener) {
        disconnect(ABOUT_TO_FINISH.class, listener);
    }

    /**
     * Adds a listener for the <code>video-changed</code> signal
     */
    public void connect(final VIDEO_CHANGED listener) {
        connect(VIDEO_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(PlayBin elem) {
                listener.videoChanged(elem);
            }
        });
    }

    /**
     * Removes a listener for the <code>video-changed</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(VIDEO_CHANGED listener) {
        disconnect(VIDEO_CHANGED.class, listener);
    }

    /**
     * Adds a listener for the <code>audio-changed</code> signal
     */
    public void connect(final AUDIO_CHANGED listener) {
        connect(AUDIO_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(PlayBin elem) {
                listener.audioChanged(elem);
            }
        });
    }

    /**
     * Removes a listener for the <code>audio-changed</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(AUDIO_CHANGED listener) {
        disconnect(AUDIO_CHANGED.class, listener);
    }

    /**
     * Adds a listener for the <code>audio-changed</code> signal
     */
    public void connect(final TEXT_CHANGED listener) {
        connect(TEXT_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(PlayBin elem) {
                listener.textChanged(elem);
            }
        });
    }

    /**
     * Removes a listener for the <code>text-changed</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(TEXT_CHANGED listener) {
        disconnect(TEXT_CHANGED.class, listener);
    }

    /**
     * Adds a listener for the <code>video-tags-changed</code> signal
     */
    public void connect(final VIDEO_TAGS_CHANGED listener) {
        connect(VIDEO_TAGS_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(PlayBin elem, int stream) {
                listener.videoTagsChanged(elem, stream);
            }
        });
    }

    /**
     * Removes a listener for the <code>video-tags-changed</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(VIDEO_TAGS_CHANGED listener) {
        disconnect(VIDEO_TAGS_CHANGED.class, listener);
    }

    /**
     * Adds a listener for the <code>audio-tags-changed</code> signal
     */
    public void connect(final AUDIO_TAGS_CHANGED listener) {
        connect(AUDIO_TAGS_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(PlayBin elem, int stream) {
                listener.audioTagsChanged(elem, stream);
            }
        });
    }

    /**
     * Removes a listener for the <code>audio-tags-changed</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(AUDIO_TAGS_CHANGED listener) {
        disconnect(AUDIO_TAGS_CHANGED.class, listener);
    }

    /**
     * Adds a listener for the <code>audio-tags-changed</code> signal
     */
    public void connect(final TEXT_TAGS_CHANGED listener) {
        connect(TEXT_TAGS_CHANGED.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(PlayBin elem, int stream) {
                listener.textTagsChanged(elem, stream);
            }
        });
    }

    /**
     * Removes a listener for the <code>text-tags-changed</code> signal
     *
     * @param listener The listener that was previously added.
     */
    public void disconnect(TEXT_TAGS_CHANGED listener) {
        disconnect(TEXT_TAGS_CHANGED.class, listener);
    }

    /**
     * Add a listener for the <code>element-setup</code> signal.
     *
     * @param listener listener to add
     */
    @Gst.Since(minor = 10)
    public void connect(final ELEMENT_SETUP listener) {
        Gst.checkVersion(1, 10);
        connect(ELEMENT_SETUP.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(PlayBin playbin, Element element) {
                listener.elementSetup(playbin, element);
            }
        });
    }

    /**
     * Remove listener for the <code>element-setup</code> signal.
     *
     * @param listener listener to remove
     */
    @Gst.Since(minor = 10)
    public void disconnect(ELEMENT_SETUP listener) {
        disconnect(ELEMENT_SETUP.class, listener);
    }

    /**
     * Add a listener for the <code>source-setup</code> signal.
     *
     * @param listener listener to add
     */
    public void connect(final SOURCE_SETUP listener) {
        connect(SOURCE_SETUP.class, listener, new GstCallback() {
            @SuppressWarnings("unused")
            public void callback(PlayBin playbin, Element element) {
                listener.sourceSetup(playbin, element);
            }
        });
    }

    /**
     * Remove listener for the <code>source-setup</code> signal.
     *
     * @param listener listener to remove
     */
    public void disconnect(SOURCE_SETUP listener) {
        disconnect(SOURCE_SETUP.class, listener);
    }

    /**
     * Signal emitted when the current uri is about to finish. You can set the
     * uri and suburi to make sure that playback continues.
     */
    public static interface ABOUT_TO_FINISH {

        /**
         */
        public void aboutToFinish(PlayBin element);
    }

    /**
     * Signal is emitted whenever the number or order of the video streams has
     * changed. The application will most likely want to select a new video
     * stream.
     */
    public static interface VIDEO_CHANGED {

        /**
         */
        public void videoChanged(PlayBin element);
    }

    /**
     * Signal is emitted whenever the number or order of the audio streams has
     * changed. The application will most likely want to select a new audio
     * stream.
     */
    public static interface AUDIO_CHANGED {

        /**
         */
        public void audioChanged(PlayBin element);
    }

    /**
     * Signal is emitted whenever the number or order of the audio streams has
     * changed. The application will most likely want to select a new audio
     * stream.
     */
    public static interface TEXT_CHANGED {

        /**
         */
        public void textChanged(PlayBin element);
    }

    /**
     * Signal is emitted whenever the tags of a video stream have changed. The
     * application will most likely want to get the new tags.
     */
    public static interface VIDEO_TAGS_CHANGED {

        /**
         * @param stream stream index with changed tags
         */
        public void videoTagsChanged(PlayBin element, int stream);
    }

    /**
     * Signal is emitted whenever the tags of an audio stream have changed. The
     * application will most likely want to get the new tags.
     */
    public static interface AUDIO_TAGS_CHANGED {

        /**
         * @param stream stream index with changed tags
         */
        public void audioTagsChanged(PlayBin element, int stream);
    }

    /**
     * Signal is emitted whenever the tags of a text stream have changed. The
     * application will most likely want to get the new tags.
     */
    public static interface TEXT_TAGS_CHANGED {

        /**
         * @param stream stream index with changed tags
         */
        public void textTagsChanged(PlayBin element, int stream);
    }

    /**
     * This signal is emitted when a new element is added to playbin or any of
     * its sub-bins. This signal can be used to configure elements, e.g. to set
     * properties on decoders. This is functionally equivalent to connecting to
     * the deep-element-added signal, but more convenient.
     * <p>
     * This signal is usually emitted from the context of a GStreamer streaming
     * thread, so might be called at the same time as code running in the main
     * application thread.
     */
    @Gst.Since(minor = 10)
    public static interface ELEMENT_SETUP {

        /**
         * Element setup signal callback.
         *
         * @param playbin signal source
         * @param element added element
         */
        public void elementSetup(PlayBin playbin, Element element);
    }

    /**
     * This signal is emitted after the source element has been created, so it
     * can be configured by setting additional properties (e.g. set a proxy
     * server for an http source, or set the device and read speed for an audio
     * cd source). This is functionally equivalent to connecting to the
     * notify::source signal, but more convenient.
     * <p>
     * This signal is usually emitted from the context of a GStreamer streaming
     * thread.
     */
    public static interface SOURCE_SETUP {

        /**
         * Source setup signal callback.
         *
         * @param playbin signal source
         * @param element source element added
         */
        public void sourceSetup(PlayBin playbin, Element element);
    }

}
