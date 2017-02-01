package org.freedesktop.gstreamer.nice;

import org.freedesktop.gstreamer.GObject;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.lowlevel.NativeObject;
import org.freedesktop.gstreamer.lowlevel.NiceAPI;
import org.freedesktop.gstreamer.lowlevel.NiceAPI.CANDIDATE_GATHERING_DONE;
import org.freedesktop.gstreamer.lowlevel.NiceAPI.COMPONENT_STATE_CHANGED;
import org.freedesktop.gstreamer.lowlevel.NiceAPI.NEW_CANDIDATE_FULL;
import org.freedesktop.gstreamer.lowlevel.NiceAPI.NiceCandidate;
import org.freedesktop.gstreamer.lowlevel.enums.NiceCompatibility;
import org.freedesktop.gstreamer.lowlevel.enums.NiceComponentState;
import org.freedesktop.gstreamer.lowlevel.enums.NiceRelayType;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public class NiceAgent extends GObject {
	
	public NiceAgent() {
		this(false, NiceCompatibility.NICE_COMPATIBILITY_RFC5245);
	}
	
	public NiceAgent(boolean reliable, NiceCompatibility compatibility) {
		this(
			NativeObject.initializer(
				( reliable ?  							
						NiceAPI.NICE_API.nice_agent_new_reliable(Gst.getMainContext(), compatibility)
						:	NiceAPI.NICE_API.nice_agent_new(Gst.getMainContext(), compatibility))
			)
		);
		
	}

	public NiceAgent(Initializer init) {
		super(init);
	}

	public String getStunServerIp() {
		return get("stun-server");
	}
	
	public NiceAgent setStunServerIp(String serverIp) {
		set("stun-server", serverIp);
		return this;
	}
	
	public Integer getStunServerPort() {
		return get("stun-server-port");
	}
	
	public NiceAgent setStunServerPort(int port) {
		set("stun-server-port", port);
		return this;
	}
	
	public boolean isControllingModeEnabled() {
		return get("controlling-mode");
	}
	
	public NiceAgent setControllingModeEnabled(boolean enabled) {
		set("controlling-mode", enabled);
		return this;
	}
	
	public boolean gatherCandidates(int streamId) {
		return NiceAPI.NICE_API.nice_agent_gather_candidates(this, streamId);
	}
	
	public boolean addRelayInfo(int streamId, int componentId, String serverIp, int port, String username, String password, NiceRelayType type) {
		return NiceAPI.NICE_API.nice_agent_set_relay_info(this, streamId, componentId, serverIp, port, username, password, type);
	}
	
	public NiceStream addStream(int numberOfComponents) {
		int streamId = NiceAPI.NICE_API.nice_agent_add_stream(this, numberOfComponents);
		if (streamId == 0) {
			throw new RuntimeException("Error while trying to add stream to agent "+this);
		} else {
			return new NiceStream(this, streamId, numberOfComponents);
		}
	}

	public void removeStream(NiceStream stream) {
		NiceAPI.NICE_API.nice_agent_remove_stream(this, stream.getStreamId());
	}
	
	public int parseRemoteSDP(String remoteSDP) {
		// Check not null and convert CR+LF into LF
		if (remoteSDP == null) {
			return -1;
		}
		remoteSDP = remoteSDP.replaceAll("\\r\\n", "\n");
		return NiceAPI.NICE_API.nice_agent_parse_remote_sdp(this, remoteSDP);
	}
	
	public String generateLocalSDP() {
		return NiceAPI.NICE_API.nice_agent_generate_local_sdp(this);
	}
	
	public void connect(final NEW_CANDIDATE_FULL listener) {
	    connect(NEW_CANDIDATE_FULL.class, listener, new Callback() {
	        @SuppressWarnings("unused")
			public void callback(Pointer agent, NiceCandidate.ByReference candidate) {
	        	listener.onNewCandidate(NiceAgent.this, candidate);
			}
		});
	}

	public void disconnect(final NEW_CANDIDATE_FULL listener) {
		disconnect(NEW_CANDIDATE_FULL.class, listener);
	}

	public void connect(final CANDIDATE_GATHERING_DONE listener) {
	    connect(CANDIDATE_GATHERING_DONE.class, listener, new Callback() {
	        @SuppressWarnings("unused")
			public void callback(Pointer agent, int streamId, Pointer data){
	        	listener.onCandidateGatheringDone(NiceAgent.this, streamId);
			}
		});
	}

	public void disconnect(final CANDIDATE_GATHERING_DONE listener) {
		disconnect(CANDIDATE_GATHERING_DONE.class, listener);
	}

	public void connect(final COMPONENT_STATE_CHANGED listener) {
	    connect(COMPONENT_STATE_CHANGED.class, listener, new Callback() {
	        @SuppressWarnings("unused")
			public void callback(Pointer agent, int streamId, int componentId, int state, Pointer data) {
	        	listener.onComponentStateChanged(NiceAgent.this, streamId, componentId, NiceComponentState.values()[state]);
			}
		});
	}

	public void disconnect(final COMPONENT_STATE_CHANGED listener) {
		disconnect(COMPONENT_STATE_CHANGED.class, listener);
	}

}
