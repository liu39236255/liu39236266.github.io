package com.nifu.study.Base_01_Scala

object _01_ConditionDemo {
  def main(args: Array[String]): Unit = {
    val x= 0
    val result = if (x ==0 ) true else false
    println(s"result的值$result")

    // 代码块
    val result_2={
      if( 0 ==1 ) false
      else if (0==0) true
      else "其他情况"
    }
    println(result_2+"：结果")


    // 循环

    /**
      * 1-10
      */
    for( i <- 1 to 10 )
      println(i)

    println(s"-------------------")

    /**
      * 1-9
      */
    for(i <- 1 until 10)
      println(i)

    println(s"-------------------")

    //for(i <- 数组)
    val arr = Array("a", "b", "c")
    for (i <- arr)
      println(i)


    println(s"-------------------")
    //高级for循环
    //每个生成器都可以带一个条件，注意：if前面没有分号
    for(i <- 1 to 3; j <- 1 to 3 if i != j)
      print((10 * i + j) + " ")
    println()

    //for推导式：如果for循环的循环体以yield开始，则该循环会构建出一个集合
    //每次迭代生成集合中的一个值
    val v = for (i <- 1 to 10) yield i * 10
    println(v)

  }

}
