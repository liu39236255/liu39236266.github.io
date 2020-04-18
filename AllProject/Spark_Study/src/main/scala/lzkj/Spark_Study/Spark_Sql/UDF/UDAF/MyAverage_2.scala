package lzkj.Spark_Study.Spark_Sql.UDF.UDAF
import lzkj.Spark_Study.Spark_Sql.UDF.UDAF.Common.{Average, Employee}
import org.apache.spark.sql._
import org.apache.spark.sql.expressions.Aggregator
//import org.apache.spark.Aggregator  // 这个是错误的包，导上面的包，别导错了

object MyAverage_2 extends Aggregator[Employee,Average,Double] {
  // A zero value for this aggregation. Should satisfy the property that any b + zero = b (这个聚合中的zero应该满足这个特性， b +zero= b)
  override def zero:Average = Average(0L,0L)

  // Combine two values to produce a new value. For performance, the function may modify `buffer`
  // and return it instead of constructing a new object
  override def reduce(buffer: Average, employee: Employee):Average = {
    buffer.sum+=employee.salary
    buffer.count+=1
    buffer
  }

  // Merge two intermediate(中间的) values
  override def merge(b1: Average, b2: Average) :Average= {
    b1.sum+=b2.sum
    b1.count+=b2.count
    b1
  }

  // Transform the output of the reduction,转换最终的输出
  override def finish(reduction: Average):Double = {
    reduction.sum.toDouble / reduction.count

  }
  // Specifies the Encoder for the intermediate value type(指定中间值类型的编码器。)
  //import org.apache.spark.sql.{Encoder, Encoders} ，这是下面代码需要导入的两个包
  override def bufferEncoder:Encoder[Average] = Encoders.product
  // Specifies the Encoder for the final output value type （指定最终输出值类型的编码器。）
  override def outputEncoder :Encoder[Double]= Encoders.scalaDouble
}

