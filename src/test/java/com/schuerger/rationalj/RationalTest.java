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

package com.schuerger.rationalj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class RationalTest {

    @Test
    void testOf() {
        assertThrows(IllegalArgumentException.class, () -> Rational.of(0, 0));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(1, 0));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(5, 0));
    }

    @Test
    void testNormalization() {
        assertEquals(Rational.of(0, 1), Rational.of(0, 4431));
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
    void testReciprocal() {
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.reciprocal());

        assertEquals(Rational.ONE, Rational.ONE.reciprocal());
        assertEquals(Rational.ONE_HALF, Rational.TWO.reciprocal());
        assertEquals(Rational.MINUS_ONE_HALF, Rational.MINUS_TWO.reciprocal());
        assertEquals(Rational.of(13, 5), Rational.of(5, 13).reciprocal());
    }

    @Test
    void testSignum() {
        assertEquals(1, Rational.of(1).signum());
        assertEquals(1, Rational.of(2).signum());
        assertEquals(1, Rational.of(3).signum());
        assertEquals(0, Rational.of(0).signum());
        assertEquals(-1, Rational.of(-1).signum());
        assertEquals(-1, Rational.of(-2).signum());
        assertEquals(-1, Rational.of(-3).signum());
    }

    @Test
    void testNegate() {
        assertEquals(Rational.of(0), Rational.of(0).negate());
        assertEquals(Rational.of(1), Rational.of(-1).negate());
        assertEquals(Rational.of(7, 17), Rational.of(-7, 17).negate());
    }

    @Test
    void testPow() {
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.pow(0));

        assertEquals(Rational.ZERO, Rational.ZERO.pow(1));
        assertEquals(Rational.ZERO, Rational.ZERO.pow(2));
        assertEquals(Rational.ZERO, Rational.ZERO.pow(17));
        assertEquals(Rational.ONE, Rational.ONE.pow(0));
        assertEquals(Rational.ONE, Rational.ONE.pow(1));
        assertEquals(Rational.ONE, Rational.ONE.pow(2));
        assertEquals(Rational.ONE, Rational.ONE.pow(17));
        assertEquals(Rational.of(289, 81), Rational.of(17, 9).pow(2));
        assertEquals(Rational.of(456, 123), Rational.of(123, 456).pow(-1));
        assertEquals(Rational.of(456 * 456, 123 * 123), Rational.of(123, 456).pow(-2));
    }

    @Test
    void testToString() {
        assertEquals("0", Rational.ZERO.toString());
        assertEquals("1", Rational.ONE.toString());
        assertEquals("-1", Rational.MINUS_ONE.toString());
        assertEquals("41/152", Rational.of(123, 456).toString());
        assertEquals("-41/152", Rational.of(-123, 456).toString());
    }

}
