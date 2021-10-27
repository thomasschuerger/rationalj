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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class RationalTest {

	@Test
	void testConstructor() {
		assertThrows(IllegalArgumentException.class, () -> new Rational(0, 0));
		assertThrows(IllegalArgumentException.class, () -> new Rational(1, 0));
		assertThrows(IllegalArgumentException.class, () -> new Rational(-1, 0));
		assertThrows(IllegalArgumentException.class, () -> new Rational(5, 0));
	}

	@Test
	void testNormalization() {
		assertEquals(new Rational(0, 1), new Rational(0, 4431));
		assertEquals(new Rational(1, 1), new Rational(BigInteger.ONE, BigInteger.ONE));
		assertEquals(new Rational(1, 1), new Rational(2, 2));
		assertEquals(new Rational(-5, -5), new Rational(5, 5));
		assertEquals(new Rational(-5, -5), new Rational(-10, -10));
		assertEquals(new Rational(4389327, 37800), new Rational(4389327 / 9, 37800 / 9));
		assertEquals(new Rational(-4389327, 37800), new Rational(4389327 / 9, -37800 / 9));
		assertEquals(new Rational(-4389327, 37800), new Rational(-4389327 / 9, 37800 / 9));
		assertEquals(new Rational(-4389327, -37800), new Rational(4389327 / 9, 37800 / 9));
	}

}
