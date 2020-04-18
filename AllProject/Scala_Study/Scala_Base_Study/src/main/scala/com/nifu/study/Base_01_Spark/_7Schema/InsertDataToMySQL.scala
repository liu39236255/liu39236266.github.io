package com.nifu.study.Base_01_Spark._7Schema

import java.util.Properties
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
object InsertDataToMySQL {
  def main(args: Array[String]): Unit = {
   val conf = new SparkConf().setAppName("InferSchema_2").setMaster("local[2]")
    //val conf = new SparkConf().setAppName("InferSchema_2")//打包上传的话后面这个setMaster就不能要了
    val sc = new SparkContext(conf)
    val sqlContext:SQLContext = new SQLContext(sc)
    //获取数据
    val linesRDD: RDD[Array[String]] = sc.textFile("hdfs://hadoop01:9000/root/tmp/person").map(_.split("\t"))
    //StructType制定schema
    val schema =StructType{
      List(
          StructField("id",IntegerType,false),
          StructField("name",StringType,false),
          StructField("age",IntegerType,false)
      )
    }
    //linesRDD 映射到Row
    val personRDD: RDD[Row] = linesRDD.map(p =>Row(p(0).toInt,p(1),p(2).toInt))
    //生成DataFrame
    val personDF: DataFrame = sqlContext.createDataFrame(personRDD,schema) //sqlContext不能到包
    //声明一个属性，用于封装请求MySQl的一些配置
    val prop: Properties = new Properties()
    prop.put("user","root")
    prop.put("password","root")
    prop.put("driver","com.mysql.jdbc.Driver")
    //写入到数据库MySQL
    personDF.write.mode("append").jdbc("jdbc:mysql://hadoop01:3306/bigdata","person",prop)
    //停止SparkContext
    sc.stop()
  }
}