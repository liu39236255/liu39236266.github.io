package lzkj.Spark_Study.Spark_Core.Accumulators.MyOwnAccumulators

object My_Vector {
  def createZeroVector(): My_Vector = {
    return new My_Vector(1, 2, 3)
  }
}

class My_Vector(x: Int, y: Int, val z: Int = 0) {
  def createZeroVector(): Unit = {

  }

  def reset(): Unit = {

  }

  def add(v: My_Vector): Unit = {

  }
}