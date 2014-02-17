/* 
 * Copyright (c) 2007,2008 Wayne Meissner
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import org.gstreamer.media.event.EndOfMediaEvent;
import org.gstreamer.media.event.MediaListener;
import org.gstreamer.media.event.PauseEvent;
import org.gstreamer.media.event.StartEvent;
import org.gstreamer.media.event.StopEvent;

/**
 * Provides a partial implementation of <tt>MediaPlayer</tt> that handles {@link MediaListener}
 * and playlist management.
 */
public abstract class AbstractMediaPlayer implements MediaPlayer {

    private final Map<MediaListener, MediaListener> mediaListeners = new HashMap<MediaListener, MediaListener>();
    private final List<MediaListener> listeners = new CopyOnWriteArrayList<MediaListener>();
    protected final Executor eventExecutor;
    protected final Queue<URI> playList = new ConcurrentLinkedQueue<URI>();
    
    protected AbstractMediaPlayer(Executor eventExecutor) {
        this.eventExecutor = eventExecutor;
    }
    
    protected void fireEndOfMediaEvent(EndOfMediaEvent ev) {
        for (MediaListener l : getMediaListeners()) {
            l.endOfMedia(ev);
        }
    }
    
    protected void fireStartEvent(StartEvent ev) {
        for (MediaListener l : getMediaListeners()) {
            l.start(ev);
        }
    }
    
    protected void fireStopEvent(StopEvent ev) {
        for (MediaListener l : getMediaListeners()) {
            l.stop(ev);
        }
    }
    protected void firePauseEvent(PauseEvent ev) {
        for (MediaListener l : getMediaListeners()) {
            l.pause(ev);
        }
    }
    
    /**
     * Adds a uri to the playlist
     * 
     * @param uri The uri to add to the playlist.
     */
    public void enqueue(URI uri) {
        playList.add(uri);
    }
    
    /**
     * Adds a list of media files to the playlist.
     * 
     * @param playlist The list of media files to add.
     */
    public void enqueue(Collection<URI> playlist) {
        this.playList.addAll(playlist);
    }
    
    /**
     * Replaces the current play list with a new play list.
     * 
     * @param playlist The new playlist.
     */
    public void setPlaylist(Collection<URI> playlist) {
        this.playList.clear();
        this.playList.addAll(playlist);
    }
    
    /**
     * Removes a file from the play list.
     * 
     * @param uri The uri to remove.
     */
    public void remove(URI uri) {
        this.playList.remove(uri);
    }
    
    /**
     * Adds a {@link MediaListener} that will be notified of media events.
     * 
     * @param listener the MediaListener to add.
     */
    public synchronized void addMediaListener(MediaListener listener) {
        // Wrap the listener in a swing EDT safe version
        MediaListener proxy = wrapListener(MediaListener.class, listener, eventExecutor);
        mediaListeners.put(listener, proxy);
        listeners.add(proxy);
    }
    
    /**
     * Adds a {@link MediaListener} that will be notified of media events.
     * 
     * @param listener the MediaListener to add.
     */
    public synchronized void removeMediaListener(MediaListener listener) {
        MediaListener proxy = mediaListeners.remove(listener);
        listeners.remove(proxy);
    }
    
    /**
     * Gets the current list of media listeners
     * @return a list of {@link MediaListener}
     */
    protected List<MediaListener> getMediaListeners() {
        return listeners;
    }
    private static <T> T wrapListener(Class<T> interfaceClass, T instance, Executor executor) {
        return interfaceClass.cast(Proxy.newProxyInstance(interfaceClass.getClassLoader(), 
                new Class[]{ interfaceClass }, 
                new ExecutorInvocationProxy(instance, executor)));
    }
    
    /**
     * Provides a way of automagically executing methods on an interface on a 
     * different thread.
     */
    private static class ExecutorInvocationProxy implements InvocationHandler {

        private final Executor executor;
        private final Object object;

        public ExecutorInvocationProxy(Object object, Executor executor) {
            this.object = object;
            this.executor = executor;
        }

        public Object invoke(Object self, final Method method, final Object[] argArray) throws Throwable {
            if (method.getName().equals("hashCode")) {
                return object.hashCode();
            }
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        method.invoke(object, argArray);
                    } catch (Throwable t) {}
                }
            });
            return null;
        }
    }
}
