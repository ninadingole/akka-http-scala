package com.sample.rejection

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Rejection, RejectionHandler, Route}
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.io.StdIn

object MessageBasedRejectionHandler extends App with SprayJsonSupport {
  import spray.json.DefaultJsonProtocol._

  implicit val system           = ActorSystem("http-server")
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  case class ErrorResponse(code: Int, `type`: String, message: String)
  final case class DataNotFound(message: String) extends Rejection
  implicit val dataNotFound = jsonFormat1(DataNotFound)

  implicit def rejectionHandler =
    RejectionHandler
      .newBuilder()
      .handle {
        case DataNotFound(msg) => complete(msg)
      }
      .handleNotFound {
        complete(HttpResponse(StatusCodes.NotFound, entity = HttpEntity(ContentTypes.`application/json`, "404 Not Found")))
      }
      .result()

  val routes: Route = {
    path("reject-empty-response") {
      rejectEmptyResponse {
        get {
          complete(DataNotFound("The Data is not available at the moment"))
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
