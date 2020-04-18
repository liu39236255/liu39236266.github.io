package lzkj.Spark_Study.Spark_Core.Accumulators.MyOwnAccumulator_2

import org.apache.spark.util.AccumulatorV2


class  MyAccumulator extends AccumulatorV2[String,String]{

  /**
    * 1、类继承extends AccumulatorV2[String, String]，第一个为输入类型，第二个为输出类型
    * 2、覆写抽象方法：
    * isZero: 当AccumulatorV2中存在类似数据不存在这种问题时，是否结束程序。
    * copy: 拷贝一个新的AccumulatorV2
    * reset: 重置AccumulatorV2中的数据
    * add: 操作数据累加方法实现
    * merge: 合并数据
    * value: AccumulatorV2对外访问的数据结果
    */

  private var res = ""
  /**
    * 此方法必须返回true
    * @return
    */

  override def isZero: Boolean = {res == ""}

  override def merge(other: AccumulatorV2[String, String]): Unit = other match {
    case o : MyAccumulator => res += o.res
    case _ => throw new UnsupportedOperationException(
      s"Cannot merge ${this.getClass.getName} with ${other.getClass.getName}")
  }

  override def copy(): MyAccumulator = {
    val newMyAcc = new MyAccumulator
    newMyAcc.res = this.res
    newMyAcc
  }

  override def value: String = res

  override def add(v: String): Unit = res += v +"-"

  override def reset(): Unit = res = ""
}
