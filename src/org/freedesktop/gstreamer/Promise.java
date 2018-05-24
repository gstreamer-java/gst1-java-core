/*
 * Copyright (c) 2015 Neil C Smith Copyright (c) 2007,2008 Wayne Meissner Copyright (C) 1999,2000
 * Erik Walthinsen <omega@cse.ogi.edu> 2004,2005 Wim Taymans <wim@fluendo.com>
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

package org.freedesktop.gstreamer;

import static org.freedesktop.gstreamer.lowlevel.GstPromiseAPI.*;

import org.freedesktop.gstreamer.lowlevel.GstAPI.GstCallback;

import com.sun.jna.Pointer;

/**

 */
public class Promise extends MiniObject
{
  public static final String GTYPE_NAME = "GstPromise";

  public Promise(final Initializer init)
  {
    super(init);
  }

  public Promise(final GstCallback callback)
  {
    this(initializer(GSTPROMISE_API.ptr_gst_promise_new_with_change_func(callback)));
  }

  protected static Initializer initializer(final Pointer ptr)
  {
    return new Initializer(ptr, false, true);
  }

  public void waitResult()
  {
    GSTPROMISE_API.gst_promise_wait(this);
  }

  public void reply(final Structure s)
  {
    GSTPROMISE_API.gst_promise_reply(this, s);
  }

  public void interrupt()
  {
    GSTPROMISE_API.gst_promise_interrupt(this);
  }

  public void expire()
  {
    GSTPROMISE_API.gst_promise_expire(this);
  }

  public Structure getReply()
  {
    return GSTPROMISE_API.gst_promise_get_reply(this);
  }

}
