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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

/**
 * Tests for the class Rational.
 *
 * @author Thomas Schuerger (thomas@schuerger.com)
 */

class RationalTest {

    @Test
    void testOf() {
        assertThrows(IllegalArgumentException.class, () -> Rational.of(0, 0));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(1, 0));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(5, 0));

        assertThrows(IllegalArgumentException.class, () -> Rational.of(0L, 0L));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(1L, 0L));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(-1L, 0L));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(5L, 0L));

        assertThrows(IllegalArgumentException.class, () -> Rational.of(BigInteger.ZERO, BigInteger.ZERO));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(BigInteger.ONE, BigInteger.ZERO));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(BigInteger.ONE.negate(), BigInteger.ZERO));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(BigInteger.valueOf(5), BigInteger.ZERO));

        assertThrows(IllegalArgumentException.class, () -> Rational.of("5/7/"));
        assertThrows(NumberFormatException.class, () -> Rational.of(""));
        assertThrows(NumberFormatException.class, () -> Rational.of("abc"));
        assertThrows(NumberFormatException.class, () -> Rational.of("abc/def"));
        assertThrows(NumberFormatException.class, () -> Rational.of("123/def"));
        assertThrows(NumberFormatException.class, () -> Rational.of("abc/456"));

        assertEquals(Rational.of(5, 7), Rational.valueOf("5/7"));
        assertEquals(Rational.of(5, 7), Rational.of("-5/-7"));
        assertEquals(Rational.of(12345), Rational.of("12345"));
        assertEquals(Rational.MINUS_ONE, Rational.of(-1));
        assertEquals(Rational.MINUS_ONE, Rational.of(-1, 1));
        assertEquals(Rational.MINUS_ONE, Rational.of(-1L));
        assertEquals(Rational.MINUS_ONE, Rational.of(-1L, 1L));
        assertEquals(Rational.MINUS_ONE, Rational.of(BigInteger.valueOf(-1)));
        assertEquals(Rational.MINUS_ONE, Rational.of(BigInteger.valueOf(-1), BigInteger.ONE));
        assertEquals(Rational.ONE, Rational.of(7, 7));
        assertEquals(Rational.ONE, Rational.of(7L, 7L));
        assertEquals(Rational.ONE, Rational.of(BigInteger.valueOf(7), BigInteger.valueOf(7)));
        assertEquals(Rational.ZERO, Rational.of(0));
        assertEquals(Rational.ZERO, Rational.of(0L));
        assertEquals(Rational.ZERO, Rational.valueOf(BigInteger.ZERO));
        assertEquals(Rational.ZERO, Rational.of(BigInteger.ZERO, BigInteger.TEN));
        assertEquals(Rational.ONE, Rational.valueOf(1));
        assertEquals(Rational.ONE, Rational.valueOf(1L));
        assertEquals(Rational.ONE, Rational.of(BigInteger.ONE));
        assertEquals(Rational.TWO, Rational.of(2));
        assertEquals(Rational.TWO, Rational.of(2L));
        assertEquals(Rational.TWO, Rational.of(BigInteger.valueOf(2)));
        assertEquals(Rational.TEN, Rational.of(10));
        assertEquals(Rational.TEN, Rational.of(10L));
        assertEquals(Rational.TEN, Rational.of(BigInteger.valueOf(10)));
    }

    @Test
    void testNumerator() {
        assertEquals(BigInteger.valueOf(10), Rational.of(10).numerator());
        assertEquals(BigInteger.valueOf(2), Rational.of(2).numerator());
        assertEquals(BigInteger.valueOf(1), Rational.of(17, 17).numerator());
        assertEquals(BigInteger.valueOf(-1), Rational.of(17, -17).numerator());
        assertEquals(BigInteger.valueOf(35), Rational.of(35, 241).numerator());
    }

    @Test
    void testDenominator() {
        assertEquals(BigInteger.valueOf(1), Rational.of(10L).denominator());
        assertEquals(BigInteger.valueOf(1), Rational.of(2L).denominator());
        assertEquals(BigInteger.valueOf(1), Rational.of(17, 17).denominator());
        assertEquals(BigInteger.valueOf(241), Rational.of(35, 241).denominator());
    }

    @Test
    void testNormalization() {
        assertEquals(Rational.of(0, 1), Rational.of(0L, 4431L));
        assertEquals(Rational.of(1, 1), Rational.of(BigInteger.ONE, BigInteger.ONE));
        assertEquals(Rational.of(1, 1), Rational.of(2, 2));
        assertEquals(Rational.of(-5, -5), Rational.of(BigInteger.ONE));
        assertEquals(Rational.of(-5, 5), Rational.of(-1));
        assertEquals(Rational.of(5, -5), Rational.of(-1));
        assertEquals(Rational.of(5, 5), Rational.of(1));
        assertEquals(Rational.of(4389327, 37800), Rational.of(4389327 / 9, 37800 / 9));
        assertEquals(Rational.of(-4389327, 37800), Rational.of(4389327 / 9, -37800 / 9));
        assertEquals(Rational.of(-4389327, 37800), Rational.of(-4389327 / 9, 37800 / 9));
        assertEquals(Rational.of(-4389327, -37800), Rational.of(4389327 / 9, 37800 / 9));
    }

    @Test
    void testIsInteger() {
        assertTrue(Rational.of(5, 1).isInteger());
        assertTrue(Rational.of(10, 2).isInteger());
        assertTrue(Rational.of(-7, 1).isInteger());
        assertTrue(Rational.of(21, -3).isInteger());
        assertFalse(Rational.of(1, 3).isInteger());
        assertFalse(Rational.of(7, 15).isInteger());
    }

    @Test
    void testAdd() {
        assertEquals(Rational.of(1), Rational.of(-3).add(Rational.of(4)));
        assertEquals(Rational.of(1), Rational.of(1, 2).add(Rational.of(1, 2)));
        assertEquals(Rational.of(37, 21), Rational.of(3, 7).add(Rational.of(4, 3)));
        assertEquals(Rational.ZERO, Rational.of(-4, 3).add(Rational.of(4, 3)));
        assertEquals(Rational.of(4, 3), Rational.ZERO.add(Rational.of(4, 3)));
        assertEquals(Rational.of(4, 3), Rational.of(4, 3).add(Rational.ZERO));
        assertEquals(Rational.of(9), Rational.of(4).add(Rational.of(5)));
        assertEquals(Rational.of(17, 3), Rational.of(4).add(Rational.of(5, 3)));
        assertEquals(Rational.of(17, 3), Rational.of(5, 3).add(Rational.of(4)));
    }

    @Test
    void testSubtract() {
        assertEquals(Rational.of(-19, 21), Rational.of(3, 7).subtract(Rational.of(4, 3)));
        assertEquals(Rational.of(3, 7), Rational.of(3, 7).subtract(Rational.ZERO));
        assertEquals(Rational.ZERO, Rational.of(4, 3).subtract(Rational.of(4, 3)));
        assertEquals(Rational.ZERO, Rational.ZERO.subtract(Rational.ZERO));
        assertEquals(Rational.of(-2), Rational.of(5).subtract(Rational.of(7)));
        assertEquals(Rational.of(3, 2), Rational.of(5).subtract(Rational.of(7, 2)));
        assertEquals(Rational.of(8, 5), Rational.of(3, 5).subtract(Rational.of(-1)));
    }

    @Test
    void testMultiply() {
        assertEquals(Rational.of(9, 16), Rational.of(3, 4).multiply(Rational.of(3, 4)));
        assertEquals(Rational.ONE, Rational.of(15, 8).multiply(Rational.of(8, 15)));
        assertEquals(Rational.of(8, 7), Rational.of(15, 7).multiply(Rational.of(8, 15)));
        assertEquals(Rational.of(8, 7), Rational.of(8, 15).multiply(Rational.of(15, 7)));
        assertEquals(Rational.ZERO, Rational.ZERO.multiply(Rational.of(8, 15)));
        assertEquals(Rational.ZERO, Rational.of(8, 15).multiply(Rational.ZERO));
        assertEquals(Rational.ZERO, Rational.ZERO.multiply(Rational.ZERO));
        assertEquals(Rational.of(17, 4), Rational.ONE.multiply(Rational.of(17, 4)));
        assertEquals(Rational.of(17, 4), Rational.of(17, 4).multiply(Rational.ONE));
        assertEquals(Rational.of(17, 2), Rational.of(17, 4).multiply(Rational.TWO));
    }

    @Test
    void testSquare() {
        assertEquals(Rational.ZERO, Rational.ZERO.square());
        assertEquals(Rational.ONE, Rational.ONE.square());
        assertEquals(Rational.ONE, Rational.of(-1).square());
        assertEquals(Rational.of(169, 36), Rational.of(13, 6).square());
        assertEquals(Rational.of(169, 36), Rational.of(-13, 6).square());
    }

    @Test
    void testDivide() {
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.divide(Rational.ZERO));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(4, 1).divide(Rational.ZERO));

        assertEquals(Rational.ZERO, Rational.ZERO.divide(Rational.of(5, 3)));
        assertEquals(Rational.of(19, 4), Rational.of(19, 4).divide(Rational.ONE));
        assertEquals(Rational.ONE, Rational.of(5, 3).divide(Rational.of(5, 3)));
        assertEquals(Rational.of(57, 52), Rational.of(19, 4).divide(Rational.of(13, 3)));
    }

    @Test
    void testDivideInteger() {
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.divideInteger(Rational.ZERO));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(4, 1).divideInteger(Rational.ZERO));

        assertEquals(BigInteger.ZERO, Rational.ZERO.divideInteger(Rational.of(2, 3)));
        assertEquals(BigInteger.ONE, Rational.of(2, 3).divideInteger(Rational.of(2, 3)));
        assertEquals(BigInteger.ONE, Rational.of(4, 5).divideInteger(Rational.of(2, 3)));
        assertEquals(BigInteger.valueOf(70), Rational.of(972, 13).divideInteger(Rational.of(412, 389)));
        assertEquals(BigInteger.valueOf(3), Rational.of(27, 14).divideInteger(Rational.of(37, 62)));
        assertEquals(BigInteger.valueOf(3), Rational.of(-27, 14).divideInteger(Rational.of(-37, 62)));
        assertEquals(BigInteger.valueOf(-13), Rational.of(49, 6).divideInteger(Rational.of(-13, 22)));
        assertEquals(BigInteger.valueOf(-13), Rational.of(-49, 6).divideInteger(Rational.of(13, 22)));
        assertEquals(BigInteger.valueOf(16), Rational.of(16).divideInteger(Rational.of(1)));
        assertEquals(BigInteger.valueOf(5), Rational.of(16).divideInteger(Rational.of(3)));
        assertEquals(BigInteger.valueOf(26), Rational.of(16).divideInteger(Rational.of(3, 5)));
        assertEquals(BigInteger.valueOf(2), Rational.of(28, 5).divideInteger(Rational.of(2)));
    }

    @Test
    void testDivideIntegerAndRemainder() {
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.divideIntegerAndRemainder(Rational.ZERO));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(4, 1).divideIntegerAndRemainder(Rational.ZERO));

        assertArrayEquals(new Number[] { BigInteger.ZERO, Rational.ZERO }, Rational.ZERO.divideIntegerAndRemainder(Rational.of(2, 3)));
        assertArrayEquals(new Number[] { BigInteger.ONE, Rational.ZERO }, Rational.of(2, 3).divideIntegerAndRemainder(Rational.of(2, 3)));
        assertArrayEquals(new Number[] { BigInteger.ONE, Rational.of(1, 5) }, Rational.of(4, 5).divideIntegerAndRemainder(Rational.of(2, 3)));
        assertArrayEquals(new Number[] { BigInteger.valueOf(70), Rational.of(797, 1339) },
                Rational.of(972, 13).divideIntegerAndRemainder(Rational.of(412, 389)));
        assertArrayEquals(new Number[] { BigInteger.valueOf(3), Rational.of(60, 259) },
                Rational.of(27, 14).divideIntegerAndRemainder(Rational.of(37, 62)));
        assertArrayEquals(new Number[] { BigInteger.valueOf(3), Rational.of(60, 259) },
                Rational.of(-27, 14).divideIntegerAndRemainder(Rational.of(-37, 62)));
        assertArrayEquals(new Number[] { BigInteger.valueOf(-13), Rational.of(-32, 39) },
                Rational.of(49, 6).divideIntegerAndRemainder(Rational.of(-13, 22)));
        assertArrayEquals(new Number[] { BigInteger.valueOf(-13), Rational.of(-32, 39) },
                Rational.of(-49, 6).divideIntegerAndRemainder(Rational.of(13, 22)));
        assertArrayEquals(new Number[] { BigInteger.valueOf(16), Rational.ZERO }, Rational.of(16).divideIntegerAndRemainder(Rational.of(1)));
        assertArrayEquals(new Number[] { BigInteger.valueOf(5), Rational.ONE }, Rational.of(16).divideIntegerAndRemainder(Rational.of(3)));
        assertArrayEquals(new Number[] { BigInteger.valueOf(26), Rational.of(2, 3) }, Rational.of(16).divideIntegerAndRemainder(Rational.of(3, 5)));
        assertArrayEquals(new Number[] { BigInteger.valueOf(2), Rational.of(4, 5) }, Rational.of(28, 5).divideIntegerAndRemainder(Rational.of(2)));
    }

    @Test
    void testReciprocal() {
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.reciprocal());

        assertEquals(Rational.ONE, Rational.ONE.reciprocal());
        assertEquals(Rational.ONE_HALF, Rational.TWO.reciprocal());
        assertEquals(Rational.of(2, 1), Rational.of(1, 2).reciprocal());
        assertEquals(Rational.of(1, 2), Rational.of(2, 1).reciprocal());
        assertEquals(Rational.of(-2, 1), Rational.of(-1, 2).reciprocal());
        assertEquals(Rational.of(1, -2), Rational.of(-2, 1).reciprocal());
        assertEquals(Rational.of(13, 5), Rational.of(5, 13).reciprocal());
        assertEquals(Rational.of(5, 13), Rational.of(13, 5).reciprocal());
        assertEquals(Rational.of(-13, 5), Rational.of(-5, 13).reciprocal());
        assertEquals(Rational.of(-5, 13), Rational.of(-13, 5).reciprocal());
    }

    @Test
    void testIsReciprocalOf() {
        assertFalse(Rational.of(0).isReciprocalOf(Rational.of(0)));
        assertFalse(Rational.of(3, 7).isReciprocalOf(Rational.of(0)));
        assertFalse(Rational.of(0).isReciprocalOf(Rational.of(3, 7)));
        assertFalse(Rational.of(-3, 7).isReciprocalOf(Rational.of(0)));
        assertFalse(Rational.of(0).isReciprocalOf(Rational.of(-3, 7)));
        assertFalse(Rational.of(19, 8).isReciprocalOf(Rational.of(19, 8)));
        assertTrue(Rational.of(19, 8).isReciprocalOf(Rational.of(8, 19)));
        assertFalse(Rational.of(-19, 8).isReciprocalOf(Rational.of(8, 19)));
        assertTrue(Rational.of(-19, 8).isReciprocalOf(Rational.of(-8, 19)));
        assertFalse(Rational.of(-19, 8).isReciprocalOf(Rational.of(-8, 18)));
        assertFalse(Rational.of(-19, 8).isReciprocalOf(Rational.of(-7, 19)));
        assertFalse(Rational.of(-19, 8).isReciprocalOf(Rational.of(-9, 19)));
        assertFalse(Rational.of(-19, 8).isReciprocalOf(Rational.of(8, 19)));
        assertTrue(Rational.of(1, 5).isReciprocalOf(Rational.of(5)));
        assertTrue(Rational.of(5).isReciprocalOf(Rational.of(1, 5)));
    }

    @Test
    void testSignum() {
        assertEquals(1, Rational.of(1L).signum());
        assertEquals(1, Rational.of(2).signum());
        assertEquals(1, Rational.of(3L).signum());
        assertEquals(0, Rational.of(0L).signum());
        assertEquals(-1, Rational.of(-1).signum());
        assertEquals(-1, Rational.of(-2).signum());
        assertEquals(-1, Rational.of(-3).signum());
    }

    @Test
    void testNegate() {
        assertEquals(Rational.of(0), Rational.of(0).negate());
        assertEquals(Rational.of(1), Rational.of(-1).negate());
        assertEquals(Rational.of(-1), Rational.of(1).negate());
        assertEquals(Rational.of(7L, 17L), Rational.of(-7, 17).negate());
        assertEquals(Rational.of(5L, 1L), Rational.of(-5, 1).negate());
    }

    @Test
    void testIsNegationOf() {
        assertTrue(Rational.of(0).isNegationOf(Rational.of(0)));
        assertFalse(Rational.of(0).isNegationOf(Rational.of(3, 2)));
        assertFalse(Rational.of(3, 2).isNegationOf(Rational.of(0)));
        assertTrue(Rational.of(4, 7).isNegationOf(Rational.of(-4, 7)));
        assertTrue(Rational.of(-4, 7).isNegationOf(Rational.of(4, 7)));
        assertFalse(Rational.of(4, 7).isNegationOf(Rational.of(4, 7)));
        assertFalse(Rational.of(-4, 7).isNegationOf(Rational.of(-4, 7)));
        assertFalse(Rational.of(3, 7).isNegationOf(Rational.of(4, 7)));
        assertFalse(Rational.of(3, 6).isNegationOf(Rational.of(3, 7)));
        assertFalse(Rational.of(-3, 7).isNegationOf(Rational.of(4, 7)));
        assertFalse(Rational.of(-3, 6).isNegationOf(Rational.of(3, 7)));
    }

    @Test
    void testMin() {
        assertEquals(Rational.of(2, 5), Rational.of(3, 5).min(Rational.of(2, 5)));
        assertEquals(Rational.of(-3, 5), Rational.of(-3, 5).min(Rational.of(-2, 5)));
        assertEquals(Rational.of(3, 5), Rational.of(3, 5).min(Rational.of(3, 5)));
        assertEquals(Rational.of(3, 5), Rational.of(3, 5).min(Rational.of(19, 12)));
        assertEquals(Rational.of(-19, 12), Rational.of(-3, 5).min(Rational.of(-19, 12)));
    }

    @Test
    void testMax() {
        assertEquals(Rational.of(3, 5), Rational.of(3, 5).max(Rational.of(2, 5)));
        assertEquals(Rational.of(-2, 5), Rational.of(-3, 5).max(Rational.of(-2, 5)));
        assertEquals(Rational.of(3, 5), Rational.of(3, 5).max(Rational.of(3, 5)));
        assertEquals(Rational.of(19, 12), Rational.of(3, 5).max(Rational.of(19, 12)));
        assertEquals(Rational.of(-3, 5), Rational.of(-3, 5).max(Rational.of(-19, 12)));
    }

    @Test
    void testAbs() {
        assertEquals(Rational.of(3, 5), Rational.of(3, 5).abs());
        assertEquals(Rational.of(3, 5), Rational.of(-3, 5).abs());
        assertEquals(Rational.of(733, 166), Rational.of(733, 166).abs());
        assertEquals(Rational.of(733, 166), Rational.of(-733, 166).abs());
        assertEquals(Rational.of(0), Rational.of(0).abs());
    }

    @Test
    void testRedouble() {
        assertEquals(Rational.of(0), Rational.of(0).redouble());
        assertEquals(Rational.of(1), Rational.of(1, 2).redouble());
        assertEquals(Rational.of(-1), Rational.of(-1, 2).redouble());
        assertEquals(Rational.of(2), Rational.of(1).redouble());
        assertEquals(Rational.of(-2), Rational.of(-1).redouble());
        assertEquals(Rational.of(3), Rational.of(3, 2).redouble());
        assertEquals(Rational.of(-3), Rational.of(-3, 2).redouble());
        assertEquals(Rational.of(30, 19), Rational.of(15, 19).redouble());
        assertEquals(Rational.of(-30, 19), Rational.of(-15, 19).redouble());
        assertEquals(Rational.of(15, 9), Rational.of(15, 18).redouble());
        assertEquals(Rational.of(-15, 9), Rational.of(-15, 18).redouble());
    }

    @Test
    void testHalve() {
        assertEquals(Rational.of(0), Rational.of(0).halve());
        assertEquals(Rational.of(1), Rational.of(2).halve());
        assertEquals(Rational.of(-1), Rational.of(-2).halve());
        assertEquals(Rational.of(1, 4), Rational.of(1, 2).halve());
        assertEquals(Rational.of(-1, 4), Rational.of(-1, 2).halve());
        assertEquals(Rational.of(1, 2), Rational.of(1).halve());
        assertEquals(Rational.of(-1, 2), Rational.of(-1).halve());
        assertEquals(Rational.of(3, 4), Rational.of(3, 2).halve());
        assertEquals(Rational.of(-3, 4), Rational.of(-3, 2).halve());
        assertEquals(Rational.of(15, 38), Rational.of(15, 19).halve());
        assertEquals(Rational.of(-15, 38), Rational.of(-15, 19).halve());
        assertEquals(Rational.of(7, 19), Rational.of(14, 19).halve());
        assertEquals(Rational.of(-7, 19), Rational.of(-14, 19).halve());
    }

    @Test
    void testPow() {
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.pow(0));
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.pow(-1));
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.pow(-2));
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.pow(-25));

        assertEquals(Rational.ZERO, Rational.ZERO.pow(1));
        assertEquals(Rational.ZERO, Rational.ZERO.pow(2));
        assertEquals(Rational.ZERO, Rational.ZERO.pow(17));
        assertEquals(Rational.ONE, Rational.ONE_HALF.pow(0));
        assertEquals(Rational.ONE, Rational.ONE.pow(0));
        assertEquals(Rational.ONE, Rational.TWO.pow(0));
        assertEquals(Rational.ONE, Rational.MINUS_ONE.pow(0));
        assertEquals(Rational.ONE, Rational.ONE.pow(1));
        assertEquals(Rational.MINUS_ONE, Rational.MINUS_ONE.pow(1));
        assertEquals(Rational.ONE, Rational.MINUS_ONE.pow(2));
        assertEquals(Rational.TWO, Rational.TWO.pow(1));
        assertEquals(Rational.ONE, Rational.ONE.pow(2));
        assertEquals(Rational.ONE, Rational.ONE.pow(17));
        assertEquals(Rational.of(289, 81), Rational.of(17L, 9L).pow(2));
        assertEquals(Rational.of(289, 81), Rational.of(-17L, 9L).pow(2));
        assertEquals(Rational.of(4913, 729), Rational.of(17L, 9L).pow(3));
        assertEquals(Rational.of(-4913, 729), Rational.of(-17L, 9L).pow(3));
        assertEquals(Rational.of(83521, 6561), Rational.of(17L, 9L).pow(4));
        assertEquals(Rational.of(1419857, 59049), Rational.of(17L, 9L).pow(5));
        assertEquals(Rational.of(456, 123), Rational.of(123, 456).pow(-1));
        assertEquals(Rational.of(456 * 456, 123 * 123), Rational.of(123, 456).pow(-2));
        assertEquals(Rational.of(456 * 456 * 456, 123 * 123 * 123), Rational.of(123, 456).pow(-3));
        assertEquals(Rational.of(456L * 456L * 456L * 456L, 123L * 123L * 123L * 123L), Rational.of(123, 456).pow(-4));
    }

    @Test
    void testGcd() {
        assertEquals(Rational.ZERO, Rational.ZERO.gcd(Rational.ZERO));
        assertEquals(Rational.of(3, 5), Rational.ZERO.gcd(Rational.of(3, 5)));
        assertEquals(Rational.of(1, 5), Rational.ONE.gcd(Rational.of(3, 5)));
        assertEquals(Rational.of(3, 5), Rational.of(3, 5).gcd(Rational.ZERO));
        assertEquals(Rational.of(1, 5), Rational.of(3, 5).gcd(Rational.ONE));
        assertEquals(Rational.of(3, 5), Rational.of(3, 5).gcd(Rational.of(3, 5)));
        assertEquals(Rational.of(1, 15), Rational.of(3, 5).gcd(Rational.of(4, 6)));
        assertEquals(Rational.of(1, 28), Rational.of(-9, 4).gcd(Rational.of(13, 7)));
        assertEquals(Rational.of(1, 80), Rational.of(255, 16).gcd(Rational.of(193, 5)));
        assertEquals(Rational.of(5, 4), Rational.of(55, 4).gcd(Rational.of(5, 4)));
        assertEquals(Rational.of(11, 12), Rational.of(55, 4).gcd(Rational.of(11, 6)));
        assertEquals(Rational.of(1, 13), Rational.of(85, 17).gcd(Rational.of(217, 13)));
    }

    @Test
    void testLcm() {
        assertEquals(Rational.ZERO, Rational.ZERO.lcm(Rational.ZERO));
        assertEquals(Rational.of(0), Rational.ZERO.lcm(Rational.of(3, 5)));
        assertEquals(Rational.of(3), Rational.ONE.lcm(Rational.of(3, 5)));
        assertEquals(Rational.of(0), Rational.of(3, 5).lcm(Rational.ZERO));
        assertEquals(Rational.of(3), Rational.of(3, 5).lcm(Rational.ONE));
        assertEquals(Rational.of(3, 5), Rational.of(3, 5).lcm(Rational.of(3, 5)));
        assertEquals(Rational.of(6), Rational.of(3, 5).lcm(Rational.of(4, 6)));
        assertEquals(Rational.of(117), Rational.of(-9, 4).lcm(Rational.of(13, 7)));
        assertEquals(Rational.of(49215), Rational.of(255, 16).lcm(Rational.of(193, 5)));
        assertEquals(Rational.of(55, 4), Rational.of(55, 4).lcm(Rational.of(5, 4)));
        assertEquals(Rational.of(55, 2), Rational.of(55, 4).lcm(Rational.of(11, 6)));
        assertEquals(Rational.of(1085), Rational.of(85, 17).lcm(Rational.of(217, 13)));
    }

    @Test
    void testToInteger() {
        assertEquals(BigInteger.ZERO, Rational.ZERO.toInteger());
        assertEquals(BigInteger.ONE, Rational.ONE.toInteger());
        assertEquals(BigInteger.valueOf(7), Rational.of(77, 10).toInteger());
        assertEquals(BigInteger.valueOf(-7), Rational.of(-77, 10).toInteger());
    }

    @Test
    void testToDecimal() {
        assertEquals(BigDecimal.valueOf(0), Rational.ZERO.toDecimal());
        assertEquals(BigDecimal.valueOf(1), Rational.ONE.toDecimal());
        assertTrue(Math.abs(Rational.of(77, 10).toDecimal().doubleValue() - new BigDecimal("7.7").doubleValue()) < 0.0000001);
        assertTrue(Math.abs(Rational.of(-77, 10).toDecimal().doubleValue() - new BigDecimal("-7.7").doubleValue()) < 0.0000001);
        assertTrue(Math.abs(Rational.of(12345678, 100).toDecimal().doubleValue() - new BigDecimal("123456.78").doubleValue()) < 0.0000001);
        assertTrue(Math.abs(Rational.of(-12345678, 100).toDecimal().doubleValue() - new BigDecimal("-123456.78").doubleValue()) < 0.0000001);

        assertEquals("0.14285", Rational.of(1, 7).toDecimal(5, RoundingMode.DOWN).toString());
        assertEquals("0.142857142857", Rational.of(1, 7).toDecimal(12, RoundingMode.DOWN).toString());
        assertEquals("0.89361702127659574468085106382978723404255319148936", Rational.of(42, 47).toDecimal(50, RoundingMode.DOWN).toString());
        assertEquals("4711", Rational.of(4711).toDecimal(50, RoundingMode.DOWN).toString());
    }

    @Test
    void testIntValue() {
        assertEquals(0, Rational.ZERO.intValue());
        assertEquals(1, Rational.ONE.intValue());
        assertEquals(7, Rational.of(77, 10).intValue());
        assertEquals(-7, Rational.of(-77, 10).intValue());
    }

    @Test
    void testLongValue() {
        assertEquals(0L, Rational.ZERO.longValue());
        assertEquals(1L, Rational.ONE.longValue());
        assertEquals(7L, Rational.of(77, 10).longValue());
        assertEquals(-7L, Rational.of(-77, 10).longValue());
    }

    @Test
    void testDoubleValue() {
        assertTrue(Math.abs(Rational.ZERO.doubleValue() - new BigDecimal("0").doubleValue()) < 0.0000001);
        assertTrue(Math.abs(Rational.ONE.doubleValue() - new BigDecimal("1").doubleValue()) < 0.0000001);
        assertTrue(Math.abs(Rational.of(-77, 10).doubleValue() - new BigDecimal("-7.7").doubleValue()) < 0.0000001);
        assertTrue(Math.abs(Rational.of(12345678, 100).doubleValue() - new BigDecimal("123456.78").doubleValue()) < 0.0000001);
        assertTrue(Math.abs(Rational.of(-12345678, 100).doubleValue() - new BigDecimal("-123456.78").doubleValue()) < 0.0000001);
    }

    @Test
    void testFloatValue() {
        assertTrue(Math.abs(Rational.ZERO.floatValue() - new BigDecimal("0").floatValue()) < 0.0000001);
        assertTrue(Math.abs(Rational.ONE.floatValue() - new BigDecimal("1").floatValue()) < 0.0000001);
        assertTrue(Math.abs(Rational.of(-77, 10).floatValue() - new BigDecimal("-7.7").floatValue()) < 0.0000001);
        assertTrue(Math.abs(Rational.of(12345678, 100).floatValue() - new BigDecimal("123456.78").floatValue()) < 0.0000001);
        assertTrue(Math.abs(Rational.of(-12345678, 100).floatValue() - new BigDecimal("-123456.78").floatValue()) < 0.0000001);
    }

    @Test
    void testCheck() {
        assertThrows(RuntimeException.class, () -> new Rational(null, BigInteger.ZERO, 1, true, true).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.ZERO, null, 1, true, true).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.ONE, BigInteger.ZERO, 1, true, true).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.ONE, BigInteger.valueOf(-1), -1, true, false).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(8), BigInteger.valueOf(4), 1, true, false).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(1), BigInteger.valueOf(1), 1, true, false).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(7), BigInteger.valueOf(1), -1, true, false).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(7), BigInteger.valueOf(1), 0, true, false).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(-7), BigInteger.valueOf(1), 1, true, false).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(-7), BigInteger.valueOf(1), 0, true, false).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(0), BigInteger.valueOf(1), 1, true, false).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(0), BigInteger.valueOf(1), -1, true, false).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(0), BigInteger.valueOf(1), 0, false, false).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(1), BigInteger.valueOf(2), 1, true, false).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(1), BigInteger.valueOf(1), 0, true, true).check());
        assertThrows(RuntimeException.class, () -> new Rational(BigInteger.valueOf(0), BigInteger.valueOf(1), 1, true, false).check());
    }

    @Test
    void testToString() {
        assertEquals("0", Rational.ZERO.toString());
        assertEquals("1", Rational.ONE.toString());
        assertEquals("-1", Rational.ONE.negate().toString());
        assertEquals("41/152", Rational.of(123, 456).toString());
        assertEquals("-41/152", Rational.of(-123, 456).toString());
    }

    @Test
    void testCompareTo() {
        assertEquals(-1, Rational.ZERO.compareTo(Rational.ONE));
        assertEquals(0, Rational.ONE.compareTo(Rational.ONE));
        assertEquals(1, Rational.ONE.compareTo(Rational.ZERO));
        assertEquals(1, Rational.of(2, 3).compareTo(Rational.of(4, 9)));
        assertEquals(-1, Rational.of(4, 9).compareTo(Rational.of(2, 3)));
        assertEquals(-1, Rational.of(-4, 9).compareTo(Rational.of(2, 3)));
        assertEquals(-1, Rational.of(-2, 3).compareTo(Rational.of(4, 9)));
        assertEquals(1, Rational.of(2, 3).compareTo(Rational.of(-4, 9)));
        assertEquals(1, Rational.of(-4, 9).compareTo(Rational.of(-2, 3)));
        assertEquals(-1, Rational.of(-4, 9).compareTo(Rational.of(4, 9)));
    }

    @Test
    void testEquals() {
        assertFalse(Rational.of(16, 5).equals(new Object()));
        assertTrue(Rational.of(16, 5).equals(Rational.of(32, 10)));
        assertFalse(Rational.of(16, 5).equals(Rational.of(16, 4)));
        assertFalse(Rational.of(15, 5).equals(Rational.of(16, 5)));
        assertFalse(Rational.of(15, 5).equals(Rational.of(16, 4)));
        assertFalse(Rational.of(16, 5).equals(Rational.of(16, -5)));
        assertFalse(Rational.of(16, 5).equals(Rational.of(-16, 5)));
        assertTrue(Rational.of(-16, 5).equals(Rational.of(16, -5)));
        assertTrue(Rational.of(-16, 5).equals(Rational.of(-16, 5)));
    }

    @Test
    void testHashCode() {
        assertEquals(-1107190671, Rational.of(12345, 67890).hashCode());
        assertEquals(61886582, Rational.of(-1234567890L, 9876543210L).hashCode());
    }

    @Test
    void testE1() {
        // approximate e

        Rational sum = Rational.ZERO;
        Rational factorial = Rational.ONE;

        for (int i = 0; i < 50; i++) {
            sum = sum.add(factorial);
            factorial = factorial.divide(Rational.of(i + 1));
        }

        assertEquals(Rational.of(new BigInteger("12719088750658039780384089386046426661997613299898270200126969"),
                new BigInteger("4679091261802058160555785871702272129904252549070848000000000")), sum);
    }

    @Test
    void testE2() {
        // approximate e^3, without using pow()

        Rational c = Rational.of(3);

        Rational x = Rational.ONE;
        Rational sum = Rational.ZERO;
        Rational factorial = Rational.ONE;

        for (int i = 0; i < 50; i++) {
            sum = sum.add(x.multiply(factorial));
            x = x.multiply(c);
            factorial = factorial.divide(Rational.of(i + 1));
        }

        assertEquals(Rational.of(new BigInteger("97333136547926737301012888635254293286814530131950829"),
                new BigInteger("4845931523770265122090735735752760324259840000000000")), sum);

    }

    @Test
    void testE3() {
        // approximate e^3, using pow()

        Rational c = Rational.of(3);

        Rational sum = Rational.ZERO;
        Rational factorial = Rational.ONE;

        for (int i = 0; i < 50; i++) {
            sum = sum.add(c.pow(i).multiply(factorial));
            factorial = factorial.divide(Rational.of(i + 1));
        }

        assertEquals(Rational.of(new BigInteger("97333136547926737301012888635254293286814530131950829"),
                new BigInteger("4845931523770265122090735735752760324259840000000000")), sum);
    }

    @Test
    void testH1000() {
        // calculate H_1000, the 1000'th Harmonic number

        Rational sum = Rational.ZERO;

        for (int i = 1; i <= 1000; i++) {
            sum = sum.add(Rational.of(1, i));
        }

        assertEquals(Rational.of(new BigInteger(
                "53362913282294785045591045624042980409652472280384260097101349248456268889497101757506097901985035691409088731550468098378442172117885009464302344326566022502100278425632852081405544941210442510142672770294774712708917963967779610453224692426866468888281582071984897105110796873249319155529397017508931564519976085734473014183284011724412280649074307703736683170055800293659235088589360235285852808160759574737836655413175508131522517"),
                new BigInteger(
                        "7128865274665093053166384155714272920668358861885893040452001991154324087581111499476444151913871586911717817019575256512980264067621009251465871004305131072686268143200196609974862745937188343705015434452523739745298963145674982128236956232823794011068809262317708861979540791247754558049326475737829923352751796735248042463638051137034331214781746850878453485678021888075373249921995672056932029099390891687487672697950931603520000")),
                sum);
    }

    @Test
    void testLn2() {
        // approximate ln(2) using the 1000th partial sum of the alternating Harmonic series

        Rational sum = Rational.ZERO;

        for (int i = 1; i <= 1000; i++) {
            sum = sum.add(Rational.of((i % 2) > 0 ? 1 : -1, i));
        }

        assertEquals(Rational.of(
                "4937790215303904379625531938825779946100713125770473348897738659941095286866450943449698645388475442704066094104875369365939573941321907700818330906904692080097434687930193989481947211915390582175815541421224025307798265496347982662631751380933793540471517859473601783917361384214703881079292192008187313164050082034643619238257085294408414212863236104619096962528583819754790264673829998544542831721722741539798798387356122429364267/7128865274665093053166384155714272920668358861885893040452001991154324087581111499476444151913871586911717817019575256512980264067621009251465871004305131072686268143200196609974862745937188343705015434452523739745298963145674982128236956232823794011068809262317708861979540791247754558049326475737829923352751796735248042463638051137034331214781746850878453485678021888075373249921995672056932029099390891687487672697950931603520000"),
                sum);
    }

    @Test
    void testSqrt3() {
        // approximate sqrt(3) starting from 2 using 10 Newton-Raphson iterations

        // compute square root of this number
        Rational sqrt = Rational.of(3);

        // start
        Rational x = Rational.of(2);

        for (int i = 0; i < 10; i++) {
            x = x.add(sqrt.divide(x)).halve();
        }

        assertEquals(Rational.of(
                "2361884621859093944424922583935647542436774565268000651212692535795840310603318657942311035831699375859782443869472701468345963893510551577942858717646305960908338050131552256583071900247836267846089971404454566365817515288559790438512594780498882596927166880522373508088684203563180215446887894295934440879096770627402486850806652075366125287038565343886661389071308861952705717106035709402238044209645303094553866793781270755330115697899221958373888893626146711615861323391324707819971073837627846255498558710993962380962928954736309126424782515719869712443608110852912638598783696897/1363634722225185346012432568604655281965637920387792783352830682862026431895620844573348305071198606158370874512697525762562289957278887213574720947757024801525760999320058378260256817290160387640723183188708724537026006624238541945157545997873202442384320516505965266653822333556554331669580708994301176269000399204046135889646652915938110019603902245189164901762986209575567837748255737815920882301791618125897038249292485541939772791812756983271833309148799576136554511984857589823068623771704247809469831220704791967040916531095340485448464636693176731893805381741819663018121312256"),
                x);
    }

    @Test
    void testSqrt5() {
        // approximate sqrt(5) starting from 5 using 12 Newton-Raphson iterations

        // compute square root of this number
        Rational sqrt = Rational.of(5);

        // start
        Rational x = sqrt;

        for (int i = 0; i < 12; i++) {
            x = x.add(sqrt.divide(x)).halve();
        }

        assertEquals(Rational.of(
                "10312749385725830441802540740065515313851581175704414324490412533525451490068004056562555361964908876484176036910001863956624258046666641669263753146501441696735313929679410169743156859880792433984484789744611483427989571676851808465471269623465120455949137399300388899881375908805357877664472786728973959341025009696822210060021549414146476712439210747763775120130672397433546451190984800181655778096308930673967929247719493575654387577417021188689832435468683226810568309660909493278434794372904636841074705443843966438365619077399335713902545305125588892910321945577013801232719970629342173590182755151700994830811432603344551390194800209686663531108157905060696146769539632464420378593090076812946660899750748092666861150545474346290900031927298783752074470630435760298639123733285580454340205109167983980835350647593511713883686631638820631484590891007/4612001732280431247456445708563614127173224997617390534215059226137357133453956236072775985077061637311848907129417864574275423997101439882308358166652317363373656716074141072814493065517475413688262677419077617088948496309673353922704120725679705669386361748442871720790233981292904246541321855474289727005675146240418903692583131115962989146454578739972233255840007113102596686397958930124518885822059783685448190039658062872691964066428723178769322339485834664335313247796472730324095846596733944704930052412653763777113749102514483039561246866695780115646150369678333299122486379683222039167477498691611996122878629556831081616202064636498715093853352203252703786287926199052408354498825123496861419106453928530148716831934981264321286848387438601077819789292236505514653845305057927646386419899455438488952785050077521931600327064840520442470066917947"),
                x);
    }

    @Test
    void testPhi() {
        // approximate phi (the Golden Ratio) using a continued fraction to 250 decimal digits

        Rational x = Rational.ONE;

        for (int i = 0; i < 601; i++) {
            x = x.reciprocal().add(Rational.ONE);
        }

        assertEquals(Rational.of(
                "467801993911057346969253632393329698441821925792111695787002567703451068793258021745557947676079828499403918483241873884718402/289117532242004794657842939580523992192206081574833651083505729789364385249494747835588176048973824211799073812844695893338801"),
                x);
    }

    /**
     * Like assertEquals(...), but does a sanity check on both arguments first.
     *
     * @param expected the expected Rational
     * @param actual the actual Rational
     */
    private static void assertEquals(Rational expected, Rational actual) {
        expected.check();
        actual.check();
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }

    /**
     * Delegates to org.junit.jupiter.api.Assertions.assertEquals(...).
     *
     * @param expected the expected Object
     * @param actual the actual Object
     */
    private static void assertEquals(Object expected, Object actual) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }

}
