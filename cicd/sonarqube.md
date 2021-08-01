Jenkins sonarqube stage 

```groovy
stage('Analysis') {
    steps {
        sh 'mvn sonar:sonar \
            -Dsonar.login=$SONAR_TOKEN \
            -Dsonar.projectName=<project-name> \
            -Dsonar.projectKey=<project-key> \
            -Dsonar.host.url=$SONAR_SERVER'
    }
}
```

maven jacoco code coverage 

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.3</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <!-- attached to Maven test phase -->
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```