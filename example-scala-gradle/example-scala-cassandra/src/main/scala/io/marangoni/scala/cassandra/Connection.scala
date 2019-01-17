package io.marangoni.scala.cassandra

import com.typesafe.scalalogging._
import com.datastax.driver.core.{Cluster, Session, ProtocolVersion}
import com.datastax.driver.core.exceptions.{NoHostAvailableException}
//import com.datastax.driver.core.Host
//import com.datastax.driver.core.Metadata

object Connection extends Object with LazyLogging {
  
  var host:String = "localhost"
  var port:String = "9042"
  var keySpace:String = "system"
  
  def connect {
    
    println("Connecting...")
    val cluster:Cluster = Cluster.builder().addContactPoints(host).build();
    var session:Session = null
    try {
      val session:Session = cluster.connect(keySpace);
      println("Connected!")
    }
    catch {
      case e: NoHostAvailableException => {
        println(e.getErrors)
        println(e.printStackTrace)
      }
    }
  }
}