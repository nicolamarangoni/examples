package io.marangoni.scala.cassandra

import java.util.{Date, Calendar, Properties}
import java.io.FileInputStream

object CLI {
  
  var action:String = ""
  var propertyFile:String = "config.properties"
  val defaultPropertyFile:String = "config.properties"

  var properties:Properties = null
  
  var host:String = "localhost"
  var port:String = "9042"
  var keySpace:String = "system"

  def main(args: Array[String]) {
    
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
    
    if (action == "connect") {
      connect
    }
    else {
      println("Action \"" + action + "\" not found!")
    }
  }
  
  def connect {
    
    Connection.host = host
    Connection.port = port
    Connection.keySpace = keySpace
    
    Connection.connect
    
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

    if (properties.containsKey("host")) {
      host = properties.getProperty("host")
      println("Host: " + host)
    }
    if (properties.containsKey("port")) {
      port = properties.getProperty("port")
      println("Port: " + port)
    }
    if (properties.containsKey("keyspace")) {
      keySpace = properties.getProperty("keyspace")
      println("keySpace: " + keySpace)
    }
  }
  
}
