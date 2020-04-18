package lzkj.Spark_Study.Spark_Core

// 如下导入spark core
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

class Spark_Core_01 {

}

object Spark_Core_01 {

  var sc: SparkContext = null

  def main(args: Array[String]): Unit = {
    // 初始化sparkContext
    Demo0_Init()
    // 1- Demo01_Parallelized_Collections
    //Demo01_Parallelized_Collections()

    // 2- spark 中变量方法的生命周期
    //Demo02_Understanding_closures

    // 3- shuffle keyvalue pairs
    //    Demo03_Key_Value_Pairs

    // 4- transformation 算子操作

    transformation_Ope

    //* Common 将页面中表格中的数据转化成md格式
    //common_ModifyTable("src/main/MyData/TxtData/action.txt")
  }

  /**
    * 官网案例
    */
  def Demo0_Init() = {
    // 创建
    val master = "local[2]"
    val conf = new SparkConf().setAppName("AppName").setMaster(master)
    sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

  }

  /**
    * 并行数据
    */
  def Demo01_Parallelized_Collections() = {
    // 官网案例链接 (http://spark.apache.org/docs/2.2.0/rdd-programming-guide.html#using-the-shell)
    val data = Array(1, 2, 3, 4, 5)
    //可以在第二个的参数中设置分区数，
    val distData = sc.parallelize(data)
    // distData.collect().foreach(println(_))
    val resoult: Int = distData.reduce((a, b) => Math.max(a, b))
    //println("数值中最大数"+resoult)

    /**
      * 官方描述
      * External Datasets 外部数据集
      * Spark可以从Hadoop支持的任何存储源创建分布式数据集，
      * 包括本地文件系统、HDFS、Cassandra、HBase、Amazon S3等。
      * Spark支持文本文件、SequenceFiles和任何其他Hadoop InputFormat。
      * scala> val distFile = sc.textFile("data.txt")
      * distFile: org.apache.spark.rdd.RDD[String] = data.txt MapPartitionsRDD[10] at textFile at <console>:26
      */
    //加载本地数据
    val SourceLine: RDD[String] = sc.textFile("src/main/MyData/TxtData/readme.md")
    SourceLine.collect().foreach(println)

    //此外可以通过map reduce 计算每一行的内容长度，并返回内容最长哪一行的字的个数
    /**
      *
      * distFile.map(s => s.length).reduce((a, b) => a + b).
      */
    SourceLine.map(_.size).reduce((a, b) => if (a > b) a else b)
    //或者SourceLine.map(_.size).reduce((a,b) => Math.max(a,b))
    /**
      * Some notes on reading files with Spark
      * If using a path on the local filesystem, the file must also be accessible at the same path on worker nodes. Either copy the file to all workers or use a network-mounted shared file system.
      * 如果是在本地路径的话，每个机器在相同的路径上都应该有相同的文件，
      *
      * All of Spark’s file-based input methods, including textFile, support running on directories, compressed files, and wildcards as well.
      * For example, you can use textFile("/my/directory"), textFile("/my/directory/星号.txt"), and textFile("/my/directory/星号.gz")
      * spark 读取文件都基于input方法，包括 文本文档。支持正在使用的目录，压缩文件甚至还能支持通配符
      *
      * The textFile method also takes an optional second argument for controlling the number of partitions of
      * the file. By default, Spark creates one partition for each block of the file (blocks being 128MB by default in HDFS),
      * but you can also ask for a higher number of partitions by passing a larger value. Note that you cannot have fewer partitions than blocks.
      * 默认情况下，Spark为文件的每个块创建一个分区(默认情况下是在HDFS中为128MB一个块)，但是您也可以通过传递一个较大的值来请求更多的分区。请注意，不能有比块更少的分区。比如你一个文件分了两个块儿，你不能设置分区小于2
      *
      * 而且除了文本格式spark还支持其他数据格式：
      *
      * SparkContext。wholeTextFiles允许您读取包含多个小文本文件的目录，并将其中的每个文件作为(文件名、内容)对返回。
      * 这与textFile形成对比，textFile将在每个文件中返回一行记录。分区是由数据位置决定的，
      * 在某些情况下，可能导致分区太少。对于这些情况，wholeTextFiles提供了一个可选的第二个参数，用于控制最小数量的分区。
      *
      * 对于 SequenceFiles，可以使用 SparkContext 的  sequenceFile[K, V]  方法创建，K 和 V
      * 分别对应的是 key 和 values 的类型。像 IntWritable 与 Text 一样，它们必须是 Hadoop
      * 的 Writable 接口的子类。另外，对于几种通用的 Writables，Spark 允许你指定原生类型
      * 来替代。例如：  sequenceFile[Int, String]  将会自动读取 IntWritables 和 Text。
      *
      * 对于其他的 Hadoop InputFormats，你可以使用  SparkContext.hadoopRDD  方法，它可以
      * 指定任意的  JobConf  ，输入格式(InputFormat)，key 类型，values 类型。你可以跟设置
      * Hadoop job 一样的方法设置输入源。你还可以在新的 MapReduce 接口
      * (org.apache.hadoop.mapreduce)基础上使用  SparkContext.newAPIHadoopRDD  (译者注：老
      * 的接口是  SparkContext.newHadoopRDD )
      *
      * RDD.saveAsObjectFile and SparkContext.objectFile 这两种方法定义了一种可以存储任何rdd到本地，但是不是一种有效率的格式比如avro
      */

    /**
      * 读取外部数据，由于懒加载机制，在textfile/map  reduce  三个算子中 只有reduce 才真正进行数据的加载计算
      * 如果重复想使用数据 的话可以缓存
      */

    // SourceLine.persist() // 进行缓存数据

    /**
      * 分割*************
      */
    //Passing Functions to Spark 通过传入函数
    /**
      * object MyFunctions {
      * def func1(s: String): String = { ... }
      * }
      *
      *myRdd.map(MyFunctions.func1)
      */

    object MyFunctions {
      def func1(s: String): String = {
        var rs: String = ""
        if (s.contains("com/)")) {
          rs = s
        }
        rs
      }
    }
    // 打印到控制台
    //SourceLine.map(MyFunctions.func1(_)).filter(_!="").collect().foreach(println(_))

    // 存储到本地
    //SourceLine.map(MyFunctions.func1(_)).filter(_!="").saveAsTextFile("F:\\MyTestFold_Spark\\gitTestData\\output\\00")

    /**
      * 分割***************
      */

    // * 也可以定义定义到一个类中
    /*    class MyClass {
          def func1(s: String): String = { s }
          def doStuff(rdd: RDD[String]): RDD[String] = { rdd.map(func1) }
        }*/

    // ---------------------------------------------------------------------------------
    /*  class MyClass  {
        val field = "Hello"
        def doStuff(rdd: RDD[String]): RDD[String] = { rdd.map(x => field + x) }
      }*/
    // 下面这种情况会报错
    /**
      * Exception in thread "main" org.apache.spark.SparkException: Task not serializable
      *
      * 因为任务要传到集群上面，而每一个map中都调用了外部类的一个属性字段，就相当于每次都调用了外部类，但是外部类没有序列化就会有此类错误！
      * 这里参考过的博客：http://www.cnblogs.com/zwCHAN/p/4305156.html
      */
    // new MyClass().doStuff(SourceLine).foreach(println(_))

    /**
      * 解决办法
      *
      */

    class MyClass {
      val field = "前缀"

      def doStuff(rdd: RDD[String]): RDD[String] = {
        val field_ = this.field
        rdd.map(x => field_ + x)
      }
    }
    new MyClass().doStuff(SourceLine).foreach(println(_))

    // -------------------------------------------------------------------------------

  }


