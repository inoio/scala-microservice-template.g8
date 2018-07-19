enablePlugins(GitVersioning)
enablePlugins(GitBranchPrompt)
enablePlugins(BuildInfoPlugin)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "$organization$",
      scalaVersion := "$scala_version$"
    )),
    name := "$name$"
  ).settings(dependencyCheckSuppressionFiles += file("suppress-checks.xml"))
  .settings(dependencyCheckFailBuildOnCVSS := 1)
  .settings(
    buildInfoOptions += BuildInfoOption.BuildTime,
    buildInfoOptions += BuildInfoOption.ToJson,
    buildInfoPackage := "$package$",
    buildInfoOptions += BuildInfoOption.Traits("$package$.logging.LoggerContextInfo"),
    buildInfoKeys := Seq[BuildInfoKey](name, version, "gitHash" -> git.gitHeadCommit.value.getOrElse("emptyRepository"))
  )

lazy val compileDependencies = {
  val macVersion = "2.3.1"
  val prometheusVersion = "0.4.0"

  Seq(
    "com.typesafe.scala-logging" %% "scala-logging"% "3.7.1",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.codehaus.janino" % "janino" % "3.0.8",
    "net.logstash.logback" % "logstash-logback-encoder" % "4.11",
    "com.softwaremill.macwire" %% "macros" % macVersion,
    "com.softwaremill.macwire" %% "util" % macVersion,
    "com.softwaremill.macwire" %% "proxy" % macVersion,
    "io.prometheus" % "simpleclient" % prometheusVersion,
    "io.prometheus" % "simpleclient_hotspot" % prometheusVersion,
    "fr.davit" %% "akka-http-prometheus" % "0.1.1",
    "com.typesafe.akka" %% "akka-slf4j" % "2.5.12",
    "com.typesafe.akka" %% "akka-http" % "10.1.3",
    "com.typesafe.akka" %% "akka-actor" % "2.5.8",
    "com.typesafe.akka" %% "akka-stream" % "2.5.8"
  )
}


libraryDependencies ++= compileDependencies

lazy val testDependencies = Seq(
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.5",
  "org.scalatest" %% "scalatest" % "3.0.1",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0"
).map(_ % "test")
libraryDependencies ++= testDependencies
