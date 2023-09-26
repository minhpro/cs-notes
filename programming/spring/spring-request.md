## Mô tả cách Spring MVC xử lý một request

* Client (web browser) gửi request
* Ban đầu `FrontController` sẽ tiếp nhận request và lựa chọn `Controller` tương tác để xử lý request
* `Controller` xử lý request (tương tác database, service khác) rồi trả về dữ liệu `Model` kèm theo tên `View` sẽ hiển thị
* `FrontController` sẽ chọn `View` tương ứng để render response trước khi gửi lại cho client


## Vậy `FrontController`, `Controller`, `View` là gì?

`FrontController` ở đây là `DispatcherServlet` là một `Servlet` thực hiện mapping giữa request với `Controller`.

`Controller` là class sẽ trực tiếp xử lý request, thực hiện validate, gọi service thực hiện logic business.

`View` thực ra gọi là `View Resolver` thì đúng hơn, sẽ thực hiện sẽ render trang web từ file template (html thymeleaf) và data (dữ liệu - `Model`) do `Controller` trả về.

## Vậy cấu hình mapping giữa request với Controller được thực hiện như thế nào?

* Sử dụng annotation `@RequestMapping` cho `Controller`
* Annotation được sử dụng ở class hay method của `Controller`

Các thông tin có thê khai báo khi dùng annotation `@RequestMapping` là gì ?

* method là gì: GET, POST, PUT, ...
* path pattern: là pattern (explicit or regular expression) mapping path của request với Controller.

Còn có cách khác không?

* Sử dụng annotation khác `@GetMapping`, `@PostMapping`, `@DeleteMapping`, ... (tương ứng cho các method GET, POST, DELETE, ...)

Còn có cách khác không?

* Cấu hình thủ công trong file xml sử dụng `servlet-mapping`

## Làm thế nào để ghi log lại thông tin request mà server nhận

Ghi lại log request ví dụ như: request nào, tham số là gì, ai thực hiện?

* Sử dụng `Filter`

`Filter` là gì? Nó hoạt động khi nào?

* `Filter` là class được sử dụng được manipulate (sửa đổi) hay block (chặn) request. Và nó cũng được để manipulate hay block response trước khi tới client

* Nó hoạt động trước khi request tới `Controller` và trước khi response tới client.

