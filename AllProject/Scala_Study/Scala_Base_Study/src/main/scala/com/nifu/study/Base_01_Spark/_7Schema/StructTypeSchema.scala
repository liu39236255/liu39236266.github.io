package com.nifu.study.Base_01_Spark._7Schema

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SQLContext}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 通过StrucType直接指定
  */
object StructTypeSchema {
  def main(args: Array[String]): Unit = {
    val conf =new SparkConf().setAppName("StructTypeSchema").setMaster("local[2]")
    val sc =new SparkContext(conf)
    val sQLContext=new SQLContext(sc)
    //获取数据
    val linesRDD: RDD[Array[String]] = sc.textFile("hdfs://hadoop01:9000/root/tmp/person2",1).map(_.split("\t"))
    //有structType类型制定schema
    val schema: StructType = StructType {
      //导入到时spark  sql的包
      Array(
        StructField("id", IntegerType, false), //false 不能为空
        StructField("name", StringType, true), //
        StructField("age", IntegerType, true),
        StructField("fv", IntegerType, true)
      )
    }
    //吧LinesRDD映射到rowRDD
    val rowRDD: RDD[Row] = linesRDD.map(p => Row(p(0).toInt,p(1),p(2).toInt,p(3).toInt))
    //生成DataFrame
    //sQLContext.createDataFrame(rowRDD,schema)
    val personDF: DataFrame = sQLContext.createDataFrame(rowRDD,schema)
    //注册成二维表
    personDF.registerTempTable("person2")
    //sql语句
    val sql ="select name ,age fv from person2 order by fv asc"
    //查询
    val res: DataFrame = sQLContext.sql(sql)
    //存储到本地文件
    res.write.mode("append").json("C:\\Users\\Administrator\\Desktop\\person_02")
  }
}
