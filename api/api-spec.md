## Create user API

Tạo một user mới cho hệ thống. Chỉ ADMIN có quyền sử dụng chức năng này.

### Authorization (Xác thực)

* type: Bearer token (hay là Basic auth hoặc API-KEY)

### Request

* Method: POST
* PATH: /users
* Headers:
    * Content-Type: application/json

#### Request body

* username
    * type: string
    * required: true
    * description: username được dùng để hiện thị trên hệ thống, tối thiểu 5 ký tự và tối đa là 150 ký tự
* email
    * type: string
    * required: true
    * description: email address dùng để login, cần phải là địa chỉ email hợp lệ và không được đăng ký trước đó
* password
    * type: string
    * required: true
    * description: mật khẩu login, cần là chuỗi tối thiểu 8 ký tự, có chứa cả ký tự hoa, thường và ký tự đặc biệt.

**Ví dụ**

```JSON
{
    "username": "demo user",
    "email": "demo@example.com",
    "password": "Abc#1234"
}
```

### Response

#### Tạo thành công

* Http Code: 200
* Body:
    * id
        * type: number
        * description: id của user vừa được tạo
    * username
        * type: string
        * description: username của user
    * email
        * type: string
        * description: email address của user

**Ví dụ**

```JSON
{
    "id": 100,
    "username": "demo user",
    "email": "demo@example.com"
}
```

#### Lỗi đầu vào

* Http Code: 400
* Body:
    * errorCode
        * type: string
        * description: mã lỗi
    * errorMessage
        * type: string
        * description: message lỗi

**Ví dụ**

```JSON
{
    "errorCode": "INVALID_EMAIL",
    "errorMessage": "Địa chỉ email không hợp lệ"
}
```

#### Lỗi user đã tồn tại

* Http Code: 400
* Body:
    * errorCode
        * type: string
        * description: mã lỗi
    * errorMessage
        * type: string
        * description: message lỗi

**Ví dụ**

```JSON
{
    "errorCode": "EXISTED_EMAIL",
    "errorMessage": "Email đã được đăng ký"
}
```
