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
      dependencies.playJson,
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
      dependencies.jdbcHive
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

lazy val sparkSimple = (project in file("example-spark-simple"))
  .settings(
    organization := orgName,
    name := "example-spark-simple",
    settings,
    libraryDependencies ++= commonDependencies ++ sparkDependencies,
    assemblySettings
  )

/*lazy val sparkStreaming = (project in file("example-spark-streaming"))
  .settings(
    organization := orgName,
    name := "example-spark-streaming",
    settings,
    libraryDependencies ++= commonDependencies ++ streamingDependencies ++ jdbcDependencies,
    assemblySettings
  )*/


lazy val sparkJdbc = (project in file("example-spark-jdbc"))
  .settings(
    name := "example-spark-jdbc",
    settings,
    libraryDependencies ++= commonDependencies ++ sparkDependencies ++ jdbcDependencies,
    assemblySettings
  )

// DEPENDENCIES

lazy val dependencies =
  new {
    val scalaLoggingV = "+"
    val playJsonV = "+"
    
    val sparkV = "+"

    val bahirV = "+"
    val jdbcMariaV = "+"
    val jdbcPostgreV = "+"
    val jdbcFirebirdV = "+"
    val jdbcJTDSV = "+"
    val jdbcNuoV = "+"
    val jdbcHSQLV = "+"
    val jdbcH2V = "+"
    val jdbcDerbyV = "+"
    val jdbcHiveV = "+"
    
    val hbaseClientV = "+"
    val cassandraDriverV = "+"

    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV
    val playJson = "com.typesafe.play" %% "play-json" % playJsonV

    val sparkCore = "org.apache.spark" %% "spark-core" % sparkV % "provided"
    val sparkSQL = "org.apache.spark" %% "spark-sql" % sparkV % "provided"
    val sparkStreaming = "org.apache.spark" %% "spark-streaming" % sparkV % "provided"
    val sparkStreamingKafka = "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkV % "provided"
    val sparkStreamingTwitter = "org.apache.bahir" %% "spark-streaming-twitter" % bahirV % "provided"

    val jdbcMaria = "org.mariadb.jdbc" % "mariadb-java-client" % jdbcMariaV % "provided"
    val jdbcPostgre = "org.postgresql" % "postgresql" % jdbcPostgreV % "provided"
    val jdbcFirebird = "org.firebirdsql.jdbc" % "jaybird-jdk18" % jdbcFirebirdV % "provided"
    val jdbcJTDS = "net.sourceforge.jtds" % "jtds" % jdbcJTDSV % "provided"
    val jdbcNuo = "com.nuodb.jdbc" % "nuodb-jdbc" % jdbcNuoV % "provided"
    val jdbcHSQL = "org.hsqldb" % "hsqldb" % jdbcHSQLV % "provided"
    val jdbcH2 = "com.h2database" % "h2" % jdbcH2V % "provided"
    val jdbcDerby = "org.apache.derby" % "derby" % jdbcDerbyV % "provided"
    val jdbcDerbyClient = "org.apache.derby" % "derbyclient" % jdbcDerbyV % "provided"
    val jdbcDerbyNet = "org.apache.derby" % "derbynet" % jdbcDerbyV % "provided"
    val jdbcDerbyTools = "org.apache.derby" % "derbytools" % jdbcDerbyV % "provided"
    val jdbcDerbyOptionalTools = "org.apache.derby" % "derbyoptionaltools" % jdbcDerbyV % "provided"
    val jdbcHive = "org.apache.hive" % "hive-jdbc" % jdbcHiveV % "provided"
    
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

lazy val sparkDependencies = Seq(
  dependencies.sparkCore,
  dependencies.sparkSQL
)

lazy val streamingDependencies = Seq(
  dependencies.sparkStreaming,
  dependencies.sparkStreamingKafka,
  dependencies.sparkStreamingTwitter
)

lazy val jdbcDependencies = Seq(
  dependencies.jdbcMaria,
  dependencies.jdbcPostgre,
  dependencies.jdbcFirebird,
  dependencies.jdbcJTDS,
  dependencies.jdbcNuo,
  dependencies.jdbcHSQL,
  dependencies.jdbcH2,
  dependencies.jdbcDerby,
  dependencies.jdbcDerbyClient,
  dependencies.jdbcDerbyNet,
  dependencies.jdbcDerbyTools,
  dependencies.jdbcDerbyOptionalTools,
  dependencies.jdbcHive
)

lazy val noSQLDependencies = Seq(
  dependencies.hbaseClient,
  dependencies.cassandraDriverCore,
  dependencies.cassandraDriverMapping,
  dependencies.cassandraDriverExtras
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