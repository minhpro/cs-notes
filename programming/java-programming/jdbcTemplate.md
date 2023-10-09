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

## Một số tools khác

Các thư viện làm việc với database thường chia làm 2 styles:

**ORM**:
* Trong lập trình Java, các thư viện ORM (Object Relational Mapping) thường implement chuẩn JPA - Jakarta Persistence API
* https://jakarta.ee/specifications/persistence/
* Một số thư viện có thể kể đến như
  * Spring JPA (Hibernate powered)
  * Hibernate (ngoài implement JPA thì Hibernate còn cung cấp nhiều tính năng khác??)
  * EclipseLink
  * Apache OpenJPA
**Non ORM**
* JdbcTemplate
* Apache Common DbUtils
* FluentJdbc: https://github.com/zsoltherpai/fluent-jdbc
* Mybatis (một template engine để viết SQL)
* JOOQ (với SQL theo kiểu type-safe DSL)

## Obtaining Auto-generated keys

Thường thì các bảng hay có key `id` là trường auto-generated, và rất nhiều trường hợp như `insert` dữ liệu mới thì chúng ta muốn nhận về key này (để dùng làm foreign key cho bảng khác chẳng hạn). Nhiều database hỗ trợ điều này, cụ thể qua `RETURNING` clause.

```sql
INSERT ... RETURNING ID;
```

Nhưng thay vì sử dụng API `update` thì bạn cần dùng `query`.

```Java
final var sql = "insert into products(pname, price) values(?, ?) returning id";

var insertedId = jdbcTemplate.selectForObject(sql, Integer.class, "Product 01", 100);
```

Postgresql, MariaDB and Oracle support `RETURNING` clause, but MySQL doesn't.

* https://www.postgresql.org/docs/current/dml-returning.html
* https://mariadb.com/kb/en/insertreturning/
* https://docs.oracle.com/en/database/oracle/oracle-database/19/lnpls/RETURNING-INTO-clause.html

Với MySQL, bạn cần dùng API sau:

```Java
// psc – a callback that provides SQL and any necessary parameters 
// generatedKeyHolder – a KeyHolder that will hold the generated keys
// Returns the number of rows affected
int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder) throws DataAccessException;
```

Xét ví dụ,

```Java
final var sql = "insert into products(pname, price) values(?, ?) returning id";

KeyHolder keyHolder = new GeneratedKeyHolder();
jdbcTemplate.update(connection -> {
  PreparedStatement ps = connection
      .prepareStatement(insertSql);
  ps.setString(1, "Product 01");
  ps.setInt(2, 100);
  return ps;
}, keyHolder);

var insertedId = (long) keyHolder.getKey();
```

Nếu không thích API trên, thì Spring JDBC cung cấp một API khác sử dụng `SimpleJdbcInsert`, để sửa dụng class này thì bạn cần `Autowired` datasource.

```Java
// args – a Map containing column names and corresponding value
// returns the generated key value
Number executeAndReturnKey(Map<String, ?> args);

// returns the KeyHolder containing all generated keys
KeyHolder executeAndReturnKeyHolder(Map<String, ?> args);
```

Xét ví dụ,

```Java
@Autowired Datasource datasource;

final var sql = "insert into products(pname, price) values(?, ?)";

SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(datasource)
  .withTableName("products")
  .usingGeneratedKeyColumns("id");

Map<String, Object> params = Map.of("pname", "Product 01", "price", 100);
var insertedId = (long) simpleJdbcInsert.executeAndReturnKey(params);
```
