/* 
 * Copyright (c) 2019 Neil C Smith
 * Copyright (c) 2007,2008 Wayne Meissner
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
 * Represents a fraction value.
 */
public class Fraction {
    public final int numerator;
    public final int denominator;
    
    /**
     * Creates a new {@code Fraction}.
     * 
     * @param numerator the numerator value.
     * @param denominator the denominator value.
     */
    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }
    
    /**
     * Gets the numerator of the {@code Fraction}
     * 
     * @return the numerator as an integer.
     */
    public int getNumerator() {
        return numerator;
    }
    
    /**
     * Gets the denominator of the {@code Fraction}
     * 
     * @return the denominator as an integer.
     */
    public int getDenominator() {
        return denominator;
    }
    
    /**
     * Returns the fraction as a floating point value (numerator divided by the denominator).
     * @return fraction as a <code>double</code>, or <code>Double.NaN</code> if the denominator is 0
     */
    public double toDouble() {
    	return denominator != 0 ? ((double) numerator / denominator) : Double.NaN;
    }
    
}
