package org.gstreamer.swing;

import java.awt.Dimension;

import com.apple.eawt.CocoaComponent;
import com.sun.jna.Pointer;


public class OSXVideoComponent extends CocoaComponent {
	private static final long serialVersionUID = -8534578348583192142L;

	public OSXVideoComponent(final Pointer nsview) {
		this(nsview, false);
	}
	
	public OSXVideoComponent(final Pointer nsview, boolean enableCocoaCompatibilityMode) {
		assert nsview != null;
		this.nsview = nsview;
		
		if (!enableCocoaCompatibilityMode) {
			System.setProperty("com.apple.eawt.CocoaComponent.CompatibilityMode", "false"); 
		}
	}

	private final Pointer nsview;
	private Dimension preferredSize = new Dimension(2, 2);
	
	@Override
	public int createNSView() {
		return (int) createNSViewLong();
	}

	@Override
	public long createNSViewLong() {
		return Pointer.nativeValue(nsview);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(2, 2); // minimum size recommended by Apple
	}

	@Override
	public Dimension getPreferredSize() {
		return preferredSize;
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize) {
		this.preferredSize = preferredSize;
	}
}
