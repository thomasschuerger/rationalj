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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements an immutable arbitrary-scale rational number based on BigInteger numerators and denominators. The rational numbers are always stored in
 * canonical form, which means that they are stored completely reduced, the denominator is always positive (which means the sign is in the numerator
 * only), and the number 0 always has denominator 1. The API is similar to the one from BigInteger. It implements many of BigInteger's methods, but
 * none of its bitwise methods.
 *
 * @author Thomas Schuerger (thomas@schuerger.com)
 */

public class Rational extends Number implements Comparable<Rational> {
    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** BigInteger minus one. */
    private static final BigInteger BI_MINUS_ONE = BigInteger.valueOf(-1);

    /** BigInteger zero. */
    private static final BigInteger BI_ZERO = BigInteger.ZERO;

    /** BigInteger one. */
    private static final BigInteger BI_ONE = BigInteger.ONE;

    /** BigInteger two. */
    private static final BigInteger BI_TWO = BigInteger.valueOf(2);

    /** BigInteger ten. */
    private static final BigInteger BI_TEN = BigInteger.TEN;

    /** BigInteger 2^31. */
    private static final BigInteger BI_TWO_TO_31 = BI_ONE.shiftLeft(31);

    /** BigInteger 2^63. */
    private static final BigInteger BI_TWO_TO_63 = BI_ONE.shiftLeft(63);

    /** Minus one. */
    public static final Rational MINUS_ONE = new Rational(BI_MINUS_ONE);

    /** Minus one half. */
    public static final Rational MINUS_ONE_HALF = new Rational(BI_MINUS_ONE, BI_TWO);

    /** Zero. */
    public static final Rational ZERO = new Rational(BI_ZERO);

    /** One half. */
    public static final Rational ONE_HALF = new Rational(BI_ONE, BI_TWO);

    /** One. */
    public static final Rational ONE = new Rational(BI_ONE);

    /** Two. */
    public static final Rational TWO = new Rational(BI_TWO);

    /** Ten. */
    public static final Rational TEN = new Rational(BI_TEN);

    /** The numerator. */
    private final BigInteger numerator;

    /** The denominator (always greater than 0). */
    private final BigInteger denominator;

    /** The signum of this Rational (&lt;0 = negative, 0 = zero, &gt;0 = positive). */
    private final int signum;

    /** True iff this Rational is an integer. */
    private final boolean isInteger;

    /** True iff this Rational is equal to one. */
    private final boolean isOne;

    /**
     * Creates an integer Rational from the given integer.
     *
     * @param integer the integer
     */
    private Rational(BigInteger integer) {
        this.numerator = integer;
        this.denominator = BI_ONE;
        this.isInteger = true;
        this.signum = integer.signum();
        this.isOne = integer.equals(BI_ONE);
    }

    /**
     * Creates a Rational from the given numerator and denominator. Numerator and denominator are converted into canonical form. The denominator must
     * not be equal to zero.
     *
     * @param numerator the numerator
     * @param denominator the denominator (must not be 0)
     *
     * @throws IllegalArgumentException if the denominator is zero
     */
    private Rational(BigInteger numerator, BigInteger denominator) {
        if (denominator.signum() == 0) {
            throw new IllegalArgumentException("denominator must be non-zero");
        }

        if (numerator.signum() == 0) {
            this.numerator = BI_ZERO;
            this.denominator = BI_ONE;
            this.isInteger = true;
            this.signum = 0;
            this.isOne = false;
            return;
        }

        if (denominator.signum() == -1) {
            // make sure the denominator is always positive
            numerator = numerator.negate();
            denominator = denominator.negate();
        }

        BigInteger gcd = numerator.gcd(denominator);

        if (!gcd.equals(BI_ONE)) {
            // the numerator and denominator are not coprime; make them coprime by dividing by their GCD
            numerator = numerator.divide(gcd);
            denominator = denominator.divide(gcd);
        }

        if (denominator.equals(BI_ONE)) {
            this.isInteger = true;
            this.isOne = numerator.equals(BI_ONE);
        } else {
            this.isInteger = false;
            this.isOne = false;
        }

        this.numerator = numerator;
        this.denominator = denominator;
        this.signum = numerator.signum();
    }

