# References

The `Box<T>` heap pointer, the pointers internal to `String` and `Vec` values - are owning pointer;
when the owner is dropped, the value goes with it. Rust has non-owning pointer types call **references**,
which have no effect on their referents' lifetimes.

References must never outlive their referents. Rust refers to creating a reference to some valu as
*borrowning* the value: what you have borrowed, you must eventually return its owner.

## References to Values

Take an example, the list of Strings:

```rust
fn show(l: Vec<String>) {
  for s in l {
    println!("Value: {}", s);
  }
}

fn main() {
  let l = vec!["s1".to_string(), "s2".to_string(), "s3".to_string()];

  show(l);
  println!("Size: {}", l.len();
}
```

You cannot use *l* after passing it to *show* function, Rust complains that:

```txt
error[E0382]: borrow of moved value: `l`
  --> src/main.rs:11:26
   |
8  |     let l = vec!["s1".to_string(), "s2".to_string(), "s3".to_string()];
   |         - move occurs because `l` has type `Vec<String>`, which does not implement the `Copy` trait
9  |
10 |     show(l);
   |          - value moved here
11 |     println!("Size: {}", l.len());
   |                          ^^^^^^^ value borrowed here after move
   |
note: consider changing this parameter type in function `show` to borrow instead if owning the value isn't necessary
  --> src/main.rs:1:12
   |
1  | fn show(l: Vec<String>) {
   |    ----    ^^^^^^^^^^^ this parameter takes ownership of the value
   |    |
   |    in this function
```

The right way to handle this is to use references. A reference lets you access a value without affecting 
its ownership. References come in two kinds:

* A *shared reference* lets you read but not modify its referent. You can have as many shared references
to a particular value at a time as you like. The expression `&e` yields a shared reference to *e's* value;
if *e* has the type *T*, then *&e* has type *&T*, pronounced "ref T". Shared references are *Copy*.

* If you have a *mutable reference* to a value, you may both read anh modify the value. However, you
may not have any other references of any sort to the vallue active at the same time. The expression
*&mut e* yields a mutable reference to *e's* value; its type is *&mut T*, pronounced *ref mut T*.
Mutable references are not *Copy*.

The rule between shared and mutable references can be think as *multiple readers or single writer* rule.
In fact, this rule doesn't apply only to references; it covers the borrowed value's owner as well. As
long as there are references to a value, its owner cannot modify it.

The *show* function doesn't need modify the list, the caller should pass a shared reference to the list.

```rust
show(&l);
```

The *show* function needs to adjust:

```rust
fn show(l: &Vec<String>) {
  for s in l {
    println!("Value: {}", s);
  }
}
```

## Implicitly Dereferences

References are created explicitly with the `&` operater, and dereferenced explicitly with the `*` operater.

```rust
let mut x = 20;
let m = &mut x;       // &mut x is a mutable reference to x
*m += 20;            // explicitly dereference m to set x's value
assert!(*m == 40);  // and to see x's new value
```

But Rust provides the implicitly dereference future when using `.` operator.
The `.` operator implicitly dereferences its left operand, if needed:

```rust
struct Book { title: String, author: String, price: f32 };

let b = Book { title: "new age".to_string(), author: "unknown".to_string(), price: 20.5 };
let b_ref = &b;
assert_eq!(b_ref.title, "new age".to_string());

// Equivalent to the above, but with explicitly dereference written out
assert_eq!((*b_ref).title, "new age".to_string()); 
```

The `println!` macro used in the `show` function expands to code that uses the `.` operator,
so it takes an implicit dereference to its arguments if needed.

Let's take more examples, such as `Vector's sort` method:

```rust
let mut numbers = vec![1993, 2001, 1196, 2023];
numbers.sort();        // implicitly borrows a mutable reference to numbers
(&mut numbers).sort(); // equivalent, but more verbose
```

## Structs Containing References

How about placing a reference in a struct

```rust
struct S {
    r: &i32
}
```

There is an error when compliling code

```txt
error[E0106]: missing lifetime specifier
 --> src/main.rs:2:8
  |
2 |     r: &i32,
  |        ^ expected named lifetime parameter
```

Whenever a reference type appears inside another type's definition, you mus write out its lifetime

```rust
struct S<'a> {
  r: &'a i32
}
```

The expression `S { r: &x }` creates a fresh S value with some lifetime `'a`. When you store `&x`
in the `r` field, you constrain `'a` to lie entirely within `x`'s lifetime.

How does you define another type contains a reference to `S`? You have place a lifetime parameter.

```rust
struct D<'a> {
  s: S<'a>
}
```
