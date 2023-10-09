## Overview

`JdbcTemplate` là thư viện trong project Spring, được dùng để làm việc với database SQL. Thay vì phải làm việc trực tiếp với JDBC để truy cập database thì JdbcTemplate cung cấp cho chúng ta các interfaces (APIs) tiện tích, và dễ sử dụng hơn. Một số lợi ích của JdbcTemplate có thể kể đến như:

- Tạo và close connections, chúng ta không cần trực tiếp làm điều này.
- Running statements và stored procefure calls.
- Iterating over the ResultSet và trả về giá trị.

Table of contents:

- [Cấu hình](#cấu-hình)
- [Query](#query-dữ-liệu)
- [Update](#update-dữ-liệu)
- [References](#references)

## Cấu hình

Khi sử dụng Spring Boot, thêm thư viện sau để sử dụng JdbcTemplate

`org.springframework.boot:spring-boot-starter-jdbc`

Khi này Spring Boot sẽ tự động tạo JdbcTemplate. Việc của chúng ta là cung cấp thông tin cấu hình qua các thuộc tính sau.

```sh
spring.datasource.url=<DATBASE_URL> // e.g. jdbc:postgresql://localhost:5432/demo
spring.datasource.username=<USER>
spring.datasource.password=<PASSWORD>
```

Để sử dụng JdbcTemplate, bạn chỉ cần `@Autowired JdbcTemplate jdbcTemplate`.

Notes:

- See class `org.springframework.jdbc.core.JdbcTemplate`, nó extends `JdbcAccessor`, để xem nó được cài đặt `DataSource` như thế nào.

## Query dữ liệu

Để làm quen với JdbcTemplate, hãy xét một cơ sở dữ liệu sau:

```sql
CREATE TABLE users (
  id bigint PRIMARY KEY,
  username text
);

CREATE TABLE products (
  id bigint PRIMARY KEY,
  pname text,
  price int
);

CREATE TABLE orders (
  user_id bigint REFERENCES TO users(id),
  product_id bigint REFERENCES TO products(id),
  quantity int,
  order_time timestamp
);
```

Các APIs sử dụng để query dữ liệu hầu hết bắt đầu bằng `query`,

```Java
<T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException;
```

RowMapper dùng để map mỗi row trong ResultSet sang một Class trong Java.

```Java
@FunctionInterface
public interface RowMapper<T> {
  T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
```

Ví dụ

```Java
record User(int id, String username){}

final String sql = "SELECT id, username from users";

List<User> users = jdbcTemplate.query(sql,
  (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("username")));
```

Nếu muốn sửa dụng Stream thay vì List thì dùng API sau,

```Java
<T> Stream<T> queryForStream(String sql, RowMapper<T> rowMapper) throws DataAccessException
```

### DataClassRowMapper

Đôi khi, lúc nào cũng phải viết `RowMapper` khiến chúng ta phiền lòng, thay vào đó hãy sử dụng `DataClassRowMapper`.

```Java
List<User> users = jdbcTemplate.query(sql, new DataClassRowMapper<>(User.class));
```

### Truyền tham số khi query

Sử dụng API sau khi cần truyền tham số:

```Java
List<T> query(String sql, RowMapper<T> rowMapper, Object... args);
```

Xét ví dụ:

```Java
record Product(int id, String name, int price){};

final String sql = "SELECT id, pname as name, price from products WHERE pname like ? AND price > ?";

List<Product> products = jdbcTemplate.query(sql, new DataClassRowMapper<>(Product.class), "Java%", 100);
```

Notes: 
 - Khi tên trường trong class và database là khác nhau, bạn nên sử dụng alias để đồng nhất.
 - Khi truyền tham số, `PreparedStatement` sẽ đoán (guess) kiểu của tham số, nếu bạn gặp vấn đề nào đó với kiểu và muốn chỉ định chính xác kiểu thì có thể dùng `SqlParameterValue` để truyền tham số hay sử dụng API sau:

 ```Java
 // explicit args types
 <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException;
 ```

### Truyền tham số theo tên

Khi bạn thích dùng tên hơn thay vì dấu `?` để truyền tham số, hãy dùng `NamedParameterJdbcTemplate` thay vì `JdbcTemplate`. Object này cũng được tạo tự động bởi Spring Boot, và chúng ta cần làm là `Autowired`.

Sử dụng API sau để truyền tham số:

```Java
public <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) throws DataAccessException
```

Có 2 classes implement `SqlParameterSource` hay được dùng để truyền tham số,

- **MapSqlParameterSource**: holds a given Map of parameters.
- **BeanPropertySqlParameterSource**: obtains parameter values from bean properties of a given JavaBean object. The names of the bean properties have to match the parameter names. Supports components of record classes as well, with accessor methods matching parameter names.

Xét ví dụ,

```Java
final String sql = "SELECT id, pname as name, price from products WHERE pname like :searchName AND price > :minPrice";

MapSqlParameterSource params = new MapSqlParameterSource()
  .addValue("searchName", "Java%")
  .addValue("minPrice", 100);

List<Product> products = namedParameterJdbcTemplate.query(sql, params, new DataClassRowMapper<>(Product.class));
```

Chúng ta có thể sử dụng trực tiếp `Map` để truyền parameters bằng cách sử dụng API sau:

```Java
public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper)
			throws DataAccessException
```

### Query scalar value

Khi muốn query một giá trị duy nhất, sử dụng API sau:

```Java
// The query is expected to be a single row/single column query; the returned result will be directly mapped to the corresponding object type
@Nullable
<T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException
```

Ví dụ,

```Java
final var sql = "select pname from products where id = 10";

String productName = jdbcTemplate.queryForObject(sql, String.class);
```

Khi muốn query một row và map thành một object thì sử dụng API sau:

```Java
// mapping a single result row to a result object via a RowMapper
@Nullable
public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException
```

Ví dụ,

```Java
final var sql = "select id, pname as name, price from products where id = 10";

Product product = jdbcTemplate.queryForObject(sql, new DataClassRowMapper<>(Product.class);
```

### Các APIs query khác

Tham khảo các APIs query khác ở [reference](#references)

## Update dữ liệu

Để update dữ liệu (insert, update, or delete), sử dụng API sau:

```Java
// returns the number of rows affected
public int update(String sql) throws DataAccessException;
```

Ví dụ,

```Java
var sql = "update products set price = 100 where id > 10";

var affectedNum = jdbcTemplate.update(sql);
```

Khi cần truyền parameters, sử dụng API sau:

```Java
int update(String sql, @Nullable Object... args) throws DataAccessException;
```

Ví dụ,

```Java
var sql = "update products set price = 100 where id > ?";

var affectedNum = jdbcTemplate.update(sql, 10);
```

Và để sử dụng parameter với tên cụ thể thay vì `?` thì sử dụng APIs của `NamedParameterJdbcTemplate` (giống với trường hợp query dữ liệu bên trên),

```Java
//
int update(String sql, SqlParameterSource paramSource) throws DataAccessException;
// OR
int update(String sql, Map<String, ?> paramMap) throws DataAccessException;
```

## Batch update

Sử dụng API sau để thực hiện batchUpdate,

```Java
// Returns an array containing the numbers of rows affected by each update in the batch
int[] batchUpdate(String sql, List<Object[]> batchArgs) throws DataAccessException;
```

Xét ví dụ,

```Java
final var sql = "insert into products(pname, price) values(?, ?)"

List<Object[]> params = Arrays.asList(new Object[]{"Product 1", 100}, new Object[]{"Product 2", 200});

int[] rowNumAffected = jdbcTemplate.batchUpdate(sql, params);
```

Để sử dụng tham số với tên cụ thể, sử dụng API tương tự của `NamedParameterJdbcTemplate`.

## References

Có khá nhiều APIs khác hữu ích, và bạn nên tham khảo javadocs,

- https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html
- Or class `org.springframework.jdbc.core.JdbcOperations`
- `org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate`

Notes: ngoài ra, bạn cũng nên tham khảo javadoc để biết cách xử lý các case exception của mỗi APIs.
