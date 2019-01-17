package io.marangoni.spark.simple

import com.typesafe.scalalogging._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object WordCount extends Object with LazyLogging {
  
  def count(sc:SparkContext, inputDir:String, outputDir:String) {

    // read in text file and split each document into words
    val tokenized = sc.textFile(inputDir).flatMap(_.split(" "))
    
    // count the occurrence of each word
    val wordCounts = tokenized.map((_, 1)).reduceByKey(_ + _)
    wordCounts.saveAsTextFile(outputDir)
    println("Word count performed, see results in " + outputDir)
    
  }
}