## Tính cách

1. Tại sao bạn lại chọn theo công nghệ thông tin và nghề lập trình.

> Nói lên được lý do theo ngành theo nghề và thể hiện được đam mê.

2. Mô tả định hướng, kế hoạch trong tương lai gần và xa (vài năm nữa).

> Có được định hướng rõ ràng và cụ thể.

3. Bạn dành khoảng thời gian nào để học tập và nghiên cứu.

> Mức độ đầu tư

4. Mô tả phương pháp học tập và nghiên cứu của bạn.

> Mức độ đầu tư và có quan điểm riêng

5. Khi gặp phải một vấn đề khó khăn không thể giải quyết được, bạn sẽ làm gì?

> Khả năng tìm kiếm, mức độ kiên trì, có hướng giải quyết mới mẻ và đột phá.

6. Bạn đã làm việc cùng người khác chưa? Bạn thích làm một mình hay làm việc với một nhóm (>1)? Tại sao?

> Có khả năng làm việc độc lập hay có khả năng làm việc nhóm.

**Chú ý**: Với mọi câu hỏi, yêu cầu là trả lời
* Chân thật
* Không rập khuân máy móc
* Có suy nghĩ tự do của bản thân

## Kiến thức nền tảng

1. Thuật toán là gì? Thế nào là một thuật toán tốt?

> công thức, các bước giả quyết một vấn đề, phải kết thúc.
> thời gian, bộ nhớ, phức tạp hay đơn giản (khó hiểu hay dễ hiểu), dễ lập trình hay không

2. Đệ quy là gì? Điểm tốt và không tốt của thuật toán đệ quy?

> gọi lại chính nó, trong thân hàm lại có bước gọi lại chính hàm đó.
> ví dụ hàm giai thừa factorial(n) = n * factorial(n-1)
> tốt: dễ dàng mô tả thuật toán
> xấu: hiệu năng bị ảnh hưởng (do chiếm dung lượng bộ nhớ stack) // cái này cần hiểu sâu một chút

3. Phân biệt mảng và danh sách móc nối.

> Direct access vs sequential access
> Can be static allocation or dynamic allocation

4. Một cửa hàng bánh mỳ cần có một chương trình ghi nhận đơn đặt hàng. Chủ quán muốn hiển thị các đơn chưa được giao hàng sao cho đơn đặt sớm hơn thì ở phía trên. Nếu bạn được thuê để xây dựng chương trình này thì bạn lựa chọn lưu trữ danh sách đơn đặt hàng như thế nào?

>  Lưu bằng queue

5. Địa chỉ IP là gì? Port hay cổng được dùng để làm gì?

> Trong mạng máy tính, mỗi máy tính được gán với một địa chỉ IP duy nhất dùng để định danh. Lấy ví dụ địa chỉ IP (127.0.0.1, 198.16.1.29,...)
> Cũng như để tìm đến nhà của ai đó bạn cần biết địa chỉ nhà vậy.
> Trong một mạng máy tính, hai tiến trình ở hai máy tính khác nhau muốn trao đổi thông tin thì ngoài địa chỉ IP cần có thêm thông tin Port (số tự nhiên) để tránh nhầm với tiến trình khác (do trên một máy tính có rất nhiều tiến trình chạy đồng thời)
> Bạn muốn gửi thư cho bạn mình, ngoài địa chỉ nhà thì ngoài bì thư cần điền tên người nhận nữa nhé.

6. Web server là gì? Web tĩnh là gì? Web động là gì?

> Web server là chương trình phục vụ các yêu cầu HTTP. Chúng ta thường gửi các yêu cầu HTTP thông qua trình duyệt web và server sẽ trả về dữ liệu có thể là webpage (HTML, CSS, Javascript) hay files hay JSON, XML, ..
> Web tĩnh là trang web có nội dung không thay đổi không phụ thuộc vào thông tin người dùng. Ví dụ nginx phục vụ download file
> Web động là nội dung trang web được server tạo ra tuỳ biến theo tham số trong yêu cầu và thông tin người dùng cũng như trạng thái của server. Ví dụ trang thương mại điện tử.

7. Tại sao lại cần dùng database thay vì lưu dữ liệu trong file?

> Tránh dư thừa, đảm bảo toàn vẹn dữ liệu, chuẩn hoá thao tác, an toàn bảo mật

8. Lấy ví dụ về quan hệ 1-1, 1-nhiều và nhiều-nhiều trong thiết kế database.

> 1-1: Sản phẩm - Thông số chi tiết
> 1-nhiều: Sản phẩm - Hãng sản xuất
> nhiều-nhiều: Sản phẩm - Khách hàng

9. Có một cơ sở dữ liệu như sau:
Supplier(name, address) - khoá chính là name
Product(title, price, sname) - khoá chính là title, khoá ngoài là sname liên kết tới Supplier.name
Order(pname, quantity) - pname là khoá ngoài liên kết tới Product.name

* Viết truy vấn SQL lấy ra các sản phẩm có price > 1000 và kèm theo thông tin tên nhà cung (supplier).
* Viết truy vấn SQL lấy thông tin sản phẩm (product) bán được (order) nhiều nhất đối với từng Supplier.

## Ngôn ngữ

1. JVM là gì? Với mỗi chương trình Java sẽ cần chạy bao nhiêu JVM?

> Máy ảo Java dùng để chạy chương trình Java, tuỳ interviewer hỏi thêm
> 1

2. Một chương trình Java không có hàm main có chạy được không?

> No

3. Class là gì? Object là gì?

> Class là tập hợp các đối tượng có cùng tính chất và hành động (lấy ví dụ thực tế)
> Object là một đối tượng trong class (lấy ví dụ cụ thể)

4. Tính kế thừa là gì? Lớp con có được truy cập thuộc tính của lớp cha không?

> Lớp con có thể kế thừa thuộc tính và các phương thức của lớp cha, người ra có thể có thêm đặc tính riêng (lấy ví dụ)
> Yes (nếu thuộc tính ở lớp cha khai báo private thì sao?)

5. Interface được dùng để làm gì?

> Nhiều khi còn gọi một phương thức (hàm) nhưng ta lại muốn chúng hoạt động tuỳ theo đối tượng được gọi. Ví dụ ta muốn format văn bản trước khi gửi đi, chỉ có một hàm format nhưng nếu là XMLFormat thì sẽ cho đầu ra là XML, còn JSONFormat thì cho ra JSON. Khi đó ta sử dụng interface Format cho phương thức format còn các class XMLFormat hay JSONFormat sẽ thực thi interface Format. Tính chất đó gọi là đa hình hay polymorphism.

6. Phân biệt các từ khoá: puclic, protected, private, static

7. Trong java package được dùng để làm gì?

> Dùng để module hoá chương trình, để từ đó dễ dàng quản lý và phát triển.
