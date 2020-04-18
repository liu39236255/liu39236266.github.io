package lzkj.Spark_Study.Spark_Sql.Data_Source

import java.io.{File, PrintWriter}

import lzkj.Spark_Study.Common_Remember.Create_Data.Bean.People
import lzkj.Spark_Study.Common_Remember.Use_Tools
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql._

object Data_Source_Demo {
  var spark: SparkSession = null

  def init(): Unit = {
    spark = SparkSession.builder().master("local").appName("Data_Source_Demo").getOrCreate()


  }




  def main(args: Array[String]): Unit = {
    init()
    // save_Load_General(spark)

    //read_Write_Parquet(spark)

    // 分区的指定
    schemaMerge(spark)

    // json dataset

    jsonDataSet(spark)

  }


  /**
    * parquet 的读写
    *
    * @param spark
    */

  def read_Write_Parquet(spark: SparkSession): Unit = {

    import spark.implicits._
    val peopleDF = spark.read.json(s"${Use_Tools.Spark_resource}/people.json")
    peopleDF.write.parquet(s"${Use_Tools.Spark_resource}/people.parquet")

    val readParquetDF: DataFrame = spark.read.parquet(s"${Use_Tools.Spark_resource}/people.parquet")

    readParquetDF.createOrReplaceTempView("parqueFile")

    val nameDS: DataFrame = spark.sql("select name from parqueFile where age between 13 and 19")

    // 直接使用查询出来的DF 调用map等参数的时候需要引入隐式转换  //import spark.implicits._
    nameDS.map(attribute => {
      s"name:${attribute(0).toString.trim}"
    }).show()


    // 如果rdd.map 转化成df 隐式转换需注意：
    nameDS.rdd.map(attribute => {
      s"name:${attribute(0).toString.trim}"
    }).toDF("名字")

  }

  /**
    * 读写parquet 文件之前的 Datasource
    *
    * @param spark
    */
  def save_Load_General(spark: SparkSession): Unit = {

    val peopleDF = spark.read.format("json").load(s"${Use_Tools.Spark_resource}/people.json")
    peopleDF.printSchema()
    peopleDF.select("name", "age").write.save(s"${Use_Tools.Spark_resource}/namesAndFavColors.quet")

    val sqlDF1 = spark.sql(s"SELECT * FROM parquet.`${Use_Tools.Spark_resource}/users.parquet`")


    peopleDF.write.bucketBy(42, "name").sortBy("age").saveAsTable("people_buckted")

    sqlDF1.write.partitionBy("favorite_color").format("parquet").save(s"${Use_Tools.Spark_resource}/namesPartByColor.parquet")


    peopleDF.write
      .partitionBy("favorite_clor")
      .bucketBy(42, "name")
      .saveAsTable(s"${Use_Tools.Spark_resource}/people_partitioned_bucketed")

    //让程序结束
    System.exit(0)


    val userDF: DataFrame = spark.read.load(s"${Use_Tools.Spark_resource}/users.parquet")
    userDF.printSchema()
    //    root
    //    |-- name: string (nullable = true)
    //    |-- favorite_color: string (nullable = true)
    //    |-- favorite_numbers: array (nullable = true)
    //    |    |-- element: integer (containsNull = true)

    userDF.show(false)


    var selectExprDF: DataFrame = userDF.selectExpr("name as Name", "favorite_color as Color")

    selectExprDF.printSchema()
    selectExprDF.show(false)


    /**
      * 使用sql的方式加载 parquent文件
      */

    val sqlDF = spark.sql(s"SELECT * FROM parquet.`${Use_Tools.Spark_resource}/users.parquet`")

    sqlDF.printSchema()
    sqlDF.show(false)

    /**
      * 以下是补充 与练习
      */
    println(s"下面是补充扩展练习")
    var count = 0
    val Pro_RDD: RDD[Row] = selectExprDF.rdd.map(x => {
      val count_curr = count + 1
      Row("Name:" + x.getAs[String](0), "Color:" + x.getAs("Color"), count_curr)
    })

    //val schema=StructType((StructField("Name",StringType,true)::StructField("Color",StringType,true)::StructField("Count_Index",IntegerType,true)::Nil))
    val schema = StructType(Array(
      StructField("Name", StringType, true),
      StructField("Color", StringType, true),
      StructField("Count_Index", IntegerType, true)
    ))
    val Pro_DF = spark.createDataFrame(Pro_RDD, schema)
    println(s" Pro_DF.printSchema()")

    // // Pro_DF.printSchema()

    //    root
    //    |-- Name: string (nullable = true)
    //    |-- Color: string (nullable = true)
    //    |-- Count_Index: integer (nullable = true)
    // // Pro_DF.show(false)
    //    +-----------+----------+-----------+
    //    |Name       |Color     |Count_Index|
    //    +-----------+----------+-----------+
    //    |Name:Alyssa|Color:null|0          |
    //    |Name:Ben   |Color:red |1          |
    //    +-----------+----------+-----------+

    // // Pro_DF.write.save(s"${Use_Tools.Spark_resource}/output/spark_parquent")

    // ---------------------------------------------------

    // 下面是隐士转化进行统一一列的
    var Color_index = 1

    import spark.implicits._ // 这里如果不引入隐士转化的话，那么需要进行.rdd.map()进行操作
    val Text_DF = selectExprDF.map(x => {
      val cur_Color_index = Color_index + 1
      s"${x.getAs[String]("Name")},${x.getAs[String]("Color")},${cur_Color_index}"
    }).cache()

    println("Text_DF.show() ↓")
    Text_DF.printSchema()
    Text_DF.unpersist()
    //    root
    //    |-- value: string (nullable = true)

    Text_DF.show(false)
    //    +-------------+
    //    |value        |
    //    +-------------+
    //    |Alyssa,null,2|
    //    |Ben,red,2    |
    //    +-------------+


    // // Text_DF.write.save(s"${Use_Tools.Spark_resource}/output/spark_parquent_text")

    // ------------------ 创建Row 类型数据 这种操作如果DataFrame .rdd 才能重新构建，这里的.rdd 不能省略！-------------------


    val Text_DF_2 = selectExprDF.rdd.map(x => {
      val cur_Color_index = Color_index + 1
      Row(x.getAs[String]("Name"), x.getAs[String]("Color"), cur_Color_index)
    }).cache()

    // 一下操作与第一步基本相同
    //val schema=StructType((StructField("Name",StringType,true)::StructField("Color",StringType,true)::StructField("Count_Index",IntegerType,true)::Nil))
    val schema_2 = StructType(Array(
      StructField("Name", StringType, true),
      StructField("Color", StringType, true),
      StructField("Count_Index", IntegerType, true)
    ))
    val Pro_DF_2 = spark.createDataFrame(Pro_RDD, schema_2)
    println(s" Pro_DF.printSchema()")
    Pro_DF_2.printSchema()

    //    root
    //    |-- Name: string (nullable = true)
    //    |-- Color: string (nullable = true)
    //    |-- Count_Index: integer (nullable = true)


  }

