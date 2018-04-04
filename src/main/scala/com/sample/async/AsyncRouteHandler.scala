package com.sample.async

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.io.StdIn

object AsyncRouteHandler extends App {
  val config                      = ConfigFactory.load()
  implicit val system             = ActorSystem("blocking-ex", config)
  implicit val materialzier       = ActorMaterializer()
  implicit val blockingDispatcher = system.dispatchers.lookup("blocking-io-dispatcher")
  val logger                      = Logging(system, getClass)
//  implicit val executionContext = system.dispatcher  // Dont use this as its a http dispatcher and will block akka system

  def doBlockingIO(): Future[String] = {
    Future {
      logger.info("Will block for 4 sec...")
      Thread.sleep(4000)
      logger.info("sending response")
      "Done Blocking, Here is the response."
    }
  }

  val routes = (httpRequest: HttpRequest) =>
    Future[HttpResponse] {

      val future: Future[String] = doBlockingIO()
      logger.info("IO call done...")
      HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, "Hello"))

  }

  val future = Http().bindAndHandleAsync(routes, "localhost", 8080)
  logger.info("Server Started @8080\nPress Enter to Stop...")
  StdIn.readLine()
  future
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
