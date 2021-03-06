package com.sample.rejection

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{RejectionHandler, Route}
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.io.StdIn

object SimpleRejectionHandler extends App {

  implicit val system           = ActorSystem("http-server")
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  case class ErrorResponse(code: Int, `type`: String, message: String)
  final case class DataNotFound(message: String)

  implicit def rejectionHandler =
    RejectionHandler
      .newBuilder()
      .handleNotFound {
        complete((NotFound, "Not here!"))
      }
      .result()

  val routes: Route = {
    path("reject-empty-response") {
      rejectEmptyResponse {
        complete {
          Option.empty[String]
        }
      }
    }
  }

  private val future: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "localhost", 8080)
  StdIn.readLine("Server Running on localhost:8080.\nPress Enter to exit...")
  future
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
