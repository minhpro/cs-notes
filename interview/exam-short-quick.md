**T1** Which collection class(es) allows you to grow or shrink its size and provides indexed access to its elements, but whose methods are not synchronized? (Choose all that apply.)
A. java.util.HashSet
B. java.util.LinkedHashSet
C. java.util.List
D. java.util.ArrayList ''
E. java.util.Vector
F. java.util.PriorityQueue

**T2**
Given:

```Java
import java.util.*;

public class Mixup {
    public static void main(String[] args) {
        Object o = new Object();
        // insert code here
        s.add("o");
        s.add(o);
    }
}
```

And these three fragments:
        I.   Set s = new HashSet();
        II.  TreeSet s = new TreeSet();
        III. LinkedHashSet s = new LinkedHashSet();

When fragments I, II, or III are inserted independently at line 7, which are true? (Choose all
that apply.)
A. Fragment I compiles v
B. Fragment II compiles v
C. Fragment III compiles v
D. Fragment I executes without exception v
E. Fragment II executes without exception
F. Fragment III executes without exception v

**E1**. Fibonacci sequence is a sequence of numbers such that each number is the sum of the two preceding ones, the first and the second number are 1 and 1. Write a function that calculates the nth number of the Fibonacci sequence.

**E1.1** Write Unit Tests for the following function

```Java
/**
* Find the closest number that divides three (x % 3 = 0)
*/
int f(int n) {
  // TODO
}
```


**E2**. Assume you have the class hierarchy such as:

B extends A; C extends B; D extends A

And a function:

void f(B x);

Which classes of objects can you pass to the function?

**E2.1**

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

**E3**

```Java
public class DaysOfWeek {
    public static void main(String[] args) {
        Days d1 = Days.TH;
        Days d2 = Days.M;
        for(Days d: Days.values()) {
            if(d.equals(Days.F)) break;
            d2 = d;
        }
        System.out.println((d1 == d2)?"same old" : "newly new");
    }

    enum Days {M, T, W, TH, F, SA, SU};
}
```

What is the result?

* A. same old
* B. newly new
* C. Compilation fails due to multiple errors
* D. Compilation fails due only to an error on line 7
* E. Compilation fails due only to an error on line 8
* F. Compilation fails due only to an error on line 11
* G. Compilation fails due only to an error on line 13

**E4**

Department: deptId*, deptName, building

Courses: courseId*, title, deptId**, credits

Teachers: teacherId*, teacherName, deptId**, salary

1. Find the titles of courses in the `Comp. Sci.` department that have 3 credits.

2. Increase the salary of each instructor in the `Comp. Sci.` department by 10%.

3. Find the highest salary of any instructor.


A(id, aName)  B(id, bName)
1, x1            1, y1
2, x2            3, y3

**E5**

40 bạn học sinh trong một lớp đứng thành một vòng tròn để tham gia trò chơi. Các bạn sẽ lần lượt theo chiều kim đồng hồ đếm 1,2,1,2,... Lớp trưởng là người đếm đầu tiên, người nào đếm số 2 thì tự động rời khỏi vòng tròn. Cứ như thế, người cuối cùng còn lại là người thắng cuộc. Nếu là bạn, bạn sẽ đứng ở vị trí nào để trở thành người thắng cuộc?