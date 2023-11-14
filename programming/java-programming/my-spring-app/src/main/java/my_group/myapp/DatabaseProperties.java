package my_group.myapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class DatabaseProperties {
    @Value("${database.url}")
    String url;
    @Value("${database.user}")
    String user;
    @Value("${database.password}")
    String password;
    @Value("${database.ssl}")
    boolean ssl;

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSsl() {
        return ssl;
    }
}
