package lzkj.Spark_Study.Spark_Sql.sql

// 如下需要导入Spark-sql
import lzkj.Spark_Study.Common_Remember.Use_Tools

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Dataset, Row, SparkSession}

object Spark_Sql_01 {
  def main(args: Array[String]): Unit = {


    /**
      * 一，spark sql 基本操作,包括DataFrame 的创建Dataset的创建
      */
    //    Spark_Sql_01()

    /**
      * 二，rdd 两种数据集转换的方式。
      * 1 反射的方式
      */
        RDD_Interoperating_reflection

    /**
      * 二，rdd 两种数据集转换的方式。
      * 2 编程方式转化
      */
    RDD_Interoperation_Programmatically

  }

  /**
    * dataset dataframe 的创建
    */
  def Spark_Sql_01(): Unit = {
    //    The entry point into all functionality in Spark is the SparkSession class. To create a basic SparkSession, just use SparkSession.builder():
    val spark = SparkSession
      .builder()
      .master("local[4]")
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    // For implicit conversions like converting RDDs to DataFrames

    import spark.implicits._

    // 2
    val df = spark.read.json(s"${Use_Tools.Spark_resource}/people.json")

    /**
      * {"name":"Michael"}
      * {"name":"Andy", "age":30}
      * {"name":"Justin", "age":19}
      */
    // Displays the content of the DataFrame to stdout

    /**
      * df.show()
      */

    // +----+-------+
    // | age|   name|
    // +----+-------+
    // |null|Michael|
    // |  30|   Andy|
    // |  19| Justin|
    // +----+-------+

    /**
      * df.printSchema()
      */
    //打印目录树结构
    //df.printSchema()
    // root
    // |-- age: long (nullable = true)
    // |-- name: string (nullable = true)

    //df.select("name").show()
    // +-------+
    // |   name|
    // +-------+
    // |Michael|
    // |   Andy|
    // | Justin|
    // +-------+


    // Select everybody, but increment the age by 1 年龄都 +1

    /**
      * df.select($"name", $"age" + 1).show()
      */
    // +-------+---------+
    // |   name|(age + 1)|
    // +-------+---------+
    // |Michael|     null|
    // |   Andy|       31|
    // | Justin|       20|
    // +-------+---------+


    //年龄超过21的

    /**
      * df.filter($"age" > 21 ).show()
      */
    //    +---+----+
    //    |age|name|
    //    +---+----+
    //    | 30|Andy|
    //    +---+----+

    // Count people by age
    // 根据age分组
    /**
      * df.groupBy("age").count().show()
      */
    // +----+-----+
    // | age|count|
    // +----+-----+
    // |  19|    1|
    // |null|    1|
    // |  30|    1|
    // +----+-----+


    /** -2- Running SQL Queries Programmatically **/


    /**
      *  df.createOrReplaceTempView("people")
      *
      * val sqlDF = spark.sql("SELECT * FROM people")
      * sqlDF.show()
      */

    // +----+-------+
    // | age|   name|
    // +----+-------+
    // |null|Michael|
    // |  30|   Andy|
    // |  19| Justin|
    // +----+-------+


    /**
      * Register the DataFrame as a global temporary view
      */
    df.createGlobalTempView("people")

    // Global temporary view is tied to a system preserved database `global_temp`
    // spark.sql("SELECT * FROM global_temp.people").show()
    // +----+-------+
    // | age|   name|
    // +----+-------+
    // |null|Michael|
    // |  30|   Andy|
    // |  19| Justin|
    // +----+-------+

    // Global temporary view is cross-session
    // spark.newSession().sql("SELECT * FROM global_temp.people").show()
    // +----+-------+
    // | age|   name|
    // +----+-------+
    // |null|Michael|
    // |  30|   Andy|
    // |  19| Justin|
    // +----+-------+


    /**
      * dataset 的创建，dataset与dataframe区别就不多说了
      */

    //case class Person(name: String, age: Long) ,这里需要在碗面定义
    // Encoders are created for case classes

    val caseClassDS: Dataset[Person] = Seq(Person("张三", 32), Person("小明", 23)).toDS()
    caseClassDS.show()

    /*    val primitiveDS=Seq(1,2,3).toDS()
        primitiveDS.map(_ + 2 ).show()*/

    val primitiveDS = Seq(1, 2, 3).toDF()
    //primitiveDS.printSchema()

    /*    root
        |-- value: integer (nullable = false)*/


    val jsonPerson = spark.read.json(s"${Use_Tools.Spark_resource}/people.json").as[Person]
    //jsonPerson.show()

  }

