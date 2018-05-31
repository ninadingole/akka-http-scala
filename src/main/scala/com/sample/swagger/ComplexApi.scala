package com.sample.swagger

import io.swagger.annotations._
import javax.ws.rs.{Path, PathParam}
import spray.json._

@Api(value = "/complex", produces = "application/json")
@Path("/employees")
trait ComplexApi {

  @ApiOperation(value = "Returns Employee Detail", httpMethod = "GET", response = classOf[Employee])
  @Path("/{employeeId}")
  @ApiImplicitParams(
    Array(
      new ApiImplicitParam(name = "employeeId",
                           value = "Id of Employee",
                           required = true,
                           dataTypeClass = classOf[Int],
                           paramType = "path")
    )
  )
  @ApiResponses(
    Array(new ApiResponse(code = 200, message = "Details of Employee", response = classOf[Employee]),
          new ApiResponse(code = 404, message = "Employee Id not Found"))
  )
  def getEmployee(@PathParam(value = "employeeId") id: Int): Employee

}

case class Employee(id: Int, name: Name, dept: String, joiningDate: String)
case class Name(firstName: String, middleName: Option[String], lastName: String)

object EmployeeSupport extends DefaultJsonProtocol {
  implicit val formatName     = jsonFormat3(Name)
  implicit val formatEmployee = jsonFormat4(Employee)
}
