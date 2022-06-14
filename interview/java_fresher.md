1. Will you implement a queue by an array or a linked-list? (hỏi thêm về memory, độ phức tạp)

2. Write a function that calculates the Fibonacci nth (hỏi về đệ quy, vòng lặp, performance, unit test)

3. Write a function that calculates the factorial of n (same as above)

4. Different between HashMap and TreeMap

5. Different between quicksort vs mergesort

## Java

**E1**. Assume you have the class hierarchy such as:

B extends A; C extends B; D extends A

And a function:

void f(B x);

Which classes of objects can you pass to the function?

**E2**

```Java
import java.util.*;
        class MapTest {
          public static void main(String[] args) {
            Map<ToDo, String> m = new HashMap<ToDo, String>();
            ToDo t1 = new ToDo("Monday");
            ToDo t2 = new ToDo("Monday");
            ToDo t3 = new ToDo("Tuesday");
            m.put(t1, "cooking");
            m.put(t2, "reading");
            m.put(t3, "playing");
            System.out.println(m.size()); // What is result?
} }
class ToDo{
    String day;
    ToDo(String d) { 
        day = d; 
    }
    public boolean equals(Object o) {
        return ((ToDo)o).day.equals(this.day);
    }
}
```


1. Different between Exception and Error

2. Different between Checked and Unchecked Exception

3. Why Builder? Why not use multiple arguments Constructor?

## Database

Department: deptId*, deptName, building

Courses: courseId*, title, deptId**, credits

Teachers: teacherId*, teacherName, deptId**, salary

Students: studentId*, studentName, deptId**, totalCredits

StudentCourses: courseId*, studentId*, semester, year

1. Find the titles of courses in the `Computer. Science` department that have 3 credits.

2. Find the highest salary of any instructor.

Hỏi về Joining

## Other

1. Bạn An viết lên bảng 2021 số từ 1 đến 2021, sau đó An làm như sau: mỗi lần An xoá đi 2 số bất kỳ và viết một số mới bằng tổng hai số đã xoá. An làm như vậy đến khi trên bảng chỉ còn một số. Hỏi số đó là số nào?