# RationalJ

RationalJ is a light-weight Java library for rational numbers. The library manages rationals using arbitrary integer precision for enumerators and denominators. RationalJ internally uses BigInteger, therefore the magnitude for numerators and denominators is 2^

Author: Thomas Schuerger (thomas@schuerger.com)

# License

RationalJ is licensed under the Apache License 2.0. See file [LICENSE](LICENSE) for details.

# Usage

Rational numbers are implemented in a class called `Rational`. Like Integer, BigInteger and BigDecimal, instances of Rational are immutable. Operations on Rationals do not modify the Rationals but return Rationals as results. `Rational` implements most operations that are similar to the methods of `BigInteger`. It does not implement any of the bit-wise operations that `BigInteger` offers.

Rationals can be created as follows:

- `Rational.of(int numerator, int denominator)`
- `Rational.of(long numerator, long denominator)`
- `Rational.of(BigInteger numerator, BigInteger denominator)`
- `Rational.of(String string)` (using "`<numerator>/<denominator>`", e.g. "`-431/214`")

Integer Rationals are available via

- `Rational.of(int integer)`
- `Rational.of(long integer)`
- `Rational.of(BigInteger integer)`
- `Rational.of(String string)` (using "`<integer>`", e.g. "`247`")

# Operators

# Unary operators

- `negate()`
- `reciprocal()`
- `square()`
- `signum()`
- `abs()`

# Binary operators

- `add(Rational other)`
- `subtract(Rational other)`
- `multiply(Rational other)`
- `divide(Rational other)`
- `divideInteger(Rational other)`
- `divideIntegerAndRemainder(Rational other)`
- `gcd(Rational other)`
- `max(Rational other)`
- `min(Rational other)`
- `pow(int power)`
