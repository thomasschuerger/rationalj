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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Implements an immutable arbitrary-scale rational number based on BigInteger. The rational numbers are always normalized, which means that they are
 * stored completely reduced, the denominator is always positive (which means the sign is in the numerator only), and the number 0 always has
 * denominator 1. The API is similar to the one from BigInteger.
 *
 * @author Thomas Schuerger
 */

public class Rational extends Number implements Comparable<Rational> {
	/** Ten. */
	public static Rational TEN = new Rational(BigInteger.TEN, BigInteger.ONE);

	/** Two. */
	public static Rational TWO = new Rational(2, 1);

	/** One. */
	public static Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);

	/** One half. */
	public static Rational ONE_HALF = new Rational(1, 2);

	/** Zero. */
	public static Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);

	/** Minus one half. */
	public static Rational MINUS_ONE_HALF = new Rational(-1, 2);

	/** Minus one. */
	public static Rational MINUS_ONE = new Rational(-1, 1);

	/** Minus two. */
	public static Rational MINUS_TWO = new Rational(-2, 1);

	/** Minus ten. */
	public static Rational MINUS_TEN = new Rational(-10, 1);

	/** The numerator. */
	private final BigInteger numerator;

	/** The denominator (always >= 1). */
	private BigInteger denominator;

	/** True iff this Rational is an integer. */
	private final boolean isInteger;

	/** True iff this Rational is zero. */
	private final boolean isZero;

	/** True iff this Rational is one. */
	private final boolean isOne;

	/**
	 * Creates a Rational from the given numerator and denominator.
	 *
	 * @param numerator   the numerator
	 * @param denominator the denominator
	 */
	public Rational(int numerator, int denominator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	/**
	 * Creates a Rational from the given numerator and denominator.
	 *
	 * @param numerator   the numerator
	 * @param denominator the denominator
	 */
	public Rational(BigInteger numerator, BigInteger denominator) {
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
			this.numerator = numerator.divide(gcd);
			this.denominator = denominator.divide(gcd);
		}

		if (this.denominator.equals(BigInteger.ONE)) {
			this.isInteger = true;
			if (numerator.equals(BigInteger.ONE)) {
				this.isOne = true;
				this.isZero = false;
			} else {
				// numerator can't be zero
				this.isOne = false;
				this.isZero = false;
			}
		} else {
			this.isInteger = false;
			this.isOne = false;
			this.isZero = false;
		}
	}

	/**
	 * Creates an integer Rational from the given integer.
	 *
	 * @param integer the integer
	 */
	public Rational(int integer) {
		this(integer, 1);
	}

	/**
	 * Creates an integer Rational from the given integer.
	 *
	 * @param integer the integer
	 */
	public Rational(BigInteger integer) {
		this(integer, BigInteger.ONE);
	}

	/**
	 * Returns true if this Rational is an integer, false otherwise.
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
	 * @return the sum of this and other
	 */
	public Rational add(Rational other) {
		if (isZero) {
			return other;
		} else if (other.isZero) {
			return this;
		} else if (isInteger && other.isInteger) {
			return new Rational(numerator.add(other.numerator));
		} else {
			return new Rational(numerator.multiply(other.denominator).add(denominator.multiply(other.numerator)),
					denominator.multiply(other.denominator));
		}
	}

	/**
	 * Returns a Rational that represents this Rational minus the given other Rational.
	 *
	 * @param other the other rational
	 * @return the difference of this and other
	 */
	public Rational subtract(Rational other) {
		if (isZero) {
			return other.negate();
		} else if (other.isZero) {
			return this;
		} else if (isInteger && other.isInteger) {
			return new Rational(numerator.subtract(other.numerator));
		} else {
			return new Rational(numerator.multiply(other.denominator).subtract(denominator.multiply(other.numerator)),
					denominator.multiply(other.denominator));
		}
	}

	/**
	 * Returns a Rational that represents this Rational multiplied by the given other Rational.
	 *
	 * @param other the other rational
	 * @return the product of this and other
	 */
	public Rational multiply(Rational other) {
		if (isZero || other.isZero) {
			return ZERO;
		} else if (isOne) {
			return other;
		} else if (other.isOne) {
			return this;
		} else {
			return new Rational(numerator.multiply(other.numerator), denominator.multiply(other.denominator));
		}
	}

	/**
	 * Returns a Rational that represents this Rational divided by the given other Rational.
	 *
	 * @param other the other rational (must not be 0)
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
			return new Rational(numerator.multiply(other.denominator), denominator.multiply(other.numerator));
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
	 * Returns the negation of this Rational.
	 *
	 * @return the negation
	 */
	public Rational negate() {
		if (isZero) {
			return this;
		} else {
			return new Rational(numerator.negate(), denominator);
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
		}

		return new Rational(denominator, numerator);
	}

	/**
	 * Returns this Rational to the given integer power. Not both of the Rational and the power must be 0.
	 *
	 * @param power the power
	 * @return the power'th power
	 */
	public Rational pow(int power) {
		if (power == 0) {
			if (isZero) {
				throw new IllegalArgumentException("0^0");
			}
			return ONE;
		} else if (power == 1) {
			return this;
		} else if (power == 2) {
			return multiply(this);
		} else if (power == -1) {
			return reciprocal();
		} else if (power < 0) {
			return new Rational(denominator.pow(-power), numerator.pow(-power));
		} else {
			return new Rational(numerator.pow(power), denominator.pow(power));
		}
	}

	@Override
	public String toString() {
		if (denominator.equals(BigInteger.ONE)) {
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
		return numerator.multiply(other.denominator).subtract(denominator.multiply(other.numerator)).signum();
	}
}
