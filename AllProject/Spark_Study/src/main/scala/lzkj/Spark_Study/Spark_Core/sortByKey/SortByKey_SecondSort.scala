package lzkj.Spark_Study.Spark_Core.sortByKey

import org.apache.spark.{SparkConf, SparkContext}

class SortByKey_SecondSort{

}
object SortByKey_SecondSort {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SecondarySortApp").setMaster("local")
    val sc = new SparkContext(conf)

    val lines = sc.textFile("src/main/MyData/TxtData/SecondSortBy/SecondSortByKey.txt")

    val pairRDD = lines.map(line => {
      val splited = line.split(" ")
      val key = new SecondarySortKey(splited(0).toInt, splited(1).toInt)
      (key, line)
    })

    val sorted = pairRDD.sortByKey(false)
    val result = sorted.map(item => item._2)
    result.collect().foreach(println)
  }
  /**
    * Created by Administrator on 2016/8/14 0014.
    */
  class SecondarySortKey(val first: Int, val second: Int) extends Ordered[SecondarySortKey] with Serializable {
    override def compare(other: SecondarySortKey): Int = {
      if (this.first > other.first || (this.first == other.first && this.second > other.second)) {
        return 1;
      }
      else if (this.first < other.first || (this.first == other.first && this.second < other.second)) {
        return -1;
      }
      return 0;
    }
  }

}


