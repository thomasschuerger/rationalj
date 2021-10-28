# RationalJ

RationalJ is a light-weight Java library for rational numbers. The library manages rationals using arbitrary integer precision for enumerators and denominators. RationalJ internally uses `BigInteger`, therefore the maximum magnitude for numerators and denominators is 2^2147483647.

Rational numbers are implemented by the class `Rational`. Like for `Integer`, `BigInteger` and `BigDecimal`, instances of `Rational` are immutable. Operations on Rationals do not modify the Rationals but return Rationals or other types as results. `Rational` implements most of the arithmetic methods offered by `BigInteger`. It does not implement any of the bitwise operations that `BigInteger` offers.

Rationals are automatically normalized:

- numerators and denominators are always co-prime (fully reduced)
- denominators are always positive
- zero uses an enumerator of 0 and a denominator of 1 

Therefore, two Rationals are equal if and only if their enumerators are equal and their denominators are equal.

Rational offers no public constructors to allow minimizing the amount of instances created. Rationals are retrieved via `Rational.of(...)` or `Rational.valueOf(...)` instead.

Author: Thomas Schuerger (thomas@schuerger.com)

# License

RationalJ is licensed under the Apache License 2.0. See file [LICENSE](LICENSE) for details.

# Usage

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
- `gcd(Rational other) - Rational`
- `max(Rational other) - Rational`
- `min(Rational other) - Rational`
- `pow(int power) - Rational`

# Build

Run

```bash
mvn clean install
```

# Integrate

## Maven

```xml
<dependency>
    <groupId>com.schuerger.util</groupId>
    <artifactId>rationalj</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Gradle

```gradle
implementation 'com.schuerger.util:rationalj:1.0.0'
```

# Example

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