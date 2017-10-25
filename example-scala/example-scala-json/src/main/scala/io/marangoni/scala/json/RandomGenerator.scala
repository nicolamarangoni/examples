package io.marangoni.scala.json

import com.typesafe.scalalogging._
import java.util.{Properties, Date, Calendar}
import java.io.{PrintWriter, File}
import java.text.SimpleDateFormat

object RandomGenerator extends Object with LazyLogging {
  
  var pw:PrintWriter = null
  val dateFormatter:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  var properties:Properties = null
  
  var jsonText:String = ""
  var jsonValue:String = ""
  
  val r = new scala.util.Random
  
  def generate {
    val recordCount = properties.getProperty("json.record.count").toInt
    pw = new PrintWriter(new File(properties.getProperty("output.file")))
    pw.write("[\n")
    for (record <- 1 to recordCount) {
      if (record == recordCount) {
        pw.write(generateRecord + "\n")
      }
      else {
        pw.write(generateRecord + ",\n")
      }
    }
    pw.write("]")
    pw.close
  }
  
  def generateRecord:String = {
    
    var jsonElement:String = "{"
    
    val elements = properties.getProperty("json.elements").split(",")
    for (element <- elements) {
      jsonElement += "\"" + element + "\": "
      val elementDef = properties.getProperty("json.element." + element)
      if (elementDef == "randomNumber") {
        jsonElement += "\""
        if (properties.containsKey("json.element." + element + ".prefix")) {
          jsonElement += properties.getProperty("json.element." + element + ".prefix")
        }
        jsonElement += "%06d".format(r.nextInt(999999)) + "\", "
      }
      else if (elementDef == "timeStamp") {
        jsonElement += "\""
        if (properties.containsKey("json.element." + element + ".prefix")) {
          jsonElement += properties.getProperty("json.element." + element + ".prefix")
        }
        val timeStamp = Calendar.getInstance.getTimeInMillis
        jsonElement += timeStamp  + "\", "
      }
      else if (elementDef == "dateTime") {
        jsonElement += "\""
        if (properties.containsKey("json.element." + element + ".prefix")) {
          jsonElement += properties.getProperty("json.element." + element + ".prefix")
        }
        val dateTime = new Date(Calendar.getInstance.getTimeInMillis)
        jsonElement += dateFormatter.format(dateTime)  + "\", "
      }
      else if (elementDef.startsWith("list.")) {
        jsonElement += "\""
        if (properties.containsKey("json.element." + element + ".prefix")) {
          jsonElement += properties.getProperty("json.element." + element + ".prefix")
        }
        val items = properties.getProperty(elementDef).split(",")
        jsonElement += items(r.nextInt(items.length))  + "\", "
      }
      else if (elementDef == "randomJsonElement") {
        jsonElement += randomJsonElement + ", "
      }
    }
    
    jsonElement = jsonElement.trim.stripSuffix(",") + "}"
    
    return jsonElement
  }
  
  def randomJsonElement:String = {
    
    val nElements = 1 + r.nextInt(9)
    
    var jsonElement = "{"
    for (elem <- 1 to nElements) {
      jsonElement += "\"" + r.alphanumeric.take(10).mkString + "\": \"" + r.alphanumeric.take(10).mkString + "\",";
    }
    jsonElement = jsonElement.stripSuffix(",") + "}"
    
    return jsonElement
  }
}