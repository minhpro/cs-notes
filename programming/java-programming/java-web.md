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
* Code web theo chuẩn Servlet
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

Servlet API spec được mô tả theo tài liệu https://jakarta.ee/specifications/servlet/6.0/

Servlet hỗ trợ tạo dynamic content, nó tương tác với client thông qua mô hình request/response Theo đó trong mô hình lập trình web với công nghệ Servlet thì Web server (HTTP server like Tomcat, Jetty, Undertow, ...) sẽ có một phần chức năng gọi là **servlets engines* (hay servlets container) dùng để quản lý và cung cấp các chức năng của servlets.

Để hiểu các thành phần phối hợp hoạt động với nhau như thế nào, hãy xem một luồng xử lý request như sau:

1. A client (e.g., web brower) gọi một HTTP request.
2. The request được nhận bởi web server, sau đó được chuyển qua cho servlet container. The servlet container có thể được chạy cùng process với web server, hay ở process khác, cùng host hay khác host.
3. The servlet container xác định servlet nào sẽ xử lý request dựa trên cấu hình mapping servlet của nó, và sau đó gọi servlet kèm theo tham số là một cặp request/response object.
4. The servlet sẽ phân tích thông tin request, xử lý, tạo dữ liệu trả lời, và send back cho client thông qua response object.
5. Khi servlet kết thúc xử lý request, the servlet container sẽ đảm bảo response được flushed hợp lý, và returns control back cho the host web server.

References:
- Undertow Servlet: https://undertow.io/undertow-docs/undertow-docs-2.1.0/index.html#undertow-servlet
- Spring MVC Rest with embedded Undertow: https://github.com/yarosla/spring-undertow
- Jetty embedded and servlet: https://www.baeldung.com/jetty-embedded
- https://jakarta.ee/specifications/servlet/6.0/

## The Servlet Interface

Trung tâm của Servlet interface là hàm `service`

```Java
public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException;
```

Tuy nhiên, đa phần chúng ta sẽ không implement trực tiếp Servlet interface, mà thay vào đó là extends **HttpServlet** class - đã cung cấp một số tiện ích, khi này thay vì implement **service** method thì chúng ta thực hiện override các methods (based on HTTP method) của HttpServlet class:

- `doGet` for handling HTTP `GET` requests
- `doPost` for handling HTTP `POST` requests
- `doPut` for handling HTTP `PUT` requests
- `doDelete` for handling HTTP `DELETE` requests
- `doHead` for handling HTTP `HEAD` requests
- `doOptions` for handling HTTP `OPTIONS` requests
- `doTrace` for handling HTTP `TRACE` requests

Phần lớn thì khi lập trình, chúng ta hay sử dụng 2 method là `GET` và `POST`, do đó sẽ thường xuyên override 2 methods `doGet` và `doPost`.

Ví dụ một Servlet như sau:

```Java
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-type", "text/plain");
        resp.getWriter().println("Hello World!");
    }
}
```

Cách xử dụng Servlet trong Undertow:

```Java
public class AppServlet {
    final static String CONTEXT_PATH = "/myapp";
    public static void main( String[] args ) throws ServletException {
        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(AppServlet.class.getClassLoader())
                .setContextPath(CONTEXT_PATH)
                .setDeploymentName("myapp")
                .addServlets(
                        Servlets.servlet("HelloServlet", HelloServlet.class)
                                .addInitParam("message", "Hello World")
                                .addMapping("/*"),
                        Servlets.servlet("MyServlet", HelloServlet.class)
                                .addInitParam("message", "MyServlet")
                                .addMapping("/myservlet")
                );
        DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(servletBuilder);
        deploymentManager.deploy();

        HttpHandler servletHandler = deploymentManager.start();
        PathHandler pathHandler = Handlers.path(Handlers.redirect(CONTEXT_PATH))
                .addPrefixPath(CONTEXT_PATH, servletHandler);

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(pathHandler)
                .build();
        server.start();
    }
}
```

Mô tả các bước trong đoạn code trên như sau:
- Đầu tiên là tạo `DeploymentInfo` chứa thông tin các Servlets và thông tin mapping.
- Sử dụng `DeploymentManager` tạo ra `Handler` gọi là **servletHandler** từ thông tin `DeploymentInfo`.
- Tạo một `PathHandler` đóng vai trò như root Handler (root of the tree of Handlers), kế tiếp nó là **servletHandler** (thông qua method `addPrefixPath`).
- Start Undertow server với Handler là `pathHandler`.

Tham khảo code [Undertow web](./my-web-app/src/main/java/com/example/App.java)

Cách setup Servlet trong mỗi thư viện sẽ khác nhau, nhưng cơ bản thì đều là tạo ra sevlet mapping. Xét ví dụ với Jetty như sau:

```Java
Server server = new Server();
ServerConnector connector = new ServerConnector(server);
connector.setPort(8090);
server.addConnector(connector);

// Create a ServletContextHandler with contextPath.
ServletContextHandler context = new ServletContextHandler();
context.setContextPath("/myapp");
// Link the context to the server.
server.setHandler(context);

// Add the Servlet implementing the cart functionality to the context.
ServletHolder servletHolder = context.addServlet(HelloServlet.class, "/hello/*");
// Configure the Servlet with init-parameters.
servletHolder.setInitParameter("name", "MyServlet");

server.start();
```

Tham khảo code [Jetty web](./my-web-app/src/main/java/com/example/AppJetty.java)

## Asynchronous Servlet

References:

- https://github.com/eugenp/tutorials/blob/master/libraries-server/src/main/java/com/baeldung/jetty/AsyncServlet.java
- https://jakarta.ee/specifications/servlet/6.0/jakarta-servlet-spec-6.0#asynchronous-processing
