# Getting started

Maven là gì? Nói đơn giản thì Maven là một build tool cho project Java, nhưng Maven cũng không chỉ là build tool, có thể gọi chung nó là project tool. Thực tế thì gọi nó là gì, hay xếp nó vào loại nào cũng không quá quan trọng, thay vào đó hãy cùng tìm hiểu xem nó giúp gì chúng ta trong quá trình phát triển các project Java nhé.

Nhìn chung thì mỗi project đều các cần được: built, tested, packaged, documented và deployed. Tuy vậy, sẽ có vô số những khác biệt (variation) ở mỗi steps trên cho các project khác nhau. Để tránh sự phức tạp và khó quản lý do những khác biệt này tạo nên, Maven sẽ giúp chúng ta nhìn nhận và sử dụng chúng dưới **một đường hướng chung, rõ ràng** (well defined path) dưới hình thức một tập **mẫu** (set of patterns) được shared chung cho mọi người trong dự án. Dưới đây là nguyên mô tả official của Maven:

> In a nutshell Maven is an attempt to *apply patterns to a project's build infrastructure in order to promote comprehension and productivity by providing a clear path in the use of best practices*. Maven is essentially a project management and comprehension tool and as such provides a way to help with managing:
>
> - Builds
> - Documentation
> - Reporting
> - Dependencies
> - SCMs
> - Releases
> - Distribution

Maven có lợi ích như thế nào đối với quá trình phát triển dự án?

> Help you employing standard conventions and practices to accelerate your development cycle.

References:
- https://maven.apache.org/guides/getting-started/index.html
- https://maven.apache.org/background/philosophy-of-maven.html
- [Download Maven](https://maven.apache.org/download.cgi)
- [Cấu hình Maven](https://maven.apache.org/guides/mini/guide-configuring-maven.html)

## Created project

Để tạo project, sử dụng câu lệnh mvn sau để generate một cấu trúc project đơn giản.

```sh
mvn -B archetype:generate -DgroupId=my-group -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4
```

Trong thư mục của project sẽ có một file `pom.xml` được dùng để cấu hình project. File `pom.xml` chứa Project Object Model (POM) cho project.

References:
- Các thông tin cơ bản của POM: https://maven.apache.org/guides/getting-started/index.html#how-do-i-make-my-first-maven-project
- Layout chuẩn của một project: https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html

**Cách compile project**

```sh
mvn compile
```

Các classes được compile sẽ nằm ở thư mục `${project.basedir}/target/classes`.

**Cách compile và run unit tests**

```sh
mvn test
```

**Các tạo một JAR**

```sh
mvn package
```

File jar có tên dựa theo format `<artifactId>-<version>.jar` được tạo ra và nằm trong thư mục `${project.basedir}/target`

**Install the artifact in the local repostiory**

```sh
mvn install
```

The artifact đã generated (the JAR file và your `pom.xml` file) được install vào your local repositry (`${user.home}/.m2/repository` is the default location).

**Cách add resource to JAR**

Đơn giản là thêm vào folder `{project.basedir}/src/main/resources`.

Sẽ có một số file tiêu chuẩn được tự động thêm vào (nếu ko có) bời Maven như `META-INF/MANIFEST.MF`, `pom.xml` và `pom.properties` file.

References:

- https://maven.apache.org/guides/getting-started/index.html#how-do-i-add-resources-to-my-jar

**Cách filter (variable interpolation) resources**

References:
- https://maven.apache.org/guides/getting-started/index.html#how-do-i-filter-resource-files
- https://maven.apache.org/plugins/maven-resources-plugin/examples/filter.html

**Sử dụng external dependencies**

Khi cần sử dụng thư viện bên ngoài thì khai báo **dependency** trong thẻ **dependencies**. Ví dụ, muốn sử dụng JUnit.

```xml
<dependencies>
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.11</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

References:
- https://maven.apache.org/guides/getting-started/index.html#how-do-i-use-external-dependencies
- https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html

**Cách chỉ định main class**


```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-jar-plugin</artifactId>
      <!-- Here come other details
      ...
      -->
      <configuration>
        <archive>
          <manifest>
            <addClasspath>true</addClasspath>
            <mainClass>fully.qualified.MainClass</mainClass>
          </manifest>
        </archive>
      </configuration>
      <!-- Here come other details
      ...
      -->
    </plugin>
  </plugins>
</build>
```

References:
- https://maven.apache.org/plugins/maven-jar-plugin/
- https://maven.apache.org/shared/maven-archiver/
- https://docs.oracle.com/javase/tutorial/deployment/jar/index.html

**Cách copy dependencies**

Sử dụng command

```sh
mvn dependency:copy-dependencies
```

Với cách này, tất cả dependencies được copy vào thư mực `target/dependency`, và có thể run jar file theo cách

```sh
java target/<artifactId>-<version>.jar
```

Nhưng trước đó cần cấu hình `maven-jar-plugin`

```xml
<manifest>
  <addClasspath>true</addClasspath>
  <classpathPrefix>dependency</classpathPrefix>
  <mainClass>fully.qualified.MainClass</mainClass>
</manifest>
```

Khi này trong file Manifest sẽ có dòng

```
Class-Path: dependency/lib1.jar dependency/lib2.jar ...
```

References:
- https://maven.apache.org/shared/maven-archiver/examples/classpath.html

**Cách tạo executable Jar (fat Jar)**

Sử dụng shade-plugin

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-shade-plugin</artifactId>
  <version>3.5.1</version>
  <executions>
    <execution>
      <phase>package</phase>
      <goals>
        <goal>shade</goal>
      </goals>
      <configuration>
        <shadedArtifactAttached>true</shadedArtifactAttached>
        <transformers>
          <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
            <mainClass>com.example.App</mainClass>
          </transformer>
        </transformers>
      </configuration>
    </execution>
  </executions>
</plugin>
```

Sau khi chạy `mvn package`, trong thư mục `target` sẽ có hai file jar
* một file là không chứa dependencies
* một file (shaded) chứa dependencies

References:
- https://www.baeldung.com/executable-jar-with-maven
- https://gist.github.com/simonwoo/04b133cb0745e1a0f1d6

**Generate webapp project***


```sh
mvn archetype:generate \
    -DarchetypeGroupId=org.apache.maven.archetypes \
    -DarchetypeArtifactId=maven-archetype-webapp \
    -DgroupId=my-group \
    -DartifactId=my-webapp
```

References:
- https://maven.apache.org/guides/getting-started/index.html#how-do-i-build-other-types-of-projects

**Cách build nhiều project cùng lúc**


References:
- https://maven.apache.org/guides/getting-started/index.html#how-do-i-build-more-than-one-project-at-once

# Cấu trúc POM

References:
- https://maven.apache.org/guides/introduction/introduction-to-the-pom.html
- https://maven.apache.org/ref/3.9.5/maven-model/maven.html

# Build Lifecircle

References:
- https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html

# Plugins

References:
- Available plugins: https://maven.apache.org/plugins/
- Develop plugin: https://maven.apache.org/plugin-developers/index.html