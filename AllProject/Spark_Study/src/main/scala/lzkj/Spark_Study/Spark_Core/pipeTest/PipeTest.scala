package lzkj.Spark_Study.Spark_Core.pipeTest

import org.apache.spark.{SparkConf, SparkContext}



object PipeTest {
  def main(args: Array[String]): Unit = {

    val inputpath = args.length match {
      case 0 => {
        println("参数为零")
        System.exit(1)
      }
      case 1 => {
        args(0)
        println(s"输入shell脚本路径${args(0)}")
      }
    }


    val sparkConf = new SparkConf().setAppName("PipeTest")
    val sc = new SparkContext(sparkConf)
    // sc.addJar("/opt/data02/sparkApp/out/sparkApp.jar")
    //sc.addJar("/shenyabo/HadoopTest/jars/pipe.jar")
    val data = List("hi", "hello", "how", "are", "you")
    val dataRDD = sc.makeRDD(data)
    //out123.txt里有hi hello how are you，如果加一个参数变成sc.makeRDD(data，2)则是how are you，我想应该是只有一个worker的缘故
    //val scriptPath = "/home/gt/spark/bin/echo.sh"

    // val scriptPath = "src\\main\\MyData\\TxtData\\pipeTest\\echo.sh"

    /**
      * sh脚本内容如下
      * #!/bin/bash
      * echo "Running shell script";
      * RESULT="";#变量两端不能直接接空格符
      * while read LINE; do
      * RESULT=${RESULT}" "${LINE}
      * done
      *
      * echo ${RESULT} > out123.txt
      */

    /**
      * 提交作业
      * spark-submit --class lzkj.Spark_Study.Spark_Core.pipeTest.PipeTest  --master spark://lzkj01:7077  /shenyabo/HadoopTest/jars/pipe.jar  /shenyabo/HadoopTest/shell/echo.sh
      *spark-submit --class lzkj.Spark_Study.Spark_Core.pipeTest.PipeTest  --master yarn  /shenyabo/HadoopTest/jars/pipe.jar  /shenyabo/HadoopTest/shell/echo.sh
      */
    val pipeRDD = dataRDD.pipe(inputpath.toString)
    print(pipeRDD.collect())
    sc.stop()
  }
}

