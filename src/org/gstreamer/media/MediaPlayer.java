/* 
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

package org.gstreamer.media;

import java.net.URI;
import java.util.Collection;

import org.gstreamer.Element;
import org.gstreamer.Pipeline;
import org.gstreamer.media.event.MediaListener;

/**
 * The control interface used to control/query the gstreamer pipeline that
 * {@link org.gstreamer.swing.VideoPlayer} uses to play media files.
 * @author wayne
 */
public interface MediaPlayer {
    /**
     * Gets the {@link Pipeline} that the MediaPlayer uses to play media.
     * 
     * @return A Pipeline
     */
    Pipeline getPipeline();

    /**
     * Sets the Element to use for audio output.
     *
     * @param sink The {@link org.gstreamer.Element} to use for audio output.
     */
    public void setAudioSink(Element sink);

    /**
     * Sets the Element to use for video output.
     *
     * @param sink The {@link org.gstreamer.Element} to use for video output.
     */
    public void setVideoSink(Element sink);

    /**
     * Sets the media file to play.
     * 
     * @param uri The URI that describes the location of the media file.
     */
    void setURI(URI uri);
    
    /**
     * Starts playing the media (as set by {@link #setURI}.
     */    
    void play();

    /**
     * Pauses playing the currently playing media file.
     */
    void pause();
    
    /**
     * Stops playing the currently playing media file.
     */
    void stop();
    
    /**
     * Tests if this media player is currently playing a media file.
     * 
     * @return true if a media file is being played.
     */
    public boolean isPlaying();

    /**
     * Adds a uri to the playlist
     * 
     * @param uri The uri to add to the playlist.
     */
    void enqueue(URI uri);
    
    /**
     * Adds a list of media files to the playlist.
     * 
     * @param playlist The list of media files to add.
     */
    void enqueue(Collection<URI> playlist);
    
    /**
     * Replaces the current playlist with a new playlist.
     * @param playlist The new playlist.
     */
    void setPlaylist(Collection<URI> playlist);
    
    /**
     * Removes a file from the playlist.
     * @param uri The uri to remove.
     */
    void remove(URI uri);
    
    void setVolume(double volume);
    
    double getVolume();
    
    /**
     * Adds a listener for media events
     * 
     * @param listener The {@link org.gstreamer.media.event.MediaListener} to receive the events.
     */
    void addMediaListener(MediaListener listener);
    
    /**
     * Removes a listener for media events.
     * @param listener The previously added {@link org.gstreamer.media.event.MediaListener}.
     */
    void removeMediaListener(MediaListener listener);
}
