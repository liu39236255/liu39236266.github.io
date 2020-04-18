package lzkj.Spark_Study.Spark_Sql.UDF.UDAF

import lzkj.Spark_Study.Common_Remember.Use_Tools
import org.apache.spark.sql.SparkSession

object MyAverage_Demo {
  def main(args: Array[String]): Unit = {
    val spark= SparkSession.builder().master("local[4]").appName("MyAverage_Demo").getOrCreate()
    //spark.udf.register("Average",MyAverage)
    spark.udf.register("MyAverage_Function",MyAverage)

    val employeeDF=spark.read.json(s"${Use_Tools.Spark_resource}/employees.json")

    employeeDF.createOrReplaceTempView("employees")
    employeeDF.show(false)

//      +-------+------+
//      |name   |salary|
//      +-------+------+
//      |Michael|3000  |
//      |Andy   |4500  |
//      |Justin |3500  |
//      |Berta  |4000  |
//      +-------+------+

    // 求出来平均成绩
    val averageDF=spark.sql("select MyAverage_Function(salary) as Average_Salary from employees ")
    averageDF.show(false)
//    +--------------+
//    |Average_Salary|
//    +--------------+
//    |3750.0        |
//    +--------------+
  }
}
