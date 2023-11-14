package com.example;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import jakarta.servlet.ServletException;

public class AppServlet {
    final static String CONTEXT_PATH = "/myapp";
    public static void main( String[] args ) throws ServletException {
        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(AppServlet.class.getClassLoader())
                .setContextPath(CONTEXT_PATH)
                .setDeploymentName("myapp")
                .addServlets(
                        Servlets.servlet("HelloServlet", HelloServlet.class)
                                .addInitParam("message", "Hello World")
                                .addMapping("/*"),
                        Servlets.servlet("MyServlet", HelloServlet.class)
                                .addInitParam("message", "MyServlet")
                                .addMapping("/myservlet")
                );
        DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(servletBuilder);
        deploymentManager.deploy();

        HttpHandler servletDispatchHandler = deploymentManager.start();
        PathHandler pathHandler = Handlers.path(Handlers.redirect(CONTEXT_PATH))
                .addPrefixPath(CONTEXT_PATH, servletDispatchHandler);

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(pathHandler)
                .build();
        server.start();
    }
}
