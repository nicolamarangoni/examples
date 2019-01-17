package io.marangoni.spark.streaming

import com.typesafe.scalalogging._
import java.util.Properties
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.sql._
import org.apache.spark.streaming.twitter._
import twitter4j.Status
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

object SparkStream extends Object with LazyLogging {
  
  var appName:String = "spark-streaming-application"
  var master:String = "local[*]"
  var pollingInterval:Int = 10
  var inputType:String = "folder"
  var inputSocketHost:String = ""
  var inputSocketPort:String = ""
  var inputFolder:String = ""
  var target = "direct"
  var hadoopTargetPrefix = "";
  
  var kafkaBootstrapServers = "";
  var kafkaGroupId = "";
  var kafkaAutoOffsetReset = "earliest";
  var kafkaEnableAutoCommit = "false";
  var kafkaTopics = "";

  def start {
    
    println("Spark application name: " + appName)
    
    val conf = new SparkConf().setAppName(appName).setMaster(master)
    val ssc = new StreamingContext(conf, Seconds(pollingInterval))
    
    var lines:org.apache.spark.streaming.dstream.DStream[String] = null
    var tweets:org.apache.spark.streaming.dstream.DStream[Status] = null
    
    logger.info("#######################################################")
    if (inputType == "folder") {
      logger.info("Input folder: " + inputFolder)
      lines = ssc.textFileStream(inputFolder)
    }
    else if (inputType == "socket") {
      logger.info("Socket source: " + inputSocketHost + ":" + inputSocketPort)
      lines = ssc.socketTextStream(inputSocketHost, inputSocketPort.toInt)
    }
    else if (inputType == "twitter") {
      logger.info("See twitter4j.properties for configuration")
      tweets = TwitterUtils.createStream(ssc, None)
    }
    else if (inputType == "kafka") {
      logger.info("Bootstrap servers: " + kafkaBootstrapServers)
      val topics:Array[String] = kafkaTopics.split(",")
      val kafkaParams = Map[String, Object](
        "bootstrap.servers" -> kafkaBootstrapServers,
        "key.deserializer" -> classOf[StringDeserializer],
        "value.deserializer" -> classOf[StringDeserializer],
        "group.id" -> kafkaGroupId,
        "auto.offset.reset" -> kafkaAutoOffsetReset,
        "enable.auto.commit" -> (java.lang.Boolean.valueOf(kafkaEnableAutoCommit))
      )
      lines = KafkaUtils.createDirectStream[String, String](
        ssc,
        PreferConsistent,
        Subscribe[String, String](topics, kafkaParams)
      ).map(record => record.value())
    }
    logger.info("#######################################################")
    
    if (hadoopTargetPrefix != "") {
      println("#######################################################")
      println("HDFS folder: " + hadoopTargetPrefix)
      lines.saveAsTextFiles(hadoopTargetPrefix)
      logger.info("#######################################################")
    }
    
    println("#######################################################")    
    if (target == "direct") {
      logger.info("Insert into target RDBMS through direct jdbc connection")
      Database.connect
      Database.saveDStream(lines)
    }
    else if (inputType != "twitter" && target == "dataframe") {
      logger.info("Insert into target RDBMS through data frame")
      DataFrame.save(lines)
    }
    else if (inputType == "twitter" && target == "dataframe") {
      logger.info("Insert into target RDBMS through data frame")
      DataFrame.saveTweets(tweets)
    }
    else if (target == "console") {
      logger.info("Output to the console")
      tweets.print
      
    }
    logger.info("#######################################################")    
    
    ssc.start()
    ssc.awaitTermination()
  }
}