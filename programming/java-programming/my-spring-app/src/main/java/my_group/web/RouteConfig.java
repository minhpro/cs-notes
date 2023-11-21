package my_group.web;

import jakarta.servlet.ServletException;
import my_group.myapp.JWTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration(proxyBeanMethods = false)
public class RouteConfig {

    @Autowired
    JWTService jwtService;

    @Bean
    RouterFunction<ServerResponse> appRouter() {
        return  route()
                .GET("/hello", this::hello)
                .POST("/login", this::login)
                .build();
    }

    public ServerResponse hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body("Hello!");
    }

    public ServerResponse login(ServerRequest request) throws ServletException, IOException {
        LoginParam loginParam = request.body(LoginParam.class);
        if ("admin".equals(loginParam.username) && "admin".equals(loginParam.password)) {
            return ServerResponseUtil.okResponse(jwtService.generateToken(loginParam.username));
        }
        return ServerResponseUtil.errorResponse("invalidLogin");
    }

    record LoginParam(String username, String password){}
}
