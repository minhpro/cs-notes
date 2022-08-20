HikariCP is a high-performance JDBC connection pool.

https://github.com/brettwooldridge/HikariCP

Every connection you take out of a connection pool, you have to *return* it to the pool.

You can using `Connection.close()` - the pool hands out a wrapper function where `close()` doesn't physically close the connection, only returns it.

```
PreparedStatement preparedStatement = con.prepareStatement(sql);
preparedStatement.executeQuery();
preparedStatement.close();
con.close(); // this returns the connection to the pool
```
