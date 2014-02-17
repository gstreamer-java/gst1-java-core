/*
 * Copyright (c) 2009 Tamas Korodi <kotyo@zamba.fm>
 * Copyright (c) 2010 Levente Farkas <lfarkas@lfarkas.org>
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

package org.gstreamer.swt.overlay;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.gstreamer.Bin;
import org.gstreamer.Bin.ELEMENT_ADDED;
import org.gstreamer.BusSyncReply;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.GhostPad;
import org.gstreamer.Message;
import org.gstreamer.MessageType;
import org.gstreamer.State;
import org.gstreamer.Structure;
import org.gstreamer.elements.BaseSink;
import org.gstreamer.event.BusSyncHandler;

import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.platform.unix.X11.Display;
import com.sun.jna.platform.unix.X11.Window;
import com.sun.jna.platform.unix.X11.XCrossingEvent;
import com.sun.jna.platform.unix.X11.XEvent;

/**
 * VideoComponent which use OS's overlay video component 
 */
public class VideoComponent extends Canvas implements BusSyncHandler, DisposeListener {
	private static int counter = 0;
	
	private final Bin sink = new Bin();
	private final Bin autosink;
	private ELEMENT_ADDED sinkListener;
	private SWTOverlay overlay;
	private BaseSink videosink;
//	private BusSyncHandler oldSyncHandler;
	private Map<String, Object> properties = new HashMap<String, Object>();

	private boolean x11Events;
	private long nativeHandle;
	private boolean watcherRunning = false;

	/**
	 * Overlay VideoComponent
	 * @param parent
	 * @param style
	 * @param enableX11Events true if X11 event should have to be grabbed (mouse move, enter and leave event on Linux).
	 * 
	 * On Linux by default the handling of mouse move, enter and leave event are not propagated.
	 * Unfortunately the "handle-events" properties hide some important expose events too, 
	 * sowe've to do some lowlevel trick to be able to get these events.
	 */
	public VideoComponent(final Composite parent, int style, boolean enableX11Events) {
		super(parent, style | SWT.EMBEDDED);
		x11Events = enableX11Events;
		addDisposeListener(this);

		Element colorspace = ElementFactory.make("videoconvert", "colorspace" + counter);
		Element videoscale = ElementFactory.make("avvideoscale", "videoscale" + counter);
		autosink = (Bin)ElementFactory.make("autovideosink", "VideoComponent" + counter++);
		sink.addMany(colorspace, videoscale, autosink);
		Element.linkMany(colorspace, videoscale, autosink);
		sink.addPad(new GhostPad("colorspace_sink_ghostpad", colorspace.getSinkPads().get(0)));
		
		sinkListener = new ELEMENT_ADDED() {
			public void elementAdded(Bin bin, Element element) {
				if (element instanceof BaseSink) {
					videosink = (BaseSink)element;
					for (Map.Entry<String, Object> e : properties.entrySet())
						videosink.set(e.getKey(), e.getValue());
					// according to gstreamer docs:
					// http://gstreamer.freedesktop.org/data/doc/gstreamer/head/gst-plugins-base-libs/html/gst-plugins-base-libs-gstxoverlay.html
					// the overlay should have to be set from bus's SyncHandler,
					// but we use autosink and in element-added the pipe already 
					// created so the windows must be prepared so we simple call
					// the setOverlay and not the comment code bellow 
//					Bus bus = videosink.getBus();
//					oldSyncHandler = bus.getSyncHandler();
//					bus.setSyncHandler(VideoComponent.this); // for prepare-xwindow-id
					setOverlay();
					autosink.disconnect(this);               // from element-added
					sinkListener = null;                     // no longer needed
				}
			}
		};
		autosink.connect(sinkListener);
	}

	/**
	 * Overlay VideoComponent
	 * @param parent
	 * @param style
	 */
	public VideoComponent(final Composite parent, int style) {
		this(parent, style, false);
	}

	/**
	 * Dispose this Widget
	 */
	public void widgetDisposed(DisposeEvent arg0) {
		if (sinkListener != null)
			autosink.disconnect(sinkListener);
		removeDisposeListener(this);
		if (x11Events)
			watcherRunning = false;
		if (videosink != null && !videosink.getState().equals(State.NULL))
			throw new IllegalStateException("Wrong state on dispose: " + videosink.getState() + " -> " + videosink.getName());
	}	
	
