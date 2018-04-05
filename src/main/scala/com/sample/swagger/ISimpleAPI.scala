package com.sample.swagger

import akka.http.scaladsl.server
import io.swagger.annotations._
import javax.ws.rs.Path
import spray.json._

@Api(value = "/simple", produces = "application/json")
@Path("/hello")
trait ISimpleAPI {

  @ApiOperation(value = "Returing Hello World", httpMethod = "GET", response = classOf[Response])
  @Path("/{name}")
  @ApiImplicitParams(
    Array(
      new ApiImplicitParam(name = "name",
                           value = "Name of the person to Greet",
                           defaultValue = "ninad",
                           required = true,
                           dataType = "string",
                           paramType = "path")
    )
  )
  @ApiResponses(Array(new ApiResponse(code = 200, message = "Return hello response", response = classOf[Response])))
  def getHello: server.Route
}

trait Response

case class HelloMessage(message: String) extends Response

object HelloMessageSupport extends DefaultJsonProtocol {
  implicit val format = jsonFormat1(HelloMessage)
}
