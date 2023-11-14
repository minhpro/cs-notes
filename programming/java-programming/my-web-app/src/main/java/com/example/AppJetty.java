package com.example;

import jakarta.servlet.DispatcherType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.EnumSet;

/**
 * <a href="https://eclipse.dev/jetty/documentation/jetty-12/programming-guide/index.html#pg-server-http-handler-use-servlet">Jetty Servlet</a>
 */
public class AppJetty {
    public static void main(String[] args) throws Exception {
        final Logger LOG = LogManager.getLogger(App.class);
        LOG.info("Hello");
        int maxThreads = 100;
        int minThreads = 10;
        int idleTimeout = 120;

        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

        Server server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.addConnector(connector);

        // Create a ServletContextHandler with contextPath.
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/myapp");
        // Link the context to the server.
        server.setHandler(context);

        // Add the Servlet
        ServletHolder servletHolder = context.addServlet(HelloServlet.class, "/hello/*");
        // Configure the Servlet with init-parameters.
        servletHolder.setInitParameter("name", "MyServlet");

        // Add the CrossOriginFilter to protect from CSRF attacks.
        FilterHolder filterHolder = context.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        // Configure the filter.
        filterHolder.setAsyncSupported(true);

        server.start();
    }
}
