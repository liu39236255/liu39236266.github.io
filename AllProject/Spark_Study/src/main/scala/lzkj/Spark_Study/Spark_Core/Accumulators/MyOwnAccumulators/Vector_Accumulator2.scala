import lzkj.Spark_Study.Spark_Core.Accumulators.MyOwnAccumulators.My_Vector
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.util.{AccumulatorV2, CollectionAccumulator, DoubleAccumulator, LongAccumulator}

class Vector_Accumulator2 extends AccumulatorV2[My_Vector, My_Vector] {
  private val v: My_Vector = My_Vector.createZeroVector()

  override def isZero: Boolean = ???

  override def copy(): AccumulatorV2[My_Vector, My_Vector] = ???

  override def reset(): Unit = {
    v.reset()
  }

  override def add(v: My_Vector): Unit = v.add(v)

  override def merge(other: AccumulatorV2[My_Vector, My_Vector]): Unit = ???

  override def value: My_Vector = ???


}

object Vector_Accumulator2 {
  var sc: SparkContext = null

  def stopSparkContext(sc:SparkContext)={
    sc.stop()
  }
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("Vector_Accumulator2")
    sc = new SparkContext(conf)


    //关闭sparkcontext
    stopSparkContext(sc)

  }

  def userAccumularot(Acc_Name: String): LongAccumulator = {

    val my_Accumulator: LongAccumulator = sc.longAccumulator("My_Accumulator")
    sc.parallelize(List(1, 2, 3, 4)).map(x => my_Accumulator.add(x)).collect()
    //println(s"value:${my_Accumulator.value},\n avg:${my_Accumulator.avg}") // 10, 2.5
    my_Accumulator
  }

  //  创建并注册一个long accumulator, 从“0”开始，用“add”累加
  def longAccumulator(name: String): LongAccumulator = {
    val acc = new LongAccumulator
    sc.register(acc, name)
    acc
  }

  //  创建并注册一个double accumulator, 从“0”开始，用“add”累加
  def doubleAccumulator(name: String): DoubleAccumulator = {
    val acc = new DoubleAccumulator()
    sc.register(acc, name)
    acc
  }

  //  创建并注册一个CollectionAccumulator, 从“empty list”开始，并加入集合
  def collectionAccumulator[T](name: String): CollectionAccumulator[T] = {
    val acc = new CollectionAccumulator[T]
    sc.register(acc, name)

    acc
  }

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


}



