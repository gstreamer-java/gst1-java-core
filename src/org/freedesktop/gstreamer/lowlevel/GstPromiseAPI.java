/*
 * Copyright (c) 2018 Vinicius Tona
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

import org.freedesktop.gstreamer.Promise;
import org.freedesktop.gstreamer.PromiseResult;
import org.freedesktop.gstreamer.Structure;
import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;
import org.freedesktop.gstreamer.lowlevel.GstMiniObjectAPI.MiniObjectStruct;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

import com.sun.jna.Pointer;

/**
 * GstPromise methods and structures
 * 
 * @see https://github.com/GStreamer/gstreamer/blob/master/gst/gstpromise.h
 * available since GStreamer 1.14
 */
public interface GstPromiseAPI extends com.sun.jna.Library {
  GstPromiseAPI GSTPROMISE_API = GstNative.load(GstPromiseAPI.class);

  public static final class PromiseStruct extends com.sun.jna.Structure {
    public volatile MiniObjectStruct parent;

    public PromiseStruct() {
    }

    public PromiseStruct(Pointer ptr) {
      useMemory(ptr);
    }

    @Override
    protected List<String> getFieldOrder() {
      return Arrays.asList(new String[] { "parent" });
    }
  }

  GType gst_promise_get_type();
  GType gst_static_promise_get_type();

  @CallerOwnsReturn Promise gst_promise_new();
  @CallerOwnsReturn Pointer ptr_gst_promise_new();

  @CallerOwnsReturn Promise gst_promise_new_with_change_func(GstCallback callback);
  @CallerOwnsReturn Pointer ptr_gst_promise_new_with_change_func(GstCallback callback);

  PromiseResult gst_promise_wait(Promise promise);

  void gst_promise_reply(Promise promise, Structure s);
  void gst_promise_interrupt(Promise promise);
  void gst_promise_expire(Promise promise);

  @CallerOwnsReturn Structure gst_promise_get_reply(Promise promise);
  @CallerOwnsReturn Pointer ptr_gst_promise_get_reply(Promise promise);
}
