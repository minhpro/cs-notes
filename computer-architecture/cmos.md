## Transistor

Transistor là thiết bị bán dẫn (semiconductor) hoạt động như là một controlled switch hay một amplifier control (thiết bị khuếch tán tín hiệu - time-varying current or voltage). Transistor điều khiển dòng điện giống như cách vòi nước điều khiển dòng nước trong nhà bạn. Với vòi nước, bạn sử dụng khoá để điều khiển dòng nước. Với transistor, một mức nhỏ voltage và/hoặc current (dòng điện) được áp dụng vào một terminal - control lead sẽ điều khiển một dòng điện lớn hơn đi qua hai terminals - other leads khác. Như vậy mọi transistor sẽ có 3 terminal, một dùng để điều khiển, còn lại để cho dòng chạy qua.

Có 2 nhóm transistor là: Bipolar transistor và Field-effect transistor (FET). Trong khi Bipolar cần một dòng doping tại control lead thì FET sử dụng một voltage - không dòng. Đại diện cho FET là MOSFET. Chúng ta sẽ tìm hiểu Bipolar trước.

![bipolar_npn](img/bipolar_npn.png)

Một NPN transistor được cấu tạo như hình vẽ, gồm ba thành phần bán dẫn: bán dẫn loại P ở giữa 2 bán dẫn loại N. Tuỳ theo nguyên tố được pha trộn vào Silicon mà hình thành bán dẫn loại N hay P. Nếu sử dụng Photpho - có hoá trị 5 - thì trong chất bán dẫn sẽ có electron tự do dư thừa tạo nên loại N (Negative), còn sử dụng Bo - có hoá trị 3 - thì trong chất bán dẫn sẽ có lỗ trống điện tích dương, từ đó thu hút electron tạo nên loại P (Positive).

Bình thường dòng điện sẽ không thể đi qua được transistor, E và C không kết nối. Khi có một dòng nhỏ có thể phá vỡ lực giữa vùng tiếp xúc N-P-N thì nó có thể cho dòng điện chạy qua transistor, E và C kết nối. IC/ IB thường = 100 hay tuỳ thuộc từng transistor.

## MOSFET

MOSFET - Metal Oxide Semiconductor Field Effect Transistor là một loại transistor sử dụng voltage để điều khiển (giảm điện trở và cho phép dòng điện chạy qua 2 cực) dòng điện chạy qua nó thay vì sử dụng một dòng điện khác như transistor thông thường (NPN, hay PNP, Base Emitter and Collector). Cũng do vậy MOSFET được coi là voltage-controlled switch.

## CMOS

CMOS - Complementary Metal-Oxide-Semiconductor used to product logic gates. 