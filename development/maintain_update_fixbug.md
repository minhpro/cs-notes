# Thay đổi hệ thống

Việc thay đổi hệ thống (thêm chức năng mới, sửa chức năng, sửa lỗi, cập nhật thư viện, cập nhật hệ điều hành, ...) cần chú ý đánh giá ảnh hưởng để đảm bảo:
- Chức năng hiện tại vẫn hoạt động bình thường.
- Các yêu cầu khác vẫn được đáp ứng (giao diện, hiệu năng, bảo mật, và tài nguyên sử dụng).

Việc đánh giá ảnh hưởng là cần thiết do một số lý do sau:
- Có được kế hoạch test lại các phần ảnh hưởng, do chúng ta ko thể bỏ ra công số để test lại toàn bộ hệ thống (toàn bộ hệ thống >> phần ảnh hưởng).
- Đưa ra giải pháp hợp lý để tiết kiệm chi phí test (giảm mức độ ảnh hưởng).

Có thể xác định mức độ ảnh hưởng hay phạm vị ảnh hưởng khi thay đổi hệ thống dựa trên các nguồn thông tin sau:
- Yêu cầu thay đổi.
- Giải pháp và code thay đổi.
- Thư viện hay phần mềm hệ thống (ngôn ngữ, compiler, hệ điều hành).

## Xác định phạm vi ảnh hưởng từ yêu cầu thay đổi

Trong quá trình phân tích yêu cầu, chúng ta có thể xác nhận một phần phạm vi ảnh hưởng. Tại sao cần bước này?
- Việc đánh giá phạm vi ảnh hường từ khi phân tích yêu cầu sẽ ảnh hưởng đến thiết kế giải pháp (nhằm tối ưu chi phí test) và kế hoạch test.
- Bước này có thể làm sớm để đưa ra kế hoạch phù hợp.

Cách tìm ra phạm vi ảnh hưởng:
- Chức năng mới hay phần cập nhật có thay đổi đến chức năng khác không?
    - Phải sửa chức năng khác hay không?
    - Phải sửa một phần trong chức năng khác hay không?
    - Phần phải sửa này lại có được sử dụng bởi các chức năng khác không?
    - Có thay đổi quyền hạn không?

Output của bước này dùng để:
- Thiết kế giải pháp hợp lý và tối ưu.
- Liên kế hoạch (phát triển và test) phù hợp.

## Xác định phạm vi ảnh hưởng từ giải pháp và code thay đổi.

Khi thiết kế giải pháp và code:
- Có cần sửa chức năng khác không?
    - Cần test lại.
- Code thay đổi?
    - Thay đổi cách xử lý (thuật toán, refactoring, clean code), không thay đổi logic xử lý?
        - Có thể dùng Unittest để kiểm chứng?
        - Nếu không được thì cần test manual các chức năng sử dụng code đã thay đổi này?
    - Thay đổi logic xử lý (input/output, logic xử lý)
        - Buộc phải test lại các chức năng sử dụng code đã thay đổi này?
- Cách xác định chức năng ảnh hưởng (có sử dụng) đến phần code thay đổi.
    - Dựa vào các công cụ lập trình (code editor, IDE, grep command) để tìm kiếm các file code, các function có sử dụng đến phần code thay đổi (class, function, biến). 
    - Tiếp tục truy ngược lại cho đến khi tìm thấy các function có thể test được, các function này, đối với code backend service, thường là APIs, message listeners (listen message từ các message queue như kafka, rabbitmq), schedulers (các hàm lập lịch). Đối với code frontend thì thường là file liên quan đến pages, router handlers. Từ đó suy ra các chức năng ảnh hưởng cần test lại.
    - Hướng dẫn tìm kiếm code usages của IntelliJ: https://www.jetbrains.com/help/idea/find-highlight-usages.html
    - Dùng `Find All References` của Visual Studio Code để tìm kiếm file code liên quan: https://github.com/Microsoft/vscode-tips-and-tricks#find-all-references
    - Tìm kiếm từ khóa (Cmd + Shift + F trên IntelliJ, VSCode) để xem các file sửa dụng biến môi trường, giá trị có thay đổi.
    - Ngoài sử dụng IDE, code editor thì có thể tìm kiếm theo từ khóa bằng tool `grep`. Ví dụ như tìm kiếm từ khóa được sử dụng trong thư mục hiện tại và tất cả thư mực con bằng command: `grep -r "keyword" .`
- Template [file điều tra ảnh hưởng](https://docs.google.com/spreadsheets/d/1ye8ti1ZXPiBFFlX5P4Fmec3cRyTUbdjz7fyba-6LC6E/edit?usp=sharing)

## Xác định phạm vị ảnh hưởng từ việc cập nhật thư viện và phần mềm hệ thống

Khi cập nhật hay nâng cấp thư viện, ngôn ngữ lập trình hay hệ điều hành, cần lưu ý các điểm sau để xác định được phạm vi ảnh hưởng và phạm vi test cần thực hiện:
- Version nâng cấp là major hay minor?
    - Thường thì các bản nâng cấp major sẽ chứa các thay đổi lớn, nên mức độ ảnh hưởng sẽ nhiều hơn.
- Phiên bản nâng cấp có hướng dẫn cập nhật (migration guideline) không?
    - Nếu có thì tham khảo để thực hiện nâng cấp và đánh giá phạm vi ảnh hưởng.
    - Các tài liệu migration thường có thông tin phạm vi ảnh hưởng và cách kiểm tra sau khi migration.
- Phiên bản nâng cấp có tài liệu release notes không?
    - Tài liệu release notes sẽ chứa thông tin thay đổi của các phiên bản.
    - Từ thông tin này chúng ta đánh giá phạm vi ảnh hưởng.
    - Đặc biệt chú ý đến các thông breaking changes, là những thay đổi phá vỡ tính tương thích so với phiên bản trước đó.

Việc nâng cấp như trên có thể cần phải sửa code hay không cần sửa code:
- Nếu phải sửa code của hệ thống hiện tại thì chúng ta quay lại phần code thay đổi để đánh giá thêm phạm vi ảnh hưởng.

Từ thông tin nâng cấp (migration, release notes, breaking changes) để xác định được phạm vi ảnh hưởng cần xác định được hệ thống hiện tại có đang xử dụng phần nào (hàm nào, chức năng nào) trong thông tin này không:
- Việc này có thể được làm bởi một số tips như sau:
    - Searching keyword từ tài liệu nâng cấp của thư viện (migration, release notes, breaking changes)trong docs giải pháp của dự án.
    - Searching keyword từ tài liệu nâng cấp của thư viện (migration, release notes, breaking changes)trong source code của dự án.
    - Dự theo hiểu biết source code của dự án, hiểu biết về thư viện, về hệ điều hành, ngôn ngữ lập trình, ... cần nâng cấp (phần này khá là heuristic).