package io.marangoni.spark.streaming

import java.util.Properties
import java.io.FileInputStream

object CLI {
  
  val defaultPropertyFile = "config.properties"

  def main(args: Array[String]): Unit = {
    
    if (args.length > 0) {
      loadProperties(args(0))
    }
    else {
      loadProperties(defaultPropertyFile)
    }
    SparkStream.start
  }
  
  def loadProperties(propertyFile:String) {
    
    // Load property file
    val properties = new Properties
    try {
      properties.load(new FileInputStream(propertyFile))
    }
    catch {
      case e:Throwable => {
        println("Using default property file " + defaultPropertyFile)
        properties.load(new FileInputStream(defaultPropertyFile))
      }
    }
    
    // Set streaming source properties
    if (properties.containsKey("source.inputType")) {
      SparkStream.inputType = properties.getProperty("source.inputType")
    }
    if (properties.containsKey("source.inputSocketHost")) {
      SparkStream.inputSocketHost = properties.getProperty("source.inputSocketHost")
    }
    if (properties.containsKey("source.inputSocketPort")) {
      SparkStream.inputSocketPort = properties.getProperty("source.inputSocketPort")
    }
    if (properties.containsKey("source.inputFolder")) {
      SparkStream.inputFolder = properties.getProperty("source.inputFolder")
    }
    
    // Set spark streaming properties
    if (properties.containsKey("spark.appName")) {
      SparkStream.appName = properties.getProperty("spark.appName")
    }
    if (properties.containsKey("spark.master")) {
      SparkStream.master = properties.getProperty("spark.master")
    }
    if (properties.containsKey("spark.pollingInterval")) {
      try {
        var pi = properties.getProperty("spark.pollingInterval")
        SparkStream.pollingInterval = pi.toInt
      }
      catch {
        case e:Throwable => print("")
      }
    }
    if (properties.containsKey("spark.target")) {
      SparkStream.target = properties.getProperty("spark.target")
      println("DB Target: " + SparkStream.target)      
    }
    
    // Set target properties
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
    
    // Hadoop target
    if (properties.containsKey("hadoopTargetPrefix")) {
      SparkStream.hadoopTargetPrefix = properties.getProperty("hadoopTargetPrefix")
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
  }
}