## Mở đầu

Phần mềm ngày càng trở nên phức tạp, vì thế việc cấu trúc nó (phần lớn là source code) càng trở nên quan trọng hơn.

Một phần mềm được cấu trúc tốt là phần mềm dễ đọc source code, đễ dàng debug, đễ sửa đổi, mở rộng, và cung cấp các modules có thể được tái sử dụng để từ đó giảm chi phí lập trình sau này. Khả năng module hóa là yếu tố cốt lõi giúp xây dựng phần mềm tốt.

Functional programming cung cấp các tính năng để hỗ trợ tối đa khả năng module hóa (modularity) của source code. Hãy cùng khám phá xem các lợi ích quan trọng của functional programming là gì.

## Functional programming là gì?

Functional programming hay còn gọi là **lập trình hàm**, đúng như cái tên của nó, là mô hình lập trình trong đó xoay quanh việc sử dụng functions (hàm).

Với một ngôn ngữ theo mô hình hoàn toàn functional (pure functional programing) thì mọi function sẽ giống như một hàm toán học thuần túy (mathematical functions). Một function có tham số đầu vào input gọi là arguments và sinh ra một output value. Với mỗi input thì luôn luôn có chỉ một và một output (không thay đổi theo thời gian hay môi trường), cho dù là thực thi function bất kỳ lúc nào.

Một functional program sẽ chứa một hàm main, cũng có input và output, hàm main này thường được build từ các hàm khác. Tất cả functional programs đều không có assignment statements, từ đó một biến, một khi đã gán giá trị thì không bao giờ thay đổi. Việc gọi function (function call) sẽ không có ảnh hưởng gì khác ngoài vậy tính toán (compute) ra kết quả output từ input, thuật ngữ gọi là **no side-effect**. Sẽ dễ dàng hơn khi trace hay phân tích functional program. Nó giống như chúng ta đọc các function toán học, function này gọi function kia, tính toán output từ input, tất cả chỉ có vậy, không hơn không kém.

Khác với functional programming là các mô hình lập trình có chứa assignment statement, variable có thể đổi giá trị, call function có thể **side-effect**. Các mô hình như vậy gọi là **imperative programming**, ví dụ như **structured programming** trong C programming, hay **object oriented programming** như trong Java programming. Dưới đây là một bảng so sánh các khái niệm cơ bản trong các mô hình lập trình khác nhau, điều này giúp bạn có các nhìn tổng quát vào các mô hình lập trình phổ biến.

| Khái niệm | Functional programming | Structure programming | Object Oriented Programming |
|-----------|------------------------|-----------------------|---------------------------|
| Function | no side-effect | side effect, có thể không input, không output | side effect, có thể không input, không output |
| Variable | không đổi value | trừ constant, có thể đổi value | trừ constant, có thể đổi value |
| Assignment | không có | hay được dùng | hay được dùng |
| Loop statement | không có | for, while | for, while |
| Object | không có, dùng type, record | không có, dùng type, record | object với attributes và methods |
| Memory management | Automatic | Manual | Automatic |

Các ngôn ngữ hiện đại đều support fucntional programming ở một mức nào đó. Ví dụ như sau.
- Pure functional proramming: Haskell, Agda, Eml, PureScript
- Impure functional programming: là các ngôn ngữ support style of functional programming nhưng vẫn cho phát mutation (thay đổi value của biến) như Ocaml, Java 8+, C++11, Kotlin, Scala, JavaScript, ...

Trong bài viết này, Haskell sẽ được sử dụng làm ví dụ. Một số khái niệm có thể có thêm ví dụ cho ngôn ngữ khác như C, Java, JavaScript.

## Recursive Function

Recursive Functions hay còn gọi là hàm đệ quy là khái niệm quan trọng cần nắm bắn để tìm hiểu và sử dụng functional programming. Hàm đệ quay là hàm được định nghĩa bằng cách gọi lại chính nó. Một ví dụ kinh điển cho hàm đệ quy là hàm giai thừa. Định nghĩa của hàm giai thừa như sau.
- 0! = 1
- 1! = 1
- 2! = 2 * 1
...
- n! = n * (n-1) * (n-2) * ... * 1

Với cách tính thông thường sử dụng vòng lặp, chúng ta có thể viết hàm giai thừa như sau.

```C
// Programming in C
int factorial(int n) {
    if (n == 0) return 0;
    int m = n;
    for (int i = n - 1; i >= 1; i--) {
        m = m * i;
    }
    return m;
}
```

Chúng ta có thể định nghĩa hàm giai thừa theo cách đệ quy như sau:
- 0! = 1
- n! = n * (n-1)!

Áp dụng trong coding, chúng ta sẽ làm như sau:

```C
// Programming in C
int factorial(int n) {
    if (n == 0) return 1;
    return n * factorial(n-1);
}
```

Hay trong Haskell

```Haskell
factorial :: Int -> Int -- khai báo signature của hàm
factorial 0 = 1
factorial n = n * factorial (n-1)
```

Các tham số của hàm trong Haskell không được bọc trong 2 dấu ngoặc tròn (), mà chúng đứng ngay sau tên hàm. Trong các pháp toán thì việc apply hàm với các arguments luôn luôn được ưu tiên, do đó chúng ta không phải viết là `n * (factorial (n-1))`.

Các thuật toán đệ quy thường dựa trên việc định nghĩa base case, ví dụ ở đây là 0! = 1 và cách tính đệ quy từ tham số ban đầu so với tham số nhỏ hơn như kiểu n! = n * (n-1)!

Chúng ta sẽ xét một ví dụ phức tạp hơn để hiểu rõ về hàm đệ quy. Ví dụ về thuật toán quick sort. Thuật toán quick sort dựa trên việc chọn một phần tử trong mảng (gọi là pivot), sau đó chia mảng làm 2 phần: nhỏ hơn và lớn hơn phần tử pivot này. Tiếp tục áp dụng cách làm này với 2 phần đã chia ra đến khi không thể áp dụng được nữa, nghĩa là phần áp dụng chỉ có một phần tử hoặc không có phần tử nào.

Ví dụ đầu vào là: [3, 5, 1, 4, 2]. Chúng ta chọn pivot là item đầu tiên của mảng. Quá trình thực hiện của thuật toán được mô phỏng như sau:
- [1,2], 3, [5, 4]
- [], 1, [2], 3, [4], 5
- 1, 2, 3, 4, 5

Cách implement thuật toán quick sort trong Haskell như sau:

```Haskell
qsort:: [Int] -> [Int]  -- signature take an array of integers and return an sorted array of integers.
qsort [] = []
qsort [x] =  [x]
qsort [x:xs] = qsort smaller ++ [x] ++ qsort larger
                where
                  smaller = [a | a <- xs, a <= x]
                  larger =  [b | b <- xs, b > x]
```

Trong đó:
- [x:xs]: x là phần tử đầu tiên, xs là biểu diễn các mảng các phần tử còn lại
- ++ là phép nối 2 mảng
- ký hiệu [a | a <- xs, a <= x] là lấy ra các phần tử từ mảng xs thỏa mãn điều kiện <= x.
