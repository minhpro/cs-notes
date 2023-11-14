## MySQL with Docker

Start container

```sh
docker run --name some-mysql -d \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=my-db \
  -e MYSQL_USER=myuser \
  -e MYSQL_PASSWORD=mypassword \
  -v "$PWD/init-data":/docker-entrypoint-initdb.d \
  -v some-mysql-vol:/var/lib/mysql \
  --network my-network \
  -p 3307:3306 \
  --restart always \
  mysql:8-debian
```

Where:

* MYSQL_ROOT_PASSWORD: biến bắt buộc, là mật khẩu của tài khoản `root`
* MYSQL_DATABASE: biến option, tên database khởi tạo khi tạo container
* MYSQL_USER, MYSQL_PASSWORD: biến option, tạo user và password, đồng thời gán quyền ALL với database `MYSQL_DATABASE`
(`GRANT ALL PRIVILEGES ON database_name.* TO 'username'@'%';`)
* `-v "$PWD/init-data":/docker-entrypoint-initdb.d`: mọi file `.sh`, `.sql`, `.sql.gz` trong `/docker-entrypoint-initdb.d`
sẽ được chạy khi lần đầu container được chạy.
* `/var/lib/mysql` là nơi lưu trữ dữ liệu của MySQL, cần mount vào một volumes (hay bind mount) để tránh mất dữ liệu
khi container bị remove.
* `mysql:8-debian`: mysql mặc định là `oracle-linux` (`oraclelinux:8-slim`), lựa chọn `debian` (`debian:bullseye-slim`) là bạn quen với cái nào hơn (dành cho trường hợp
muốn custom và inspect sau này, nếu không thì chọn gì cũng được). Nếu bạn chạy kiên trúc ARM (như máy Mac M1) thì buộc phải chọn mặc định.

**Dump database**

`$ docker exec some-mysql sh -c 'exec mysqldump --all-databases -uroot -p"$MYSQL_ROOT_PASSWORD"' > /some/path/on/your/host/all-databases.sql`

**Restore data from dump files**

`$ docker exec -i some-mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < /some/path/on/your/host/all-databases.sql`

## MySQL with docker compose

```yaml
services:
  mysql:
    image: mysql:8
    environment:
      - MYSQL_ROOT_PASSWORD=rootpassword
      - MYSQL_DATABASE=my-db
      - MYSQL_USER=myuser
      - MYSQL_PASSWORD=mypassword
       
```