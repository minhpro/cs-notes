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

Cách tạo DispatchServlet và kết nối với Server như sau:

```Java
// Create and register the DispatcherServlet
// It automatically calls ctx.refresh() properly, don't call it manually
DispatcherServlet dispatcherServlet = new DispatcherServlet(ctx);

// Create a ServletContextHandler with contextPath.
ServletContextHandler servletContextHandler = new ServletContextHandler();
servletContextHandler.setContextPath("/myapp");
// Add Spring DispatchServlet as the root servlet
servletContextHandler.addServlet(dispatcherServlet, "/*");

// Link the context to the server.
server.setHandler(servletContextHandler);
```

Sau đó cấu hình MVC configuration bằng cách extends `DelegatingWebMvcConfiguration` (hoặc dùng `@EnableWebMvc` annotation để load class này, sau đó implement `WebMvcConfigurer`).

```Java
@Configuration
public class WebConfig extends DelegatingWebMvcConfiguration {

    ApplicationContext applicationContext;

    public WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.freeMarker();
    }

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates/");
        return freeMarkerConfigurer;
    }
```

Như ví dụ trên, thì ngoài cấu hình mặc định cho Spring MVC thì chúng ta đã cấu hình ViewResolver là FreeMarker. Ngoài ra còn nhiều cấu hình khác và cần tham khảo thêm ở class `DelegatingWebConfiguration`, cũng như class cha của nó `WebMvcConfigurationSupport`.

Tham khảo 
- [code](./my-spring-app/src/main/java/my_group/web/WebConfig.java)
- Tài liệu cấu hình mvc-config: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config.html

Sau đó chúng ta có thể viết các hàm xử lý request trong Spring MVC, có 2 cách thông dụng để làm điều này:
- Sử dụng Annotated Controller classes (class with `@Controller` annotation): https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller.html
- Sử dụng Functional Endpoints: https://docs.spring.io/spring-framework/reference/web/webmvc-functional.html

Chúng ta sẽ tìm hiểu thêm về Annotated Controller, còn đối với web-functional thì sẽ tham khảo tài liệu ở link phía trên.

# Request mapping

Để định nghĩa request mapping, chúng tả sử dụng **PathPattern**:
- https://docs.spring.io/spring-framework/docs/6.0.13/javadoc-api/org/springframework/web/util/pattern/PathPattern.html

Ví dụ:

```Java
@GetMapping("/{name:[a-z-]+}-{version:\\d\\.\\d\\.\\d}{ext:\\.[a-z]+}")
public void handle(@PathVariable String name, @PathVariable String version, @PathVariable String ext) {
	// ...
}
```

Ngoài định nghĩa PathPattern, chúng ta có thể khai báo thêm
- Consumable Media Type
- Producible Media Type
- Parameters and headers conditions. You can test for the presence of a request parameter (`myParam`), for the absence of one (`!myParam`), or for a specific value (`myParam=myValue`).


```Java
@GetMapping(path = "/pets/{petId}", params = "myParam=myValue")
public void findPet(@PathVariable String petId) {
	// ...
}
```

# Handler methods

Sau khi xác định request mapping, việc tiếp theo là sử dụng handler methods để xử lý request. Spring MVC cung cấp các tiện ích truy cập vào xử lý request, cũng như giá trị trả về. Đầu tiên phải kể đến là **Method Arguments**, bạn chỉ cần khi báo arguments trong hàm handler, mọi thông tin hay giá trị sẽ được Spring MVC fill vào. Xét ví dụ để truy cập Request Param như sau:

```Java
@GetMapping(path = "/pets/{petId}")
public void findPet(@PathVariable String petId, @RequestParam("myParam") String param) {
	// ...
}
```

Còn khá nhiều tiện ích Method Arguments nữa, tham khảo: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/arguments.html

Tiếp theo là một số tiện ích đối với response trả về. Ví dụ sử dụng annotation `@ResponseBody` để giá trị trả về được convert thông qua `HttpMessageConverter`.

```Java
@GetMapping("/accounts/{id}")
@ResponseBody
public Account handle() {
	// ...
}
```

Thông thường khi phát triển REST API, chúng ta sẽ trả response về dưới dạng JSON. Để làm điều này thì cấu hình `MappingJackson2HttpMessageConverter`, phần message converter này được tự động cấu hình bởi class `WebMvcConfigurationSupport` khi có lib `jacksonxml-databind`

```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>${jackson.version}</version>
</dependency>
```

Trường hợp làm việc với JDK8 (kiểu Optional) thì cần khai báo jdk8Module

```xml
<dependency>
  <groupId>com.fasterxml.jackson.datatype</groupId>
  <artifactId>jackson-datatype-jdk8</artifactId>
  <version>${jackson.version}</version>
</dependency>
```

Khi này WebMvcConfigurationSupport sẽ tự động đăng ký jdk8Module cho ObjectMapper của MappingJackson2HttpMessageConverter. Cụ thể thì xem đoạn code của `Jackson2ObjectMapperBuilder` 

