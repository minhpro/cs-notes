package com.example;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;
import io.undertow.util.Headers;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        HttpHandler helloHandler = exchange -> {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Hello World");
        };

        AccessLogReceiver consoleLogReceiver = System.out::println;

        AccessLogHandler accessLogHandler = new AccessLogHandler(
                helloHandler,
                consoleLogReceiver,
                "%s",
                ClassLoader.getPlatformClassLoader()
        );
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(accessLogHandler)
                .build();
        server.start();
    }
}
