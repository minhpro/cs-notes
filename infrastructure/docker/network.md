## Networking

Như đã đề cập, docker cho phép chúng ta chạy các container một các cô lập (isolation). Vậy làm thế nào để các container có thể giao tiếp với nhau?

Câu trả lời là **networking**, docker network giúp các container có thể giao tiếp với nhau và giao tiếp với host machine.

Trở lại với get-started phần https://docs.docker.com/get-started/07_multi_container/

Tạo một network

`docker network create todo-app`

Chạy một container và kết nối nó với network vừa tạo.

```sh
docker run -d \
   --network todo-app --network-alias mysql \
   -v todo-mysql-data:/var/lib/mysql \
   -e MYSQL_ROOT_PASSWORD=secret \
   -e MYSQL_DATABASE=todos \
   mysql:8.0
```

Chạy một container khác trong cùng network và thử kết nối với container `mysql`

```sh
docker run -it --network todo-app nicolaka/netshoot`
```

Dùng `dig` (DNS tool) để kiểm tra dns tới container `mysql`

`$ dig mysql`

Output sẽ cho biết IP của container mysql.

**Network drivers**

Hệ thống docker network được thiết kế có khả năng cấu hình plugin (pluggable) bằng cách sử dụng **drivers**. Docker cung cấp sẵn một số drivers:

* `bridge`: là driver mặc định. Brigde network được dùng khi cần chạy các containers trong một mạng riêng. Các containers có thể giao tiếp với nhau.
Các containers cũng có thể gọi network bên ngoài (external network).
* `host`: khi container được chạy dưới mode `host network` thì coi như loại bỏ sự cô lập về network với host machine và sử dụng host network trực tiếp.
* `overlay`: Overlay networks được dùng để kết nối các Docker daemons tạo thành *swarm servics* (tìm hiểu thêm ở nguồn khác để có thông tin chi tiết).
* `none`: Khi container được chạy và chỉ định network = `none` (--network none) thì có nghĩa là disable chức năng network. (chưa rõ là dùng khi nào).
* một số khác như `ipvlan`, `macvlan`: xem trên docs của Docker để có thông tin.

**IP và hostname**

Container lấy địa chỉ IP từ các Docker network mà nó được gán vào. Địa chỉ IP được cấp cho container được lấy từ IP pool của network mà nó tham gia.
Docker daemon đóng vai trò như DHCP server cho các container. Mỗi network cũng có sẽ có một default subnet mask và một gateway.

Khi một container start, nó chỉ có thể được gán vào một network (sử dụng `--network`). Sau đó sử dụng `docker network connect` để đính container vào
nhiều network. Trong cả hai trường hợp, có thể sử dụng tham số `--ip` hay `--ip6` để chỉ địa IP sẽ được gán cho container.

Mặc định container hostname sẽ là container id trong docker (cat /proc/sys/kernel/hostname). Để override hostname thì sử dụng option `--hostname`.

**DNS Services** 

Mặc định container sẽ kế thừa cấu hình DNS của host, được định nghĩa trong file `/ect/resolv.conf`. Các container được gán vào `bridge` network mặc
định (có tên là bridge), sẽ nhận một bản copy file cấu hình này. Các container tham gia các mạng `bridge` khác sẽ sử dụng một DNS server nhúng của Docker.
Server DNS nhúng này sẽ forward các `external` DNS lookup tới các DNS servers được cài đặt trên host machine. 

**Publish ports**

Để giao tiếp với container từ ngoài (outside world), khi chạy container cần publish port. Sử dụng cờ `--publish` hay `-p` để publish port ra ngoài
host machine.

* `-p 8080:80` Map TCP port 8080 từ Docker host với TCP port 80 trong container.
* `-p 8080:80/udp` Map UDP port 8080 từ Docker host với UDP port 80 trong container.
* `-p 198.162.1.100:8080:80` map TCP port 8080 từ Docker host với TCP port 80 trong container đối với các connection đến IP 198.162.1.100 trên host.
* `-p 8080:80/tcp -p 8080:80/udp` Map TCP port 8080 từ host với TCP port 80 trong container, UDP port 8080 từ host với UDP port 80 trong container.

## Sử dụng bridge network

Trong Docker, các container cùng kết nối tới một mạng bridge thì có thể giao tiếp với nhau, và cô lập với các container không kết nối vào mạng.

Mạng bridge áp dụng cho các container chạy trên máy cùng với Docker daemon. Muốn giao tiếp với cotnainer giữa các host thì dùng `overlay` network.

Khi chạy Docker, một mạng bridge mặc định được tạo ra tự động có tên là `bridge`. Khi chạy một container nếu không chỉ định network thì nó sẽ được
kết nối vào mạng bridge mặc định này. Chúng ta có thể tạo mạng bridge gọi là **user-defined bridge network**.

**Sự khác nhau giữa mạng bridge mặc định và user-defined**

* User-defined bridge cung cấp *automatic DNS resolution* giữa các container

Container trong mạng bridge mặc định chỉ có thể giao tiếp với nhau qua địa chỉ IP, trừ khi sử dụng `--link` (nhưng option này không được khuyến khích
sử dụng - legacy). Còn đối với mạng user-defined bridge thì các container có thể giao tiếp với nhau qua tên hay alias.

* User-defined bridge cung cấp khả năng cô lập tốt hơn

Khi chạy container mà không chỉ định network (`--network`) thì mặc định sẽ kết nối vào mạng bridge mặc định, điều này khá rủi ro vì các container
không liên quan có thể giao tiếp với nhau mà không có chủ đích.

* Container có thể được gán (attach) hay gỡ (detach) khỏi mạng user-defined bridge bất kỳ lúc nào. Còn với mạng mặc định thì không, bạn phải stop
container, tạo lại nó với mạng khác.

* Mỗi một mạng user-defined sẽ tạo một configurable bridge

Nếu container sử dụng mạng bridge mặc định thì khi cấu hình nó, tất cả các container khác sẽ sử dụng cùng cấu hình ví dụ như MTU, `iptables`. Thêm
vào đó cấu hình mặc bridge mặc định nằm ngoài Docker nên cần phải restart Docker.

Mạng bridge user-defined được cấu hình khi sử dụng `docker network create`. Nếu các nhóm container khác nhau yêu cầu cấu hình mạng khác nhau thì
nên tạo các mạng bridge và cấu hình chúng riêng rẽ.

**Quản lý các mạng bridge user-defined**

Để tạo một mạng bridge

`docker network create [OPTIONS] <name>`

Các options:

* `--subnet`: Subnet in CIDR format
* `--gateway`: IPv4 hay IPv6 Gateway cho subnet
* `--opt` hay `-o`: network driver options như là MTU, name (Linux bridge name)
* `--ipv6`: Enable Ipv6 network

**Kết nối container với network**

Kết nối lúc tạo container

```sh
docker create --name my-nginx \
  --network my-network \
  -p 8080:80 \
   nginx:latest
```

Kết nối một container đang chạy vào một network

```sh
docker network connect my-network my-ngninx
```

Disconnect một container đang chạy khỏi một network

```sh
docker network disconnect my-network my-nginx
```

## Tham khảo

https://docs.docker.com/network/

https://docs.docker.com/engine/tutorials/networkingcontainers/

