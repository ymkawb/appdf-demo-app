import sbt._
import Keys._
import PlayProject._
import Classpaths.managedJars

object ApplicationBuild extends Build {

    val appName         = "appdf_demo_app"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.onepf.appdf" % "parser" % "1.0-SNAPSHOT"
      
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
        resolvers += "Local Maven " at file("repo").toURI.toURL.toString
    )
}
