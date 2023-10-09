# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.4.0] - 2023-10-09

### Enhanced

- `Rational.of(String string)` now also supports numbers with integer and fractional part, plus optionally a repeating fractional part, for example "123.456789" (representing 123456789/1000000) or "123.456_789" (representing 123.456789789789... = 41111111/333000).

## [1.3.0] - 2023-08-21

### Added

- `floor() - Rational`
- `ceil() - Rational`
- `round() - Rational`
- `Rational.random(int bits) - Rational`
- `Rational.random(int bits, Random random) - Rational`

## [1.2.0] - 2021-11-24

### Added

- `Rational.ofReciprocal(int denominator) - Rational`
- `Rational.ofReciprocal(long denominator) - Rational`
- `Rational.ofReciprocal(BigInteger denominator) - Rational`

### Fixed

 - `negate()` could deliver wrong result if the numerator was -1

## [1.1.1] - 2021-11-15

Corrected Maven version. Other than that, this version is identical to version 1.1.0.

## [1.1.0] - 2021-11-15

Version 1.1.0 has accidentally been released on Maven as version "1.1.0-RELEASE". Use version "1.1.1" instead.

### Added

- `redouble() - Rational`
- `halve() - Rational`
- `mod(Rational other) - Rational`
- `lcm(Rational other) - Rational`
- `isNegationOf(Rational other) - boolean`
- `isReciprocalOf(Rational other) - boolean`
- `toDecimal(int scale, RoundingMode roundingMode) - BigDecimal`
- `toContinuedFraction() - BigInteger[]`
- `Rational.ofContinuedFraction(BigInteger... integers) - Rational`


## [1.0.0] - 2021-10-29

Initial release.
