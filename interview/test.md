## Algorithm and Datastructure

1. Will you implement a queue by an array or a linked-list?

2. Given two strings, write a function that checks whether the one is a rotation of the other.

3. Each time, there is a generated number inserted to a sorted array (can be expanded), how to keep track the median.

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

3. Em Bông rất thích nghịch cửa. Ở hội trường có 20 cái cửa được đánh số từ 1 đến 20. Lần đầu, em mở hết chúng ra. Lần thứ 2, em đóng tất cả các cửa có số chia hết cho 2. Lần 3, em lật (đóng thì mở mà mở thì đóng) hết các cửa có số chia hết cho 3. Cứ như thế sau 20 lần. Sau lần thứ 20, khi này em Bông lật cửa có số 20, thì có bao nhiêu các cửa đang mở?

4. How many ways are there to split a dozen people into 3 teams, where one team has 2 people, and the other two teams have 5 people each?


## Database

Courses: courseId*, courseName, teacherId

Teachers: teacherId*, teacherName

Students: studentId*, studentName

StudentCourses: courseId*, studentId*

1. Write a query to get a list of all students and how many courses each student is enrolled in.

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
