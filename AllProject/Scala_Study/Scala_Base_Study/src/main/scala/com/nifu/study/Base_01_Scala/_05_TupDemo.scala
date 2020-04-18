package com.nifu.study.Base_01_Scala

/**
  * scala 的 元祖
  */

object _05_TupDemo {
  def main(args: Array[String]): Unit = {

    val tup2_1=(12,213.123,"字符串",Array(1,2,3))
    println(tup2_1) //(12,213.123,字符串,[I@1134affc)
    println(tup2_1._4.toBuffer) // ArrayBuffer(1, 2, 3)



  }

}
