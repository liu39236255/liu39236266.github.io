package lzkj.Spark_Study.Spark_Core.Accumulators.MyOwnAccumulator_2

import org.apache.spark.{SparkConf, SparkContext}

object MyAccumulator_Test {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("MyAccumulator_Test").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val myAcc = new MyAccumulator
    sc.register(myAcc,"myAcc")
    val numsRdd = sc.parallelize(Array("1","2","3","4")) //MyAccumulator(id: 0, name: Some(myAcc), value: 1-2-3-4-5-6-7-8-)
   // val numsRdd = sc.parallelize(Array("1","2","3","","5","6","7","8")) //MyAccumulator(id: 0, name: Some(myAcc), value: 1-2-3--5-6-7-8-)
    numsRdd.foreach(num => myAcc.add(num))
    println(myAcc)
    sc.stop()
  }
}
