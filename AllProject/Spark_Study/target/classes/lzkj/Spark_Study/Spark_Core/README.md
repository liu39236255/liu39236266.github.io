# 本案例中来自[Spark Programming Guide](http://spark.apache.org/docs/2.2.0/rdd-programming-guide.html)


## 涉及的操作

### 设置spark 中的日志级别

---
#### Spark
[三种设置SparkContext日志级别](https://blog.csdn.net/high2011/article/details/52660118)

* spark这里列出代码设置级别
```scala
val sc: SparkContext = new SparkContext(sparkConf)
sc.setLogLevel("WARN")
//sc.setLogLevel("DEBUG")
//sc.setLogLevel("ERROR")
//sc.setLogLevel("INFO")
```

---
#### log4j

 [log4j设置日志级别](https://blog.csdn.net/baidu_34046383/article/details/52795789)

[log4j日志设置参数说明](https://blog.csdn.net/rogger_chen/article/details/50587920)

    Log4j建议只使用四个级别，优先级从高到低分别是 ERROR、WARN、INFO、DEBUG,
    可以是OFF、FATAL、ERROR、WARN、INFO、DEBUG、ALL
如下是上述博客内容
```

 日志记录器(Logger)是日志处理的核心组件。log4j具有5种正常级别(Level)。日志记录器(Logger)的可用级别Level (不包括自定义级别 Level)， 以下内容就是摘自log4j API (http://jakarta.apache.org/log4j/docs/api/index.html):

static Level WARN

WARN level表明会出现潜在错误的情形。

static Level ERROR

ERROR level指出虽然发生错误事件，但仍然不影响系统的继续运行。

static Level FATAL

FATAL level指出每个严重的错误事件将会导致应用程序的退出。

另外，还有两个可用的特别的日志记录级别: (以下描述来自log4j APIhttp://jakarta.apache.org/log4j/docs/api/index.html):

static Level ALL

ALL Level是最低等级的，用于打开所有日志记录。

static Level OFF

OFF Level是最高等级的，用于关闭所有日志记录。

日志记录器（Logger）的行为是分等级的。如下表所示：

分为OFF、FATAL、ERROR、WARN、INFO、DEBUG、ALL或者您定义的级别。Log4j建议只使用四个级别，优先级从高到低分别是 ERROR、WARN、INFO、DEBUG。通过在这里定义的级别，您可以控制到应用程序中相应级别的日志信息的开关。比如在这里定义了INFO级别，则应用程序中所有DEBUG级别的日志信息将不被打印出来，也是说大于等于的级别的日志才输出。

日志记录的级别有继承性，子类会记录父类的所有的日志级别。

 logger日志设置：

1、加包：log4j-1.2.16.jar  一般还会加入 commons-logging-1.1.1.jar

2、在CLASSPATH 下建立log4j.properties

logger日志设置

可以再resource下面创建 log4j.properties


 在要输出的日志的类中

定义：private static final org.apache.log4j.Logger logger = Logger.getLogger(类名.class);

在类输位置：logger.info(XXX);


logger 配置说明：

1、 log4j.rootLogger=INFO, stdout , R

此句为将等级为INFO的日志信息输出到stdout和R这两个目的地，stdout和R的定义在下面的代码，可以任意起名。等级可分为OFF、FATAL、ERROR、WARN、INFO、DEBUG、ALL，如果配置OFF则不打出任何信息，如果配置为INFO这样只显示INFO, WARN, ERROR的log信息，而DEBUG信息不会被显示，具体讲解可参照第三部分定义配置文件中的logger。

2、log4j.appender.stdout=org.apache.log4j.ConsoleAppender

此句为定义名为stdout的输出端是哪种类型，可以是

org.apache.log4j.ConsoleAppender（控制台），

org.apache.log4j.FileAppender（文件），

org.apache.log4j.DailyRollingFileAppender（每天产生一个日志文件），

org.apache.log4j.RollingFileAppender（文件大小到达指定尺寸的时候产生一个新的文件）

org.apache.log4j.WriterAppender（将日志信息以流格式发送到任意指定的地方）

3、log4j.appender.stdout.layout=org.apache.log4j.PatternLayout

此句为定义名为stdout的输出端的layout是哪种类型，可以是

org.apache.log4j.HTMLLayout（以HTML表格形式布局），

org.apache.log4j.PatternLayout（可以灵活地指定布局模式），

org.apache.log4j.SimpleLayout（包含日志信息的级别和信息字符串），

org.apache.log4j.TTCCLayout（包含日志产生的时间、线程、类别等等信息）

4、log4j.appender.stdout.layout.ConversionPattern= [QC] %p [%t] %C.%M(%L) | %m%n

如果使用pattern布局就要指定的打印信息的具体格式ConversionPattern，打印参数如下：

%m 输出代码中指定的消息

%p 输出优先级，即DEBUG，INFO，WARN，ERROR，FATAL

%r 输出自应用启动到输出该log信息耗费的毫秒数

%c 输出所属的类目，通常就是所在类的全名

%t 输出产生该日志事件的线程名

%n 输出一个回车换行符，Windows平台为“rn”，Unix平台为“n”

%d 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyyy MMM dd HH:mm:ss,SSS}，输出类似：2002年10月18日 22：10：28，921

%l 输出日志事件的发生位置，包括类目名、发生的线程，以及在代码中的行数。

[QC]是log信息的开头，可以为任意字符，一般为项目简称。

输出的信息

[TS] DEBUG [main] AbstractBeanFactory.getBean(189) | Returning cached instance of singleton bean 'MyAutoProxy'


5、 log4j.appender.R=org.apache.log4j.DailyRollingFileAppender

此句与第3行一样。定义名为R的输出端的类型为每天产生一个日志文件。

6、log4j.appender.R.File=D:\\Tomcat 5.5\\logs\\qc.log

此句为定义名为R的输出端的文件名为D:\\Tomcat 5.5\\logs\\qc.log可以自行修改。

7、 log4j.appender.R.layout=org.apache.log4j.PatternLayout

与第4行相同。

8、 log4j.appender.R.layout.ConversionPattern=%d-[TS] %p %t %c - %m%n

与第5行相同。

9、 log4j.logger.com. neusoft =DEBUG

指定com.neusoft包下的所有类的等级为DEBUG。

可以把com.neusoft改为自己项目所用的包名。

10、  log4j.logger.com.opensymphony.oscache=ERROR

11、 log4j.logger.net.sf.navigator=ERROR

这两句是把这两个包下出现的错误的等级设为ERROR，如果项目中没有配置EHCache，则不需要这两句。

12、log4j.logger.org.apache.commons=ERROR

13、 log4j.logger.org.apache.struts=WARN

这两句是struts的包。

14、  log4j.logger.org.displaytag=ERROR

这句是displaytag的包。（QC问题列表页面所用）

15、 log4j.logger.org.springframework=DEBUG

此句为Spring的包。

16、 log4j.logger.org.hibernate.ps.PreparedStatementCache=WARN

17、log4j.logger.org.hibernate=DEBUG

此两句是hibernate的包。

以上这些包的设置可根据项目的实际情况而自行定制。
```    

---