package io.marangoni.scala.stream.generator

import java.util.Properties
import java.io.FileInputStream

object Generator {
  
  var action:String = "startSocket"
  var propertyFile:String = "config.properties"
  val defaultPropertyFile:String = "config.properties"
  var properties:Properties = null
      
  def main(args: Array[String]): Unit = {
    
    if (args.length > 0) {
      action = args(0)
    }
    else {
      println("No action specified!")
    }
    
    if (args.length > 1) {
      propertyFile = args(1)
    }
    
    println("Property file: " + propertyFile)
    loadProperties(propertyFile)
    
    if (action == "logFile") {
      println("Generate random log files")
      TextGenerator.generateFiles
    }
    if (action == "jdbcTable") {
      println("Generate random table rows")
      TextGenerator.generateDBLines
    }
    else if (action == "startSocket") {
      println("Generate random log stream")
      SocketExecutor.run
    }
    else {
      println("Action \"" + action + "\" not found!")
    }
  }
  
  def loadProperties(propertyFile:String) {

    // Load property file
    properties = new Properties
    try {
      properties.load(new FileInputStream(propertyFile))
    }
    catch {
      case e:Throwable => {
        println("Using default property file " + defaultPropertyFile)
        properties.load(new FileInputStream(defaultPropertyFile))
      }
    }
    
    // Action
    if (properties.containsKey("action")) {
      action = properties.getProperty("action")
    }
    
    // Set text generator properties
    if (properties.containsKey("text.source.file")) {
      SocketExecutor.sourceFile = properties.getProperty("text.source.file")
      TextGenerator.sourceFile = properties.getProperty("text.source.file")
      println("Source file: " + TextGenerator.sourceFile)
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
      println("DB Driver: " + TextGenerator.driver)
    }
    if (properties.containsKey("db.url")) {
      TextGenerator.url = properties.getProperty("db.url")
      println("DB url: " + TextGenerator.url)
    }
    if (properties.containsKey("db.username")) {
      TextGenerator.username = properties.getProperty("db.username")
      println("DB username: " + TextGenerator.username)
    }
    if (properties.containsKey("db.password")) {
      TextGenerator.password = properties.getProperty("db.password")
      println("DB password: " + TextGenerator.password)
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
  }
}