package com.nifu.study.Base_01_Scala

/**
  * scala 中的映射
  */

object _04_Map {


  def main(args: Array[String]): Unit = {
    /**
      * 1-不可变映射
      */
    val map_1=Map("小红"->1,"小红2"->2)
    println(map_1) //Map(小红 -> 1, 小红2 -> 2)
    println(map_1("小红")) // 1
    println(map_1.getOrElse("小兰",0)) // 0

    /**
      * 可变map映射
      */
    val map_2=collection.mutable.Map("小红"->1,"小红2"->2)
    map_2("小红2")=3
    println(map_2("小红2"))
//    map_2-=("小红")
    map_2.remove("小红")
    println(map_2.getOrElse("小红",0)) // 0

    map_2+=("小红"->1)
    map_2.put("小红4",4)
    map_2+=(("小红5",5),("小红6",6))
    map_2+=("小红7"->7,"小红8"->8)

    println(map_2) // Map(小红6 -> 6, 小红2 -> 3, 小红5 -> 5, 小红4 -> 4, 小红 -> 1)

    println(map_2.remove("小红9")) // None
    if(map_2.remove("小红9").equals(None)){
      println(s"没有小红9！")
    }
    map_2+=("小红8"->9)

    println(map_2) // Map(小红6 -> 6, 小红8 -> 9, 小红2 -> 3, 小红5 -> 5, 小红7 -> 7, 小红4 -> 4, 小红 -> 1)

    for(map <- map_2){
      if(map._1.equals("小红6")){
        map_2.put("小红6",7)
      }
    }
    println(map_2)  // Map(小红6 -> 7, 小红8 -> 9, 小红2 -> 3, 小红5 -> 5, 小红7 -> 7, 小红4 -> 4, 小红 -> 1)

  }

}
