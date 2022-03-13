# Getting Started

This service is for booking tickets.

## How to start application in local?
If you want to start it in local, You can follow those steps:
* run  ```./auto/localstack/start``` to set up local aws env
* run ```./auto/localstack/sqs-set-up``` to create sqs 
* run ```./gradlew bootRun``` to start application

After finish running, you can stop local stack env, using below command: 

```./auto/localstack/stop```

3rd party set up in local env is in progress.

## How to run test?
```./gradlew test```
