package com.sample.swagger

import akka.http.scaladsl.server.Directives

trait Site extends Directives {
  val site =
  path("swagger") { getFromResource("swagger/index.html") } ~
  getFromResourceDirectory("swagger") ~ getFromDirectory("docs/src/main") ~
  path("redoc") { getFromResource("redoc/index.html") }
}
