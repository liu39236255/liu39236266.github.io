package Spark_Test

// 如下导入spark core
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test

@Test
class Spark_Core_01 {

  var sc: SparkContext = null

  def main(args: Array[String]): Unit = {

  }

  /**
    * 官网案例
    */
  def Demo0_Init() = {

  }

  /**
    * 并行数据
    */
  @Test
  def Demo01_Parallelized_Collections() = {
    // 创建
    val master = "local[2]"
    val conf = new SparkConf().setAppName("AppName").setMaster(master)
    sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val data = Array(1, 2, 3, 4, 5)
    val distData = sc.parallelize(data)
    distData.collect()
    println("测试")

  }
  @Test
  def Demo_Test(): Unit ={
    val res=3
    println(res)

  }

}
