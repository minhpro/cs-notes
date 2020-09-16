E1

```java
char firstNonRepeated(String s) {
    Map<char, int> count; // character count

    for ch in s {
        if count.containsKey(ch) {
            int value = count.get(ch);
            value += 1;
            count.put(ch, value);
        } else {
            count.put(ch, 1);
        }
    }

    for ch in count.allKeys() {
        int value = count.get(ch);
        if value == 1 {
            return ch;
        }
    }

    return NULL;
}
```

E2: B and C

E3: E

E4: B

E5: A

E6: B

E7: H

E8

```java
String wordsReverse(String s) {
    String[] words = s.split(" ");
    StringBuilder sb;
    for (int i = words.length - 1; i >= 0; i--) {
        sb.append(words[i]);
        if i !=0 {
            sb.append(" ");
        }
    }
    return sb.toString();
}
```

E9.

a, 

```sql
SELECT * FROM Product where price > 1000
```

b, 
```sql
UPDATE Order SET delivered = true WHERE product = 'Iphone XS Max'
```

c, 

```sql
SELECT s.name, sum(quantity)
FROM Supplier s 
    JOIN Project p on s.name = p.sname
    JOIN Order o on p.title = o.product
GROUP BY s.name
```

E10: 4! * A(3,5) = 1440
