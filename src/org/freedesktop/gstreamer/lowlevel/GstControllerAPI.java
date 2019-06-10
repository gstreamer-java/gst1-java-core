/* 
 * Copyright (c) 2019 Neil C Smith
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

import com.sun.jna.Library;
import org.freedesktop.gstreamer.lowlevel.annotations.CallerOwnsReturn;

/**
 * GstController API functions
 * 
 * https://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer-libs/html/GstTimedValueControlSource.html
 * https://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer-libs/html/GstInterpolationControlSource.html
 * https://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer-libs/html/GstLFOControlSource.html
 * https://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer-libs/html/GstTriggerControlSource.html
 * https://gstreamer.freedesktop.org/data/doc/gstreamer/head/gstreamer-libs/html/GstDirectControlBinding.html
 * 
 */
public interface GstControllerAPI extends Library {
    
    GstControllerAPI GSTCONTROLLER_API = GstNative.load("gstcontroller", GstControllerAPI.class);
    
    @CallerOwnsReturn GstTriggerControlSourcePtr gst_trigger_control_source_new();
    
    @CallerOwnsReturn GstInterpolationControlSourcePtr gst_interpolation_control_source_new();
    
    @CallerOwnsReturn GstLFOControlSourcePtr gst_lfo_control_source_new();
    
    @CallerOwnsReturn GstDirectControlBindingPtr gst_direct_control_binding_new(
            GstObjectPtr object,
            String property_name,
            GstControlSourcePtr cs);
    
    @CallerOwnsReturn GstDirectControlBindingPtr gst_direct_control_binding_new_absolute(
            GstObjectPtr object,
            String property_name,
            GstControlSourcePtr cs);
    
    @CallerOwnsReturn GstARGBControlBindingPtr gst_argb_control_binding_new(
            GstObjectPtr object,
            String property_name,
            GstControlSourcePtr cs_a,
            GstControlSourcePtr cs_r,
            GstControlSourcePtr cs_g,
            GstControlSourcePtr cs_b);
    
    // since 1.12
    @CallerOwnsReturn GstProxyControlBindingPtr gst_proxy_control_binding_new(
            GstObjectPtr object,
            String property_name,
            GstObjectPtr ref_object,
            String ref_property_name); 
            
    
    // GSequenceIter gst_timed_value_control_source_find_control_point_iter(
    //                        GstTimedValueControlSourcePtr self,
    //                        long timestamp);
    
    boolean gst_timed_value_control_source_set(GstTimedValueControlSourcePtr self,
            long timestamp,
            double value);
    
    boolean gst_timed_value_control_source_set_from_list(
                GstTimedValueControlSourcePtr self,
                GlibAPI.GSList timedvalues);

    // transfer container
    // caller owns list, not contained values!
    @CallerOwnsReturn GlibAPI.GList gst_timed_value_control_source_get_all(
            GstTimedValueControlSourcePtr self);
    
    boolean gst_timed_value_control_source_unset(GstTimedValueControlSourcePtr self,
            long timestamp);
    
    void gst_timed_value_control_source_unset_all(GstTimedValueControlSourcePtr self);
    
    int gst_timed_value_control_source_get_count(GstTimedValueControlSourcePtr self);
    
    void gst_timed_value_control_invalidate_cache(GstTimedValueControlSourcePtr self);
    
    // gst_control_point_copy
    // gst_control_point_free
    
}
