enablePlugins(GitVersioning)
enablePlugins(GitBranchPrompt)
enablePlugins(BuildInfoPlugin)

name := "$name$"
organization := "$organization$"
scalaVersion := "$scala_version$"

dependencyCheckSuppressionFiles += file("suppress-checks.xml")
dependencyCheckFailBuildOnCVSS := 1
dependencyCheckRetireJSAnalyzerEnabled := Option(false)

scalacOptions += "-deprecation"

buildInfoOptions += BuildInfoOption.BuildTime
buildInfoOptions += BuildInfoOption.ToJson
buildInfoPackage := "$package$"
buildInfoOptions += BuildInfoOption.Traits(
  "$package$.logging.LoggerContextInfo"
)

buildInfoKeys := Seq[BuildInfoKey](name, version, "gitHash" -> git.gitHeadCommit.value.getOrElse("emptyRepository")
)

val macVersion = "2.5.0"
val akkaVersion = "2.6.18"
val akkaHttpVersion = "10.2.8"

lazy val compileDependencies = {
  Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
    "ch.qos.logback" % "logback-classic" % "1.2.6",
    "org.codehaus.janino" % "janino" % "3.1.6",
    "net.logstash.logback" % "logstash-logback-encoder" % "6.6",
    "com.softwaremill.macwire" %% "macros" % macVersion,
    "com.softwaremill.macwire" %% "util" % macVersion,
    "com.softwaremill.macwire" %% "proxy" % macVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "de.heikoseeberger" %% "akka-http-play-json" % "1.38.2"
  )
}


libraryDependencies ++= compileDependencies

lazy val testDependencies = Seq(
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "org.scalatest" %% "scalatest" % "3.2.11",
  "org.scalamock" %% "scalamock" % "5.2.0"
).map(_ % "test")
libraryDependencies ++= testDependencies
