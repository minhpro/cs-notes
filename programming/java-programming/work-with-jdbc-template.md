## Work with JdbcTemplate

`JdbcTemplate` is a library in the project Spring, it supports us to work with the database SQL. 

Instead of working with JDBC directly, `JdbcTemplate` provides a set of convenient interfaces (APIs) that wrap the JDBC operations internally:

- Creating and closing connections, you don't have to do these directly.
- Running statements and stored procedure calls.
- Iterating over the ResultSet and returning the mapped result.

`NamedParameterJdbcTemplate` is the similar version of `JdbcTemplate` to work with named parameters, instead of `?` character placeholders.

## Query API

```Java
/**
 * @param paramSource container of arguments to bind to the query
 * @param rowMapper object that will map one object per row                 
 */
public <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper);
```

Example,

```Java
final String sql = "SELECT id, pname as name, price from products WHERE pname like :searchName AND price > :minPrice";

MapSqlParameterSource params = new MapSqlParameterSource()
  .addValue("searchName", "Java%")
  .addValue("minPrice", 100);

List<Product> products = namedParameterJdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Product.class));
```

`RowMapper` is used to map one row from the ResultSet to on object. Its primary method is:

```Java
T mapRow(ResultSet rs, int rowNum);
```

You can construct a `RowMapper` as following guide:

- If your data class (e.g. DTO) is a bean (no args constructor, getter, setter) -> using `BeanPropertyRowMapper`.
- If your data class is immutable (only constructors) -> using the helper factory method `DatabaseHelper.constructorRowMapper(Class<T> mappedClass)`.
- If your data class is a Java Record -> using `DataClassRowMapper`.
- Otherwise, making your own `RowMapper`, like that.

```Java
List<Product> products = namedParameterJdbcTemplate.query(sql, params,
        (rs, rowNum) -> {
            Integer id = rs.getInt(1);
            String name = rs.getString(2);
            Integer price = rs.getInt(3);
            return new Product(id, name, price);
        });
```

See more about `JdbcTemplate` in the [link](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html).

## Pagination and Sorting

[`DatabaseHelper`](code/DatabaseHelper.java) class provides some useful APIs to work with pagination and sorting.

For example, an API to working `Pagable` parameter,

```Java
/**
 * Execute the query with named parameters and a PageRequest and return a page of data
 * @param sql the SQL query to execute
 * @param paramSource container of arguments to bind to the query
 * @param pageParam pageRequest
 * @param rowMapper object that will map one object per row
 * @return a page of mapped records
 * 
 * see more APIs on {@code DatabaseHelper.class}
 */
public static <T> Page<T> pageDataQuery(
        NamedParameterJdbcTemplate namedParameterJdbcTemplate,
        String sql, SqlParameterSource paramSource, Pageable pageParam, RowMapper<T> rowMapper);
```
