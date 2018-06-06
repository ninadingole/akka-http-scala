name := "akka-http-scala"

version := "0.1"

scalaVersion := "2.12.5"

val akkaHttpVersion = "10.1.0"
val akkaVersion = "2.5.11"


releaseIgnoreUntrackedFiles := true

libraryDependencies += "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor"  % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http2-support" % akkaHttpVersion
libraryDependencies += "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.14.0"
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion     % Test

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.3"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.1"


publishTo := Some(Resolver.file("file",new File(".")))

publishArtifact:= false

enablePlugins(SiteScaladocPlugin)
enablePlugins(OpenShiftDockerPlugin)
enablePlugins(CustomReleasePlugin)
previewFixedPort := Some(8090)
