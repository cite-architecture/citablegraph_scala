
organization := "edu.furman.folio"
name := "citablegraph"

version := "0.1.0"


// offline := true

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.11.8", "2.12.1")

resolvers += "jcenterRepo" at "https://jcenter.bintray.com/"
resolvers += "uh-nexus" at "http://beta.hpcc.uh.edu/nexus/content/groups/public"


// libraryDependencies += "edu.holycross.shot" %% "cite" % "3.1.0"
libraryDependencies += "edu.holycross.shot.cite" %% "xcite" % "1.3.0"
libraryDependencies += "edu.holycross.shot" %% "ohco2" % "2.1.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" %  "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"

libraryDependencies += "org.scala-graph" %% "graph-core" % "1.11.4"

libraryDependencies +=  "org.pegdown"    %  "pegdown"     % "1.6.0"  % "test"

logBuffered in Test := true


testOptions in Test ++= Seq(Tests.Argument(TestFrameworks.ScalaTest, "-oIDS"), Tests.Argument(TestFrameworks.ScalaTest, "-hI", "target/test-reports"))

publishTo := Some("Sonatype Snapshots Nexus" at "http://beta.hpcc.uh.edu/nexus/content/repositories/releases/")

credentials += Credentials(Path.userHome / "nexusauth.txt" )