  /**
    * spark 难点
    */

  def Demo02_Understanding_closures(): Unit = {

    /**
      * One of the harder things about Spark is understanding the scope and life cycle of variables and methods
      * when executing code across a cluster. RDD operations that modify variables outside of their scope can be a
      * frequent source of confusion. In the example below we’ll look at code that uses foreach() to increment a counter,
      * but similar issues can occur for other operations as well.
      * Spark的难点之一是了解变量和方法的范围和生命周期。
      * 当在集群中执行代码时。在其范围之外修改变量的RDD操作可以是一种比较混乱
      * 在下面的示例中，我们将查看使用foreach()来增加计数器的代码，
      * 但类似的问题也可能出现在其他操作中。
      *
      * Consider the naive RDD element sum below, which may behave differently depending on whether
      * execution is happening within the same JVM. A common example of this is when
      * running Spark in local mode (--master = local[n]) versus deploying a Spark application
      * to a cluster (e.g. via spark-submit to YARN):
      *
      * 在不同的jvm中执行可能会会有不同的表现，一个常见的例子是在本地模式中运行Spark (- master = local[n])，
      * 而不是将Spark应用程序部署到集群(例如，通过Spark -submit to thread):
      */

    /*   val data =Array(1,2,3,4,5)
       var counter = 0
       var rdd = sc.parallelize(data)

       // 不可以这么做
       rdd.foreach(x => counter += x)

       println("Counter value: " + counter)*/

    //
    // 2018-04-19 http://spark.apache.org/docs/2.2.0/rdd-programming-guide.html#using-the-shell  (Local vs. cluster modes)

    // 2018-04-20 Start
    /**
      *
      * 上述代码的行为是未定义的，可能不像预期的那样工作。
      * 为了执行作业，Spark将RDD操作的处理分解为任务，每个任务由执行器执行。
      *
      * 在执行之前，Spark计算任务的闭包。闭包是执行器在RDD上执行其计算时必须可见的变量和方法(在本例中foreach())。
      * 这个闭包被序列化并发送给每个执行器。
      *
      * 关闭发送给每个executor的变量现在是副本，因此，当在foreach函数中引用计数器时，
      * 它不再是驱动节点上的计数器。在驱动节点的内存中仍然有一个计数器，
      * 但是这对执行器来说已经不可见了!执行程序只从序列化的闭包中看到副本。
      * 因此，计数器的最终值仍然为零，因为计数器上的所有操作都引用了序列化闭包中的值。
      *
      * 在本地模式中，在某些情况下，foreach函数实际上会在同一JVM中执行驱动程序，并引用相同的原始计数器，并可能实际更新它。
      * 为了确保在这些场景中定义良好的行为，应该使用累加器。Spark中的累加器专门用于提供一种机制，
      * 用于在集群中跨工作节点的执行时安全地更新一个变量。
      * 本指南的累加器部分更详细地讨论了这些问题。
      */


  }


