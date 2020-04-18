package lzkj.Spark_Study.Common_Remember.simplecore_test

object ConfigurationTest {

  def serchProperty(args: Array[String]): Unit = {
    val key =args(0).toString
    val value=PluginManage.getProperties(key)
    println(s"参数${key}对应的值是，${value}")
  }

  def main(args: Array[String]): Unit = {

    args.length match {
        case  0 => {
            println(" 请输入参数")
        }
        case 1 => serchProperty(args)

        case _=> {println("仅限于一个参数")}
      }

  }

}
