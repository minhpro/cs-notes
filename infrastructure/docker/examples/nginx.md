## Nginx with Docker

Start container to serve some content

```sh
docker run --name some-nginx -d \
  -v "$(pwd)"/html-content:/usr/share/nginx/html \
  -p 8088:80 \
  nginx
```
