## Features of Java 17

Java 17 là phiên bản release LTS (Long Term Support) tiếp theo sau Java 11, ngoài nhiều cải tiến hiệu năng thì nó mang lại một số tính năng đáng chú ý dành cho developer. Sau đây chúng ta cùng xem chúng là gì.

Notes:
* Java 11 (LTS) release date: 25th September 2018
* Java 17 (LTS) release date: 14 September 2021
  * Nên nâng cấp lên Java 17
* Java 21 (LTS) release date: 19 September 2023
  * Java 21 mới ra, do đó chưa có dự kiến nâng cấp.

### Text Blocks

Text blocks cho phép developer viết String thành nhiều dòng mà không cần toán tử `+` hay StringBuilder. Do đó trông code sẽ dễ đọc và bắt mắt hơn.

```Java
String text = """
    {
      "name": "Michael",
      "age": 25,
      "address": "Doe Street, 23, Java Town"
    }
    """;
```

Với TextBlocks, bạn có thể hiểu là viết String như nào thì print sẽ ra như thế.

### Switch Expressions

Switch Expressions cho phép bạn return giá trị từ `switch` và sử dụng trong assignment, etc. Cấu pháp `switch` sẽ có chút thay đổi:
* dùng `->` thay vì `:`
* không cần `break` nữa

Xét ví dụ sau,

```Java
// Java 8, 11
private static void oldStyleWithBreak(Fruit fruit) {
    switch (fruit) {
        case APPLE, PEAR:
            System.out.println("Common fruit");
            break;
        case ORANGE, AVOCADO:
            System.out.println("Exotic fruit");
            break;
        default:
            System.out.println("Undefined fruit");
    }
}
```

Sử dụng Switch Expression mới

```Java
// Java 17
private static void withSwitchExpression(Fruit fruit) {
    switch (fruit) {
        case APPLE, PEAR -> System.out.println("Common fruit");
        case ORANGE, AVOCADO -> System.out.println("Exotic fruit");
        default -> System.out.println("Undefined fruit");
    }
}
```

Khi cần trả về giá trị từ `switch`,

```Java
private static void withReturnValue(Fruit fruit) {
    String text = switch (fruit) {
        case APPLE, PEAR -> "Common fruit";
        case ORANGE, AVOCADO -> "Exotic fruit";
        default -> "Undefined fruit";
    };
    System.out.println(text);
}
```

## Records

Record cho phép bạn tạo một immutable class một cách ngắn gọn.

```Java
record User(int id, String name){};

// new instance
User user = new User(10, "User 01");

// get properties by access them directly
System.out.println(String.format("User[id: %d, name: %s]", user.id, user.name));
```

##  Sealed Classes

Sealed Classes will give you more control about which classes may extend your class.

Nếu bạn muốn kiểm soát class nào được phép extends class của mình thì `Sealed Classes` là giải pháp cho bạn.

## Pattern matching for instanceof

Thông thường, khi cần kiểm tra một object có kiểu nào đó thì sau đó ta sẽ cần phải cast object sang một biến mới của kiểu cần kiểm tra. Xét ví dụ,

```Java
private static void oldStyle() {
    Object o = new GrapeClass(Color.BLUE, 2);
    if (o instanceof GrapeClass) {
        GrapeClass grape = (GrapeClass) o;
        System.out.println("This grape has " + grape.getNbrOfPits() + " pits.");
    }
}
```

Nhưng với pattern matching for instanceof, bạn có thể viết ngắn ngọn như sau:

```Java
private static void patternMatching() {
     Object o = new GrapeClass(Color.BLUE, 2);
     if (o instanceof GrapeClass grape) {
         System.out.println("This grape has " + grape.getNbrOfPits() + " pits.");
     }
}
```

## Helpful NullPointerExceptions

Java 11 chỉ show ra line number nơi mà `NullPointerException` xảy ra, nhưng chúng ta không rõ chained method nào dẫn tới `null`. 

Java 17 sẽ chỉ rõ `NullPointerException` xảy ra ở đâu, kèm theo thông tin chained method.

## Compact Number Formatting Support

Một factory method được thêm vào `NumberFormat` dùng để format numbers theo định dạng ngắn ngọn, thân thuộc hơn.

```Java
NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.ENGLISH, NumberFormat.Style.SHORT);
System.out.println(fmt.format(1000));
System.out.println(fmt.format(100000));
System.out.println(fmt.format(1000000));
```

Output:

```
1K
100K
1M
```

## Stream.toList()

Khi collect một stream về một list,

Thay vì phải `myStream.collect(Collectors.toList())` thì chỉ cần đơn giản `myStream.toList()`.

## References

* https://mydeveloperplanet.com/2021/09/28/whats-new-between-java-11-and-java-17/