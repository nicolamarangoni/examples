package io.marangoni.scala.cli

import com.typesafe.scalalogging._
import java.util.{Date, Calendar, Properties}
import java.io.FileInputStream
import io.marangoni.spark.jdbc.{JDBCDF}
import io.marangoni.scala.stream.generator.{TextGenerator, SocketExecutor}
import io.marangoni.scala.hbase.{Connection}
import io.marangoni.scala.json.{Extractor, RandomGenerator, GeneratorFromTemplate}
import io.marangoni.spark.streaming.{SparkStream, DataFrame, Database}

//class CLI

object CLI extends Object with LazyLogging {
  
  var action:String = ""
  var propertyFile:String = "config.properties"
  val defaultPropertyFile:String = "config.properties"
  var properties:Properties = null
  
  // Spark general properties
  var master:String = "local[*]"
  var appName:String = "spark-jdbc-application"
  var pollingInterval:Int = 10
  
  // JSON properties
  var sourceFile:String = ""
  var selectedElements:String = ""
  var outputFormat:String = "csv"
  var outputCount:Int = 1
  var outputFile = "" 
  
  var jsonTemplate:String = ""
  var jsonElement:String = ""
  var parentElement:String = ""
  var countStart = 0
  var outputFolder = ""
  
  // Spark JDBC properties
  var sourceJdbcUrl:String = "jdbc:h2:file:~/test"
  var sourceJdbcUser:String = ""
  var sourceJdbcPassword:String = ""
  var sourceJdbcTable:String = "tab_source"

  var targetJdbcUrl:String = "jdbc:h2:file:~/test"
  var targetJdbcUser:String = ""
  var targetJdbcPassword:String = ""
  var targetJdbcTable:String = "tab_target"
  
  // HBase properties
  var zkQuorum:String = "localhost"
  var zkPort:String = "2181"
  var zkParent:String = "/hbase"

  def main(args: Array[String]) {
    
    if (args.length > 0) {
      action = args(0)
    }
    else {
      logger.info("No action specified!")
    }
    
    if (args.length > 1) {
      logger.info("Using property file: " + args(1))
      loadProperties(args(1))
    }
    else {
      loadProperties(defaultPropertyFile)
    }
    
    if (action == "json-extract") {
      jsonExtract
    }
    else if (action == "json-random-generate") {
      jsonRandomGenerate
    }
    else if (action == "json-generate-from-template") {
      generateFromTemplate
    }
    else if (action == "hbase-connect") {
      hBaseConnect
    }
    else if (action == "generate-log-files") {
      logger.info("Generate random log files")
      TextGenerator.generateFiles
    }
    else if (action == "generate-table-rows") {
      logger.info("Generate random table rows")
      TextGenerator.generateDBLines
    }
    else if (action == "generate-socket-stream") {
      logger.info("Generate random log stream")
      SocketExecutor.run
    }
    else if (action == "spark-jdbc-load-table") {
      loadTable
    }
    else if (action == "spark-streaming") {
      SparkStream.start
    }
    else {
      logger.info("Action not found!")
    }
    
  }
  
  def jsonExtract {
    
    Extractor.sourceFile = sourceFile
    Extractor.selectedElements = selectedElements
    Extractor.outputFormat = outputFormat
    Extractor.outputCount = outputCount
    Extractor.outputFile = outputFile
    
    Extractor.extract
    
  }
  
  def jsonRandomGenerate {    
    
    RandomGenerator.properties = properties
    RandomGenerator.generate
  }
  
  def generateFromTemplate {
    
    GeneratorFromTemplate.template = jsonTemplate
    GeneratorFromTemplate.element = jsonElement
    GeneratorFromTemplate.parentElement = parentElement
    GeneratorFromTemplate.countStart = countStart
    GeneratorFromTemplate.outputCount = outputCount
    GeneratorFromTemplate.outputFolder = outputFolder
    
    GeneratorFromTemplate.printTemplate
    
  }
  
  def hBaseConnect {
    
    Connection.zkQuorum = zkQuorum
    Connection.zkPort = zkPort
    Connection.zkParent = zkParent
    
    Connection.connect
    
  }
  
