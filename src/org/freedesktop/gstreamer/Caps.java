/*
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2009 Levente Farkas
 * Copyright (C) 2007 Wayne Meissner
 * Copyright (C) <2003> David A. Schleef <ds@schleef.org>
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


import org.freedesktop.gstreamer.glib.Natives;
import static org.freedesktop.gstreamer.lowlevel.GstCapsAPI.GSTCAPS_API;

/**
 * Structure describing sets of media formats
 * <p>
 * Caps (capabilities) are lightweight objects describing media types. They are
 * composed of an array of {@link Structure}.
 * <p>
 * See upstream documentation at
 * <a href="https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstCaps.html"
 * >https://gstreamer.freedesktop.org/data/doc/gstreamer/stable/gstreamer/html/GstCaps.html</a>
 * <p>
 * Caps are exposed on {@link PadTemplate} to describe all possible types a
 * given pad can handle. They are also stored in the {@link Registry} along with
 * a description of the {@link Element}.
 * <p>
 * Caps are exposed on the element pads using the {@link Pad#getCaps} method.
 * This method describes the possible types that the pad can handle or produce
 * at runtime.
 * <p>
 * Caps are also attached to buffers to describe the content of the data pointed
 * to by the buffer with {@link Buffer#setCaps}. Caps attached to a
 * {@link Buffer} allow for format negotiation upstream and downstream.
 * <p>
 * A Caps can be constructed with the following code fragment:
 * <p>
 * <code>
 *  Caps caps = Caps.fromString("video/x-raw-rgb, bpp=32, depth=24, width=640, height=480");
 * </code>
 * <p>
 * A Caps is fixed when it has no properties with ranges or lists. Use
 * {@link #isFixed} to test for fixed caps. Only fixed caps can be set on a
 * {@link Pad} or {@link Buffer}.
 * <p>
 * Various methods exist to work with the media types such as subtracting or
 * intersecting.
 *
 * @see Structure
 */
public class Caps extends MiniObject {

    public static final String GTYPE_NAME = "GstCaps";

    /**
     * Creates a new Caps that is empty. That is, the returned Caps contains no
     * media formats.
     *
     * @see #emptyCaps
     */
    public Caps() {
        this(Natives.initializer(GSTCAPS_API.ptr_gst_caps_new_empty()));
    }

    /**
     * Construct a new Caps from a string representation.
     *
     * @param caps The string representation of the caps.
     * @see #fromString
     */
    public Caps(String caps) {
        this(Natives.initializer(GSTCAPS_API.ptr_gst_caps_from_string(caps)));
    }

    /**
     * Create a caps that is a copy of another caps.
     *
     * @param caps The caps to copy.
     * @see #copy
     */
    public Caps(Caps caps) {
        this(Natives.initializer(GSTCAPS_API.ptr_gst_caps_copy(caps)));
    }

    Caps(Initializer init) {
        super(init);
    }

    /**
     * Append the structures contained in caps to this caps object. The
     * structures in caps are not copied -- they are transferred to this caps.
     * <p>
     * If either caps is ANY, the resulting caps will be ANY.
     *
     * @param caps The Caps to append
     */
    public void append(Caps caps) {
        GSTCAPS_API.gst_caps_append(this, caps);
    }

    /**
     * Append structure to this caps. The structure is not copied; this caps
     * takes ownership, so do not use struct after calling this method.
     *
     * @param struct The structure to append.
     */
    public void append(Structure struct) {
        GSTCAPS_API.gst_caps_append_structure(this, struct);
    }

