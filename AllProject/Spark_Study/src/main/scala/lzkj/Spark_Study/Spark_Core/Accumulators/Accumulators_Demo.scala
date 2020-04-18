package lzkj.Spark_Study.Spark_Core.Accumulators

import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

class Accumulators_Demo {

}

object Accumulators_Demo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Accumulators_Demo").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val my_Accumulator: LongAccumulator = sc.longAccumulator("My_Accumulator")

    /**
      * 输出位零,Here, accum is still 0 because no actions have caused the map operation to be computed.这是官方原话
      */
    sc.parallelize(List(1, 2, 3, 4), 2).map(x => {
      my_Accumulator.add(x)
    })
    println(s".map()-${my_Accumulator.value}")

    sc.parallelize(List(1, 2, 3, 4), 2).map(x => {
      my_Accumulator.add(x)
    }).collect()
    println(s"map().collect${my_Accumulator.value}")
    /**
      * val accum = sc.longAccumulator
      *data.map { x => accum.add(x); x }
      * // Here, accum is still 0 because no actions have caused the map operation to be computed.
      */

    //输出位10
    sc.parallelize(List(1, 2, 3, 4), 2).foreach(x => {
      my_Accumulator.add(x)
    })

    /**
      * 输出位4
      */

    sc.parallelize(List(1, 2, 3, 4), 2).foreach(x => {
      val a = 1
      my_Accumulator.add(a)
    })
    println(my_Accumulator.value)
  }


}
