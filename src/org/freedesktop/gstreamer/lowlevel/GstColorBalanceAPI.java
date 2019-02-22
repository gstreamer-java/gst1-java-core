/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2009 Levente Farkas
 * Copyright (c) 2009 Tamas Korodi <kotyo@zamba.fm>
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.freedesktop.gstreamer.lowlevel;

import org.freedesktop.gstreamer.interfaces.ColorBalance;
import org.freedesktop.gstreamer.interfaces.ColorBalanceChannel;
import org.freedesktop.gstreamer.lowlevel.GlibAPI.GList;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import java.util.Arrays;
import java.util.List;

public interface GstColorBalanceAPI extends Library {
	GstColorBalanceAPI GSTCOLORBALANCE_API = GstNative.load("gstvideo", GstColorBalanceAPI.class);

	GType gst_color_balance_channel_get_type();
	GType gst_color_balance_get_type();

	/* vitrual class functions */
	GList gst_color_balance_list_channels(ColorBalance balance);

	void gst_color_balance_set_value(ColorBalance balance, ColorBalanceChannel channel, int value);

	int gst_color_balance_get_value(ColorBalance balance, ColorBalanceChannel channel);

	public static final class ColorBalanceChannelStruct extends com.sun.jna.Structure {
		public volatile GObjectAPI.GObjectStruct parent;
		public volatile String label;
		public volatile int min_value;
		public volatile int max_value;

		public String getLabel() {
			return (String) readField("label");
		}
		public int getMinValue() {
			return (Integer) readField("min_value");
		}
		public int getMaxValue() {
			return (Integer) readField("max_value");
		}
		public void read() {}
		public void write() {}
		public ColorBalanceChannelStruct(Pointer ptr) {
			useMemory(ptr);
		}

                @Override
                protected List<String> getFieldOrder() {
                    return Arrays.asList(new String[]{
                        "parent", "label", "min_value",
                        "max_value"
                    });
                }
	}
}
