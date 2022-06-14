1. Factory method là gì? Hơn kém Constructor? Khi nào nên dùng?

2. Builder vs many args constructor vs setters

3. Khi nào thì chúng ta không bao giờ tạo ra instance của một class

4. try-with-resource vs try-finally

5. information hiding or encapsulation

6. Định nghĩa hằng public static final trong Interface?

7. Phân biệt Exception vs Error. Checked and Runtime (Unchecked) exceptions?

8. Khi nào thì Throw Exception (propagate), khi nào thì nên handle nó?

9. Remove các bản ghi duplicate trong bảng SQL


```
delete from role_ip_addresses
where 
id NOT IN 
(select  id from (SELECT 
MIN(id) as id
from 
role_ip_addresses 
group by role_id, ip_address) t);
```

Bọc thêm select bên ngoài để tránh bị lỗi

```
You can't specify target table 'role_ip_addresses' for update in FROM clause
```
