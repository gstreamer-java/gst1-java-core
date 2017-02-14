package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Pad;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

import com.sun.jna.Pointer;

public class RtpSsrcDemux extends Element{


	public static final String GST_NAME = "rtpssrcdemux";
	public static final String GTYPE_NAME = "GstRtpSsrcDemux";

	public RtpSsrcDemux() {
		this((String) null);
	}

	public RtpSsrcDemux(String name) {
		this(makeRawElement(GST_NAME, name));
	}

	public RtpSsrcDemux(Initializer init) {
		super(init);
	}

	public static interface NEW_SSRC_PAD {

		public void onNewSsrcPad(RtpSsrcDemux rtpSsrcDemux, int ssrc, Pad pad);

	}

	public void connect(final NEW_SSRC_PAD listener) {
		connect(NEW_SSRC_PAD.class, listener, new GstCallback() {
			@SuppressWarnings("unused")
			public void callback(Pointer rtpSsrcDemux, int ssrc, Pad pad, Pointer data) {
				listener.onNewSsrcPad(RtpSsrcDemux.this, ssrc, pad);
			}
		});
	}

	public void disconnect(final NEW_SSRC_PAD listener) {
		disconnect(NEW_SSRC_PAD.class, listener);
	}

	public static interface CLEAR_SSRC {

		public void onClearSsrc(RtpSsrcDemux rtpSsrcDemux, int ssrc);

	}

	public void connect(final CLEAR_SSRC listener) {
		connect(CLEAR_SSRC.class, listener, new GstCallback() {
			@SuppressWarnings("unused")
			public void callback(Pointer rtpSsrcDemux, int ssrc,  Pointer data) {
				listener.onClearSsrc(RtpSsrcDemux.this, ssrc);
			}
		});
	}

	public void disconnect(final CLEAR_SSRC listener) {
		disconnect(CLEAR_SSRC.class, listener);
	}

	public static interface REMOVED_SSRC_PAD {

		public void onRemovedSsrcPad(RtpSsrcDemux rtpSsrcDemux, int ssrc, Pad pad);

	}

	public void connect(final REMOVED_SSRC_PAD listener) {
		connect(REMOVED_SSRC_PAD.class, listener, new GstCallback() {
			@SuppressWarnings("unused")
			public void callback(Pointer rtpSsrcDemux, int ssrc, Pad pad, Pointer data) {
				listener.onRemovedSsrcPad(RtpSsrcDemux.this, ssrc, pad);
			}
		});
	}

	public void disconnect(final REMOVED_SSRC_PAD listener) {
		disconnect(REMOVED_SSRC_PAD.class, listener);
	}

}
