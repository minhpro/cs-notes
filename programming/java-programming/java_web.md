# Java Web Programming

Một số task cần làm khi lập trình web
* Build server: HTTP servers
* Setup request mappings
* Hỗ trợ Request filters (hay gọi là middlewares), một số middleware cần có như
  * Request session (thường liên quan đến HTTP Cookie header)
  * Security
  * Exception handler
  * CORS
  * Request Logging
  * IP filter
  * Request limit
  * Your custom middlewares
* Template engine: fill dữ liệu vào một template thường là HTML để trả về cho client
* REST API support (with JSON serialize and deserialize)
* Database
  * ORM and Non-ORM
  * SQL and No-SQL

Có khá nhiều frameworks hay libraries hỗ trợ lập trình Web với Java
  * Netty
  * Undertow
  * Tomcat
  * Jetty
  * Spring framework
  * Ktor
  * Vert.x
  * Thymeleaf
  * JacksonXML
  * Hibernate


Có hai hướng để lập trình Web với Java.
* Code web the chuẩn Servlet
* Pick một thư viện, sử dụng API của thư viện và theo đó build một HTTP server và setup request mappings cũng như request handlers của thư viện đó.

Sau đây là một ví dụ sử dụng hướng thứ 2

## Web programming with Undertow


```Java
Undertow server = Undertow.builder()
        .addHttpListener(8080, "localhost")
        .setHandler(new HttpHandler() {
            @Override
            public void handleRequest(final HttpServerExchange exchange) throws Exception {
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                exchange.getResponseSender().send("Hello World");
            }
        }).build();
server.start();
```

Programming theo một thư viện, ví dụ ở trên là Undertow, sẽ cần phải setup
* Connection listener (HttpListener)
* Handler chain (a chain of requests handlers)

**Listeners** đóng vai trò là entrypoint của một Undertow application. Tất cả incomming requests sẽ đi qua một listener. Listener sẽ translate một request sang một instance **HttpServerExchange**, pass instance này qua cho một chain of Handlers và rồi turning kết quả thành một response để sent back to the client.

Undertow supports HTTP 1.1, HTTP2 listeners. HTTPS được cung cấp bằng cách sử dụng HTTP listener với SSL enabled connection.

**Handler** sẽ xử lý **HttpServerExchange** instance. Các handlers thường được chained togother để thực hiện toàn bộ chức năng của một server.

```Java
public interface HttpHandler {
  void handleRequest(HttpServerExchange exchange) throws Exception;
}
```

Các chain handler là giống như cách tạo một tree, có root handler, sau đó đến có handler con, và cuối cùng là các handler lá (handler không có next handler). Một ví dụ cho handler không phải là handler lá (leaf handler).

```Java
public class SetHeaderHandler implements HttpHandler {

    private final HttpString header;
    private final String value;
    private final HttpHandler next;

    public SetHeaderHandler(final HttpHandler next, final String header, final String value) {
        this.next = next;
        this.value = value;
        this.header = new HttpString(header);
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().put(header, value);
        next.handleRequest(exchange);
    }
}
```

Chi tiết kiến luồng xử lý request, tham khảo tài liệu **Request Lifecircle** của Undertow:
- https://undertow.io/undertow-docs/undertow-docs-2.1.0/undertow-request-lifecycle.html

Danh sách các built in Handlers:
- https://undertow.io/undertow-docs/undertow-docs-2.1.0/index.html#built-in-handlers-2

Các khái niệm **Filter**, **Middleware** trong các mô hình lập trình khác có thể được coi là **Handler** trong Undertow.


## Http Programming with Netty

References:
- https://github.com/fmarchioni/mastertheboss/tree/master/netty/http-server
- https://www.alibabacloud.com/blog/598081
- Netty REST API example: https://github.com/linuradu/netty-rest-api/tree/master

# Using Servlet API

References:
- Undertow Servlet: https://undertow.io/undertow-docs/undertow-docs-2.1.0/index.html#undertow-servlet
- Spring MVC Rest with embedded Undertow: https://github.com/yarosla/spring-undertow
- Jetty embedded and servlet: https://www.baeldung.com/jetty-embedded