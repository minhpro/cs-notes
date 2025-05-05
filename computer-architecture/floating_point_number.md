# Floating-Point Numbers (FP)

Going beyond signed and unsigned integers, programming languages support numbers with fractions, called `real` in mathematics.

> 3.14159265... (pi)
> 2.71828... (e)
> 0.000000000052917710 or 5.2917710 x 10^(-11) (Bohr radius)
> 3,155,760,000 or 3.15576 × 10^9 (seconds in a typical century)

With too small or too big numbers (like the two last cases), we cannot store them in a limitation bits (32 bits or 64 bits). The alternative notation for these numbers is called **scientific notation**, which has a single digit to the left of the decimal point. A number in scientific notation that has no leading 0s is called **normalized**.
- 1.0 x 10^(-9) is in normalized scientific notation.
- 0.1 x 10^(-8) and 10.0 x 10^(-10) are not.

> 1.640625   x   10 ^ (-1)

- significand: 1.640625 (normalized)
- radix: base 10
- exponent: -1

Computer arithmetic support scientific notation in binary representation.

IEEE 754 Floating Point Standard

> (+/-)1.xxx...x   x  2 ^ (yyy...y)

- xxx...x: fraction bits
- yyy...y: exponent bits 

Bit format:

```
----------------------------------
|s|  exponent   |  fraction   |
----------------------------------
```

- s: a single sign bit

Single precision floating-point (32bit- fp32, **float**)
- 8 bits exponent (biased exponent: E)
- 23 bits fraction

Floating-point formula:

> (-1)^s x (1+fraction)two x 2^(E-127)
> (-1)^s x (1 + b1x2^-1 + b2x2^-2 + ...) x 2^(E-127)

Example:

```
---------------------------------------------
|1| 1000 0001 | 111 0000 0000 0000 0000 0000 |
---------------------------------------------
```

```
= (-1)^1 x (1 + .111)two x 2^(129-127)
= (-1) x (1.111)two x 2^2
= -111.1two
= -7.5ten
```

# Step Size

What is the next consecutive number after **x**?

```
---------------------------------------------
|1| 1000 0001 | 111 0000 0000 0000 0000 0000 |  x
---------------------------------------------
                                           +1
---------------------------------------------
|1| 1000 0001 | 111 0000 0000 0000 0000 0001 |  next number after x
---------------------------------------------                                   
```

> x + ((.0...01)two x 2^(129-127))
> x + ( 2^(-23) x 2^2)
> x + 2^(-21)

We cannot use the floating-point to represent all **real** numbers in a range:
- **Step size** is the spacing between consecutive floats with a given exponent.
- Bigger exponents -> bigger step size.
- Smaller exponents -> smaller step size.

# Special numbers

| Biased exponent | Fraction field | Number |
|-----------------|----------------|--------|
| 0               | all zeros      | +/-0   |
| 0               | nonzero        | Denormalized numbers      |
| 1 - 254         | anything       | Normalized floating point |
| 255             | all zeros      | +/-∞   |
| 255             | nonzero        | NaNs   |

# Doudle precision floating-point

Doudle precision floating-point (64bit, fp64, **doule**):
- 11 bits exponent (biased exponent: E)
- 52 bits fraction

Formula:

> (-1)^s x (1 + fraction) x 2^(E-1023)

# Fixed-Point Numbers

`Fixed-point notation` is another way to represent the fraction numbers, it has an implied `binary point` between the integer and fraction bits, analogous to the decimal point between the integer and frac- tion digits of an ordinary decimal number.

> 10101 = 10.101 = (1x2^1 + 0x2^0) . (1x2^-1 + 0x2^-2 + 1x2^-3) = 2.625

Signed fixed-point numbers can use the two's complement notation.

In general, we use `Ua.b` to denote an unsigned fixed-point number with `a` integer bits and `b` fraction bits. `Qa.b` to denote signed (two's compliment) fixed-point number with `a` integer bits (including the sign bit) and `b` fraction bits.

Excercise: Compute 2.75 + (-0.625) using Q4.4 fixed-point numbers.

# Reading

- https://en.wikipedia.org/wiki/Floating-point_arithmetic
