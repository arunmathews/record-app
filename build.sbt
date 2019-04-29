organization := "com.compchallenge.record"

name := "RecordWebApp"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.8"

test in assembly := {}

assembly / assemblyExcludedJars := {
  val cp = (assembly / fullClasspath).value
  cp filter {_.data.getName == "slf4j-nop-1.6.4.jar"}
}

assembly / assemblyMergeStrategy := {
  case PathList("mime.types") =>
    MergeStrategy.last
  case "conf/logback.xml" =>
    MergeStrategy.discard
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}

assembly / assemblyJarName := "commandline-assembly.jar"

val ScalatraVersion = "2.6.+"
val SlickVersion = "3.3.0"
val Json4sVersion = "3.6.5"
val EnumeratumVersion = "1.5.13"
val FlywayVersion = "5.2.4"
val JodaVersion = "2.10.1"
val LogbackVersion = "1.2.3"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  //JSON
  "org.json4s" % "json4s-core_2.12" % Json4sVersion,
  "org.json4s" % "json4s-jackson_2.12" % Json4sVersion,
  "org.json4s" % "json4s-ext_2.12" % Json4sVersion,
  //Configuration
  "com.typesafe" % "config" % "1.3.3",
  "com.iheart" %% "ficus" % "1.4.5",
  //Database
  "com.typesafe.slick" %% "slick" % SlickVersion,
  "com.mchange" % "c3p0" % "0.9.5.4",
  "com.h2database" % "h2" % "1.4.199",
  "org.flywaydb" % "flyway-core" % FlywayVersion,
  //Datetime
  "joda-time" % "joda-time" % JodaVersion,
  //General utils
  "com.beachape" %% "enumeratum" % EnumeratumVersion,
  "com.beachape" %% "enumeratum-json4s" % EnumeratumVersion,
  "org.apache.httpcomponents" % "httpcore" % "4.4.11",
  //Test
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
  "org.scalatest" %% "scalatest" % "3.0.7" % "test",
  //Runtime
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.9.v20180320" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
)

containerPort in Jetty := 9080

enablePlugins(ScalatraPlugin)
