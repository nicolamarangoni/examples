package io.marangoni.scala.json

import com.typesafe.scalalogging._
import scala.util.control.Breaks._
import java.io.{PrintWriter, File}
import java.nio.charset.StandardCharsets
import scala.io.Source
import play.api.libs.json._
import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.libs.json.Json.toJsFieldJsValueWrapper

object Extractor extends Object with LazyLogging {
  
  var sourceFile:String = ""
  var selectedElements = ""
  var outputFormat:String = "csv"
  var outputCount = 1
  var outputFile:String = ""
  var outputJson:JsValue = null
  var pw:PrintWriter = null
  
  def extract {
    println("Current path: " + new File(".").getAbsolutePath())
    val elementsToExport = selectedElements.split(",")
    var row = 0
    var jsonLine: JsValue = null
    var outputLine: String = ""
    pw = new PrintWriter(new File(outputFile))
    println("#" * 100)
    println("File: " + outputFile)
    breakable {
      for (line <- Source.fromFile(sourceFile).getLines) {
        try {
          jsonLine = Json.parse(line)
          outputLine = ""
          for (col <- 1 to elementsToExport.length) {
            outputLine += (jsonLine \ elementsToExport(col - 1)).get + ","
          }
          pw.write(outputLine.stripSuffix(",") + "\n")
          row = row + 1
          if (row % 1000 == 0) {
            println(row + " line extracted")
          }
          if (row == outputCount) {
            break
          }
        }
        catch {
          case e: Exception => println(row + "  =>  " + line)
        }
      }
    }
    pw.close
    println("#" * 100)
  }
  
}