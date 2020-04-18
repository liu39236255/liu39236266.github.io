package lzkj.Spark_Study.Common_Remember.Create_Data.Bean

class People {

  var Name:String=null
  var Age:Int=0
  var hobby:String =null

  /**
    * 带参构造方法
    * @param name
    * @param age
    * @param hobby
    */
  def this (name:String,age:Int,hobby:String){
    this()
    this.Name=name
    this.Age=age
    this.hobby=hobby
  }


  override def toString = s"People(Name=$Name, Age=$Age, hobby=$hobby)"
}
