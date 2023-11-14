package my_group.myapp;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class PgDataSource {
    String url;
    Properties props;

    public PgDataSource(DatabaseProperties properties) {
        url = properties.getUrl();
        props = new Properties();
        props.setProperty("user", properties.getUser());
        props.setProperty("password", properties.getPassword());
        props.setProperty("ssl", String.valueOf(properties.isSsl()));
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, props);
    }
}
