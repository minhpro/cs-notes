# Introduction to Spring IoC container

IoC - Inversion of Control là một khái niệm hay mô hình lập trình mà trong đó bạn không cần tự kết nối các objects phụ thuộc lẫn nhau, thay vào đó một IoC container sẽ làm điều đó thay cho bạn. Để hiểu hơn điều này, hãy xem Spring IoC container làm nó như thế nào.

Xét ví dụ một app có 2 class là **UserService**, class này phụ thuộc vào class **DataSource** để lấy ra connection tới database.

```Java
class DataSource {
  ...
  public Connection getConnection() {
    ...
  }
}

class UserService {
  DataSource dataSource

  public UserService(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void userDataProcess() {
    Connection conn = dataSource.getConnection();
    // TODO process user data
    ...
  }
}
```

Cách thông thường không sử dụng IoC container

```Java
public static void main(String[] args) {
  Datasource dataSource = new DataSource();
  UserService userService = new UserService(dataSource);
  userService.userDataProcess();
}
```

Khi sử dụng Spring IoC

```Java
@Component // Tell Spring IoC container it is a bean
class DataSource {
  ...
}

@Component
class UserService {
  ...
}

public static void main( String[] args ) throws SQLException {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.scan(App.class.getPackageName());
    ctx.refresh();
    UserService userService = ctx.getBean(UserService.class);
    userService.userDataProcess();
}
```

Spring IoC container (**ApplicationContext**) sẽ giúp chúng ta khởi tạo (*instantiating*), cấu hình (*configuring*), và ghép nối (*assembling*, *injecting*) các objects (*beans*) bằng cách đọc *configuration metadata* (như ví dụ trên là annotation *@Component*).

> Full configured system (Ready for Use) = Spring-IoC-container.produces(Configuration metadata, You classes)

The configuration metadata có thể được mô tả trong file XML, Groovy DSL, Java annotations, và Java code. Chúng ta sẽ tập trung vào Java-based container configuration, còn các cách khác nếu cần nên tham khảo docs của Spring project.
 

Để tạo được **bean**, Spring IoC container cần thông tin **configuration metadata**, thông tin định nghĩa của một bean sẽ gồm:
- Class: [Instantiating Bean]
- Name: [Naming Beans]
- Scope: [Bean Scopes]
- Constructor arguments: [Dependency Injection]
- Properties: [Dependency Injection]
- Autowiring mode: [Autowiring Collaborators]
- ...


# Java based configuration

Đầu tiên, chúng ta sẽ xem có những cách nào để khai báo beans.

Cách thứ nhất là dùng một class với annotation `@Configuration`, và trong class đó sẽ tạo các beans bằng cách xử dụng annotation `@Bean`.

```Java
@Configuration
public class AppConfig {
  
  @Bean
  public Datasource datasource() {
    ...
    return new Datasource(...);
  }

  @Bean
  public UserService userService() {
    ...
    return new UserService(datasource());
  }
}
```

Cách thứ hai là dùng các annotation `@Component` (hay các annotation kế thừa khác như `@Service`, `@Repository`, ...) để khai báo một class sẽ tương ứng với một bean.

```Java
@Component
public class UserService {
  DataSource datasource;

  public UserService(DataSource datasource) {
    this.datasource = datasource;
  }

  ...
}
```

Tiếp đó là chỉ cho ApplicationContext biết được là sẽ sử dụng class Configuration nào hay scan các Component nào hay là kết hợp cả 2 cách này.

```Java
var ctx = ctx = new AnnotationConfigWebApplicationContext(AppConfig.class);
ctx.scan("my_group.myapp"); // You can use `@ComponentScan` trong class Configuration
ctx.refesh(); // After scan, you have to refesh() to get beans
```

References:
- https://docs.spring.io/spring-framework/reference/core/beans/java.html

# Enviroment Abstraction

Ứng dụng của bạn cần được chạy đúng trên mỗi môi trường khác nhau. Để làm được điều đó, app cần nhận biết được các tham số của môi trường đang chạy để từ đó hoạt động hợp lý. Trong Spring, tham số môi trường được khái quát trong khái niệm **Environment Abstraction**. Mỗi môi trường **application environment** sẽ bao gồm hai khía cạnh: **profiles** và **properties**.

Một profile là một tập hợp (có đặt tên) logical các định nghĩa bean (bean definitions) được đăng ký với IoC container khi và chỉ khi profile đó được active. Beans được gán cho một profile bằng cách sử dụng file cấu hình XML hay sử dụng annotation `@Profile`.

Properties chứa thông tin tham số môi trường (biến môi trường) được tập hợp từ đa dạng các nguồn: properties files, JVM system properties, system environment variables, JNDI, servlet context parameters, ad-hoc Properties objects, Map objects, and so on.

Mỗi ApplicationContext trong Spring sẽ chứa một object *Environment*. Object này cho phép xác định profile hiện tại là gì, profile nào sẽ được mặc định active. Đối với properties thì *Environment* sẽ cung cấp API để cấu hình property source cũng như resove properties từ các sources này.

