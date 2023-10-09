/* Copyright 2023 Thomas Schuerger (thomas@schuerger.com)
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
import java.security.SecureRandom;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Tests for the class Rational.
 *
 * @author Thomas Schuerger (thomas@schuerger.com)
 */

class RationalTest {

    /** Pi with 1000 decimal digits. */
    private static final String PI_1000 = "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679821480865132823066470938446095505822317253594081284811174502841027019385211055596446229489549303819644288109756659334461284756482337867831652712019091456485669234603486104543266482133936072602491412737245870066063155881748815209209628292540917153643678925903600113305305488204665213841469519415116094330572703657595919530921861173819326117931051185480744623799627495673518857527248912279381830119491298336733624406566430860213949463952247371907021798609437027705392171762931767523846748184676694051320005681271452635608277857713427577896091736371787214684409012249534301465495853710507922796892589235420199561121290219608640344181598136297747713099605187072113499999983729780499510597317328160963185950244594553469083026425223082533446850352619311881710100031378387528865875332083814206171776691473035982534904287554687311595628638823537875937519577818577805321712268066130019278766111959092164201989";

    @Test
    void testOfString() {
        assertThrows(IllegalArgumentException.class, () -> Rational.of("125-125"));
        assertThrows(IllegalArgumentException.class, () -> Rational.of("125-"));
        assertThrows(IllegalArgumentException.class, () -> Rational.of("12*5"));
        assertThrows(IllegalArgumentException.class, () -> Rational.of("7/0"));
        assertThrows(IllegalArgumentException.class, () -> Rational.of("5/7/"));
        assertThrows(IllegalArgumentException.class, () -> Rational.of(""));
        assertThrows(IllegalArgumentException.class, () -> Rational.of("abc"));
        assertThrows(IllegalArgumentException.class, () -> Rational.of("abc/def"));
        assertThrows(IllegalArgumentException.class, () -> Rational.of("123/def"));
        assertThrows(IllegalArgumentException.class, () -> Rational.of("abc/456"));
        assertThrows(IllegalArgumentException.class, () -> Rational.of("123,456"));
        assertThrows(IllegalArgumentException.class, () -> Rational.of("-123.567#5a"));
        assertThrows(IllegalArgumentException.class, () -> Rational.of("-123.567_123+4"));

        assertEquals(Rational.of(0), Rational.of("0"));
        assertEquals(Rational.of(-1), Rational.of("-1"));
        assertEquals(Rational.of(12345), Rational.of("12345"));
        assertEquals(Rational.of(-87654321), Rational.of("-87654321"));
        assertEquals(Rational.of(5, 7), Rational.valueOf("5/7"));
        assertEquals(Rational.of(5, 7), Rational.of("-5/-7"));
        assertEquals(Rational.of(34, 57), Rational.valueOf("850/1425"));
        assertEquals(Rational.of(-34, 57), Rational.valueOf("850/-1425"));
        assertEquals(Rational.of(5), Rational.of("5."));
        assertEquals(Rational.of(-18), Rational.of("-18."));
        assertEquals(Rational.of(277), Rational.of("277.00000000"));
        assertEquals(Rational.of(-444), Rational.of("-444.00000"));
        assertEquals(Rational.of(1, 1000000), Rational.of("0.000001"));
        assertEquals(Rational.of(-100001, 1000000), Rational.of("-0.100001"));
        assertEquals(Rational.of(62831853, 20000000), Rational.of("3.14159265"));
        assertEquals(Rational.of(13333333, 10000000), Rational.of("1.3333333"));
        assertEquals(Rational.of(0), Rational.of("0._"));
        assertEquals(Rational.of(0), Rational.of("-0._"));
        assertEquals(Rational.of(2, 9), Rational.of("0._2"));
        assertEquals(Rational.of(-23, 99), Rational.of("-0._23"));
        assertEquals(Rational.of(4, 3), Rational.of("1._3"));
        assertEquals(Rational.of(4, 3), Rational.of("1._33"));
        assertEquals(Rational.of(4, 3), Rational.of("1._333"));
        assertEquals(Rational.of(-5, 3), Rational.of("-1._6"));
        assertEquals(Rational.of(-5, 3), Rational.of("-1._666"));
        assertEquals(Rational.of(33, 7), Rational.of("4._714285"));
        assertEquals(Rational.of(-1, 7), Rational.of("-0._142857"));
        assertEquals(Rational.of(4), Rational.of("4._"));
        assertEquals(Rational.of(41, 10), Rational.of("4.1_"));
        assertEquals(Rational.of(-3), Rational.of("-3._"));
        assertEquals(Rational.of(-31, 10), Rational.of("-3.1_"));
        assertEquals(Rational.of(120296611, 2499750), Rational.of("48.123_4567"));
        assertEquals(Rational.of(-274348422475994513L, 277777777750000L), Rational.of("-987.6543210_1234567890"));
        assertEquals(Rational.of(1234567890123456789L, 100000000000000L), Rational.of("12345.67890123456789"));
        assertEquals(Rational.of(-1234567890123456789L, 100000000000000L), Rational.of("-12345.67890123456789"));
        assertEquals(Rational.of(1234567890123456789L, 100000000000000L), Rational.of("12345.678901234567890000000000"));
        assertEquals(Rational.of(-1234567890123456789L, 100000000000000L), Rational.of("-12345.678901234567890000000000"));
    }

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
    void testOfReciprocal() {
        assertThrows(IllegalArgumentException.class, () -> Rational.ofReciprocal(0L));
        assertThrows(IllegalArgumentException.class, () -> Rational.ofReciprocal(0));
        assertThrows(IllegalArgumentException.class, () -> Rational.ofReciprocal(BigInteger.ZERO));

        assertEquals(Rational.ONE, Rational.ofReciprocal(1));
        assertEquals(Rational.ONE, Rational.ofReciprocal(1L));
        assertEquals(Rational.ONE, Rational.ofReciprocal(BigInteger.ONE));
        assertEquals(Rational.MINUS_ONE, Rational.ofReciprocal(-1));
        assertEquals(Rational.MINUS_ONE, Rational.ofReciprocal(-1L));
        assertEquals(Rational.MINUS_ONE, Rational.ofReciprocal(BigInteger.valueOf(-1)));
        assertEquals(Rational.ONE_HALF, Rational.ofReciprocal(2));
        assertEquals(Rational.ONE_HALF, Rational.ofReciprocal(2L));
        assertEquals(Rational.ONE_HALF, Rational.ofReciprocal(BigInteger.valueOf(2)));
        assertEquals(Rational.MINUS_ONE_HALF, Rational.ofReciprocal(-2));
        assertEquals(Rational.MINUS_ONE_HALF, Rational.ofReciprocal(-2L));
        assertEquals(Rational.MINUS_ONE_HALF, Rational.ofReciprocal(BigInteger.valueOf(-2)));
        assertEquals(Rational.of(1, 7), Rational.ofReciprocal(7));
        assertEquals(Rational.of(1, 7), Rational.ofReciprocal(7L));
        assertEquals(Rational.of(1, 7), Rational.ofReciprocal(BigInteger.valueOf(7)));
        assertEquals(Rational.of(-1, 7), Rational.ofReciprocal(-7));
        assertEquals(Rational.of(-1, 7), Rational.ofReciprocal(-7L));
        assertEquals(Rational.of(-1, 7), Rational.ofReciprocal(BigInteger.valueOf(-7)));

        assertEquals(Rational.TWO.pow(31).negate().reciprocal(), Rational.ofReciprocal(Integer.MIN_VALUE));
        assertEquals(Rational.TWO.pow(63).negate().reciprocal(), Rational.ofReciprocal(Long.MIN_VALUE));
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
    void testMod() {
        assertThrows(IllegalArgumentException.class, () -> Rational.of(7, 9).mod(Rational.ZERO));
        assertThrows(IllegalArgumentException.class, () -> Rational.ZERO.mod(Rational.ZERO));

        assertEquals(Rational.ZERO, Rational.ZERO.mod(Rational.of(3, 5)));
        assertEquals(Rational.ZERO, Rational.ONE.mod(Rational.ONE));
        assertEquals(Rational.ZERO, Rational.TEN.mod(Rational.ONE));
        assertEquals(Rational.of(4), Rational.TEN.mod(Rational.of(6)));
        assertEquals(Rational.of(2, 9), Rational.TEN.mod(Rational.of(8, 9)));
        assertEquals(Rational.of(4, 9), Rational.of(4, 9).mod(Rational.of(3, 5)));
        assertEquals(Rational.of(7, 45), Rational.of(3, 5).mod(Rational.of(4, 9)));
        assertEquals(Rational.of(26, 203), Rational.of(19, 7).mod(Rational.of(5, 29)));
        assertEquals(Rational.of(-26, 203), Rational.of(-19, 7).mod(Rational.of(-5, 29)));
        assertEquals(Rational.of(9, 203), Rational.of(-19, 7).mod(Rational.of(5, 29)));
        assertEquals(Rational.of(-9, 203), Rational.of(19, 7).mod(Rational.of(-5, 29)));
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
        assertEquals(Rational.of(1, 5), Rational.of(-1, 5).negate());
        assertEquals(Rational.of(-1, 5), Rational.of(1, 5).negate());
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
    void testFloor() {
        assertEquals(Rational.of(0), Rational.of(0).floor());
        assertEquals(Rational.of(1), Rational.of(1).floor());
        assertEquals(Rational.of(3), Rational.of(3).floor());
        assertEquals(Rational.of(-1), Rational.of(-1).floor());
        assertEquals(Rational.of(-17), Rational.of(-17).floor());

        assertEquals(Rational.of(0), Rational.of(1, 2).floor());
        assertEquals(Rational.of(0), Rational.of(1, 3).floor());
        assertEquals(Rational.of(0), Rational.of(1, 5).floor());
        assertEquals(Rational.of(0), Rational.of(2, 3).floor());
        assertEquals(Rational.of(0), Rational.of(4, 5).floor());
        assertEquals(Rational.of(1), Rational.of(7, 5).floor());
        assertEquals(Rational.of(4), Rational.of(29, 7).floor());
        assertEquals(Rational.of(4), Rational.of(99, 24).floor());
        assertEquals(Rational.of(23), Rational.of(541, 23).floor());

        assertEquals(Rational.of(-1), Rational.of(-1, 2).floor());
        assertEquals(Rational.of(-1), Rational.of(-1, 3).floor());
        assertEquals(Rational.of(-1), Rational.of(-1, 5).floor());
        assertEquals(Rational.of(-1), Rational.of(-2, 3).floor());
        assertEquals(Rational.of(-1), Rational.of(-4, 5).floor());
        assertEquals(Rational.of(-2), Rational.of(-7, 5).floor());
        assertEquals(Rational.of(-5), Rational.of(-29, 7).floor());
        assertEquals(Rational.of(-5), Rational.of(-99, 24).floor());
        assertEquals(Rational.of(-24), Rational.of(-541, 23).floor());
    }

    @Test
    void testCeil() {
        assertEquals(Rational.of(0), Rational.of(0).ceil());
        assertEquals(Rational.of(1), Rational.of(1).ceil());
        assertEquals(Rational.of(3), Rational.of(3).ceil());
        assertEquals(Rational.of(-1), Rational.of(-1).ceil());
        assertEquals(Rational.of(-17), Rational.of(-17).ceil());

        assertEquals(Rational.of(1), Rational.of(1, 2).ceil());
        assertEquals(Rational.of(1), Rational.of(1, 3).ceil());
        assertEquals(Rational.of(1), Rational.of(1, 5).ceil());
        assertEquals(Rational.of(1), Rational.of(2, 3).ceil());
        assertEquals(Rational.of(1), Rational.of(4, 5).ceil());
        assertEquals(Rational.of(2), Rational.of(7, 5).ceil());
        assertEquals(Rational.of(5), Rational.of(29, 7).ceil());
        assertEquals(Rational.of(5), Rational.of(99, 24).ceil());
        assertEquals(Rational.of(24), Rational.of(541, 23).ceil());

        assertEquals(Rational.of(0), Rational.of(-1, 2).ceil());
        assertEquals(Rational.of(0), Rational.of(-1, 3).ceil());
        assertEquals(Rational.of(0), Rational.of(-1, 5).ceil());
        assertEquals(Rational.of(0), Rational.of(-2, 3).ceil());
        assertEquals(Rational.of(0), Rational.of(-4, 5).ceil());
        assertEquals(Rational.of(-1), Rational.of(-7, 5).ceil());
        assertEquals(Rational.of(-4), Rational.of(-29, 7).ceil());
        assertEquals(Rational.of(-4), Rational.of(-99, 24).ceil());
        assertEquals(Rational.of(-23), Rational.of(-541, 23).ceil());
    }

    @Test
    void testRound() {
        assertEquals(Rational.of(0), Rational.of(0).round());
        assertEquals(Rational.of(1), Rational.of(1).round());
        assertEquals(Rational.of(3), Rational.of(3).round());
        assertEquals(Rational.of(-1), Rational.of(-1).round());
        assertEquals(Rational.of(-17), Rational.of(-17).round());

        assertEquals(Rational.of(1), Rational.of(1, 2).round());
        assertEquals(Rational.of(0), Rational.of(1, 3).round());
        assertEquals(Rational.of(0), Rational.of(1, 5).round());
        assertEquals(Rational.of(1), Rational.of(2, 3).round());
        assertEquals(Rational.of(1), Rational.of(4, 5).round());
        assertEquals(Rational.of(1), Rational.of(7, 5).round());
        assertEquals(Rational.of(4), Rational.of(29, 7).round());
        assertEquals(Rational.of(4), Rational.of(99, 24).round());
        assertEquals(Rational.of(24), Rational.of(541, 23).round());

        assertEquals(Rational.of(-1), Rational.of(-1, 2).round());
        assertEquals(Rational.of(0), Rational.of(-1, 3).round());
        assertEquals(Rational.of(0), Rational.of(-1, 5).round());
        assertEquals(Rational.of(-1), Rational.of(-2, 3).round());
        assertEquals(Rational.of(-1), Rational.of(-4, 5).round());
        assertEquals(Rational.of(-1), Rational.of(-7, 5).round());
        assertEquals(Rational.of(-4), Rational.of(-29, 7).round());
        assertEquals(Rational.of(-4), Rational.of(-99, 24).round());
        assertEquals(Rational.of(-24), Rational.of(-541, 23).round());

        assertEquals(Rational.of(2), Rational.of(10, 5).round());
        assertEquals(Rational.of(2), Rational.of(11, 5).round());
        assertEquals(Rational.of(2), Rational.of(12, 5).round());
        assertEquals(Rational.of(3), Rational.of(13, 5).round());
        assertEquals(Rational.of(3), Rational.of(14, 5).round());
        assertEquals(Rational.of(3), Rational.of(15, 5).round());

        assertEquals(Rational.of(3), Rational.of(18, 6).round());
        assertEquals(Rational.of(3), Rational.of(19, 6).round());
        assertEquals(Rational.of(3), Rational.of(20, 6).round());
        assertEquals(Rational.of(4), Rational.of(21, 6).round());
        assertEquals(Rational.of(4), Rational.of(22, 6).round());
        assertEquals(Rational.of(4), Rational.of(23, 6).round());
        assertEquals(Rational.of(4), Rational.of(24, 6).round());

        assertEquals(Rational.of(-2), Rational.of(-10, 5).round());
        assertEquals(Rational.of(-2), Rational.of(-11, 5).round());
        assertEquals(Rational.of(-2), Rational.of(-12, 5).round());
        assertEquals(Rational.of(-3), Rational.of(-13, 5).round());
        assertEquals(Rational.of(-3), Rational.of(-14, 5).round());
        assertEquals(Rational.of(-3), Rational.of(-15, 5).round());

        assertEquals(Rational.of(-3), Rational.of(-18, 6).round());
        assertEquals(Rational.of(-3), Rational.of(-19, 6).round());
        assertEquals(Rational.of(-3), Rational.of(-20, 6).round());
        assertEquals(Rational.of(-4), Rational.of(-21, 6).round());
        assertEquals(Rational.of(-4), Rational.of(-22, 6).round());
        assertEquals(Rational.of(-4), Rational.of(-23, 6).round());
        assertEquals(Rational.of(-4), Rational.of(-24, 6).round());
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
    void testRandomWithDefaultRandomGenerator() {
        assertThrows(IllegalArgumentException.class, () -> Rational.random(0));
        assertThrows(IllegalArgumentException.class, () -> Rational.random(-1));
        assertThrows(IllegalArgumentException.class, () -> Rational.random(-17));

        testRandom(7, 4 << 7, null);
        testRandom(8, 4 << 8, null);
        testRandom(15, 4 << 15, null);
        testRandom(16, 4 << 16, null);
        testRandom(63, 1 << 12, null);
        testRandom(64, 1 << 12, null);
        testRandom(512, 1 << 10, null);
    }

    @Test
    void testRandomWithRandomGenerator() {
        assertThrows(IllegalArgumentException.class, () -> Rational.random(0));
        assertThrows(IllegalArgumentException.class, () -> Rational.random(-1));
        assertThrows(IllegalArgumentException.class, () -> Rational.random(-17));
        assertThrows(IllegalArgumentException.class, () -> Rational.random(7, null));
        assertThrows(IllegalArgumentException.class, () -> Rational.random(0, null));

        Random random = new SecureRandom();

        testRandom(7, 4 << 7, random);
        testRandom(8, 4 << 8, random);
        testRandom(15, 4 << 15, random);
        testRandom(16, 4 << 16, random);
        testRandom(63, 1 << 12, random);
        testRandom(64, 1 << 12, random);
        testRandom(512, 1 << 10, random);
    }

    private void testRandom(int bits, int iterations, Random random) {
        BigInteger maxDenominator = BigInteger.ONE.shiftLeft(bits);
        for (int i = 0; i < iterations; i++) {
            Rational rational = random != null ? Rational.random(bits, random) : Rational.random(bits);
            assertTrue(rational.compareTo(Rational.ZERO) >= 0);
            assertTrue(rational.compareTo(Rational.ONE) < 0);
            assertTrue(rational.denominator().compareTo(maxDenominator) <= 0);
            // check if denominator is a power of 2
            assertEquals(BigInteger.ZERO, rational.denominator().and(rational.denominator().subtract(BigInteger.ONE)));
        }
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
    void testToContinuedFraction() {
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(0) }, Rational.of(0).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(1) }, Rational.of(1).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(-1) }, Rational.of(-1).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(7) }, Rational.of(7).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(-7) }, Rational.of(-7).toContinuedFraction());

        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(0), BigInteger.valueOf(2) }, Rational.of(1, 2).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(-1), BigInteger.valueOf(2) }, Rational.of(-1, 2).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(0), BigInteger.valueOf(3) }, Rational.of(1, 3).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(-1), BigInteger.valueOf(1), BigInteger.valueOf(2) },
                Rational.of(-1, 3).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(0), BigInteger.valueOf(4) }, Rational.of(1, 4).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(-1), BigInteger.valueOf(1), BigInteger.valueOf(3) },
                Rational.of(-1, 4).toContinuedFraction());

        assertArrayEquals(new BigInteger[] { BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE,
                BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE,
                BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE,
                BigInteger.valueOf(2) }, Rational.of(75025, 46368).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(-2), BigInteger.valueOf(2), BigInteger.valueOf(1), BigInteger.ONE, BigInteger.ONE,
                BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE,
                BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigInteger.ONE,
                BigInteger.valueOf(2), }, Rational.of(-75025, 46368).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(12), BigInteger.valueOf(4) },
                Rational.of(649, 200).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(-4), BigInteger.valueOf(1), BigInteger.valueOf(3), BigInteger.valueOf(12),
                BigInteger.valueOf(4) }, Rational.of(-649, 200).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.ZERO, BigInteger.valueOf(8), BigInteger.ONE, BigInteger.ONE, BigInteger.valueOf(4) },
                Rational.of(9, 77).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(-1), BigInteger.valueOf(1), BigInteger.valueOf(7), BigInteger.ONE,
                BigInteger.valueOf(1), BigInteger.valueOf(4) }, Rational.of(-9, 77).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.ZERO, BigInteger.valueOf(3), BigInteger.ONE, BigInteger.valueOf(242) },
                Rational.of(243, 971).toContinuedFraction());
        assertArrayEquals(new BigInteger[] { BigInteger.valueOf(-1), BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(1),
                BigInteger.valueOf(242) }, Rational.of(-243, 971).toContinuedFraction());
    }

    @Test
    void testOfContinuedFraction() {
        assertThrows(IllegalArgumentException.class, () -> Rational.ofContinuedFraction(new BigInteger[0]));

        assertEquals(Rational.of(0), Rational.ofContinuedFraction(Rational.of(0).toContinuedFraction()));
        assertEquals(Rational.of(1), Rational.ofContinuedFraction(Rational.of(1).toContinuedFraction()));
        assertEquals(Rational.of(-1), Rational.ofContinuedFraction(Rational.of(-1).toContinuedFraction()));
        assertEquals(Rational.of(17), Rational.ofContinuedFraction(Rational.of(17).toContinuedFraction()));
        assertEquals(Rational.of(-17), Rational.ofContinuedFraction(Rational.of(-17).toContinuedFraction()));

        assertEquals(Rational.of(1, 2), Rational.ofContinuedFraction(Rational.of(1, 2).toContinuedFraction()));
        assertEquals(Rational.of(-1, 2), Rational.ofContinuedFraction(Rational.of(-1, 2).toContinuedFraction()));
        assertEquals(Rational.of(1, 3), Rational.ofContinuedFraction(Rational.of(1, 3).toContinuedFraction()));
        assertEquals(Rational.of(-1, 3), Rational.ofContinuedFraction(Rational.of(-1, 3).toContinuedFraction()));
        assertEquals(Rational.of(1, 4), Rational.ofContinuedFraction(Rational.of(1, 4).toContinuedFraction()));
        assertEquals(Rational.of(-1, 4), Rational.ofContinuedFraction(Rational.of(-1, 4).toContinuedFraction()));

        assertEquals(Rational.of(75025, 46368), Rational.ofContinuedFraction(Rational.of(75025, 46368).toContinuedFraction()));
        assertEquals(Rational.of(-75025, 46368), Rational.ofContinuedFraction(Rational.of(-75025, 46368).toContinuedFraction()));
        assertEquals(Rational.of(133, 29), Rational.ofContinuedFraction(Rational.of(133, 29).toContinuedFraction()));
        assertEquals(Rational.of(-133, 29), Rational.ofContinuedFraction(Rational.of(-133, 29).toContinuedFraction()));
        assertEquals(Rational.of(5, 133), Rational.ofContinuedFraction(Rational.of(5, 133).toContinuedFraction()));
        assertEquals(Rational.of(-5, 133), Rational.ofContinuedFraction(Rational.of(-5, 133).toContinuedFraction()));
        assertEquals(Rational.of(3, 5), Rational.ofContinuedFraction(Rational.of(3, 5).toContinuedFraction()));
        assertEquals(Rational.of(-3, 5), Rational.ofContinuedFraction(Rational.of(-3, 5).toContinuedFraction()));
        assertEquals(Rational.of(4, 3), Rational.ofContinuedFraction(Rational.of(4, 3).toContinuedFraction()));
        assertEquals(Rational.of(-4, 3), Rational.ofContinuedFraction(Rational.of(-4, 3).toContinuedFraction()));
        assertEquals(Rational.of(19, 7), Rational.ofContinuedFraction(Rational.of(19, 7).toContinuedFraction()));
        assertEquals(Rational.of(-19, 7), Rational.ofContinuedFraction(Rational.of(-19, 7).toContinuedFraction()));
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
            sum = sum.add(Rational.ofReciprocal(i));
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
            sum = sum.add(Rational.ofReciprocal((i % 2) > 0 ? i : -i));
        }

        assertEquals(Rational.of(
                "4937790215303904379625531938825779946100713125770473348897738659941095286866450943449698645388475442704066094104875369365939573941321907700818330906904692080097434687930193989481947211915390582175815541421224025307798265496347982662631751380933793540471517859473601783917361384214703881079292192008187313164050082034643619238257085294408414212863236104619096962528583819754790264673829998544542831721722741539798798387356122429364267/7128865274665093053166384155714272920668358861885893040452001991154324087581111499476444151913871586911717817019575256512980264067621009251465871004305131072686268143200196609974862745937188343705015434452523739745298963145674982128236956232823794011068809262317708861979540791247754558049326475737829923352751796735248042463638051137034331214781746850878453485678021888075373249921995672056932029099390891687487672697950931603520000"),
                sum);
    }

    @Test
    void testSqrt2() {
        // approximate sqrt(2) starting from 1 using 10 Newton-Raphson iterations

        // compute square root of this number
        Rational sqrt = Rational.of(2);

        // start
        Rational x = Rational.ONE;

        for (int i = 0; i < 10; i++) {
            x = x.add(sqrt.divide(x)).halve();
        }

        assertEquals(Rational.of(
                "45842869094724092282256664559525216692173091162526497018856817207453767127095354052418072639857700888691134361639497243771528176716782876457748796945284249594870200383920805322146999777923783319507727283850422831309915607777814766948159289963355518294865659883896712136501856004613518112550490551045680741845733583765205748593300762225277706242970719821414680060203467479543923750220952764417/32415803605926610717906209990215925889160576452720547724429585547651493040781333832362975891811042370960101005436793459936444123023843707180863927766463725709098731479142615931281199106042945274354413006295825020803450451201071934075872286636802447240751072096022370680513420925571815159954608073162324835009657121509219599603167122882578517323407726312472428238351392267283960361495336307712"),
                x);
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

    @Test
    void testPhi2() {
        // approximate phi (the Golden Ratio) via Newton-Raphson iterations by solving x^2 - x - 1 = 0

        Rational x = Rational.ONE;

        for (int i = 0; i < 10; i++) {
            x = x.subtract(x.square().subtract(x).subtract(Rational.ONE).divide(x.redouble().subtract(Rational.ONE)));
        }

        assertEquals(Rational.of(
                "7291993184377412737043195648396979558721167948342308637716205818587400148912186579874409368754354848994831816250311893410648104792440789475340471377366852420526027975140687031196633477605718294523235826853392138525/4506699633677819813104383235728886049367860596218604830803023149600030645708721396248792609141030396244873266580345011219530209367425581019871067646094200262285202346655868899711089246778413354004103631553925405243"),
                x);
    }

    @Test
    void testPi() {
        // Euler's, 1.2 digits per iteration
        // arccot(2) + arccot(3)
        // assertEquals(PI_1000, testPi(829, 1000, 1, 2, 1, 3));

        // Hermann's, 1.2 digits per iteration
        // 2*arccot(2) - arccot(7)
        // assertEquals(PI_1000, testPi(829, 1000, 2, 2, -1, 7));

        // Hutton's/Vega's, 1.9 digits per iteration
        // 2*arccot(3) + arccot(7)
        // assertEquals(PI_1000, testPi(523, 1000, 2, 3, 1, 7));

        // Machin's, 2.8 digits per iteration
        // 4*arccot(5) - arccot(239)
        // assertEquals(PI_1000, testPi(357, 1000, 4, 5, -1, 239));

        // Kikuo Takano, 6.8 digits per iteration
        // 12*arccot(49) + 32*arccot(57) -5*arccot(239) + 12*arccot(110443)
        assertEquals(PI_1000, testPi(148, 1000, 12, 49, 32, 57, -5, 239, 12, 110443));

        // F. C. M. Størmer, 7 digits per iteration
        // 44*arccot(57) + 7*arccot(239) -12*arccot(682) + 24*arccot(12943)
        assertEquals(PI_1000, testPi(143, 1000, 44, 57, 7, 239, -12, 682, 24, 12943));

        // Hwang Chien-Lih, 9.5 digits per iteration
        // 183*arccot(239) + 32*arccot(1023) - 68*arccot(5832) + 12*arccot(110443) - 12*arccot(4841182) - 100*arccot(6826318)
        assertEquals(PI_1000, testPi(105, 1000, 183, 239, 32, 1023, -68, 5832, 12, 110443, -12, 4841182, -100, 6826318));

        // Uwe Arndt, 22.2 digits per iteration
        assertEquals(PI_1000, testPi(45, 1000, 36462, 390112, 135908, 485298, 274509, 683982, -39581, 1984933, 178477, 2478328, -114569, 3449051,
                -146571, 18975991, 61914, 22709274, -69044, 24208144, -89431, 201229582, -43938, 2189376182L));

        // Hwang Chien-Lih, 10.9 digits per iteration
        assertEquals(PI_1000, testPi(53, 1000, 36462, 51387, 26522, 485298, 19275, 683982, -3119, 1984933, -3833, 2478328, -5183, 3449051, -37185,
                18975991, -11010, 22709274, 3880, 24208144, -16507, 201229582, -7476, 2189376182L));

        // unknown, 6.2 digits per iteration
        // 22*arctan(24478/873121) + 17*arctan(685601/69049993)
        // assertEquals(PI_1000, testPiRational(161, 1000, 22, 24478, 873121, 17, 685601, 69049993));

        // unknown, 9.2 digits per iteration
        // 44*arctan(74684/14967113) + 139*arctan(1/239) -12*arctan(20138/15351991)
        assertEquals(PI_1000, testPiRational(109, 1000, 44, 74684, 14967113, 139, 1, 239, -12, 20138, 15351991));

    }

    /**
     * Calculate Pi by summing up integer multiples of unit fraction arctangents.
     *
     * @param iterations the number of iterations
     * @param digits the number of decimal digits to use
     * @param numbers pairs (a,b) making up each summand of the type a*arctan(1/b) or, equivalently, a*arccot(b)
     *
     * @return the result
     */
    private String testPi(int iterations, int digits, long... numbers) {
        int n = numbers.length / 2;

        Rational number = Rational.of(1);

        Rational[] coefficients = new Rational[n];
        Rational[] reciprocals = new Rational[n];
        Rational[] squares = new Rational[n];
        Rational[] powers = new Rational[n];
        Rational[] sums = new Rational[n];

        double x = 0.0d;

        for (int i = 0; i < n; i++) {
            coefficients[i] = Rational.of(numbers[i * 2]);
            reciprocals[i] = Rational.ofReciprocal(numbers[i * 2 + 1]);
            if (i > 0) {
                if (numbers[i * 2] > 0) {
                    // System.out.print("+");
                    if (numbers[i * 2] != 1) {
                        // System.out.print(numbers[i * 2] + "*");
                    }
                } else if (numbers[i * 2] == -1) {
                    // System.out.print("-");
                } else {
                    // System.out.print(numbers[i * 2] + "*");
                }
            } else if (numbers[i * 2] != 1) {
                // System.out.print(numbers[i * 2] + "*");
            }
            // System.out.print("arccot(" + numbers[i * 2 + 1] + ")");
            squares[i] = reciprocals[i].square();
            powers[i] = reciprocals[i];
            sums[i] = Rational.ZERO;
            x += 1 / Math.log10(numbers[i * 2 + 1]);
        }

        // System.out.println(", Lehmer measure: " + x);

        for (int i = 0; i < iterations; i++) {
            for (int k = 0; k < n; k++) {
                sums[k] = sums[k].add(powers[k].divide(number));
                powers[k] = powers[k].multiply(squares[k]);
            }
            number = number.add(Rational.TWO);

            for (int k = 0; k < n; k++) {
                sums[k] = sums[k].subtract(powers[k].divide(number));
                powers[k] = powers[k].multiply(squares[k]);
            }
            number = number.add(Rational.TWO);
        }

        Rational sum = Rational.ZERO;
        for (int i = 0; i < n; i++) {
            sum = sum.add(coefficients[i].multiply(sums[i]));
        }

        return sum.multiply(Rational.of(4)).toDecimal(digits, RoundingMode.DOWN).toString();
    }

    /**
     * Calculate Pi by summing up integer multiples of arctangents of rationals.
     *
     * @param iterations the number of iterations
     * @param digits the number of decimal digits to use
     * @param numbers triples (a,b,c) making up each summand of the type a*arctan(b/c)
     *
     * @return the result
     */
    private String testPiRational(int iterations, int digits, long... numbers) {
        int n = numbers.length / 3;

        Rational number = Rational.of(1);

        Rational[] coefficients = new Rational[n];
        Rational[] reciprocals = new Rational[n];
        Rational[] squares = new Rational[n];
        Rational[] powers = new Rational[n];
        Rational[] sums = new Rational[n];

        double x = 0.0d;

        for (int i = 0; i < n; i++) {
            coefficients[i] = Rational.of(numbers[i * 3]);
            reciprocals[i] = Rational.of(numbers[i * 3 + 1], numbers[i * 3 + 2]);
            if (i > 0) {
                if (numbers[i * 3] > 0) {
                    // System.out.print("+");
                    if (numbers[i * 3] != 1) {
                        // System.out.print(numbers[i * 3] + "*");
                    }
                } else if (numbers[i * 3] == -1) {
                    // System.out.print("-");
                } else {
                    // System.out.print(numbers[i * 3] + "*");
                }
            } else if (numbers[i * 3] != 1) {
                // System.out.print(numbers[i * 3] + "*");
            }
            // System.out.print("arctan(" + numbers[i * 3 + 1] + "/" + numbers[i * 3 + 2] + ")");
            squares[i] = reciprocals[i].square();
            powers[i] = reciprocals[i];
            sums[i] = Rational.ZERO;
            x += 1 / Math.log10(numbers[i * 3 + 2] / numbers[i * 3 + 1]);
        }

        // System.out.println(", Lehmer measure: " + x);

        for (int i = 0; i < iterations; i++) {
            for (int k = 0; k < n; k++) {
                sums[k] = sums[k].add(powers[k].divide(number));
                powers[k] = powers[k].multiply(squares[k]);
            }
            number = number.add(Rational.TWO);

            for (int k = 0; k < n; k++) {
                sums[k] = sums[k].subtract(powers[k].divide(number));
                powers[k] = powers[k].multiply(squares[k]);
            }
            number = number.add(Rational.TWO);
        }

        Rational sum = Rational.ZERO;
        for (int i = 0; i < n; i++) {
            sum = sum.add(coefficients[i].multiply(sums[i]));
        }

        return sum.multiply(Rational.of(4)).toDecimal(digits, RoundingMode.DOWN).toString();
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
