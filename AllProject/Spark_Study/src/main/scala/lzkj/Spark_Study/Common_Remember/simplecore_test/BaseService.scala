package lzkj.Spark_Study.Common_Remember.simplecore_test

//import com.lzkj.bi.helper.dbconfig.DataBaseConfigProvider
//import com.lzkj.bi.helper.dbconfig.impl.{BiProductionOracleDataBaseConfigProvider, BiTestOracleDataBaseConfigProvider}
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * 服务类的基类
  */
abstract class BaseService(cmd: String, name: String) extends Serializable {

  /**
    * 数据库配置抽象类,默认报表输出源
    */
//  var reportDataBaseConfigProvider: DataBaseConfigProvider = null

  /**
    * 日期时间
    */
  var date = ""

  /**
    * 小时
    */
  var hour = ""

  /**
    * 服务命令
    */
  var app_cmd = cmd

  /**
    * 服务描述
    */
  var app_name = name

  /**
    * spark上下文对象
    */
  var spark: SparkSession = null

  /**
    * true:开发模式，false:生产环境，默认false
    */
  def debugMode: Boolean = {
    val conf = SimpleCoreConfiguration.getConf()
    return conf.getBoolean("debug", false)
  }

  /**
    * 忽略这个任务,默认false
    *
    * @return
    */
  def ignore: Boolean = false

  /**
    * 任务组,默认rpt
    */
  def group: String = "rpt"

  /**
    * 对服务类的参数进行初始化
    */
  def init(args: Array[String]): Unit = {

    if (debugMode) {
      /*spark = SparkFactory.createLocalSparkSession()
      reportDataBaseConfigProvider = new BiTestOracleDataBaseConfigProvider().initConfig*/
    } else {
     /* spark = SparkFactory.createSparkSession(app_name)
      reportDataBaseConfigProvider = new BiProductionOracleDataBaseConfigProvider().initConfig*/
    }

    // 默认日期和小时参数初始化
    if (null == args || args.length == 0) {
      nilDateArgsHandler
      nilHourArgsHandler
    } else {
      if (args.length == 1) {
        date = args(0)
        nilHourArgsHandler
      }
      if (args.length == 2) {
        date = args(0)
        hour = args(1)
      }
      if (date.equals("")) {
        nilDateArgsHandler
      }
      if (hour.equals("")) {
        nilHourArgsHandler
      }
    }
  }

  def nilDateArgsHandler: Unit = {
    //date = DateTimeUtil.lastOffset(DateTimeUtil.defaultDateFormat, DateTimeUtil.dayUnit)
  }

  def nilHourArgsHandler: Unit = {
   // hour = DateTimeUtil.lastOffset("HH", DateTimeUtil.hourUnit)
  }

  /**
    * 基类服务中的exec 方法，子类中的方法都需要执行一遍当前方法
    */
  def exec: Unit = {
    println(s"jobId:${spark.sparkContext.applicationId} date:${date} hour:${hour} url:${spark.sparkContext.uiWebUrl.getOrElse("")}")
  }

  /**
    * 打印当前已记载文件
    *
    * @param df
    */
  def printInputFiles(df: DataFrame): Unit = {
    println(s"数据文件:\r\n${df.inputFiles.mkString("\r\n")}")
  }

  /**
    * 清理资源
    */
  def stop: Unit = {}

}
