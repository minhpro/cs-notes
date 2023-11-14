## Overview

Docker Compose là một tool để chạy hệ thống (ứng dụng) gồm nhiều container được định nghĩa trong một `Compose file format`.
Một Compose file sẽ định nghĩa các container trong hệ thống được cấu hình như thế nào. Khi đã có Compose file thì việc tạo
và chạy hệ thống được đơn giản bằng một command `docker compose up`.

## Compose application model

Mô hình một compose application bao gồm:

* **Services**: mỗi một dịch vụ trong ứng dụng được định nghĩa là `Service`. Service là một khái niệm abstract được thể hiện
bằng cách chạy cùng một container image một hay nhiều lần.
* **Networks**: Các services có thể được chạy trong các network khác nhau. Nếu chạy cùng network thì chúng có thể giao tiếp
với nhau.
* **Volumes**: Mô tả dữ liệu (files, directory) trong service được mount như thế nào.
* **Config**:  Là phần dữ liệu cấu hình của services và thuộc kiểu chỉ đọc.
* **Secret**: Giống với Config, nhưng không được expose là bên ngoài, thường là chứa dữ liệu bảo mật.

Mỗi khi chạy (deploy) một application thì sẽ có một tham số là **Project name**. Project name được sử dụng để nhóm các resources
lại và cô lập với application khác và với cả các deploy khác (project name khác) cho cùng application (cùng Compose file).
Điều này cho phép chúng ta deploy một Compose file nhiều lần (với project name khác nhau) mà không cần thay đổi gì cả. 

Xem xét ví dụ mô tả

```
(External user) --> 443 [frontend network]
                            |
                  +--------------------+
                  |  frontend service  |...ro...<HTTP configuration>
                  |      "webapp"      |...ro...<server certificate> #secured
                  +--------------------+
                            |
                        [backend network]
                            |
                  +--------------------+
                  |  backend service   |  r+w   ___________________
                  |     "database"     |=======( persistent volume )
                  +--------------------+        \_________________/  
```

Ứng dụng trong ví dụ trên bao gồm các thành phần:

* 2 services, sử dụng Docker images lần lượt là `webapp` và `database`
* 1 secret (HTTPS certificate), được injected vào frontend service.
* 1 config (HTTP), được injected vào frontend service.
* 1 persistent volume, được gán vào backend service.
* 2 networks

```yaml
services:
  frontend:
    image: awesome/webapp
    ports:
      - "443:8043"
    networks:
      - front-tier
      - back-tier
    configs:
      - httpd-config
    secrets:
      - server-certificate

  backend:
    image: awesome/database
    volumes:
      - db-data:/etc/data
    networks:
      - back-tier

volumes:
  db-data:
    driver: flocker
    driver_opts:
      size: "10GiB"

configs:
  httpd-config:
    external: true

secrets:
  server-certificate:
    external: true

networks:
  # The presence of these objects is sufficient to define them
  front-tier: {}
  back-tier: {}  
```

Config và Secret được khai báo là `external`, nghĩa là chúng không được quản lý cùng với vòng đời của ứng dụng (application).

## Compose file

