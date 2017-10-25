package io.marangoni.scala.hive

import java.util.{Date, Calendar, Properties}

object CLI {

  def main(args: Array[String]) {

    val sysProps:Properties = System.getProperties
    println("#######################################")
    println("########### JAVA PROPERTIES ###########")
    sysProps.list(System.out)
    println("########### JAVA PROPERTIES ###########")
    println("#######################################")
  }
}
