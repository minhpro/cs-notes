
# 0. Overview

To access the database from Java application, you need to use Driver libraries (including them in you application classpath). Usually, most of the drivers will implement Java Database Connectivity (JDBC) API.
* JDBC: https://download.oracle.com/otndocs/jcp/jdbc-4_2-mrel2-spec/
* Postgresql JDBC Driver: https://jdbc.postgresql.org/documentation/
* MySQL Connector/J: https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-overview.html
* MariaDB Connector/J: https://mariadb.com/kb/en/about-mariadb-connector-j
* Oracle JDBC: https://docs.oracle.com/en/database/oracle/oracle-database/23/jjdbc/toc.htm
* SQL Server: https://learn.microsoft.com/sql/connect/jdbc/microsoft-jdbc-driver-for-sql-server

Maven repository:
* https://mvnrepository.com/artifact/org.postgresql/postgresql
* https://mvnrepository.com/artifact/com.microsoft.sqlserver/mssql-jdbc
* https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
* https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client

# 1. Connecting to the Database

**Using DriverManager**

```java
String url = "jdbc:postgresql://localhost/test";
Properties props = new Properties();
props.setProperty("user", "john");
props.setProperty("password", "secret");
Connection conn = DriverManager.getConnection(url, props);
```

URL Connection string format

```shell
jdbc:<db-type>://<host>[:port]/<db-name>[?param1=value1&param2=value2&...]
```

* db-type: mysql, postgresql, mariabd, oracle, ...
* host: database server address
* port: server port, default is 5432 with postgresql, 3306 with mysql
* param: such as `user`, `password`, ...

Instead of setting parameters in the connection string, you can set them with `Properties`.

To use `Connection Failover`, setup the connection string like that:

`jdbc:postgresql://host1:port1,host2:port2/database`

More detail:
* https://jdbc.postgresql.org/documentation/use/#connection-fail-over
* https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-config-failover.html
* https://mariadb.com/kb/en/about-mariadb-connector-j/#connection-strings

**Using Connection pool**

```Java
MariaDbPoolDataSource pool = new MariaDbPoolDataSource("jdbc:mariadb://server/db?user=myUser&maxPoolSize=10");

try (Connection connection = pool.getConnection()) {
    try (Statement stmt = connection.createStatement()) {
        ResultSet rs = stmt.executeQuery("SELECT CONNECTION_ID()");
        rs.next();
        System.out.println(rs.getLong(1)); //4489
    }
}

try (Connection connection = pool.getConnection()) {
    try (Statement stmt = connection.createStatement()) {
        ResultSet rs = stmt.executeQuery("SELECT CONNECTION_ID()");
        rs.next();
        System.out.println(rs.getLong(1)); //4489 (reused same connection)
    }
}

pool.close();
```

To see more on https://mariadb.com/kb/en/pool-datasource-implementation/

