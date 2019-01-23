package io.marangoni.spark.jdbc

import org.apache.spark.sql.jdbc.{JdbcDialect, JdbcType}
import org.apache.spark.sql.types.{DataType, DataTypes, DecimalType}

/**
  * This class tells spark how to connect to a Exasol via spark.read.jdbc.
  * It must be registered before the call is made with
  *
  * JdbcDialects.registerDialect(ExasolDialect)
  *
  */
object ExasolDialect extends JdbcDialect {

  /**
    * This functions defines for which JDBC connection strings the dialect is used
    *
    * @param url  JDBC connection string
    * @return     'true' if it can be used, 'false' otherwise
    */
  override def canHandle(url: String): Boolean = url.startsWith("jdbc:exa")


  /**
    * Mapping of Spark data types to JDBC data types
    *
    * @param dt A Spark data types
    * @return   The JDBC data type for the spark type
    */
  override def getJDBCType(dt: DataType): Option[JdbcType] = {
    dt match {
      // http://www.docjar.com/html/api/java/sql/Types.java.html
      case DataTypes.StringType   => Some(JdbcType("VARCHAR(100000)", java.sql.Types.VARCHAR))
      case DataTypes.DoubleType   => Some(JdbcType("DOUBLE PRECISION", java.sql.Types.DOUBLE))
      case DataTypes.BooleanType  => Some(JdbcType("BOOLEAN",          java.sql.Types.BIT))
      case t: DecimalType =>
        Some(JdbcType(s"DECIMAL(${if (t.precision > 36) 36 else t.precision},${t.scale})", java.sql.Types.DECIMAL))
      case x => super.getJDBCType(x)
    }
  }
}
