package com.nifu.study.Base_01_Spark._3IpSerach

import java.sql.{Connection, Date, DriverManager, PreparedStatement}

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
object IPSearch {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("IPSerach").setMaster("local[2]")
    val sc =new SparkContext(conf)
    //获取IP端的基础数据
    val ipInfo: RDD[(String, String, String)] = sc.textFile("G:\\重要文件\\前锋移动数据\\千峰培训\\每天记录\\spark新阶段\\测试数据\\dao07\\IPSearch\\ip.txt").map(line => {
      val fields = line.split("\\|")
      val startIP = fields(2)//起始IP
      val endIP = fields(3) //结束IP
      val province = fields(6) //省份
      (startIP, endIP, province)
    })
    //在广播之前，需要调用action类型的算子把数据计算出来 acction类型的一个算子，
    val arrIPInfo: Array[(String, String, String)] = ipInfo.collect()
    //将需要广博的数据广播到集群中性赢得Executor
    val broadcastIPInfo: Broadcast[Array[(String, String, String)]] = sc.broadcast(arrIPInfo)
    //获取用户点击流日志
    val provinceAndOne: RDD[(String, Int)] = sc.textFile("G:\\重要文件\\前锋移动数据\\千峰培训\\每天记录\\spark新阶段\\测试数据\\dao07\\IPSearch\\http.log").map(line => {
      val fields = line.split("\\|")
      //切分
      val ip = fields(1) //获取
      val ipToLong = ip2Long(ip)
      //吧IP转换成long类型
      val arrIPInfo: Array[(String, String, String)] = broadcastIPInfo.value //获取广播变量的数据
      //获取IP的索引
      val index: Int = binarySearch(arrIPInfo, ipToLong)
      //根据IP索引找到对应的省份
      val provice = arrIPInfo(index)._3
      (provice, 1)
    })
    val res: RDD[(String, Int)] = provinceAndOne.reduceByKey(_+_)
    res.foreachPartition(data2MySql)
   // println(res.collect().toBuffer)
    sc.stop
  }
  //ip地址转换成long类型的方法
  def ip2Long(ip: String): Long = {
    val fragments = ip.split("[.]")
    var ipNum = 0L
    for (i <- 0 until fragments.length) {
      ipNum = fragments(i).toLong | ipNum << 8L
    }
    ipNum
  }
  //二分法检索
  def binarySearch(arr:Array[(String,String,String)],ip:Long) :Int ={
    var low =0
    var high=arr.length
    while(low<=high){
      val middle=(low+high)/2
      if((ip >= arr(middle)._1.toLong) && (ip<=arr(middle)._2.toLong)){
        return middle
      }
      if(ip<arr(middle)._1.toLong){
        high=middle -1
      }else {
        low =middle + 1
      }
    }
    -1
  }
  val data2MySql=(it:Iterator[(String,Int)]) =>{
    var conn:Connection=null//java.sql.connection
    var ps:PreparedStatement=null
    val sql ="insert into location_info(location,counts,access_data)values(?,?,?)"
    try{
      conn= DriverManager.getConnection("jdbc:mysql://hadoop01:3306/bigdata?useUnicode=true&characterEncoding=utf8","root","root")
      it.foreach(line =>{
        ps =conn.prepareStatement(sql)
        ps.setString(1,line._1);
        ps.setInt(2,line._2)
        ps.setDate(3,new Date(System.currentTimeMillis()))
        ps.executeUpdate()
      })
    }catch{
      case e:Exception => println(e.printStackTrace())
    }finally{
      if(ps !=null)
        ps.close()
      if(conn!=null)
        conn.close()
    }
  }
}


