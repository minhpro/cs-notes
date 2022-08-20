# Giới thiệu Transaction

Trong hầu hết các hệ thống, chúng ta không đơn giản là chỉ có một người dùng tại một thời điểm. Nhiều người dùng có thể cùng đồng thời truy cập đến database (database management system - DBMS) tại một thời điểm từ đó dẫn đến bài toán xử lý đồng thời **concurrency control**.
* Chuyện gì xảy ra khi một người cập nhật trong khi người khác đọc cùng một dữ liệu?
* Nếu cả hai cập nhật cùng một dữ liệu?

<!-- Khi nhiều người cùng sử dụng database tại một thời điểm, sẽ có một số vấn đề phát sinh nếu chúng ta không đối ứng hợp lý:

* **Inconsitent Reads**: Người dùng chỉ đọc một phần những gì đã cập nhật
    * User1 update Table1, rồi update Table2.
    * User2 read Table2 (lúc này chưa được User1 update), rồi read Table1 (đã được User1 update), do đó User2 đã read database ở trạng thái trung gian (intermediate state).
* **Lost Update**: Hai người cùng update một bản ghi tại cùng một thời điểm, do đó một update sẽ bị mất.
    * User1 update price của một product thành price * 2
    * User2 update price của cùng product thành price + 5, và làm mất update của User1
* **Dirty Reads**: Người dùng đọc một giá trị update mà update này không được commit.
    * User1 update price của product, ví dụ price + 5, nhưng bị hủy giữa chừng (aborted).
    * User2 read giá trị price được update trước khi nó bị rolled back (khôi phục lại). -->

Để giải quyết bài toán **concurrency control** như mô tả phía trên, chúng ta sẽ định nghĩa một tập các luật **roles** và các đảm bảo **guarantees** cho các thao tác **operations** (read, write) tới database. Điều này được làm bằng cách sử dụng **transactions**. Một transaction là một chuỗi (sequence) các operations được thực thi như là một *single, logical, atomic* unit (đơn vị hợp nhất). Trạng thái (kết quả) trung gian (intermediate states) giữa các operations sẽ không khả dụng (invisible) với các transaction khác ở cùng thời điểm. Và nếu có lỗi nào đó xảy ra mà transaction không thể hoàn thành thì không có operations nào ảnh hưởng đến database - all-or-nothing operation. 

## Tính ACID

DBMS phải đảm bảo bốn tính chất quan trọng của transactions để lưu trữ và bảo vệ dữ liệu trước các vấn đề truy cập đồng thời **concurrent access** và lỗi hệ thống **system failures**.
* **Atomic**: Người dùng có thể xem việc xử lý mỗi transaction là **atomic** - tính nguyên tử. Tất cả các actions hoặc là được thực hiện hoặc không action nào. Và người dùng không cần bận tâm về ảnh hưởng của các transaction chưa hoàn thiện (incomplete), ví như khi hệ thống bị crash.
* **Consistency**: Mỗi transaction chạy độc lập phải đảm bảo tính toàn vẹn **consistency** (trạng thái đúng, hợp lệ) của database. Còn thế nào là toàn vẹn thì được định nghĩa bởi người dùng (người lập trình). Các constraints của database không đủ để bảo toàn tính toàn vẹn của database. Người dùng, người lập trình cần sử dụng transaction một các hợp lý để duy trì sự toàn vẹn của database. DBMS giả định rằng tính toàn vẹn được giữ cho từng transaction. Còn đảm bảo tính chất này là trách nhiệm của người sử dụng.
* **Isolation**: Các transactions là độc lập và được cô lập với sự ảnh hưởng của các transactions khác đang chạy đồng thời. Nghĩa là chúng ta có thể coi mỗi transaction đang chạy một mình mà không có transaction nào đồng thời chạy. DBMS sẽ lập lịch (scheduling) chạy các transactions đồng thời theo cách nhất định để đảm bảo tính chất này.
* **Durability**: Một khi DBMS phản hồi người dùng là transaction đã hoàn thành, thì các tác động và thay đổi của nó cần được lưu lại (persist) cho dù là hệ thống có crash trước khi tất cả thay đổi được phản ánh trên storage (disk). Tính chất này được gọi là độ bền vững - durability.

**Consistency và Isolation**

