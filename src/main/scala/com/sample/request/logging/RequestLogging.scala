package com.sample.request.logging

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.http.scaladsl.settings.ServerSettings
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.LoggingMagnet
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.io.StdIn

object RequestLogging extends App {
  val config                    = ConfigFactory.load()
  implicit val system           = ActorSystem("http-server", config)
  val serverSettings            = ServerSettings(config)
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val logger           = Logging(system, getClass)

  def myLoggingFunction(logger: LoggingAdapter)(req: HttpRequest): Unit = {
    logger.info(req.toString())
  }

  val routes = logRequest(LoggingMagnet(log => myLoggingFunction(log))) {
    path("hello") {
      complete(
        Future(HttpResponse(status = StatusCodes.OK, entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, "Hello World")))
      )
    }
  }

  val future = Http().bindAndHandle(handler = routes,
                                    interface = "localhost",
                                    port = serverSettings.defaultHttpPort,
                                    settings = serverSettings,
                                    log = logger)
  logger.info(s"Server Started on ${serverSettings.defaultHttpPort}\nPress Enter to stop...")
  StdIn.readLine()
  future
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
