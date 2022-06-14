## Algorithm and Datastructure

1. Will you implement a queue by an array or a linked-list?

2. Given two strings, write a function that checks whether the one is a rotation of the other.

3. Each time, there is a generated number inserted to a sorted array (can be expanded), how to keep track the median.

4. Array vs Linked List

5. Stack is? How implement?

6. Binary Tree? How to check it is BST ?

7. Thiết kế một enum cho các ngày trong tuần và viết hàm đầu vào là enum này với kết quả đầu ra là thứ chẵn hay thứ lẻ

Example; MON -> chẵn, TUE -> lẻ

## Java

1. Different between Exception and Error

2. Different between Checked and Unchecked Exception

3. Why Builder? Why not use multiple arguments Constructor?

3. Different between Collection and Stream

4. Different between map() and flatMap()

5. What is the difference between a normal and functional interface


## Other

1. Assume that now is 10h00, what is angle between the hour and minute hands? How about 10h50?

2. Bạn có 20 chai dung dịch H2SO4. Trong đó có 19 chai nặng 500g, nhưng một chai thì nặng 505g. Với một cái cân thăng bằng, bạn sẽ tìm ra chai dung dịch nặng hơn bằng cách nào?

4. How many ways are there to split a dozen people into 3 teams, where one team has 2 people, and the other two teams have 5 people each?

5. Bạn An viết lên bảng 2021 số từ 1 đến 2021, sau đó An làm như sau: mỗi lần An xoá đi 2 số bất kỳ và viết một số mới bằng tổng hai số đã xoá. An làm như vậy đến khi trên bảng chỉ còn một số. Hỏi số đó là số nào?

6. Nam tung súc sắc (6 mặt) 6 lần liên tiếp. Bạn hãy tính xem xác suất để một mặt nào đó xuất hiện nhiều hơn một lần.


## Database

Department: deptId*, deptName, building

Courses: courseId*, title, deptId**, credits

Teachers: teacherId*, teacherName, deptId**, salary

Students: studentId*, studentName, deptId**, totalCredits

StudentCourses: courseId*, studentId*, semester, year

1. Find the titles of courses in the `Computer. Science` department that have 3 credits.

2. Find the highest salary of any instructor.

3. Find all instructors earning the highest salary (there may be more than one with the same salary)

3. Write a query to get a list of all students and how many credits each student is taked in semester `Spring` of year 2009.

## Design

Xây dựng một website chia sẻ kiến thức

Các thành viên sau khi đăng ký tài khoản và đăng nhập thành công thì có thể post bài viết.

Bài viết có thể được gán nhiều tag (sql, java, os, ...)

Khi xem bài viết, thành viên có thể like và comment bài viết, hay có thể follow tác giả của bài viết.

Tác giả của bài viết có thể cập nhật thông tin bài viết của mình

Trang chủ sẽ chứa danh sách bài viết có phân trang, vào xem chi tiết mỗi bài viết sẽ gồm thông tin:
* Title
* Tên tác giả
* Mô tả
* Nội dung
* Số lượng like
* Danh sách comment


Query ra tất cả các tag kèm theo số lượng bài viết được gán cho từng tag

Java 10
Sql 20
Mac 10
....

## Twitter

Ứng dụng 