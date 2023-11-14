package java-example.database;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Some APIs to help to build SQL queries, RowMapper, etc.
 */
public class DatabaseHelper {
    static final String ORDER_BY_TEMPLATE = "ORDER BY %s";
    static final String PAGE_TEMPLATE = "LIMIT %d OFFSET %d";
    static final String COUNT_SQL_TEMPLATE = "SELECT count(*) FROM (%s) t";

    private DatabaseHelper() {}

    /**
     * Execute the query with named parameters and a PageRequest and return a list of data
     * @param sql the SQL query to execute
     * @param params container of arguments to bind to the query
     * @param pageParam pageRequest
     * @param rowMapper object that will map one object per row
     * @return a list of mapped records
     */

    public static <T> List<T> query(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            String sql, SqlParameterSource params, Pageable pageParam, RowMapper<T> rowMapper) {
        return namedParameterJdbcTemplate.query(
                buildSqlWithPageable(sql, pageParam), params, rowMapper);
    }

    /**
     * Execute the query with named parameters and a PageRequest and return a list of data
     * @param sql the SQL query to execute
     * @param params map of parameters to bind to the query
     * @param pageParam pageRequest
     * @param rowMapper object that will map one object per row
     * @return a list of mapped records
     */

    public static <T> List<T> query(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            String sql, Map<String, Object> params, Pageable pageParam, RowMapper<T> rowMapper) {
        return namedParameterJdbcTemplate.query(
                buildSqlWithPageable(sql, pageParam), params, rowMapper);
    }

    /**
     * Execute the query with named parameters and a PageRequest and return a page of data
     * @param sql the SQL query to execute
     * @param paramSource container of arguments to bind to the query
     * @param pageParam pageRequest
     * @param rowMapper object that will map one object per row
     * @return a page of mapped records
     */
    public static <T> Page<T> pageDataQuery(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            String sql, SqlParameterSource paramSource, Pageable pageParam, RowMapper<T> rowMapper) {
        List<T> items = namedParameterJdbcTemplate.query(
                buildSqlWithPageable(sql, pageParam), paramSource, rowMapper);
        return new PageImpl<>(items, pageParam, namedParameterCount(namedParameterJdbcTemplate, sql, paramSource));
    }

    /**
     * Execute the query with named parameters and a PageRequest and return a page of data
     * @param sql the SQL query to execute
     * @param params map of parameters to bind to the query
     * @param pageParam pageRequest
     * @param rowMapper object that will map one object per row
     * @return a page of mapped records
     */
    public static <T> Page<T> pageDataQuery(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            String sql, Map<String, Object> params, Pageable pageParam, RowMapper<T> rowMapper) {
        List<T> items = namedParameterJdbcTemplate.query(
                buildSqlWithPageable(sql, pageParam), params, rowMapper);
        return new PageImpl<>(items, pageParam, namedParameterCount(namedParameterJdbcTemplate, sql, params));
    }

    /**
     * Execute the query with named parameters and a Sort parameter and return a list of data
     * @param sql the SQL query to execute
     * @param params container of arguments to bind to the query
     * @param sort Sort parameter
     * @param rowMapper object that will map one object per row
     * @return a list of mapped records
     */
    public static <T> List<T> query(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            String sql, SqlParameterSource params, Sort sort, RowMapper<T> rowMapper) {
        String withSortSql = sql + " " + buildOrderClause(sort);
        return namedParameterJdbcTemplate.query(withSortSql, params, rowMapper);
    }

    /**
     * Execute the query with named parameters and a Sort parameter and return a list of data
     * @param sql the SQL query to execute
     * @param params map of parameters to bind to the query
     * @param sort Sort parameter
     * @param rowMapper object that will map one object per row
     * @return a list of mapped records
     */
    public static <T> List<T> query(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            String sql, Map<String, Object> params, Sort sort, RowMapper<T> rowMapper) {
        String withSortSql = sql + " " + buildOrderClause(sort);
        return namedParameterJdbcTemplate.query(withSortSql, params, rowMapper);
    }

    /**
     * Execute the query with named parameters, page, size and return a list of data
     * @param sql the SQL query to execute
     * @param params container of arguments to bind to the query
     * @param page page number
     * @param size size of a page
     * @param rowMapper object that will map one object per row
     * @return a list of mapped records
     */
    public static <T> List<T> query(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            String sql, MapSqlParameterSource params, int page, int size, RowMapper<T> rowMapper) {
        String pageSql = sql + " " + buildLimitOffsetClause(page, size);
        return namedParameterJdbcTemplate.query(pageSql, params, rowMapper);
    }

    /**
     * Execute the query with named parameters, page, size and return a list of data
     * @param sql the SQL query to execute
     * @param params map of parameters to bind to the query
     * @param page page number
     * @param size size of a page
     * @param rowMapper object that will map one object per row
     * @return a list of mapped records
     */
    public static <T> List<T> query(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            String sql, Map<String, Object> params, int page, int size, RowMapper<T> rowMapper) {
        String pageSql = sql + " " + buildLimitOffsetClause(page, size);
        return namedParameterJdbcTemplate.query(pageSql, params, rowMapper);
    }

