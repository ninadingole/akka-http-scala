package com.sample.swagger

import io.swagger.annotations._
import javax.ws.rs.{Path, PathParam}
import spray.json._

@Api(value = "/simple", produces = "application/json")
@Path("/hello")
trait ISimpleAPI {

  @ApiOperation(value = "Returing Hello World", httpMethod = "GET", response = classOf[HelloMessage])
  @Path("/{name}")
  @ApiImplicitParams(
    Array(
      new ApiImplicitParam(name = "name",
                           value = "Name of the person to Greet",
                           defaultValue = "ninad",
                           required = true,
                           dataTypeClass = classOf[String],
                           paramType = "path")
    )
  )
  @ApiResponses(Array(new ApiResponse(code = 200, message = "Return hello response", response = classOf[HelloMessage])))
  def getHello(@PathParam(value = "name") name: String): HelloMessage

  @ApiOperation(value = "Returing Hello World", httpMethod = "POST", response = classOf[HelloMessage])
  @Path("/")
  @ApiResponses(Array(new ApiResponse(code = 200, message = "Return hello response", response = classOf[HelloMessage])))
  def postHello(postRequest: PostRequest): HelloMessage
}

trait Response

case class HelloMessage(message: String) extends Response

object HelloMessageSupport extends DefaultJsonProtocol {
  implicit val format = jsonFormat1(HelloMessage)
}

case class PostRequest(name: String)

object PostRequestSupport extends DefaultJsonProtocol {
  implicit val postRequest = jsonFormat1(PostRequest)
}
