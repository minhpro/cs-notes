# Introduction to Spring MVC

Spring MVC sử dụng công nghệ Servlet, do đó bạn nên tìm hiểu trước về mô hình lập trình web với Servlet. Tài liệu [tham khảo](./java_web.md)

Về cơ bản một app Spring MVC sẽ có:
* Webserver: có thể là deploy riêng hay nhúng vào cùng app, ví dụ như Jetty, Tomcat.
* DispatcherServlet: thay vì tạo DeploymentInfo chứa thông tin mapping cho các Servlet thì Spring MVC sử dụng một **DispatcherServlet** đóng vai trò là cầu nối. Tất cả các request sẽ đi qua DispatcherServlet này, nó sẽ mapping request với Servlet (ở Spring MVC các Servlet này được tạo thông qua khái niệm Controller) tương ứng. Ngoài quả lý mapping servlet, DispatchServlet cũng quản lý **ViewResolver**, dùng để generate webcontent từ template như Freemarker, Thymeleaf.

# Setup Spring MVC

Setup một app Spring MVC sửa dụng server nhúng Jetty. Đầu tiên là khai báo dependency

```xml
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <!-- Jetty embedded -->
    <dependency>
      <groupId>org.eclipse.jetty.ee10</groupId>
      <artifactId>jetty-ee10-webapp</artifactId>
      <version>${jetty.version}</version>
    </dependency>
```

**References**
- https://www.baeldung.com/spring-mvc-handlerinterceptor-vs-filter