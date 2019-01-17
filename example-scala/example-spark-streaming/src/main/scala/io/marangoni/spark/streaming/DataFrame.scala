package io.marangoni.spark.streaming

import com.typesafe.scalalogging._
import java.util.Properties
import org.apache.spark.sql._
import org.apache.kafka.clients.consumer.ConsumerRecord
import twitter4j.Status

object DataFrame extends Object with LazyLogging {
  
  var url = "jdbc:h2:file:~/test"
  var username = "test"
  var password = "test"
  
  var tableName = "test_stream"
  var tableColumns = "*"
  var tableSchema = "simple"
  
  var whereClause = "1=1"
  var columnDelimiter = ";"
  
  var zkQuorum:String = "localhost"
  var zkPort:String = "2181"
  var zkParent:String = "/hbase"
  
  def save(lines: org.apache.spark.streaming.dstream.DStream[String]) {
    val jdbcProperties = new Properties() 
    jdbcProperties.put("user", username)
    jdbcProperties.put("password", password)
    var logLineDF:org.apache.spark.sql.DataFrame = null
    
    lines.foreachRDD{rdd =>
      
      logger.info("#######################################################")
      val sqlContext = SQLContext.getOrCreate(rdd.sparkContext)
      import sqlContext.implicits._
      logLineDF = sqlContext.read.json(rdd)
      logLineDF.show()
      logLineDF.createOrReplaceTempView(tableName)
      
      if (rdd.count() > 0) {
        logger.info("#######################################################")
        logger.info("SQL query:")
        val sqlText = "SELECT " + tableColumns + " FROM " + tableName + " WHERE " + whereClause
        println(sqlText)
        
        val selectFilterDF= sqlContext.sql(sqlText)
        logger.info("#######################################################")
          
        logger.info("#######################################################")
        logger.info("Write to database: begin")
        val dfWriter = selectFilterDF.write.mode("append")
        dfWriter.jdbc(url, tableName, jdbcProperties)
        logger.info("Write to database: end")
        logger.info("#######################################################")
      }
    }
  }
  
  def saveTweets(lines: org.apache.spark.streaming.dstream.DStream[Status]) {
    val jdbcProperties = new Properties() 
    jdbcProperties.put("user", username)
    jdbcProperties.put("password", password)
    var logLineDF:org.apache.spark.sql.DataFrame = null
    var jsonData:String = "";
    
    lines.foreachRDD{rdd =>
      
      println("#######################################################")
      val sqlContext = SQLContext.getOrCreate(rdd.sparkContext)
      import sqlContext.implicits._
      
      rdd.foreach {s =>      
        jsonData += "{\"text\": \"" + s.getText + "\"}"    
        if (jsonData != "" && jsonData != null) {  
          println(jsonData);
          logLineDF = sqlContext.read.json(jsonData)
          logLineDF.show()
          logLineDF.createOrReplaceTempView(tableName)
          println("#######################################################")
          println("SQL query:")
          val sqlText = "SELECT " + tableColumns + " FROM " + tableName + " WHERE " + whereClause
          println(sqlText)
          
          val selectFilterDF= sqlContext.sql(sqlText)
          println("#######################################################")
            
          println("#######################################################")
          println("Write to database: begin")
          val dfWriter = selectFilterDF.write.mode("append")
          dfWriter.jdbc(url, tableName, jdbcProperties)
          println("Write to database: end")
          println("#######################################################")
        }
      }
    }
  }
}