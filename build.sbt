organization := ""
name         := """play-websocket-seed"""
version      := "0.1"

scalaVersion := "2.12.11"
scalacOptions in (Compile, doc) := Seq("-diagrams")
scalacOptions in Compile ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint")
javacOptions  in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")
javaOptions   in run ++= Seq("-Xms128m", "-Xmx1024m")

val akkaVersion = "2.6.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  ws,
  guice,
  "com.typesafe.akka" %% "akka-actor"                  % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics"        % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools"          % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding"       % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-typed"          % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding-typed" % akkaVersion,
)

// dependencyOverrides += "com.typesafe.akka" %% "akka-actor" % akkaVersion

libraryDependencies ++= Seq(
  "org.scalatest"          %% "scalatest"          % "3.0.7" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
)

PlayKeys.devSettings += "play.server.http.idleTimeout" -> "120 s"
PlayKeys.devSettings += "play.server.websocket.frame.maxLength" -> "64k"

fork in run := true
