# Programming in JavaScript with Nodejs environment

To get start with Nodejs, visit the page: https://nodejs.dev/learn

## Grammar and types

JavaScript applications consist of statements with an appropriate syntax. A single statement may span multiple lines. Multiple statements may occur on a single line if each statement is separated by a semicolon.

Statements and declarations by category:

* Control flow: if, else, throw, try...catch,...
* Declarations: var, let, const
* Functions and classes
* Iterations: do...while, for, for...in, for...of, for await...of, while
* Others: export, import, ...

## Comments

```js
// a one line comment

/* this is a
* multi-line comment
*/ 
```

## Variable declarations

**var**

The *var statement* declares a function-scoped or globally-scoped variable, optionally initalizing it to a value.

```js
var x = 1;

if (x === 1) {
    var x = 2;
    console.log(x)
    // expected output: 2
}

console.log(x)
// expected output: 2
```

**let**

The *let declaration* declares a block-scoped local variable, optionally initializing it to a value.

```js
let x = 1;
if (x === 1) {
    let x = 2;
    console.log(x)/
    // expected output: 2
}

console.log(x);
//expected output: 2
```

**const**

Using *const* to declare contants. Constants are block-scoped, much like variables declared using the *let* keyword. The value of a contant can't be changed through reassignment, and it can't be redeclared. However, if a constant is an object or array its properties or items can be updated or removed.

```js
const number = 23;

try {
    number = 99;
} catch (err) {
    console.log(err);
    // expected output: TypeError: invalid assignment to const `number'
    // Note - error messages will vary depending on browser
}

console.log(number);
//expected output: 23
```

## JavaScript data types and data structures

JavaScript is a *loosely typed* and *dynamic* language. Variables can be assigned and re-assigned values of all types:

```js
let x = 23;  // x is now a number
x = 'Hello'; // x is now a string
x = true;    // x is now a boolean
```

JavaScript types consists of **primitive values** and **objects**.

* Primitive values (data represented directly at the lowest level of the language)
    * Boolean type
    * Null type
    * Undefined type
    * Number type
    * BigInt type
    * String type
    * Symbol type
* Objects (collections of properties)

