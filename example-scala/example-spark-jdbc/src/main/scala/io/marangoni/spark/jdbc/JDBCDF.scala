package io.marangoni.spark.jdbc

import com.typesafe.scalalogging._
import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.sql.jdbc.JdbcDialects


object JDBCDF extends Object with LazyLogging {
  
  var appName:String = "spark-jdbc-application"
  var master:String = "local[*]"
  var pollingInterval:Int = 10
  
  var sourceJdbcUrl:String = "jdbc:h2:file:~/test"
  var sourceJdbcUser:String = ""
  var sourceJdbcPassword:String = ""
  var sourceJdbcTable:String = "tab_source"

  var targetJdbcUrl:String = "jdbc:h2:file:~/test"
  var targetJdbcUser:String = ""
  var targetJdbcPassword:String = ""
  var targetJdbcTable:String = "tab_target"

  def load {
    
    println("########################################################")
    println("Starting spark session")

    JdbcDialects.registerDialect(ExasolDialect)

    val conf = new SparkConf().setAppName(appName).setMaster(master)
    val spark = SparkSession
      .builder()
      .master(master)
      .appName(appName)
      .getOrCreate()
      
    println("Spark session created")
    
    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", sourceJdbcUrl)
      .option("user", sourceJdbcUser)
      .option("password", sourceJdbcPassword)
      .option("dbtable", sourceJdbcTable)
      .load()
      
    println("Table loaded")
      
    jdbcDF.printSchema()
    
    jdbcDF.write
      .mode("overwrite")
      .format("jdbc")
      .option("url", targetJdbcUrl)
      .option("user", targetJdbcUser)
      .option("password", targetJdbcPassword)
      .option("dbtable", targetJdbcTable)
      .save()
      
    println("Table saved")
    

    println("#######################################################")    
  }
}