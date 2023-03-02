## Storage

Khi container tạo, cập nhật hay xóa files, tất cả những thay đổi dữ liệu này là cô lập trong container và đều sẽ 
mất khi remove container.

Để lưu lại dữ liệu hay chia sẻ với container khác, docker cho phép bạn kết nối dữ liệu (directory - filesystem paths) 
của container với host machine - gọi là **mount**. Nếu một directory trong container được mounted, thì mọi thay đổi
trong directory cũng sẽ được lưu lại và thấy trên host machine. Nếu nhiều containers cùng mount dữ liệu tới cùng một
vị trí trên host machine thì sẽ thấy các files giống nhau, có thể coi đây là một cách share dữ liệu giữa các containers.

Có hai cách chính để để mount:

* Volume mounts
* Bind mounts

So sánh giữa hai loại mounts:

|                         | **Volume mounts**           | **Bind mounts**              |
|-------------------------|-----------------------------|------------------------------|
| Host location           | Docker chooses              | You decide                   |
| Mount example (--mount) | type=volume, src=my-volume, | type=bind,src=/path/to/data, |
|                         | target=/usr/local/data      | target=/usr/local/data       |
| Populates new volume    | Yes                         | No                           |   
| with container contents |                             |                              |
| Support volume drivers  | Yes                         | No                           |

Ví dụ:

* Volume mounts example: https://docs.docker.com/get-started/05_persisting_data/ 
* Bind mounts: https://docs.docker.com/get-started/06_bind_mounts/

Ngoài ra docker còn hỗ trợ lưu files in-memory của host machine, trên Linux gọi là *tmpfs* mount, Windows là *named pipe*.

![type_of_mount](types-of-mounts.png)

Nơi lưu trữ dữ liệu trên host machine của các loại mount là khác nhau:

* **Volumes** được lưu ở một phần của host filesystem, phần này được quản lý bởi Docker (`/var/lib/docker/volumes` trên Linux)
* **Bind mounts** được lưu trữ ở bất kỳ đầu trên host filesystem (tùy bạn chọn).
* `tpmfs` **mounts** được lưu trong memory của host machine và không bao giờ được lưu lại ở filesystem.

**Chi tiết hơn về các loại mounts**:

* `Volumes`: được tạo và quản lý bởi Docker. Bạn có thể tạo volume trực tiếp bằng command `docker volume create`, hoặc là Docker
sẽ tạo volume khi tạo container.

Khi bạn tạo một volume, nó được lưu trong một thư mục của Docker host. Khi mount volume này vào một container, thư mục này được
mounted vào container. Điều này giống với cách *bind mounts* hoạt động, như khác là được quản lý bởi Docker và cô lập với phần 
chính của host machine.

Một volume có thể được mount vào nhiều container cùng lúc. Khi không có container nào chạy thì volume vẫn còn, bạn có thể xóa
chúng bằng command `docker volume prune`.

Khi mount một volume, bạn có thể đặt tên **named**, nếu không Docker sẽ tạo tên ngẫu nhiên **anonymous** đảm bảo là duy nhất.

* `Bind mounts`: Khi bạn sử dụng một bind mount, một file hay một thư mục trên *host machine* được mount vào container. File
và thư mục được chỉ định bằng path của nó trên host machine, nếu không tồn tại thì sẽ được tạo.

* `tmpfs mounts`: Một *tmpfs* mount không được lưu trên filesystem của host machine hay trong container. Nó được sử dụng bởi
một container trong thời thời container chạy, được dùng để lứu dữ liệu mật. Ví dụ swarm service sử dụng *tmpfs* mounts để mount
*secrets* vào trong containers của service.

**Gợi ý sử dụng các loại mounts**

* Volumes được khuyến khích sử dụng để lưu trữ dữ liệu của containers.
* Sử dụng *bind mounts* trong quá trình phát triển app, khi đó bạn có thể mount source directory hay binary vào container.
* Sử dụng *tmpfs* mounts cho các trường hợp an toàn bảo mật hay hiệu năng.

**Lưu ý khi dùng bind mounts hay volumes**

* Nếu mount một **empty volume** vào một thư mục trong container, thì các files và thư mục con trong thư mục này sẽ được
copy vào volume. Nếu start một container và khai báo sử dụng một volume, nhưng volume không tồn tại thì một empty volume
sẽ được tạo.
* Nếu mount một **bind mount hay non-empty volume** vào một thư mục trong container và thư mục này tồn tại files cũng như
thư mục con, thì files và thư mục này sẽ bị che khuất bởi mount, giống với khi bạn lưu files vào `/mnt` trên Linux và mount
một USB vào `/mnt`. Nội dung của `/mnt` sẽ bị che khuất bởi nội dung của USB đến khi USB được unmounted. Các files bị che
khuất không bị remove hay sửa đổi, nhưng không thể truy cập khi *bind mount* hay *volume* được mount.

## Volumes

Volumes là cơ chế được khuyến khích để lưu trữ dữ liệu của containers. Trong khi `bind mounts` phụ thuộc vào cấu trúc thư mục
và OS của host machine, còn volumes thì hoàn toàn được quản lý bởi Docker. Các ưu điểm của volume so với bind mount:

