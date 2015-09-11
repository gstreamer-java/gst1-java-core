/*
 * Copyright (c) 2015 Neil C Smith
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

package org.gstreamer;

import org.gstreamer.lowlevel.GstCapsAPI;
import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.RefCountedObject;

import com.sun.jna.Pointer;

/**
 * Structure describing sets of media formats
 * <p>
 * Caps (capabilities) are lightweight objects describing media types.
 * They are composed of an array of {@link Structure}.
 * <p>
 * Caps are exposed on {@link PadTemplate} to describe all possible types a
 * given pad can handle. They are also stored in the {@link Registry} along with
 * a description of the {@link Element}.
 * <p>
 * Caps are exposed on the element pads using the {@link Pad#getCaps} method. 
 * This method describes the possible types that the pad can handle or produce at runtime.
 * <p>
 * Caps are also attached to buffers to describe the content of the data
 * pointed to by the buffer with {@link Buffer#setCaps}. Caps attached to
 * a {@link Buffer} allow for format negotiation upstream and downstream.
 * <p>
 * A Caps can be constructed with the following code fragment:
 * <p>
 *  <code>
 *  Caps caps = Caps.fromString("video/x-raw-rgb, bpp=32, depth=24, width=640, height=480");
 *  </code>
 * <p>
 * A Caps is fixed when it has no properties with ranges or lists. Use
 * {@link #isFixed} to test for fixed caps. Only fixed caps can be
 * set on a {@link Pad} or {@link Buffer}.
 * <p>
 * Various methods exist to work with the media types such as subtracting
 * or intersecting.
 *
 * @see Structure
 */
public class Caps extends MiniObject {
    public static final String GTYPE_NAME = "GstCaps";
    
    private static final GstCapsAPI gst = GstNative.load(GstCapsAPI.class);
    
    /**
     * Creates a new Caps that is empty.  
     * That is, the returned Caps contains no media formats.
     *
     * @return The new Caps.
     */
    public static Caps emptyCaps() {
        return new Caps(initializer(gst.ptr_gst_caps_new_empty()));
    }
    
    /**
     * Creates a new Caps that indicates that it is compatible with
     * any media format.
     *
     * @return The new Caps.
     */
    public static Caps anyCaps() {
        return new Caps(initializer(gst.ptr_gst_caps_new_any()));
    }
    /**
     * Construct a new Caps from a string representation.
     * Example:
     * <p>
     * <code>
     *  Caps caps = Caps.fromString("video/x-raw, format=RGB, bpp=32, depth=24, width=640, height=480");
     * </code>
     * @param caps The string representation of the caps.
     * @return The new Caps.
     */
    public static Caps fromString(String caps) {
        return new Caps(initializer(gst.ptr_gst_caps_from_string(caps)));
    }
    
    /**
     * Creates a new Caps that is empty.  
     * That is, the returned Caps contains no media formats.
     * @see #emptyCaps
     */
    public Caps() {
        this(initializer(gst.ptr_gst_caps_new_empty()));
    }
    
    /**
     * Construct a new Caps from a string representation.
     * 
     * @param caps The string representation of the caps.
     * @see #fromString
     */
    public Caps(String caps) {
        this(initializer(gst.ptr_gst_caps_from_string(caps)));
    }
    /**
     * Create a caps that is a copy of another caps.
     * 
     * @param caps The caps to copy.
     * @see #copy
     */
    public Caps(Caps caps) {
        this(initializer(gst.ptr_gst_caps_copy(caps)));
    }
    
    protected static Initializer initializer(Pointer ptr) {
        return new Initializer(ptr, false, true);
    }
    
    public Caps(Initializer init) {
        super(init);
    }
    
    /**
     * Get the number of structures contained in this caps.
     *
     * @return the number of structures that this caps contains
     */
    public int size() {
        return gst.gst_caps_get_size(this);
    }
    
    /**
     * Create a new Caps as a copy of the this caps.
     * 
     * The new Caps will be a copy of this caps, with all the internal structures 
     * copied as well.
     *
     * @return The new Caps.
     */
    public Caps copy() {
        return gst.gst_caps_copy(this);
    }
    
    /**
     * Creates a new {@link Caps} that contains all the formats that are in
     * either this Caps or the other Caps.
     * 
     * @param other The {@link Caps} to union with this one.
     * @return The new {@link Caps}
     */
    public Caps union(Caps other) {
        return gst.gst_caps_union(this, other);
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
        return gst.gst_caps_intersect(this, other);
    }
    
    /**
     * Subtracts the subtrahend Caps from this Caps.
     * 
     * <note>This function does not work reliably if optional properties for caps
     * are included on one caps and omitted on the other.</note>
     * @param subtrahend The {@link Caps} to subtract.
     * @return The resulting caps.
     */
    public Caps subtract(Caps subtrahend) {
        return gst.gst_caps_subtract(this, subtrahend);
    }

    /**
     * Normalize the Caps.
     * 
     * Creates a new {@link Caps} that represents the same set of formats as
     * this Caps, but contains no lists.  Each list is expanded into separate
     * {@link Structure}s
     * 
     * @return The new {@link Caps}
     * @see Structure
     */
    public Caps normalize() {
        return gst.gst_caps_normalize(this);
    }
    
    /**
     * Modifies this caps inplace into a representation that represents the
     * same set of formats, but in a simpler form.  Component structures that are
     * identical are merged.  Component structures that have values that can be
     * merged are also merged.
     *
     * @return true if the caps could be simplified
     */
    public boolean simplify() {
        return gst.gst_caps_simplify(this);
    }
    
