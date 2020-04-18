package lzkj.Spark_Study.Spark_Sql.UDF.UDAF

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

/**
  * 本包实现的是udaf 的官网案例,继承UDAF类
  */
object MyAverage extends  UserDefinedAggregateFunction{
  // Data types of input arguments of this aggregate function （聚合缓冲区中的输入数据类型）
  override def inputSchema: StructType = StructType(StructField("inputColum",LongType,true)::Nil)
  // Data types of values in the aggregation buffer（聚合缓冲区中的数据类型）
  override def bufferSchema: StructType = {
    StructType(StructField("count",LongType,true)::StructField("sum",LongType,true)::Nil)
  }
  // // The data type of the returned value（返回的数据类型）
  override def dataType: DataType = DoubleType

  //Whether this function always returns the same output on the identical input(这个函数是否总是在相同的输入上返回相同的输出。)
  override def deterministic: Boolean = true

  // Initializes the given aggregation buffer. The buffer itself is a `Row` that in addition to
  // standard methods like retrieving a value at an index (e.g., get(), getBoolean()), provides
  // the opportunity to update its values. Note that arrays and maps inside the buffer are still
  // immutable.
 /* (初始化给定的聚合缓冲区。缓冲区本身就是一个“行”，除了
  标准方法，比如在索引中检索值(例如，get()， getBoolean())，提供
  更新其值的机会。注意，缓冲区中的Arrays和inside仍然是
  不可变的。)*/
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0)=0L
    buffer(1)=0L
  }

  // Updates the given aggregation buffer `buffer` with new input data from `input`(使用来自“buffer”的新输入数据更新给定的聚合缓冲区“缓冲区”)
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    if(!input.isNullAt(0)){
      buffer(0)=buffer.getLong(0)+input.getLong(0)
      buffer(1)=buffer.getLong(1)+1
    }
  }

  // Merges two aggregation buffers and stores the updated buffer values back to `buffer1`（合并两个聚合缓冲区并将更新后的缓冲区值存储回“buffer1”）
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0)=buffer1.getLong(0)+buffer2.getLong(0)
    buffer1(1)=buffer1.getLong(1)+buffer2.getLong(1)

  }

  // Calculates the final result (计算最终结果)
  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0).toDouble / buffer.getLong(1)
  }

}
