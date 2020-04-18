package lzkj.Spark_Study.Common_Remember.simplecore_test

import org.apache.hadoop.conf.Configuration

/***
* 配置系统
*
* @param loadDefaults
*/
class SimpleCoreConfiguration(loadDefaults: Boolean = false)
  extends Configuration(loadDefaults)
    with Serializable {

  /**
    * 配置系统默认参数初始化
    */
  def this() {
    this(false)
    this.addResource("simple-core.xml")
  }

}


/**
  * 获取配置
  */
object SimpleCoreConfiguration {

  private var conf: SimpleCoreConfiguration = null

  def getConf(): SimpleCoreConfiguration = {
    synchronized {
      if (null == conf) {
        conf = new SimpleCoreConfiguration()
      }
    }
    conf
  }
}
