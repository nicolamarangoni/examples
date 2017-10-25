package io.marangoni.scala.hbase

import com.typesafe.scalalogging._
import org.apache.hadoop.conf.{Configuration}
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Connection, Admin}

object Connection extends Object with LazyLogging {
  
  var zkQuorum:String = "localhost"
  var zkPort:String = "2181"
  var zkParent:String = "/hbase"
  
  var tableName = ""
  
  def connect {
    
    logger.info("Connecting to HBase...")
    val conf:Configuration = HBaseConfiguration.create;
    conf.set("hbase.zookeeper.property.clientPort", zkPort);
    conf.set("hbase.zookeeper.quorum", zkQuorum);
    conf.set("zookeeper.znode.parent", zkParent);
    val conn:Connection = ConnectionFactory.createConnection(conf);
    val admin:Admin = conn.getAdmin();
    
    val tname:TableName = TableName.valueOf("tab-test");
    
    if (admin.tableExists(tname) ) {
      logger.info("Table exists!")
    }
    logger.info("Connected to HBase!")
  
  }
}