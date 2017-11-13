package org.freedesktop.gstreamer.elements;

import org.freedesktop.gstreamer.Format;
import org.freedesktop.gstreamer.RecoverPolicy;
import org.freedesktop.gstreamer.SyncMethod;

public class TcpServerSink extends MultiSocketSink<TcpServerSink>{
	
	public static final String GST_NAME = "tcpserversink";
    public static final String GTYPE_NAME = "GstTCPServerSink";
    
    public TcpServerSink() {
		this((String) null);
	}

    public TcpServerSink(String name) {
		this(makeRawElement(GST_NAME, name));
	}

    public TcpServerSink(Initializer init) {
		super(init);
	}

    public int getQueuedBufferCount() {
    	return get("buffers-queued");
    }
    
    public Format getUnitFormat() {
    	return Format.valueOf((Integer) get("unit-format"));
    }
    
    public TcpServerSink setUnitFormat(Format format) {
    	set("unit-format", format.intValue());
    	return this;
    }
    
    public long getUnitsMax() {
    	return get("unit-max");
    }
    
    public TcpServerSink setUnitsMax(long units) {
    	set("unit-max", units);
    	return this;
    }
    
    public long getUnitsSoftMax() {
    	return get("unit-soft-max");
    }
    
    public TcpServerSink setUnitsSoftMax(long units) {
    	set("unit-soft-max", units);
    	return this;
    }
    
    public int getBuffersMax() {
    	return get("buffers-max");
    }

    public TcpServerSink setBuffersMax(int buffersMax) {
    	set("buffers-max", buffersMax);
    	return this;
    }

    public int getBuffersSoftMax() {
    	return get("buffers-soft-max");
    }

    public TcpServerSink setBuffersSoftMax(int buffersSoftMax) {
    	set("buffers-soft-max", buffersSoftMax);
    	return this;
    }
    
    public long getTimeMin() {
    	return get("time-min");
    }
    
    public TcpServerSink setTimeMin(long minimumQueuedTimeInNanos) {
    	set("time-min", minimumQueuedTimeInNanos);
    	return this;
    }
    
    public int getBytesMin() {
    	return get("bytes-min");
    }
    
    public TcpServerSink setBytesMin(int minimumQueuedBytes) {
    	set("bytes-min", minimumQueuedBytes);
    	return this;
    }

    public int getBuffersMin() {
    	return get("buffers-min");
    }
    
    public TcpServerSink setBuffersMin(int minimumQueuedBuffers) {
    	set("buffers-min", minimumQueuedBuffers);
    	return this;
    }

    public RecoverPolicy getRecoverPolicy() {
    	return RecoverPolicy.valueOf((Integer) get("recover-policy"));
    }
    
    public TcpServerSink setRecoverPolicy(RecoverPolicy recoverPolicy) {
    	set("recover-policy", recoverPolicy.intValue());
    	return this;
    }
    
    public long getTimeout() {
    	return get("timeout");
    }
    
    public TcpServerSink setTimeout(long timeoutInNanos) {
    	set("timeout", timeoutInNanos);
    	return this;
    }
    
    public SyncMethod getSyncMethod() {
    	return SyncMethod.valueOf((Integer) get("sync-method"));
    }
    
    public TcpServerSink setSyncMethod(SyncMethod recoverPolicy) {
    	set("sync-method", recoverPolicy.intValue());
    	return this;
    }
    
    public long getBytesToServe() {
    	return get("bytes-to-serve");
    }

    public long getBytesServed() {
    	return get("bytes-served");
    }
    
    public Format getBurstFormat() {
    	return Format.valueOf((Integer) get("burst-format"));
    }
    
    public TcpServerSink setBurstFormat(Format format) {
    	set("burst-format", format.intValue());
    	return this;
    }

    public long getBurstValue() {
    	return get("burst-value");
    }
    
    public TcpServerSink setBurstValue(long units) {
    	set("burst-value", units);
    	return this;
    }
    
    public int getQosDscp() {
    	return get("qos-dscp");
    }
    
    public TcpServerSink setQosDscp(int qosDscp) {
    	set("qos-dscp", qosDscp);
    	return this;
    }
    
    public boolean isSendStreamHeaderEnabled() {
    	return get("send-streamheader");
    }
    
    public TcpServerSink setSendStreamHeaderEnabled(boolean enabled) {
    	set("send-streamheader", enabled);
    	return this;
    }

    public long getNumHandles() {
    	return get("busrt-value");
    }
    
    public boolean isSendDispatchedEnabled() {
    	return get("send-dispatched");
    }
    
    public TcpServerSink setSendDispatchedEnabled(boolean enabled) {
    	set("send-dispatched", enabled);
    	return this;
    }

    public boolean isSendMessagesEnabled() {
    	return get("send-messages");
    }
    
    public TcpServerSink setSendMessagesEnabled(boolean enabled) {
    	set("send-messages", enabled);
    	return this;
    }

    public String getHost() {
    	return get("host");
    }
    
    public TcpServerSink setHost(String host) {
    	set("host", host);
    	return this;
    }

    public Integer getPort() {
    	return get("port");
    }

    public TcpServerSink setPort(int port) {
    	set("port", port);
    	return this;
    }
    
    public Integer getCurrentPort() {
    	return get("current-port");
    }

}
