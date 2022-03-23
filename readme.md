## Description
Aws lambda that implements JetMail java client to send mails

## Environment variables 
- MAILJET_APIKEY_PUBLIC
- MAILJET_APIKEY_PRIVATE
- PROJECT_CONTACT_MAIL

If you want to use infra-as-code, you have to set these variables in AWS Secrets Manager

## Terraform
[terraform/main.tf](terraform/main.tf) allow to create aws lambda function with logging

## CICD
[Github CI](.github/workflows/aws.yml) allow to build and deploy lambda function into two environments "dev" and "prod"

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

## Used technology
- AWS Lambda
- Terraform
- Github Actions
- JAVA 11 (limit due to aws support)
- JetMail
