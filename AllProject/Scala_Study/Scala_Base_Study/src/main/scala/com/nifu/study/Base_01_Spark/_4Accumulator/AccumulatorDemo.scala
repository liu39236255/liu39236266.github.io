package com.nifu.study.Base_01_Spark._4Accumulator

import org.apache.spark.{Accumulable, Accumulator, SparkConf, SparkContext}

object AccumulatorDemo {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setAppName("AccumulatorDemo").setMaster("local[2]")
    val sc =new SparkContext(conf)
    val sum: Accumulator[Int] = sc.accumulator(0)
    val numbers =sc.parallelize(Array(1,2,3,4,5,6),2)
    numbers.foreach(num => sum +=num)
    println(sum)
    sc.stop()
  }
}