## Profile

Để gán beans cho Profile, bạn có thể sử dụng annotation `@Profile` với annotation `@Bean` hay `@Configuration`.

```Java
@Configuration
@Profile("development")
public class StandaloneDataConfig {

	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.HSQL)
			.addScript("classpath:com/bank/config/sql/schema.sql")
			.addScript("classpath:com/bank/config/sql/test-data.sql")
			.build();
	}
}
```

Cách active profiles

```Java
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
ctx.getEnvironment().setActiveProfiles("development");
ctx.register(SomeConfig.class, StandaloneDataConfig.class, JndiDataConfig.class);
ctx.refresh();
```

Ngoài ra có thể active profiles thông qua property `spring.profiles.active`, biến này có thể được setup thông qua biến môi trường (system environment variables), JVM system properties, servlet context parameters in `web.xml`, ... Trong integration tests, có thể sử dụng `@ActiveProfiles` annotation ([context configuration](https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/ctx-management/env-profiles.html)).


## PropertySource Abstraction

Environment trong Spring cung cấp các phương thức tìm kiếm (search operations) thông qua một hệ thống phân cấp (hierarchical) các property sources có thể cấp hình được. Xét đoạn code sau.

```Java
ApplicationContext ctx = new GenericApplicationContext();
Environment env = ctx.getEnvironment();
boolean containsMyProperty = env.containsProperty("my-property");
System.out.println("Does my environment contain the 'my-property' property? " + containsMyProperty);
```

Đoạn code trên, chúng ta có hỏi Spring là một property `my-property` có được định nghĩa trong môi trường hiện tại không. Để trả lời cho câu hỏi này, `Environment` của Spring sẽ search  over một set các `PropertySource` objects. Một `PropertySource` sẽ chứa các cặp key-value. Abstract class [`PropertySource`](org.springframework.core.env.PropertySource) là class cơ sở để định nghĩa cho các loại PropertySource. Một `StandardEnvironment` của Spring sẽ được cấu hình với 2 PropertySource objects - một là thể hiện tập JVM system properties (`System.getProperties()`) và một là thể hiện tập các biến môi trường system environment variables (`System.getEnv()`).

Quá trình tìm kiếm properties là hierarchical. Mặc định thì system properties có mức ưu tiên cao hơn biến môi trường. Tùy các ApplicationContext khác nhau mà ở đó thứ tự ưu tiên khác nhau. Ví dụ trong một `StandardServletEnvironment` sẽ có các PropertySource và thứ tự từ cao đến thấp như sau:

1. ServletConfig parameters (if applicable — for example, in case of a DispatcherServlet context)
2. ServletContext parameters (web.xml context-param entries)
3. JNDI environment variables (java:comp/env/ entries)
4. JVM system properties (-D command-line arguments)
5. JVM system environment (operating system environment variables)

Với Spring boot application thì tham khảo thứ tự sau (từ thấp đến cao):
- https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config


Cách thêm một `PropertySource` vào Environment

```Java
ConfigurableApplicationContext ctx = new GenericApplicationContext();
ResourcePropertySource propertySource = new ResourcePropertySource("propertyFromFile", "classpath:application.properties");
MutablePropertySources propertySources = ctx.getEnvironment().getPropertySources();
propertySources.addLast(propertySource);
```

Đoạn code trên đã thêm một PropertyResource với độ ưu tiên thấp nhất (`addLast`) vào Environment.

Một cách khác để tạo PropertyResource và thêm vào Environment là sử dụng annotation `@PropertyResource` với `@Configuration` (hay với bất kỳ annotation tạo bean)

```Java
@Configuration
@PropertySource("classpath:my-app.properties")
public class AppConfig {

 @Autowired
 Environment env;

 @Bean
 public TestBean testBean() {
  TestBean testBean = new TestBean();
  testBean.setName(env.getProperty("testbean.name"));
  return testBean;
 }
}
```

Một số PropertyResource hỗ trợ relaxed binding, ví dụ như `org.springframework.core.env.SystemEnvironmentPropertySource`:

```
For example, a call to getProperty("foo.bar") will attempt to find a value for the original property or any 'equivalent' property, returning the first found:
foo.bar - the original name
foo_bar - with underscores for periods (if any)
FOO.BAR - original, with upper case
FOO_BAR - with underscores and upper case
```

**Reference**:
- https://docs.spring.io/spring-framework/reference/core/beans/environment.html#beans-using-propertysource
- https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config
- https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties.relaxed-binding


# Docker

https://spring.io/guides/topicals/spring-boot-docker/


# Spring security

https://docs.spring.io/spring-security/reference/servlet/getting-started.html#hello-expectations

If you define a @Configuration with a SecurityFilterChain bean in your application, it switches off the default webapp security settings in Spring Boot.


# Jasper report

https://github.com/manish-in-java/spring-mvc-jasper/blob/master/src/main/scala/org/example/web/servlet/jasper/GraphicsServlet.scala