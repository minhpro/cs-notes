**Closures** là anonymous functions, chúng có thể capture values from environment - scope nơi mà chúng được định nghĩa. Closures có thể được gán vào biến và pass as arguments to other functions.

Depending on how the captured variables are used in the closure, the compiler will specify the closure may capture variables by (in order of decreasing restriction) :
* by reference: `&T`
* by mutable reference: `&mut T`
* by value: `T`


