package io.marangoni.scala.json

import com.typesafe.scalalogging._
import java.io.{PrintWriter, File}
import scala.io.Source
import play.api.libs.json._
import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.libs.json.Json.toJsFieldJsValueWrapper

object GeneratorFromTemplate extends Object with LazyLogging {
  
  var template:String = "";
  var element:String = "";
  var parentElement:String = "";
  var countStart:Int = 0
  var outputCount:Int = 1
  var outputFolder:String = ""
  var outputJson:JsValue = null
  var pw:PrintWriter = null
  
  def printTemplate {
    println("Current path: " + new File(".").getAbsolutePath())
    var i = countStart
    while (i < countStart + outputCount) {
      for (line <- Source.fromFile(template).getLines) {
        println("#" * 100)
        val json: JsValue = Json.parse(line)
        val jsonElement = (json \ parentElement).get.as[JsObject] ++ Json.obj(element -> i)
        val jsonUpdated = json.as[JsObject] ++ Json.obj(parentElement -> jsonElement)
        println("File: " + outputFolder + i + ".json")
        println(Json.stringify(jsonUpdated))
        println("#" * 100)
        pw = new PrintWriter(new File(outputFolder + "%09d".format(i) + ".json"))
        pw.write(Json.stringify(jsonUpdated))
        pw.close
        i = i + 1
      }
    }
  }
  
}