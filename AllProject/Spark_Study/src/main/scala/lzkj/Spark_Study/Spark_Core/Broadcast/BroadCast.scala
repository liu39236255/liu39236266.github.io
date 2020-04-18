package lzkj.Spark_Study.Spark_Core.Broadcast

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}


class BroadCast{

}
object BroadCast {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("Broadcast test").setMaster("local[3]")
    val sc: SparkContext = new SparkContext(conf)
    val broadcastVar: Broadcast[Array[Int]] = sc.broadcast(Array(1,2,3))
    println(broadcastVar.value.asInstanceOf[Array[Int]].map(x => println(x)))


  }
}
