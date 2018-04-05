package com.sample.swagger

import com.github.swagger.akka.SwaggerHttpService
import com.github.swagger.akka.model.Info
import io.swagger.models.ExternalDocs
import io.swagger.models.auth.BasicAuthDefinition

object SwaggerDocService extends SwaggerHttpService {
  override val apiClasses                = Set(classOf[SimpleAPI])
  override val host                      = "localhost:8089"
  override val info                      = Info(version = "1.0")
  override val externalDocs              = Some(new ExternalDocs("Core Docs", "http://acme.com/docs"))
  override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())
  override val unwantedDefinitions       = Seq("Function1", "Function1RequestContextFutureRouteResult")
}
