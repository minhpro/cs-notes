package my_group;

import jakarta.servlet.ServletContext;
import my_group.myapp.JWTService;
import my_group.web.filter.CrossOriginFilter;
import my_group.web.filter.LogFilter;
import my_group.web.filter.JWTFilter;
import my_group.web.filter.ReflectResponseFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


/**
 * Hello world!
 *
 */
@Configuration
public class App 
{
    static final Logger LOG = LogManager.getLogger(App.class);
    static JWTService jwtService;

    public static void main( String[] args ) throws Exception {
        jwtService = new JWTService(KeyPairGenerator.getInstance("Ed25519").generateKeyPair());
        AnnotationConfigWebApplicationContext ctx = getWebApplicationContext();
        // Create and register the DispatcherServlet
        // It automatically calls ctx.refresh() properly, don't call it manually
        DispatcherServlet dispatcherServlet = new DispatcherServlet(ctx);

        // Create a ServletContextHandler with contextPath.
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/myapp");
        // Add Spring DispatchServlet as the root servlet
        servletContextHandler.addServlet(dispatcherServlet, "/*");
        ServletContext servletContext = servletContextHandler.getServletContext();
        Map<String, String> allowedOriginsParam = Map.of(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "localhost:8090, example.com");
        servletContext.addFilter("CORSFilter", new CrossOriginFilter(allowedOriginsParam))
                .addMappingForUrlPatterns(null, false, "/*");
        servletContext.addFilter("logFilter", LogFilter.class)
                .addMappingForUrlPatterns(null, false, "/hello");
        servletContext.addFilter("logResponseFilter", new ReflectResponseFilter(System.out))
                .addMappingForUrlPatterns(null, false, "/book");
        servletContext.addFilter("JWT token filter", new JWTFilter(jwtService))
                .addMappingForUrlPatterns(null, false, "/greeting");

        int maxThreads = 100;
        int minThreads = 10;
        int idleTimeout = 120;

        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

        Server server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setHost("127.0.0.1");
        connector.setPort(8090);
        server.addConnector(connector);

        // Link the context to the server.
        server.setHandler(servletContextHandler);

        server.start();
        LOG.info("Server started at host 127.0.0.1 and port 8090");
    }

    public static AnnotationConfigWebApplicationContext getWebApplicationContext() throws IOException {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.scan(App.class.getPackageName());
        ResourcePropertySource propertySource = new ResourcePropertySource("propertyFromFile", "classpath:application.properties");
        MutablePropertySources propertySources = ctx.getEnvironment().getPropertySources();
        propertySources.addLast(propertySource);
        return ctx;
    }

    @Bean
    public JWTService jwtService() {
        return jwtService;
    }
}
