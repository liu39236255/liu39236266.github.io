package lzkj.Spark_Study.Spark_Sql.UDF.UDAF

import lzkj.Spark_Study.Common_Remember.Use_Tools
import lzkj.Spark_Study.Spark_Sql.UDF.UDAF.Common.Employee
import org.apache.spark.sql.{Dataset, SparkSession, TypedColumn}


object MyAverage_2_Demo {
  def main(args: Array[String]): Unit = {
    val spark=SparkSession.builder().master("local").appName("MyAverage_2_Demo").getOrCreate()
    var employee_DS= spark.read.json(s"${Use_Tools.Spark_resource}/employees.json")
    import spark.implicits._
    var employee_DF= employee_DS.as[Employee]
    employee_DS.show()
    val averageSalary: TypedColumn[Employee, Double] = MyAverage_2.toColumn.name("average_salary")
    val result: Dataset[Double] = employee_DF.select(averageSalary)
    result.show(false)
  }

}
