## Remote debug ứng dụng Java trong IntelliJ

### Cài đặt debugger

1. Từ main menu, chọn Run | Edit Configurations.

2. Hiện ra Run/Debug Configurations dialog, click Add New Configuration và chọn Remote

3. Thiết lập thuộc tính cho Configuration mới như sau

  * Name: Tên configuration
  * Host: address của máy được dùng để chạy ứng dụng: localhost hay 192.168.19.52
  * Command line arguments for remote JVM: -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005

4. Click Apply

### Cài đặt chạy ứng dụng

Chạy ứng dụng với tham số như sau

`java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 remote-app.jar`

Nếu bạn chạy bạn đã chạy docker container với biến môi trường (JAVA_OPTS)

`java $JAVA_OPTS -jar remote-app.jar`

Thì chỉ cần thêm biến môi trường, chẳng hạn thêm vào file `docker-compose.yml` phần `environment` như sau

```
environment:
    JAVA_OPTS: "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
    ...
ports:
    - 6005:5005
```

Rồi chạy lại container bằng lệnh `docker-compose up -d remote-debug-service`

Sau đó đổi cấu hình trong IntelliJ sang cổng 6005

`-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:6005`
