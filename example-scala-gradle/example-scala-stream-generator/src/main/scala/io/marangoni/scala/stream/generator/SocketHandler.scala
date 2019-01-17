package io.marangoni.scala.stream.generator

import com.typesafe.scalalogging._
import scala.io.Source
import java.io.PrintStream
import java.net._

class SocketHandler(socket: Socket, sleepTime: Int, sourceFile: String) extends Runnable with LazyLogging {
  
  val random = scala.util.Random

  def run() {
    val out = new PrintStream(socket.getOutputStream)
    while (true) {
      if (sourceFile == "") {
        out.println(TextGenerator.generateLine)
        Thread.sleep(sleepTime)
      }
      else {
        for (line <- Source.fromFile(sourceFile).getLines) {
          var outputLine:String = ""
          if (TextGenerator.tsPosition == "") {
            outputLine = line
          }
          else {
            outputLine = TextGenerator.setTimestamp(line)
          }
          out.println(outputLine)
          Thread.sleep(sleepTime)
        }
      }
    }
    socket.getOutputStream.close()
  }
}