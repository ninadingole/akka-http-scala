package com.sample.async

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.http.scaladsl.settings.ServerSettings
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.io.StdIn

object AsyncRouteHandler extends App {
  val config                    = ConfigFactory.load()
  implicit val system           = ActorSystem("blocking-ex", config)
  implicit val materialzier     = ActorMaterializer()
  val blockingDispatcher        = system.dispatchers.lookup("blocking-io-dispatcher")
  val logger                    = Logging(system, getClass)
  val serverSettings            = ServerSettings(config)
  implicit val executionContext = system.dispatcher

  def doBlockingIO(): Future[HttpResponse] = {
    Future {
//    logger.info("Will block for 4 sec...")
      Thread.sleep(4000)
      logger.info("sending response")
      HttpResponse(status = StatusCodes.OK, entity = "Done Blocking, Here is the response.")
    }(blockingDispatcher)
  }

  val asyncHandler: HttpRequest => Future[HttpResponse] = {
    case HttpRequest(GET, Uri.Path("/async"), _, _, _) => {
      logger.info("calling io")
      val future = doBlockingIO()
      future.onComplete(_ => logger.info("future done"))
      logger.info("done calling io")
      future
    }
  }

  val future = Http().bindAndHandleAsync(asyncHandler, "localhost", 8080, settings = serverSettings, parallelism = 4)
  logger.info("Server Started @8080\nPress Enter to Stop...")
  StdIn.readLine()
  future
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
