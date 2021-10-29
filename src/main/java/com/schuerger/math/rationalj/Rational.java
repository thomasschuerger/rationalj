/* Copyright 2021 Thomas Schuerger (thomas@schuerger.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.schuerger.math.rationalj;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Implements an immutable arbitrary-scale rational number based on BigInteger numerators and denominators. The rational numbers are always
 * normalized, which means that they are stored completely reduced, the denominator is always positive (which means the sign is in the numerator
 * only), and the number 0 always has denominator 1. The API is similar to the one from BigInteger. Implements most of BigInteger's methods, but none
 * of its bitwise methods.
 *
 * @author Thomas Schuerger (thomas@schuerger.com)
 */

public class Rational extends Number implements Comparable<Rational> {
    /** Ten. */
    public static final Rational TEN = new Rational(BigInteger.TEN, BigInteger.ONE);

    /** Two. */
    public static final Rational TWO = new Rational(BigInteger.valueOf(2), BigInteger.ONE);

    /** One. */
    public static final Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);

    /** One half. */
    public static final Rational ONE_HALF = new Rational(BigInteger.ONE, BigInteger.valueOf(2));

    /** Zero. */
    public static final Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);

    /** The numerator. */
    private final BigInteger numerator;

    /** The denominator (always greater than 0). */
    private final BigInteger denominator;

    /** True iff this Rational is an integer. */
    private final boolean isInteger;

    /** True iff this Rational is zero. */
    private final boolean isZero;

    /** True iff this Rational is one. */
    private final boolean isOne;

    /**
     * Creates a Rational from the given numerator and denominator.
     *
     * @param numerator the numerator
     * @param denominator the denominator
     */
    private Rational(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("denominator must be non-zero");
        }

        if (numerator.equals(BigInteger.ZERO)) {
            this.numerator = numerator;
            this.denominator = BigInteger.ONE;
            this.isInteger = true;
            this.isOne = false;
            this.isZero = true;
            return;
        }

        if (denominator.signum() == -1) {
            // make sure the denominator is always positive
            numerator = numerator.negate();
            denominator = denominator.negate();
        }

        BigInteger gcd = numerator.gcd(denominator);

        if (gcd.equals(BigInteger.ONE)) {
            this.numerator = numerator;
            this.denominator = denominator;
        } else {
            // make the numerator and denominator co-prime
            this.numerator = numerator.divide(gcd);
            this.denominator = denominator.divide(gcd);
        }

        if (this.denominator.equals(BigInteger.ONE)) {
            this.isInteger = true;
            // numerator can't be zero
            this.isZero = false;
            if (numerator.equals(BigInteger.ONE)) {
                this.isOne = true;
            } else {
                this.isOne = false;
            }
        } else {
            this.isInteger = false;
            this.isOne = false;
            this.isZero = false;
        }
    }

    /**
     * Returns the numerator of this Rational.
     *
     * @return the numerator
     */
    public BigInteger numerator() {
        return numerator;
    }

    /**
     * Returns the denominator of this Rational.
     *
     * @return the denominator
     */
    public BigInteger denominator() {
        return denominator;
    }

    public static Rational of(String string) {
        int p = string.indexOf('/');

        if (p < 0) {
            return Rational.of(new BigInteger(string));
        } else {
            int p2 = string.lastIndexOf('/');

            if (p == p2) {
                return Rational.of(new BigInteger(string.substring(0, p)), new BigInteger(string.substring(p + 1)));
            } else {
                throw new IllegalArgumentException("Zero or one '/' expected");
            }
        }
    }

    /**
     * Returns a Rational based on the given numerator and denominator.
     *
     * @param numerator the numerator
     * @param denominator the denominator
     *
     * @return the Rational
     */
    public static Rational of(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("denominator must be non-zero");
        } else if (numerator == 0) {
            return ZERO;
        } else if (numerator == denominator) {
            return ONE;
        } else {
            return new Rational(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
        }
    }

    /**
     * Returns a Rational based on the given numerator and denominator.
     *
     * @param numerator the numerator
     * @param denominator the denominator
     *
     * @return the Rational
     */
    public static Rational of(long numerator, long denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("denominator must be non-zero");
        } else if (numerator == 0) {
            return ZERO;
        } else if (numerator == denominator) {
            return ONE;
        } else {
            return new Rational(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
        }
    }

    /**
     * Returns a Rational based on the given numerator and denominator.
     *
     * @param numerator the numerator
     * @param denominator the denominator
     *
     * @return the Rational
     */
    public static Rational of(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("denominator must be non-zero");
        } else if (numerator.equals(BigInteger.ZERO)) {
            return ZERO;
        } else if (numerator.equals(denominator)) {
            return ONE;
        } else {
            return new Rational(numerator, denominator);
        }
    }

    /**
     * Returns an integer Rational based on the given integer.
     *
     * @param integer the integer
     *
     * @return the Rational
     */
    public static Rational of(int integer) {
        switch (integer) {
        case 0:
            return ZERO;
        case 1:
            return ONE;
        case 2:
            return TWO;
        case 10:
            return TEN;
        default:
            return new Rational(BigInteger.valueOf(integer), BigInteger.ONE);
        }
    }

    /**
     * Returns an integer Rational based on the given integer.
     *
     * @param integer the integer
     *
     * @return the Rational
     */
    public static Rational of(long integer) {
        if (integer == 0) {
            return ZERO;
        } else if (integer == 1) {
            return ONE;
        } else if (integer == 2) {
            return TWO;
        } else if (integer == 10) {
            return TEN;
        } else {
            return new Rational(BigInteger.valueOf(integer), BigInteger.ONE);
        }
    }

    /**
     * Returns an integer Rational based on the given integer.
     *
     * @param integer the integer
     *
     * @return the Rational
     */
    public static Rational of(BigInteger integer) {
        if (integer.equals(BigInteger.ZERO)) {
            return ZERO;
        } else if (integer.equals(BigInteger.ONE)) {
            return ONE;
        } else if (integer.equals(BigInteger.TEN)) {
            return TEN;
        } else {
            return new Rational(integer, BigInteger.ONE);
        }
    }

    /**
     * Returns true if this Rational is an integer, false otherwise.
     *
     * @return true or false
     */
    public boolean isInteger() {
        return isInteger;
    }

    /**
     * Returns a BigInteger that represents this Rational, rounding towards zero.
     *
     * @return a BigInteger that is a rounded representation of this Rational
     */
    public BigInteger toInteger() {
        if (isInteger) {
            return numerator;
        } else {
            return numerator.divide(denominator);
        }
    }

    /**
     * Returns an int that represents this Rational, rounding towards zero.
     *
     * @return an int that is a rounded representation of this Rational
     */
    @Override
    public int intValue() {
        if (isInteger) {
            return numerator.intValue();
        } else {
            return numerator.divide(denominator).intValue();
        }
    }

    /**
     * Returns a long that represents this Rational, rounding towards zero.
     *
     * @return a long that is a rounded representation of this Rational
     */
    @Override
    public long longValue() {
        if (isInteger) {
            return numerator.longValue();
        } else {
            return numerator.divide(denominator).longValue();
        }
    }

    /**
     * Returns a BigDecimal that represents this Rational.
     *
     * @return a BigDecimal that is a rounded representation of this Rational
     */
    public BigDecimal toDecimal() {
        if (isInteger) {
            return new BigDecimal(numerator);
        } else {
            return new BigDecimal(numerator).divide(new BigDecimal(denominator), 100, RoundingMode.DOWN);
        }
    }

    /**
     * Returns a double that represents this Rational.
     *
     * @return a double that is a rounded representation of this Rational
     */
    @Override
    public double doubleValue() {
        return toDecimal().doubleValue();
    }

    /**
     * Returns a float that represents this Rational.
     *
     * @return a float that is a rounded representation of this Rational
     */
    @Override
    public float floatValue() {
        return toDecimal().floatValue();
    }

    /**
     * Returns a Rational that represents this Rational plus the given other Rational.
     *
     * @param other the other rational
     *
     * @return the sum of this and other
     */
    public Rational add(Rational other) {
        if (isZero) {
            return other;
        } else if (other.isZero) {
            return this;
        } else if (isInteger && other.isInteger) {
            return Rational.of(numerator.add(other.numerator));
        } else {
            return Rational.of(numerator.multiply(other.denominator).add(denominator.multiply(other.numerator)),
                    denominator.multiply(other.denominator));
        }
    }

    /**
     * Returns a Rational that represents this Rational minus the given other Rational.
     *
     * @param other the other rational
     *
     * @return the difference of this and other
     */
    public Rational subtract(Rational other) {
        if (isZero) {
            return other.negate();
        } else if (other.isZero) {
            return this;
        } else if (equals(other)) {
            return ZERO;
        } else if (isInteger && other.isInteger) {
            return Rational.of(numerator.subtract(other.numerator));
        } else {
            return Rational.of(numerator.multiply(other.denominator).subtract(denominator.multiply(other.numerator)),
                    denominator.multiply(other.denominator));
        }
    }

    /**
     * Returns a Rational that represents this Rational multiplied by the given other Rational.
     *
     * @param other the other rational
     *
     * @return the product of this and other
     */
    public Rational multiply(Rational other) {
        if (isZero || other.isZero) {
            return ZERO;
        } else if (isOne) {
            return other;
        } else if (other.isOne) {
            return this;
        } else if (numerator.equals(other.denominator) && denominator.equals(other.numerator)) {
            return ONE;
        } else {
            return Rational.of(numerator.multiply(other.numerator), denominator.multiply(other.denominator));
        }
    }

    /**
     * Returns a Rational that represents this Rational divided by the given other Rational.
     *
     * @param other the other rational (must not be 0)
     *
     * @return the quotient of this and other
     */
    public Rational divide(Rational other) {
        if (other.isZero) {
            throw new IllegalArgumentException("Division by zero");
        } else if (isZero) {
            return ZERO;
        } else if (other.isOne) {
            return this;
        } else if (equals(other)) {
            return ONE;
        } else {
            return Rational.of(numerator.multiply(other.denominator), denominator.multiply(other.numerator));
        }
    }

    /**
     * Returns a BigInteger that represents the integer part when dividing this Rational by the given other Rational.
     *
     * @param other the other rational (must not be 0)
     *
     * @return the quotient of this and other
     */
    public BigInteger divideInteger(Rational other) {
        if (other.isZero) {
            throw new IllegalArgumentException("Division by zero");
        } else if (isZero) {
            return BigInteger.ZERO;
        } else if (equals(other)) {
            return BigInteger.ONE;
        } else if (isInteger) {
            if (other.isOne) {
                return numerator;
            } else if (other.isInteger) {
                return numerator.divide(other.numerator);
            } else {
                return numerator.multiply(other.denominator).divide(other.numerator);
            }
        } else if (other.isInteger) {
            return numerator.divide(other.numerator.multiply(denominator));
        } else {
            return numerator.multiply(other.denominator).divide(other.numerator.multiply(denominator));
        }
    }

    /**
     * Returns an array that contains a BigInteger in its first element representing the integer part when dividing this Rational by the given other
     * Rational and the remainder as a Rational in its second element.
     *
     * @param other the other rational (must not be 0)
     *
     * @return an Object array with a BigInteger and a Rational
     */
    public Number[] divideIntegerAndRemainder(Rational other) {
        if (other.isZero) {
            throw new IllegalArgumentException("Division by zero");
        } else if (isZero) {
            return new Number[] { BigInteger.ZERO, ZERO };
        } else if (equals(other)) {
            return new Number[] { BigInteger.ONE, ZERO };
        } else if (isInteger) {
            if (other.isOne) {
                return new Number[] { numerator, ZERO };
            } else if (other.isInteger) {
                BigInteger[] result = numerator.divideAndRemainder(other.numerator);
                return new Number[] { result[0], Rational.of(result[1]) };
            } else {
                BigInteger[] result = numerator.multiply(other.denominator).divideAndRemainder(other.numerator);
                return new Number[] { result[0], Rational.of(result[1], other.numerator) };
            }
        } else if (other.isInteger) {
            BigInteger[] result = numerator.divideAndRemainder(other.numerator.multiply(denominator));
            return new Number[] { result[0], Rational.of(result[1], denominator.multiply(other.numerator)) };
        } else {
            BigInteger[] result = numerator.multiply(other.denominator).divideAndRemainder(other.numerator.multiply(denominator));
            return new Number[] { result[0], Rational.of(result[1], denominator.multiply(other.numerator)) };
        }
    }

    /**
     * Returns the signum of this Rational. Returns -1 if negative, 0 if zero, 1 if positive.
     *
     * @return the signum
     */
    public int signum() {
        return numerator.signum();
    }

    /**
     * Returns a Rational that represents the negation of this Rational.
     *
     * @return the negation
     */
    public Rational negate() {
        if (isZero) {
            return this;
        } else {
            return Rational.of(numerator.negate(), denominator);
        }
    }

    /**
     * Returns a Rational that represents the absolute value of this Rational.
     *
     * @return the negation
     */
    public Rational abs() {
        if (isZero || signum() > 0) {
            return this;
        } else {
            return negate();
        }
    }

    /**
     * Returns the reciprocal of this Rational. The Rational must not be 0.
     *
     * @return the reciprocal
     */
    public Rational reciprocal() {
        if (isZero) {
            throw new IllegalArgumentException("Division by zero");
        } else if (isOne) {
            return ONE;
        } else {
            return Rational.of(denominator, numerator);
        }
    }

    /**
     * Returns a Rational that represents the minimum of this Rational and the given other Rational.
     *
     * @param other the other Rational
     *
     * @return the minimum of the two Rationals
     */
    public Rational min(Rational other) {
        if (compareTo(other) < 0) {
            return this;
        } else {
            return other;
        }
    }

    /**
     * Returns a Rational that represents the maximum of this Rational and the given other Rational.
     *
     * @param other the other Rational
     *
     * @return the maximum of the two Rationals
     */
    public Rational max(Rational other) {
        if (compareTo(other) < 0) {
            return other;
        } else {
            return this;
        }
    }

    /**
     * Returns the square of this Rational.
     *
     * @return the reciprocal
     */
    public Rational square() {
        if (isZero) {
            return ZERO;
        } else if (isOne) {
            return ONE;
        } else {
            return this.multiply(this);
        }
    }

    /**
     * Returns this Rational to the given integer power. Not both of the Rational and the power must be 0.
     *
     * @param power the power
     *
     * @return the power'th power
     */
    public Rational pow(int power) {
        if (power == 0) {
            if (isZero) {
                throw new IllegalArgumentException("0^0");
            } else {
                return ONE;
            }
        } else if (isZero) {
            return ZERO;
        } else if (isOne) {
            return ONE;
        } else if (power == 1) {
            return this;
        } else if (power == 2) {
            return square();
        } else if (power == 3) {
            return square().multiply(this);
        } else if (power == 4) {
            return square().square();
        } else if (power == -1) {
            return reciprocal();
        } else if (power < 0) {
            return Rational.of(denominator.pow(-power), numerator.pow(-power));
        } else {
            return Rational.of(numerator.pow(power), denominator.pow(power));
        }
    }

    /**
     * Returns a Rational representing the greatest common divisor (GCD) of this Rational and the given other Rational. Returns 0 if both Rationals
     * are 0.
     *
     * @param other the other Rational
     *
     * @return the GCD of this and the other Rational
     */
    public Rational gcd(Rational other) {
        if (this.equals(other)) {
            return this;
        } else if (isZero) {
            return other;
        } else if (isOne) {
            return Rational.of(BigInteger.ONE, other.denominator);
        } else if (other.isZero) {
            return this;
        } else if (other.isOne) {
            return Rational.of(BigInteger.ONE, this.denominator);
        } else {
            return Rational.of(numerator.multiply(other.denominator).gcd(denominator.multiply(other.numerator)),
                    denominator.multiply(other.denominator));
        }
    }

    /**
     * Returns a Rational representing the integer.
     *
     * @param integer the integer
     *
     * @return the Rational
     */
    public static Rational valueOf(int integer) {
        return Rational.of(integer);
    }

    /**
     * Returns a Rational representing the long.
     *
     * @param integer the long
     *
     * @return the Rational
     */
    public static Rational valueOf(long integer) {
        return Rational.of(integer);
    }

    /**
     * Returns a Rational representing the integer.
     *
     * @param integer the integer
     *
     * @return the Rational
     */
    public static Rational valueOf(BigInteger integer) {
        return Rational.of(integer);
    }

    /**
     * Returns a Rational of the given string. The string is expected to be of the form "[number]" or "[numerator]/[denominator]", where all
     * placeholders are integers.
     *
     * @param string the string
     *
     * @return the Rational
     */
    public static Rational valueOf(String string) {
        return Rational.of(string);
    }

    /**
     * Returns a string representation of this Rational. For integers, the string just contains the integer value. For non-integers, the string
     * consists of the numerator, followed by a slash, followed by the denominator.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        if (isInteger) {
            return numerator.toString();
        } else {
            return numerator.toString() + '/' + denominator;
        }
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Rational)) {
            return false;
        }
        if (this == otherObject) {
            return true;
        }

        Rational other = (Rational) otherObject;
        return numerator.equals(other.numerator) && denominator.equals(other.denominator);
    }

    @Override
    public int hashCode() {
        return numerator.hashCode() + 1681302443 * denominator.hashCode();
    }

    @Override
    public int compareTo(Rational other) {
        return numerator.multiply(other.denominator).compareTo(denominator.multiply(other.numerator));
    }
}