Người dùng có trách nhiệm bảo đảm tính toàn vẹn của database. Khi người dùng tạo một transaction thì cần đảm bảo khi chạy xong transaction đó thì database sẽ chuyển từ trạng thái toàn vẹn hiện tại sang trạng thái toàn vẹn mới. Xét ví dụ một transaction chuyển tiền từ tài khoản của A cho B cần đảm bảo tiêu chí toàn vẹn quan trọng như tổng số tiền cả các tài khoản là không đổi. Thao tác đầu tiên là trừ tiền (debit) ở tài khoản A, lúc này tạm thời để database ở trạng thái không toàn vẹn, sau đó cộng tiền (credit) vào tài khoản B. Nếu một chương trình lỗi luôn cộng tiền vào tài khoản B ít hơn 1 dollar so với việc trừ tiền ở tài khoản A, chúng ta không thể nào mong đợi là DBMS sẽ phát hiện ra trạng thái không toàn vẹn do lỗi trong xử lý logic của người dùng được.

Tính cô lập - isolation đảm bảo là dù các transactions được lập lịch xử lý xen lẫn nhau (interleaved) thì kết quả thay đổi tổng thể cũng giống như là chạy lần lượt từng transactions một. Ví dụ nếu hai transactions T1, T2 được chạy đồng thời thì tổng kết quả cũng giống như chạy T1, sau đó chạy T2 hoặc chạy T2, sau đó chạy T1.

**Atomicity and Durability**

Transaction có thể không được hoàn thành (incomplete) bởi các nguyên nhân như:
* Bị hủy giữa chừng - aborted hay kết thúc không thành công
* Hệ thống bị crash (có thể do nguồn điện bị ngắt) trong khi có transaction đang chạy.
* Transaction rơi vào tình huống không mong muốn (unexpected situations) kiểu như đọc phải dữ liệu không mong muốn hay không thể truy cập ổ cứng.

Khi transaction bị dừng giữa chừng, database sẽ ở trạng thái inconsistent - không toàn vẹn. DBMS phải tìm cách xóa bỏ các thay đổi của transaction không hoàn thiện (partial transactions) để đảm bảo tính toàn vẹn. Điều đó cũng có nghĩa là DBMS cần đảm bảo tính nguyên tử - atomicity của transactions. Hoặc tất cả các thao tác của transaction được thực hiện (carried out) hay không thao tác nào cả. DBMS đảm bảo tính nguyên tử bằng cách *undoing* các thao tác của incomplete transactions. Để làm điều này, DBMS sẽ lưu lại và quản lý các thao tác writes tới database gọi là *log*. Log cũng được dùng để đảm bảo tính bền vững - *durability*. Nếu hệ thống bị crash trước khi các thay đổi bởi một transaction hoàn thiện - completed transaction được lưu số ổ cứng, log sẽ được dùng để ghi nhớ và phục hồi các thay đổi khi hệ thống khởi động lại.

## Transactions và Lập lịch

Một transaction dưới góc nhìn của DBMS là một chuỗi các thao tác. Các thao tác bao gồm: đọc - **reads** và ghi - **writes** các đối tượng *objects* của database. Chúng ta quy ước các ký hiệu sau:

* Transaction T đọc object O as RT(O)
* Ghi sẽ là WT(O)
* Nếu ở ngữ cảnh rõ ràng, ta có thể bỏ T đi.

Ngoài thao tác đọc và ghi, mỗi transaction cần chỉ rõ một thao tác cuối cùng hoặc là **commit** (hoàn thành) hoặc là **abort** (hủy và undo tất cả các theo tác), ký hiệu tương ứng là *CommitT* và *AbortT*.

Một lịch xử lý các transactions là danh sách các thao tác (actions) (read, write, commit, abort) từ một tập các transactions, và danh sách này đảm bảo thứ tự các thao tác của từng transaction được giữ nguyên. Nghĩa là nếu action A1 trước A2 trong transaction T thì trong danh sách lập lịch A1 cũng trước A2. Ví dụ một lịch xử lý hai transactions như sau

|T1     |      T2|
|-------|--------|
| R(A)  |        |
| W(A)  |        |
|       | R(B)   |
|       | W(B)   |
| R(C)  |        |
| W(C)  |        |

Một lịch xử lý các transactions được gọi là **serializable schedule** nếu tổng thay đổi của nó là giống với việc thực hiện lần lượt (serial) các transactions. Ví dụ lịch sau là serializable

|T1     |      T2|
|-------|--------|
| R(A)  |        |
| W(A)  |        |
|       | R(A)   |
|       | W(A)   |
| R(B)  |        |
| W(B)  |        |
|       | R(B)   |
|       | W(B)   |
|       | Commit |
| Commit|        |

DBMS đôi khi có thể thực hiện các lịch không serializable nhưng vẫn đảm bảo kết quả giống serial bằng cách sử dụng các phương thức **concurrency control** hay cung cấp cho người dùng khả năng lựa chọn các lịch non-serializable hợp lý (dùng biện pháp **locking**).

## Concurrent Execution of Transactions

