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

package com.schuerger.util.rationalj;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

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
        assertEquals(Rational.ONE, Rational.of(7, 7));
        assertEquals(Rational.ONE, Rational.of(7L, 7L));
        assertEquals(Rational.ONE, Rational.of(BigInteger.valueOf(7), BigInteger.valueOf(7)));
        assertEquals(Rational.ZERO, Rational.of(0));
        assertEquals(Rational.ZERO, Rational.of(0L));
        assertEquals(Rational.ZERO, Rational.valueOf(BigInteger.ZERO));
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
    }

    @Test
    void testSquare() {
        assertEquals(Rational.ZERO, Rational.ZERO.square());
        assertEquals(Rational.ONE, Rational.ONE.square());
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
        assertEquals(Rational.of(1, -2), Rational.of(-2, 1).reciprocal());
        assertEquals(Rational.of(13, 5), Rational.of(5, 13).reciprocal());
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
        assertEquals(Rational.of(7L, 17L), Rational.of(-7, 17).negate());
        assertEquals(Rational.of(5L, 1L), Rational.of(-5, 1).negate());
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
    void testPow() {
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.pow(0));

        assertEquals(Rational.ZERO, Rational.ZERO.pow(1));
        assertEquals(Rational.ZERO, Rational.ZERO.pow(2));
        assertEquals(Rational.ZERO, Rational.ZERO.pow(17));
        assertEquals(Rational.ONE, Rational.ONE.pow(0));
        assertEquals(Rational.ONE, Rational.ONE.pow(1));
        assertEquals(Rational.TWO, Rational.TWO.pow(1));
        assertEquals(Rational.ONE, Rational.ONE.pow(2));
        assertEquals(Rational.ONE, Rational.ONE.pow(17));
        assertEquals(Rational.of(289, 81), Rational.of(17L, 9L).pow(2));
        assertEquals(Rational.of(4913, 729), Rational.of(17L, 9L).pow(3));
        assertEquals(Rational.of(83521, 6561), Rational.of(17L, 9L).pow(4));
        assertEquals(Rational.of(1419857, 59049), Rational.of(17L, 9L).pow(5));
        assertEquals(Rational.of(456, 123), Rational.of(123, 456).pow(-1));
        assertEquals(Rational.of(456 * 456, 123 * 123), Rational.of(123, 456).pow(-2));
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
        assertEquals(0L, Rational.ZERO.intValue());
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

}