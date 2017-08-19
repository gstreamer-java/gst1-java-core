/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freedesktop.gstreamer.device;

import java.util.ArrayList;
import java.util.List;
import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.GstObject;
import org.freedesktop.gstreamer.Plugin;
import org.freedesktop.gstreamer.lowlevel.GType;
import org.freedesktop.gstreamer.lowlevel.GlibAPI;
import org.freedesktop.gstreamer.lowlevel.GstDeviceProviderAPI;
import org.freedesktop.gstreamer.lowlevel.GstNative;

/**
 *
 * @author andres
 */
public class DeviceProvider extends GstObject {
    public static final String GTYPE_NAME = "GstDeviceProvider";
    
    private static final GstDeviceProviderAPI gst = GstNative.load(GstDeviceProviderAPI.class);

    public DeviceProvider(Initializer init) {
        super(init);
    }
    
    public boolean canMonitor() {
        return gst.gst_device_provider_can_monitor(this);
    }

    public void addMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass, String key, String value) {
        gst.gst_device_provider_class_add_metadata(klass, key, value);        
    }
    
    public void addStaticMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass, String key, String value) {
        gst.gst_device_provider_class_add_static_metadata(klass, key, value);
    }
    
    public String getMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass, String key) {
        return gst.gst_device_provider_class_get_metadata(klass, key);
    }
    
    public void setMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass,
                            String longname, String classification, String description, String author) {
        gst.gst_device_provider_class_set_metadata(klass, longname, classification, description, author);
    }
    
    public void setStaticMetadata(GstDeviceProviderAPI.GstDeviceProviderClass klass,
                                  String longname, String classification, String description, String author) {
        gst.gst_device_provider_class_set_static_metadata(klass, longname, classification, description, author);
    }
    
    public void add(Device device) {
        gst.gst_device_provider_device_add(this, device);
    }
    
    public void remove(Device device) {
        gst.gst_device_provider_device_remove(this, device);  
    }
    
    public Bus getBus() {
        return gst.gst_device_provider_get_bus(this);
    }
    
    public List<Device> getDevices() {
        GlibAPI.GList glist = gst.gst_device_provider_get_devices(this);        
        List<Device> list = new ArrayList<Device>();

        GlibAPI.GList next = glist;
        while (next != null) {
            if (next.data != null) {
                Device dev = new Device(initializer(next.data, true, true));
                list.add(dev);
            }
            next = next.next();
        }
        
        return list;        
    }
    
    public DeviceProviderFactory getFactory() {
        return gst.gst_device_provider_get_factory(this);
    }

    public boolean register(Plugin plugin, String name, int rank, GType type) {
        return gst.gst_device_provider_register(plugin, name, rank, type);
    }
    
    public boolean start() {
        return gst.gst_device_provider_start(this);
    }

    public void stop() {
        gst.gst_device_provider_stop(this);
    }    
}
