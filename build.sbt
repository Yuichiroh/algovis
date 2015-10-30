name := "algovis"

version := "1.0"

scalaVersion := "2.11.7"

javaOptions in run += "-Xmx1G"

resolvers += Opts.resolver.sonatypeSnapshots

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.4"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "latest.integration" % "test"

libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.60-R9"

fork in run := true

connectInput in run := true

scalacOptions in (Compile, doc) ++= Seq("-groups", "-implicits", "-diagrams")

scalacOptions += "-feature"

scalacOptions += "-deprecation"

unmanagedClasspath in Runtime += baseDirectory.value / "resources"

unmanagedJars in Compile ++= Seq(
  Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar")),
  Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/soundbank-deluxe.gm"))
)

lazy val algovis = (project in file("."))
//  .dependsOn(util)
//
//lazy val util = RootProject(file("../util"))

fork in run := true

connectInput in run := true

scalacOptions in (Compile,doc) ++= Seq("-groups", "-implicits", "-diagrams")
