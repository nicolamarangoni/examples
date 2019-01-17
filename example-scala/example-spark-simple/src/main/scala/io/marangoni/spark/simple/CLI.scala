package io.marangoni.spark.simple

import java.util.{Date, Calendar, Properties}
import java.text.SimpleDateFormat
import java.io.FileInputStream
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object CLI {
  
  var action:String = ""
  var propertyFile:String = "config.properties"
  val defaultPropertyFile:String = "config.properties"
  var properties:Properties = null
  
  val dateFormatter:SimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
  var appName:String = "spark-test-simple"
  var master:String = null
  var inputPath:String = "input.txt"
  var outputPath:String = "output.txt"
  
  def main(args: Array[String]) {
    
    val submitMaster:String = System.getProperty("spark.master")
    
    println("spark.master = " + submitMaster)
    println("#######################################")
    
    if (args.length > 0) {
      println("Using property-file: " + args(0))
      loadProperties(args(0))
    }
    else {
      println("Using default property-file: " + defaultPropertyFile)
      loadProperties(defaultPropertyFile)
    }
    
    // create Spark context with Spark configuration
    var conf = new SparkConf().setAppName(appName)
    if (master != null && master != "" && (submitMaster == null || submitMaster == "")) {
      conf = conf.setMaster(master)
    }
    val sc = new SparkContext(conf)
    
    WordCount.count(sc,inputPath,outputPath + "_" + dateFormatter.format(Calendar.getInstance.getTimeInMillis))
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
    
    if (properties.containsKey("spark.appName")) {
      appName = properties.getProperty("spark.appName")
      println("Application name: " + appName)      
    }
    if (properties.containsKey("spark.master")) {
      master = properties.getProperty("spark.master")
      println("Master: " + master)      
    }
    if (properties.containsKey("input.path")) {
      inputPath = properties.getProperty("input.path")
      println("Input file path: " + inputPath)      
    }
    if (properties.containsKey("output.path")) {
      outputPath = properties.getProperty("output.path")
      println("Output file path: " + outputPath)      
    }
  }
}