  def loadTable {
    
    JDBCDF.master = master
    JDBCDF.appName = appName
    JDBCDF.pollingInterval = pollingInterval
  
    JDBCDF.sourceJdbcUrl = sourceJdbcUrl
    JDBCDF.sourceJdbcUser = sourceJdbcUser
    JDBCDF.sourceJdbcPassword = sourceJdbcPassword
    JDBCDF.sourceJdbcTable = sourceJdbcTable
  
    JDBCDF.targetJdbcUrl = targetJdbcUrl
    JDBCDF.targetJdbcUser = targetJdbcUser
    JDBCDF.targetJdbcPassword = targetJdbcPassword
    JDBCDF.targetJdbcTable = targetJdbcTable
    
    JDBCDF.load    
  }
  
  def loadProperties(propertyFile:String) {

    // Load property file
    properties = new Properties
    try {
      properties.load(new FileInputStream(propertyFile))
    }
    catch {
      case e:Throwable => {
        logger.info(e.getMessage)
        logger.info("Using default property file " + defaultPropertyFile)
        properties.load(new FileInputStream(defaultPropertyFile))
      }
    }
    
    // Set spark properties
    if (properties.containsKey("spark.appName")) {
      appName = properties.getProperty("spark.appName")
      logger.info("Spark application name: " + appName)
    }
    if (properties.containsKey("spark.master")) {
      master = properties.getProperty("spark.master")
      logger.info("Spark master: " + master)
    }
    if (properties.containsKey("spark.pollingInterval")) {
      try {
        pollingInterval = properties.getProperty("spark.pollingInterval").toInt
        logger.info("Polling interval: " + pollingInterval)
      }
      catch {
        case e:Throwable => print("")
      }
    }
    
    // JSON properties
    if (properties.containsKey("json.sourceFile")) {
      sourceFile = properties.getProperty("json.sourceFile")
      logger.info("Source file: " + sourceFile)
    }
    if (properties.containsKey("json.selectedElements")) {
      selectedElements = properties.getProperty("json.selectedElements")
      logger.info("Selected elements: " + selectedElements)
    }
    if (properties.containsKey("json.outputFormat")) {
      outputFormat = properties.getProperty("json.outputFormat")
      logger.info("Output format: " + outputFormat)
    }
    if (properties.containsKey("output.count")) {
      outputCount = properties.getProperty("output.count").toInt
      logger.info("Input file path: " + outputCount)
    }
    if (properties.containsKey("output.file")) {
      outputFile = properties.getProperty("output.file")
      logger.info("Output file path: " + outputFile)
    }
        
    // JSON template properties
    if (properties.containsKey("json.template")) {
      jsonTemplate = properties.getProperty("json.template")
      logger.info("Template: " + jsonTemplate)
    }
    if (properties.containsKey("json.element")) {
      jsonElement = properties.getProperty("json.element")
      logger.info("Element: " + jsonElement)
    }
    if (properties.containsKey("json.parentElement")) {
      parentElement = properties.getProperty("json.parentElement")
      logger.info("Element: " + parentElement)
    }
    if (properties.containsKey("count.start")) {
      countStart = properties.getProperty("count.start").toInt
      logger.info("Input file path: " + countStart)
    }
    if (properties.containsKey("output.count")) {
      outputCount = properties.getProperty("output.count").toInt
      logger.info("Input file path: " + outputCount)
    }
    if (properties.containsKey("output.folder")) {
      outputFolder = properties.getProperty("output.folder")
      logger.info("Output file path: " + outputFolder)
    }
    
    // Set jdbc source properties
    if (properties.containsKey("source.jdbc.url")) {
      sourceJdbcUrl = properties.getProperty("source.jdbc.url")
      logger.info("Source URL: " + sourceJdbcUrl)
    }
    if (properties.containsKey("source.jdbc.user")) {
      sourceJdbcUser = properties.getProperty("source.jdbc.user")
      logger.info("Source user: " + sourceJdbcUser)
    }
    if (properties.containsKey("source.jdbc.password")) {
      sourceJdbcPassword = properties.getProperty("source.jdbc.password")
      logger.info("Source password: " + sourceJdbcPassword)
    }
    if (properties.containsKey("source.jdbc.table")) {
      sourceJdbcTable = properties.getProperty("source.jdbc.table")
      logger.info("Source table: " + sourceJdbcTable)
    }
    
    // Set jdbc target properties
    if (properties.containsKey("target.jdbc.url")) {
      targetJdbcUrl = properties.getProperty("target.jdbc.url")
      logger.info("Target url: " + targetJdbcUrl)
    }
    if (properties.containsKey("target.jdbc.user")) {
      targetJdbcUser = properties.getProperty("target.jdbc.user")
      logger.info("Target user: " + targetJdbcUser)
    }
    if (properties.containsKey("target.jdbc.password")) {
      targetJdbcPassword = properties.getProperty("target.jdbc.password")
      logger.info("Target password: " + targetJdbcPassword)
    }
    if (properties.containsKey("target.jdbc.table")) {
      targetJdbcTable = properties.getProperty("target.jdbc.table")
      logger.info("Target table: " + targetJdbcTable)
    }
    
    // Set hbase properties
    if (properties.containsKey("hbase.zookeeper.quorum")) {
      zkQuorum = properties.getProperty("hbase.zookeeper.quorum")
      logger.info("ZK quorum: " + zkQuorum)
    }
    if (properties.containsKey("hbase.zookeeper.property.clientPort")) {
      zkPort = properties.getProperty("hbase.zookeeper.property.clientPort")
      logger.info("ZK port: " + zkPort)
    }
    if (properties.containsKey("zookeeper.znode.parent")) {
      zkParent = properties.getProperty("zookeeper.znode.parent")
      logger.info("ZK parent node: " + zkParent)
    }
    
    // Set text generator properties
    if (properties.containsKey("text.source.file")) {
      SocketExecutor.sourceFile = properties.getProperty("text.source.file")
      TextGenerator.sourceFile = properties.getProperty("text.source.file")
      logger.info("Source file: " + TextGenerator.sourceFile)
    }
    if (properties.containsKey("text.timestamp.position")) {
      TextGenerator.tsPosition = properties.getProperty("text.timestamp.position")
    }
    
    // Set text generator properties
    if (properties.containsKey("text.columnCount")) {
      TextGenerator.numCols = properties.getProperty("text.columnCount").toInt
    }
    if (properties.containsKey("text.delimiter")) {
      TextGenerator.delimiter = properties.getProperty("text.delimiter")
    }
    if (properties.containsKey("text.maxColumnLemgth")) {
      TextGenerator.maxColLength = properties.getProperty("text.maxColumnLemgth").toInt
    }
    if (properties.containsKey("text.maxLines")) {
      TextGenerator.maxLines = properties.getProperty("text.maxLines").toInt
    }
    
    // Set folder properties
    if (properties.containsKey("folder.path")) {
      TextGenerator.targetPath = properties.getProperty("folder.path")
    }
    if (properties.containsKey("folder.maxFiles")) {
      TextGenerator.maxFiles = properties.getProperty("folder.maxFiles").toInt
    }
    if (properties.containsKey("folder.sleepTime")) {
      TextGenerator.sleepTime = properties.getProperty("folder.sleepTime").toInt
    }
    
    // Set jdbc properties
    if (properties.containsKey("db.driver")) {
      TextGenerator.driver = properties.getProperty("db.driver")
      logger.info("DB Driver: " + TextGenerator.driver)
    }
    if (properties.containsKey("db.url")) {
      TextGenerator.url = properties.getProperty("db.url")
      logger.info("DB url: " + TextGenerator.url)
    }
    if (properties.containsKey("db.username")) {
      TextGenerator.username = properties.getProperty("db.username")
      logger.info("DB username: " + TextGenerator.username)
    }
    if (properties.containsKey("db.password")) {
      TextGenerator.password = properties.getProperty("db.password")
      logger.info("DB password: " + TextGenerator.password)
    }
    if (properties.containsKey("db.tableName")) {
      TextGenerator.insertSQL = properties.getProperty("db.insertSQL")
    }
    
    // Set socket properties
    if (properties.containsKey("socket.port")) {
      SocketExecutor.socketPort = properties.getProperty("socket.port").toInt
    }
    if (properties.containsKey("socket.poolSize")) {
      SocketExecutor.poolSize = properties.getProperty("socket.poolSize").toInt
    }
    if (properties.containsKey("socket.sleepTime")) {
      SocketExecutor.sleepTime = properties.getProperty("socket.sleepTime").toInt
    }
    // Assign all properties to text generator
    TextGenerator.properties = properties
    
    
    // Spark streaming properties
    if (properties.containsKey("source.inputType")) {
      SparkStream.inputType = properties.getProperty("source.inputType")
    }
    // folder source
    if (properties.containsKey("source.inputFolder")) {
      SparkStream.inputFolder = properties.getProperty("source.inputFolder")
    }
    // socket source
    if (properties.containsKey("source.inputSocketHost")) {
      SparkStream.inputSocketHost = properties.getProperty("source.inputSocketHost")
    }
    if (properties.containsKey("source.inputSocketPort")) {
      SparkStream.inputSocketPort = properties.getProperty("source.inputSocketPort")
    }
    // Kafka source
    if (properties.containsKey("kafka.bootstrap.servers")) {
      SparkStream.kafkaBootstrapServers = properties.getProperty("kafka.bootstrap.servers")
    }
    if (properties.containsKey("kafka.group.id")) {
      SparkStream.kafkaGroupId = properties.getProperty("kafka.group.id")
    }
    if (properties.containsKey("kafka.auto.offset.reset")) {
      SparkStream.kafkaAutoOffsetReset = properties.getProperty("kafka.auto.offset.reset")
    }
    if (properties.containsKey("kafka.enable.auto.commit")) {
      SparkStream.kafkaEnableAutoCommit = properties.getProperty("kafka.enable.auto.commit")
    }
    if (properties.containsKey("kafka.topics")) {
      SparkStream.kafkaTopics = properties.getProperty("kafka.topics")
    }
    
    // Set target properties    
    if (properties.containsKey("spark.target")) {
      SparkStream.target = properties.getProperty("spark.target")
      println("DB Target: " + SparkStream.target)      
    }
    // Hadoop target
    if (properties.containsKey("hadoopTargetPrefix")) {
      SparkStream.hadoopTargetPrefix = properties.getProperty("hadoopTargetPrefix")
    }
    // Database target
    if (properties.containsKey("source.columnDelimiter")) {
      Database.columnDelimiter = properties.getProperty("source.columnDelimiter")
      DataFrame.columnDelimiter = properties.getProperty("source.columnDelimiter")
    }
    if (properties.containsKey("db.driver")) {
      Database.driver = properties.getProperty("db.driver")
      println("DB Driver: " + Database.driver)      
    }
    if (properties.containsKey("db.url")) {
      Database.url = properties.getProperty("db.url")
      DataFrame.url = properties.getProperty("db.url")
      println("DB url: " + Database.url)      
    }
    if (properties.containsKey("db.username")) {
      Database.username = properties.getProperty("db.username")
      DataFrame.username = properties.getProperty("db.username")
    }
    if (properties.containsKey("db.password")) {
      Database.password = properties.getProperty("db.password")
      DataFrame.password = properties.getProperty("db.password")
    }
    if (properties.containsKey("db.insertSQL")) {
      Database.insertSQL = properties.getProperty("db.insertSQL")
    }
    if (properties.containsKey("db.tableName")) {
      Database.tableName = properties.getProperty("db.tableName")
      DataFrame.tableName = properties.getProperty("db.tableName")
    }
    if (properties.containsKey("db.tableColumns")) {
      DataFrame.tableColumns = properties.getProperty("db.tableColumns")
    }
    if (properties.containsKey("db.tableSchema")) {
      DataFrame.tableSchema = properties.getProperty("db.tableSchema")
    }
    if (properties.containsKey("db.whereClause")) {
      DataFrame.whereClause = properties.getProperty("db.whereClause")
    }
    
  }
  
  def printSysProperties {

    val sysProps:Properties = System.getProperties
    println("#######################################")
    println("########### JAVA PROPERTIES ###########")
    sysProps.list(System.out)
    println("########### JAVA PROPERTIES ###########")
    println("#######################################")
    
  }
}