	/**
	 * Set the overlay for this composite.
	 */
	private void setOverlay() {
		nativeHandle = SWTOverlay.getNativeHandle(this);
		overlay = SWTOverlay.wrap(videosink);
		overlay.setWindowHandle(nativeHandle);
		handleX11Events();
		expose();		
	}

	/**
	 * Implements the BusSyncHandler interface
	 * @param message
	 * @return
	 */
	public BusSyncReply syncMessage(Message message) {
		if (message.getType() != MessageType.ELEMENT)
			return BusSyncReply.PASS;
		Structure s = message.getStructure();
		if (s == null || !s.hasName("prepare-xwindow-id"))
			return BusSyncReply.PASS;
		setOverlay();
//		videosink.getBus().setSyncHandler(oldSyncHandler);
		return BusSyncReply.DROP;
	}

	/**
     * Tell an overlay that it has been exposed. This will redraw the current frame
     * in the drawable even if the pipeline is PAUSED.
     */
	public void expose() {
		getDisplay().asyncExec(new Runnable() {
			public void run() {
				if(!isDisposed() && (overlay != null))
					overlay.expose();
			}
		});
	}
	
	/**
	 * Set the final sink's properties (or cache it until it'll be created).
	 * 
	 * @param property
	 * @param data
	 */
	public void set(String property, Object data) {
		properties.put(property, data);
		if (videosink != null)
			videosink.set(property, data);
	}

	/**
	 * Set to keep aspect ratio
	 * 
	 * @param keepAspect
	 */
	public void setKeepAspect(boolean keepAspect) {
		set("force-aspect-ratio", keepAspect);
	}

	/**
	 * Retrieves the Gstreamer element made for the video component
	 * 
	 * @return element
	 */
	public Element getElement() {
		return sink;
	}
	
	/**
	 * Retrieves the Gstreamer element used as sink in the video component
	 * 
	 * @return element
	 */
	public Element getSinkElement() {
		return videosink;
	}
	
	/**
	 * On Linux by default the handling of mouse move, enter and leave event are not propagated.
	 * Unfortunately the "handle-events" properties hide some important expose events too, 
	 * sowe've to do some lowlevel trick to be able to get these events.
	 * In this case we (gstreamer-linux) must handle redraw too!
	 *
	 * @param enableX11Events true if X11 event should have to be grabbed (mouse move, enter and leave event on Linux).
	 */
    private void handleX11Events() {
		if (x11Events && Platform.isLinux()) {
			videosink.set("handle-events", !x11Events);
			overlay.handleEvent(!x11Events);
			watcherRunning = true;
			new Thread() {
				@Override
				public void run() {
					try {
						final X11 x11 = X11.INSTANCE;
						final Display display = x11.XOpenDisplay(null);
						Window window = new Window(nativeHandle);
						x11.XSelectInput(display, window,
								new NativeLong(X11.ExposureMask |
										X11.VisibilityChangeMask |
										X11.StructureNotifyMask |
										X11.FocusChangeMask |
										//X11.PointerMotionMask |
										X11.EnterWindowMask |
										X11.LeaveWindowMask));
						while (watcherRunning) {
							final XEvent xEvent = new XEvent();
							x11.XNextEvent(display, xEvent);
							if (watcherRunning && !isDisposed()) {
								getDisplay().asyncExec(new Runnable() {
									public void run() {
										if (watcherRunning && !isDisposed()) {
											final Event swtEvent = new Event();
											XCrossingEvent ce;
											switch (xEvent.type) {
//												case X11.MotionNotify:
//													XMotionEvent e = (XMotionEvent)xEvent.readField("xmotion");
//													swtEvent.x = e.x;
//													swtEvent.y = e.y;
//													notifyListeners(SWT.MouseMove, swtEvent);
//													break;
												case X11.EnterNotify:
													ce = (XCrossingEvent)xEvent.readField("xcrossing");
													swtEvent.x = ce.x;
													swtEvent.y = ce.y;
													notifyListeners(SWT.MouseEnter, swtEvent);
													break;
												case X11.LeaveNotify:
													ce = (XCrossingEvent)xEvent.readField("xcrossing");
													swtEvent.x = ce.x;
													swtEvent.y = ce.y;
													notifyListeners(SWT.MouseExit, swtEvent);
													break;
												default:
													overlay.expose();														
											}
										}
									}
								});
							}
						}
						x11.XCloseDisplay(display);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
}
