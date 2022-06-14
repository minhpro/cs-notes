**E1**. Fibonacci sequence is a sequence of numbers such that each number is the sum of the two preceding ones, the first and the second number are 1 and 1. Write a function that calculates the nth number of the Fibonacci sequence.


fib(1) = fib(2) = 1
fib(n) = fib(n-1) + fib(n-2)


int fib(int n) {
    int sum = 0;
    for (int i =0; i<n;i++>) {
        sum += 
    }
}

factorial n!

int f(int n,int count) {
    if (count<n) {
    count++;
    return f(n*count    );
    }
}

**E2**. Assume you have the class hierarchy such as:

B extends A; C extends B; D extends A

And a function:

void f(B x);

Which classes of objects can you pass to the function?


*E2.1**

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


Department: deptId*, deptName, building

Courses: courseId*, title, deptId**, credits

Teachers: teacherId*, teacherName, deptId**, salary

1. Find the titles of courses in the `Comp. Sci.` department that have 3 credits.

SELECT title  FROM Department d join Courses c on d.deptId = c.deptID WHERE c.credit = 3

INNER JOIN vs LEFT JOIN

A(id, aName)  B(id, bName)
1, x1            1, y1
2, x2            3, y3

A INNER JOIN B (id)
1, x1, y1

A LEFT JOIN B (id)
1, x1,y1
3,y3

B LEFT JOIN A
1, x1,y1
2,x2