  /**
    * shuffle
    */

  def Demo03_Key_Value_Pairs() {
    val lines = sc.textFile("src/main/MyData/TxtData/readme2.md")
    val pairs = lines.map(s => (s, 1))
    val counts = pairs.reduceByKey((a, b) => a + b).sortByKey(false)
    counts.collect().foreach(println)
  }

  /**
    * wc transformation 算子操作
    *
    *
    */

  def transformation_Ope() ={
    val input="D:\\MyGitClone-Res\\Git-liu-55\\AllProject\\Spark_Study\\src\\main\\MyData\\TxtData\\action.txt"
    val mapped: RDD[Array[String]] = sc.textFile(input).map(line => {
      line.split(" ")
    })
    val flatmap: RDD[String] = sc.textFile(input).flatMap(line => line.split(" "))
//    flatmap.collect().foreach(println)


  }
  /**
    * 把表格转换成md 的表格
    */
  def common_ModifyTable(input: String = "src/main/MyData/TxtData/action.txt", output: String = "src/main/MyData/TxtData/output") = {
    val data = sc.textFile(input)
      .map(line => {
        val arrays = line.split("\\t")
        s"|${arrays(0)}|${arrays(1)}|注释|"
      }).persist()
    data.repartition(1).saveAsTextFile(output)
  }

}

