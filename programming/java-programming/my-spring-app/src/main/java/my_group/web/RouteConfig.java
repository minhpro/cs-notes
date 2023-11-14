package my_group.web;

import my_group.web.handler.HelloHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration(proxyBeanMethods = false)
public class RouteConfig {
    @Bean
    RouterFunction<ServerResponse> helloRoute(HelloHandler helloHandler) {
        return  route()
                .GET("/hello", helloHandler::hello)
                .build();
    }
}
