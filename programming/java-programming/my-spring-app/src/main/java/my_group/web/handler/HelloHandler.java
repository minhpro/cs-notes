package my_group.web.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class HelloHandler {
    final static Logger LOG = LogManager.getLogger(HelloHandler.class);
    public ServerResponse hello(ServerRequest request) {
        LOG.info("Start request handler {}", request.servletRequest().getRequestURI());
        LOG.info("My header {}", request.servletRequest().getHeader("myHeader"));
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body("Hello!");
    }
}
