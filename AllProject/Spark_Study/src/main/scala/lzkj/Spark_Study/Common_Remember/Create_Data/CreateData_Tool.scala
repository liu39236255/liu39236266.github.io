package lzkj.Spark_Study.Common_Remember.Create_Data

import java.util

import lzkj.Spark_Study.Common_Remember.Create_Data.Bean.People

import scala.collection.mutable.ListBuffer

object CreateData_Tool {


   /* def create_PeopleListData_Java(ifShowData:Boolean=false): util.ArrayList[People] ={
        var PeopleList = new util.ArrayList[People]()
        for(i <- 1 to 20){
          val CurPeoPle=new People(s"People_${i}",i,s"爱好${i}")
          PeopleList.add(CurPeoPle)
        }
      if(ifShowData){
        for( people <- PeopleList){
          println(people)
        }
      }

      PeopleList
    }
*/

  /**
    * 创建ListBuffer[People]() 对象并且返回
    * @param ifShowData
    * @return
    */
  def create_PeopleListData_Scala(ifShowData:Boolean=false): ListBuffer[People] ={
    val PeopleList = ListBuffer[People]()
    for(i <- 1 to 20){
      val CurPeoPle=new People(s"People_${i}",i,s"爱好${i}")
      PeopleList+=CurPeoPle
    }
    if(ifShowData){
      for(people <- PeopleList){
        println(people)
      }
    }
    PeopleList
  }





}
