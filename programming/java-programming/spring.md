## @Component

Indicates that an annotated class is a "component". Such classes are considered as candidates for auto-detection when using annotation-based configuration and classpath scanning and registered in the context as Spring beans.

## How to use environment variables

## Docker


https://spring.io/guides/topicals/spring-boot-docker/

## Spring boot get started

https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html#getting-started

File `build.gradle`

```
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.1.5'
}

apply plugin: 'io.spring.dependency-management'

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

repositories {
    mavenCentral()
}

dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-web'
}

```

## Spring security

https://docs.spring.io/spring-security/reference/servlet/getting-started.html#hello-expectations

If you define a @Configuration with a SecurityFilterChain bean in your application, it switches off the default webapp security settings in Spring Boot.


## Jasper report

https://github.com/manish-in-java/spring-mvc-jasper/blob/master/src/main/scala/org/example/web/servlet/jasper/GraphicsServlet.scala