package Scala_Vacant.Scala_List_Vacant

object Scala_List {
  def main(args: Array[String]): Unit = {

    scala_list_Demo()
  }

  def scala_list_Demo(): Unit ={

      val  a = List(1,2,3,4)
      for (i <- a) println(i)

      // 将0添加到a中
      val b = 0::a
      for (i <- b) println(i)

      val c = List("x","y","z")

      // 两个list合并
      val d = a ::: c
      for (i <- d) println(i)

      println(a.head) // 返回第一个元素
      println(a.tail) // 返回除第一个元素的List
      println(a.isEmpty) // 判断List是否为空

      // 取出List中的偶数;
      // filrter高阶函数：他的参数就是一个匿名函数，匿名参数输入参数x就代表的是列表中的一个元素，
      // filter会遍历列表中的每个元素，每个元素就去套用传进来的匿名函数的判断条件，如果判断为true就保留这个元素
      val e = a.filter(x => x % 2 ==0)
      println(e)
      // filter简写
      val e1 = a.filter(_ % 2 ==0)
      println(e1)

      // 过滤字符串中的数字
      val str = "123 hello scala 168"
      val f = str.toList.filter(x => Character.isDigit(x))
      println(f)

      // 取到某个字符之前的所有字符
      val g = str.toList.takeWhile(x => x != 's')
      println(g)

      // map高阶函数:将每个元素进行转换映射
      val h = c.map(x => x.toUpperCase())
      println(h)
      // map简写
      val h1 = c.map(_.toUpperCase)
      println(h1)

      // 取出列表中的偶数，并且为每个元素加上100
      val k = a.filter(_ % 2 ==0).map(_ + 100)
      println(k)

      // 两层List
      val q = List(a,List(4,5,6))
      // 取出q中的所有偶数;第一步：map获取每个list，第二步:filter过滤每个list中的偶数元素
      val r = q.map(x => x.filter(y => y % 2 == 0 ))
      println(r)
      // 简写
      val r1 = q.map(_.filter(_ % 2 ==0))
      println(r1)

      // flatMap高阶函数：是将List中的结果打平
      val p = q.flatMap(_.filter(_ % 2 == 0))
      println(p)

      // 总结map与flatMap区别
      // map:返回的结果和原List结构一致，如果是两层，返回的就是两层;r: List[List[Int]] = List(List(2,4), List(4, 6))
      // flatMap：返回的结果只有一层List结构; p: List[Int] = List(2, 4, 4, 6)

      // 规约操作：reduceLeft(op:(T,T) => T)
      // 求List中元素的和
      val m = a.reduceLeft((x,y) => x + y)
      println(m)
      // reduceLeft简写
      val m1 = a.reduceLeft(_ + _)
      println(m1)

      // 规约操作：foldLeft(z:U)(op:(U,T)) => U
      // 求List中元素的和
      val n = a.foldLeft(0)((x,y) => x+y)
      println(n)
      // 简写
      val n1 = a.foldLeft(0)(_ + _)
      println(n1)

    }


}
