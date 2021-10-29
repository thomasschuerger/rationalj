# RationalJ

RationalJ is a light-weight Java library for rational numbers. The library manages rationals using arbitrary integer precision for numerators and denominators. RationalJ internally uses `BigInteger`, therefore the maximum magnitude for numerators and denominators is 2^2147483647-1.

Rational numbers are implemented by the class `Rational`. Like for `Integer`, `BigInteger` and `BigDecimal`, instances of `Rational` are immutable. Operations on `Rational`s do not modify the `Rational`s but return `Rational`s or other types as results. `Rational` implements most of the arithmetic methods offered by `BigInteger`. It does not implement any of the bitwise operations that `BigInteger` offers.

`Rational`s are automatically normalized:

- numerators and denominators are always co-prime (fully reduced)
- denominators are always positive
- zero uses an numerator of 0 and a denominator of 1 

Therefore, two `Rational`s are equal if and only if their numerators are equal and their denominators are equal.

Rational offers no public constructors to allow minimizing the amount of instances created. `Rational`s are retrieved via `Rational.of(...)` or `Rational.valueOf(...)` instead.

Author: Thomas Schuerger (thomas@schuerger.com)

# License

RationalJ is licensed under the Apache License 2.0. See file [LICENSE](LICENSE) for details.

# Prerequisites

Java 8 or later is required.

# Dependencies

No external dependencies are required.

# Changelog

The changelog is maintained in the file [CHANGELOG.md](CHANGELOG.md).

# Usage

Arbitrary `Rational`s can be created as follows:

- `Rational.of(int numerator, int denominator)`
- `Rational.of(long numerator, long denominator)`
- `Rational.of(BigInteger numerator, BigInteger denominator)`
- `Rational.of(String string)` (using "`<numerator>/<denominator>`", e.g. "`-431/214`")

Integer `Rational`s can be created as follows:

- `Rational.of(int integer)`
- `Rational.of(long integer)`
- `Rational.of(BigInteger integer)`
- `Rational.of(String string)` (using "`<integer>`", e.g. "`247`")

# Operators

### Unary operators

- `negate() - Rational`
- `reciprocal() - Rational`
- `square() - Rational`
- `signum() - int`
- `abs() - Rational`

### Binary operators

- `add(Rational other) - Rational`
- `subtract(Rational other) - Rational`
- `multiply(Rational other) - Rational`
- `divide(Rational other) - Rational`
- `divideInteger(Rational other) - BigInteger`
- `divideIntegerAndRemainder(Rational other) - {BigInteger, Rational}`
- `pow(int power) - Rational`
- `gcd(Rational other) - Rational`
- `min(Rational other) - Rational`
- `max(Rational other) - Rational`

# Build

Run

```bash
mvn clean install
```

# Integrate

## Maven

```xml
<dependency>
    <groupId>com.schuerger.math</groupId>
    <artifactId>rationalj</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Gradle

```gradle
implementation 'com.schuerger.math:rationalj:1.0.0'
```

# Examples

## Approximate e

The following Java code calculates a rational approximation to Euler's number e, summing the first 100 terms of the usual infinite series:

```java
Rational sum = Rational.ZERO;
Rational factorial = Rational.ONE;

for (int i = 0; i < 100; i++) {
    sum = sum.add(factorial);
    factorial = factorial.divide(Rational.of(i + 1));
}

System.out.println(sum + " = " + sum.toDecimal());
```

Output:

```
31710869445015912176908843526535027555643447320787267779096898248431156738548305814867560678144006224158425966541000436701189187481211772088720561290395499/11665776930493019085212404857033337561339496033047702683574120486902199999153739451117682997019564785781712240103402969781398151364608000000000000000000000 = 2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274
```

## Determine Egyptian fraction of a rational number

```java
// every positive rational number can be expressed as a finite sum of distinct unit fractions
// (fractions of the form 1/n); for example: 9/11 = 1/2 + 1/4 + 1/15 + 1/660

// the following will find a sequence of distinct unit fractions that sums to a positive
// target rational

// we do this by keeping a sum of unit fractions and in each iteration we find the largest
// unused unit fraction that does not let the sum exceed the target

Rational target = Rational.of(47, 29);
Rational sum = Rational.ZERO;
Rational diffToTarget = target;
BigInteger k = BigInteger.ZERO;

do {
    // increase k to the smallest k' > k such that sum + 1/k' <= target
    Rational x = diffToTarget.reciprocal();
    BigInteger kprime = x.isInteger() ? x.toInteger() : x.toInteger().add(BigInteger.ONE);
    k = kprime.compareTo(k) <= 0 ? k.add(BigInteger.ONE) : kprime;

    sum = sum.add(Rational.of(BigInteger.ONE, k));
    diffToTarget = target.subtract(sum);

    System.out.printf("k: %6d  sum: %32.30f %s\n", k, sum.toDecimal(), sum);
} while (diffToTarget.signum() > 0);
```

Output:

```
k:      1  sum: 1.000000000000000000000000000000 1
k:      2  sum: 1.500000000000000000000000000000 3/2
k:      9  sum: 1.611111111111111111111111111111 29/18
k:    105  sum: 1.620634920634920634920634920635 1021/630
k:  18270  sum: 1.620689655172413793103448275862 47/29
```

Now we know that 47/29 = 1 + 1/2 + 1/9 + 1/105 + 1/18270.
