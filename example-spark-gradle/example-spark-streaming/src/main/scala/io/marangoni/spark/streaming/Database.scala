package io.marangoni.spark.streaming

import com.typesafe.scalalogging._
import java.sql.DriverManager
import java.sql.Connection

object Database extends Object with LazyLogging {
    
  var driver = "org.h2.Driver"
  var url = "jdbc:h2:file:~/test"
  var username = "test"
  var password = "test"
  var tableName = "test_stream"
  var createSQL = "CREATE TABLE test_stream(test_col0 VARCHAR(256),test_col1 VARCHAR(256)) VALUES (?, ?)"
  var insertSQL = "INSERT INTO test_stream(test_col0,test_col1) VALUES (?, ?)"
  var dfWhereClause = "1=1"
  var columnDelimiter = ";"
  
  var connection:Connection = null
  
  def connect {
    
    try {
  	  Class.forName(driver).newInstance
  	  connection = DriverManager.getConnection(url, username, password)
    }
    catch {
      case e:Throwable => e.printStackTrace
    }
  }
  
  def saveDStream(dStream: org.apache.spark.streaming.dstream.DStream[String]) {
    dStream.foreachRDD(
      rdd => rdd.foreachPartition {
        partitionOfRecords => partitionOfRecords.foreach{
          record => saveRecord(record)
        }
      }
    )
  }
  
  def saveRecord(record: String) {
    
    if (!(record == null) && !(record.trim.isEmpty)) {
      val cols = record.split(columnDelimiter).length
  	  val stmt = connection.prepareStatement(insertSQL)
      for( col <- 1 to cols){
        stmt.setString(col, record.split(columnDelimiter)(col - 1))
      }
      stmt.executeUpdate
    }
  }
  
  def saveDataFrame(record: String) {
    
  }
}