    /**
     * Create a new Caps as a copy of the this caps.
     *
     * The new Caps will be a copy of this caps, with all the internal
     * structures copied as well.
     *
     * @return The new Caps.
     */
    public Caps copy() {
        return GSTCAPS_API.gst_caps_copy(this);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Caps)) {
            return false;
        }
        return other == this || isEqual((Caps) other);
    }

    /**
     * Get a {@link Structure} contained in this caps.
     *
     * Finds the structure in @caps that has the index @index, and returns it.
     *
     * @param index The index of the structure to get.
     * @return The Structure corresponding to index.
     */
    public Structure getStructure(int index) {
        /*
         * WARNING: This function takes a const GstCaps *, but returns a
         * non-const GstStructure *.  This is for programming convenience --
         * the caller should be aware that structures inside a constant
         * #GstCaps should not be modified.
         */
        // The above means we return a Structure proxy which does not own the pointer.
        // gst_caps_get_structure is not marked as CallerOwnsReturn, so it should work
        return GSTCAPS_API.gst_caps_get_structure(this, index);
    }

    /**
     * Creates a new {@link Caps} that contains all the formats that are common
     * to both this Caps and the other Caps.
     *
     * @param other The {@link Caps} to intersect with this one.
     *
     * @return The new {@link Caps}
     */
    public Caps intersect(Caps other) {
        return GSTCAPS_API.gst_caps_intersect(this, other);
    }

    /**
     * Check if this caps is always compatible with another caps.
     * <p>
     * A given Caps structure is always compatible with another if every media
     * format that is in the first is also contained in the second. That is,
     * this caps1 is a subset of other.
     *
     * @param other The caps to test against.
     * @return true if other is always compatible with this caps.
     */
    public boolean isAlwaysCompatible(Caps other) {
        return GSTCAPS_API.gst_caps_is_always_compatible(this, other);
    }

    /**
     * Determine if this caps represents any media format.
     *
     * @return true if this caps represents any format.
     */
    public boolean isAny() {
        return GSTCAPS_API.gst_caps_is_any(this);
    }

    /**
     * Determine if this caps represents no media formats.
     *
     * @return true if this caps represents no formats.
     */
    public boolean isEmpty() {
        return GSTCAPS_API.gst_caps_is_empty(this);
    }

    /**
     * Checks if the given caps represent the same set of caps.
     * <p>
     * This function does not work reliably if optional properties for caps are
     * included on one caps and omitted on the other.
     *
     * @param other The caps to compare this caps to.
     * @return true if other caps equals this one.
     */
    public boolean isEqual(Caps other) {
        return GSTCAPS_API.gst_caps_is_equal(this, other);
    }

    /**
     * Tests if two Caps are equal. This function only works on fixed Caps.
     *
     * @param other The other caps to test against.
     * @return true if the other caps is equal to this one.
     */
    public boolean isEqualFixed(Caps other) {
        return GSTCAPS_API.gst_caps_is_equal_fixed(this, other);
    }

    /**
     * Determine if this caps is fixed.
     * <p>
     * Fixed Caps describe exactly one format, that is, they have exactly one
     * structure, and each field in the structure describes a fixed type.
     * Examples of non-fixed types are GST_TYPE_INT_RANGE and GST_TYPE_LIST.
     *
     * @return true if this caps is fixed
     */
    public boolean isFixed() {
        return GSTCAPS_API.gst_caps_is_fixed(this);
    }

    /**
     * Checks if all caps represented by this caps are also represented by
     * superset.
     * <p>
     * This function does not work reliably if optional properties for caps are
     * included on one caps and omitted on the other.
     *
     * @param superset The potentially greater Caps
     * @return true if this caps is a subset of superset
     */
    public boolean isSubset(Caps superset) {
        return GSTCAPS_API.gst_caps_is_subset(this, superset);
    }

    /**
     *
     * Returns a writable copy of this caps.
     * <p>
     * This method will invalidate the native side of this caps object, so it
     * should not be used after calling this method, and only the returned Caps
     * object should be used.
     * <p>
     *
     * @return A writable version of this caps object.
     */
    // @TODO should this take a ref ?
    public Caps makeWritable() {
        return GSTCAPS_API.gst_caps_make_writable(this);
    }

    /**
     * Normalize the Caps.
     *
     * Creates a new {@link Caps} that represents the same set of formats as
     * this Caps, but contains no lists. Each list is expanded into separate
     * {@link Structure}s
     *
     * @return The new {@link Caps}
     * @see Structure
     */
    public Caps normalize() {
//        this.ref(); // gst_caps_normalize copies "this" and drops one reference
        Natives.ref(this);
        return GSTCAPS_API.gst_caps_normalize(this);
    }

    /**
     * Remove a structure from the caps. Removes the structure with the given
     * index from the list of structures contained in this caps.
     *
     * @param index Index of the structure to remove.
     */
    public void removeStructure(int index) {
        GSTCAPS_API.gst_caps_remove_structure(this, index);
    }

    public void setInteger(String field, Integer value) {
        GSTCAPS_API.gst_caps_set_simple(this, field, value, null);
    }

    /**
     * Modifies this caps inplace into a representation that represents the same
     * set of formats, but in a simpler form. Component structures that are
     * identical are merged. Component structures that have values that can be
     * merged are also merged.
     *
     * @return The new {@link Caps}
     */
    public Caps simplify() {
//        this.ref(); // gst_caps_simplify copies "this" and drops one reference
        Natives.ref(this);
        return GSTCAPS_API.gst_caps_simplify(this);
    }

    /**
     * Get the number of structures contained in this caps.
     *
     * @return the number of structures that this caps contains
     */
    public int size() {
        return GSTCAPS_API.gst_caps_get_size(this);
    }

    /**
     * Subtracts the subtrahend Caps from this Caps.
     *
     * <note>This function does not work reliably if optional properties for
     * caps are included on one caps and omitted on the other.</note>
     *
     * @param subtrahend The {@link Caps} to subtract.
     * @return The resulting caps.
     */
    public Caps subtract(Caps subtrahend) {
        return GSTCAPS_API.gst_caps_subtract(this, subtrahend);
    }

    @Override
    public String toString() {
        return GSTCAPS_API.gst_caps_to_string(this);
    }

    /**
     * Destructively discard all but the first structure from this caps.
     *
     * Useful when fixating. This caps must be writable.
     *
     * @return truncated copy of the Caps
     */
    public Caps truncate() {
//        this.ref();
        Natives.ref(this);
        return GSTCAPS_API.gst_caps_truncate(this);
    }

    /**
     * Creates a new Caps that indicates that it is compatible with any media
     * format.
     *
     * @return The new Caps.
     */
    public static Caps anyCaps() {
        return new Caps(Natives.initializer(GSTCAPS_API.ptr_gst_caps_new_any()));
    }

    /**
     * Creates a new Caps that is empty. That is, the returned Caps contains no
     * media formats.
     *
     * @return The new Caps.
     */
    public static Caps emptyCaps() {
        return new Caps(Natives.initializer(GSTCAPS_API.ptr_gst_caps_new_empty()));
    }

    /**
     * Construct a new Caps from a string representation. Example:
     * <p>
     * <code>
     *  Caps caps = Caps.fromString("video/x-raw, format=RGB, bpp=32, depth=24, width=640, height=480");
     * </code>
     *
     * @param caps The string representation of the caps.
     * @return The new Caps.
     */
    public static Caps fromString(String caps) {
        return new Caps(Natives.initializer(GSTCAPS_API.ptr_gst_caps_from_string(caps)));
    }

    /**
     * Merge two {@link Caps} together.
     * <p>
     * Appends the structures contained in caps2 to caps1 if they are not yet
     * expressed by caps1 . The structures in caps2 are not copied -- they are
     * transferred to a writable copy of caps1 , and then caps2 is freed. If
     * either caps is ANY, the resulting caps will be ANY.
     * </p>
     *
     * @param caps1 the {@link Caps} that will take the new entries.
     * @param caps2 the {@link Caps} to merge in
     * @return merged Caps
     */
    public static Caps merge(Caps caps1, Caps caps2) {
        return GSTCAPS_API.gst_caps_merge(caps1, caps2);
    }

}
