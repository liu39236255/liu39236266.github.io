package lzkj.Spark_Study.Common_Remember

import org.apache.log4j.{Logger, PropertyConfigurator}


class MyLog4j{

}
object MyLog4j {

  def main(args: Array[String]): Unit = {
    // 默认读取的是根目录src为下级目录
    //PropertyConfigurator.configure("./src/main/resources/log4j.properties")
    PropertyConfigurator.configure("src/main/resources/log4j.properties")
    val log:Logger = Logger.getLogger(MyLog4j.getClass)
    log.info("info信息")
    println("-")
    log.error("这是一个错误")

  }

}
