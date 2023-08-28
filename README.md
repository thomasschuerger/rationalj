# RationalJ

RationalJ is a lightweight Java library for rational numbers. It is lightweight because it has no external dependencies and as of now only consists of a single class.

The library manages rational numbers using arbitrary integer precision for numerators and denominators. RationalJ internally uses `BigInteger`, therefore the maximum magnitude for numerators and denominators is 2^2147483647-1, which is indeed *big*.

Rational numbers are implemented by the class `Rational`, which extends `Number`. Like for `Integer`, `BigInteger` and `BigDecimal` (and other number classes), instances of `Rational` are immutable. Thus, operations on `Rational`s do not modify them but return `Rational`s or other types (like `BigInteger` or `int`) as results. `Rational` implements most of the arithmetic methods offered by `BigInteger`. It does not implement any of the bitwise operations or prime number methods that `BigInteger` offers.

Upon creation, `Rational`s are automatically brought into canonical form:

- numerators and denominators are always made coprime (making the `Rational` fully reduced)
- if denominators are negative, the signs of the numerators and denominators each are switched
- zero uses a numerator of 0 and a denominator of 1 

Therefore, two `Rational`s are equal if and only if their numerators are equal and their denominators are equal. Working in canonical form makes sure that computations use smaller numbers and allows for certain optimizations.

`Rational` offers no public constructors to allow minimizing the amount of instances created by being able to return `Rational`s from a pool of constants. Instead, `Rational`s are retrieved via `Rational.of(...)`.

