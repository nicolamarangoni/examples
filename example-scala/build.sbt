lazy val scalaV = "2.11.12"

lazy val orgName = "io.marangoni"

organization := orgName

name := "example-scala"

version := "0.1"

scalaVersion := scalaV

skip in publish := true


// PROJECTS

lazy val cli = (project in file("example-scala-cli"))
  .settings(
    name := "example-scala-cli",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.exampleScalaJson,
      dependencies.exampleScalaStreamGenerator,
      dependencies.exampleSparkJdbc
    ),
    assemblySettings
  )

lazy val simple = (project in file("example-scala-simple"))
  .settings(
    name := "example-scala-simple",
    settings,
    libraryDependencies ++= commonDependencies,
    assemblySettings
  )

lazy val cassandra = (project in file("example-scala-cassandra"))
  .settings(
    name := "example-scala-cassandra",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.cassandraDriverCore,
      dependencies.cassandraDriverMapping,
      dependencies.cassandraDriverExtras
    ),
    assemblySettings
  )

/*lazy val hbase = (project in file("example-scala-hbase"))
  .settings(
    name := "example-scala-hbase",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.hbaseClient
    ),
    assemblySettings
  )*/

lazy val hive = (project in file("example-scala-hive"))
  .settings(
    name := "example-scala-hive",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.hiveJdbc
    ),
    assemblySettings
  )

lazy val json = (project in file("example-scala-json"))
  .settings(
    name := "example-scala-json",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.playJson
    ),
    assemblySettings
  )

lazy val streamGenerator = (project in file("example-scala-stream-generator"))
  .settings(
    name := "example-scala-stream-generator",
    settings,
    libraryDependencies ++= commonDependencies,
    assemblySettings
  )


// DEPENDENCIES

lazy val dependencies =
  new {
    val scalaLoggingV = "+"
    val playJsonV = "+"
    val hiveJdbcV = "+"
    val hbaseClientV = "+"
    val cassandraDriverV = "+"

    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV % "provided"
    val playJson = "com.typesafe.play" %% "play-json" % playJsonV % "provided"
    val hiveJdbc = "org.apache.hive" % "hive-jdbc" % hiveJdbcV % "provided"
    val hbaseClient = "org.apache.hbase" % "hbase-client" % hbaseClientV % "provided"
    val cassandraDriverCore = "com.datastax.cassandra" % "cassandra-driver-core" % cassandraDriverV % "provided"
    val cassandraDriverMapping = "com.datastax.cassandra" % "cassandra-driver-mapping" % cassandraDriverV % "provided"
    val cassandraDriverExtras = "com.datastax.cassandra" % "cassandra-driver-extras" % cassandraDriverV % "provided"

    val exampleScalaJson = "io.marangoni" %% "example-scala-json" % "+"
    //val exampleScalaHbase = "io.marangoni" %% "example-scala-hbase" % "+"
    val exampleScalaStreamGenerator = "io.marangoni" %% "example-scala-stream-generator" % "+"
    val exampleSparkJdbc = "io.marangoni" %% "example-spark-jdbc" % "+"
    //val exampleScalaSparkStreaming = "io.marangoni" %% "example-spark-streaming" % "+"
  }

lazy val commonDependencies = Seq(
  dependencies.scalaLogging
)


// SETTINGS

lazy val settings = commonSettings

lazy val commonSettings = Seq(
  organization := orgName,
  scalaVersion := scalaV,
  resolvers ++= Seq(
    Resolver.JCenterRepository,
    Resolver.DefaultMavenRepository,
    "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/Software/Maven"
  )
)

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  publishTo := Some(Resolver.file("file",  new File( Path.userHome.absolutePath + "/Software/Maven/" )) )
)