* Volumes dễ dàng backup và migrate hơn
* Volumes có thể được quản lý qua sử dụng Docker CLI và Docker API
* Volumes hoạt động ở cả Linux và Windows containers.
* Volumes an toàn hơn khi share giữa các containers.
* Volume drivers cho phép lưu volume trên remote host hay cloud provider, cho phép encrypt data hay thêm chức năng khác.
* Volume mới có thể có nội dung để copy (pre-populated) từ một container.

Mount volume khi chạy container `docker run` bằng tham số (flag) `--mount` (hay `-v`, `--volume`, nhưng không được rõ ràng).

`--mount`: bao gồm nhiều cặp key-value, ngăn cách bới dấu phẩy `<key>=<value>`. Key bao gồm:

* `type` của mount, value có thể là `volume`, `bind`, hay `tmpfs`, với volume thì là `volume` (mặc định).
* `source` (`src`) của mount, với named volumes, đây là tên của volume. Với anonymous volume thì trường này không khai báo.
* `destination` (`dst`, `target`): khai báo path được mounted trong container (e.g. /var/lib/postgresql/data).
* `readonly` (`ro`) : nếu xuất hiện option này thì volume được mount readonly (xem chi tiết ở phần sau).
* `volume-opt`: option này có thể xuất hiện nhiều lần, giá trị là một cặp key-value thể hiện tên của option và giá trị của nó.

`-v` hay `--volume`: gồm 3 trường, phân cách bởi `:`, và phải theo đúng thứ tự

* trường đầu tiên là tên volume, với anonymous volume thì trường đầu tiên không có
* trường thứ hai là đường dẫn trong container
* trường thứ ba là option, một danh sách các options phân cách bới dấu phẩy, như là `ro`.

**Tạo và quản lý volume**

Tạo một volume

`docker volume create my-vol`

Liệt kê danh sách volume

`docker volume ls`

Inspect một volume

`docker volume inspect my-vol`

Xóa một volume

`docker volume rm my-vol`


**Start container với volume**

```sh
docker run -d \
   --name test \
   --mount source=my-vol,target=/app \
  nginx:latest
```

Inspect container để xem thông tin volume được mount ở mục `Mounts`

`docker inspec -f '{{ .Mounts }}'  test`


**Backup, restore, hay migrate data volumes**

*Backup một volume*

Giả sử, tạo một container tên `dbstore`

`docker run -it --mount dst=/dbdata --name dbstore ubuntu /bin/bash`

Chúng ta sẽ backup volume trên như sau:

`docker run --rm --volumes-from dbstore -v $(pwd):/backup ubuntu tar cvf /backup/backup.tar /dbdata`

* Chạy một container mới và mount tất cả volume từ `dbstore` vào container mới này.
* Mount thư mục hiện tại trên host machine vào container tại thư mục `/backup`
* Chạy command `tar` để compress nội dung của `/dbdata` vào file `backup.tar` trong thư mục `/backup`
* Khi này thư mục hiện tại của host machine sẽ có file `backup.tar`

*Restore volume từ backup*

Giả sử tạo một container mới là `dbstore2`

`docker run -it --mount dst=/dbdata --name dbstore2 ubuntu /bin/bash`

Sau đó untar backup file vào volume của container mới vừa tạo

`docker run --rm --volumes-from dbstore2 -v $pwd:/backup ubuntu bash -c "cd /dbdata && tar xvf /backup/backup.tar --strip 1"`


*Xóa volumes*

Xóa volume có tên

`docker volume rm <volume-name>`

Xóa anonymous volume tự động sau khi chạy container xong

`docker run --rm -v /foo -v myvol:/bar busybox top`

`--rm` sẽ xóa container sau khi chạy xong, anonymous volume (`/foo`) sẽ được xóa tự động, còn named volume (`myvol`) thì không.

Để xóa hết các volumes không được sử dụng:

`docker volume prune`

## Bind mounts

Khi sử dụng bind mount, một file hay một thư mục trên host machine được mout vào trong container.
File hay thư mục này để chỉ định bằng absolute path trên host machine và nếu chúng không tồn tại
thì sẽ được tạo mới.

Sử dụng bind mount khi chạy container với cờ `--mount`:

* `type`, có thể là `volume`, `bind` hay `tmpfs`. Ở đây sử dụng `bind`.
* `source` (`src`) là path của file hay directory trên host machine.
* `destination` (`dst`, `target`) là path nơi file hay thư mục được mount trong container.
* `readonly` (`ro`): nếu có option này thì bind mount sẽ là read-only.
* `bind-propagation`, nếu có sẽ thay đổi `bind propagation`: `rprivate`, `private`, `rshared`, `shared`,
`rslave`, `slave`.

**Start container với một bind mount**

```sh
docker run -d \
  -p 8080:80 \
  --name devtest \
  --mount type=bind,source="$(pwd)"/static-html-dir,target=/usr/share/nginx/html \
  nginx:latest
```

Inspect container và xem thông tin mount

`docker inspect -f '{{ .Mounts }}' devtest`

Nếu bind-mount một thư mục vào một thư mục có dữ liệu (non-empty) trong container, thì nội dung đã tồn tại
sẽ bị che mất bởi thư mục bên ngoài host machine. Đều này có lợi ích khi bạn muốn thử version mới của application
mà không cần build lại image. Cách thức hoạt động này lại khác so với `volumes`.

**Sử dụng read-only bind mount**

Với một số application, container cần update nội dung vào bind mount, khi đó các thay đổi được phản ánh lại trên 
host machine. Lúc khác thì container chỉ cần đọc dữ liệu thôi.

