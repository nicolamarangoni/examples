package io.marangoni.scala.stream.generator

import com.typesafe.scalalogging._
import java.net._
import java.util.concurrent._

object SocketExecutor extends Runnable with LazyLogging {
  
  var sourceFile:String = ""
  var socketPort:Int = 9999
  var poolSize:Int = 5
  var sleepTime:Int = 500

  def run() {
    println("Run \"telnet localhost " + socketPort + "\" to see output")
    val serverSocket = new ServerSocket(socketPort)
    val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)
    
    try {
      while (true) {
        // This will block until a connection comes in.
        val socket = serverSocket.accept()
        pool.execute(new SocketHandler(socket, sleepTime, sourceFile))
      }
    }
    finally {
      pool.shutdown()
    }
  }
}