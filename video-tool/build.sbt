name := "wtf-video-tool"
version := "0.1"
scalaVersion := "3.5.0"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.19" % Test,
)
