package com.sample.first

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.http.scaladsl.settings.ServerSettings
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

object HelloWorld extends HttpApp {

  implicit val system           = ActorSystem("http-server")
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  override protected def routes: Route = {
    path("hello") {
      complete("Hello World")
    }
  }
}

object App {
  def main(args: Array[String]): Unit = {
    lazy val config    = ConfigFactory.load()
    val serverSettings = ServerSettings(config)
    HelloWorld.startServer("localhost", serverSettings.defaultHttpPort, serverSettings)
  }
}
