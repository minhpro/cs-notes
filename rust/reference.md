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


