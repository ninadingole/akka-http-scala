package com.sample.swagger

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.Future
import scala.io.StdIn

object SwaggerServer extends Site with App {

  val config                    = ConfigFactory.load()
  implicit val system           = ActorSystem("swagger-app", config)
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val routes = new SimpleAPI().routes ~ SwaggerDocService.routes ~ site

  private val future: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "localhost", 8089)
  println("Swagger API started on 8089\nPress Enter to stop...")
  StdIn.readLine()

  future
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
