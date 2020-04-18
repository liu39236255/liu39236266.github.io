package com.nifu.study.Base_01_Spark._01Station

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
/**
  * 需求：在一定的时间范围内，手机用户在哪两个基站附近停留的时间最长
  */
object MobieLocation {
  def main(args: Array[String]): Unit = {
    //两个县城
    val conf = new SparkConf().setAppName("MobieLocation").setMaster("local[2]")

    //获取数据
    val sc: SparkContext = new SparkContext(conf)

    //切分数据
    val file: RDD[String] = sc.textFile("G:\\重要文件\\前锋移动数据\\你福学习\\每天记录\\spark新阶段\\视频\\day06\\log")

    //切分数据
    val phoneAndLacAndTime = file.map(line => {
      val fields = line.split(",")
      val phone = fields(0)
      //获取手机号
      val time = fields(1).toLong
      //时间戳
      val lac: String = fields(2)
      //基站ID
      val eventType = fields(3).toInt//这里要注意，1 根你获取的时间类型不是一样的，类型要注意
      //时间类型
      //把t1 t3 弄成负数，返回
      val time_long = if (eventType == 1) -time else time
      ((phone, lac), time_long)
    })
    //用户在基站停留的时间段的总和
    val sumedPhoneAndLacAndTime : RDD[((String, String), Long)] = phoneAndLacAndTime.reduceByKey(_+_)

    //用户在某一个基站停留的时间需要和经纬度做对接

    val lacAndPhoneAndTime: RDD[(String, (String, Long))] = sumedPhoneAndLacAndTime.map(x => {
      val phone = x._1._1 //手机号
      val lac = x._1._2 //基站ID
      val timeSum = x._2 //用户在这个基站的时间
      //因为基站需要 join  ，需要时key,value 类型的
      (lac, (phone, timeSum))
    })

    //读取基站的经纬度
    //"G:\\重要文件\\前锋移动数据\\你福学习\\每天记录\\spark新阶段\\视频\\day06\\lac_info.txt"

    val lacInfo: RDD[String] = sc.textFile("G:\\重要文件\\前锋移动数据\\你福学习\\每天记录\\spark新阶段\\视频\\day06\\lac_info.txt")
    val lacAndXY: RDD[(String, (String, String))] = lacInfo.map(line => {
      val fields = line.split(",")
      val lac = fields(0) //基站ID
      val x = fields(1) //经度

      val y = fields(2) //维度
      (lac, (x, y))
    })
    //用户在基站上停留的时间加上经纬度
    val join: RDD[(String, ((String, Long), (String, String)))] = lacAndPhoneAndTime.join(lacAndXY)

    //安手机号进行分组并按照停留时间来排序
    val unit: RDD[(String, List[(String, Any, (String, String))])] = join.map(x => {
      val phone = x._2._1._1 //拿到用户的手机号
      val lac = x._1 //基站ID
      val time = x._2._1._2 //停留时间
      val xy = x._2._2 //经纬度
      (phone, time, xy)
    }).groupBy(_._1).mapValues(_.toList.sortBy(_._2).reverse.take(2))//翻转，take 区前两行
    //toList 的时候是如下数据
    //(18101056888,List((18101056888,80641308525100,(116.303955,40.041935)), (18101056888,40320654262500,(116.296302,40.032296))))
    //unit.foreach(println(_))
    unit.foreach(x => println(x))
  }

}