    /**
     * Creates a Rational from the given parameters. All parameters must be in canonical form and valid.
     *
     * @param numerator the numerator
     * @param denominator the denominator (must not be 0)
     * @param isInteger flag indicating whether the Rational is an integer
     * @param signum the signum
     * @param isOne flag indicating whether the Rational is one
     */
    Rational(BigInteger numerator, BigInteger denominator, int signum, boolean isInteger, boolean isOne) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.signum = signum;
        this.isInteger = isInteger;
        this.isOne = isOne;
    }

    /**
     * Converts the given rational into a continued fraction, represented as an array of integers, where the first integer may be negative and all
     * other integers are non-negative.
     *
     * @return the array of integers
     */
    public BigInteger[] toContinuedFraction() {
        if (isInteger) {
            return new BigInteger[] { numerator };
        } else if (numerator.equals(BI_ONE)) {
            // positive unit fraction
            return new BigInteger[] { BI_ZERO, denominator };
        } else if (numerator.equals(BI_MINUS_ONE)) {
            // negative unit fraction
            if (denominator.equals(BI_TWO)) {
                // -1/2
                return new BigInteger[] { BI_MINUS_ONE, BI_TWO };
            } else {
                return new BigInteger[] { BI_MINUS_ONE, BI_ONE, denominator.subtract(BI_ONE) };
            }
        }

        List<BigInteger> integers = new ArrayList<>(25);

        BigInteger numerator = this.numerator;
        BigInteger denominator = this.denominator;

        if (signum > 0) {
            do {
                BigInteger[] numbers = numerator.divideAndRemainder(denominator);
                integers.add(numbers[0]);
                numerator = denominator;
                denominator = numbers[1];
            } while (!denominator.equals(BI_ONE));
            integers.add(numerator);
        } else {
            BigInteger[] numbers = numerator.divideAndRemainder(denominator);
            integers.add(numbers[0].subtract(BI_ONE));
            numerator = denominator;
            denominator = numbers[1].add(denominator);
            while (!denominator.equals(BI_ONE)) {
                numbers = numerator.divideAndRemainder(denominator);
                integers.add(numbers[0]);
                numerator = denominator;
                denominator = numbers[1];
            }
            integers.add(numerator);
        }

        BigInteger[] result = new BigInteger[integers.size()];

        return integers.toArray(result);
    }

    /**
     * Returns the Rational represented by the given integers a_i that resemble the continued fraction a_1 + 1/(a_2 + 1/(a_3 + 1/(a_4 + ...))).
     *
     * @param integers the array of integers (must not be empty)
     *
     * @return the Rational
     *
     * @throws IllegalArgumentException if the array of integers is empty
     */
    public static Rational ofContinuedFraction(BigInteger... integers) {
        int len = integers.length;
        switch (len) {
        case 0:
            throw new IllegalArgumentException("integers must be non-empty");
        case 1:
            return Rational.of(integers[0]);
        default:
            Rational x = Rational.of(integers[len - 1]);
            for (int i = len - 2; i >= 0; i--) {
                x = x.reciprocal().add(Rational.of(integers[i]));
            }
            return x;
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

    /**
     * Returns a Rational based on the given string.
     *
     * @param string the string
     *
     * @return the Rational
     *
     * @throws IllegalArgumentException if the string contains more than one '/' character or if the denominator is 0
     */

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
     * @param denominator the denominator (must not be 0)
     *
     * @return the Rational
     *
     * @throws IllegalArgumentException if the denominator is 0
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
     *
     * @throws IllegalArgumentException if the denominator is 0
     *
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
     *
     * @throws IllegalArgumentException if the denominator is 0
     */
    public static Rational of(BigInteger numerator, BigInteger denominator) {
        if (denominator.signum() == 0) {
            throw new IllegalArgumentException("denominator must be non-zero");
        } else if (numerator.signum() == 0) {
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
        case -1:
            return MINUS_ONE;
        case 0:
            return ZERO;
        case 1:
            return ONE;
        case 2:
            return TWO;
        case 10:
            return TEN;
        default:
            return new Rational(BigInteger.valueOf(integer));
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
        } else if (integer == -1) {
            return MINUS_ONE;
        } else if (integer == 1) {
            return ONE;
        } else if (integer == 2) {
            return TWO;
        } else if (integer == 10) {
            return TEN;
        } else {
            return new Rational(BigInteger.valueOf(integer));
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
        if (integer.signum() == 0) {
            return ZERO;
        } else if (integer.equals(BI_MINUS_ONE)) {
            return MINUS_ONE;
        } else if (integer.equals(BI_ONE)) {
            return ONE;
        } else if (integer.equals(BI_TEN)) {
            return TEN;
        } else {
            return new Rational(integer);
        }
    }

    /**
     * Returns a Rational that represents the unit fraction 1/denominator. The denominator must not be 0.
     *
     * @param denominator the denominator (must not be 0)
     *
     * @return the Rational
     *
     * @throws IllegalArgumentException if the denominator is 0
     */
    public static Rational ofReciprocal(int denominator) {
        switch (denominator) {
        case 0:
            throw new IllegalArgumentException("denominator must be non-zero");
        case 1:
            return ONE;
        case -1:
            return MINUS_ONE;
        case 2:
            return ONE_HALF;
        default:
            if (denominator > 0) {
                // positive
                return new Rational(BI_ONE, BigInteger.valueOf(denominator), 1, false, false);
            } else if (denominator == Integer.MIN_VALUE) {
                // we can't negate Integer.MIN_VALUE as an int
                return new Rational(BI_MINUS_ONE, BI_TWO_TO_31, -1, false, false);
            } else {
                return new Rational(BI_MINUS_ONE, BigInteger.valueOf(-denominator), -1, false, false);
            }
        }
    }

    /**
     * Returns a Rational that represents the unit fraction 1/denominator. The denominator must not be 0.
     *
     * @param denominator the denominator (must not be 0)
     *
     * @return the Rational
     *
     * @throws IllegalArgumentException if the denominator is 0
     */
    public static Rational ofReciprocal(long denominator) {
        if (denominator == 0L) {
            throw new IllegalArgumentException("denominator must be non-zero");
        } else if (denominator > 0) {
            if (denominator == 1L) {
                return ONE;
            } else if (denominator == 2L) {
                return ONE_HALF;
            } else {
                return new Rational(BI_ONE, BigInteger.valueOf(denominator), 1, false, false);
            }
        } else if (denominator == -1L) {
            return MINUS_ONE;
        } else if (denominator == Long.MIN_VALUE) {
            // we can't negate Long.MIN_VALUE as a long
            return new Rational(BI_MINUS_ONE, BI_TWO_TO_63, -1, false, false);
        } else {
            return new Rational(BI_MINUS_ONE, BigInteger.valueOf(-denominator), -1, false, false);
        }
    }

    /**
     * Returns a Rational that represents the unit fraction 1/denominator. The denominator must not be 0.
     *
     * @param denominator the denominator (must not be 0)
     *
     * @return the Rational
     *
     * @throws IllegalArgumentException if the denominator is 0
     */
    public static Rational ofReciprocal(BigInteger denominator) {
        int signum = denominator.signum();

        if (signum == 0) {
            throw new IllegalArgumentException("denominator must be non-zero");
        } else if (signum > 0) {
            if (denominator.equals(BI_ONE)) {
                return ONE;
            } else if (denominator.equals(BI_TWO)) {
                return ONE_HALF;
            } else {
                return new Rational(BI_ONE, denominator, 1, false, false);
            }
        } else if (denominator.equals(BI_MINUS_ONE)) {
            return MINUS_ONE;
        } else {
            return new Rational(BI_MINUS_ONE, denominator.negate(), -1, false, false);
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
     * Returns an int that represents this Rational, rounding towards zero. Note that the behavior is undefined if the result does not fit into an
     * int.
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
     * Returns a long that represents this Rational, rounding towards zero. Note that the behavior is undefined if the result does not fit into a
     * long.
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
     * Returns a BigDecimal that represents this Rational. The BigDecimal uses a scale of zero if this Rational is an integer, otherwise it uses a
     * scale of 100 and RoundingMode.DOWN.
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
     * Returns a BigDecimal that represents this Rational. The BigDecimal uses a scale of zero if this Rational is an integer, otherwise it uses the
     * given scale and rounding mode.
     *
     * @param scale the scale
     * @param roundingMode the rounding mode (e.g. RoundingMode.DOWN)
     *
     * @return a BigDecimal that is a rounded representation of this Rational
     */
    public BigDecimal toDecimal(int scale, RoundingMode roundingMode) {
        if (isInteger) {
            return new BigDecimal(numerator);
        } else {
            return new BigDecimal(numerator).divide(new BigDecimal(denominator), scale, roundingMode);
        }
    }

    /**
     * Returns a double that represents this Rational. Note that the behavior is undefined if the result does not fit into a double. Note also that
     * precision may be lost.
     *
     * @return a double that is a rounded representation of this Rational
     */
    @Override
    public double doubleValue() {
        return toDecimal().doubleValue();
    }

    /**
     * Returns a float that represents this Rational. Note that the behavior is undefined if the result does not fit into a float. Note also that
     * precision may be lost.
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
        if (signum == 0) {
            return other;
        } else if (other.signum == 0) {
            return this;
        } else if (isInteger && other.isInteger) {
            BigInteger sum = numerator.add(other.numerator);
            return new Rational(sum, BI_ONE, sum.signum(), true, sum.equals(BI_ONE));
        } else if (isNegationOf(other)) {
            return ZERO;
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
        if (signum == 0) {
            return other.negate();
        } else if (other.signum == 0) {
            return this;
        } else if (equals(other)) {
            return ZERO;
        } else if (isInteger && other.isInteger) {
            BigInteger difference = numerator.subtract(other.numerator);
            return new Rational(difference, BI_ONE, difference.signum(), true, difference.equals(BI_ONE));
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
        if (signum == 0 || other.signum == 0) {
            return ZERO;
        } else if (isOne) {
            return other;
        } else if (other.isOne) {
            return this;
        } else if (other.equals(TWO)) {
            return redouble();
        } else if (isReciprocalOf(other)) {
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
     *
     * @throws IllegalArgumentException if other is 0
     */
    public Rational divide(Rational other) {
        if (other.signum == 0) {
            throw new IllegalArgumentException("Division by zero");
        } else if (signum == 0) {
            return ZERO;
        } else if (other.isOne) {
            return this;
        } else if (equals(other)) {
            return ONE;
        } else if (other.equals(TWO)) {
            return halve();
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
     *
     * @throws IllegalArgumentException if other is 0
     */
    public BigInteger divideInteger(Rational other) {
        if (other.signum == 0) {
            throw new IllegalArgumentException("Division by zero");
        } else if (signum == 0) {
            return BI_ZERO;
        } else if (equals(other)) {
            return BI_ONE;
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
     *
     * @throws IllegalArgumentException if other is 0
     */
    public Number[] divideIntegerAndRemainder(Rational other) {
        if (other.signum == 0) {
            throw new IllegalArgumentException("Division by zero");
        } else if (signum == 0) {
            return new Number[] { BI_ZERO, ZERO };
        } else if (equals(other)) {
            return new Number[] { BI_ONE, ZERO };
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
     * Returns a Rational that represents this Rational modulo the other rational.
     *
     * @param other the other rational (must not be 0)
     *
     * @return this modulo other
     *
     * @throws IllegalArgumentException if other is 0
     */
    public Rational mod(Rational other) {
        if (other.signum == 0) {
            throw new IllegalArgumentException("Division by zero");
        } else if (signum == 0) {
            return ZERO;
        } else if (equals(other)) {
            return ZERO;
        } else if (isInteger) {
            if (other.isOne) {
                return ZERO;
            } else if (other.isInteger) {
                return Rational.of(numerator.mod(other.numerator));
            }
        }

        if (signum == other.signum) {
            return this.subtract(Rational.of(this.divideInteger(other)).multiply(other));
        } else {
            return this.subtract(Rational.of(this.divideInteger(other).subtract(BI_ONE)).multiply(other));
        }
    }

    /**
     * Returns the signum of this Rational. Returns -1 if negative, 0 if zero, 1 if positive.
     *
     * @return the signum
     */
    public int signum() {
        return signum;
    }

    /**
     * Returns a Rational that represents the negation of this Rational.
     *
     * @return the negation
     */
    public Rational negate() {
        if (signum == 0) {
            return ZERO;
        } else if (isInteger && numerator.equals(BI_MINUS_ONE)) {
            return ONE;
        } else {
            // negation keeps numerator and denominator coprime
            return new Rational(numerator.negate(), denominator, -signum, isInteger, false);
        }
    }

    /**
     * Returns true iff this Rational is the negation of the given other Rational. This is faster than retrieving the negation and comparing it.
     *
     * @param other the other Rational
     *
     * @return true or false
     */
    public boolean isNegationOf(Rational other) {
        if (signum == 0 && other.signum == 0) {
            return true;
        } else {
            return signum == -other.signum && denominator.equals(other.denominator) && numerator.negate().equals(other.numerator);
        }
    }

    /**
     * Returns a Rational that represents the absolute value of this Rational.
     *
     * @return the negation
     */
    public Rational abs() {
        if (signum >= 0) {
            return this;
        } else {
            return negate();
        }
    }

    /**
     * Returns the reciprocal of this Rational. The Rational must not be 0.
     *
     * @return the reciprocal
     *
     * @throws IllegalArgumentException if this Rational is 0
     */
    public Rational reciprocal() {
        switch (signum) {
        case -1:
            // numerator and denominator stay coprime for reciprocals
            return new Rational(denominator.negate(), numerator.negate(), signum, numerator.equals(BI_MINUS_ONE), false);
        case 1:
            if (isOne) {
                return ONE;
            } else {
                // numerator and denominator stay coprime for reciprocals
                return new Rational(denominator, numerator, signum, numerator.equals(BI_ONE), false);
            }
        default:
            throw new IllegalArgumentException("Division by zero");
        }
    }

    /**
     * Returns true iff this Rational is the reciprocal of the given other Rational. This is faster than retrieving the reciprocal and comparing it.
     *
     * @param other the other Rational
     *
     * @return true or false
     */
    public boolean isReciprocalOf(Rational other) {
        switch (signum) {
        case -1:
            return other.signum == -1 && denominator.equals(other.numerator.negate()) && numerator.negate().equals(other.denominator);
        case 1:
            return other.signum == 1 && denominator.equals(other.numerator) && numerator.equals(other.denominator);
        default:
            // 0 has no reciprocal
            return false;
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
        if (compareTo(other) <= 0) {
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
        if (compareTo(other) <= 0) {
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
        if (signum == 0) {
            return ZERO;
        } else if (isOne || equals(MINUS_ONE)) {
            return ONE;
        } else {
            return new Rational(numerator.multiply(numerator), denominator.multiply(denominator), 1, isInteger, false);
        }
    }

    /**
     * Returns this Rational to the given integer power. If this Rational is 0, the power must be positive.
     *
     * @param power the power
     *
     * @return the power'th power of this Rational
     *
     * @throws IllegalArgumentException if this Rational is 0 and the power is not positive
     */
    public Rational pow(int power) {
        if (power == 0) {
            if (signum == 0) {
                throw new IllegalArgumentException("0^0");
            } else {
                return ONE;
            }
        } else if (signum == 0) {
            if (power < 0) {
                throw new IllegalArgumentException("Division by zero");
            } else {
                return ZERO;
            }
        } else if (isOne || (power % 2 == 0 && equals(MINUS_ONE))) {
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
        } else if (power == -2) {
            return reciprocal().square();
        } else if (power < 0) {
            return new Rational(denominator.pow(-power), numerator.pow(-power), power % 2 == 0 ? 1 : signum, numerator.equals(BI_ONE), false);
        } else {
            return new Rational(numerator.pow(power), denominator.pow(power), power % 2 == 0 ? 1 : signum, isInteger, false);
        }
    }

    /**
     * Returns a Rational which is twice this Rational.
     *
     * @return twice of this Rational
     */
    public Rational redouble() {
        if (signum == 0) {
            return this;
        } else if (denominator.testBit(0)) {
            // denominator is odd
            if (isOne) {
                return TWO;
            } else {
                // double the numerator
                return new Rational(numerator.shiftLeft(1), denominator, signum, isInteger, false);
            }
        } else if (equals(ONE_HALF)) {
            return ONE;
        } else {
            // denominator is even: halve the denominator
            return new Rational(numerator, denominator.shiftRight(1), signum, denominator.equals(BI_TWO), false);
        }
    }

    /**
     * Returns a Rational which is half of this Rational.
     *
     * @return half of this Rational
     */
    public Rational halve() {
        if (signum == 0) {
            return this;
        } else if (numerator.testBit(0)) {
            // numerator is odd
            if (isOne) {
                return ONE_HALF;
            } else {
                // double the denominator
                return new Rational(numerator, denominator.shiftLeft(1), signum, false, false);
            }
        } else if (equals(TWO)) {
            return ONE;
        } else {
            // numerator is even: halve the numerator
            return new Rational(numerator.shiftRight(1), denominator, signum, denominator.equals(BI_ONE), false);
        }
    }

    /**
     * Returns a Rational that represents the largest integer smaller than or equal to this Rational. Thus, if the Rational is not an integer, rounds
     * down to the previous integer.
     *
     * @return the floor of this Rational
     */
    public Rational floor() {
        if (isInteger) {
            return this;
        } else if (signum > 0) {
            return Rational.of(numerator.divide(denominator));
        } else {
            return Rational.of(numerator.divide(denominator).subtract(BI_ONE));
        }
    }

    /**
     * Returns a Rational that represents the smallest integer larger than or equal to this rational. Thus, if the Rational is not an integer, rounds
     * up to the next integer.
     *
     * @return the ceiling of this Rational
     */
    public Rational ceil() {
        if (isInteger) {
            return this;
        } else if (signum > 0) {
            return Rational.of(numerator.divide(denominator).add(BI_ONE));
        } else {
            return Rational.of(numerator.divide(denominator));
        }
    }

    /**
     * Returns a Rational that represents the integer closest to this Rational. In case of a tie, rounds towards infinity for positive numbers and
     * towards minus infinity for negative numbers.
     *
     * @return the
     */
    public Rational round() {
        if (isInteger) {
            return this;
        }

        BigInteger[] m = numerator.divideAndRemainder(denominator);
        BigInteger mod = m[1].shiftLeft(1);

        if (signum > 0) {
            if (mod.compareTo(denominator) >= 0) {
                return Rational.of(m[0].add(BI_ONE));
            } else {
                return Rational.of(m[0]);
            }
        } else if (mod.negate().compareTo(denominator) >= 0) {
            return Rational.of(m[0].subtract(BI_ONE));
        } else {
            return Rational.of(m[0]);
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
        } else if (signum == 0) {
            return other;
        } else if (isOne) {
            return Rational.of(BI_ONE, other.denominator);
        } else if (other.signum == 0) {
            return this;
        } else if (other.isOne) {
            return Rational.of(BI_ONE, this.denominator);
        } else {
            // generic equation: gcd(a/b,c/d) = gcd(a*d,b*c)/(b*d)

            // for gcd(a,b)=gcd(c,d)=1, this is faster especially for large numbers:
            // gcd(a/b,c/d) = gcd(a,c)/lcm(b,d) = gcd(a,c)*gcd(b,d)/(b*d)
            // it requires two GCD calculations, but the involved numbers are smaller
            return Rational.of(numerator.gcd(other.numerator).multiply(denominator.gcd(other.denominator)), denominator.multiply(other.denominator));
        }
    }

    /**
     * Returns a Rational representing the least common multiple (LCM) of this Rational and the given other Rational. Returns zero if any of the two
     * Rationals is zero.
     *
     * @param other the other Rational
     *
     * @return the LCM of this and the other Rational
     */
    public Rational lcm(Rational other) {
        if (signum == 0 || other.signum == 0) {
            return ZERO;
        } else if (equals(other)) {
            return this;
        } else {
            return reciprocal().gcd(other.reciprocal()).reciprocal();
        }
    }

    /**
     * Returns a uniformly distributed random Rational from the rational set {0, 1/2^bits, ..., (2^bits-1)/2^bits}, i.e. a Rational from the quantized
     * interval [0,1) using a quantization of 1/2^bits. A default thread-safe non-cryptographic random generator is used.
     *
     * @param bits the quantization of the Rational, given in bits
     *
     * @return the random Rational
     */
    public static Rational random(int bits) {
        return random(bits, ThreadLocalRandom.current());
    }

    /**
     * Returns a uniformly distributed random Rational from the rational set {0, 1/2^bits, ..., (2^bits-1)/2^bits}, i.e. a Rational from the quantized
     * interval [0,1) using a quantization of 1/2^bits. The given random generator is used.
     *
     * @param bits the quantization of the Rational, given in bits
     * @param random the random generator to use
     *
     * @return the random Rational
     */
    public static Rational random(int bits, Random random) {
        if (bits <= 0) {
            throw new IllegalArgumentException("bits must be > 0");
        }

        if (random == null) {
            throw new IllegalArgumentException("random must not be null");
        }

        byte[] mag = new byte[bits / 8 + 1];
        mag[0] = (byte) (1 << (bits & 7));

        return Rational.of(new BigInteger(bits, random), new BigInteger(1, mag));
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
        if (signum == -other.signum || other.signum == 0) {
            return signum;
        } else if (signum == 0) {
            return -other.signum;
        } else {
            // expensive case: numbers have the same non-zero signum
            return numerator.multiply(other.denominator).compareTo(denominator.multiply(other.numerator));
        }
    }

    /**
     * Sanity-checks this Rational. Throws an IllegalStateException if the sanity check fails, otherwise does nothing.
     *
     * @throws IllegalStateException if this Rational is in an illegal state
     */
    void check() {
        if (numerator == null) {
            throw new IllegalStateException("numerator is null: " + toDetailString());
        }

        if (denominator == null) {
            throw new IllegalStateException("denominator is null: " + toDetailString());
        }

        if (denominator.signum() == 0) {
            throw new IllegalStateException("denominator is 0: " + toDetailString());
        }

        if (denominator.signum() < 0) {
            throw new IllegalStateException("Denominator is not positive: " + toDetailString());
        }

        if (!numerator.gcd(denominator).equals(BI_ONE)) {
            throw new IllegalStateException("numerator and denominator are not coprime: " + toDetailString());
        }

        if (signum != numerator.signum()) {
            throw new IllegalStateException("signum is wrong: " + toDetailString());
        }

        if (isInteger ^ denominator.equals(BI_ONE)) {
            throw new IllegalStateException("isInteger is wrong: " + toDetailString());
        }

        if (isOne ^ (isInteger && numerator.equals(BI_ONE))) {
            throw new IllegalStateException("isOne is wrong: " + toDetailString());
        }
    }

    /**
     * Returns all fields of this Rational as a string.
     *
     * @return the string
     */
    private String toDetailString() {
        return "numerator=" + numerator + ", denominator=" + denominator + ", isInteger=" + isInteger + ", signum=" + signum + ", isOne=" + isOne;
    }
}