    /**
     * Count the number of rows that matches the SQL query.
     * This is done by wrapping the SQL query inside a `SELECT COUNT(*)` clause
     * @param sql the SQL query to execute
     * @return the number of rows
     */
    public static int count(JdbcTemplate jdbcTemplate, String sql) {
        String countSql = String.format(COUNT_SQL_TEMPLATE, sql);
        Integer total = jdbcTemplate.queryForObject(countSql, Integer.class);
        return total != null ? total : 0;
    }

    /**
     * Count the number of rows that matches the SQL query with named parameters.
     * @param sql the SQL query to execute
     * @param params container of arguments to bind to the query
     * @return the number of rows
     */
    public static int namedParameterCount(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate, String sql, SqlParameterSource params) {
        String countSql = String.format(COUNT_SQL_TEMPLATE, sql);
        Integer total = namedParameterJdbcTemplate.queryForObject(countSql, params, Integer.class);
        return total != null ? total : 0;
    }

    /**
     * Count the number of rows that matches the SQL query with named parameters.
     * @param sql the SQL query to execute
     * @param params map of parameters to bind to the query
     * @return the number of rows
     */
    public static int namedParameterCount(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate, String sql, Map<String, Object> params) {
        String countSql = String.format(COUNT_SQL_TEMPLATE, sql);
        Integer total = namedParameterJdbcTemplate.queryForObject(countSql, params, Integer.class);
        return total != null ? total : 0;
    }

    /**
     * Build a SQL query with an order clause and a limit-offset clause from a Pageable parameter.
     * @param sql main SQL query
     * @param pageRequest pageable parameter
     * @return a SQL with an order clause and a limit-offset clause.
     */
    public static String buildSqlWithPageable(String sql, Pageable pageRequest) {
        return String.format("%s %s %s", sql,  buildOrderClause(pageRequest.getSort())
                , buildLimitOffsetClause(pageRequest.getPageNumber(), pageRequest.getPageSize()));
    }

    /**
     * Build an order by clause from a Sort parameter.
     * @param sort Sort parameter
     * @return an order by clause.
     */
    public static String buildOrderClause(Sort sort) {
        if (sort.isEmpty())
            return Strings.EMPTY;
        String orderColumns = sort.stream()
                .map(x -> x.getProperty() + " " + x.getDirection().name())
                .collect(Collectors.joining(", "));

        return String.format(ORDER_BY_TEMPLATE, orderColumns);
    }

    /**
     * Build an order by clause from a list of Order parameter.
     * @param orders a list of Order to ORDER BY
     * @return an order by clause.
     */
    public static String buildOrderClause(List<Sort.Order> orders) {
        if (orders.isEmpty())
            return Strings.EMPTY;
        String orderColumns = orders.stream()
                .map(x -> x.getProperty() + " " + x.getDirection().name())
                .collect(Collectors.joining(", "));

        return String.format(ORDER_BY_TEMPLATE, orderColumns);
    }

    /**
     * Build a limit-offset clause from a page and a size parameters
     * Don't have to pass limit and offset as Prepared parameters.
     * There is no sql injections here (page and size are integers).
     * @param page page number
     * @param size size of a page
     * @return a limit-offset clause
     */

    public static String buildLimitOffsetClause(int page, int size) {
        int offset = page * size;
        return String.format(PAGE_TEMPLATE, size, offset);
    }

    /**
     * Build AND conditions clause from list of fields
     * <pre>
     *     List<String> fields = List.of("name", "price", "year");
     *     String conditionClause = DatabaseHelper.buildAndCondition(fields)
     *     // conditionClause = "name = :name AND price = :price AND year = :year"
     * </pre>
     * @param fields list of field to build the condition
     * @return an AND conditions clause
     */
    public static String buildAndCondition(List<String> fields) {
        return fields.stream()
                .map(f -> String.format("%s = :%s", f, f))
                .collect(Collectors.joining(" AND "));
    }

    /**
     * Construct a RowMapper from a given mappedClass by guessing its constructor
     * The order of the constructor's arguments and their types should be aligned with the ResultSet (the SELECT clause)
     * @param mappedClass the class that each row should be mapped to
     * @return a RowMapper
     */
    public static <T> RowMapper<T> constructorRowMapper(Class<T> mappedClass) {
        return (rs, rowNum) -> {
            int columnCount = rs.getMetaData().getColumnCount();
            Constructor<T> mappedConstructor = getConstructor(mappedClass, columnCount);
            Parameter[] parameters = mappedConstructor.getParameters();
            Object[] args = new Object[parameters.length];
            for (int i = 0; i < args.length; i++) {
                Object value = rs.getObject(i + 1, parameters[i].getType());
                args[i] = value;
            }
            try {
                return mappedConstructor.newInstance(args);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot construct object from the constructor: " + e);
            }
        };
    }

    /**
     * Guess constructor for given class and number of its arguments
     * @param clazz
     * @param numberArguments number of arguments
     * @return the proper constructor
     */
    public static <T> Constructor<T> getConstructor(Class<T> clazz, int numberArguments) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length == 0)
            constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors)
            if (constructor.getParameterCount() == numberArguments)
                return (Constructor<T>) constructor;
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No proper constructor found for " + clazz);
        }
    }
}
