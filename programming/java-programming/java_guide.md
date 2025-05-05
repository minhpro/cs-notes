## Table of contents

1. [Introduction](#introduction)
2. [Source file](#source-file)

### Introduction

Coding standard (code style) for source code in the Java Programming Language.

**Terminology notes**

- The term *class* is used inclusively to mean an "ordinary" class, enum class, interface or annotation type (`@interface`).
- The term *member* (of a class) is used inclusively to mean a nested class, field, method, *or constructor*; that is, all top-level contents of a class except initializers and comments.
- The term *comment* always refers to *implementation* comments. We do not use the phrase "documentation comments", and instead use the common term "Javadoc."

### Source file

- The source file name consists of the case-sensitive name of the top-level class it contains, plus the `.java` extension.
- Source files are encoded in UTF-8.
- Tab characters are not used for indentation.


A source file consists of, in order:
- License or copyright information, if present
- Package statement
- Import statements
- Exactly one top-level class

Exactly one blank line separates each section that is present.

#### Import statements

Imports are ordered as follows:
- All static imports in a single block.
- All non-static imports in a single block.

If there are both static and non-static imports, a single blank line separates the two blocks. There are no other blank lines between import statements.

Within each block the imported names appear in ASCII sort order.

### Formatting

// TODO

### References

- [Google Java style](https://google.github.io/styleguide/javaguide.html)