Issues can be reported [here](https://github.com/thomasschuerger/rationalj/issues).

Suggestions, ideas and feedback are welcome!

Author: Thomas Schuerger (thomas@schuerger.com)

# License

RationalJ is licensed under the Apache License 2.0. See file [LICENSE](LICENSE) for details.

# Prerequisites

Java 8 or later is required. The library would also work with Java 5, but the unit tests currently require Java 8.

Maven 3.6.0 or later is required for building the library.

# Dependencies

No external dependencies are required.

# Changelog

The changelog is maintained in the file [CHANGELOG.md](CHANGELOG.md).

# Usage

> :warning: This documentation is based on the latest released version. Unreleased versions may differ.

## Creating `Rational`s

Arbitrary `Rational`s can be created as follows:

- `Rational.of(int numerator, int denominator)`
- `Rational.of(long numerator, long denominator)`
- `Rational.of(BigInteger numerator, BigInteger denominator)`
- `Rational.of(String string)` (using "`<numerator>/<denominator>`", e.g. "`-431/214`")
- `Rational.ofReciprocal(int denominator) - Rational`
- `Rational.ofReciprocal(long denominator) - Rational`
- `Rational.ofReciprocal(BigInteger denominator) - Rational`
- `Rational.ofContinuedFraction(BigInteger... integers) - Rational`

Integer `Rational`s can be created as follows:

- `Rational.of(int integer)`
- `Rational.of(long integer)`
- `Rational.of(BigInteger integer)`
- `Rational.of(String string)` (using "`<integer>`", e.g. "`247`")

Random `Rational`s can be created as follows:

- `Rational.random(int bits)`
- `Rational.random(int bits, Random random)`

## Methods on `Rational`s

### Getters

- `numerator() - BigInteger`
- `denominator() - BigInteger`

### Unary operators

- `negate() - Rational`
- `reciprocal() - Rational`
- `redouble() - Rational`
- `halve() - Rational`
- `square() - Rational`
- `signum() - int`
- `abs() - Rational`
- `floor() - Rational`
- `ceil() - Rational`
- `round() - Rational`

### Binary operators

- `add(Rational other) - Rational`
- `subtract(Rational other) - Rational`
- `multiply(Rational other) - Rational`
- `divide(Rational other) - Rational`
- `divideInteger(Rational other) - BigInteger`
- `divideIntegerAndRemainder(Rational other) - Number[] {BigInteger, Rational}`
- `mod(Rational other) - Rational`
- `pow(int power) - Rational`
- `gcd(Rational other) - Rational`
- `lcm(Rational other) - Rational`
- `min(Rational other) - Rational`
- `max(Rational other) - Rational`

### Tests

- `isInteger() - boolean`
- `isNegationOf(Rational other) - boolean`
- `isReciprocalOf(Rational other) - boolean`

### Conversion

- `intValue() - int`
- `longValue() - long`
- `floatValue() - float`
- `doubleValue() - double`
- `toInteger() - BigInteger`
- `toDecimal() - BigDecimal`
- `toDecimal(int scale, RoundingMode roundingMode) - BigDecimal`
- `toContinuedFraction() - BigInteger[]`

# Git

## Clone the repository

Clone the repository via

```bash
git clone https://github.com/thomasschuerger/rationalj.git
```

## Versions

The master branch contains the newest version, which is usually the newest unreleased version. Versions are tagged with "v<version>", e.g. "v1.0.0".

Show all released versions:

```bash
git tag
```

Switch to a released version:

```bash
git checkout <tag>
```

Switch to the newest (unreleased) version:

```bash
git checkout master
```

# Build

RationalJ uses Maven.

Run

```bash
mvn clean install
```

to build RationalJ and install it into your local Maven repository.

Run

```bash
mvn clean install -DskipTests
```

to do the same, but skipping the compilation and execution of unit tests.

# Integrate

## Maven

```xml
<dependency>
    <groupId>com.schuerger.math</groupId>
    <artifactId>rationalj</artifactId>
    <version>1.3.0</version>
</dependency>
```

## Gradle

```gradle
implementation 'com.schuerger.math:rationalj:1.3.0'
```

# Examples

## Approximate e

The following Java code calculates a rational approximation to Euler's number e, summing the first 100 terms of the usual infinite series:

```java
Rational sum = Rational.ZERO;
Rational invFactorial = Rational.ONE;

for (int i = 0; i < 100; i++) {
    sum = sum.add(invFactorial);
    invFactorial = invFactorial.divide(Rational.of(i + 1));
}

System.out.println(sum + " = " + sum.toDecimal());
```

Output:

```
31710869445015912176908843526535027555643447320787267779096898248431156738548305814867560678144006224158425966541000436701189187481211772088720561290395499/11665776930493019085212404857033337561339496033047702683574120486902199999153739451117682997019564785781712240103402969781398151364608000000000000000000000 = 2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274
```

## Determine the Egyptian fraction expansion of a rational number

```java
// every positive rational number can be expressed as a finite sum of distinct unit fractions
// (fractions of the form 1/n); for example: 9/11 = 1/2 + 1/4 + 1/15 + 1/660

// the following will find a sequence of distinct unit fractions that sums to a positive
// target rational

// we do this by keeping a sum of unit fractions and in each iteration we find the largest
// unused unit fraction that does not let the sum exceed the target; this must eventually
// end, but the denominators can grow very big when close to the target

// naturally, the first x summands are from the partial sum of the Harmonic series,
// where x is floor(e^(target-gamma)-1/2) with gamma being the Euler-Mascheroni constant

Rational target = Rational.of(157, 145);
Rational diffToTarget = target;
Rational sum = Rational.ZERO;
BigInteger k = BigInteger.ZERO;

do {
    // increase k to the smallest k' > k such that sum + 1/k' <= target, but at least by 1
    Rational x = diffToTarget.reciprocal();
    BigInteger kprime = x.isInteger() ? x.toInteger() : x.toInteger().add(BigInteger.ONE);
    k = kprime.compareTo(k) <= 0 ? k.add(BigInteger.ONE) : kprime;

    sum = sum.add(Rational.ofReciprocal(k));
    diffToTarget = target.subtract(sum);

    System.out.printf("k: %22d  sum: %32.30f %s%n", k, sum.toDecimal(), sum);
} while (diffToTarget.signum() > 0);
```

Output:

```
k:                      1  sum: 1.000000000000000000000000000000 1
k:                     13  sum: 1.076923076923076923076923076923 14/13
k:                    172  sum: 1.082737030411449016100178890877 2421/2236
k:                  46318  sum: 1.082758620290113898003542593973 56069057/51783524
k:             2502870327  sum: 1.082758620689655172360582026803 140333579079955163/129607445647092348
k:   18793079618828390460  sum: 1.082758620689655172413793103448 157/145
```

Now we know that 157/145 = 1/1 + 1/13 + 1/172 + 1/46318 + 1/2502870327 + 1/18793079618828390460.

# FAQ
## Shouldn't the class be called `BigRational` instead of `Rational`?

`BigRational` would indeed have been a better name to match the other `Big*` classes, but only if there were already a class called `Rational` working with smaller number types. Since there isn't, the shorter name was chosen.