    /**
     * Append the structures contained in caps to this caps object. 
     * The structures in caps are not copied -- they are transferred to this caps.
     * <p>
     * If either caps is ANY, the resulting caps will be ANY.
     * 
     * @param caps The Caps to append
     */
    public void append(Caps caps) {
        gst.gst_caps_append(this, caps);
    }
    
    /**
     * Append structure to this caps.
     * The structure is not copied; this caps takes ownership, so do not use struct
     * after calling this method.
     * 
     * @param struct The structure to append.
     */
    public void append(Structure struct) {
        gst.gst_caps_append_structure(this, struct);
    }
    
    /**
     * Remove a struture from the caps.
     * Removes the stucture with the given index from the list of structures
     * contained in this caps.
     * 
     * @param index Index of the structure to remove.
     */
    public void removeStructure(int index) {
        gst.gst_caps_remove_structure(this, index);
    }
    
    /**
     * Merge another {@link Caps} with this one.
     * <p>
     * Appends the structures contained in the other Caps to this one, if they 
     * are not yet expressed by this Caps. 
     * <p>
     * The structures in other are not copied, they are transferred to this Caps,
     * and then other is freed.
     * <p>
     * If either caps is ANY, the resulting caps will be ANY.
     * <p><b>Do not use the argument after calling this method, as the native peer
     * is no longer valid.</b>
     * @param other The other {@link Caps} to merge.
     */
    public Caps merge(Caps other) {
        return gst.gst_caps_merge(this, other);
    }
    
    /**
     * Append a structure to this caps. 
     * Appends structure to this caps if its not already expressed.  
     * The structure is not copied; caps becomes the owner of structure and structure 
     * must not be used after calling this method.
     *
     * @param structure The structure to merge.
     */
    public void merge(Structure structure) {
        gst.gst_caps_merge_structure(this, structure);
    }
    
    /**
     *
     * Returns a writable copy of this caps.
     * <p>
     * This method will invalidate the native side of this caps object, so it should
     * not be used after calling this method, and only the returned Caps object should be used.
     * <p>
     * 
     * @return A writable version of this caps object.
     */
    public Caps makeWritable() {
        return gst.gst_caps_make_writable(this);
    }
    
    public void setInteger(String field, Integer value) {
        gst.gst_caps_set_simple(this, field, value, null);
    }
    
    /**
     * Get a {@link Structure} contained in this caps.
     * 
     * Finds the structure in @caps that has the index @index, and
     * returns it.
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
        return gst.gst_caps_get_structure(this, index);
    }
    /**
     * Destructively discard all but the first structure from this caps.
     * 
     * Useful when fixating. This caps must be writable.
     */
    public void truncate() {
        gst.gst_caps_truncate(this);
    }
    
    @Override
    public String toString() {
        return gst.gst_caps_to_string(this);
    }
    
    /**
     * Determine if this caps represents any media format.
     *
     * @return true if this caps represents any format.
     */
    public boolean isAny() {
        return gst.gst_caps_is_any(this);
    }
    
    /**
     * Determine if this caps represents no media formats.
     *
     * @return true if this caps represents no formats.
     */
    public boolean isEmpty() {
        return gst.gst_caps_is_empty(this);
    }
    
    /**
     * Determine if this caps is fixed.
     * <p>
     * Fixed Caps describe exactly one format, that is, they have exactly
     * one structure, and each field in the structure describes a fixed type.
     * Examples of non-fixed types are GST_TYPE_INT_RANGE and GST_TYPE_LIST.
     *
     * @return true if this caps is fixed
     */
    public boolean isFixed() {
        return gst.gst_caps_is_fixed(this);
    }
    
    /**
     * Checks if the given caps represent the same set of caps.
     * <p>
     * This function does not work reliably if optional properties for caps
     * are included on one caps and omitted on the other.
     *
     * @param other The caps to compare this caps to.
     * @return true if other caps equals this one.
     */
    public boolean isEqual(Caps other) {
        return gst.gst_caps_is_equal(this, other);
    }
    
    /**
     * Tests if two Caps are equal.  This function only works on fixed Caps.
     *
     * @param other The other caps to test against.
     * @return true if the other caps is equal to this one.
     */
    public boolean isEqualFixed(Caps other) {
        return gst.gst_caps_is_equal_fixed(this, other);
    }
    
    /**
     * Checks if all caps represented by this caps are also represented by superset.
     * <p>
     * This function does not work reliably if optional properties for caps
     * are included on one caps and omitted on the other.
     *
     * @param superset The potentially greater Caps
     * @return true if this caps is a subset of superset
     */
    public boolean isSubset(Caps superset) {
        return gst.gst_caps_is_subset(this, superset);
    }
    
    /**
     * Check if this caps is always compatible with another caps.
     * <p>
     * A given Caps structure is always compatible with another if every media 
     * format that is in the first is also contained in the second.  That is, 
     * this caps1 is a subset of other.
     *
     * @param other The caps to test against.
     * @return true if other is always compatible with this caps.
     */
    public boolean isAlwaysCompatible(Caps other) {
        return gst.gst_caps_is_always_compatible(this, other);
    }
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Caps)) {
            return false;
        }
        return other == this || isEqual((Caps) other);
    }
//    protected void ref() {
//        gst.gst_caps_ref(this);
//    }
//    protected void unref() {
//        gst.gst_caps_unref(this);
//    }
//    protected void disposeNativeHandle(Pointer ptr) {
//        gst.gst_caps_unref(ptr);
//    }

    
}
