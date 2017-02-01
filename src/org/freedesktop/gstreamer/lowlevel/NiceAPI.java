package org.freedesktop.gstreamer.lowlevel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.freedesktop.gstreamer.GSocket;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GSList;
import org.freedesktop.gstreamer.lowlevel.enums.NiceCompatibility;
import org.freedesktop.gstreamer.lowlevel.enums.NiceComponentState;
import org.freedesktop.gstreamer.lowlevel.enums.NiceRelayType;
import org.freedesktop.gstreamer.nice.NiceAgent;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;

@SuppressWarnings("serial")
public interface NiceAPI extends Library {

	NiceAPI NICE_API = GNative.loadLibrary("nice", NiceAPI.class,
			new HashMap<String, Object>() {{
				put(Library.OPTION_TYPE_MAPPER, new GTypeMapper());
			}});

	public static final int NICE_CANDIDATE_MAX_FOUNDATION = 32 + 1;
	
	public static class NiceCandidate extends Structure {
		
		public static class ByReference extends NiceCandidate implements Structure.ByReference{};
		
		public int type;
		public int transport;
		
		// NiceAddress
		public byte[] addr = new byte[28];
		public byte[] base_addr = new byte[28];
		public int priority;
		public int stream_id;
		public int component_id;
		public byte[] foundation = new byte[NICE_CANDIDATE_MAX_FOUNDATION];
		public String username;
		public String password;
		public Pointer turn;
		public Pointer sockptr;
		

		public String toSDP(NiceAgent agent) {
			return NICE_API.nice_agent_generate_local_candidate_sdp(agent, this);
		}
		
		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[]{
					"type", "transport", "addr", "base_addr", 
					"priority", "stream_id", "component_id", 
					"foundation",
					"username", "password", "turn", "sockptr",
			});
		}
		
	}
	
	public Pointer nice_agent_new(GMainContext gMainContext, NiceCompatibility niceCompatibility);
	public Pointer nice_agent_new_reliable(GMainContext gMainContext, NiceCompatibility niceCompatibility);
	public boolean nice_agent_restart(NiceAgent agent);

	public void nice_agent_set_software(NiceAgent agent, String software);

	public int nice_agent_set_port_range(NiceAgent agent, int streamId, int componentId, int minPort, int maxPort);

	public int nice_agent_add_stream(NiceAgent agent, int numberOfComponents);
	public void nice_agent_remove_stream(NiceAgent agent, int streamId);
	public String nice_agent_get_stream_name(NiceAgent agent, int streamId);
	public boolean nice_agent_set_stream_name(NiceAgent agent, int streamId, String name);
	public int nice_agent_set_stream_tos(NiceAgent agent, int streamId, int tos);
	public int nice_agent_restart_stream(NiceAgent agent, int streamId);


	public boolean nice_agent_set_relay_info(NiceAgent agent, int streamId, int componentId, String serverIp, int serverPort, String username, String password, NiceRelayType type);
	public boolean nice_agent_forget_relays(NiceAgent agent, int streamId, int componentId);

	public boolean nice_agent_set_remote_credentials(NiceAgent agent, int streamId, String ufrag, String pwd);
	public boolean nice_agent_get_local_credentials(NiceAgent agent, int streamId, PointerByReference ufragPtr, PointerByReference pwdPtr);
	public boolean nice_agent_set_local_credentials(NiceAgent agent, int streamId, String ufrag, String pwd);

	public boolean nice_agent_gather_candidates(NiceAgent agent, int streamId);
	public int nice_agent_set_remote_candidates(NiceAgent agent, int streamId, int componentId, GSList gsListCandidates);
	public GSList nice_agent_get_remote_candidates(NiceAgent agent, int streamId, int componentId);
	public GSList nice_agent_get_local_candidates(NiceAgent agent, int streamId, int componentId);
	public NiceCandidate nice_agent_parse_remote_candidate_sdp(NiceAgent agent, int streamId, String remoteSDP);


	public String nice_agent_generate_local_sdp(NiceAgent agent);
	public String nice_agent_generate_local_stream_sdp(NiceAgent agent, int streamId, boolean includeNonIce);
	public String nice_agent_generate_local_candidate_sdp(NiceAgent agent, NiceCandidate candidate);
	
	public int nice_agent_parse_remote_sdp(NiceAgent agent, String remoteSDP);
	public int nice_agent_parse_remote_stream_sdp(NiceAgent agent, String remoteSDP, String ufrag, String pwd);

	public GSocket nice_agent_get_selected_socket(NiceAgent agent, int streamId, int componentId);
	public boolean nice_agent_attach_recv(NiceAgent agent, int streamId, int componentId, GMainContext context, Callback callback, Pointer data);
	
	
	public NiceComponentState nice_agent_get_component_state(NiceAgent agent, int streamId, int component);
	public String nice_component_state_to_string(NiceComponentState state);


	public static interface NEW_CANDIDATE {
		
		public void onNewCandidate(NiceAgent agent, int streamId, int componentId, String foundation);
		
	}

	public static interface NEW_CANDIDATE_FULL {
		
		public void onNewCandidate(NiceAgent agent, NiceCandidate candidate);
		
	}

	public static interface CANDIDATE_GATHERING_DONE {

		public void onCandidateGatheringDone(NiceAgent agent, int streamId);

	}

	public static interface COMPONENT_STATE_CHANGED {

		public void onComponentStateChanged(NiceAgent agent, int streamId, int componentId, NiceComponentState state);

	}

	public static interface NICE_RECV {
		
		public void onNiceMessageReceived(NiceAgent agent, int streamId, int componentId, int len, Pointer buffer);
		
	}

	public static interface INITIAL_BINDING_REQUEST_RECEIVED {
		
		
		
	}

}
