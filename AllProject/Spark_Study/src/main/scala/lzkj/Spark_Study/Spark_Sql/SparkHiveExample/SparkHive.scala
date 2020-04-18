package lzkj.Spark_Study.Spark_Sql.SparkHiveExample

import java.io.File

import lzkj.Spark_Study.Common_Remember.Use_Tools
import org.apache.spark.sql.{Row, SparkSession}


object SparkHive {

  case class Record(key: Int, value: String)

  var spark: SparkSession = null

  def init(): Unit = {

    val warehouseLocation = new File(s"${Use_Tools.Spark_resource}/warehouse").getAbsolutePath

    /**
      * <!--spark 操作hive 需要导入 包，否则spark对象创建会失败 -->
      * <dependency>
      * <groupId>org.apache.spark</groupId>
      * <artifactId>spark-hive_2.11</artifactId>
      * <version>${apache.spark.version}</version>
      * </dependency>
      */
    spark = SparkSession
      .builder()
      .appName("Spark Hive Example")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .master("local")
      .getOrCreate()

  }

  def main(args: Array[String]): Unit = {
    init

    // sparkHiveTest1

    sparkHiveTest1(spark)
  }


  def sparkHiveTest1(spark: SparkSession): Unit = {
    import spark.implicits._
    import spark.sql

    // 注意语句中的数据类型的大小写,且如果存在 会当做一个错误报出

    sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) USING hive")

    sql(s"load data local inpath '${Use_Tools.Spark_resource}/kv1.txt' into table src ")

    // sql("select * from src ").show(false )

    sql("select count(*) from src ").show()

    val sqlDF = sql("SELECT key, value FROM src WHERE key < 10 ORDER BY key")

    val stringsDS = sqlDF.map {
      case Row(key: Int, value: String) => s"Key: $key, Value: $value"
    }

    stringsDS.show(false)

    val recordsDF = spark.createDataFrame((1 to 100).map(i => Record(i, s"val_$i"))) //import spark.implicits._ 使用

    recordsDF.createOrReplaceTempView("records")

    sql("SELECT * FROM records r JOIN src s ON r.key = s.key").show()


  }
}
