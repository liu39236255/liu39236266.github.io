package com.nifu.study.Base_01_Spark._5CustomerSort

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
/*object MySort{
  implicit val girlOrdering =new Ordering[Girl]{
    override def compare(x: Girl, y: Girl) :Int ={
      if(x.faceValue !=y.faceValue){
        x.faceValue -y.faceValue
      }else {
        y.age-x.age
      }
    }
  }
}*/
object CustomerSort {
  def main(args: Array[String]): Unit = {
  val conf=new SparkConf().setAppName("CustomerSort").setMaster("local[2]")
    val sc =new SparkContext(conf)
    //val girlInfo =sc.parallelize(Array(("ningning",80,26),("tingting",85,24),("Angelababy",80,30)))
    val girlInfo: RDD[(String, Int, Int)] = sc.parallelize(Array(("ningning",80,26),("tingting",85,24),("Angelababy",80,30)))

    //第一种排序方式
  /*  import MySort.girlOrdering
    val res: RDD[(String, Int, Int)] = girlInfo.sortBy(x => Girl(x._2,x._3),false)
   //ArrayBuffer((tingting,85,24), (ningning,80,26), (Angelababy,80,30))
    println(res.collect.toBuffer)*/

    //第二种排序方式,倒序
    val res: RDD[(String, Int, Int)] = girlInfo.sortBy(x => Girl(x._2,x._3),false)
    println(res.collect.toBuffer)
    //ArrayBuffer((tingting,85,24), (ningning,80,26), (Angelababy,80,30))
    sc.stop()
  }
}

//第一种排序方式
//case class Girl(faceValue:Int ,age:Int)

//第二种排序方式
case class Girl (faceValue:Int,age:Int)extends Ordered[Girl]{
  override def compare(that: Girl) = {
    if(this.faceValue!=that.faceValue){
      this.faceValue-that.faceValue
    }else {
      that.age-this.age
    }
  }
}