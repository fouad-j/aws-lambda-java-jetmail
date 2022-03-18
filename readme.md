
## Build project
To build the project run `gradle buildZip`

This task create a deployment package with the function's code and dependencies as Zip build type

[More details here...](https://docs.aws.amazon.com/lambda/latest/dg/java-package.html#java-package-gradle)
```gradle
task buildZip(type: Zip) {
    from test
    from compileJava
    from processResources
    into('lib') {
        from configurations.runtimeClasspath
    }
}
```
