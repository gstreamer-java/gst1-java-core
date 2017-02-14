package org.freedesktop.gstreamer.nice;

import org.freedesktop.gstreamer.lowlevel.GlibAPI;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GSList;
import org.freedesktop.gstreamer.lowlevel.NiceAPI;
import org.freedesktop.gstreamer.lowlevel.NiceAPI.NiceCandidate;

import com.sun.jna.ptr.PointerByReference;

public class NiceStream {
	
	
	private NiceAgent agent;
	private int streamId;
	private int numberOfComponents;
	
	
	protected NiceStream(NiceAgent agent, int streamId, int numberOfComponents) {
		super();
		this.agent = agent;
		this.streamId = streamId;
		this.numberOfComponents = numberOfComponents;
	}
	
	protected NiceAgent getAgent() {
		return agent;
	}
	
	public int getStreamId() {
		return streamId;
	}
	
	public int getNumberOfComponents() {
		return numberOfComponents;
	}
	
	public String getName() {
		return NiceAPI.NICE_API.nice_agent_get_stream_name(getAgent(), getStreamId());
	}
	
	public boolean setName(String name) {
		return NiceAPI.NICE_API.nice_agent_set_stream_name(getAgent(), getStreamId(), name);
	}
	
	public NiceStreamCredentials getLocalCredentials() {
		NiceStreamCredentials result = null;
		PointerByReference ufragPtr = new PointerByReference();
		PointerByReference pwdPtr = new PointerByReference();
		if ( NiceAPI.NICE_API.nice_agent_get_local_credentials(getAgent(), getStreamId(), ufragPtr, pwdPtr) ) {
			result = new NiceStreamCredentials(ufragPtr.getValue().getString(0), pwdPtr.getValue().getString(0));
			GlibAPI.GLIB_API.g_free(ufragPtr.getValue());
			GlibAPI.GLIB_API.g_free(pwdPtr.getValue());
		} 
		return result;
	}
	
	public boolean setLocalCredentials(NiceStreamCredentials credentials) {
		return setLocalCredentials(credentials.getUfrag(), credentials.getPwd());
	}
	
	public boolean setLocalCredentials(String ufrag, String pwd) {
		return NiceAPI.NICE_API.nice_agent_set_local_credentials(getAgent(), getStreamId(), ufrag, pwd);
	}
	
	public boolean setRemoteCredentials(String ufrag, String pwd) {
		return NiceAPI.NICE_API.nice_agent_set_remote_credentials(getAgent(), getStreamId(), ufrag, pwd);
	}
	
	public int setRemoteCandidate(NiceCandidate candidate) {
		GSList firstCandidate = new GSList();
		firstCandidate.data = candidate.getPointer();
		return NiceAPI.NICE_API.nice_agent_set_remote_candidates(getAgent(), getStreamId(), candidate.component_id, firstCandidate);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+"{id="+getStreamId()+", name="+getName()+"}";
	}
	
}
