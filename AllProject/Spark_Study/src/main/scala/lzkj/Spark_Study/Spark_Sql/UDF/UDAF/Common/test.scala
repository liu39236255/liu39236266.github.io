package lzkj.Spark_Study.Spark_Sql.UDF.UDAF.Common


case class Employee( name:String, salary:Long)
case class Average(var sum:Long,var count:Long)
