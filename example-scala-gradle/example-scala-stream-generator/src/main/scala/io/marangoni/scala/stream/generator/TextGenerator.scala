package io.marangoni.scala.stream.generator

import com.typesafe.scalalogging._
import java.io.{PrintWriter, File}
import java.util.{Date, Calendar, Properties}
import java.text.SimpleDateFormat
import java.sql.DriverManager
import java.sql.Connection
import scala.io.Source
import scala.util.Try

object TextGenerator extends Object with LazyLogging {
  
  var properties:Properties = null
  
  var sourceFile:String = ""
  
  var numCols:Int = 2
  var maxLines:Int = 10
  var maxColLength:Int = 50
  var delimiter:String = ";"
  var tsPosition = "";
  
  var targetPath:String = ""
  var sleepTime:Int = 5000
  var maxFiles:Int = 100
    
  var driver = "org.h2.Driver"
  var url = "jdbc:h2:file:~/test"
  var username = "test"
  var password = "test"
  var insertSQL = ""
  
  val random = scala.util.Random
  val dateFormatter:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  var date:Date = null
  
  var connection:Connection = null
  
  def dbConnect {
    
    try {
  	  Class.forName(driver).newInstance
  	  connection = DriverManager.getConnection(url, username, password)
    }
    catch {
      case e:Throwable => e.printStackTrace
    }
  }
  
  def getTimestamp:String = {
    date = new Date(Calendar.getInstance.getTimeInMillis)
    return dateFormatter.format(date)
  }
  
  def setTimestamp (inputLine:String): String = {
    val inputRecord = inputLine.split(delimiter);
    var newPosition:String = tsPosition;
    var outputLine:String = "";
    
    if (Try(tsPosition.toInt).isSuccess) {
      if (tsPosition.toInt < 1) {
        newPosition = "before"
      }
      if (tsPosition.toInt > inputRecord.length) {
        newPosition = "after"
      }
    }
    
    if (newPosition == "before") {
      outputLine = TextGenerator.getTimestamp + delimiter + inputLine;
    }
    else if (newPosition == "after") {
      outputLine = inputLine + delimiter + TextGenerator.getTimestamp;
    }
    else if (Try(newPosition.toInt).isSuccess) {
       inputRecord(newPosition.toInt - 1) = TextGenerator.getTimestamp
       outputLine = inputRecord.mkString(delimiter)
    }
    else {
      outputLine = inputLine
    }
    return outputLine
  }
  
  def generateLine:String = {
    
    var line:String = ""
    var lineLength:Int = 1
    if (properties.containsKey("text.columns")) {
      val columns = properties.getProperty("text.columns").split(",")
      var col = 0
      for (column <- columns) {
        if (col > 0) {
          line += delimiter
        }
        val randomType = properties.getProperty("text.column." + column)
        if (randomType == "number" || randomType == "sequence") {
          line += random.nextInt(100)
        }
        else if (randomType == "timestamp") {
          date = new Date(Calendar.getInstance.getTimeInMillis)
          line += dateFormatter.format(date)
        }
        else if (randomType.startsWith("list.")) {
          val items = properties.getProperty("text." + randomType).split(",")
          line += items(random.nextInt(items.length))
        }
        else {
          lineLength = 1 + random.nextInt(maxColLength - 1)
          line += random.alphanumeric.take(lineLength).mkString
        }
        col += 1
      }
    }
    else {
      for( col <- 1 to numCols) {
        if (col > 1) {
          line += delimiter
        }
        lineLength = 1 + random.nextInt(maxColLength - 1)
        line += random.alphanumeric.take(lineLength).mkString 
      }
    }
    return line
  }
  
  def generateLines:String = {
    
    val rows = 1 + random.nextInt(maxLines - 1)
    var lines:String = ""
    
    for(row <- 1 to rows) {
      if (row > 1) {
        lines += "\n"
      }
      lines += generateLine
    }
    return lines
  }
  
  def toFile(text:String) {
    // Print to file
    val filePath = targetPath + Calendar.getInstance.getTimeInMillis + ".log"
    val pw = new PrintWriter(new File(filePath))
    pw.write(text)
    pw.close
    println("New log: " + filePath)
  }
  
  def generateFiles {
    if (sourceFile == "") {
      for (file <- 1 to maxFiles) {
        val randomString = TextGenerator.generateLines
        TextGenerator.toFile(randomString)
        Thread.sleep(sleepTime)
      }
    }
  }
  
  def generateDBLines {
    dbConnect
    var outputLine:String = ""
    if (sourceFile == "") {
      outputLine = TextGenerator.generateLine
    }
    else {
      println("Reading lines from " + sourceFile)
      for (line <- Source.fromFile(sourceFile).getLines) {
        if (TextGenerator.tsPosition == "") {
          outputLine = line
        }
        else {
          outputLine = TextGenerator.setTimestamp(line)
        }
        insertDBLine(outputLine)
        Thread.sleep(sleepTime)
      }
    }
  }
  
  def insertDBLine(line:String) {
    val record = line.split(delimiter)
  	val stmt = connection.prepareStatement(insertSQL)
    for( col <- 1 to record.length){
      stmt.setString(col, record(col - 1))
    }
    stmt.executeUpdate
  }
}