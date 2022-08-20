## Bài toán

Bắt đầu hành trình với bài toán sau đây:

```
Một trang bán hàng online cần lưu trữ thông tin sản phẩm, thông tin khách hàng và thông tin bán hàng:

Thông tin nhà cung cấp (provider):
* id: id của nhà cung cấp
* name: tên nhà cung cấp
* desctiption: mô tả về nhà cung cấp
* address: địa chỉ nhà cung cấp
* status: đang hoạt động hay ngừng hoạt động

Thông tin sản phẩm:
* id: id của sản phẩm
* name: tên sản phẩm
* description: mô tả về sản phẩm
* price: giá sản phẩm (đơn vị VND)
* mfg_date: ngày sản xuất (manufactoring)
* exp_date: ngày hết hạn (có thể không có)
* total: số lượng còn lại
* status: đang bán hay ngừng kinh doanh

Thông tin sản phầm thuộc nhà cung cấp nào:
* sản phẩm
* nhà cung cấp

Thông tin khách hàng:
* id: mã khách hàng
* name: tên khách hàng
* phone_number: số điện thoại
* email: địa chỉ email (có thể không có)
* note: thông tin bổ sung
* status: đang hoạt động hay không

Thông tin đơn hàng
* id: mã đơn hàng
* đơn hàng của khách hàng nào
* address: địa chỉ giao hàng
* total: tổng số tiền cần thanh toán
* status: đang chuẩn bị, đang giao hàng, hay đã giao hàng
* paid: đã thanh toán chưa

Thông tin đơn hàng gồm sản phẩm nào:
* đơn hàng
* sản phẩm
* số lượng mua

Việc quản lý trên giấy tờ hay trên file (excel) khá là vất vả. Vậy hãy thử sử dụng cơ sở dữ liệu SQL để quản lý thông tin của trang bán hàng này xem sao.
```

Để sử dụng được SQL, trước hết hãy xem xét mô hình lưu trữ dữ liệu SQL

## Mô hình lưu trữ dữ liệu

Dữ liệu được lưu vào các bảng (table), mỗi bảng sẽ lưu trữ một loại dữ liệu nhất định. Chúng ta có bảng lưu trữ thông tin:
* provider: nhà cung cấp
* product: sản phẩm
* customer: khách hàng
* product_order: đơn hàng

Để định nghĩa bảng lưu trữ thông tin của **provider**, chúng ta sẽ định nghĩa các trường (**fields**) thuộc tính của bảng tương ứng với các trường thuộc tính của **provider**. Ví dụ định nghĩa bảng **provider** như sau (chi tiết cú pháp sẽ tìm hiểu sau):

TABLE provider {
    id: integer,
    name: string,
    description: string,
    address: string,
    status: string
}

Mỗi trường (**field**) sẽ gồm:
    * tên trường: id, name, address, ...
    * kiểu dữ liệu: string, integer, date, boolean, ...
    * và một số thông tin khác: có bắt buộc hay không, có duy nhất không, ... (tìm hiểu sau)

Mỗi dữ liệu nhất định được lưu vào bảng sẽ được gọi là một **row**, ví dụ hiện tại có 10 nhà cung cấp, như vậy bảng **provider** sẽ có 10 **row**.

## Khóa chính, khóa ngoài và mối quan hệ

### Khóa chính

Mỗi **provider** sẽ có thể được định danh hay phân biệt bởi một hay nhiều trường thuộc tính. Ví dụ mỗi nhà cung cấp sẽ phân biệt bởi trường **id**. Một trường hay tổ hợp nhỏ nhất các trường dùng để phân biệt các **row** dữ liệu được gọi là khóa chính của bảng - **primary key**.

Khóa chính của bảng **provider** sẽ là (id), ký hiệu trong SQL sẽ là:

`PRIMARY KEY (id)`

Đôi khi khóa chính sẽ gồm nhiều trường, ví dụ bảng **book** lưu trữ thông tin sách sẽ gồm các trường sau:

TABLE book {
    title: string,
    edition: integer,
    year: integer,
    price: integer,
    description: string
}

Khóa chính của bảng **book** sẽ là tên (title) và lần tái bản thứ mấy (edition):

`PRIMARY KEY (title, edition)`

Sẽ không tồn tại 2 row (book) có cùng **title** và **edition** được.

### Khóa ngoài và quan hệ

Ta sẽ định nghĩa bảng sản phẩm như sau:

TABLE product {
    id: integer,
    name: string,
    description: string,
    price: integer,
    mfg_date: date,
    exp_date: date,
    total: integer,
    status: string
}

Để lưu thông tin sản phẩm (product) thuộc nhà cung cấp (provider), không nhất thiết phải lưu tất cả các thông tin của **provider** và bảng **product** mà chỉ cần lưu khóa chính **PRIMARY KEY** của **provider** vào bảng **product** là được. Hãy xem xét ví dụ sau:

Một provider là:
* id = 5
* name = Công ty trách nhiệm hữu hạn TNT
* description = Chuyên bán buôn bán lẻ điện thoại thông minh
* address = 1102 Duy Tân, Hà Nội

Một product là:
* id = 1
* name = Iphone 13
...
* provider_id = 5

Giá trị **provider_id** = 5 thể hiện provider của product này có **id** = 5. Trường **provider_id** được gọi là khóa ngoài **FOREIGN KEY** liên kết đến khóa chính của bảng **provider**. Khóa ngoài được định nghĩa như sau:

TABLE product {
    id: integer,
    name: string,
    description: string,
    price: integer,
    mfg_date: date,
    exp_date: date,
    total: integer,
    status: string,
    provider_id: integer FOREIGN KEY REFERENCES provider(id)
}

Hai bảng **product** và **provider** gọi là có quan hệ một-nhiều, cụ thể là một provider có nhiều product, nhưng không phải ngược lại.

Ngoài quan hệ một-nhiều thì còn có quan hệ:
* nhiều-nhiều: ví dụ, sách thì có thể được viết bởi nhiều tác giả, và một tác giả thì có thể có nhiều cuốn sách.
* quan hệ gồm nhiều bảng
