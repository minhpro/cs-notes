**E1**. Fibonacci sequence is a sequence of numbers such that each number is the sum of the two preceding ones, the first and the second number are 1 and 1. Write a function that calculates the nth number of the Fibonacci sequence.




<!-- De quy -->

int fib(int n) {
    if(n==1) return 1;
    if(n==2) return 1;
    return fib(n-1) + fib(n-2);
}

<!-- De quy + quy hoach dong -->
int b[] = int new [100];
b[0] = 1;
b[1] = 1;

int fib(int n) {
    if(b[n]!=0) return b[n];
    return fib(n-1) + fib(n-2);
}

**E2**. Assume you have the class hierarchy such as:

B extends A; C extends B; D extends A

And a function:

void f(B x);

Which classes of objects can you pass to the function? B, C


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


E5

Departments: deptId*, deptName, building

Courses: courseId*, title, deptId**, credits

Teachers: teacherId*, teacherName, deptId**, salary

1. Find the titles of courses in the `Comp. Sci.` department that have 3 credits.

select title from Courses c
join Departments d
on c.deptId = d.deptId
where c.credits = 3 and d.deptName = 'Comp. Sci.'

2. Increase the salary of each instructor in the `Comp. Sci.` department by 10%.

update Teachers set salary=salary*1.1
where deptId = (select d.deptID from Departments d
join Teachers t
on t.deptId = d.deptId
where d.deptName = 'Comp. Sci.')


40 bạn học sinh trong một lớp đứng thành một vòng tròn để tham gia trò chơi. Các bạn sẽ lần lượt theo chiều kim đồng hồ đếm 1,2,1,2,... Lớp trưởng là người đếm đầu tiên, người nào đếm số 2 thì tự động rời khỏi vòng tròn. Cứ như thế, người cuối cùng còn lại là người thắng cuộc. Nếu là bạn, bạn sẽ đứng ở vị trí nào để trở thành người thắng cuộc? 

