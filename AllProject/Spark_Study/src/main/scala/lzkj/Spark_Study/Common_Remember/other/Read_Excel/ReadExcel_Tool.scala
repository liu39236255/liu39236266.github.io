package lzkj.Spark_Study.Common_Remember.other.Read_Excel

import java.io._

import lzkj.Spark_Study.Common_Remember.Create_Data.Bean.People
import lzkj.Spark_Study.Common_Remember.Create_Data.CreateData_Tool

import scala.collection.mutable.ListBuffer
import scala.io.{BufferedSource, Source}

class ReadExcel_Tool {
  var CurEncoding=""
  val Encoding_Utf_8="utf-8"
  val Encoding_Operator="GB2312"
  var CurInputPath=""
  var CurOutputPath=""
  var CurrentPrintWriter:PrintWriter=null

  def this (inputPath:String=null){
    this()
    this.CurInputPath=inputPath
  }
  /**
    * 初始化数据
    * @return
    */
  def initDataToExcel(InputPath:String=CurInputPath): ListBuffer[People] ={
    CreateData_Tool.create_PeopleListData_Scala(true)
  }

  /**
    * 加载指定路径的excel文件
    */
  def scalaLoadExcel( InputPath:String=CurInputPath,OutputPath:String=CurOutputPath,CurEncoding:String=CurEncoding): Unit = {
    var PeoList = List[People]()
    val InputBufferSource: BufferedSource = Source.fromFile(InputPath,CurEncoding)
    val listExcel: List[String] = InputBufferSource.getLines().toList

    if(listExcel.size!=0){
      println(s"${InputPath}  不为空！")
      listExcel.foreach(println)
    }else{
      println(s"${InputPath}  为空！")
      PeoList=CreateData_Tool.create_PeopleListData_Scala().toList
      println(s"创建数据大小"+PeoList.size)
    }
    val fileXls=new File(s"${OutputPath}")
    if(fileXls.isFile&&(!fileXls.exists())){
      println("不存在创建")
      fileXls.createNewFile()
    }
    CurrentPrintWriter=new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileXls),"utf-8"))


    PeoList.map(x=>{
      val result =s"${x.Name},${x.Age},${x.hobby}"
      CurrentPrintWriter.println(result)
    })
    CurrentPrintWriter.flush()
    CurrentPrintWriter.close()
  }
}
