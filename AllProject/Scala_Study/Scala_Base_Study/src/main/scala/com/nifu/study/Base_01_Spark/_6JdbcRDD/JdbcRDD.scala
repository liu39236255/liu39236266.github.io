package com.nifu.study.Base_01_Spark._6JdbcRDD

import java.sql.DriverManager
/**
  * 只能用来进行数据的读取
  */
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}
object JdbcRDD {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setAppName("JdbcRDD").setMaster("local")
    val sc =new SparkContext(conf)
    val conn =() => {
      Class.forName("com.mysql.jdbc.Driver").newInstance()
      DriverManager.getConnection("jdbc:mysql://hadoop01:3306/bigdata?useUnicode=true&characterEncoding=utf8","root","root")
    }
    val sql ="select id,location,counts,access_data  from localtion_info " +
      "where id >=? and id <=? order by counts"
    val jdbcRDD=new JdbcRDD(
      sc,conn,sql,0,100,2,
      res => {
        val id =res.getInt("id")
        val location =res.getString("location")
        val counts =res.getString("counts")
        val access_date =res.getDate("access_date")
        (id,location,counts,access_date)
      }
    )
    println(jdbcRDD.collect().toBuffer)
    sc.stop()
  }
}
