package com.nifu.study.Base_01_Scala

object _02_Function {
  def main(args: Array[String]): Unit = {
    // 方法
    println(add(3))
    // 函数
    println(hanshu(2,3))
    // 方法调用函数
    m3(m1)
    m3(m2)

  }

  /**
    * 1 第一个方法
    */
  def add(b:Int):Int = 1*b

  /**
    * 2 第一个函数
    */
  val hanshu = (a:Int , b:Int) => {
    println(s"这是要给函数")
    a + b
  }

  /**
    * 3 第二个方法，参数是一个两个int类型的 函数
    */
  def m3(fangfa:(Int,Int)=> Int) =fangfa(2,4)
  val m1 =(a:Int,b:Int)=>{val c= a+b ; println(s"加法$c") ; c }
  val m2 =(a:Int,b:Int)=>{val c= a-b ; println(s"减法$c") ; c }

  /**
    * 4 将方法转换为函数
    */
  val m4 =m3 _

}