```Java
private void registerWellKnownModulesIfAvailable(MultiValueMap<Object, Module> modulesToRegister) {
		try {
			Class<? extends Module> jdk8ModuleClass = (Class<? extends Module>)
					ClassUtils.forName("com.fasterxml.jackson.datatype.jdk8.Jdk8Module", this.moduleClassLoader);
			Module jdk8Module = BeanUtils.instantiateClass(jdk8ModuleClass);
			modulesToRegister.set(jdk8Module.getTypeId(), jdk8Module);
		}
		catch (ClassNotFoundException ex) {
			// jackson-datatype-jdk8 not available
		}

		try {
			Class<? extends Module> javaTimeModuleClass = (Class<? extends Module>)
					ClassUtils.forName("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule", this.moduleClassLoader);
			Module javaTimeModule = BeanUtils.instantiateClass(javaTimeModuleClass);
			modulesToRegister.set(javaTimeModule.getTypeId(), javaTimeModule);
		}
...
```

Để xử dụng jdk8 datetime với jackson thì khai báo

```xml
<dependency>
  <groupId>com.fasterxml.jackson.datatype</groupId>
  <artifactId>jackson-datatype-jsr310</artifactId>
  <version>${jackson.version}</version>
</dependency>
```

Xem thêm phần cấu hình MessageConverter ở tài liệu: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config/message-converters.html

# Exception Handling

Để xử lý Exception do Controller throws, Spring cung cấp các tiện ích để chúng ta handle cho từng Controller hay handle global cho tất cả các Controller. Để handle cho một Controller cụ thể thì xử dụng `@ExceptionHandler` trong Controller đó. Còn nếu muốn handle cho tất cả các Controller thì dùng annotation này trong một class được khai báo với `@ControllerAdvice`.

```Java
@ExceptionHandler({FileSystemException.class, RemoteException.class})
public ResponseEntity<String> handle(IOException ex) {
	// ...
}
```

# Filter

Filter được dùng để transform nội dung của request, response hay của header. Thông thường thì Filter sẽ không tạo ra response, mà thay vào đó là modify request hay response hay cả hai. Servlet container sẽ pass request và response objects cho các Filter xử lý sau đó mới đến Servlet (nếu ở Spring MVC là DispatchServlet).

Servlet container sẽ quản lý Filter theo chain gọi là FilterChain, có nghĩa là Filter này xử lý xong sẽ đến Filter kia, nối tiếp nhau. Cũng do đó một Filter sẽ cần implement method sau:

```Java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
```

Ví dụ một Filter sẽ thường được implement như sau:

```Java
public MyFilter implements Filter {
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
    // do with request and response here
    ...
    // then pass process to the chain
    chain.doFilter(req, res);
  }
}
```

Thông thường chúng ta hay làm việc với HttpRequest nên hay tạo Filter extends HttpFilter (nó đã hỗ trợ convert sang HttpServletRequest - 3 dòng code) và override method

```Java
protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
```

## Filter xử lý request

Có một số thông tin của request chúng ta không thể thay đổi trực tiếp nó (như là header), thì có một kỹ thuật là wrapper request trong một object khác theo mô hình **decorator**.

```Java
class DecoratorExample implements OriginalInterface {
  private OriginalClass original

  public DecoratorExample(OriginalClass original) {
    this.original = original;
  }

  public void myMethod() {
    ...
  }

  // Override method (decorator methods)
  @Override
  public void originalMethod() {
    // do something
    original.originalMethod();
    // do something
  }
}
```

Một ví dụ sử dụng khái niệm wrapper (hay decorator) để thêm header vào request: [RequestHeaderWapper](./my-spring-app/src/main/java/my_group/web/filter/RequestHeaderWrapper.java).

Cách dùng wrapper: [example](./my-spring-app/src/main/java/my_group/web/LogFilter.java)

## Filter xử lý response

Với một số trường hợp có thể sửa response trực tiếp như thêm header hay cookie, nếu cần sửa lý được biệt như can thiệp vào body của response thì cần phải có kỹ thuật riêng. Ví dụ tạo một filter để ghi lại response sang một OutputStream khác (như là System.out), chúng ta cần wrap ServletOutputStream và thực hiện cách hàm `write` trên đồng thời của ServletOutputStream và OutputStream của chúng ta. Xem [ví dụ](./my-spring-app/src/main/java/my_group/web/filter/ReflectResponseFilter.java).

# Security

Chúng ta có thể sử dụng Spring security project, tuy nhiên để đơn giản chúng ta có thể tự tạo cơ chế authentication và authorization cho riêng mình. 

Xét ví dụ demo sử dụng một Filter để parse JwtToken [JWTFilter](./my-spring-app/src/main/java/my_group/web/filter/JWTFilter.java). Từ đó dùng Jwt token để authentication.


# References
- https://www.baeldung.com/spring-mvc-handlerinterceptor-vs-filter
- [Java web](./java-web.md)
- [Spring framework](./spring-framework.md)