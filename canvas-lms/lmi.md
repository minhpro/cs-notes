# Learning Tools Interoperability

LTI là một chuẩn công nghệ về giáo dục được phát triển biển IMS Global Learning Consortium
https://www.imsglobal.org/activity/learning-tools-interoperability

Version hiện tại là 1.3:

Canvas hỗ trợ LTI từ đó cho phép tích hợp third-party hoặc làm phong phú thêm courses bằng các Web application khác và các resources khác trên Internet


## Canvas Integration

Có thể integrate thông qua Modules hay Pages của Course

https://www.youtube.com/watch?v=3uhjAm1nHWM

## Tích hợp external tools vào Canvas thông qua chuẩn LTI

https://canvas.instructure.com/doc/api/file.tools_intro.html

Các tools có thể được deployed theo course hay theo account level. Khi đã được cấu hình, các tools có thể được surfaced (liệt kê) như là external links trong course modules hay được sử dụng trong assignments khi tạo hay sửa assignments.

* external link: https://canvas.instructure.com/doc/api/file.link_selection_placement.html
* assignment selection: https://canvas.instructure.com/doc/api/file.assignment_selection_placement.html

Ngoài ra còn một số điểm integration points using LTI khác (xem Mục `Placements`)
* Navigation
* Homework Submission
* Editor Button
* Migration Selection
* Collaborations


Trước khi có thể tích hợp external tools vào Canvas thì, cần cấu hình `Configuration LTI Advantage Tools`

https://canvas.instructure.com/doc/api/file.lti_dev_key_config.html

Việc cấu hình được thực hiện ở cả hai phái là Canvas và Tools


https://github.com/IMSGlobal/ltibootcamp