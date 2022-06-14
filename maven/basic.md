## Create maven project

https://www.journaldev.com/33593/creating-java-project-maven-archetypes

mvn -B archetype:generate -DgroupId=com.example.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4

mvn archetype:generate -DgroupId=com.example.app -DartifactId=java-app -DarchetypeArtifactId=maven-archetype-quickstart

```
mvn archetype:generate -DgroupId=jp.co.softbank -DartifactId=retailapp-performance-test -DarchetypeArtifactId=maven-archetype-quickstart
```

## POM

https://maven.apache.org/guides/introduction/introduction-to-the-pom.html

## Install manual

mvn install:install-file -Dfile=i<path-to-jar> -DpomFile=<path-to-pom>

## Create jar with dependencies

```

<build>
    <plugins>
        <plugin>
            <artifactId>maven-assembly-plugin</artifactId>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>com.example.Main</mainClass>
                    </manifest>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Build jar file

`mvn clean package`

to skip assembly

`mvn clean package -Dassembly.skipAssembly=true`

and exec

`mvn exec:java -Dexec.mainClass=com.example.Main`

## Create jar with libs

Create jar and libs which includes all dependencies

```
<build>
    <plugins>
         <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <version>3.2.0</version>
                <executions>
                    <execution>
                        <id>copy-dependencies</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>
                                ${project.build.directory}/libs
                            </outputDirectory>
                            <includeScope>runtime</includeScope>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.2.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>libs/</classpathPrefix>
                            <mainClass>com.example.Main</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
    <plugins>
</build>
```

## Create fat-jar with Maven Shade Plugin

```
<build> 
    <plugins> 
        <plugin> 
            <groupId>org.apache.maven.plugins</groupId>    
            <artifactId>maven-shade-plugin</artifactId>             
            <executions> 
                <execution> 
                    <phase>package</phase> 
                    <goals> 
                        <goal>shade</goal> 
                    </goals> 
                    <configuration> 
                        <transformers> 
                             <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer"> 
                                <mainClass>
                                    com.example.Main
                                </mainClass> 
                            </transformer> 
                        </transformers> 
                        <createDependencyReducedPom>
                            false
                        </createDependencyReducedPom>     
                    </configuration> 
                </execution> 
            </executions> 
        </plugin> 
    </plugins> 
</build>
```


