E1. Algorithm analysis

Providing the runtime-complexity and space-complexity of the following algorithms:

Algorithm 1:

```Java
int sumOfSquares(int n) {
    int sum = 0;
    for (int i = 1; i <= n; i++) {
        sum += i*i;
    }
    return sum;
}
```

Runtime complexity?
Space complexity?

Algorithm 2:

```Java
int otherSumOfSquares(int n) {
    int sum = 0;
    for (int i = 1; i <= n; i = i*2) {
        sum += i*i;
    }
    return sum;
}
```

Runtime complexity?
Space complexity?

E2. Recursion

What advantanges and disadvantanges of recursion algorithms? Could you take an example?

E3. Could you compare linked lists and array lists? Which problems should we apply linked lists or array lists?

E4. Java programming

What purpose of the `static` keyword?

Similary question with `final` keyword?

What differents between primitive types and object types?

int square(int n) {
    n = n * n;
}

int x = 10;

square(x)

System.out.println(x)? x = ?

How to concat 100 integers from 1 to 100 into a string?

E5. Coding - Merge Strings Alternately

You are given two strings `word1` and `word2`. Merge the strings by adding letters in alternating order, starting with `word1`. If a string is longer than the other, append the additional letters onto the end of the merged string.

*Return the merged string*.

Example 1:

```
Input: word1 = "abc", word2 = "pqr"
Output: "apbqcr"
Explanation: The merged string will be merged as so:
word1:  a   b   c
word2:    p   q   r
merged: a p b q c r
```

Example 2:

```
Input: word1 = "ab", word2 = "pqrs"
Output: "apbqrs"
Explanation: Notice that as word2 is longer, "rs" is appended to the end.
word1:  a   b 
word2:    p   q   r   s
merged: a p b q   r   s
```

Example 3:

```
Input: word1 = "abcd", word2 = "pq"
Output: "apbqcd"
Explanation: Notice that as word1 is longer, "cd" is appended to the end.
word1:  a   b   c   d
word2:    p   q 
merged: a p b q c   d
```

E5.2

Cho một mảng các số nguyên nums và số nguyên k, tìm 2 số trong mảng nums có tổng bằng k, nếu không tìm thấy thì in ra là không tìm thấy

Ví dụ Input: nums = [2,7,11,15], target = 9
Thì kết quả là tìm thấy là 2 số: 2 và 7

E6 Database

X(a, b)   Y(b, c)
1, 2       2, 3
1, 4       5, 6
2, 3       3, 7

X inner join Y on b


X left join Y on b



