package Spark_Test

import java.util

import lzkj.Spark_Study.Common_Remember.Create_Data.Bean.People
import lzkj.Spark_Study.Common_Remember.Create_Data.CreateData_Tool
import lzkj.Spark_Study.Common_Remember.Use_Tools
import lzkj.Spark_Study.Common_Remember.other.Read_Excel.ReadExcel_Tool
import org.junit.Test

@Test
class CreateDataTest_01 {

  @Test
  def createDataPeopleList(): Unit = {


  }

  /**
    * 1.-创建，并初始化，注意csv 格式的才能发够使用逗号分开
    */
  def Create_PersonCsv(): Unit = {
    val InputPath = s"${Use_Tools.Spark_resource}/people.txt"
    val OutputPath = s"${Use_Tools.Spark_resource}/MyTest/person.csv"
    //val SLoadExcel: ReadExcel_Tool =new ReadExcel_Tool(s"${Use_Tools.Spark_resource}/person.xlsx")
    val SLoadExcel: ReadExcel_Tool = new ReadExcel_Tool()
    // CreateData_Tool.create_PeopleListData_Scala(true)
    SLoadExcel.scalaLoadExcel(InputPath, OutputPath, "utf-8")
  }

  @Test
  def Test_PersonCsv(): Unit = {
    val InputPath = s"${Use_Tools.Spark_resource}/MyTest/person.csv"
    val OutputPath = s"${Use_Tools.Spark_resource}/MyTest/person_2.csv"
    val SLoadExcel: ReadExcel_Tool = new ReadExcel_Tool()
    SLoadExcel.scalaLoadExcel(InputPath, OutputPath, "utf-8")

  }


}
