package com.sample.swagger

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._

class SimpleAPI extends ISimpleAPI with SprayJsonSupport {
  import HelloMessageSupport._

  val routes = getHello
  override def getHello: server.Route = {
    path("hello" / Segment) { name =>
      get {
        complete(HelloMessage(s"Hello ${name}"))
      }
    }
  }
}
