/*
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

import java.awt.Dimension;
import java.io.File;
import java.net.URI;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.ElementFactory;
import org.freedesktop.gstreamer.Fraction;
import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.Video;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;


/**
 * <p>
 * Playbin provides a stand-alone everything-in-one abstraction for an
 * audio and/or video player.
 * </p>
 * <p>
 * At this stage, playbin is considered UNSTABLE. The API provided in the
 * signals and properties may yet change in the near future. When playbin
 * is stable, it will probably replace <span class="type">playbin</span>
 * </p>
 * <p>
 * It can handle both audio and video files and features
 * </p>
 * <ul>
 * <li>
 * automatic file type recognition and based on that automatic
 * selection and usage of the right audio/video/subtitle demuxers/decoders
 * </li>
 * <li>
 * visualisations for audio files
 * </li>
 * <li>
 * subtitle support for video files. Subtitles can be store in external
 * files.
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
 * playbin is a {@link org.freedesktop.gstreamer.Pipeline}. It will notify the application of everything
 * that's happening (errors, end of stream, tags found, state changes, etc.)
 * by posting messages on its {@link org.freedesktop.gstreamer.Bus}. The application needs to watch the
 * bus.
 *
 * <p>
 * Playback can be initiated by setting the playbin to PLAYING state using
 * {@link #setState setState} or {@link #play play}. Note that the state change will take place in
 * the background in a separate thread, when the function returns playback
 * is probably not happening yet and any errors might not have occured yet.
 * Applications using playbin should ideally be written to deal with things
 * completely asynchroneous.
 * </p>
 *
 * <p>
 * When playback has finished (an EOS message has been received on the bus)
 * or an error has occured (an ERROR message has been received on the bus) or
 * the user wants to play a different track, playbin should be set back to
 * READY or NULL state, then the input file/URI should be set to the new
 * location and then playbin be set to PLAYING state again.
 * </p>
 *
 * <p>
 * Seeking can be done using {@link #seek seek} on the playbin element.
 * Again, the seek will not be executed instantaneously, but will be done in a
 * background thread. When the seek call returns the seek will most likely still
 * be in process. An application may wait for the seek to finish (or fail) using
 * {@link #getState(long)} with -1 as the timeout, but this will block the user
 * interface and is not recommended at all.
 *
 * <p>
 * Applications may query the current position and duration of the stream
 * via {@link #queryPosition} and {@link #queryDuration} and
 * setting the format passed to {@link Format#TIME}. If the query was successful,
 * the duration or position will have been returned in units of nanoseconds.
 * </p>
 * 
 * Was playbin2
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
    public PlayBin(Initializer init) {
        super(init);
    }


	/*private static String slashify(String path, boolean isDirectory) {
		String p = path;
		if (File.separatorChar != '/')
			p = p.replace(File.separatorChar, '/');
		if (!p.startsWith("/"))
			p = "/" + p;
		if (!p.endsWith("/") && isDirectory)
			p = p + "/";
		return p;
	}*/

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
     * <p> To disable audio output, call this method with a <tt>null</tt> argument.
     *
     * @param element The element to use for audio output.
     */
    public void setAudioSink(Element element) {
        setElement("audio-sink", element);
    }

    /**
     * Sets the video output Element.
     * <p> To disable video output, call this method with a <tt>null</tt> argument.
     *
     * @param element The element to use for video output.
     */
    public void setVideoSink(Element element) {
        setElement("video-sink", element);
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
        if (element == null) {
            element = ElementFactory.make("fakesink", "fake-" + key);
        }
        set(key, element);
    }

    /**
     * Set the volume for the PlayBin.
     *
     * @param percent Percentage (between 0 and 100) to set the volume to.
     */
    public void setVolumePercent(int percent) {
        setVolume(Math.max(Math.min((double) percent, 100d), 0d) / 100d);
    }

    /**
     * Get the current volume.
     * @return The current volume as a percentage between 0 and 100 of the max volume.
     */
    public int getVolumePercent() {
        return (int) ((getVolume() * 100d) + 0.5);
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
     * @return The current volume as a percentage between 0 and 100 of the max volume.
     */
    public double getVolume() {
        return ((Number) get("volume")).doubleValue();
    }
    
    /**
     * Retrieves the framerate from the caps of the video sink's pad.
     * 
     * @return frame rate (frames per second), or 0 if the framerate is not
     *         available
     */
    public double getVideoSinkFrameRate() {
      for (Element sink : getSinks()) {
        for (Pad pad : sink.getPads()) {
          Fraction frameRate = Video.getVideoFrameRate(pad);
          if (frameRate != null) {
            return frameRate.toDouble();
          }
        }
      }
      return 0;
    }

    /**
     * Retrieves the width and height of the video frames configured in the caps
     * of the video sink's pad.
     * 
     * @return dimensions of the video frames, or null if the video frame size is
     *         not available
     */
    public Dimension getVideoSize() {
      for (Element sink : getSinks()) {
        for (Pad pad : sink.getPads()) {
          Dimension size = Video.getVideoSize(pad);
          if (size != null) {
            return size;
          }
        }
      }
      return null;
    }    

    /**
     * Signal emitted when the current uri is about to finish. You can set 
     * the uri and suburi to make sure that playback continues.
     */
    public static interface ABOUT_TO_FINISH {
        /**
         */
        public void aboutToFinish(PlayBin element);
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
     * Signal is emitted whenever the number or order of the video streams has changed. 
     * The application will most likely want to select a new video stream.
     */
    public static interface VIDEO_CHANGED {
        /**
         */
        public void videoChanged(PlayBin element);
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
     * Signal is emitted whenever the number or order of the audio streams has changed. 
     * The application will most likely want to select a new audio stream.
     */
    public static interface AUDIO_CHANGED {
        /**
         */
        public void audioChanged(PlayBin element);
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
     * Signal is emitted whenever the number or order of the audio streams has changed. 
     * The application will most likely want to select a new audio stream.
     */
    public static interface TEXT_CHANGED {
        /**
         */
        public void textChanged(PlayBin element);
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
     * Signal is emitted whenever the tags of a video stream have changed. 
     * The application will most likely want to get the new tags.
     */
    public static interface VIDEO_TAGS_CHANGED {
        /**
         * @param stream stream index with changed tags
         */
        public void videoTagsChanged(PlayBin element, int stream);
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
     * Signal is emitted whenever the tags of an audio stream have changed. 
     * The application will most likely want to get the new tags.
     */
    public static interface AUDIO_TAGS_CHANGED {
        /**
         * @param stream stream index with changed tags
         */
        public void audioTagsChanged(PlayBin element, int stream);
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
     * Signal is emitted whenever the tags of a text stream have changed. 
     * The application will most likely want to get the new tags.
     */
    public static interface TEXT_TAGS_CHANGED {
        /**
         * @param stream stream index with changed tags
         */
        public void textTagsChanged(PlayBin element, int stream);
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
}
