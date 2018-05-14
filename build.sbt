name := "nifiscalaclient"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.7"

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "1.1.5",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % "2.9.2",
  "com.fasterxml.jackson.module" % "jackson-module-paranamer" % "2.9.2",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.2",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.apache.kafka" %% "kafka" % "0.9.0.1",
  "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0",
  "com.novocode" % "junit-interface" % "0.8" % "test->default"
)

