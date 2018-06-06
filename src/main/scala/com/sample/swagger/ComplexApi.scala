package com.sample.swagger

import io.swagger.annotations._
import javax.ws.rs.{Path, PathParam}
import spray.json._

import scala.annotation.meta.field

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
  def getEmployee(@(PathParam @field)("employeeId") id: Int): Employee

  @ApiOperation(value = "List All Employees", httpMethod = "GET", response = classOf[List[Employee]])
  @Path("/")
  @ApiImplicitParams(
    Array(
      new ApiImplicitParam(name = "userId",
                           value = "userid of logged in user",
                           required = true,
                           dataTypeClass = classOf[Int],
                           paramType = "header")
    )
  )
  @ApiResponses(Array(new ApiResponse(code = 200, message = "List of Employees", response = classOf[List[Employee]])))
  def listEmployees(): List[Employee]

}

@ApiModel(description = "Employee Details Object")
case class Employee(@(ApiModelProperty @field)(value = "Id of Employee", required = true)
                    id: Int,
                    @(ApiModelProperty @field)(value = "Name of Employee", required = true)
                    name: Name,
                    @(ApiModelProperty @field)(value = "Working Department of Employee")
                    dept: String,
                    @(ApiModelProperty @field)(value = "Date of employee joining", required = true)
                    joiningDate: String)
@ApiModel(description = "Model To Represent Name of an Employee")
case class Name(@(ApiModelProperty @field)(value = "First Name of employee", required = true)
                firstName: String,
                @(ApiModelProperty @field)(value = "Middle name which can be optional")
                middleName: Option[String],
                @(ApiModelProperty @field)(value = "Last Name of employee", required = true)
                lastName: String)

object EmployeeSupport extends DefaultJsonProtocol {
  implicit val formatName     = jsonFormat3(Name)
  implicit val formatEmployee = jsonFormat4(Employee)
}
