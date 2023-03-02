## Algebraic data types

**Sum Types**

*Haskell*

```c
data Bool = False | True

let bValue = True
```

**data** means defining a new data type. The part before **=** denotes the type, which is **Bool**. 
The parts after the **=** are **value constructors**. They specify the different values that this type can have.
The **|** is read as *or*.

*Rust*

Using **enum**

```c
enum Bool {
	False,
	True,
}

let bValue = Bool::True;
```

**Product Types**:

*Haskell*

*Value constructors* can have parameters.

```c
data Point = Point Float Float

let p = Point 10.2 20.3
```

or *Record syntax* (only syntax sugar at all!)

```c
data Point = Point { 
	  x :: Float,
		y :: Float
}	

// x :: Point -> Float
// x (Point a _) = a

let p = Point {x=10.2, y=20.3}
```

*Rust*

Using **struct**

```c
struct Point {
	x: f32,
	y: f32,
}

let p = Point {x: 10.2, y: 20.3,};
```

**Mix Types**

*Haskell*

```c
data Shape = Circle Point Float | Rectangle Point Point
```

*Rust*

Mixing **enum** and **struct**

```c
enum Shape {
	Circle { c: Point, r: f32},
	Rectangle { topLeft: Point, bottomRight: Point },
}

let circle = Shape::Circle { c: Point { x: 1.0, y: 2.0, }, r: 10.5, };
```

**Pattern Matching**

*Haskell*

```c
surface:: Shape -> Float
surface (Circle _ r) = pi * r^2
surface (Rectangle (Point x1 y1) (Point x2 y2)) = (abs $ x2 - x1) * (abs $ y2 - y1)
```

*Rust*

```c
fn surface(s: &Shape) -> f32 {
	match *s {
		Shape::Circle { r, .. } => pi * r^2,
		Shape::Rectangle { topLeft: t, bottomRight: b } => (t.x - b.x).abs() * (t.y - b.y).abs(),
	}
}
```

**Type Parameter (Generic Type)**

*Haskell*

```c
data Maybe a = Nothing | Just a

let n = Just 10
// n :: Maybe Int

data Either a b = Left a | Right b

let s = Left "Hello" :: Either String Int
// s :: Either String Int
```

*Rust*

```c
struct Point<T> {
	x: T,
	y: T,
}

enum Option<T> {
	Some(T),
	None,
}

enum Result<T, E> {
	Ok(T),
	Err(E),
}

let p = Point { x: 5, y: 10 }; // x: Point<i32>
let n = Some(10);// n: Option<i32>, Option and it variants brought into scope by prelude, don't need Option::Some
let r: Result<i32, String> = Ok(String::from("Hello"));
```

**Recursive data structures**

*Haskell*

```c
data List a = Empty | Cons a (List a)

let l = Cons 4 (Cons 5 Empty)

// Binary search tree
data Tree a = EmptyTree | Node a (Tree a) (Tree a)

singleton :: a -> Tree a
singleton x = Node x EmptyTree EmptyTree

treeInsert :: (Ord a) => a -> Tree a -> Tree a
treeInsert x EmptyTree = singleton x
treeInsert x (Node a left right)
		| x == a = Node a left right
		| x < a  = Node a (treeInsert x left) right
		| x > a  = Node a left (treeInsert x right)
```

*Rust*

Using smart pointers, **Box<T>**  to point to data on the Heap

```c
enum List<T> {
	Cons(T, Box<List<T>>), // using Box to pass `recursive type `List` has infinite size` error
	Nil,
}

use crate::List::{Cons, Nil};

let l = Cons(1, Box::new(Cons(2, Box::new(Cons(3, Box::new(Nil))))));
```
