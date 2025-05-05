keyword: C Programming

# C Basic Types

Basic types:
- **int**: Integer numbers, at least 16 bits, can be larger, 0, 23, -34, 0x7262.
- **unsigned int**: Unsigned integers (non-negative).
- **float**: Floating point decimal.
- **double**: Equal or higher precision floating point.
- **char**:  Single character, 'a', 'A'.'\n'.
- **long**: Longer integer, size >= sizeof(int), at least 32b.
- **long long**: Even longer int, size >= sizeof(long), at least 64b

The C standard does not define the absolute size of integer types, other than char!, but guarantees relative sizes:

> sizeof(long long) >= sizeof(long) >= sizeof(int) >= sizeof(short)

Every integer types have an unsigned version, `unsigned int`, `unsigned short`, `unsigned long`, `unsigned long long`.

Encourage use `intN_t` and `uintN_t`

```c
#include <stdint.h>

uint16_t x = 34;
```

Boolean is **not** a primitive type in C! Instead:
- false
    - 0 (integer, i.e., all bits are 0)
    - NULL (pointer)
- true
    - Everything else!

- Nowadays: true and false are provided by `stdbool.h`

# Consts, Enums, #define in C

- Constant, **const**, is assigned a typed value once in the declaration.
    - Value can't change during entire execution of program

    ```c
    const int days_in_week = 7;
    ```
    - You can have a constant version of any of the standard C variable types.
- **#define PI (3.14159)** is a C Preprocessor Macro.
    - Prior to compilation, preprocess by string replacement.
    - Replace all **PI** with **(3.14159)** -> In effect, makes PI a "constant".
- Enums: a group of related integer constants, e.g.,
    
    ```c
    enum color {RED, GREEN, BLUE};
    int c = RED;
    ```

# Typedefs and Structs

- `typedef` allows you to define new types.

    ```c
    typedef uint8_t BYTE;
    BYTE b1, b2;
    ```

- structs are structured groups of variables, e.g.,

    ```c
    // SONG is alias for typdef struct { int length_in_seconds; int year_recorded; }
    typedef struct {
        int length_in_seconds;
        int year_recorded;
    } SONG;

    SONG s;
    s.length_in_seconds = 245;
    s.year_recorded = 2013;
    ```

    Using `typedef` with `struct` to use `SONG` instead of `struct SONG`.

- Recursive data structures

    ```c
    typedef struct Node Node;

    struct Node {
        int value;
        Node *next;
    }
    ```

    or another way

    ```c
    typedef struct Node {
        int value;
        struct Node *next;
    } Node;
    ```

# Pointers, Arrays and Strings

A pointer is a reference (its value is a memory address) of another variable.

```c
int x = 3;
int *p = &x; // pointer p holds the reference (address) of variable x.
printf("p points to value: %d\n", *p); // access the value pointed to by p using derefernce operator *.
printf("p points to address: %x\n", p);
*p = 5; // change the value pointed to by p.
```

## Pointers and function parameters

C passes parameters **by value**. A function parameter gets assigened a copy of the argument value. Chaning the function's copy cannot change the original.

```c
void add_one(int x)
{
    x = x + 1;
}

int y = 3;
add_one(y);
// y is still 3.
```

To get a function to change a value, *pass in a pointer*.

```c
void add_one(int *p)
{
    *p = *p + 1;
}

int y = 3;
add_one(&y);
// y is now 4.
```

## Pointers and structs

```c
typedef struct {
    int x;
    int y;
} Coord;

Coord coord = { 12, 34 };
Coord *p = &coord;
int k = p->x; // arrow notation
k = (*p).x; // equivalent
```

# Arrays, Pointer and Arithmetic

A C array is really just a big block of memory.

```c
int arr[2]; // declaration
int arr[] = {123, 456}; // declaration and initialization
int k = arr[n]; // accessing element, return the (n+1)th element, this is a shorthand for **pointer arithmetic**.
```

## Pointer Arithmetic

Equivalent:
- a[i] <=> *(a+i)

```c
uint32_t a[] = { 10, 20, 30 };
uint32_t *p = a;
int k = *(p+2); // k = 30
```

## Pointer and function

How to get a function to change a pointer.
- Write a function `increment_ptr` to change where the pointer points to.
- Remember: C is pass by value!

```c
void increment_ptr(int *p) {
    p = p + 1;
}

int arr[3] = {10, 20, 30};
int *q = arr;
increment_ptr(q);
printf("*q is %d\n", *q); // *q is still 10
```

Solution: pass **a pointer to a pointer**.

```c
void increment_ptr(int **p) {
    *p = *p + 1;
}

int arr[3] = {10, 20, 30};
int *q = arr;
increment_ptr(&q);
printf("*q is %d\n", *q); // *q is 20 now
```

## Pointers and Arrays

Arrays are (almost) indentical to pointers.
- `char *str` and `char str[]` are nearly identical declarations.
- They are differ in `very subtle` ways: incrementing, declaration of filled arrays, ... (more in a bit).
- `arr` is an array variable, it is also a "pointer" to ther first (0-th) element.
- `arr[0]` is the same as `*arr`.
- `arr[2]` is the same as `*(arr+2)`.

An array is passed to a function as a pointer.
- You should always explicitly **include array length as a parameter**.

```c
int sum(int arr[], unsigned int size) {
    ...
}

int main(void) {
    int a[5];
    int s = sum(a, 5);
}
```

Declared arrays are only allocated while the scope is valid (same as normal variables).

```c
char *random_str() {
    char str[32];
    ...
    return str;
} // is incorrect, str is valid only in the local scope of the function.
```

**Solution**: Dynamic memory allocation! (more later)

# C Strings

A C string is just an array of characters, followed by **a null terminator**.
- Null terminator: the byte 0 (number) aka '\0' character.

```c
char str[] = "abc"; // |'a'|'b'|'c'|'\0'| 
```

The standard C library `string.h` assumes **null-terminated strings**.
- String operations do not include the null terminator, e.g., strlen() to return the length of a string.

Some example codes with string.

```c
/* copy 'from` into 'to`; assume to is big enough */
void copy(char to[], char from[])
{
    int i = 0;
    while ((to[i] = from[i]) != '\0')
        ++i;
}

/* read a line into s, return length */
int getline(char s[], int lim)
{
    int c, i;
    for (i=0; i < lim-1 && (c=getchar()) != EOF && c != '\n'; ++i)
        s[i] = c;
    if (c == '\n') {
        s[i] = c;
        ++i;
    }
    s[i] = '\0';
    return i;
}
```
