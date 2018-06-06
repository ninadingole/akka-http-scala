import sbt.{AutoPlugin, Def}
import sbtrelease.ReleasePlugin

object CustomReleasePlugin extends AutoPlugin {
  import ReleasePlugin.autoImport._
  import sbtrelease.ReleaseStateTransformations._

  override def requires = ReleasePlugin

  override def projectSettings: Seq[Def.Setting[_]] = {
    releaseProcess := Seq[ReleaseStep](checkSnapshotDependencies,
                                       inquireVersions,
                                       runClean,
                                       runTest,
                                       setReleaseVersion,
                            `           commitReleaseVersion,
                                       tagRelease,
                                       releaseStepCommandAndRemaining("publish"),
                                       setNextVersion,
                                       commitNextVersion,
                                       pushChanges)
  }
}