  /**
    * Interoperating with RDDs  rdd 转化成dataset的两种方式  方式 一：通过反射
    */
  def RDD_Interoperating_reflection(): Unit = {
    /**
      * rdd 转化成dataset的两种方式
      */

    /**
      *
      */
    val spark = SparkSession.builder()
      .master("local")
      .appName("reflection_RDD_DAtaset")
      .getOrCreate()

    // 注意下面的隐式转换，与反射的类// case class Person(name: String, age: Int) ,注意
     import spark.implicits._
    val person_DF = spark.sparkContext.textFile(s"${Use_Tools.Spark_resource}/people.txt")
      .map(_.split(","))
      .map(attribute =>
        Person(attribute(0), attribute(1).trim.toInt)
      ).toDF()

    person_DF.createOrReplaceTempView("people")
    val select_Df = spark.sql("select name ,age  from people where age between 13 and 19")
    select_Df.show()
    select_Df.printSchema()
    //    root
    //    |-- name: string (nullable = true)
    //    |-- age: long (nullable = false)

    select_Df.map(x =>

      s"name:${x(0)},age:${x(1)}"
    ).show(false)
    //    +--------------------+
    //    |value               |
    //    +--------------------+
    //    |name:Justin,age:19  |
    //      |name:Justin_3,age:18|
    //      |name:Justin_4,age:16|
    //      +--------------------+

    /*    select_Df.map(x=>{
          {
            s"name:${x(0)},age:${x(1)}"
          }
        }).show(false)*/

    select_Df.map(x => {
      s"naem:${x.getAs[String]("name")}"
    }).show()

    //+-------------+
    //|        value|
    //+-------------+
    //|  naem:Justin|
    //|naem:Justin_3|
    //|naem:Justin_4|
    //+-------------+


    // No pre-defined encoders for Dataset[Map[K,V]], define explicitly,没有预先定义好的数据集编码器类型
    implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]
    // Primitive types and case classes can be also defined as
    // implicit val stringIntMapEncoder: Encoder[Map[String, Any]] = ExpressionEncoder()

    // row.getValuesMap[T] retrieves multiple columns at once into a Map[String, T]
    // select_Df.map(teenager => teenager.getValuesMap[Any](List("name", "age"))).show(false)

    //+-------------------------------------------------------------------------------------------+
    //|value                                                                                      |
    //+-------------------------------------------------------------------------------------------+
    //|[35 01 02 40 01 03 01 6E 61 6D E5 03 01 4A 75 73 74 69 EE 40 01 03 01 61 67 E5 09 26]      |
    //|[35 01 02 40 01 03 01 6E 61 6D E5 03 01 4A 75 73 74 69 6E 5F B3 40 01 03 01 61 67 E5 09 24]|
    //|[35 01 02 40 01 03 01 6E 61 6D E5 03 01 4A 75 73 74 69 6E 5F B4 40 01 03 01 61 67 E5 09 20]|
    //+-------------------------------------------------------------------------------------------+

    select_Df.map(teenager => teenager.getValuesMap[Any](List("name", "age"))).collect().foreach(println(_))

    //Map(name -> Justin, age -> 19)
    //Map(name -> Justin_3, age -> 18)
    //Map(name -> Justin_4, age -> 16)
    // Array(Map("name" -> "Justin", "age" -> 19))


  }


  /**
    * 第二种，编程方式指定 schema 的数据结构
    */

  def RDD_Interoperation_Programmatically = {

    val spark = SparkSession.builder().master("local").appName("RDD_Interoperation_Programmatically").getOrCreate()
    val peopleRDD = spark.sparkContext.textFile(s"${Use_Tools.Spark_resource}/people.txt")
    import spark.implicits._

    /**
      * 注意这样指定的类型是String类型的
      * 构建StructField需要导入包
      * import org.apache.spark.sql.types.{StringType, StructField, StructType}
      */

    /**
      * 1. 构建StructType
      */
    val schemaString = "name age"
    var fields = schemaString.split(" ").map(x => {
      StructField(x, StringType, nullable = true)
    })
    /**
      * 此外 fields也可以是 不同类型的,如下。Array封装的不同的StructField 都可行
      */

    /*   fields=Array(
         StructField("name",StringType,true),
         StructField("age",IntegerType,true)
       )*/
    // or 写成以下形式

    /*   val fields = StructType(
         StructField("a", StringType) ::
           StructField("b", IntegerType) ::
           StructField("c", IntegerType) ::
           StructField("d", IntegerType) ::
           StructField("e", IntegerType) :: Nil)
           */

    val schema = StructType(fields)

    /**
      * 2.构建RDD（Row()） 对象.
      */
    val rowRdd = peopleRDD.map(_.split(","))
      .map(x => {
        Row(x(0), x(1).trim)
      })

    val peopleDF = spark.createDataFrame(rowRdd, schema)


    peopleDF.createOrReplaceTempView("people")

    val result = spark.sql("select name from people")

    // The results of SQL queries are DataFrames and support all the normal RDD operations（SQL查询的结果是DataFrames并支持所有正常的RDD操作。）
    // The columns of a row in the result can be accessed by field index or by field name（结果中的一行的列可以通过字段索引或字段名访问。）

    // 注意这里需要引入隐士转换 import spark.implicits._
    // //peopleDF.map(attributes => "Name:" + attributes(0)).show()

    /**
      * 展示结果
      */

    // 使用$"列明" 需要注意导入隐式转换  import spark.implicits._

    peopleDF.show(false)
    peopleDF.printSchema()
    result.select($"name").show()
    // 这里即使是string类型的 spark 也会转化成能够比较的对象
    result.filter($"age" > 18 ).show()


  }
}

case class Person(name: String, age: Int)