Thay vì xử lý tuần tự các transactions, chúng ta hướng tới xử lý đồng thời các transactions với các lý do như:
* Tránh chờ đợi I/O khi đọc ghi ổ cứng, trong khi chờ I/O từ transaction này ta có thể xử lý transaction khác.
* Tận dụng tài nguyên CPU có thể xử lý song song.
* Không nên để một transaction đợi quá lâu trong khi transaction khác được xử lý luôn và ngay.

Trong quá trình xử lý đồng thời các transactions, có thể xảy ra các bất thường, khi hai transactions cùng chạy thì chúng có thể xung đột với nhau:
* write-read (WR) conflict: T2 đọc data object được ghi trước đó bởi T1
* read-write (RW) và write-write (WW) conflicts được định nghĩa tương tự

**Reading Uncommited Data (WR Conflicts)**


Một transaction đọc dữ liệu đã được ghi bởi một transaction đồng thời nhưng chưa commit.

Ví dụ có 2 transaction đồng thời, T1 thực hiện transfer 100$ từ tài khoản A sang B và T2 thực hiện tăng giá trị của cả A và B thêm 6% (tiền gửi tiết kiệm - annual interest deposit). Giả sử T1 thực hiện đọc giá trị của A và giảm A đi 100$, sau đó T2 thực hiện tăng A và B thêm 6%, và rồi T1 thực hiện tăng B lên 100$.

|T1     |      T2|
|-------|--------|
| R(A)  |        |
| W(A)  |        |
|       | R(A)   |
|       | W(A)   |
|       | R(B)   |
|       | W(B)   |
|       | Commit |
| R(B)  |        |
| W(B)  |        |
| Commit|        |

**Nonrepeatable read (RW Conflicts)**

Một transaction đọc lại (re-read) dữ liệu mà trước đó nó đã đọc nhưng phát hiện ra là dữ liệu này đã bị thay đổi bởi một transaction khác (dữ liệu này đã được commit từ lần đọc đầu tiên).

Ví dụ A là tổng số bản copies của một cuốn sách ở thời điểm hiện tại. Một chương trình nhận yêu cầu về đơn hàng và đầu tiên nó đọc A và kiểm tra nếu A có lớn hơn 0 hay không sau đó thực hiện giảm giá trị của A (decrement). Giả sử tại cùng một thời điểm có 2 transactions T1 và T2. T1 đọc A và thấy giá trị là 1. T2 cũng đọc A và thấy giá trị là 1, sau đó nó giảm A còn 0 và commit. Tiếp đó T1 thử giảm A nhưng bị lỗi (do có ràng buộc A không thể nhỏ hơn 0).

**Overwriting Uncommited Data (WW Conflicts)**

Transaction T2 có thể ghi đè giá trị và trước đó T1 đã thay đổi nhưng chưa commit.

Ví dụ, có hai nhân viên A và B, và lương của họ được cần được giữ cho giống nhau. Transaction T1 cập nhật lương của họ thành $2000 và transaction T2 cập nhật lương của họ thành $1000. Nếu thực hiện T1 rồi T2 thì hai người đều có lương là $1000, nếu T2 rồi T1 thì lương của hai người đều là $2000. Cả hai trường hợp đều là chấp nhận được. Nhưng nếu T1 và T2 được thực hiện như sau:
* T1 cập nhật A = $2000
* T2 cập nhật B = $1000
* T2 cập nhật A = $1000
* T2 Commit
* T1 cập nhật B = $2000
* T1 Commit

Sau khi thực hiện xong, lương của A sẽ khác B ($1000 vs $2000)

## Lập lịch gồm aborted transactions

Serializable schedule chỉ định nghĩa với tập các commited transactions thôi. Lấy ví dụ sau khi có transaction abort.

Transaction chuyển tiền $100 từ A sáng B và transaction cộng tiền tiết kiệm T2 thêm 6% cho A và B và một lịch xử lý như sau:

T1 giảm A $100, T2 đọc A và B rồi thêm 6% tương ứng và commit, T1 abort.

|T1     |      T2|
|-------|--------|
| R(A)  |        |
| W(A)  |        |
|       | R(A)   |
|       | W(A)   |
|       | R(B)   |
|       | W(B)   |
|       | Commit |
| Abort |        |

Như vậy T1 đã bị Hủy nhưng T2 đã đọc giá trị của A mà T1 đã thay đổi. Để đảm bảo tính **isolation**, aborted transaction sẽ không ảnh hưởng đến transaction khác, chúng ta cần abort T2 cũng như các transactions đã đọc dữ liệu của T2, và cứ thế nữa, rồi sau đó thực hiện lại tất cả các transactions này. Nhưng T2 đã commit rồi nên không thể undo được, do đó ta nói rằng lịch trên là lịch không thể khôi phục **unrecoverable schedule**. 

