name := "wtf-video-tool"
version := "0.1"
scalaVersion := "3.4.1"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.18" % Test,
)
