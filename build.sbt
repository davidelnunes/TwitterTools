
name := "TwitterTools"

version := "1.0"

scalaVersion := "2.11.5"

assemblyJarName in assembly := "twitter-tools.jar"

mainClass in assembly := Some("cli.TwitterTools")

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "org.twitter4j" % "twitter4j-stream" % "4.0.2",
  "com.github.tototoshi" %% "scala-csv" % "1.1.2",
  "com.typesafe" % "config" % "1.2.1",
  "org.rogach" %% "scallop" % "0.9.5"
)