Trong một lịch có thể khôi phục **recoverable** thì các transactions chỉ được commit khi và chỉ khi tất cả các transactions có thay đổi dữ liệu mà transaction hiện tại đọc commit. Nghĩa là T có đọc R(A) thì T chỉ commit khi tất cả transaction có W(A) commit.

Còn với lịch mà các transactions chỉ đọc dữ liệu đã commit của transactions khác thì có thể được hoàn thành mà không cần phải khôi phục liên hồi (cascading) các transactions liên quan. Lịch như vậy gọi là **avoid cascading aborts**.

## Locking-Based Concurrency Control

DBMS phải đảm bảo rằng chỉ có serializable, recoverable schedule được phép tồn tại và các thao tác của các commited transactions không bị mất khi thực hiện abort các transactions khác. Để đạt được điều này, DBMS thường sử dụng phương pháp locking. Phương thức locking được sử dụng phổ biến là *Strict Two-Phase Locking* hay *Strict 2PL*. Phương thức này có hai luật như sau:
1. Nếu một transaction T muốn *read* (tương tự *write*) một object, đầu tiên nó phải yêu cầu một **shared** (tương tự **exclusive**) lock trên object. Một transaction có một **exclusive** lock cũng có thể đọc object mà không cần thêm **shared** lock. Khi transaction yêu cầu một lock sẽ bị *suspend* (treo) đến khi DBMS có thể cấp lock. DBMS lưu lại các locks đang được cấp phát (granted) và đảm bảo là nếu một transaction đang giữ một exclusive lock trên một object thì các transaction khác không thể giữ a lock (shared hay exclusive) nào trên object này.

2. Tất cả các locks được giữ bởi một transaction được giải phóng (release) sau khi transaction kết thúc.

Việc lấy (acquire) hay giải phóng (release) locks có thể được tự động thêm vào các thao tác của transaction bởi DBMS, bạn có thể không cần lo lắng chi tiết như thế nào. Chúng ta sẽ tìm hiểu chi tiết cách lập trình viên có thể tùy chọn các thuộc tính của transaction và kiểm soát locking như thế nào.

Ký hiệu hành động T lấy một shared (tương ứng exclusive) lock trên object O là ST(O) (tương ứng XT(O)) và có thể bỏ qua T khi ngữ cảnh đã rõ ràng. Ví dụ một lịch xử lý đảm bảo Strict 2PL (có xen kẽ các thao tác)


|T1     |      T2|
|-------|--------|
| S(A)  |        |
| R(A)  |        |
|       | S(A)   |
|       | R(A)   |
|       | X(B)   |
|       | R(B)   |
|       | W(B)   |
|       | Commit |
| S(B)  |        |
| R(B)  |        |
| Commit|        |

Strict 2PL sẽ chỉ cho phép serializable schedule và như thế sẽ tránh được các vấn đề xung đột đã nêu ở trước.

### Deadlocks

Lấy một ví dụ sau. T1 có một exclusive lock trên A, T2 có một exclusive lock trên B. T1 yêu cầu một exclusive trên B và phải đợi, T2 yêu cầu một exclusive trên A và phải đợi. Như vậy cả hai transaction T1 và T2 đều đợi nhau dẫn đến **dead lock**. DBMS cần phải ngăn chặn hoặc phát hiện và giải quyết dead lock. Một cách đơn giản để giải quyết vấn đề dead lock là sử dụng timeout, nếu một transaction đợi lấy lock quá lâu, nó có thể giả định là đã có deadlock xảy ra và tự hủy.

### Locking và hiệu năng

Phương pháp locking được thiết kế để giải quyết vấn đề xung đột giữa các transactions và sử dụng hai cơ chế: *blocking* và *aborting*. Cơ chế block sẽ buộc transactions phải đợi, khi aborting và restarting một transaction đương nhiên là sẽ khá tốn kém. Trong thực tế dưới 1% các transactions gặp phải deadlock và phải hủy. Do đó hiệu năng phụ thuộc chủ yếu vào thời gian blocking.

Hiệu năng của thể được cải thiện bằng cách giảm khả năng blocking và giảm thời gian blocking transaction.
* Locking khối lượng nhỏ nhất các object có thể
* Giảm thời gian transaction giữ lock (như vậy transaction khác bị blocked ngắn hơn)
* Giảm **hot spots*. Hot pot là dữ liệu (object) thường xuyên được truy vấn và thay đổi, do đó nó sẽ gây ra nhiều blocking.

## Transaction trong SQL

