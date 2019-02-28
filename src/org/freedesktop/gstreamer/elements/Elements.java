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
package org.freedesktop.gstreamer.elements;

import java.util.stream.Stream;
import org.freedesktop.gstreamer.glib.NativeObject;
import static org.freedesktop.gstreamer.glib.Natives.registration;

/**
 *
 * @author Neil C Smith - https://www.neilcsmith.net
 */
public class Elements implements NativeObject.TypeProvider {

    @Override
    public Stream<NativeObject.TypeRegistration<?>> types() {
        return Stream.of(
                registration(AppSink.class, AppSink.GTYPE_NAME, AppSink::new),
                registration(AppSrc.class, AppSrc.GTYPE_NAME, AppSrc::new),
                registration(BaseSrc.class, BaseSrc.GTYPE_NAME, BaseSrc::new),
                registration(BaseSink.class, BaseSink.GTYPE_NAME, BaseSink::new),
                registration(BaseTransform.class, BaseTransform.GTYPE_NAME, BaseTransform::new),
                registration(DecodeBin.class, DecodeBin.GTYPE_NAME, DecodeBin::new),
                registration(PlayBin.class, PlayBin.GTYPE_NAME, PlayBin::new),
                registration(URIDecodeBin.class, URIDecodeBin.GTYPE_NAME, URIDecodeBin::new));

    }

}
