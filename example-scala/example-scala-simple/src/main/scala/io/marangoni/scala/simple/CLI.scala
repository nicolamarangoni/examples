package io.marangoni.scala.simple

import java.util.{Date, Calendar, Properties}

import java.text.SimpleDateFormat
import java.io.FileInputStream

object CLI {
  
  var action:String = ""
  var propertyFile:String = "config.properties"
  val defaultPropertyFile:String = "config.properties"
  var properties:Properties = null

  def main(args: Array[String]) {

    val sysProps:Properties = System.getProperties
    println("#######################################")
    println("########### JAVA PROPERTIES ###########")
    sysProps.list(System.out)
    println("########### JAVA PROPERTIES ###########")
    println("#######################################")
  }

  def loadProperties(propertyFile:String) {

    // Load property file
    properties = new Properties
    try {
      properties.load(new FileInputStream(propertyFile))
    }
    catch {
      case e:Throwable => {
        println(e.getMessage)
        println("Using default property file " + defaultPropertyFile)
        properties.load(new FileInputStream(defaultPropertyFile))
      }
    }
  }
}
