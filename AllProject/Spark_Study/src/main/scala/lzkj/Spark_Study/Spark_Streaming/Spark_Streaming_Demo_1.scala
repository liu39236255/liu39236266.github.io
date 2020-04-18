package lzkj.Spark_Study.Spark_Streaming

import org.apache.spark._

import org.apache.spark.streaming._

object Spark_Streaming_Demo_1 {
  def main(args: Array[String]): Unit = {

    exampleSparkStreaming_1()
  }


  /**
    * 瑞士军刀测试案例：nc -lk 9999
    */
  def exampleSparkStreaming_1(): Unit ={
    // Create a local StreamingContext with two working thread and batch interval of 1 second.
    // The master requires 2 cores to prevent from a starvation scenario.
    val conf =new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
    val ssc=new StreamingContext(conf,Seconds(5))

    //
    // Create a DStream that will connect to hostname:port, like localhost:9999

    val lines =ssc.socketTextStream("192.168.153.128",9999)

    val words =lines.flatMap(_.split(" "))

    val pairs=words.map((_,1))

    val wordsCount=pairs.reduceByKey(_+_)

    wordsCount.print()

    ssc.start()  // Start the computation

    ssc.awaitTermination()// Wait for the computation to terminate

    /**
      * 在计算机上免安装 nc netcat ，
      * yum install -y nc
      * nc -lk 9999
      *
      * 然后输入信息，在其另外一个终端
      *
      * nc ip:9999
      *
      */


  }


  /**
    * 正式测试
    */

}
