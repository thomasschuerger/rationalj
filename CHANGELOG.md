# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- `Rational.ofReciprocal(int denominator) - Rational`
- `Rational.ofReciprocal(long denominator) - Rational`
- `Rational.ofReciprocal(BigInteger denominator) - Rational`

## [1.1.1] - 2021-11-15

Corrected Maven version.

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
