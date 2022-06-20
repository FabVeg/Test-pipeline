# Introduction 
Contains a default test suite for FlowConnector and TableConnector.

# Getting Started
1. Add the dependency to this library in the POM of your project as a scope 'test'.
```xml
<dependency>
    <groupId>com.alten.lab.ssde</groupId>
    <artifactId>connector-test-suite</artifactId>
    <version>3.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```
2. Create test classes extending from FlowConnectorTest or TableConnectorTest in your project.
3. Implements all unimplemented methods and run your tests. 
