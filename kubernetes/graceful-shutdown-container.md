## Kubernetes container rolling updates

Kubernetes will perform a rolling update to deploy new versions of Deployments. Rolling updates allow Deployments' update to take place with zero downtime by incrementally updating Pods instances with new ones. The new Pods will be scheduled on Nodes with available resources.

Similar to application Scaling, if a Deployment is exposed publicly, the Service will load-balance the traffic only to available Pods during the update. An available Pod is an instance that is available to the users of the application.

## How does Kubernetes terminate pods?

1. Send a SIGTERM signal to the main process (pid = 1) of containers
2. If a container doesn't terminate in a grace period, default to 30s, Kubernetes will send a SIGKILL to force terminate the container immediately.

There are two signals used to terminate a program:

* *SIGTERM* is used to cause a program termination. It is a polite way to ask a program to terminate. The program can either handle this signal, clean up resources and then exit, or it can ignore the signal.
* *SIGKILL* is used to cause immediate termination. It can't be handled or ignored by the process.

## Graceful shutdown in NodeJS

BFF is a NodeJS application. The http server in a NodeJS application has a close method that stops the server for receiving new connections and calls the callback once it finished handling all requests. We will catch the SIGTERM signal by ourself and perform a graceful shutdown process by calling the close method of the http server.

```JS
process.on('SIGTERM', function () {
  server.close(function () {
    process.exit(0);
  });
});
```

The program doesn’t exit until it finished processing and serving the last request. And after the SIGTERM signal it doesn’t handle more requests.

## Graceful shutdown in Springboot

Upgrade to Springboot 2.3.0

https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/html/spring-boot-features.html#boot-features-graceful-shutdown

Graceful shutdown is supported with all four embedded web servers (Jetty, Reactor Netty, Tomcat, and Undertow) and with both reactive and Servlet-based web applications. When enabled using `server.shutdown=graceful`, upon shutdown, the web server will no longer permit new requests and will wait for a grace period for active requests to complete. The grace period can be configured using `spring.lifecycle.timeout-per-shutdown-phase`

Projects have been upgraded to Spring boot 2.4.1:

* account-service
* db-node-batch-service
* db-node-service
* dns-service
* email-error-relector
* express-scheduler
* file-service
* log-service
* log-service-register
* mail-agent
* mail-controller
* mail-service
* schedule-trigger-scheduler
* spiral-service

Projects haven't been upgraded to the Spring boot version greater than or equal 2.3.0:

* click-count-service (2.2.4)
* contents-service (2.2.5)
* site-service (2.0.4)

## Graceful shutdown in Springboot version before 2.3.0

Implement `ApplicationListener<ContextClosedEvent>`

```
public class JettyServerGracefulShutdown implements ApplicationListener<ContextClosedEvent> {
  private static Server server;

  @Override
  public void onApplicationEvent(ContextClosedEvent event) {
    try {
      server.stop();
    } catch (Exception e) {
      log.error("Fail to shutdown gracefully.", e);
    }
  }

  public static void setServer(Server server) {
    JettyServerGracefulShutdown.server = server;
  }
}
```

Configure bean

```
@Bean
  public JettyServerGracefulShutdown initGracefulShutdownBean() {
    return new JettyServerGracefulShutdown();
  }

  @Bean
  JettyEmbeddedServletContainerFactory embeddedServletContainerFactory(
    @Value("${server.port}") int port,
    @Value("${server.jetty.threadPool.maxThreads}") int maxThreads,
    @Value("${server.jetty.threadPool.minThreads}") int minThreads,
    @Value("${server.jetty.threadPool.idleTimeout}") int idleTimeout,
    @Value("${server.jetty.shutdown.waitTime}") int shutdownWaitTime) {

    JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory(port);

    factory.addServerCustomizers(server -> {

      QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
      threadPool.setMaxThreads(maxThreads);
      threadPool.setMinThreads(minThreads);
      threadPool.setIdleTimeout(idleTimeout);

      JettyServerGracefulShutdown.setServer(server);

      if (shutdownWaitTime > 0) {
        StatisticsHandler handler = new StatisticsHandler();
        handler.setHandler(server.getHandler());
        server.setHandler(handler);
        log.info("Shutdown wait time: " + shutdownWaitTime + "ms");
        server.setStopTimeout(shutdownWaitTime);
        // We will stop it through GracefulShutdownBean class.
        server.setStopAtShutdown(false);
      }
    });

    return factory;
  }
```  

## Kong Gateway graceful shutdown

The SIGQUIT signal will be sent to nginx to stop this container, to give it an opportunity to stop gracefully (i.e, finish processing active connections). The Docker default is SIGTERM, which immediately terminates active connections. 

http://nginx.org/en/docs/control.html

To override the signal that sent to the Kong container (base on Nginx), using this instruction in the Dockerfile

```
STOPSIGNAL SIGQUIT
```

The newer version of Kong has done this instruction

https://github.com/Kong/docker-kong/pull/259/files

## Other middleware

### Redis 

The SIGTERM and SIGINT signals tell Redis to shutdown gracefully. When this signal is received the server does not immediately exit as a result, but it schedules a shutdown very similar to the one performed when the SHUTDOWN command is called.

https://redis.io/topics/signals

### Rados-gateway

Have not found any useful information about the graceful shutdown for Rados-gateway
