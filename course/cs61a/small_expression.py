"""Find the shortest *small expression*

Three functions:
    - f(x): subtracts one from an integer x
    - g(x): doubles an integer x
    - h(x, y): concatenates the digits of two positive integers x and y. For example, h(789, 12) evaluates 78912.

**Definition**: A small expression is a call expression that contains only f, g, h, the number 5, and parentheses. All of these can be repeated. For example, `h(g(5), f(f(5)))` is a small expression that evaluates to 103.

What's the shortest small expression you can find that evaluates to 2024?
"""

