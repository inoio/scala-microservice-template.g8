# A [Giter8][g8] template for Scala Microservices
## Field of application
A blueprint for a scala micro service that processes http requests and that can be deployed in k8s.
## usage
`g8 https://github.com/inoio/scala-microservice-template.git` or
`cd ..; sbt new file://scala-microservice-template.g8`

### cleanup 
* adjust versions `sbt dependencyUpdates`
* remove unnecessary libs

### run
sbt run

