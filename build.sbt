name := "spark-valuta-forecast"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  // spark
  "org.apache.spark" %% "spark-core" % "2.2.0",
  "org.apache.spark" %% "spark-sql" % "2.2.0",
  "org.apache.spark" %% "spark-mllib" % "2.2.0",
  "org.apache.spark" %% "spark-streaming" % "2.2.0",

  // config
  "com.typesafe" % "config" % "1.3.1",

  // logging
//  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
//  "org.slf4j" % "slf4j-simple" % "1.7.25",
//  "ch.qos.logback" % "logback-classic" % "1.2.1",

  // test
  "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Xlint:missing-interpolator",
  "-Ywarn-unused",
  "-Ywarn-dead-code",
  "-language:_",
  "-encoding", "UTF-8",
  "-Xmax-classfile-name", "240" // for docker container
)