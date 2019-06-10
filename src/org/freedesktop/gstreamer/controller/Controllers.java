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
package org.freedesktop.gstreamer.controller;

import java.util.stream.Stream;
import org.freedesktop.gstreamer.glib.NativeObject;
import static org.freedesktop.gstreamer.glib.Natives.registration;

/**
 *  Controllers registration
 */
public class Controllers implements NativeObject.TypeProvider {

    @Override
    public Stream<NativeObject.TypeRegistration<?>> types() {
        return Stream.of(
                registration(ARGBControlBinding.class,
                        ARGBControlBinding.GTYPE_NAME,
                        ARGBControlBinding::new),
                registration(DirectControlBinding.class,
                        DirectControlBinding.GTYPE_NAME,
                        DirectControlBinding::new),
                registration(ProxyControlBinding.class,
                        ProxyControlBinding.GTYPE_NAME,
                        ProxyControlBinding::new),
                registration(InterpolationControlSource.class,
                        InterpolationControlSource.GTYPE_NAME,
                        InterpolationControlSource::new),
                registration(TriggerControlSource.class,
                        TriggerControlSource.GTYPE_NAME,
                        TriggerControlSource::new),
                registration(LFOControlSource.class,
                        LFOControlSource.GTYPE_NAME,
                        LFOControlSource::new),
                registration(TimedValueControlSource.class,
                        TimedValueControlSource.GTYPE_NAME,
                        TimedValueControlSource::new)
        );
    }

}
