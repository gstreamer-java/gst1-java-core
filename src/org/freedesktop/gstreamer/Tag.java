/* 
 * Copyright (C) 2007 Wayne Meissner
 * Copyright (C) 2003 Benjamin Otte
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

package org.freedesktop.gstreamer;

/**
 * GStreamer core tags
 */
enum Tag {
    /** 
     * The artist name as it should be displayed, e.g. 'Jimi Hendrix' or
     * 'The Guitar Heroes'
     */
    ARTIST("artist"),
    /**
     * The artist name as it should be sorted, e.g. 'Hendrix, Jimi' or
     * 'Guitar Heroes, The'
     */
    ARTIST_SORTNAME("musicbrainz-sortname"),
    /** The title as it should be displayed, e.g. 'The Doll House' */
    TITLE("title"),
    /** The title as it should be sorted, e.g. 'Doll House, The' */
    TITLE_SORTNAME("title-sortname"),
    /** The album name as it should be displayed, e.g. 'The Jazz Guitar' */
    ALBUM("album"),
    /** The album name as it should be sorted, e.g. 'Jazz Guitar, The' */
    ALBUM_SORTNAME("album-sortname"),
    /** Person(s) who composed the recording */
    COMPOSER("composer"),
    /** Genre of the media */
    GENRE("genre"),
    COMMENT("comment"),
    EXTENDED_COMMENT("extended-comment"),
    /** Original location of file as a URI */
    LOCATION("location"),
    /** Short text describing the content of the data */ 
    DESCRIPTION("description"),
    /** Version of this data */
    VERSION("version"),
    /** Organization */
    ORGANIZATION("organization"),
    /** Copyright notice for the data */
    COPYRIGHT("copyright"),
    /** URI to location where copyright details can be found  */
    COPYRIGHT_URI("copyright-uri"),
    /** Contact information */
    CONTACT("contact"),
    /** License of data */
    LICENSE("license"),
    /** URI to location where license details can be found */
    LICENSE_URI("license-uri"),
    /** Person(s) performing */
    PERFORMER("performer"),
    /** Codec the data is stored in  */
    CODEC("codec"),
    /** Codec the audio data is stored in */
    AUDIO_CODEC("audio-codec"),
    /** Codec the video data is stored in */
    VIDEO_CODEC("video-codec"),
    /** Encoder used to encode this stream */
    ENCODER("encoder"),
    /** Version of the encoder used to encode this stream */
    ENCODER_VERSION("encoder-version"),
    /** Language code (ISO-639-1) String */
    LANGUAGE_CODE("language-code"),
    /** Track number inside a collection. */
    TRACK_NUMBER("track-number"),
    /** Count of tracks inside collection this track belongs to. */
    TRACK_COUNT("track-count"),
    /** Disc number inside a collection. */
    ALBUM_VOLUME_NUMBER("album-disc-number"),
    /** Count of discs inside collection this disc belongs to. */
    ALBUM_VOLUME_COUNT("album-disc-count"),
    /** Exact or average bitrate in bits per second */
    BITRATE("bitrate"),
    /** Nominal bitrate in bits/s */
    NOMINAL_BITRATE("nominal-bitrate"),
    /** Minimum bitrate in bits/s */
    MINIMUM_BITRATE("minimum-bitrate"),
    /** Maximum bitrate in bits/s */
    MAXIMUM_BITRATE("maximum-bitrate"),
    /** Track gain in db */
    TRACK_GAIN("replaygain-track-gain"),
    /** Peak of the track */
    TRACK_PEAK("replaygain-track-peak"),
    /** Album gain in db */
    ALBUM_GAIN("replaygain-album-gain"),
    /** Peak of the album */
    ALBUM_PEAK("replaygain-album-peak"),
    /** Reference level of track and album gain values */
    REFERENCE_LEVEL("replaygain-reference-level"),
    /** Serial number of track */
    SERIAL("serial"),
    /** date the data was created */
    DATE("date"),
    /** Length in GStreamer time units (nanoseconds) */
    DURATION("duration"),
    ISRC("isrc"),
    /** Image */
    IMAGE("image"),
    /** Image that is meant for preview purposes */
    PREVIEW_IMAGE("preview-image"),
    /** Number of beats per minute in audio */
    BEATS_PER_MINUTE("beats-per-minute");
    
    Tag(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }    
    private String id;

   
}
