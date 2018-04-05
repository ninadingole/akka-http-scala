package com.sample.swagger

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._

class SimpleAPI extends ISimpleAPI with SprayJsonSupport {

  import HelloMessageSupport._
  import PostRequestSupport._

  val routes = {
    pathPrefix("hello") {
      path(Segment) { name =>
        complete(getHello(name))
      } ~
      pathEndOrSingleSlash {
        entity(as[PostRequest]) { data =>
          complete(postHello(data))
        }
      }
    }
  }

  override def getHello(name: String): HelloMessage = HelloMessage(s"Hello ${name}")

  override def postHello(data: PostRequest): HelloMessage = HelloMessage(s"Hello ${data.name}")
}
