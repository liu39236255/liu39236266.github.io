package com.nifu.study.Base_01_Spark._02URLCount

import java.net.URL

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 统计用户对每个学科的各个模块访问的次数取top3
  */
object ObjectCount1 {
  def main(args: Array[String]): Unit = {
    val conf =new SparkConf().setAppName("ObjectCount1").setMaster("local[2]")
    val sc: SparkContext = new SparkContext(conf)

    //获取数据
    val file: RDD[String] = sc.textFile("G:\\重要文件\\前锋移动数据\\你福学习\\每天记录\\spark新阶段\\视频\\day06\\access.txt")

    //切分数据 取出url 生成元祖
    val urlAndOne: RDD[(String, Int)] = file.map(line => {
      val fields = line.split("\t")
      val url = fields(1)
      (url, 1)
    })
    //相同的url聚合
    val sumedUrl: RDD[(String, Int)] = urlAndOne.reduceByKey(_+_)
    //获取学科信息
    val project: RDD[(String, String, Int)] = sumedUrl.map(x => {
      val url = x._1
      val count = x._2
      val project = new URL(url).getHost
      (project, url, count)
    })
    // 以学科信息分组
    val res: RDD[(String, List[(String, String, Int)])] = project.groupBy(_._1).mapValues(_.toList.sortBy(_._3).reverse.take(3))
    println(res.collect.toBuffer)

/*    ArrayBuffer((android.learn.com,List((android.learn.com,http://android.learn.com/android/video.shtml,5))), (h5.learn.com,List((h5.learn.com,http://h5.learn.com/h5/course.shtml,47), (h5.learn.com,http://h5.learn.com/h5/teacher.shtml,17), (h5.learn.com,http://h5.learn.com/h5/video.shtml,11))), (ui.learn.com,List((ui.learn.com,http://ui.learn.com/ui/video.shtml,37), (ui.learn.com,http://ui.learn.com/ui/course.shtml,26), (ui.learn.com,http://ui.learn.com/ui/teacher.shtml,23))), (java.learn.com,List((java.learn.com,http://java.learn.com/java/teacher.shtml,25), (java.learn.com,http://java.learn.com/java/video.shtml,23), (java.learn.com,http://java.learn.com/java/javaee.shtml,13))), (bigdata.learn.com,List((bigdata.learn.com,http://bigdata.learn.com/bigdata/video.shtml,47), (bigdata.learn.com,http://bigdata.learn.com/bigdata/teacher.shtml,46), (bigdata.learn.com,http://bigdata.learn.com/bigdata/course.shtml,25))))
    17/10/25 14:19:45 INFO SparkUI: Stopped Spark web UI at http://10.0.107.224:4040*/
    sc.stop()

  }


}


