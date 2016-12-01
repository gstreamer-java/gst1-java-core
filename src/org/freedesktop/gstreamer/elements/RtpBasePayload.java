package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Element;
import org.freedesktop.gstreamer.Structure;

public class RtpBasePayload<Child> extends Element{
	
    public static final String GTYPE_NAME = "GstRTPBasePayload";

    public RtpBasePayload(Initializer init) {
		super(init);
	}

    public Long getMaxPacketTime() {
		return get("max-ptime");
	}
	
	@SuppressWarnings("unchecked")
	public Child setMaxPacketTime(long packetTimeInNanos) {
		set("max-ptime", packetTimeInNanos);
		return (Child) this;
	}

    public Long getMinPacketTime() {
		return get("min-ptime");
	}
	
	@SuppressWarnings("unchecked")
	public Child setMinPacketTime(long packetTimeInNanos) {
		set("min-ptime", packetTimeInNanos);
		return (Child) this;
	}
   
    public Integer getMtu() {
		return get("mtu");
	}
	
	@SuppressWarnings("unchecked")
	public Child setMtu(int mtu) {
		set("mtu", mtu);
		return (Child) this;
	}

    public Boolean isPerfectRtpTimeEnabled() {
		return get("perfect-rtptime");
	}
	
	@SuppressWarnings("unchecked")
	public Child setPerfectRtpTimeEnabled(boolean enabled) {
		set("perfect-rtptime", enabled);
		return (Child) this;
	}
	
    public Integer getPayloadType() {
		return get("pt");
	}
	
	@SuppressWarnings("unchecked")
	public Child setPayloadType(int payloadType) {
		set("pt", payloadType);
		return (Child) this;
	}

    public Long getMinPacketTimeMultiple() {
		return get("ptime-multiple");
	}
	
	@SuppressWarnings("unchecked")
	public Child setMinPacketTimeMultiple(long packetTimeInNanos) {
		set("ptime-multiple", packetTimeInNanos);
		return (Child) this;
	}
	
    public Integer getSequenceNumber() {
		return get("seqnum");
	}
	
	@SuppressWarnings("unchecked")
	public Child setSequenceNumber(int seqnum) {
		set("seqnum", seqnum);
		return (Child) this;
	}

    public Integer getSequenceNumberOffset() {
		return get("seqnum-offset");
	}
	
	@SuppressWarnings("unchecked")
	public Child setSequenceNumberOffset(int offset) {
		set("seqnum-offset", offset);
		return (Child) this;
	}

    public Integer getSSRC() {
		return get("ssrc");
	}
	
	@SuppressWarnings("unchecked")
	public Child setSSRC(int ssrc) {
		set("ssrc", ssrc);
		return (Child) this;
	}
	
	public Structure getStats() {
		return get("stats");
	}

    public Integer getTimestamp() {
		return get("timestamp");
	}

    public Integer getTimestampOffset() {
		return get("timestamp-offset");
	}
    
	@SuppressWarnings("unchecked")
	public Child getTimestampOffset(int offset) {
		set("timestamp-offset", offset);
		return (Child) this;
	}

}
