**E1**. Write an efficient function to find the first nonrepeated character in a string. For instance, the first nonrepeated character in “total” is 'o' and the first nonrepeated character in “teeter” is 'r'. How efficiency is your algorithms?

**E2**. Assume you have the class hierarchy such as:

B extends A; C extends B; D extends A

And a function:

void f(B x);

Which classes of objects can you pass to the function?

**E3**. Given:

```
class Top {
    public Top(String s) { System.out.print("B"); }
}

public class Bottom2 extends Top {
    public Bottom2(String s) { System.out.print("D"); }
    public static void main(String [] args) {
        new Bottom2("C");
        System.out.println(" ");
    }
}
```

What is the result?

* A. BD
* B. DB
* C. BDC
* D. DBC
* E. Compilation fails

**E4**. Given:

```
public class Mirror {
    int size = 7;
    public static void main(String[] args) {
        Mirror m1 = new Mirror();
        Mirror m2 = m1;
        int i1 = 10;
        int i2 = i1;
        go(m2, i2);
        System.out.println(m1.size + " " + i1);
    }
    static void go(Mirror m, int i) {
        m.size = 8;
        i = 12;
    }
}
```

What is the result?

* A. 7 10
* B. 8 10
* C. 7 12
* D. 8 12
* E. Compilation fails
* F. An exception is thrown at runtime

**E5**. Given

```
public class McGee {
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

**E6**. Given:

```
import java.util.*;
public class Sequence {
    public static void main(String[] args) {
        ArrayList<String> myList = new ArrayList<String>();
        myList.add("apple");
        myList.add("carrot");
        myList.add("banana");
        myList.add(1, "plum");
        System.out.print(myList);
    }
}
```

What is the result?

* A. [apple, banana, carrot, plum]
* B. [apple, plum, carrot, banana]
* C. [apple, plum, banana, carrot]
* D. [plum, banana, carrot, apple]
* E. [plum, apple, carrot, banana]
* F. [banana, plum, carrot, apple]
* G. Compilation fails

**E7**. Given:

```
public class OverAndOver {
    static String s = "";
    public static void main(String[] args) {
        try {
            s += "1";
            throw new Exception();
        } catch (Exception e) { 
            s += "2";
        } finally { 
            s += "3"; doStuff(); s += "4";
        }
        System.out.println(s);
    }
    static void doStuff() { int x = 0; int y = 7/x; }
}
```

What is the result?

* A. 12
* B. 13
* C. 123
* D. 1234
* E. Compilation fails
* F. 123 followed by an exception
* G. 1234 followed by an exception
* H. An exception is thrown with no other output

**E8**. Write an efficient algorithm to reverse all words in a string. Assume that words in the string are space delimiter-seperated. For example if input is "Today is the great day" then output should be "day great the is Today".

**E9**. Given a database as below:

* Supplier(name, address) // name is the primary key
* Product(title, price, year, sname) // tile is the primary key and sname is a foreign key references to Supplier's name
* Order(product, quantity, delivered) //product is a foreign key references to Product.title

a. Write a SQL query to select all Product with price > 1000 and show they Supplier's name.

b. Write a SQL script to update delivered = true for all Order with product = "Iphone XS Max"

c. Write a SQL query to show total number of products has ordered for each Supplier.

**E10**. There is a group of students: three males and four females. The students go to visit the Ho Chi Minh temple and they have to stand in a line. How many ways do they stand such as no male standing next to other male?
