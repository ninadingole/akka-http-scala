package com.sample.swagger

import java.time.LocalDate

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._

class ComplexApiImpl extends ComplexApi with SprayJsonSupport {
  import EmployeeSupport._

  val routes = pathPrefix("employees") {
    get {
      path(IntNumber) { id =>
        complete(getEmployee(id))
      } ~
      pathEndOrSingleSlash {
        complete(listEmployees())
      }
    }
  }

  override def getEmployee(id: Int): Employee = {

    Employee(101, Name("John", Some("M"), "Doe"), "HR", LocalDate.now().toString)
  }

  override def listEmployees(): List[Employee] = {
    List(Employee(101, Name("John", Some("M"), "Doe"), "HR", LocalDate.now().toString))
  }
}
