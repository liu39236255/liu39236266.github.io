package lzkj.Spark_Study.Common_Remember.simplecore_test

import java.util

import scala.collection.JavaConversions._

/**
  * 统计任务加载插件
  */
object PluginManage {

  def load(): util.List[BaseService] = loadSimpleCore

  private def loadSimpleCore(): util.List[BaseService] = {

    val conf = SimpleCoreConfiguration.getConf()

    val confPlugin = conf
      .getStrings("lzkj.simple.etl.plugin")
      .toSeq
      .map(p => p.trim)
      .filter(p => p.trim.startsWith("#") == false)

    loadPluginInstance(confPlugin)
  }

  private def loadPluginInstance(pluginClass: util.List[String]): util.List[BaseService] = {
    val pluginInstanctList = new util.ArrayList[BaseService]()
    for (pluginClassPath: String <- pluginClass) {
      try {
        if (pluginClassPath.equals("") == false) {
          val className = Class.forName(pluginClassPath)
          val classInstance = className.newInstance().asInstanceOf[BaseService]
          pluginInstanctList.add(classInstance)
        }
      } catch {
        case e: Throwable => //ThrowablePrint.printStackTrace(e)
        {
         e.printStackTrace()
        }
      }
    }
    pluginInstanctList
  }

  def getProperties(key:String): String ={
    val conf = SimpleCoreConfiguration.getConf()
    conf.getStrings(key).mkString

  }


}