Another popular pool is [HikariCP](https://github.com/brettwooldridge/HikariCP)

```Java
final HikariDataSource ds = new HikariDataSource();
ds.setMaximumPoolSize(20);
ds.setDriverClassName("org.mariadb.jdbc.Driver");
ds.setJdbcUrl("jdbc:mariadb://localhost:3306/db");
ds.addDataSourceProperty("user", "root");
ds.addDataSourceProperty("password", "myPassword");
ds.setAutoCommit(false);
```

# 2. Query and Processing the Result

**JDBC Java**

Using `Statement` or `PreparedStatement` to run SQL statements.
* `Statement` to run SQL statements without binding parameter values
* `PreparedStatement` to run SQL statements with binding parameter values

```java
Statement st = conn.createStatement();
ResultSet rs = st.executeQuery("SELECT * FROM mytable WHERE columnfoo = 500");
while (rs.next()) {
    System.out.println(rs.getString("columnfoo"));
}
rs.close();
st.close()
```

Using `PreparedStatement` to bind values in the query (also preventing SQL injection)

```java
int foovalue = 500;
PreparedStatement st = conn.prepareStatement("SELECT * FROM mytable WHERE columnfoo = ?");
st.setInt(1, foovalue);
ResultSet rs = st.executeQuery();
while (rs.next()) {
    System.out.println(rs.getString("columnfoo"));
}
rs.close();
st.close();
```

# 4. Cursor based Result Processing

By default the driver collects all the results for the query at once. This can be inconvenient for large data sets. To deal with this issue, JDBC driver can fetch a small number of rows based on database cursor.

A small number of rows are cached on the client side of the connection and when exhausted the next block of rows is retrieved by repositioning the cursor.

Changing the code to use cursor mode by setting the fetch size of the `Statement`. Setting the fetch size back to 0 will cause all rows to be cached (the default behaviour).

```java
Statement st = conn.createStatement();

// Turn use of the cursor on.
st.setFetchSize(50);
ResultSet rs = st.executeQuery("SELECT * FROM mytable");
while (rs.next()) {
    System.out.print("a row was returned.");
}
rs.close();
```

# 5. Performing Updates

To change data (INSERT, UPDATE, or DELETE), using the `executeUpdate()` method. This method returns the number of rows matched by the update statement.

```Java
PreparedStatement st = conn.prepareStatement("DELETE FROM mytable WHERE columnfoo = ?");
st.setInt(1, 500);
int rowsDeleted = st.executeUpdate();
System.out.println(rowsDeleted + " rows deleted");
st.close();
```

# 6. Get inserted ID

Some tables has an auto-generated id column, how to get the `id` of a new inserted row.

Using `RETURNING` clause with `executeQuery` (not `executeUpdate`), like that

```java
ResultSet rs = statement.executeQuery("INSERT ... RETURNING ID");
rs.next();
rs.getInt(1);
```

Postgresql, MariaDB and Oracle support `RETURNING` clause, but MySQL doesn't.

* https://www.postgresql.org/docs/current/dml-returning.html
* https://mariadb.com/kb/en/insertreturning/
* https://docs.oracle.com/en/database/oracle/oracle-database/19/lnpls/RETURNING-INTO-clause.html

With MySQL, take another way

```Java
stmt.executeUpdate("INSERT INTO ...", Statement.RETURN_GENERATED_KEYS);
ResultSet rs = stmt.getGeneratedKeys();
rs.next();
long pk = rs.getLong(1);
```

# 7. Transaction

To use explicit transaction, turn off connection `auto-commit`, and manual `commit`, `rollback`, set, release `savepoint` the transaction.

```Java
conn.setAutoCommit(false);
stmt.executeUpdate("INSERT INTO table1 VALUES (1)");
Savepoint firstSavePoint = conn.setSavepoint("first_savepoint");
stmt.executeUpdate("INSERT INTO table1 VALUES (2)");
conn.rollback(firstSavePoint);
stmt.executeUpdate("INSERT INTO table1 VALUES (3)");
conn.commit();
```

The above transaction will insert the value 1 and 3, not 2.

You can also setup the transaction isolation level like that

```Java
conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
```

References:

* https://www.postgresql.org/docs/current/sql-savepoint.html
* https://www.postgresql.org/docs/current/tutorial-transactions.html

# 8. Calling Stored Functions and Procedures

To call stored functions and procedures, using `CallableStatement` and the standard JDBC escape call syntax `{call procedure_name(?)}`.

```Java
CallableStatement stmt = conn.prepareCall("{? = call upper( ? )}");
stmt.registerOutParameter(1, Types.VARCHAR);
stmt.setString(2, "lowercase to uppercase");
stmt.execute(); // If function returns set of rows, using executeQuery() and ResultSet
String upper = stmt.getString(1);
logger.info("call upper: {}", upper);
```

# 9. Using Date and Time

JDBC drivers should support [Java 8 Date and Time API](https://www.oracle.com/technical-resources/articles/java/jf14-date-time.html)(JSR-310).

Example in Postgresql:

| PostgreSQL                      | Java 8         |
|---------------------------------|----------------|
| DATE                            | LocalDate      |
| TIME [ WITHOUT TIME ZONE ]      | LocalTime      |
| TIMESTAMP [ WITHOUT TIME ZONE ] | LocalDateTime  |
| TIMESTAMP WITH TIME ZONE        | OffsetDateTime |

You can imagine `OffsetDateTime` simply is `LocalDateTime` plus `Offset` (TimeZone) information.

**Reading Java 8 Date and Time**

```Java
Statement st = conn.createStatement();
ResultSet rs = st.executeQuery("SELECT * FROM mytable WHERE columnfoo = 10");
while (rs.next()) {
    System.out.print("Column 1 returned ");
    LocalDate localDate = rs.getObject(1, LocalDate.class);
    System.out.println(localDate);
}
rs.close();
st.close();
```

**Writing Java 8 Date and Time**

```Java
LocalDate localDate = LocalDate.now();
PreparedStatement st = conn.prepareStatement("INSERT INTO mytable (columnfoo) VALUES (?)");
st.setObject(1, localDate);
st.executeUpdate();
st.close();
```

For more Java Object Types Mapped to JDBC Types, see the Table B.4 of the [JDBC specification](https://download.oracle.com/otndocs/jcp/jdbc-4_2-mrel2-spec/).

**MySQL Date and Time**

MySQL JDBC Driver (MySQL Connector/J) has some limitations on date and time handling, you should know them.

TIMESTAMP is the only MySQL data type designed to store instants. To preserve time instants, the server applies time zone conversions in incoming or outgoing time values when needed. Incoming values are converted by server from the connection session's time zone to Coordinated Universal Time (UTC) for storage, and outgoing values are converted from UTC to the session time zone. This does not occur for other types such as DATETIME.

Mixing up instant-representing and non-instant-representing date-time types when storing and retrieving values might give rise to unexpected results.

* When storing `java.sql.Timestamp` to, for example, a `DATETIME` column, you might not get back the same instant value when retrieving it into a client that is in a different time zone than the one the client was in when storing the value.

* When storing, for example, a `java.time.LocalDateTime` to a `TIMESTAMP` column, you might not be storing the correct UTC-based value for it, because the time zone for the value is actually undefined.

In order to avoid such errors, you are recommend setting a connection time zone that uses a monotonic clock by, for example, setting `connectionTimeZone=UTC`. If you need to store `Time zone` information, 
store it in a separated column, because MySQL doesn't have any data type like `TIMESTAMP WITH TIMEZONE` (may be in the future ...).

To use date time properly as you expect, read the connection parameters guide in the MySQL Connector/J:
* `preserveInstants={true|false}`: Whether to attempt to preserve time instant values by adjusting timestamps
* `connectionTimeZone={LOCAL|SERVER|user-defined-time-zone}`: Specifies how the server's session time zone (in reference to which the timestamps are saved onto the server) is to be determined by Connector/J
* `forceConnectionTimeZoneToSession={true|false}`: Controls whether the session time_zone variable is to be set to the value specified in connectionTimeZone.
* https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-time-instants.html

Take time to examine some examples to understand the different between `TIMESTAMP` and `DATETIME` in MySQL:

```sql
CREATE TABLE time_test (
ts TIMESTAMP,
dt DATETIME
);
```

```sql
INSERT INTO time_test(ts, dt) VALUES (NOW(), NOW());
```

```sql
SELECT * FROM time_test;
```

Then change your session time_zone

```sql
SELECT @@session.time_zone;

SET time_zone = "+08:00";
SET @@session.time_zone = "+08:00";
```

And then query again to see what different?


**References**:

* https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-known-issues-limitations.html
* https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-time-instants.html
* https://dev.mysql.com/doc/refman/8.0/en/date-and-time-literals.html
* https://dev.mysql.com/doc/refman/8.0/en/datetime.html
* https://stackoverflow.com/questions/62628725/jdbc-insert-timestamp-in-utc


# _. References

* https://jdbc.postgresql.org/documentation/
* https://dev.mysql.com/doc/connector-j/8.0/en/pg