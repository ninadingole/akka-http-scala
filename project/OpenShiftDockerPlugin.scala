import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.docker.DockerPlugin
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import sbt.{AutoPlugin, Def}

object OpenShiftDockerPlugin extends AutoPlugin {
  override def requires = JavaAppPackaging && DockerPlugin

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(dockerBaseImage := "alpine:3.5", dockerExposedPorts := Seq(8080))
}
