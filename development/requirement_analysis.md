# Phân tích yêu cầu

Cần phân tích yêu cầu ra sao để có thể xử lý được (thiết kế giải pháp, thực hiện/code, test)?
- Xác định đầu vào gồm những thông tin gì?
- Xác định đầu ra là gì?
- Ai là người dùng?
- Liệt kê các trường hợp đầu vào và đầu ra cần kiểm chứng:
    - Thường sẽ có 2 nhóm trường hợp: thông thường và ngoại lệ.

Các yêu cầu khác (về giao diện, hiệu năng, bảo mật, và tài nguyên sử dụng).
- Phần này có yêu cầu có, có yêu cầu không.
- Yêu cầu về giao diện thì sẽ có, nhưng tuỳ mức độ chi tiết. 
- Yêu cầu bảo mật có nhiều mức, thông thường là phân quyền, ai có quyền thao tác.

# Ví dụ thực hành.

Sau đây là một yêu cầu:

```txt
Cung cấp chức năng đăng ký tài khoản.
```

Với yêu cầu trên thì chúng ta xác định: đầu vào, đầu ra và người sử dụng ntn?

Và để xác định được thì cần bổ sung thêm thông tin (qua điều tra, tìm hiểu hệ thống, đặt câu hỏi, và trao đổi).
- Tài khoản là gì?
- Tài khoản gồm những thông tin gì?
- Đăng ký tài khoản cần những thông tin gì? (đây chính là đầu vào)
    - Đầu vào là username và password?
    - Đầu vào là tài khoản mạng xã hội (hình thức đăng ký quan tài khoản bên thứ ba)?
    - Đầu vào là file csv chứa một loạt thông tin tài khoản?  
- Kết quả đăng ký tài khoản là gì? (đây chính là đầu ra)
    - Có phải là trả về http status 200 và kèm theo thông tin tài khoản vừa đăng ký không?
    - Nhận được email thông báo đăng ký tài khoản?
    - Có thể đăng nhập vào hệ thống?
- Ai là người sử dụng?
    - Bất kỳ ai? (Guest)
    - Người quản lý?

Liệt kê các trường hợp cần kiểm chứng:
- Thông thường
    - Thông tin tài khoản hợp lệ.
    - ...
- Ngoại lệ
    - Thông tin tài khoản đã đăng ký rồi.
    - Thiếu dữ liệu.
    - Không thể gửi email.
    - Với cái trường hợp ngoại lệ, thông thường là thông báo lỗi.

