/*
 * Copyright (c) 2018 Antonio Morales
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License version 3 only, as published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3 along with
 * this work. If not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.gstreamer.lowlevel; 

import java.util.Arrays;
import java.util.List;

import org.freedesktop.gstreamer.SDPResult;
import org.freedesktop.gstreamer.SDPMessage;
import org.freedesktop.gstreamer.lowlevel.GValueAPI.GValueArray;

import com.sun.jna.Pointer;

/**
 * GstSDPMessage methods and structures
 *
 * @see https://github.com/GStreamer/gst-plugins-base/blob/master/gst-libs/gst/sdp/gstsdpmessage.h
 */
public interface GstSDPMessageAPI extends com.sun.jna.Library {
  GstSDPMessageAPI GSTSDPMESSAGE_API = GstNative.load("gstsdp", GstSDPMessageAPI.class);

  public static final class SDPMessageStruct extends com.sun.jna.Structure {
    public volatile String version;
    public volatile SDPOriginStruct origin;
    public volatile String session_name;
    public volatile String information;
    public volatile String uri;
    public volatile GValueArray emails;
    public volatile GValueArray phones;
    public volatile SDPConnectionStruct connection;
    public volatile GValueArray bandwidths;
    public volatile GValueArray times;
    public volatile GValueArray zones;
    public volatile SDPKeyStruct key;
    public volatile GValueArray attributes;
    public volatile GValueArray medias;

    public SDPMessageStruct(final Pointer ptr) {
        useMemory(ptr);
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(new String[] {
          "version", "origin", "session_name", "information", "uri",
          "emails", "phones", "connection", "bandwidths", "times",
          "zones", "key", "attributes", "medias"
        });
    }
  }

  public static final class SDPOriginStruct extends com.sun.jna.Structure {
    public volatile String username;
    public volatile String sess_id;
    public volatile String sess_version;
    public volatile String nettype;
    public volatile String addrtype;
    public volatile String addr;

    public SDPOriginStruct(final Pointer ptr) {
        useMemory(ptr);
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(new String[] {
          "username", "sess_id", "sess_version", "nettype", "addrtype", "addr" 
        });
    }
  }

  public static final class SDPKeyStruct extends com.sun.jna.Structure {
    public volatile String type;
    public volatile String data;

    public SDPKeyStruct(final Pointer ptr) {
        useMemory(ptr);
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(new String[] { "type", "data" });
    }
  }

  public static final class SDPConnectionStruct extends com.sun.jna.Structure {
    public volatile String nettype;
    public volatile String addrtype;
    public volatile String address;
    public volatile int ttl;
    public volatile int addr_number;

    public SDPConnectionStruct(final Pointer ptr) {
        useMemory(ptr);
    }

    @Override 
    protected List<String> getFieldOrder() {
        return Arrays.asList(new String[] {
          "nettype", "addrtype", "address", "ttl", "addr_number" 
        });
    }
  }

  SDPResult gst_sdp_connection_set(SDPConnectionStruct conn, String nettype, String address, int ttl, int addr_number);
  SDPResult gst_sdp_connection_clear(SDPConnectionStruct conn);

  SDPResult gst_sdp_message_free(Pointer msg);
  SDPResult ptr_gst_sdp_message_free(Pointer msg);

  SDPResult gst_sdp_message_new(Pointer[] msg);

  SDPResult gst_sdp_message_parse_buffer(byte[] data, int size, Pointer[] msg);
  SDPResult gst_sdp_message_parse_buffer(byte[] data, int size, SDPMessage msg);
  SDPResult gst_sdp_message_copy(SDPMessage msg, Pointer[] copy);

  String gst_sdp_message_as_text(SDPMessage msg);
}
