package my_group;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.sql.SQLException;
import java.util.EnumSet;

/**
 * Hello world!
 *
 */
public class App 
{
    static final Logger LOG = LogManager.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ResourcePropertySource propertySource = new ResourcePropertySource("propertyFromFile", "classpath:application.properties");
        MutablePropertySources propertySources = ctx.getEnvironment().getPropertySources();
        propertySources.addLast(propertySource);

        ctx.scan(App.class.getPackageName());

        int maxThreads = 100;
        int minThreads = 10;
        int idleTimeout = 120;

        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

        Server server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setHost("127.0.0.1");
        connector.setPort(8090);
        server.addConnector(connector);

        // Create and register the DispatcherServlet
        // It automatically calls ctx.refresh() properly, don't call it manually
        DispatcherServlet dispatcherServlet = new DispatcherServlet(ctx);

        // Create a ServletContextHandler with contextPath.
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/myapp");
        // Add Spring DispatchServlet as the root servlet
        servletContextHandler.addServlet(dispatcherServlet, "/*");

        // Link the context to the server.
        server.setHandler(servletContextHandler);

        server.start();
        LOG.info("Server started at host 127.0.0.1 and port 8090");

//        TestService testService = ctx.getBean(TestService.class);
//        testService.testQuery();
    }
}