  /**
    * 分区的案例
    * @param spark
    */
  def schemaMerge(spark: SparkSession): Unit = {
    import spark.implicits._

/*    val squaresDF: DataFrame = spark.sparkContext.makeRDD(1 to 5).map(i => {
      (i, i * i)
    }).toDF("value", "square")
    squaresDF.write.save(s"${Use_Tools.Spark_resource}/mergerSchema/data/data-Test/key=1")

    val cubesDF: DataFrame = spark.sparkContext.makeRDD(6 to 10).map(i => {
      (i, i * i * i)
    }).toDF("value", "cube")
    cubesDF.write.save(s"${Use_Tools.Spark_resource}/mergerSchema/data/data-Test/key=2")*/

    val mergeDF: DataFrame = spark.read.option("mergeSchema","true").parquet(s"${Use_Tools.Spark_resource}/mergerSchema/data/data-Test")

    mergeDF.printSchema()

    //    root
    //    |-- value: integer (nullable = true)
    //    |-- square: integer (nullable = true)
    //    |-- cube: integer (nullable = true)
    //    |-- key: integer (nullable = true)

    mergeDF.show(false)
    //    +-----+------+----+---+
    //    |value|square|cube|key|
    //    +-----+------+----+---+
    //    |1    |1     |null|1  |
    //    |2    |4     |null|1  |
    //    |3    |9     |null|1  |
    //    |4    |16    |null|1  |
    //    |5    |25    |null|1  |
    //    |6    |null  |216 |2  |
    //    |7    |null  |343 |2  |
    //    |8    |null  |512 |2  |
    //    |9    |null  |729 |2  |
    //    |10   |null  |1000|2  |
    //    +-----+------+----+---+

    // 输出包含两个数据的文件路径
    println(mergeDF.inputFiles.mkString)

    //刷新表数据

    spark.catalog.refreshTable("my_table")

  }

  /**
    * json dataset
    */
  def jsonDataSet(spark:SparkSession): Unit = {

    import spark.implicits._
    val path=s"${Use_Tools.Spark_resource}/people.json"
    val peopleDF: DataFrame = spark.read.json(path)

    peopleDF.printSchema()
    peopleDF.show(false)

    peopleDF.createOrReplaceTempView("people")

    val teenagernameDF: DataFrame = spark.sql("select name from people where age between 13 and 19 ")

    teenagernameDF.show(false)

    // Alternatively, a DataFrame can be created for a JSON dataset represented by
    // a Dataset[String] storing one JSON object per string,在这里需要隐士转换 //import spark.implicits._
    val otherPeopleDataset: Dataset[String]= spark.createDataset(
      """{"name":"Yin","address":{"city":"Columbus","state":"Ohio"}}""" :: Nil)

    val otherPeople = spark.read.json(otherPeopleDataset)
    otherPeople.show()

  }
}