Compose file là một `YAML` file định nghĩa [services](#services), [networks](#networks), [volumes](#volumes), [configs](#configs),
và [secrets](#secrets). Đường dẫn mặc định tới Compose file là `compose.yaml` hay `compose.yml`, hay `docker-compose.yaml`và
`docker-compose.yml` trong thư mục làm việc (working directory).

Nhiều Compose files có thể được tổng hợp lại để định nghĩa một application model. Tổ hợp các YAML files phải được cài đặt bởi
appending/overriding YAML elements dựa trên thứ tự Compose files được quy định bời người dùng. 

* Simple attributes và maps sẽ bị overriden bởi Compose file có thứ tự cao nhất.
* Lists thì được merged bằng cách appending.

## Services

Một Service là định nghĩa trừu tượng cho một thành phần (dịch vụ, computing resource) trong một ứng dụng (hệ thống, application),
và có thể scaled/replaced độc lập với thành phần khác. Services là một tập các containers, được chạy theo cấu hình replication và
rằng buộc thay thế. Services được định nghĩa bởi một Docker image và một tập các tham số chạy (runtime arguments). Tất cả các containers
của cùng một server sẽ được tạo theo các tham số này.

Một Compose file phải khai báo một top-element `services`, là một map mà các key của nó là string thể hiện tên của các services.
Giá trị của các key sẽ là định nghĩa cho service.

Mỗi service có thể bao gồm một Build section, để định nghĩa các tạo Docker image cho service. (https://docs.docker.com/compose/compose-file/build/)

```yaml
services:
  frontend:
    image: awesome/webapp
    build: ./webapp

  backend:
    image: awesome/database
    build:
      context: backend
      dockerfile: ../backend.Dockerfile

  custom:
    build: ~/custom
```

Mỗi service định nghĩa rằng buộc và yêu cầu khi chạy (runtime constraints and requirements) các containers. Xem thông tin `deploy` section
(https://docs.docker.com/compose/compose-file/deploy/). Môt số cài đặt ví dụ như:

* `resources`
  * `limits`: Resource tối đa
  * `reservations`: Resource tối thiểu

```yaml
services:
  frontend:
    image: awesome/webapp
    deploy:
      resources:
        limits:
          cpus: '0.50'
          memory: 50M
          pids: 1
        reservations:
          cpus: '0.25'
          memory: 20M
```

* `restart_policy`: cấu hình khi nào thì restart containers khi chúng exit. Nếu không khai báo thì sử dụng cấu hình `restart` của service 

```yaml
deploy:
  restart_policy:
    condition: on-failure
    delay: 5s
    max_attempts: 3
    window: 120s
```

## Environment variables

**Thay thế biến (substitle) với `.env` file**

Sử dụng các biến trong file `.env` (cùng thư mục với `docker-compose.yml`) để thay thế các biến sử dụng trong Compose file.

```sh
$ cat .env
TAG=v1.5

$ cat docker-compose.yml
services:
  web:
    image: "webapp:${TAG}"
```

```sh
$ docker compose convert

services:
  web:
    image: 'webapp:v1.5'
```

Thay vì dùng file `.env`, có thể dùng các cách khác sau đây:

* `--file` (`-f`): `docker compose -f <path-to-compose-file> up`
* `--env-file`: `docker compose --env-file <path-to-env-file> up`
* `env_file`: thuộc tính trong Compose file `env_file: .env_xyz`

Hay là thay thế biến từ shell

```yaml
db:
  image: "postgres:${POSTGRES_VERSION}"
```

**Cài đặt biến môi trường cho services**

Sử dụng thuộc tính `environment` trong Compose file để set biến môi trường cho container của service. Cách thức này giống với
`docker run -e VARIABLE=VALUE ...`

```yaml
web:
  environment:
    - DEBUG=1
```

**Thứ tự xử lý biến môi trường**

Thứ tự ưu tiên từ cao đến thấp như sau:

* `docker compose run -e ...`
* Substitle từ `shell`
* Sử dụng thuộc tính `enviroment` trong Compose file
* Sử dụng `--env-file` trong `docker compose --env-file <path-to-env-file`
* Sử dụng thuộc tính `env_file` trong Compose file
* Sử dụng `.env` file trong cùng thư mục với Compose file

**Project name**

`COMPOSE_PROJECT_NAME` là biến được định nghĩa trước, giá trị là tên project. Giá trị mặc định là tên thư mục của project.
Có thể truyền project name bằng tham số `-p` (`docker compose -p <my_project_name> ...`)

## Sử dụng profile với Compose

Gán profile cho service trong Compose file

https://docs.docker.com/compose/profiles/

```yaml
version: "3.9"
services:
  frontend:
    image: frontend
    profiles: ["frontend"]

  phpmyadmin:
    image: phpmyadmin
    depends_on:
      - db
    profiles:
      - debug

  backend:
    image: backend

  db:
    image: mysql
```

Lệnh sau `docker compose --profile debug up` sẽ start services `backend`, `db` và `phpmyadmin`.

Có thể enable nhiều profiles `docker compose --profile frontend --profile debug up`

Với các service có khai báo `depends_on` thì mặc định các services phụ thuộc cũng sẽ được chạy dù có enable profile hay không.


## Networking trong Docker Compose

Mặc định, Compose cài đặt một `network` cho app. Mỗi container của một service sẽ join vào mạng mặc định này, và các container khác
có thể kết nối với chúng qua một hostname cũng là tên container. Tên network được đặt dựa trên "project name", mặc định là tên của
thư mục hiện tại. Có thể truyền "project name" qua tham số `--project-name` hay biến môi trường `COMPOSE_PROJECT_NAME`.


Xét ví dụ, thư mục app là `myapp`, và `docker-compose.yml` có nội dung sau:

```yaml
services:
  web:
    build: .
    ports:
      - "8000:8000"
  db:
    image: postgres
    ports:
      - "8001:5432"
```

Khi chạy `docker compose up` :

* Một network được tạo với tên `myapp_default`.
* Một container được tạo sử dụng cấu hình service `web` và join vào mạng `myapp_default` với tên `web`.
* Một container được tạo sử dụng cấu hình service `db` và join vào mạng `myapp_default` với tên `db`.

Mỗi container trong mạng có thể look up hostname `web` hay `db` và lấy được địa chỉ IP của container tương ứng.
Ví dụ `web` có thể connect tới `db` thông qua URL `postgres://db:5432` và sử dụng database Postgres.

Cần phân biệt `HOST_PORT` và `CONTAINER_PORT`. Trong ví dụ trên, với `db`, `HOST_PORT` là `8001` và 
`CONTAINER_PORT` là `5432` (cổng mặc định của Postgres). Service giao tiếp với service sử dụng `CONTAINER_PORT`.
Nếu `HOST_PORT` được khai báo thì service có thể được truy cập từ bên ngoài (giống với việc expose port).

Trong `web` container, có thể kết nối tới `db` thông qua `postgres://db:5432`, và từ host machine có thể dùng
`postgres://localhost:8001`.

**Chỉ định custom network**

Thay vì sử dụng một network mặc định, bạn có thể chỉ định network của mình với top-level element `networks`.
Cho phép bạn gom nhóm service trong hệ thống mạng phức tạp hơn cùng với các tùy chọn và tham số. Và bạn cũng
có thể join service vào một mạng bên ngoài không được quản lý bới Compose (mạng tạo ra trước đó rồi chẳng
hạn).

Mỗi service có thể chỉ định networks nào sẽ join vào bằng từ service-level element `networks`, element này
là danh sách tên các networks được khai báo ở top-level element `networks`.

Xét ví dụ sau, một Compose file định nghĩa 2 custom networks. `proxy` service được cô lập với `db` bởi vì chúng
không share chung một network. Chỉ có `app` là có thể giao tiếp với cả hai.

```yaml
services:
  proxy:
    build: ./proxy
    networks:
      - frontend
  app:
    build: ./app
    networks:
      - frontend
      - backend
  db:
    image: postgres
    networks:
      - backend

networks:
  frontend:
  backend:
    driver: bridge
    attachable: true
    ipam:
      config:
        - subnet: 172.28.0.0/16
          ip_range: 172.28.5.0/24
          gateway: 172.28.5.254
```

**Cấu hình mạng mặc đinh**

Bạn có thể tùy chỉnh mạng mặc định của Compose dưới element con `default` của top-level element `networks`.

```yaml
services:
  web:
    build: .
    ports:
      - "8000:8000"
  db:
    image: postgres

networks:
  default:
    # Use a custom driver
    driver: custom-driver-1
```

**Sửa dụng một mạng đã tồn tại**

Dùng từ khóa `external` để khai báo một mạng ngoài (mạng đã tồn tại).

```yaml
services:
  # ...
networks:
  network1:
    name: my-pre-existing-network
    external: true  
```

Tham khảo thêm:

* https://docs.docker.com/compose/compose-file/#networks-top-level-element
* https://docs.docker.com/compose/compose-file/#networks

## Volumes

Để lưu trữ dữ liệu của các services, Compose sử dụng `volumes`.

```yaml
services:
  database:
    image: awesome/database
    volumes: # Nested key. Configures volumes for a particular service.
      - db-data:/var/lib/awesome/data

  backup:
    image: backup-service
    volumes:
      - db-data:/var/lib/backup/data
volumes: # Top-level key. Declares volumes which can be referenced from multiple services.
  db-data:
```

Khi chạy, Compose sẽ tạo ra một volume có tên là `app_db-data` (giả sử project name là `app`), để chọn tên,
có thể khi báo key `name` là một entry của `db-data`. Volume được tạo sẽ mount vào 2 container của 2 services
sử dụng nó.

Để sử dụng volume đã tạo trước đó, sử dụng từ khóa `external`

```yaml
services:
  backend:
    image: awesome/database
    volumes:
      - db-data:/var/lib/awesome/data

volumes:
  db-data:
    external: true
```

Trường hợp muốn `bind mount` một thư mục bên ngoài host vào container của service thì dùng dường dẫn,
có thể là tuyệt đối hay tương đối.

```yaml
services:
  web:
    image: nginx
    volumes:
      - ./some/content:/usr/share/nginx/html
```

## Sử dụng Compose trong production

**Sửa Compose file cho production**

* Xóa mọi volume binding cho application code, code chỉ có thể nằm trong container và không thể thay đổi từ bên ngoài.
* Sử dụng port khác với port của host
* Cấu hình biến môi trường cho production
* `restart: always` cho service để tránh downtime
* Thêm các services khác như log aggregator, monitoring tool,...

Có thể định nghĩa thêm Compose file cho production `production.yml` để apply over file `docker-compose.yml`

`docker compose -f docker-compose.yml -f production.yml up -d`

**Cập nhật thay đổi**

Để cập nhật một service, build lại image và tạo lại container

```sh
docker compose build web
docker compose up --no-deps -d web
```

`--no-deps` để tránh tạo lại các service mà `web` phụ thuộc.

## Ví dụ

https://github.com/docker/awesome-compose/tree/master/official-documentation-samples/django

## Tham khảo

https://docs.docker.com/compose/compose-file/

https://github.com/docker/awesome-compose


